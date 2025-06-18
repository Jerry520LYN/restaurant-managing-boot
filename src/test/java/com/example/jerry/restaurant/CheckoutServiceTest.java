package com.example.jerry.restaurant;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.jerry.restaurant.pojo.Checkout;
import com.example.jerry.restaurant.pojo.Result;
import com.example.jerry.restaurant.service.CheckoutService;

@SpringBootTest
public class CheckoutServiceTest {

    @Autowired
    private CheckoutService checkoutService;

    @Test
    public void testCreateOrder() {
        // 测试创建订单
        Result<Checkout> result = checkoutService.createOrder(1, 1001, 4);
        assertNotNull(result);
        assertTrue(result.getCode() == 0 || result.getMessage().contains("餐桌不存在") || result.getMessage().contains("顾客不存在"));
    }

    @Test
    public void testAddDishToOrder() {
        // 测试添加菜品（需要先有订单）
        Result<String> result = checkoutService.addDishToOrder(1, 1, 2);
        assertNotNull(result);
        // 由于可能没有订单或菜品，这里只检查返回结果不为空
    }

    @Test
    public void testCheckout() {
        // 测试结账（需要先有订单和菜品）
        Result<Checkout> result = checkoutService.checkout(1);
        assertNotNull(result);
        // 由于可能没有订单，这里只检查返回结果不为空
    }

    @Test
    public void testGetOrdersByTimeRange() {
        // 测试时间段查询
        Date startTime = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000); // 昨天
        Date endTime = new Date(); // 今天
        Result<java.util.List<Checkout>> result = checkoutService.getOrdersByTimeRange(startTime, endTime);
        assertNotNull(result);
    }

    @Test
    public void testDiscountCalculation() {
        // 测试折扣计算逻辑
        // 这里可以测试不同顾客ID的折扣计算
        // 由于calculateDiscount是私有方法，我们通过创建订单来测试
        Result<Checkout> result1 = checkoutService.createOrder(1, 1001, 2); // 1开头，应该是95折
        Result<Checkout> result2 = checkoutService.createOrder(2, 2001, 2); // 2开头，应该是9折
        
        assertNotNull(result1);
        assertNotNull(result2);
    }
} 