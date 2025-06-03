package com.example.jerry.restaurant.service;

import com.example.jerry.restaurant.pojo.User;

public interface UserService {
    User getUserByUsername(String username);
    User login(String username, String password);

    User register(String username, String password);
}
