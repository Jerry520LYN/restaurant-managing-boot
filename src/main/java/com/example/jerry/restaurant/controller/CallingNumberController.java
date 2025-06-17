package com.example.jerry.restaurant.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.example.jerry.restaurant.pojo.CallingNumber;
import com.example.jerry.restaurant.pojo.DiningTable;
import com.example.jerry.restaurant.pojo.Result;
import com.example.jerry.restaurant.service.CallingNumberService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/callingnumber")
@RestController
public class CallingNumberController {
    @Autowired
    private CallingNumberService callingNumberService;

    @PostMapping("/getcallingnumber")
    public Result<CallingNumber> getMethodName(@RequestParam int peoplenumber) {

        if(callingNumberService.getResult(peoplenumber)==null)
            return Result.error("无需叫号");
        else{
            CallingNumber callingNumber = callingNumberService.getResult(peoplenumber);
        return Result.success(callingNumber);
        }
    }

    @GetMapping("/getcallingnumberlist")
    public Result<List<CallingNumber>> getMethodName() {
        return Result.success(callingNumberService.getResultList());
    }
}
