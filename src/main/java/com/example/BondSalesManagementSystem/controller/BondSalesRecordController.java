package com.example.BondSalesManagementSystem.controller;

import com.example.BondSalesManagementSystem.model.BondSalesRecord;
import com.example.BondSalesManagementSystem.service.BondSalesRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RestController()
@RequestMapping("/bsr")
@CrossOrigin
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

    // http://localhost:8080/bsr/listAll
    @RequestMapping(value = "/listAll")
    public List<BondSalesRecord> listAll() {
        return bondSalesRecordService.listAll();
    }

//    @RequestMapping(value = "/import", method = RequestMethod.POST)
//    public boolean importFile(@RequestBody HashMap<String, String> bsrMap) throws IOException, SQLException {
//        System.out.println(1);
//        return bondSalesRecordService.importFile("");
//    }
    // http://localhost:8080/bsr/import?path=test
//    @RequestMapping(value = "/import", method = RequestMethod.GET)
//    public boolean importFile(@RequestParam(name = "path") String path) throws IOException, SQLException {
//        return bondSalesRecordService.importFile(path);
//    }
    @RequestMapping(value = "/import", method = RequestMethod.GET)
    public boolean importFile(@RequestParam(value = "upload", required = false) MultipartFile file,
                             HttpServletRequest request, HttpServletResponse response, HttpSession session, Model rhs)
            throws IOException {
        File f = new File("test.csv");
        return bondSalesRecordService.importFile(f);
    }

    private Date getDateOrNull(String s) throws ParseException {
        if (s == null || s.isEmpty()) {
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        return formatter.parse(s);
    }
}
