package com.example.jerry.restaurant.pojo;
import java.sql.Date;

import org.springframework.context.annotation.Primary;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
@NoArgsConstructor
@AllArgsConstructor
public class order {
    
    private int orderId;
    private String orderNumber;

    @NotNull(message = "Customer ID cannot be null")
    private int customerId;
    @NotNull(message = "Table ID cannot be null")
    private int tableId;
    @NotNull(message = "Order time cannot be null")
    private Date orderTime;
    @NotNull(message = "Total amount cannot be null")
    private BigDecimal totalAmount;
    
    private BigDecimal discount;
    private BigDecimal finalAmount;

    private String status = "未结账";
    
    public order(int orderId, int customerId, int tableId, BigDecimal totalAmount){
        this.orderId = orderId;
        this.customerId = customerId;
        this.tableId = tableId;
        this.totalAmount = totalAmount;
    }
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // toString method
    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", orderNumber='" + orderNumber + '\'' +
                ", customerId=" + customerId +
                ", tableId=" + tableId +
                ", orderTime=" + orderTime +
                ", totalAmount=" + totalAmount +
                ", discount=" + discount +
                ", finalAmount=" + finalAmount +
                ", status='" + status + '\'' +
                '}';
    }

}
