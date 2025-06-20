package com.example.jerry.restaurant.service;

import com.example.jerry.restaurant.pojo.Order;
import java.math.BigDecimal;
import java.util.List;
public interface OrderService {
    Order getOrderById( int orderId);
    String deleteOrder(int orderId);
    List<Order> getAllOrders();
}

