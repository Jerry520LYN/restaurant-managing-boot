package com.example.jerry.restaurant.controller;

import com.example.jerry.restaurant.pojo.Menu;
import com.example.jerry.restaurant.pojo.Result;
import com.example.jerry.restaurant.service.MenuService;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/menus")
@Validated
@CrossOrigin(origins = "http://localhost:8081", allowCredentials = "true")
public class MenuController {

    @Autowired
    private MenuService menuService;

    // 添加菜品
    @PostMapping("/addMenu")
    public Result<Menu> addMenu(
            @RequestParam @Pattern(regexp = "^\\S{5,20}$") String dishId,
            @RequestParam @Pattern(regexp = "^\\S{1,50}$") String dishName,
            @RequestParam @Pattern(regexp = "^\\d+(\\.\\d{1,2})?$") String price,
            @RequestParam String description) {

        Menu existingMenu = menuService.getMenuById(dishId);
        if (existingMenu != null) {
            return Result.error("该菜品ID已存在");
        }

        Menu addedMenu = menuService.addMenu(dishId, dishName, price, description);
        return Result.success(addedMenu);
    }

    // 修改菜品
    @PostMapping("/updateMenu")
    public Result<Menu> updateMenu(
            @RequestParam @Pattern(regexp = "^\\S{5,20}$") String dishId,
            @RequestParam @Pattern(regexp = "^\\S{1,50}$") String dishName,
            @RequestParam @Pattern(regexp = "^\\d+(\\.\\d{1,2})?$") String price,
            @RequestParam String description) {

        Menu updatedMenu = menuService.updateMenu(dishId, dishName, price, description);
        if (updatedMenu != null) {
            return Result.success(updatedMenu);
        } else {
            return Result.error("更新失败，请检查菜品ID是否正确");
        }
    }

    // 删除菜品
    @DeleteMapping("/deleteMenu")
    public Result<String> deleteMenu(@RequestParam String dishId) {
        String result = menuService.deleteMenu(dishId);
        return Result.success(result);
    }

    // 根据菜品ID查询
    @GetMapping("/getMenuById")
    public Result<Menu> getMenuById(@RequestParam String dishId) {
        Menu menu= menuService.getMenuById(dishId);
        if(menu == null)
        return Result.error("请再自己看看哦");
        else
        return Result.success(menu);
    }
}