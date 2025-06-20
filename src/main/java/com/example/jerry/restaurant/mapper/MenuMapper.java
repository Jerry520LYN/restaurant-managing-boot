package com.example.jerry.restaurant.mapper;

import com.example.jerry.restaurant.pojo.Menu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MenuMapper {

    Menu getMenuById(String dishId);

    Menu getDishById(int dishId);

    void addMenu(Menu menu);

    void updateMenu(Menu menu);

    void deleteMenu(String dishId);

    List<Menu> getAllMenus();
}