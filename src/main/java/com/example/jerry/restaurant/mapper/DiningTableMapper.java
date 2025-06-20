package com.example.jerry.restaurant.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.jerry.restaurant.pojo.DiningTable;

@Mapper
public interface DiningTableMapper {
    void addTable(DiningTable table);
    DiningTable getTableById(Integer tableId);
    void updateStatus(@Param("tableId") Integer tableId, @Param("tableStatus") String tableStatus);
    void deleteTable(Integer tableId);
    int getNumber2();
    int getNumber4();
    int getNumber8();
    int getNumber();
    int getTableBycapacity(Integer capacity);
    Integer getAvailableTableId(Integer capacity);
    java.util.List<DiningTable> getAllTables();
}