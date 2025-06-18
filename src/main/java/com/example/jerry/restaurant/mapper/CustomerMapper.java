package com.example.jerry.restaurant.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.example.jerry.restaurant.pojo.Customer;

@Mapper
public interface CustomerMapper {
    @Select("select * from customer where phone=#{phone}")
    public Customer getCustomerByPhone(String phone);
    
    @Select("select * from customer where customer_id= #{id}")
    public Customer getCustomerById(int id);
    
    @Insert ("insert into customer (customer_id,name,phone,create_time)"+
    " values (#{customer_id},#{name},#{phone},now())")
    public void addCustomer(Customer customer);

    @Update ("update customer set customer_id=#{customer.customer_id},name=#{customer.name},phone=#{customer.phone} where phone=#{oldphone}")
    public void updateCustomer(@Param("customer") Customer customer,@Param("oldphone")String oldphone);

    @Delete("delete from customer where phone =#{phone}")
    public void deleteCustomer(String phone);
}
