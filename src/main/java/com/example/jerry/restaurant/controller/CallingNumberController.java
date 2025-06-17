package com.example.jerry.restaurant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.example.jerry.restaurant.pojo.CallingNumber;
import com.example.jerry.restaurant.pojo.DiningTable;
import com.example.jerry.restaurant.pojo.Result;
import com.example.jerry.restaurant.service.CallingNumberService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/callingnumber")
@Controller
public class CallingNumberController {
    @Autowired
    private CallingNumberService callingNumberService;

    @GetMapping("/getcallingnumber")
    public Result<DiningTable> getMethodName(@RequestParam int peoplenumber) {

        if()
            return Result.error("无需叫号");
        else{

        return Result.success(1);
        }
    }
    
}
