package com.example.BondSalesManagementSystem.service;

import com.example.BondSalesManagementSystem.model.BondSalesRecord;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public interface BondSalesRecordService {

    List<BondSalesRecord> getRecordByNameAndDate(String bondsName, String salesName, Date start, Date end);

    int insertRecord(String bondsName, String salesName, int amount, Date createdAt);

    /**
     * import file
     * @param file
     * @return
     */
    boolean importFile(MultipartFile file) throws IOException;

    /**
     * batch insert records
     * @param list
     * @return
     */
    int batchInsert(List<BondSalesRecord> list);
}
