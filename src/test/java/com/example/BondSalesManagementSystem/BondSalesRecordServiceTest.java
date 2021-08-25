package com.example.BondSalesManagementSystem;

import com.example.BondSalesManagementSystem.service.BondSalesRecordService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
public class BondSalesRecordServiceTest {
    @Autowired
    private BondSalesRecordService bondSalesRecordService;

    @Test
    @Transactional
    void testGetRecordByNameAndDate() throws ParseException {
        bondSalesRecordService.getRecordByNameAndDate("A bonds", "Sam",
                getDateOrNull("06/01/2019"), getDateOrNull("08/01/2019"));
    }


    @Test
    @Transactional
    void testGetRecordByNameAndDate_NoDate() throws ParseException {
        bondSalesRecordService.getRecordByNameAndDate("A bonds", "Sam",
                getDateOrNull(""), getDateOrNull(""));
    }

    @Test
    @Transactional
    void testGetRecordByNameAndDate_NoName() throws ParseException {
        bondSalesRecordService.getRecordByNameAndDate(null, null, getDateOrNull("06/01/2019"), getDateOrNull("08/01/2019"));
    }

    @Test
    @Transactional
    void testGetRecordByNameAndDate_OnlySalesName() throws ParseException {
        bondSalesRecordService.getRecordByNameAndDate(null, "Terry",
                getDateOrNull(""), getDateOrNull(""));
    }

    @Test
    @Transactional
    void testGetRecordByNameAndDate_NoData() throws ParseException {
        bondSalesRecordService.getRecordByNameAndDate(null, null,
                getDateOrNull(""), getDateOrNull(""));
    }


    @Test
    @Transactional
    void testInsertRecord() throws ParseException {
        bondSalesRecordService.insertRecord("C bonds", "Jack", 8000, getDateOrNull("06/01/2021"));
    }


    private Date getDateOrNull(String s) throws ParseException {
        if (s == null || s.isEmpty()) {
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        return formatter.parse(s);
    }
}

