package com.example.jerry.restaurant.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.jerry.restaurant.mapper.DiningTableMapper;
import com.example.jerry.restaurant.pojo.CallingNumber;
import com.example.jerry.restaurant.pojo.DiningTable;
import com.example.jerry.restaurant.service.CallingNumberService;

@Service
public class CallingNumberServelmpl implements CallingNumberService{
    @Autowired
    private DiningTableMapper diningTableMapper;
    
    // 三个不同人数的等待队列
    private final Queue<Integer> queue2 = new ConcurrentLinkedQueue<>(); // 2人桌等待队列
    private final Queue<Integer> queue4 = new ConcurrentLinkedQueue<>(); // 4人桌等待队列
    private final Queue<Integer> queue8 = new ConcurrentLinkedQueue<>(); // 8人桌等待队列
    
    // 叫号队列
    private final Queue<CallingNumber> callingQueue = new ConcurrentLinkedQueue<>();
    
    // 叫号ID计数器
    private int callingIdCounter = 0;
    
    @Override
    public List<CallingNumber> getResultList() {
        // 查询前先清理已完成的叫号
        checkAndCleanCompletedCallings();
        return callingQueue.stream().collect(Collectors.toList());
    }
    
    @Override
    public CallingNumber getResult(int peopleNumber) {
        if (peopleNumber <= 0) {
            return null;
        }
        
        LocalDateTime now = LocalDateTime.now();
        
        // 判断是否在营业时间
        if (!isBusinessHours(now)) {
            return null;
        }
        
        // 检查并清理已完成的叫号（餐桌已空）
        checkAndCleanCompletedCallings();
        
        // 根据人数确定应该使用哪个队列
        Queue<Integer> targetQueue = getTargetQueue(peopleNumber);
        if (targetQueue == null) {
            return null; // 不支持的人数
        }
        
        // 将顾客添加到对应队列
        targetQueue.add(peopleNumber);
        callingIdCounter++;
        
        // 尝试分配餐桌
        return tryAllocateTable(peopleNumber, now);
    }
    
    /**
     * 检查并清理已完成的叫号（餐桌已空）
     */
    private void checkAndCleanCompletedCallings() {
        Iterator<CallingNumber> iterator = callingQueue.iterator();
        while (iterator.hasNext()) {
            CallingNumber calling = iterator.next();
            // 只检查有餐桌ID的叫号（已分配餐桌的）
            if (calling.getTableId() > 0) {
                // 检查餐桌状态
                DiningTable table = diningTableMapper.getTableById(calling.getTableId());
                if (table != null && "空".equals(table.getTableStatus())) {
                    // 餐桌已空，从叫号队列中移除
                    iterator.remove();
                }
            }
        }
        
        // 更新所有叫号的等待信息
        updateAllCallingNumbers();
    }
    
    /**
     * 更新所有叫号信息
     */
    private void updateAllCallingNumbers() {
        for (CallingNumber calling : callingQueue) {
            // 重新计算等待人数
            int waitingPeople = calculateWaitingPeopleByCapacity(getTableCapacity(calling.getPeopleNumber()));
            
            // 重新获取剩余餐桌数量
            int remainingTables = getRemainingTables(getTableCapacity(calling.getPeopleNumber()));
            
            // 更新叫号信息
            calling.setDiningTableNumber(remainingTables);
            calling.setPeopleNumber(waitingPeople);
            calling.setTime(LocalDateTime.now()); // 更新时间
        }
    }
    
    /**
     * 根据餐桌容量计算等待人数
     */
    private int calculateWaitingPeopleByCapacity(int capacity) {
        Queue<Integer> targetQueue = getQueueByCapacity(capacity);
        if (targetQueue == null) {
            return 0;
        }
        return targetQueue.stream().mapToInt(Integer::intValue).sum();
    }
    
    /**
     * 根据容量获取对应的队列
     */
    private Queue<Integer> getQueueByCapacity(int capacity) {
        switch (capacity) {
            case 2:
                return queue2;
            case 4:
                return queue4;
            case 8:
                return queue8;
            default:
                return null;
        }
    }
    
