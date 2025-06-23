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
import com.example.jerry.restaurant.utils.JwtUtil;

import jakarta.validation.constraints.Pattern;
import org.springframework.web.bind.annotation.GetMapping;

@CrossOrigin(origins = "http://localhost:8081", allowCredentials = "true")
@RestController
@RequestMapping("/customers")
@Validated
public class CustomerController {
    @Autowired
    private CustomerService customerService;
    
    //@Pattern 验证注解问题
//在 CustomerController.java 中，你对 int id 使用了 @Pattern 注解，这是不正确的。@Pattern 注解只能用于 String 类型的字段。
    @PostMapping("/addCustomer")
    public Result<Customer> addCustomer(
        @RequestParam String authenticity,
        @RequestParam @Pattern(regexp = "^[012345].*") String id,  // Change int to String
        @RequestParam @Pattern(regexp = "^\\S{5,32}$") String name,
        @RequestParam @Pattern(regexp = "^\\S{5,16}$") String phone){
        if (JwtUtil.parseToken(authenticity) == null) {
            return Result.error("无效的token，请重新登录");
        }
        Customer existingCustomer = customerService.getCustomerByPhone(phone);
        if (existingCustomer != null) {
            return Result.error("该手机号已被注册");
        } else {
            // 添加新客户
            int customerId = Integer.parseInt(id);  // Convert String to int
            customerService.addCustomer(customerId, name, phone);
            
            // 获取并返回新添加的客户信息
            Customer newCustomer = customerService.getCustomerByPhone(phone);
            return Result.success(newCustomer);
        }
    }

    @PostMapping("/updateCustomer")
    public Result<Customer> updateCustomer(
        @RequestParam String authenticity,
        @RequestParam Integer customerId,
        @RequestParam @Pattern(regexp = "^\\S{5,32}$") String name,
        @RequestParam @Pattern(regexp = "^\\S{5,16}$") String phone,
        @RequestParam @Pattern(regexp = "^\\S{5,16}$") String oldphone){
        if (JwtUtil.parseToken(authenticity) == null) {
            return Result.error("无效的token，请重新登录");
        }
        int Id = (int)customerId;
        Customer updatedCustomer = customerService.getCustomerByPhone(oldphone);
        if(updatedCustomer != null)
        {
            customerService.updateCustomer(Id,name,phone,oldphone);
            Customer customer = customerService.getCustomerByPhone(phone);
            return Result.success(customer);
        }
        else
        {
            return Result.error("更新失败,检查您输入的信息是否正确");
        }
    }

    @DeleteMapping("/deleteCustomer")
    public Result<String> deleteCustomer(
        @RequestParam String authenticity,
        @RequestParam @Pattern(regexp = "^\\S{5,16}$") String phone){
        if (JwtUtil.parseToken(authenticity) == null) {
            return Result.error("无效的token，请重新登录");
        }
        return Result.success(customerService.deleteCustomer(phone));
    }

    @GetMapping("/getCustomerByPhone")
    public Result<Customer> getByPhone(@RequestParam String authenticity, @RequestParam String phone) {
        if (JwtUtil.parseToken(authenticity) == null) {
            return Result.error("无效的token，请重新登录");
        }
        return Result.success(customerService.getCustomerByPhone(phone));
    }
    
    @GetMapping("/all")
    public Result<java.util.List<Customer>> getAllCustomers(@RequestParam String authenticity) {
        if (JwtUtil.parseToken(authenticity) == null) {
            return Result.error("无效的token，请重新登录");
        }
        return Result.success(customerService.getAllCustomers());

        //你好，这里是一个示例代码，用于获取所有客户信息。
    }
}
