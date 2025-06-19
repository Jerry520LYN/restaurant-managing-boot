package com.example.jerry.restaurant.controller;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.jerry.restaurant.pojo.CallingNumber;
import com.example.jerry.restaurant.pojo.Result;
import com.example.jerry.restaurant.service.CallingNumberService;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/callingnumber")
@RestController
public class CallingNumberController {
    @Autowired
    private CallingNumberService callingNumberService;

    // 叫号
    @PostMapping("/add")
    public Result<CallingNumber> add(@RequestParam String phone, @RequestParam int peopleNumber) {
        CallingNumber callingNumber = callingNumberService.addCallingNumber(phone, peopleNumber);
        if (callingNumber == null)
            return Result.error("叫号失败");
        else
            return Result.success(callingNumber);
    }

    // 删除叫号
    @PostMapping("/remove")
    public Result<String> remove(@RequestParam int callingNumberId) {
        boolean ok = callingNumberService.removeCallingNumber(callingNumberId);
        return ok ? Result.success("删除成功") : Result.error("删除失败");
    }

    // 修改叫号状态
    @PostMapping("/updateStatus")
    public Result<String> updateStatus(@RequestParam int callingNumberId, @RequestParam String status) {
        boolean ok = callingNumberService.updateCallingNumberStatus(callingNumberId, status);
        return ok ? Result.success("状态更新成功") : Result.error("状态更新失败");
    }

    // 查询所有队列
    @GetMapping("/queues")
    public Result<Map<String, List<CallingNumber>>> getAllQueues() {
        return Result.success(callingNumberService.getAllQueues());
    }
}
