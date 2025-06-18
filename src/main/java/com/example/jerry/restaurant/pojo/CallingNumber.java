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
    private LocalDateTime time; // 需要等待的时间
    private int peopleNumber;//需要等待的人数
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

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public int getPeopleNumber() {
        return peopleNumber;
    }

    public void setPeopleNumber(int peopleNumber) {
        this.peopleNumber = peopleNumber;
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
