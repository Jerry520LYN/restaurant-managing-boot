package com.example.jerry.restaurant.service;

import java.util.List;
import java.util.Map;
import com.example.jerry.restaurant.pojo.CallingNumber;

public interface CallingNumberService {

    // 新增叫号
    CallingNumber addCallingNumber(String phone, int peopleNumber);
    // 删除叫号
    boolean removeCallingNumber(int callingNumberId);
    // 修改叫号状态
    boolean updateCallingNumberStatus(int callingNumberId, String status);
    // 获取所有队列状态
    Map<String, List<CallingNumber>> getAllQueues();

}
