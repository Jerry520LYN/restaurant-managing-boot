package com.example.jerry.restaurant.service;

import com.example.jerry.restaurant.pojo.Customer;

public interface CustomerService {
    Customer  getCustomerByPhone(String phone);

    Customer  addCustomer(String id,String name, String phone);
    
    Customer  updateCustomer(String id,String name, String phone,String oldphone);

    String deleteCustomer(String phone);
}