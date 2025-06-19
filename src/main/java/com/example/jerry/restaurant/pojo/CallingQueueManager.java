package com.example.jerry.restaurant.pojo;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CallingQueueManager {
    // 三个不同容量的等待队列（手机号）
    private final Queue<String> queue2 = new ConcurrentLinkedQueue<>();
    private final Queue<String> queue4 = new ConcurrentLinkedQueue<>();
    private final Queue<String> queue8 = new ConcurrentLinkedQueue<>();
    // 三种容量的用餐队列（正在用餐）
    private final Queue<CallingNumber> diningQueue2 = new ConcurrentLinkedQueue<>();
    private final Queue<CallingNumber> diningQueue4 = new ConcurrentLinkedQueue<>();
    private final Queue<CallingNumber> diningQueue8 = new ConcurrentLinkedQueue<>();
    // 三种容量的等待队列（等待入座）
    private final Queue<CallingNumber> waitingQueue2 = new ConcurrentLinkedQueue<>();
    private final Queue<CallingNumber> waitingQueue4 = new ConcurrentLinkedQueue<>();
    private final Queue<CallingNumber> waitingQueue8 = new ConcurrentLinkedQueue<>();

    public Queue<String> getQueueByCapacity(int capacity) {
        switch (capacity) {
            case 2: return queue2;
            case 4: return queue4;
            case 8: return queue8;
            default: return null;
        }
    }
    public Queue<CallingNumber> getDiningQueueByCapacity(int capacity) {
        switch (capacity) {
            case 2: return diningQueue2;
            case 4: return diningQueue4;
            case 8: return diningQueue8;
            default: return null;
        }
    }
    public Queue<CallingNumber> getWaitingQueueByCapacity(int capacity) {
        switch (capacity) {
            case 2: return waitingQueue2;
            case 4: return waitingQueue4;
            case 8: return waitingQueue8;
            default: return null;
        }
    }
} 