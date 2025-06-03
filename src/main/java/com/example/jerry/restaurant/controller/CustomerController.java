package com.example.jerry.restaurant.controller;

import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.jerry.restaurant.pojo.Customer;
import com.example.jerry.restaurant.pojo.Result;
import com.example.jerry.restaurant.service.CustomerService;

import jakarta.validation.constraints.Pattern;
import org.springframework.web.bind.annotation.GetMapping;

@CrossOrigin(origins = "http://localhost:8081", allowCredentials = "true")
@RestController
@RequestMapping("/customers")
@Validated
public class CustomerController {
    @Autowired
    private CustomerService customerService;
    
    @PostMapping("/addCustomer")
    public Result<Customer> addCustomer(
        @RequestParam @Pattern(regexp = "^[12].*") String id,
        @RequestParam @Pattern(regexp = "^\\S{5,16}$") String name,
        @RequestParam @Pattern(regexp = "^\\S{5,16}$") String phone){
        Customer existingCustomer = customerService.getCustomerByPhone(phone);
        if (existingCustomer != null) {
            return Result.error("该手机号已被注册");
        } else {
            // 添加新客户
            customerService.addCustomer(id, name, phone);
            
            // 获取并返回新添加的客户信息
            Customer newCustomer = customerService.getCustomerByPhone(phone);
            return Result.success(newCustomer);
        }
    }

    @PostMapping("/updateCustomer")
    public Result<Customer> updateCustomer(
        @RequestParam @Pattern(regexp = "^\\S{9}$") String id,
        @RequestParam @Pattern(regexp = "^\\S{5,16}$") String name,
        @RequestParam @Pattern(regexp = "^\\S{5,16}$") String phone,
        @RequestParam @Pattern(regexp = "^\\S{5,16}$") String oldphone){

            Customer existingCustomer = customerService.getCustomerById(phone);
            Customer updatedCustomer = customerService.getCustomerByPhone(oldphone);
            Customer newcustomer = customerService.getCustomerByPhone(phone);
            if(updatedCustomer != null && newcustomer == null&& existingCustomer == null)
            {
                customerService.updateCustomer(id, name, phone,oldphone);
                Customer customer = customerService.getCustomerByPhone(phone);
                return Result.success(customer);
            }else
            {
                return Result.error("更新失败,你输入的手机号或者ID可能已经被注册");
            }
    }

    @DeleteMapping("/deleteCustomer")
    public Result<String> deleteCustomer(
        @RequestParam @Pattern(regexp = "^\\S{5,16}$") String phone){
        return Result.success(customerService.deleteCustomer(phone));
    }

    @GetMapping("/getCustomerByPhone")
    public Result<Customer> getByPhone(@RequestParam String phone) {
        return Result.success(customerService.getCustomerByPhone(phone));
    }
    
}
