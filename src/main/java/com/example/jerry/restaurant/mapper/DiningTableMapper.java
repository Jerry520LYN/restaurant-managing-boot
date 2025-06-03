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
}