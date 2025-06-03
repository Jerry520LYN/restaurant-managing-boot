package com.example.jerry.restaurant.service;

import com.example.jerry.restaurant.pojo.Menu;

public interface MenuService {
    Menu getMenuById(String dishId);
    Menu addMenu(String dishId, String dishName, String price, String description);
    Menu updateMenu(String dishId, String dishName, String price, String description);
    String deleteMenu(String dishId);
}