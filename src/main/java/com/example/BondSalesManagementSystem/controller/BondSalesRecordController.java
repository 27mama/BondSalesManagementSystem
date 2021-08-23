package com.example.BondSalesManagementSystem.controller;

import com.example.BondSalesManagementSystem.model.BondSalesRecord;
import com.example.BondSalesManagementSystem.service.BondSalesRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController()
@RequestMapping("/bsr")
public class BondSalesRecordController {
    @Autowired
    private BondSalesRecordService bondSalesRecordService;

    // http://localhost:8080/bsr/find?bondsName=A&salesName=Sam&start=2018-01-01&end=2019-12-01
    // http://localhost:8080/bsr/find?start=2018-01-01&end=2019-12-01
    // http://localhost:8080/bsr/find?bondsName=A&salesName=Sam
    // http://localhost:8080/bsr/find?bondsName=A&start=2018-01-01
    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public List<BondSalesRecord> findRecordByNameAndDate(@RequestParam(name = "bondsName", required = false) String bondsName,
                                                         @RequestParam(name = "salesName", required = false) String salesName,
                                                         @RequestParam(name = "start", required = false) String start,
                                                         @RequestParam(name = "end", required = false) String end) throws ParseException {

        // 4个值可以同时为空
        Date startDate = getDateOrNull(start);
        Date endDate = getDateOrNull(end);
        return bondSalesRecordService.getRecordByNameAndDate(bondsName, salesName, startDate, endDate);
    }

    // http://localhost:8080/bsr/add?bondsName=B&salesName=Sam&amount=2000
    @RequestMapping(value = "/add")
    public int addRecord(@RequestParam(name = "bondsName") String bondsName,
                         @RequestParam(name = "salesName") String salesName,
                         @RequestParam(name = "amount") String amount) throws ParseException {

        return bondSalesRecordService.insertRecord(bondsName, salesName, Integer.parseInt(amount));
    }

    // http://localhost:8080/bsr/listAll
    @RequestMapping(value = "/listAll")
    public List<BondSalesRecord> listAll() {
        return bondSalesRecordService.listAll();
    }

    // http://localhost:8080/bsr/import?path=test
    @RequestMapping(value = "/import")
    public boolean importFile(@RequestParam(name = "path") String path) {
        return bondSalesRecordService.importFile(path);
    }

    // http://localhost:8080/bsr/export?path=data
    @RequestMapping(value = "/export", method = RequestMethod.GET)
    public boolean exportFile(@RequestParam(name = "path") String path) {
        return bondSalesRecordService.exportFile(path);
    }

    private Date getDateOrNull(String s) throws ParseException {
        if (s == null) {
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.parse(s);
    }
}
