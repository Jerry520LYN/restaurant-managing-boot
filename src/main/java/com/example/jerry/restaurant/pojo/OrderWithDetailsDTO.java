package com.example.jerry.restaurant.pojo;

import java.sql.Date;
import java.util.List;
import java.math.BigDecimal;

public class OrderWithDetailsDTO {
    private int orderId;
    private int customerId;
    private int tableId;
    private Date orderTime;
    private BigDecimal totalAmount;
    private BigDecimal discount;
    private BigDecimal finalAmount;
    private String status;
    private List<DishDetail> dishes;

    public OrderWithDetailsDTO() {}

    public OrderWithDetailsDTO(int orderId, int customerId, int tableId, Date orderTime, 
                              BigDecimal totalAmount, BigDecimal discount, BigDecimal finalAmount, String status) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.tableId = tableId;
        this.orderTime = orderTime;
        this.totalAmount = totalAmount;
        this.discount = discount;
        this.finalAmount = finalAmount;
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

    public List<DishDetail> getDishes() {
        return dishes;
    }

    public void setDishes(List<DishDetail> dishes) {
        this.dishes = dishes;
    }

    @Override
    public String toString() {
        return "OrderWithDetailsDTO{" +
                "orderId=" + orderId +
                ", customerId=" + customerId +
                ", tableId=" + tableId +
                ", orderTime=" + orderTime +
                ", totalAmount=" + totalAmount +
                ", discount=" + discount +
                ", finalAmount=" + finalAmount +
                ", status='" + status + '\'' +
                ", dishes=" + dishes +
                '}';
    }

    // 内部类表示菜品详情
    public static class DishDetail {
        private int dishId;
        private String dishName;
        private int quantity;

        public DishDetail() {}

        public DishDetail(int dishId, String dishName, int quantity) {
            this.dishId = dishId;
            this.dishName = dishName;
            this.quantity = quantity;
        }

        public int getDishId() {
            return dishId;
        }

        public void setDishId(int dishId) {
            this.dishId = dishId;
        }

        public String getDishName() {
            return dishName;
        }

        public void setDishName(String dishName) {
            this.dishName = dishName;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        @Override
        public String toString() {
            return "DishDetail{" +
                    "dishId=" + dishId +
                    ", dishName='" + dishName + '\'' +
                    ", quantity=" + quantity +
                    '}';
        }
    }
} 