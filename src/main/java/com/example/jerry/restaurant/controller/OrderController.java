package com.example.jerry.restaurant.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import com.example.jerry.restaurant.pojo.Result;
import com.example.jerry.restaurant.pojo.order;
import com.example.jerry.restaurant.service.OrderService;

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
    public Result<order> getOrderByID(@RequestParam String authenticity, @RequestParam int orderId) {
        if (JwtUtil.parseToken(authenticity) == null) {
            return Result.error("无效的token，请重新登录");
        }
        return Result.success(orderService.getOrderById(orderId));
    }

    @GetMapping("/all")
    public Result<List<order>> getAllOrders(@RequestParam String authenticity) {
        if (JwtUtil.parseToken(authenticity) == null) {
            return Result.error("无效的token，请重新登录");
        }
        return Result.success(orderService.getAllOrders());
    }

    @GetMapping("/allWithDetails")
    public Result<java.util.Map<String, Object>> getAllOrdersWithDetails(@RequestParam String authenticity) {
        if (JwtUtil.parseToken(authenticity) == null) {
            return Result.error("无效的token，请重新登录");
        }
        java.util.List<order> orders = orderMapper.getAllOrders();
        java.util.List<OrderDetail> orderDetails = orderDetailMapper.getAllOrderDetails();
        java.util.Map<String, Object> result = new java.util.HashMap<>();
        result.put("orders", orders);
        result.put("orderDetails", orderDetails);
        return Result.success(result);
    }
}
