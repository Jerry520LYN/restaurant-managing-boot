package com.example.jerry.restaurant.service.impl;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.example.jerry.restaurant.mapper.OrderMapper;
import com.example.jerry.restaurant.pojo.Result;
import com.example.jerry.restaurant.pojo.order;
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
    public String deleteOrder(int orderId) {
        orderMapper.deleteOrder(orderId);
        return orderMapper.getOrderById(orderId) == null ? "订单删除成功" : "删除失败";
    }
}
