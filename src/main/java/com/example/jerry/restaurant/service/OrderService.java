package com.example.jerry.restaurant.service;

import com.example.jerry.restaurant.pojo.order;
import java.math.BigDecimal;
import java.util.List;
public interface OrderService {
    order getOrderById( int orderId);
    String deleteOrder(int orderId);
    List<order> getAllOrders();
}

