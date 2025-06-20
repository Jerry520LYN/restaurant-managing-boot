package com.example.jerry.restaurant.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.example.jerry.restaurant.pojo.OrderDetail;

@Mapper
public interface OrderDetailMapper {

    java.util.List<OrderDetail> getOrderDetailsByOrderId(int orderId);

    java.util.List<OrderDetail> getOrderDetailsWithDishInfo(int orderId);

    int addOrderDetail(OrderDetail orderDetail);

    int updateOrderDetailQuantity(int detailId, int quantity);

    int deleteOrderDetail(int detailId);

    int deleteOrderDetailByOrderAndDish(int orderId, int dishId);

    int checkOrderDetailExists(int orderId, int dishId);

    java.util.List<OrderDetail> getAllOrderDetails();
} 