    /**
     * 判断是否在营业时间
     */
    private boolean isBusinessHours(LocalDateTime time) {
        int hour = time.getHour();
        return (hour >= 11 && hour <= 14) || (hour >= 18 && hour <= 23);
    }
    
    /**
     * 根据人数获取对应的队列
     */
    private Queue<Integer> getTargetQueue(int peopleNumber) {
        if (peopleNumber >= 1 && peopleNumber <= 2) {
            return queue2;
        } else if (peopleNumber >= 3 && peopleNumber <= 4) {
            return queue4;
        } else if (peopleNumber >= 5 && peopleNumber <= 8) {
            return queue8;
        }
        return null;
    }
    
    /**
     * 尝试分配餐桌
     */
    private CallingNumber tryAllocateTable(int peopleNumber, LocalDateTime now) {
        int capacity = getTableCapacity(peopleNumber);
        int availableTableId = getAvailableTableId(capacity);
        
        if (availableTableId > 0) {
            // 分配餐桌成功
            int callingId = callingIdCounter;
            int remainingTables = getRemainingTables(capacity);
            int waitingPeople = calculateWaitingPeople(peopleNumber);
            
            // 创建叫号对象
            CallingNumber callingNumber = new CallingNumber(
                callingId, 
                remainingTables, 
                availableTableId, 
                now, 
                waitingPeople
            );
            
            // 添加到叫号队列
            callingQueue.add(callingNumber);
            
            // 从等待队列中移除该顾客
            removeFromQueue(peopleNumber);
            
            // 更新餐桌状态为已占用
            diningTableMapper.updateStatus(availableTableId, "占用");
            
            return callingNumber;
        } else {
            // 没有可用餐桌，返回等待信息
            int callingId = callingIdCounter;
            int remainingTables = getRemainingTables(capacity);
            int waitingPeople = calculateWaitingPeople(peopleNumber);
            
            CallingNumber callingNumber = new CallingNumber(
                callingId, 
                remainingTables, 
                -1, // 没有分配到餐桌
                now, 
                waitingPeople
            );
            
            callingQueue.add(callingNumber);
            return callingNumber;
        }
    }
    
    /**
     * 根据人数确定餐桌容量
     */
    private int getTableCapacity(int peopleNumber) {
        if (peopleNumber >= 1 && peopleNumber <= 2) {
            return 2;
        } else if (peopleNumber >= 3 && peopleNumber <= 4) {
            return 4;
        } else if (peopleNumber >= 5 && peopleNumber <= 8) {
            return 8;
        }
        return 0;
    }
    
    /**
     * 获取可用的餐桌ID
     */
    private int getAvailableTableId(int capacity) {
        Integer tableId = diningTableMapper.getAvailableTableId(capacity);
        return tableId != null ? tableId : 0;
    }
    
    /**
     * 获取剩余餐桌数量
     */
    private int getRemainingTables(int capacity) {
        switch (capacity) {
            case 2:
                return diningTableMapper.getNumber2();
            case 4:
                return diningTableMapper.getNumber4();
            case 8:
                return diningTableMapper.getNumber8();
            default:
                return 0;
        }
    }
    
    /**
     * 计算等待人数
     */
    private int calculateWaitingPeople(int currentPeopleNumber) {
        int capacity = getTableCapacity(currentPeopleNumber);
        Queue<Integer> targetQueue = getTargetQueue(currentPeopleNumber);
        
        if (targetQueue == null) {
            return 0;
        }
        
        // 计算该队列中所有等待的人数
        return targetQueue.stream().mapToInt(Integer::intValue).sum();
    }
    
    /**
     * 从队列中移除顾客
     */
    private void removeFromQueue(int peopleNumber) {
        Queue<Integer> targetQueue = getTargetQueue(peopleNumber);
        if (targetQueue != null) {
            targetQueue.remove(peopleNumber);
        }
    }
}
