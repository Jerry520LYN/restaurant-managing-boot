package com.example.jerry.restaurant.service.impl;

import java.time.LocalDateTime;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import com.example.jerry.restaurant.mapper.DiningTableMapper;
import com.example.jerry.restaurant.pojo.CallingNumber;
import com.example.jerry.restaurant.service.CallingNumberService;

@Service
public class CallingNumberServelmpl implements CallingNumberService{
    @Autowired
    private DiningTableMapper  DiningTableMapper;
    private final Queue<Integer> queue = new ConcurrentLinkedQueue<>();
    private final Queue<CallingNumber> CallingQueue= new ConcurrentLinkedQueue<>();
    private final Queue<Integer> CallingIdQueue= new ConcurrentLinkedQueue<>();
    @Override
    public CallingNumber getResult(int  peoplenumber) {
        int i=0;
        if(peoplenumber > 0){
        queue.add(peoplenumber);
        CallingIdQueue.add(i);
        i++;
        }
        else
        return null;
        LocalDateTime now = LocalDateTime.now();
        //如果剩余的餐桌数量大于0
        //获取当前的时间
        //如果现在是中午或者晚上六点到十二点，那么就要返回实例化的CallingNumber对象
            //根据peoplenumber来判断需要返回哪一个餐桌ID
            //根据空余餐桌的数量来大致判断等待时间
            //全部获取到了之后再实例化对象返回
            if(now.getHour()>=11&&now.getHour()<=14 || now.getHour()>=18&&now.getHour()<=23)
            {
                if(peoplenumber<=2&&peoplenumber>=1)
                {
                    int tableId = DiningTableMapper.getNumber2();
                    if (tableId>0)
                    {
                        int Id = CallingIdQueue.peek();
                        int Number2 = DiningTableMapper.getNumber2();//获取空餐桌的数量
                        int peoplenumberbefore = 0;
                        for(int value:queue){
                            peoplenumberbefore += value;
                        }
                        CallingNumber callingNumber = new CallingNumber(Id,Number2, tableId, now,peoplenumberbefore);
                        CallingQueue.add(callingNumber);
                        return callingNumber;
                    }
                    
                }
                else if(peoplenumber<=4&&peoplenumber>=2)
                {
                    int tableId = DiningTableMapper.getNumber4();
                    if(tableId>0){
                        int Id = CallingIdQueue.peek();
                        int Number2 = DiningTableMapper.getNumber2();//获取空餐桌的数量
                        int peoplenumberbefore = 0;
                        for(int value:queue){
                            peoplenumberbefore += value;
                        }
                        CallingNumber callingNumber = new CallingNumber(Id,Number2, tableId, now,peoplenumberbefore);
                        CallingQueue.add(callingNumber);
                        return callingNumber;
                    }
                    
                }
                else if(peoplenumber<=8&&peoplenumber>=4)
                {
                    int tableId = DiningTableMapper.getNumber8();
                    if(tableId>0){
                        int Id = CallingIdQueue.peek();
                        int Number2 = DiningTableMapper.getNumber2();//获取空餐桌的数量
                        int peoplenumberbefore = 0;
                        for(int value:queue){
                            peoplenumberbefore += value;
                        }
                        CallingNumber callingNumber = new CallingNumber(Id,Number2, tableId, now,peoplenumberbefore);
                        CallingQueue.add(callingNumber);
                        return callingNumber;
                    }
                    
                }
                else{
                    return null;
                }    
                return null;    
        }
        
    }
}
