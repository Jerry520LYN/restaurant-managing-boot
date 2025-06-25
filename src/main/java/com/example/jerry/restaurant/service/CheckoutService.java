package com.example.jerry.restaurant.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.example.jerry.restaurant.pojo.Checkout;
import com.example.jerry.restaurant.pojo.OrderDetail;
import com.example.jerry.restaurant.pojo.Result;
import com.example.jerry.restaurant.pojo.OrderSummary;

public interface CheckoutService {
    
    // 创建订单
    Result<Checkout> createOrder(int tableId, int customerId, int peopleCount);
    
    // 添加菜品到订单
    Result<String> addDishToOrder(int orderId, int dishId, int quantity);
    
    // 从订单中移除菜品
    Result<String> removeDishFromOrder(int orderId, int dishId);
    
    // 修改菜品数量
    Result<String> updateDishQuantity(int orderId, int dishId, int quantity);
    
    // 结账
    Result<Checkout> checkout(int orderId);
    
    // 根据时间段查询订单
    Result<List<OrderSummary>> getOrdersByTimeRange(Date startTime, Date endTime);
    
    // 获取订单详情
    Result<List<OrderDetail>> getOrderDetails(int orderId);
    
    // 获取最受欢迎菜品
    Result<List<Map<String, Object>>> getPopularDishes(Date startTime, Date endTime);

    Result<List<Checkout>> getOrdersByTimeRangeAndStatus(Date startTime, Date endTime, String status);

    Result<List<OrderSummary>> getAllOrdersAsCheckout();

    Result<List<Map<String, Object>>> getOrderIdWithDishId(int tableId);
} 