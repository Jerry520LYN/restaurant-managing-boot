package com.example.jerry.restaurant.service.impl;

import java.util.*;
import org.springframework.stereotype.Service;
import com.example.jerry.restaurant.pojo.CallingNumber;
import com.example.jerry.restaurant.service.CallingNumberService;
import com.example.jerry.restaurant.pojo.CallingQueueManager;

@Service
public class CallingNumberServiceImpl implements CallingNumberService {
    private final CallingQueueManager queueManager = new CallingQueueManager();
    private int callingIdCounter = 0;

    @Override
    public CallingNumber addCallingNumber(String phone, int peopleNumber) {
        if (peopleNumber <= 0 || phone == null || phone.isEmpty()) {
            return null;
        }
        int capacity = getTableCapacity(peopleNumber);
        if (capacity == 0) return null;
        int DINING_QUEUE_MAX = 5;
        Queue<CallingNumber> diningQueue = queueManager.getDiningQueueByCapacity(capacity);
        Queue<CallingNumber> waitingQueue = queueManager.getWaitingQueueByCapacity(capacity);
        // 检查是否已存在
        for (CallingNumber cn : diningQueue) {
            if (phone.equals(cn.getPhone()) && !"FINISHED".equals(cn.getStatus())) {
                return cn;
            }
        }
        for (CallingNumber cn : waitingQueue) {
            if (phone.equals(cn.getPhone()) && !"FINISHED".equals(cn.getStatus())) {
                return cn;
            }
        }
        callingIdCounter++;
        CallingNumber callingNumber = new CallingNumber();
        callingNumber.setCallingNumberId(callingIdCounter);
        callingNumber.setPhone(phone);
        callingNumber.setStatus(diningQueue.size() < DINING_QUEUE_MAX ? "DINING" : "WAITING");
        callingNumber.setDiningCount(diningQueue.size() < DINING_QUEUE_MAX ? diningQueue.size() + 1 : diningQueue.size());
        callingNumber.setWaitingCount(waitingQueue.size() + (diningQueue.size() < DINING_QUEUE_MAX ? 0 : 1));
        if (diningQueue.size() < DINING_QUEUE_MAX) {
            diningQueue.add(callingNumber);
        } else {
            waitingQueue.add(callingNumber);
        }
        return callingNumber;
    }

    @Override
    public boolean removeCallingNumber(int callingNumberId) {
        boolean removed = false;
        for (int cap : Arrays.asList(2, 4, 8)) {
            Queue<CallingNumber> diningQueue = queueManager.getDiningQueueByCapacity(cap);
            Queue<CallingNumber> waitingQueue = queueManager.getWaitingQueueByCapacity(cap);
            removed |= removeFromQueueById(diningQueue, callingNumberId);
            removed |= removeFromQueueById(waitingQueue, callingNumberId);
        }
        return removed;
    }
    private boolean removeFromQueueById(Queue<CallingNumber> queue, int id) {
        Iterator<CallingNumber> it = queue.iterator();
        while (it.hasNext()) {
            if (it.next().getCallingNumberId() == id) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean updateCallingNumberStatus(int callingNumberId, String status) {
        for (int cap : Arrays.asList(2, 4, 8)) {
            Queue<CallingNumber> diningQueue = queueManager.getDiningQueueByCapacity(cap);
            Queue<CallingNumber> waitingQueue = queueManager.getWaitingQueueByCapacity(cap);
            for (CallingNumber cn : diningQueue) {
                if (cn.getCallingNumberId() == callingNumberId) {
                    cn.setStatus(status);
                    if ("WAITING".equals(status)) {
                        diningQueue.remove(cn);
                        waitingQueue.add(cn);
                    }
                    if ("FINISHED".equals(status)) {
                        diningQueue.remove(cn);
                    }
                    return true;
                }
            }
            for (CallingNumber cn : waitingQueue) {
                if (cn.getCallingNumberId() == callingNumberId) {
                    cn.setStatus(status);
                    if ("DINING".equals(status)) {
                        waitingQueue.remove(cn);
                        diningQueue.add(cn);
                    }
                    if ("FINISHED".equals(status)) {
                        waitingQueue.remove(cn);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Map<String, List<CallingNumber>> getAllQueues() {
        Map<String, List<CallingNumber>> map = new HashMap<>();
        map.put("diningQueue2", new ArrayList<>(queueManager.getDiningQueueByCapacity(2)));
        map.put("diningQueue4", new ArrayList<>(queueManager.getDiningQueueByCapacity(4)));
        map.put("diningQueue8", new ArrayList<>(queueManager.getDiningQueueByCapacity(8)));
        map.put("waitingQueue2", new ArrayList<>(queueManager.getWaitingQueueByCapacity(2)));
        map.put("waitingQueue4", new ArrayList<>(queueManager.getWaitingQueueByCapacity(4)));
        map.put("waitingQueue8", new ArrayList<>(queueManager.getWaitingQueueByCapacity(8)));
        return map;
    }

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
}
