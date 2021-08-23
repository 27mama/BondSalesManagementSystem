package com.example.BondSalesManagementSystem.service;

import com.example.BondSalesManagementSystem.model.BondSalesRecord;

import java.util.Date;
import java.util.List;

public interface BondSalesRecordService {

    List<BondSalesRecord> getRecordByNameAndDate(String bondsName, String salesName, Date start, Date end);

    int insertRecord(String bondsName, String salesName, int amount);

    List<BondSalesRecord> listAll();

    /**
     * export file to path
     * @param path
     * @return
     */
    boolean exportFile(String path);

    /**
     * import file from path
     * @param path
     * @return
     */
    boolean importFile(String path);

    /**
     * batch insert records
     * @param list
     * @return
     */
    int batchInsert(List<BondSalesRecord> list);
}
