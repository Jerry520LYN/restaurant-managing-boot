package com.example.jerry.restaurant.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import com.example.jerry.restaurant.pojo.Result;
import com.example.jerry.restaurant.pojo.Order;
import com.example.jerry.restaurant.pojo.OrderWithDetailsDTO;
import com.example.jerry.restaurant.pojo.OrderSummary;
import com.example.jerry.restaurant.pojo.Checkout;
import com.example.jerry.restaurant.service.OrderService;
import com.example.jerry.restaurant.service.CheckoutService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.jerry.restaurant.mapper.OrderDetailMapper;
import com.example.jerry.restaurant.mapper.OrderMapper;
import com.example.jerry.restaurant.pojo.OrderDetail;
import com.example.jerry.restaurant.utils.JwtUtil;

@RestController
@RequestMapping("/orders")
@Validated
@CrossOrigin(origins = "http://localhost:8081", allowCredentials = "true")
public class OrderController {
    
    @Autowired
    private OrderService orderService; 

    @Autowired
    private CheckoutService checkoutService;

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @DeleteMapping("/deleteOrder")
    public Result<String> deleteOrder(@RequestParam String authenticity, @RequestParam int orderId) {
        if (JwtUtil.parseToken(authenticity) == null) {
            return Result.error("无效的token，请重新登录");
        }
        String result = orderService.deleteOrder(orderId);
        return Result.success(result);
    }
    
    @GetMapping("/getOrderByID")
    public Result<OrderSummary> getOrderByID(@RequestParam String authenticity, @RequestParam int orderId) {
        if (JwtUtil.parseToken(authenticity) == null) {
            return Result.error("无效的token，请重新登录");
        }
        return Result.success(orderService.getOrderById(orderId));
    }

    @GetMapping("/all")
    public Result<List<OrderSummary>> getAllOrders(@RequestParam String authenticity) {
        if (JwtUtil.parseToken(authenticity) == null) {
            return Result.error("无效的token，请重新登录");
        }
        return checkoutService.getAllOrdersAsCheckout();
    }

    @GetMapping("/allWithDetails")
    public Result<List<OrderSummary>> getAllOrdersWithDetails(@RequestParam String authenticity) {
        if (JwtUtil.parseToken(authenticity) == null) {
            return Result.error("无效的token，请重新登录");
        }
        
        try {
            // 使用order_summary视图查询所有订单摘要信息
            List<OrderSummary> orderSummaries = orderMapper.getAllOrderSummaries();
            return Result.success(orderSummaries);
        } catch (Exception e) {
            return Result.error("获取订单详情失败: " + e.getMessage());
        }
    }

    @GetMapping("/getOrderByNumber")
    public Result<Order> getOrderByNumber(@RequestParam String authenticity, @RequestParam String orderNumber) {
        if (JwtUtil.parseToken(authenticity) == null) {
            return Result.error("无效的token，请重新登录");
        }
        return Result.success(orderService.getOrderByNumber(orderNumber));
    }
}
