package com.example.jerry.restaurant.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.jerry.restaurant.pojo.Order;
import com.example.jerry.restaurant.mapper.OrderMapper;
import com.example.jerry.restaurant.pojo.OrderSummary;
import com.example.jerry.restaurant.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public OrderSummary getOrderById(int orderId) {
        return orderMapper.getOrderById(orderId);
    }

    @Override
    public String deleteOrder(int orderId) {
        orderMapper.deleteOrder(orderId);
        return orderMapper.getOrderById(orderId) == null ? "订单删除成功" : "删除失败";
    }

    @Override
    public List<Order> getAllOrders() {
        return orderMapper.getAllOrders();
    }

    @Override
    public Order getOrderByNumber(String orderNumber) {
        return orderMapper.getOrderByNumber(orderNumber);
    }
}
