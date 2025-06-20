package com.example.jerry.restaurant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.example.jerry.restaurant.pojo.DiningTable;
import com.example.jerry.restaurant.pojo.Result;
import com.example.jerry.restaurant.service.DiningTableService;
import com.example.jerry.restaurant.utils.JwtUtil;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;

@Validated
@RestController
@RequestMapping("/tables")
@CrossOrigin(origins = "http://localhost:8081", allowCredentials = "true")
public class DiningTableController {
    @Autowired
    private DiningTableService diningTableService;

    @PostMapping("/add")
    public Result<DiningTable> addTable(
        @RequestParam String authenticity,
        @RequestParam @Pattern(regexp = "^(空|占用)$") String tableStatus,
        @RequestParam @Min(1) Integer capacity) {
        if (JwtUtil.parseToken(authenticity) == null) {
            return Result.error("无效的token，请重新登录");
        }
        return Result.success(diningTableService.addTable(tableStatus, capacity));
    }

    @PostMapping("/updateStatus")
    public Result<DiningTable> updateStatus(
        @RequestParam String authenticity,
        @RequestParam @Min(1) Integer tableId,
        @RequestParam @Pattern(regexp = "^(空|占用)$") String tableStatus) {
        if (JwtUtil.parseToken(authenticity) == null) {
            return Result.error("无效的token，请重新登录");
        }
        return Result.success(diningTableService.updateStatus(tableId, tableStatus));
    }

    @DeleteMapping("/delete")
    public Result<String> deleteTable(
        @RequestParam String authenticity,
        @RequestParam @Min(1) Integer tableId) {
        if (JwtUtil.parseToken(authenticity) == null) {
            return Result.error("无效的token，请重新登录");
        }
        return Result.success(diningTableService.deleteTable(tableId));
    }

    @GetMapping("/getById")
    public Result<DiningTable> getTableById(
        @RequestParam String authenticity,
        @RequestParam @Min(1) Integer tableId) {
        if (JwtUtil.parseToken(authenticity) == null) {
            return Result.error("无效的token，请重新登录");
        }
        return Result.success(diningTableService.getTableById(tableId));
    }

    @GetMapping("/all")
    public Result<java.util.List<DiningTable>> getAllTables(@RequestParam String authenticity) {
        if (JwtUtil.parseToken(authenticity) == null) {
            return Result.error("无效的token，请重新登录");
        }
        return Result.success(diningTableService.getAllTables());
    }
}