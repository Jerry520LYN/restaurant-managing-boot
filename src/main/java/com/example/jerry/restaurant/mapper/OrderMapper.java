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
    OrderSummary getOrderById(int orderId);
    Order getOrderByNumber(String orderNumber);
    void updateOrder(int orderId, int customerId, int tableId, BigDecimal totalAmount, BigDecimal discount, BigDecimal finalAmount);
    int addOrder(Order order);
    void deleteOrder(int orderId);
    int updateOrderStatus(int orderId, String status);
    int checkoutOrder(int orderId, BigDecimal totalAmount, BigDecimal discount, BigDecimal finalAmount);
    List<OrderSummary> getOrdersByTimeRange(Date startTime, Date endTime);
    List<Order> getOrdersByTimeRangeAndStatus(Date startTime, Date endTime, String status);
    int getOrderCount();
    List<Map<String, Object>> getPopularDishes(Date startTime, Date endTime);
    Map<String, Object> getRevenueByPeriod(Date startTime, Date endTime);
    List<Map<String, Object>> getOrderIdWithDishId(int tableId);
    List<OrderSummary> getAllOrderSummaries();
}
