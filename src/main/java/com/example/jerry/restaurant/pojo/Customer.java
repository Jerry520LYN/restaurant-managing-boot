package com.example.jerry.restaurant.pojo;

import java.sql.Date;

public class Customer {
    private String customer_id;
    private String name;
    private String phone;
    private Date create_time;

    public Customer() {
    }

    public Customer(String customerId, String name, String phone) {
        this.customer_id = customerId;
        this.name = name;
        this.phone = phone;
    }

    public String getCustomerId() {
        return customer_id;
    }

    public void setCustomerId(String customerId) {
        this.customer_id = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getCreateTime() {
        return create_time;
    }

    public void setCreateTime(Date createTime) {
        this.create_time = createTime;
    }

    // toString 方法
    @Override
    public String toString() {
        return "Customer{" +
                "customerId=" + customer_id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", createTime=" + create_time +
                '}';
    }
}
