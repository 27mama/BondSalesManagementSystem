package com.example.BondSalesManagementSystem.controller;

import com.example.BondSalesManagementSystem.model.BondSalesRecord;
import com.example.BondSalesManagementSystem.service.BondSalesRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RestController()
@RequestMapping("/bsr")
@CrossOrigin(origins = "http://localhost:4200/", allowedHeaders = "*", allowCredentials = "true")
public class BondSalesRecordController {
    @Autowired
    private BondSalesRecordService bondSalesRecordService;

    @RequestMapping(value = "/find", method = RequestMethod.POST)
    public List<BondSalesRecord> findRecordByNameAndDate(@RequestBody HashMap<String, String> bsrMap) throws ParseException {

        // 4个值可以同时为空
        String bondsName = bsrMap.get("bondsName").isEmpty() ? null : bsrMap.get("bondsName");
        String salesName = bsrMap.get("salesName").isEmpty() ? null : bsrMap.get("salesName");

        Date startDate = getDateOrNull(bsrMap.get("start"));
        Date endDate = getDateOrNull(bsrMap.get("end"));

        return bondSalesRecordService.getRecordByNameAndDate(bondsName, salesName, startDate, endDate);
    }


    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public int addRecord(@RequestBody HashMap<String, String> bsrMap) throws ParseException {

        return bondSalesRecordService.insertRecord(bsrMap.get("bondname"), bsrMap.get("username"),
                Integer.parseInt(bsrMap.get("amount")), getDateOrNull(bsrMap.get("date")));
    }

    @RequestMapping(value = "/import", method = RequestMethod.POST)
    @ResponseBody
    public boolean importFile(MultipartFile file)
            throws IOException {
        return bondSalesRecordService.importFile(file);
    }

    private Date getDateOrNull(String s) throws ParseException {
        if (s == null || s.isEmpty()) {
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        return formatter.parse(s);
    }
}
