package com.example.jerry.restaurant.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import com.example.jerry.restaurant.pojo.OrderDetail;

@Mapper
public interface OrderDetailMapper {

    @Select("SELECT * FROM order_detail WHERE order_id = #{orderId}")
    List<OrderDetail> getOrderDetailsByOrderId(int orderId);

    @Select("SELECT od.*, m.dish_name, m.price FROM order_detail od " +
            "JOIN menu m ON od.dish_id = m.dish_id " +
            "WHERE od.order_id = #{orderId}")
    List<OrderDetail> getOrderDetailsWithDishInfo(int orderId);

    @Insert("INSERT INTO order_detail (order_id, dish_id, quantity) VALUES (#{orderId}, #{dishId}, #{quantity})")
    @Options(useGeneratedKeys = true, keyProperty = "detailId")
    int addOrderDetail(OrderDetail orderDetail);

    @Update("UPDATE order_detail SET quantity = #{quantity} WHERE detail_id = #{detailId}")
    int updateOrderDetailQuantity(int detailId, int quantity);

    @Delete("DELETE FROM order_detail WHERE detail_id = #{detailId}")
    int deleteOrderDetail(int detailId);

    @Delete("DELETE FROM order_detail WHERE order_id = #{orderId} AND dish_id = #{dishId}")
    int deleteOrderDetailByOrderAndDish(int orderId, int dishId);

    @Select("SELECT COUNT(*) FROM order_detail WHERE order_id = #{orderId} AND dish_id = #{dishId}")
    int checkOrderDetailExists(int orderId, int dishId);

    @Select("SELECT * FROM order_detail")
    List<OrderDetail> getAllOrderDetails();
} 