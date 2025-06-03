package com.example.jerry.restaurant.service.impl;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.jerry.restaurant.mapper.OrderMapper;
import com.example.jerry.restaurant.pojo.order;
import com.example.jerry.restaurant.service.OrderService;
import com.example.jerry.restaurant.service.OrderService;

@Service
public class OrderServicelmpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Override
     public order getOrderById(int orderId){
         return orderMapper.getOrderById(orderId);
     }

     @Override
     public order addOrder(int orderId, int customerId, int tableId, BigDecimal totalAmount){
        order order = new order(orderId, customerId, tableId, totalAmount);
        orderMapper.addOrder(order);
        return orderMapper.getOrderById(orderId);
     }

     @Override
     public order updateOrder(int orderId, int customerId, int tableId, BigDecimal totalAmount){
         return orderMapper.updateOrder(orderId,customerId,tableId,totalAmount);
     }

     @Override
     public String deleteOrder(int orderId){
         return orderMapper.deleteOrder(orderId);
     }

}
