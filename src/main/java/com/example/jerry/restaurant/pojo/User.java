package com.example.jerry.restaurant.pojo;

public class User {
    private String username;
    private String password;

    // 无参构造器
    public User() {
    }

    // 带参构造器
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getter和Setter方法
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {  // ✅ 正确命名
    this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // toString方法
    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}