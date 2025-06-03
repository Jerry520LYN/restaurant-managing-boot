package com.example.jerry.restaurant.service.impl;

import com.example.jerry.restaurant.mapper.UserMapper;
import com.example.jerry.restaurant.pojo.User;
import com.example.jerry.restaurant.service.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class UserServeImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User getUserByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    @Override
    public User login(String username, String password) {
        User user = userMapper.findByUsername(username);
        if (user != null && user.getPassword().equals(DigestUtils.md5Hex(password))) {
            return user;
        }
        return null;
    }

    @Override
    public User register(String username, String password) {
       String md5Password = DigestUtils.md5Hex(password);
       User newUser = new User(username, md5Password);
        userMapper.insert(newUser);
        return newUser;
    }
}