package com.example.jerry.restaurant.mapper;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.example.jerry.restaurant.pojo.Order;
import com.example.jerry.restaurant.pojo.OrderSummary;

@Mapper
public interface OrderMapper {

    List<Order> getAllOrders();
    Order getOrderById(int orderId);
    
    Order getOrderByNumber(String orderNumber);
    
    void updateOrder(int orderId, int customerId, int tableId, BigDecimal totalAmount, BigDecimal discount, BigDecimal finalAmount);

    int addOrder(Order order);

    void deleteOrder(int orderId);
    
    int updateOrderStatus(int orderId, String status);
    
    int checkoutOrder(int orderId, BigDecimal totalAmount, BigDecimal discount, BigDecimal finalAmount);
    
    List<Order> getOrdersByTimeRange(Date startTime, Date endTime);
    
    List<Order> getOrdersByTimeRangeAndStatus(Date startTime, Date endTime, String status);
    int getOrderCount();
    
    // 调用存储过程获取最受欢迎菜品
    List<Map<String, Object>> getPopularDishes(Date startTime, Date endTime);
    
    // 调用存储过程获取营收统计
    Map<String, Object> getRevenueByPeriod(Date startTime, Date endTime);

    List<Map<String, Object>> getOrderIdWithDishId(int tableId);
    
    // 查询order_summary视图获取所有订单摘要信息
    List<OrderSummary> getAllOrderSummaries();
}
