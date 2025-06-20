package com.example.jerry.restaurant.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.jerry.restaurant.pojo.Customer;

@Mapper
public interface CustomerMapper {
    Customer getCustomerByPhone(String phone);
    
    Customer getCustomerById(int id);
    
    void addCustomer(Customer customer);

    void updateCustomer(@Param("customer") Customer customer,@Param("oldphone")String oldphone);

    void deleteCustomer(String phone);

    java.util.List<Customer> getAllCustomers();
}
