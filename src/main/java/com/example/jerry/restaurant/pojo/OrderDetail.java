package com.example.jerry.restaurant.pojo;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class OrderDetail {
    
    private int detailId;
    private int orderId;
    private int dishId;
    private int quantity;
    private String dishName;  // 菜品名称，用于显示
    private double price;     // 菜品价格，用于计算
    
    public OrderDetail(int orderId, int dishId, int quantity) {
        this.orderId = orderId;
        this.dishId = dishId;
        this.quantity = quantity;
    }
    
    // Getter 和 Setter 方法
    public int getDetailId() {
        return detailId;
    }

    public void setDetailId(int detailId) {
        this.detailId = detailId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getDishId() {
        return dishId;
    }

    public void setDishId(int dishId) {
        this.dishId = dishId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "OrderDetail{" +
                "detailId=" + detailId +
                ", orderId=" + orderId +
                ", dishId=" + dishId +
                ", quantity=" + quantity +
                ", dishName='" + dishName + '\'' +
                ", price=" + price +
                '}';
    }
} 