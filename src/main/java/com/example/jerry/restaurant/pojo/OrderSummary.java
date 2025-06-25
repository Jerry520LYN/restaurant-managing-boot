package com.example.jerry.restaurant.pojo;

import java.math.BigDecimal;
import java.util.Date;

public class OrderSummary {
    private int orderId;
    private int customerId;
    private String customerName;
    private String phone;
    private int tableId;
    private Date orderTime;
    private BigDecimal totalAmount;
    private BigDecimal finalAmount;
    private BigDecimal discount;
    private String dishes;
    private String status;

    public OrderSummary() {}

    public OrderSummary(int orderId, int customerId, String customerName, String phone, int tableId, Date orderTime, 
                       BigDecimal totalAmount, BigDecimal finalAmount, BigDecimal discount, String dishes, String status) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.phone = phone;
        this.tableId = tableId;
        this.orderTime = orderTime;
        this.totalAmount = totalAmount;
        this.finalAmount = finalAmount;
        this.discount = discount;
        this.dishes = dishes;
        this.status = status;
        }

    // Getter and Setter methods
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(BigDecimal finalAmount) {
        this.finalAmount = finalAmount;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public String getDishes() {
        return dishes;
    }

    public void setDishes(String dishes) {
        this.dishes = dishes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "OrderSummary{" +
                "orderId=" + orderId +
                ", customerId=" + customerId +
                ", customerName='" + customerName + '\'' +
                ", phone='" + phone + '\'' +
                ", tableId=" + tableId +
                ", orderTime=" + orderTime +
                ", totalAmount=" + totalAmount +
                ", finalAmount=" + finalAmount +
                ", discount=" + discount +
                ", dishes='" + dishes + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
} 