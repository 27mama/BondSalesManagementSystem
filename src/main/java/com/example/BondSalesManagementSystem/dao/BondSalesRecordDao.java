package com.example.BondSalesManagementSystem.dao;

import com.example.BondSalesManagementSystem.mapper.BondSalesRecordMapper;
import com.example.BondSalesManagementSystem.model.BondSalesRecord;
import org.apache.ibatis.annotations.*;


import java.util.Date;
import java.util.List;

@Mapper
public interface BondSalesRecordDao extends BondSalesRecordMapper {

    @Select("select id, bonds_name, sales_name, amount, created_at, updated_at from bonds_sales_record " +
            "where created_at between #{start} and #{end} " +
            "and bonds_name = #{bondsName}" +
            "and sales_name = #{salesName}")
    @Results({
            @Result(id=true, property="id", column="ID"),
            @Result(property="bondsName", column="BONDS_NAME"),
            @Result(property="salesName", column="SALES_NAME"),
            @Result(property="amount", column="AMOUNT"),
            @Result(property="createdAt", column="CREATED_AT"),
            @Result(property="updatedAt", column="UPDATED_AT")
    })
    List<BondSalesRecord> findRecordByNameAndDate(String bondsName, String salesName, Date start, Date end);


    @Insert("insert into bonds_sales_record(id, bonds_name, sales_name, amount, created_at, updated_at) " +
            "values(#{id}, #{bondsName}, #{salesName}, " +
            "#{amount}, #{createdAt}, #{updatedAt})")
    int insertRecord(BondSalesRecord record);

    @Select("select id, bonds_name, sales_name, amount, created_at, updated_at from bonds_sales_record")
    @Results({
            @Result(id=true, property="id", column="ID"),
            @Result(property="bondsName", column="BONDS_NAME"),
            @Result(property="salesName", column="SALES_NAME"),
            @Result(property="amount", column="AMOUNT"),
            @Result(property="createdAt", column="CREATED_AT"),
            @Result(property="updatedAt", column="UPDATED_AT")
    })
    List<BondSalesRecord> listAll();

    /**
     * 批量插入数据
     * @param recordList
     * @return
     */
    @Insert({"<script>",
            "insert into bonds_sales_record(id, bonds_name, sales_name, amount, created_at, updated_at) values ",
            "<foreach collection='recordList' item='record' index='index' separator=','>",
            "(#{record.id}, #{record.bondsName}, #{record.salesName}, #{record.amount}, #{record.createdAt}, #{record.updatedAt})",
            "</foreach>",
            "</script>"})
    int batchInsert(@Param(value="recordList") List<BondSalesRecord> recordList);

}