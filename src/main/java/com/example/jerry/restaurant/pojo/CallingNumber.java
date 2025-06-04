package com.example.jerry.restaurant.pojo;

import java.security.Timestamp;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class CallingNumber {
    private int number; // 剩余餐桌数量（正的不用等，负的需要等）
    private int tableId;// 叫到的餐桌ID
    private Timestamp time; // 需要等待的时间

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }
}
