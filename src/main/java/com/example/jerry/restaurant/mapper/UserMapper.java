package com.example.jerry.restaurant.mapper;

import com.example.jerry.restaurant.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    User findByUsername(String username);
    int insert(User user);
}
