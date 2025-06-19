package com.example.jerry.restaurant.service;

import com.example.jerry.restaurant.pojo.order;
import java.math.BigDecimal;

public interface OrderService {
    order getOrderById( int orderId);
    String deleteOrder(int orderId);
}

