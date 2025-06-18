package com.example.jerry.restaurant.pojo;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class CallingNumber {
    private int callingNumberId;//叫号单子的Id
    private int diningTableNumber; // 剩余餐桌数量（正的不用等，负的需要等）
    private int tableId;// 叫到的餐桌ID
    private int estimatedWaitingTime; // 预估等待时间（分钟）
    private int capacity; // 餐桌容量（2/4/8）
    private String phone; // 顾客手机号

    public int getDiningTableNumber() {
        return diningTableNumber;
    }

    public void setDiningTableNumber(int diningTableNumber) {
        this.diningTableNumber = diningTableNumber;
    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public int getEstimatedWaitingTime() {
        return estimatedWaitingTime;
    }

    public void setEstimatedWaitingTime(int estimatedWaitingTime) {
        this.estimatedWaitingTime = estimatedWaitingTime;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getCallingNumberId() {
        return callingNumberId;
    }

    public void setCallingNumberId(int callingNumberId) {
        this.callingNumberId = callingNumberId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
