package com.example.jerry.restaurant.mapper;

import com.example.jerry.restaurant.pojo.Menu;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MenuMapper {

    @Select("SELECT * FROM menu WHERE dish_id = #{dishId}")
    Menu getMenuById(String dishId);

    @Select("SELECT * FROM menu WHERE dish_id = #{dishId}")
    Menu getDishById(int dishId);

    @Insert("INSERT INTO menu (dish_id, dish_name, price, description, image_url) VALUES (#{dishId}, #{dishName}, #{price}, #{description}, #{imageUrl})")
    void addMenu(Menu menu);

    @Update("UPDATE menu SET dish_name=#{dishName}, price=#{price}, description=#{description}, image_url=#{imageUrl} WHERE dish_id=#{dishId}")
    void updateMenu(Menu menu);

    @Delete("DELETE FROM menu WHERE dish_id = #{dishId}")
    void deleteMenu(String dishId);

    @Select("SELECT * FROM menu")
    List<Menu> getAllMenus();
}