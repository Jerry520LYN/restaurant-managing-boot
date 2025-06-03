package com.example.jerry.restaurant.mapper;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.example.jerry.restaurant.pojo.order;

@Mapper
public interface OrderMapper {

    @Select("select * from orders where order_id=#{orderId}")
    order getOrderById(int orderId);
    @Update("update orders set customer_id=#{customerId},table_id=#{tableId},total_amount=#{totalAmount} where order_id=#{orderId}")
    void updateOrder(int orderId, int customerId, int tableId, BigDecimal totalAmount);

    @Insert("insert into orders (order_id,customer_id,table_id,total_amount) values (#{orderId},#{customerId},#{tableId},#{totalAmount})")
    @Options(useGeneratedKeys = true, keyProperty = "orderId")
    int addOrder(order order);

    @Delete("delete from orders where order_id= #{orderId}")
    void deleteOrder(int orderId);
}
