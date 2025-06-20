package com.example.jerry.restaurant.controller;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.jerry.restaurant.pojo.CallingNumber;
import com.example.jerry.restaurant.pojo.Result;
import com.example.jerry.restaurant.service.CallingNumberService;
import com.example.jerry.restaurant.utils.JwtUtil;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/callingnumber")
@RestController
public class CallingNumberController {
    @Autowired
    private CallingNumberService callingNumberService;

    // 叫号
    @PostMapping("/add")
    public Result<CallingNumber> add(@RequestParam String authenticity, @RequestParam String phone, @RequestParam int peopleNumber) {
        if (JwtUtil.parseToken(authenticity) == null) {
            return Result.error("无效的token，请重新登录");
        }
        CallingNumber callingNumber = callingNumberService.addCallingNumber(phone, peopleNumber);
        if (callingNumber == null)
            return Result.error("叫号失败");
        else
            return Result.success(callingNumber);
    }

    // 删除叫号
    @PostMapping("/remove")
    public Result<String> remove(@RequestParam String authenticity, @RequestParam int callingNumberId) {
        if (JwtUtil.parseToken(authenticity) == null) {
            return Result.error("无效的token，请重新登录");
        }
        boolean ok = callingNumberService.removeCallingNumber(callingNumberId);
        return ok ? Result.success("删除成功") : Result.error("删除失败");
    }

    // 修改叫号状态
    @PostMapping("/updateStatus")
    public Result<String> updateStatus(@RequestParam String authenticity, @RequestParam int callingNumberId, @RequestParam String status) {
        if (JwtUtil.parseToken(authenticity) == null) {
            return Result.error("无效的token，请重新登录");
        }
        boolean ok = callingNumberService.updateCallingNumberStatus(callingNumberId, status);
        return ok ? Result.success("状态更新成功") : Result.error("状态更新失败");
    }

    // 查询所有队列
    @GetMapping("/queues")
    public Result<Map<String, List<CallingNumber>>> getAllQueues(@RequestParam String authenticity) {
        if (JwtUtil.parseToken(authenticity) == null) {
            return Result.error("无效的token，请重新登录");
        }
        return Result.success(callingNumberService.getAllQueues());
    }
}
