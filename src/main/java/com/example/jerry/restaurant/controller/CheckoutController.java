package com.example.jerry.restaurant.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import com.example.jerry.restaurant.pojo.Checkout;
import com.example.jerry.restaurant.pojo.OrderDetail;
import com.example.jerry.restaurant.pojo.Result;
import com.example.jerry.restaurant.service.CheckoutService;

@RestController
@RequestMapping("/checkout")
@CrossOrigin
public class CheckoutController {

    @Autowired
    private CheckoutService checkoutService;

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
     * @param orderId 订单ID
     * @param dishId 菜品ID
     * @param quantity 数量
     * @return 操作结果
     */
    @PostMapping("/add-dish")
    public Result<String> addDishToOrder(
            @RequestParam int orderId,
            @RequestParam int dishId,
            @RequestParam int quantity) {
        return checkoutService.addDishToOrder(orderId, dishId, quantity);
    }

    /**
     * 从订单中移除菜品
     * @param orderId 订单ID
     * @param dishId 菜品ID
     * @return 操作结果
     */
    @DeleteMapping("/remove-dish")
    public Result<String> removeDishFromOrder(
            @RequestParam int orderId,
            @RequestParam int dishId) {
        return checkoutService.removeDishFromOrder(orderId, dishId);
    }

    /**
     * 修改菜品数量
     * @param orderId 订单ID
     * @param dishId 菜品ID
     * @param quantity 新数量
     * @return 操作结果
     */
    @PutMapping("/update-dish-quantity")
    public Result<String> updateDishQuantity(
            @RequestParam int orderId,
            @RequestParam int dishId,
            @RequestParam int quantity) {
        return checkoutService.updateDishQuantity(orderId, dishId, quantity);
    }

    /**
     * 结账
     * @param orderId 订单ID
     * @return 结账信息
     */
    @PostMapping("/checkout")
    public Result<Checkout> checkout(@RequestParam int orderId) {
        return checkoutService.checkout(orderId);
    }

    /**
     * 根据时间段查询订单
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 订单列表
     */
    @GetMapping("/orders-by-time")
    public Result<List<Checkout>> getOrdersByTimeRange(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime) {
        return checkoutService.getOrdersByTimeRange(startTime, endTime);
    }

    /**
     * 获取订单详情
     * @param orderId 订单ID
     * @return 订单详情
     */
    @GetMapping("/order-details/{orderId}")
    public Result<List<OrderDetail>> getOrderDetails(@PathVariable int orderId) {
        return checkoutService.getOrderDetails(orderId);
    }

    /**
     * 获取最受欢迎菜品
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 热门菜品列表
     */
    @GetMapping("/popular-dishes")
    public Result<List<Object>> getPopularDishes(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime) {
        return checkoutService.getPopularDishes(startTime, endTime);
    }

    /**
     * 获取营收统计
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 营收信息
     */
    @GetMapping("/revenue")
    public Result<Object> getRevenue(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime) {
        // 这里可以调用存储过程 get_revenue_by_period
        // 暂时返回简单的实现
        List<Checkout> orders = checkoutService.getOrdersByTimeRange(startTime, endTime).getData();
        double totalRevenue = orders.stream()
                .mapToDouble(order -> order.getFinalAmount().doubleValue())
                .sum();
        
        return Result.success(Map.of("totalRevenue", totalRevenue, "orderCount", orders.size()));
    }
} 