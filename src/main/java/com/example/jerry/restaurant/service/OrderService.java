package com.example.jerry.restaurant.service;

import com.example.jerry.restaurant.pojo.Order;
import com.example.jerry.restaurant.pojo.OrderSummary;
import java.util.List;
public interface OrderService {
    OrderSummary getOrderById( int orderId);
    String deleteOrder(int orderId);
    List<Order> getAllOrders();

    Order getOrderByNumber(String orderNumber);
}

