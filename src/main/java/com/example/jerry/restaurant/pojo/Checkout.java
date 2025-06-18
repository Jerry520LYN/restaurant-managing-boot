package com.example.jerry.restaurant.pojo;

import java.math.BigDecimal;
import java.util.Date;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class Checkout {
    
    private int orderId;
    private String orderNumber;  // 16位订单编号
    private int customerId;
    private int tableId;
    private Date orderTime;
    private BigDecimal totalAmount;
    private String status;
    private BigDecimal discount;  // 折扣率
    private BigDecimal finalAmount;  // 最终金额（折扣后）
    
    public Checkout(int orderId, String orderNumber, int customerId, int tableId, 
                   BigDecimal totalAmount, BigDecimal discount) {
        this.orderId = orderId;
        this.orderNumber = orderNumber;
        this.customerId = customerId;
        this.tableId = tableId;
        this.totalAmount = totalAmount;
        this.discount = discount;
        this.status = "未结账";
    }
    
    // Getter 和 Setter 方法
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(BigDecimal finalAmount) {
        this.finalAmount = finalAmount;
    }

    @Override
    public String toString() {
        return "Checkout{" +
                "orderId=" + orderId +
                ", orderNumber='" + orderNumber + '\'' +
                ", customerId=" + customerId +
                ", tableId=" + tableId +
                ", orderTime=" + orderTime +
                ", totalAmount=" + totalAmount +
                ", status='" + status + '\'' +
                ", discount=" + discount +
                ", finalAmount=" + finalAmount +
                '}';
    }
} 