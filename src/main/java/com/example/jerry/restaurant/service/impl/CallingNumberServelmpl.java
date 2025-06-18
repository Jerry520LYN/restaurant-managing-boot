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
    private final Queue<String> queue2 = new ConcurrentLinkedQueue<>(); // 2人桌等待队列（手机号）
    private final Queue<String> queue4 = new ConcurrentLinkedQueue<>(); // 4人桌等待队列（手机号）
    private final Queue<String> queue8 = new ConcurrentLinkedQueue<>(); // 8人桌等待队列（手机号）
    
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
    public CallingNumber getResult(String phone, int peopleNumber) {
        if (peopleNumber <= 0 || phone == null || phone.isEmpty()) {
            return null;
        }
        LocalDateTime now = LocalDateTime.now();
        if (!isBusinessHours(now)) {
            return null;
        }
        checkAndCleanCompletedCallings();
        Queue<String> targetQueue = getTargetQueue(peopleNumber);
        if (targetQueue == null) {
            return null;
        }
        // 如果手机号已在队列，直接返回当前叫号信息
        if (targetQueue.contains(phone)) {
            int position = getUserPosition(targetQueue, phone);
            int diningTableNumber = getRemainingTables(getTableCapacity(peopleNumber));
            return findCallingNumberByPhone(phone, peopleNumber, diningTableNumber, position);
        } else {
            // 新叫号
            targetQueue.add(phone);
            callingIdCounter++;
            int position = targetQueue.size() - 1;
            int diningTableNumber = getRemainingTables(getTableCapacity(peopleNumber));
            CallingNumber callingNumber = new CallingNumber(
                callingIdCounter,
                diningTableNumber,
                -1, // 不分配餐桌
                now,
                position,
                phone
            );
            callingQueue.add(callingNumber);
            return callingNumber;
        }
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
        Queue<String> targetQueue = getQueueByCapacity(capacity);
        if (targetQueue == null) {
            return 0;
        }
        return targetQueue.size();
    }
    
    /**
     * 根据容量获取对应的队列
     */
    private Queue<String> getQueueByCapacity(int capacity) {
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
    private Queue<String> getTargetQueue(int peopleNumber) {
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
     * 获取手机号在队列中的排队位置（前面有多少人）
     */
    private int getUserPosition(Queue<String> queue, String phone) {
        int pos = 0;
        for (String p : queue) {
            if (p.equals(phone)) {
                return pos;
            }
            pos++;
        }
        return -1;
    }
    
    /**
     * 查找当前叫号队列中该手机号的叫号信息
     */
    private CallingNumber findCallingNumberByPhone(String phone, int peopleNumber, int diningTableNumber, int position) {
        for (CallingNumber cn : callingQueue) {
            if (phone.equals(cn.getPhone()) && getTableCapacity(peopleNumber) == getTableCapacity(cn.getPeopleNumber())) {
                // 更新实时信息
                cn.setDiningTableNumber(diningTableNumber);
                cn.setPeopleNumber(position);
                cn.setTime(LocalDateTime.now());
                return cn;
            }
        }
        // 没找到则新建
        CallingNumber callingNumber = new CallingNumber(
            callingIdCounter,
            diningTableNumber,
            -1,
            LocalDateTime.now(),
            position,
            phone
        );
        callingQueue.add(callingNumber);
        return callingNumber;
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
     * 从队列中移除顾客
     */
    private void removeFromQueue(String phone, int peopleNumber) {
        Queue<String> targetQueue = getTargetQueue(peopleNumber);
        if (targetQueue != null) {
            targetQueue.remove(phone);
        }
    }
}
