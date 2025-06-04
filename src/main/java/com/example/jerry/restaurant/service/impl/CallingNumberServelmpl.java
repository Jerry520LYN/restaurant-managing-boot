package com.example.jerry.restaurant.service.impl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.jerry.restaurant.mapper.DiningTableMapper;
import com.example.jerry.restaurant.pojo.CallingNumber;
import com.example.jerry.restaurant.pojo.DiningTable;
import com.example.jerry.restaurant.service.CallingNumberService;

@Service
public class CallingNumberServelmpl implements CallingNumberService{
    @Autowired
    private DiningTableMapper  DiningTableMapper;


    @Override
    public CallingNumber getResult(int  peoplenumber) {
        int number = DiningTableMapper.getNumber();
        LocalDateTime now = LocalDateTime.now();
        
        //如果剩余的餐桌数量大于0
        //获取当前的时间
        //如果现在是中午或者晚上六点到十二点，那么就要返回实例化的CallingNumber对象
            //根据人数来判断需要返回哪一个餐桌ID
            //根据空余餐桌的数量来大致判断等待时间
            //全部获取到了之后再实例化对象返回

        if(number>0){

            if(now.getHour()>=11&&now.getHour()<=14 || now.getHour()>=18&&now.getHour()<=23)
            {
                if(peoplenumber<=2&&number>=1)
                {
                    int tableId = DiningTableMapper.getNumber2();
                    if (tableId>0)
                    {
                        CallingNumber callingNumber = new CallingNumber(number, tableId, now);
                        return callingNumber;
                    }
                    
                }
                else if(peoplenumber<=4&&number>=2)
                {
                    int tableId = DiningTableMapper.getNumber4();
                    if(tableId>0){
                        CallingNumber callingNumber = new CallingNumber(number, tableId, now);
                        return callingNumber;
                    }
                    }
                    
                }
                else if(peoplenumber<=8&&number>=4)
                {
                    int tableId = DiningTableMapper.getNumber8();
                    if(tableId>0){
                        CallingNumber callingNumber = new CallingNumber(number, tableId, now);
                        return callingNumber;
                    }
                    
                }
                else{
                    return null;
                }
            }else{
            return null;
        }
        
        }
        
        //否则返回null，由顾客自己选择餐桌
    }

}
