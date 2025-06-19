package com.example.jerry.restaurant.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.jerry.restaurant.pojo.Result;
import com.example.jerry.restaurant.pojo.order;
import com.example.jerry.restaurant.service.OrderService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/orders")
@Validated
@CrossOrigin(origins = "http://localhost:8081", allowCredentials = "true")
public class OrderController {
    
    @Autowired
    private OrderService orderService; 

    @DeleteMapping("/deleteOrder")
    public Result<String> deleteOrder(@RequestParam int orderId) {
        String result = orderService.deleteOrder(orderId);
        return Result.success(result);
    }
    
    @GetMapping("/getOrderByID")
    public Result<order> getOrderByID(@RequestParam int orderId) {
        return Result.success(orderService.getOrderById(orderId));
    }
}
