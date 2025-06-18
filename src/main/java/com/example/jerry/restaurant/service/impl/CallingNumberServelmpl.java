package com.example.jerry.restaurant.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.jerry.restaurant.mapper.DiningTableMapper;
import com.example.jerry.restaurant.pojo.CallingNumber;
import com.example.jerry.restaurant.pojo.DiningTable;
import com.example.jerry.restaurant.service.CallingNumberService;
import com.example.jerry.restaurant.pojo.CallingQueueManager;

@Service
public class CallingNumberServelmpl implements CallingNumberService{
    @Autowired
    private DiningTableMapper diningTableMapper;
    
    private static final CallingQueueManager queueManager = new CallingQueueManager();
    
    // 叫号ID计数器
    private int callingIdCounter = 0;
    
    @Override
    public List<CallingNumber> getResultList() {
        // 实时更新每条记录的diningTableNumber和estimatedWaitingTime
        for (CallingNumber cn : queueManager.getCallingQueue()) {
            updateCallingNumberInfo(cn);
        }
        return queueManager.getCallingQueue().stream().collect(Collectors.toList());
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
        int capacity = getTableCapacity(peopleNumber);
        Queue<String> targetQueue = queueManager.getQueueByCapacity(capacity);
        if (targetQueue == null) {
            return null;
        }
        // 如果手机号已在队列，直接返回当前叫号信息
        if (targetQueue.contains(phone)) {
            CallingNumber cn = findCallingNumberByPhone(phone, capacity);
            if (cn != null) {
                updateCallingNumberInfo(cn);
                return cn;
            } else {
                // 没找到叫号对象，兜底返回null或新建对象
                return null;
            }
        } else {
            targetQueue.add(phone);
            callingIdCounter++;
            int assignedTableId = -1;
            Integer availableTableId = diningTableMapper.getAvailableTableId(capacity);
            if (availableTableId != null && availableTableId > 0) {
                assignedTableId = availableTableId;
            }
            CallingNumber callingNumber = new CallingNumber(
                callingIdCounter,
                targetQueue.size() - 1,
                assignedTableId,
                0,
                capacity,
                phone
            );
            updateCallingNumberInfo(callingNumber);
            queueManager.getCallingQueue().add(callingNumber);
            return callingNumber;
        }
    }
    
    /**
     * 检查并清理已完成的叫号（餐桌已空）
     */
    private void checkAndCleanCompletedCallings() {
        Iterator<CallingNumber> iterator = queueManager.getCallingQueue().iterator();
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
        for (CallingNumber calling : queueManager.getCallingQueue()) {
            // 新结构下无需更新peopleNumber和time，diningTableNumber和estimatedWaitingTime已在updateCallingNumberInfo中实时更新
            updateCallingNumberInfo(calling);
        }
    }
    
    /**
     * 根据餐桌容量计算等待人数
     */
    private int calculateWaitingPeopleByCapacity(int capacity) {
        Queue<String> targetQueue = queueManager.getQueueByCapacity(capacity);
        if (targetQueue == null) {
            return 0;
        }
        return targetQueue.size();
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
            return queueManager.getQueueByCapacity(2);
        } else if (peopleNumber >= 3 && peopleNumber <= 4) {
            return queueManager.getQueueByCapacity(4);
        } else if (peopleNumber >= 5 && peopleNumber <= 8) {
            return queueManager.getQueueByCapacity(8);
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
    private CallingNumber findCallingNumberByPhone(String phone, int capacity) {
        for (CallingNumber cn : queueManager.getCallingQueue()) {
            if (phone.equals(cn.getPhone()) && cn.getCapacity() == capacity && cn.getTableId() == -1) {
                return cn;
            }
        }
        return null;
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
    
    // 更新叫号信息（位置、预估等待时间）
    private void updateCallingNumberInfo(CallingNumber cn) {
        if (cn.getTableId() > 0) {
            cn.setDiningTableNumber(0);
            cn.setEstimatedWaitingTime(0);
        } else {
            int position = getQueuePosition(cn.getPhone(), cn.getCapacity());
            cn.setDiningTableNumber(position);
            cn.setEstimatedWaitingTime(position * getWaitTimePerTable(cn.getCapacity()));
        }
    }
    
    // 获取手机号在队列中的排队位置
    private int getQueuePosition(String phone, int capacity) {
        Queue<String> queue = queueManager.getQueueByCapacity(capacity);
        int pos = 0;
        for (String p : queue) {
            if (p.equals(phone)) {
                return pos;
            }
            pos++;
        }
        return -1;
    }
    
    // 获取每桌预估等待时间
    private int getWaitTimePerTable(int capacity) {
        switch (capacity) {
            case 2: return 30;
            case 4: return 60;
            case 8: return 90;
            default: return 0;
        }
    }
}
