package com.example.jerry.restaurant.mapper;

import com.example.jerry.restaurant.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
@Mapper
public interface UserMapper {
    @Select("select * from user where username=#{username}")
    public User findByUsername(String username);

    @Insert("insert into user (username,password) values (#{username},#{password})")
    public int insert(User user);
}
