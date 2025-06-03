package com.example.jerry.restaurant.service.impl;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.example.jerry.restaurant.mapper.OrderMapper;
import com.example.jerry.restaurant.pojo.Result;
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
        // orderMapper.addOrder(order);
        try {
            int result = orderMapper.addOrder(order);
            if (result > 0) {
                return orderMapper.getOrderById(orderId);
            } else {
                throw new RuntimeException("插入订单失败");
            }
        } catch (DataIntegrityViolationException e) {
            // 捕获外键约束异常
            throw new RuntimeException("外键冲突，插入订单失败: " + e.getMessage());
        } catch (Exception e) {
            // 捕获其他异常
            throw new RuntimeException("插入订单时发生错误: " + e.getMessage());
        }
     }

     @Override
     public order updateOrder(int orderId, int customerId, int tableId, BigDecimal totalAmount){
        orderMapper.updateOrder(orderId,customerId,tableId,totalAmount);
        return orderMapper.getOrderById(orderId);
     }

     @Override
     public String deleteOrder(int orderId){
         orderMapper.deleteOrder(orderId);
         if(orderMapper.getOrderById(orderId)==null)
         return "删除成功";
         else 
         return "删除失败";
     }

}
