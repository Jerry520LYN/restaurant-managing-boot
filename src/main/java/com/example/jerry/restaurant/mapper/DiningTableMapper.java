package com.example.jerry.restaurant.mapper;

import org.apache.ibatis.annotations.*;

import com.example.jerry.restaurant.pojo.DiningTable;

@Mapper
public interface DiningTableMapper {
    @Insert("INSERT INTO dining_table (table_status, capacity) VALUES (#{tableStatus}, #{capacity})")
    @Options(useGeneratedKeys = true, keyProperty = "tableId")
    void addTable(DiningTable table);

    @Select("SELECT * FROM dining_table WHERE table_id = #{tableId}")
    DiningTable getTableById(Integer tableId);

    @Update("UPDATE dining_table SET table_status = #{tableStatus} WHERE table_id = #{tableId}")
    void updateStatus(@Param("tableId") Integer tableId, @Param("tableStatus") String tableStatus);

    @Delete("DELETE FROM dining_table WHERE table_id = #{tableId}")
    void deleteTable(Integer tableId);

    //这里要返回餐桌的数量而不是*
    @Select("select COUNT(*) from dining_table where table_status = '空' and capacity = 2")
    public int getNumber2();
    @Select("select COUNT(*) from dining_table where table_status = '空' and capacity = 4")
    public int getNumber4();
    @Select("select COUNT(*) from dining_table where table_status = '空' and capacity = 8")
    public int getNumber8();

    @Select("select COUNT(*) from dining_table where table_status = '空'")
    public int getNumber();

    @Select("SELECT COUNT(*) FROM dining_table WHERE capacity = #{capacity}")
    public int getTableBycapacity(Integer capacity);
    
    // 新增：获取可用餐桌的ID
    @Select("SELECT table_id FROM dining_table WHERE table_status = '空' AND capacity = #{capacity} LIMIT 1")
    public Integer getAvailableTableId(Integer capacity);
}