package com.example.jerry.restaurant.service.impl;

import com.example.jerry.restaurant.mapper.MenuMapper;
import com.example.jerry.restaurant.pojo.Menu;
import com.example.jerry.restaurant.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public Menu getMenuById(String dishId) {
        return menuMapper.getMenuById(dishId);
    }

    @Override
    public Menu addMenu(String dishId, String dishName, String price, String description) {
        Menu menu = new Menu(dishId, dishName, new BigDecimal(price), description);
        menuMapper.addMenu(menu);
        return menuMapper.getMenuById(dishId);
    }

    @Override
    public Menu updateMenu(String dishId, String dishName, String price, String description) {
        Menu menu = new Menu(dishId, dishName, new BigDecimal(price), description);
        menuMapper.updateMenu(menu);
        return menuMapper.getMenuById(dishId);
    }

    @Override
    public String deleteMenu(String dishId) {
        menuMapper.deleteMenu(dishId);
        if (menuMapper.getMenuById(dishId) == null) {
            return "菜品已被移除";
        } else {
            return "删除失败";
        }
    }
}