package com.example.jerry.restaurant.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import com.example.jerry.restaurant.pojo.Checkout;
import com.example.jerry.restaurant.pojo.OrderDetail;
import com.example.jerry.restaurant.pojo.OrderSummary;
import com.example.jerry.restaurant.pojo.Result;
import com.example.jerry.restaurant.service.CheckoutService;
import com.example.jerry.restaurant.mapper.OrderMapper;
import com.example.jerry.restaurant.utils.JwtUtil;

@RestController
@RequestMapping("/checkout")
@CrossOrigin(origins = "http://localhost:8081", allowCredentials = "true")
public class CheckoutController {

    @Autowired
    private CheckoutService checkoutService;
    
    @Autowired
    private OrderMapper orderMapper;

    /**
     * 创建订单
     * @param tableId 餐桌ID
     * @param customerId 顾客ID
     * @param peopleCount 人数
     * @return 订单信息
     */
    @PostMapping("/create-order")
    public Result<Checkout> createOrder(
            @RequestParam int tableId,
            @RequestParam int customerId,
            @RequestParam int peopleCount) {
        return checkoutService.createOrder(tableId, customerId, peopleCount);
    }

    /**
     * 添加菜品到订单
     * @param authenticity 认证信息
     * @param orderId 订单ID
     * @param dishId 菜品ID
     * @param quantity 数量
     * @return 操作结果
     */
    @PostMapping("/add-dish")
    public Result<String> addDishToOrder(
            @RequestParam String authenticity,
            @RequestParam int orderId,
            @RequestParam int dishId,
            @RequestParam int quantity) {
        if (JwtUtil.parseToken(authenticity) == null) {
            return Result.error("无效的token，请重新登录");
        }
        return checkoutService.addDishToOrder(orderId, dishId, quantity);
    }

    /**
     * 从订单中移除菜品
     * @param authenticity 认证信息
     * @param orderId 订单ID
     * @param dishId 菜品ID
     * @return 操作结果
     */
    @DeleteMapping("/remove-dish")
    public Result<String> removeDishFromOrder(
            @RequestParam String authenticity,
            @RequestParam int orderId,
            @RequestParam int dishId) {
        if (JwtUtil.parseToken(authenticity) == null) {
            return Result.error("无效的token，请重新登录");
        }
        return checkoutService.removeDishFromOrder(orderId, dishId);
    }

    /**
     * 修改菜品数量
     * @param authenticity 认证信息
     * @param orderId 订单ID
     * @param dishId 菜品ID
     * @param quantity 新数量
     * @return 操作结果
     */
    @PutMapping("/update-dish-quantity")
    public Result<String> updateDishQuantity(
            @RequestParam String authenticity,
            @RequestParam int orderId,
            @RequestParam int dishId,
            @RequestParam int quantity) {
        if (JwtUtil.parseToken(authenticity) == null) {
            return Result.error("无效的token，请重新登录");
        }
        return checkoutService.updateDishQuantity(orderId, dishId, quantity);
    }

    /**
     * 结账
     * @param authenticity 认证信息
     * @param orderId 订单ID
     * @return 结账信息
     */
    @PostMapping("/checkout")
    public Result<Checkout> checkout(@RequestParam String authenticity, @RequestParam int orderId) {
        if (JwtUtil.parseToken(authenticity) == null) {
            return Result.error("无效的token，请重新登录");
        }
        return checkoutService.checkout(orderId);
    }

    /**
     * 根据时间段查询订单
     * @param authenticity 认证信息
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 订单列表
     */
    @GetMapping("/orders-by-time")
    public Result<List<OrderSummary>> getOrdersByTimeRange(
            @RequestParam String authenticity,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime) {
        if (JwtUtil.parseToken(authenticity) == null) {
            return Result.error("无效的token，请重新登录");
        }
        return checkoutService.getOrdersByTimeRange(startTime, endTime);
    }

    /**
     * 获取订单详情
     * @param authenticity 认证信息
     * @param orderId 订单ID
     * @return 订单详情
     */
    @GetMapping("/order-details/{orderId}")
    public Result<List<OrderDetail>> getOrderDetails(@RequestParam String authenticity, @PathVariable int orderId) {
        if (JwtUtil.parseToken(authenticity) == null) {
            return Result.error("无效的token，请重新登录");
        }
        return checkoutService.getOrderDetails(orderId);
    }

    /**
     * 获取最受欢迎菜品
     * @param authenticity 认证信息
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 热门菜品列表
     */
    @GetMapping("/popular-dishes")
    public Result<List<Map<String, Object>>> getPopularDishes(
            @RequestParam String authenticity,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime) {
        if (JwtUtil.parseToken(authenticity) == null) {
            return Result.error("无效的token，请重新登录");
        }
        return checkoutService.getPopularDishes(startTime, endTime);
    }

    /**
     * 获取营收统计
     * @param authenticity 认证信息
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 营收信息
     */
    @GetMapping("/revenue")
    public Result<Object> getRevenue(
            @RequestParam String authenticity,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime) {
        if (JwtUtil.parseToken(authenticity) == null) {
            return Result.error("无效的token，请重新登录");
        }
        // 这里可以调用存储过程 get_revenue_by_period
        // 暂时返回简单的实现
        List<Checkout> orders = checkoutService.getOrdersByTimeRangeAndStatus(startTime, endTime, "已结账").getData();
        double totalRevenue = orders.stream()
                .mapToDouble(order -> order.getFinalAmount().doubleValue())
                .sum();
        
        return Result.success(Map.of("totalRevenue", totalRevenue, "orderCount", orders.size()));
    }

    @PostMapping("/table/{tableId}")
    public Result<List<Map<String, Object>>> getOrderIdWithDishId(
            @RequestParam String authenticity,
            @PathVariable int tableId) {
        if (JwtUtil.parseToken(authenticity) == null) {
            return Result.error("无效的token，请重新登录");
        }
        return Result.success(checkoutService.getOrderIdWithDishId(tableId).getData());
    }
    
    /**
     * 获取所有订单详情（使用order_summary视图）
     * @param authenticity 认证信息
     * @return 订单摘要列表
     */
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
} 