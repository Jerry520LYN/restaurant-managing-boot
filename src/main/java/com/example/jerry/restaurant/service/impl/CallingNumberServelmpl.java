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
    
    private final CallingQueueManager queueManager = new CallingQueueManager();
    
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
        // 先在所有队列中查找
        int existCapacity = findCapacityByPhone(phone);
        if (existCapacity > 0) {
            // 在某个队列中已存在，直接返回叫号信息
            CallingNumber cn = findCallingNumberByPhone(phone, existCapacity);
            if (cn != null) {
                updateCallingNumberInfo(cn);
                return cn;
            }
        }
    
        // 不存在，才新建
        int capacity = getTableCapacity(peopleNumber);
        Queue<String> targetQueue = queueManager.getQueueByCapacity(capacity);
        if (targetQueue == null) {
            return null;
        }
        callingIdCounter++;
        int assignedTableId = -1;
        
        // 只有排在队列第一位的顾客才分配餐桌ID
        if (targetQueue.size() == 0) {
            Integer availableTableId = diningTableMapper.getAvailableTableId(capacity);
            if (availableTableId != null && availableTableId > 0) {
                assignedTableId = availableTableId;
                // 注意：这里不更新餐桌状态，保持"空"状态
                // 餐桌状态只有在创建订单时才更新为"占用"
            }
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
        targetQueue.add(phone); // 别忘了把手机号加到对应队列
        return callingNumber;
    }

    
    
    /**
     * 检查并清理已完成的叫号（餐桌已占用）
     */
    private void checkAndCleanCompletedCallings() {
        Iterator<CallingNumber> iterator = queueManager.getCallingQueue().iterator();
        while (iterator.hasNext()) {
            CallingNumber calling = iterator.next();
            // 只检查有餐桌ID的叫号（已分配餐桌的）
            if (calling.getTableId() > 0) {
                // 检查餐桌状态
                DiningTable table = diningTableMapper.getTableById(calling.getTableId());
                if (table != null && "占用".equals(table.getTableStatus())) {
                    // 餐桌已被占用（顾客已入座），从叫号队列中移除
                    iterator.remove();
                    // 同时从容量队列中移除手机号
                    Queue<String> capacityQueue = queueManager.getQueueByCapacity(calling.getCapacity());
                    if (capacityQueue != null) {
                        capacityQueue.remove(calling.getPhone());
                    }
                }
                // 如果餐桌状态为"空"，说明顾客还未入座，保留叫号记录
            }
        }
        
        // 更新所有叫号的等待信息
        updateAllCallingNumbers();
        
        // 重新分配餐桌ID给队列第一位顾客
        reassignTableIdsToFirstInQueue();
    }
    
    /**
     * 重新分配餐桌ID给各容量队列的第一位顾客
     */
    private void reassignTableIdsToFirstInQueue() {
        // 检查2人桌队列
        reassignTableIdForCapacity(2);
        // 检查4人桌队列
        reassignTableIdForCapacity(4);
        // 检查8人桌队列
        reassignTableIdForCapacity(8);
    }
    
    /**
     * 为指定容量的队列重新分配餐桌ID
     */
    private void reassignTableIdForCapacity(int capacity) {
        Queue<String> capacityQueue = queueManager.getQueueByCapacity(capacity);
        if (capacityQueue == null || capacityQueue.isEmpty()) {
            return;
        }
        
        // 找到队列中第一位顾客的叫号记录
        String firstPhone = capacityQueue.peek();
        if (firstPhone == null) {
            return;
        }
        
        // 查找该顾客的叫号记录
        CallingNumber firstCalling = null;
        for (CallingNumber cn : queueManager.getCallingQueue()) {
            if (firstPhone.equals(cn.getPhone()) && cn.getCapacity() == capacity) {
                firstCalling = cn;
                break;
            }
        }
        
        if (firstCalling != null && firstCalling.getTableId() == -1) {
            // 如果第一位顾客还没有分配餐桌，尝试分配
            Integer availableTableId = diningTableMapper.getAvailableTableId(capacity);
            if (availableTableId != null && availableTableId > 0) {
                firstCalling.setTableId(availableTableId);
                // 注意：这里不更新餐桌状态，保持"空"状态
                // 餐桌状态只有在创建订单时才更新为"占用"
            }
        }
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
        return (hour >= 9 && hour <= 14) || (hour >= 16 && hour <= 24);
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
            if (phone.equals(cn.getPhone()) && cn.getCapacity() == capacity) {
                return cn;
            }
        }
        return null;
    }
    private int findCapacityByPhone(String phone) {
        if (queueManager.getQueueByCapacity(2).contains(phone)) return 2;
        if (queueManager.getQueueByCapacity(4).contains(phone)) return 4;
        if (queueManager.getQueueByCapacity(8).contains(phone)) return 8;
        return 0; // 没有找到
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
