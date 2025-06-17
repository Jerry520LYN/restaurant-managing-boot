package com.example.jerry.restaurant.pojo;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class CallingNumber {
    private int CallingNumberId;
    private int number; // 剩余餐桌数量（正的不用等，负的需要等）
    private int tableId;// 叫到的餐桌ID
    private LocalDateTime time; // 需要等待的时间
    private int peoplenumber;

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

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public int getPeopleNumber() {
        return peoplenumber;
    }

    public void setPeopleNumber(int peoplenumber) {
        this.peoplenumber = peoplenumber;
    }

    public int getcallingnumberId() {
        return CallingNumberId;
    }

    public void setCallingNumberId(int CallingNumberId) {
        this.CallingNumberId = CallingNumberId;
    }
}
