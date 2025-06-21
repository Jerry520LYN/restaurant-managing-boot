package com.example.jerry.restaurant.pojo;

import java.math.BigDecimal;
import java.util.Date;

public class OrderSummary {
    private int orderId;
    private String customerName;
    private int tableId;
    private Date orderTime;
    private BigDecimal totalAmount;
    private BigDecimal finalAmount;
    private BigDecimal discount;
    private String dishes;

    public OrderSummary() {}

    public OrderSummary(int orderId, String customerName, int tableId, Date orderTime, 
                       BigDecimal totalAmount, BigDecimal finalAmount, BigDecimal discount, String dishes) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.tableId = tableId;
        this.orderTime = orderTime;
        this.totalAmount = totalAmount;
        this.finalAmount = finalAmount;
        this.discount = discount;
        this.dishes = dishes;
    }

    // Getter and Setter methods
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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

    @Override
    public String toString() {
        return "OrderSummary{" +
                "orderId=" + orderId +
                ", customerName='" + customerName + '\'' +
                ", tableId=" + tableId +
                ", orderTime=" + orderTime +
                ", totalAmount=" + totalAmount +
                ", finalAmount=" + finalAmount +
                ", discount=" + discount +
                ", dishes='" + dishes + '\'' +
                '}';
    }
} 