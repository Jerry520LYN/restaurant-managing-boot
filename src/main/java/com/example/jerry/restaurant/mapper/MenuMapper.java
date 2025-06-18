package com.example.jerry.restaurant.mapper;

import com.example.jerry.restaurant.pojo.Menu;
import org.apache.ibatis.annotations.*;

@Mapper
public interface MenuMapper {

    @Select("SELECT * FROM menu WHERE dish_id = #{dishId}")
    Menu getMenuById(String dishId);

    @Select("SELECT * FROM menu WHERE dish_id = #{dishId}")
    Menu getDishById(int dishId);

    @Insert("INSERT INTO menu (dish_id, dish_name, price, description) VALUES (#{dishId}, #{dishName}, #{price}, #{description})")
    void addMenu(Menu menu);

    @Update("UPDATE menu SET dish_name=#{dishName}, price=#{price}, description=#{description} WHERE dish_id=#{dishId}")
    void updateMenu(Menu menu);

    @Delete("DELETE FROM menu WHERE dish_id = #{dishId}")
    void deleteMenu(String dishId);
}