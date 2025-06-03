package com.example.jerry.restaurant.service;

import org.springframework.stereotype.Service;

import com.example.jerry.restaurant.pojo.order;
import java.math.BigDecimal;

public interface OrderService {
    order addOrder( int orderId, int customerId, int tableId, BigDecimal totalAmount);

    order updateOrder( int orderId, int customerId, int tableId, BigDecimal totalAmount);

    order getOrderById( int orderId);

    String deleteOrder(int orderId);
}

