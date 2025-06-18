package com.example.jerry.restaurant.mapper;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    
    @Select("select * from orders where order_number=#{orderNumber}")
    order getOrderByNumber(String orderNumber);
    
    @Update("update orders set customer_id=#{customerId},table_id=#{tableId},total_amount=#{totalAmount} where order_id=#{orderId}")
    void updateOrder(int orderId, int customerId, int tableId, BigDecimal totalAmount);

    @Insert("insert into orders (order_id,customer_id,table_id,total_amount) values (#{orderId},#{customerId},#{tableId},#{totalAmount})")
    @Options(useGeneratedKeys = true, keyProperty = "orderId")
    int addOrder(order order);

    @Delete("delete from orders where order_id= #{orderId}")
    void deleteOrder(int orderId);
    
    @Update("UPDATE orders SET status = #{status} WHERE order_id = #{orderId}")
    int updateOrderStatus(int orderId, String status);
    
    @Update("UPDATE orders SET total_amount = #{totalAmount}, status = '已结账' WHERE order_id = #{orderId}")
    int checkoutOrder(int orderId, BigDecimal totalAmount);
    
    @Select("SELECT * FROM orders WHERE order_time BETWEEN #{startTime} AND #{endTime}")
    List<order> getOrdersByTimeRange(Date startTime, Date endTime);
    
    @Select("SELECT COUNT(*) FROM orders")
    int getOrderCount();
    
    // 调用存储过程获取最受欢迎菜品
    @Select("CALL get_popular_dishes(#{startTime}, #{endTime})")
    List<Map<String, Object>> getPopularDishes(Date startTime, Date endTime);
    
    // 调用存储过程获取营收统计
    @Select("CALL get_revenue_by_period(#{startTime}, #{endTime})")
    Map<String, Object> getRevenueByPeriod(Date startTime, Date endTime);
}
