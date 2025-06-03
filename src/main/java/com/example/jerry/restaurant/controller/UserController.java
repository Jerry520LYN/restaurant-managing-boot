package com.example.jerry.restaurant.controller;

import com.example.jerry.restaurant.pojo.Result;
import com.example.jerry.restaurant.pojo.User;
import com.example.jerry.restaurant.service.UserService;
import com.example.jerry.restaurant.utils.JwtUtil;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.constraints.Pattern;
import java.util.HashMap;
@CrossOrigin(origins = "http://localhost:8081", allowCredentials = "true")
@RestController
@RequestMapping("/users")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/{username}")
    public User getUser(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }

    @PostMapping("/login")
    public Result<String> login(
            @RequestParam @Pattern(regexp = "^\\S{1,16}$") String username,
            @RequestParam @Pattern(regexp = "^\\S{5,16}$") String password) {
        User user = userService.login(username, password);
        if (user != null) {
            Map<String, Object> claims = new  HashMap<>();
            claims.put("username", user.getUsername());
            claims.put("password", user.getPassword());
            String token = jwtUtil.genToken(claims);
            return Result.success(token);
        } else {
            return Result.error("用户名或密码错误");
        }
    }

    @PostMapping("/register")
    public Result<String> register(
            
            @RequestParam @Pattern(regexp = "^\\S{1,16}$") String username,
            @RequestParam @Pattern(regexp = "^\\S{5,16}$") String password) { 
            User user = userService.getUserByUsername(username);
            if(user !=  null)
            return Result.error("用户已存在");
            else
                userService.register(username, password);
                return Result.success("注册成功");
    }
}