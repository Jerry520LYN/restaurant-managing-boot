package com.example.jerry.restaurant.pojo;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CallingQueueManager {
    // 三个不同容量的等待队列（手机号）
    private final Queue<String> queue2 = new ConcurrentLinkedQueue<>();
    private final Queue<String> queue4 = new ConcurrentLinkedQueue<>();
    private final Queue<String> queue8 = new ConcurrentLinkedQueue<>();
    // 叫号队列（只存未入座记录）
    private final Queue<CallingNumber> callingQueue = new ConcurrentLinkedQueue<>();

    public Queue<String> getQueueByCapacity(int capacity) {
        switch (capacity) {
            case 2: return queue2;
            case 4: return queue4;
            case 8: return queue8;
            default: return null;
        }
    }
    public Queue<CallingNumber> getCallingQueue() {
        return callingQueue;
    }
} 