package com.example.jerry.restaurant.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.jerry.restaurant.mapper.DiningTableMapper;
import com.example.jerry.restaurant.pojo.DiningTable;
import com.example.jerry.restaurant.service.DiningTableService;

@Service
public class DiningTableServiceImpl implements DiningTableService {
    @Autowired
    private DiningTableMapper diningTableMapper;

    @Override
    public DiningTable getTableById(Integer tableId) {
        return diningTableMapper.getTableById(tableId);
    }

    @Override
    public DiningTable addTable(String tableStatus, Integer capacity) {
        DiningTable table = new DiningTable(tableStatus, capacity);
        diningTableMapper.addTable(table);
        return table;
    }

    @Override
    public DiningTable updateStatus(Integer tableId, String tableStatus) {
        diningTableMapper.updateStatus(tableId, tableStatus);
        return diningTableMapper.getTableById(tableId);
    }

    @Override
    public String deleteTable(Integer tableId) {
        diningTableMapper.deleteTable(tableId);
        return diningTableMapper.getTableById(tableId) == null ? 
            "餐桌删除成功" : "删除失败";
    }
}