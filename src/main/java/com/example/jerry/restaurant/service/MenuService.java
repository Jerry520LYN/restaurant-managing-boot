package com.example.jerry.restaurant.service;

import com.example.jerry.restaurant.pojo.Menu;
import java.util.List;

public interface MenuService {
    Menu getMenuById(String dishId);
    Menu addMenu(String dishId, String dishName, String price, String description, String imageUrl);
    Menu updateMenu(String dishId, String dishName, String price, String description, String imageUrl);
    String deleteMenu(String dishId);
    List<Menu> getAllMenus();
}