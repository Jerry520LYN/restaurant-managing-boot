package com.example.jerry.restaurant.pojo;

public class CallingNumber {
    private int callingNumberId; // 叫号单子的Id
    private int waitingCount;   // 当前等待人数
    private int diningCount;    // 当前正在用餐桌数
    private String status;      // 状态：WAITING, DINING, FINISHED
    private String phone;       // 顾客手机号

    public int getCallingNumberId() {
        return callingNumberId;
    }

    public void setCallingNumberId(int callingNumberId) {
        this.callingNumberId = callingNumberId;
    }

    public int getWaitingCount() {
        return waitingCount;
    }

    public void setWaitingCount(int waitingCount) {
        this.waitingCount = waitingCount;
    }

    public int getDiningCount() {
        return diningCount;
    }

    public void setDiningCount(int diningCount) {
        this.diningCount = diningCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
