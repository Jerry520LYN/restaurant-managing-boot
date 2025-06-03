package com.example.jerry.restaurant.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.jerry.restaurant.mapper.CustomerMapper;
import com.example.jerry.restaurant.pojo.Customer;
import com.example.jerry.restaurant.service.CustomerService;

@Service
public class CustomerServelmpl implements CustomerService {

    @Autowired
    private CustomerMapper customerMapper;

    @Override
    public Customer getCustomerByPhone(String phone) {
        return customerMapper.getCustomerByPhone(phone);
    }

    @Override
    public Customer getCustomerById(String id) {
        return customerMapper.getCustomerById(id);
    }

    @Override
    public Customer addCustomer(String id,String name, String phone) {
        Customer  customer = new Customer(id,name, phone);
        customerMapper.addCustomer(customer);
        return customerMapper.getCustomerByPhone(phone);
    }
    
    @Override
    public Customer updateCustomer(String id,String name, String phone,String oldphone) {
        Customer  customer = new Customer(id,name, phone);
        customerMapper.updateCustomer(customer,oldphone);
        return customerMapper.getCustomerByPhone(phone);
    }

    @Override
    public String deleteCustomer(String phone) {
        customerMapper.deleteCustomer(phone);
        Customer customer = customerMapper.getCustomerByPhone(phone);
        if(customer == null)
        return "用户已被移除";
        else
        return "删除失败";
    }
}
