package com.example.BondSalesManagementSystem.service.imp;

import com.example.BondSalesManagementSystem.dao.BondSalesRecordDao;
import com.example.BondSalesManagementSystem.model.BondSalesRecord;
import com.example.BondSalesManagementSystem.service.BondSalesRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class BondSalesRecordServiceImp implements BondSalesRecordService {

    @Autowired
    private BondSalesRecordDao bondSalesRecordDao;

    @Override
    public List<BondSalesRecord> getRecordByNameAndDate(String bondsName, String salesName, Date start, Date end) {
        return bondSalesRecordDao.findRecordByNameAndDate(bondsName, salesName, start, end);
    }

    @Override
    public int insertRecord(String bondsName, String salesName, int amount) {

        BondSalesRecord record = new BondSalesRecord();
        // id自增
        record.setId(null);
        record.setBondsName(bondsName);
        record.setSalesName(salesName);
        record.setAmount(amount);
        record.setCreatedAt(new Date());
        record.setUpdatedAt(null);

        return bondSalesRecordDao.insertRecord(record);
    }

    @Override
    public List<BondSalesRecord> listAll() {
        return bondSalesRecordDao.listAll();
    }

    @Override
    public boolean exportFile(String path) {
        List<BondSalesRecord> records = bondSalesRecordDao.listAll();
        File file = new File(path+".csv");
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        BufferedWriter writer = null;
        try {
            fos = new FileOutputStream(file);
            osw = new OutputStreamWriter(fos);
            writer = new BufferedWriter(osw);
            writer.write("id, bonds_name, sales_name, amount, created_at, updated_at" + "\n");

            for (BondSalesRecord record : records) {
                writer.write(record.getId() + "," + record.getBondsName() + "," + record.getSalesName() + "," + record.getAmount() + "," + formatDate(record.getCreatedAt()) + "," + formatDate(record.getUpdatedAt()) + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (writer!=null) {
                    writer.close();
                }
                if (osw != null) {
                    osw.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    @Override
    public boolean importFile(String path) {
        File file = new File(path+".csv");
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader reader = null;
        try {
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis);
            reader = new BufferedReader(isr);
            String line;
            List<BondSalesRecord> list = new ArrayList<BondSalesRecord>();
            long start1 = System.currentTimeMillis();
            System.out.println("list开始获取record...");
            while ((line = reader.readLine())!=null) {
                String[] split = line.split(",");
                BondSalesRecord record = new BondSalesRecord();
                record.setBondsName(split[0]);
                record.setSalesName(split[1]);
                record.setAmount(Integer.parseInt(split[2]));
                record.setCreatedAt(formatDate(split[3]));
                record.setUpdatedAt(formatDate(split[4]));
                list.add(record);
                if (list.size() % 100000 == 0) {
                    System.out.println("第" + (list.size()/100000+1) + "批插入集合...");
                }
            }
            long end1 = System.currentTimeMillis();
            System.out.println("插入list完成， 用时"+ (end1 - start1) + "ms");

            int insert = batchInsert(list);
            System.out.println("插入数据库完成， 用时"+ (System.currentTimeMillis() - end1) + "ms");
            System.out.println(insert + " records successfully inserted!");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (isr != null) {
                    isr.close();
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return true;
    }

    @Override
    public int batchInsert(List<BondSalesRecord> list) {
        return bondSalesRecordDao.batchInsert(list);
    }

    private String formatDate(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    private Date formatDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return date;
        }
        return date;
    }


}
