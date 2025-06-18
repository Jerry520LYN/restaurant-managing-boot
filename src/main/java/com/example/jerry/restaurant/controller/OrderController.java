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

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/orders")
@Validated
@CrossOrigin(origins = "http://localhost:8081", allowCredentials = "true")
public class OrderController {
    
    @Autowired
    private OrderService orderService; 

    @PostMapping("/addOrder")
    public Result<order> addOrder(
        @RequestParam int orderId,
        @RequestParam int customerId,
        @RequestParam int tableId,
        @RequestParam BigDecimal totalAmount
    ) {
       order existorder = orderService.getOrderById(orderId);
       if(existorder != null)
       return Result.error("该订单ID已存在");
    //    orderService.addOrder(orderId, customerId, tableId, totalAmount);
    //    return Result.success(orderService.getOrderById(orderId));
       try {
            order addedOrder = orderService.addOrder(orderId, customerId, tableId, totalAmount);
            return Result.success(addedOrder);
        } catch (RuntimeException e) {
            // 捕获服务层抛出的异常
            //这里直接用自己的提示方法
            return Result.error("请检查客人或者餐桌是否存在");
        }
    }

    @DeleteMapping("/deleteOrder")
    public Result<String> deleteOrder(@RequestParam int orderId) {
        String result = orderService.deleteOrder(orderId);
        return Result.success(result);
    }
    
    @PostMapping("/updateOrder")
    public Result<order> updateOrder(
        @RequestParam int orderId,
        @RequestParam int customerId,
        @RequestParam int tableId,
        @RequestParam BigDecimal totalAmount
    ) {
       order existorder = orderService.getOrderById(orderId);
       if(existorder == null)
       return Result.error("该订单ID不存在");
       orderService.updateOrder(orderId, customerId, tableId, totalAmount);
       return Result.success(orderService.getOrderById(orderId));
    }
    
    
    @GetMapping("/getOrderByID")
    public Result<order> getOrderByID(@RequestParam int orderId) {
        return Result.success(orderService.getOrderById(orderId));
    }
    
}
