package com.example.jerry.restaurant.service;

import com.example.jerry.restaurant.pojo.Customer;

public interface CustomerService {
    Customer  getCustomerByPhone(String phone);

    Customer  addCustomer(int id,String name, String phone);
    
    Customer  updateCustomer(int id,String name, String phone,String oldphone);

    String deleteCustomer(String phone);

    Customer  getCustomerById(int id);

    java.util.List<Customer> getAllCustomers();
}