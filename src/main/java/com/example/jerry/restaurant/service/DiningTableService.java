package com.example.jerry.restaurant.service;

import com.example.jerry.restaurant.pojo.DiningTable;

public interface DiningTableService {
    DiningTable addTable(String tableStatus, Integer capacity);
    DiningTable updateStatus(Integer tableId, String tableStatus);
    String deleteTable(Integer tableId);

    DiningTable getTableById(Integer tableId);

    java.util.List<DiningTable> getAllTables();
}