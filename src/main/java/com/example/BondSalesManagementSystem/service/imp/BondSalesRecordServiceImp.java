package com.example.BondSalesManagementSystem.service.imp;

import com.example.BondSalesManagementSystem.dao.BondSalesRecordDao;
import com.example.BondSalesManagementSystem.model.BondSalesRecord;
import com.example.BondSalesManagementSystem.service.BondSalesRecordService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
        File file = new File(path + ".csv");
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
                if (writer != null) {
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

    private List<BondSalesRecord> readCSVIntoList(String path) {
        File file = new File(path + ".csv");
        List<BondSalesRecord> list = new ArrayList<>();

        if (!file.exists()) {
            return list;
        }

        try {
            LineIterator iterator = FileUtils.lineIterator(file, "UTF-8");
            long start = System.currentTimeMillis();
            while (iterator.hasNext()) {
                String line = iterator.nextLine();
                String[] elements = line.split(",");
                BondSalesRecord record = new BondSalesRecord();
                record.setBondsName(elements[0]);
                record.setSalesName(elements[1]);
                record.setAmount(Integer.parseInt(elements[2]));
                record.setCreatedAt(formatDate(elements[3]));
                record.setUpdatedAt(formatDate(elements[4]));
                list.add(record);
                if (list.size() % 1000000 == 0) {
                    System.out.println("第"+(list.size()/1000000)+"批读取完毕");
                }
            }
            long end = System.currentTimeMillis();
            System.out.println(end - start);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

//    public boolean importFile(String path) {
//        List<BondSalesRecord> list = readCSVIntoList(path);
//        int pointsDataLimit = 10000;//限制条数作为一个批次
//        int part = list.size() / pointsDataLimit;//分批数
//        System.out.println("共有 ： " + list.size() + "条，！" + " 分为 ：" + part + "批");
//        long start = System.currentTimeMillis();
//        int insert = 0;
//        for (int i = 0; i < part; i++) {
//            List<BondSalesRecord> listPage = list.subList(0, pointsDataLimit);
//            int success = batchInsert(listPage);
//            insert+=success;
//            //剔除
//            list.subList(0, pointsDataLimit).clear();
//            System.out.println("第" + i + "批插入成功！");
//        }
//        System.out.println("插入数据库完成， 用时" + (System.currentTimeMillis() - start) + "ms");
//        System.out.println(insert + " records successfully inserted!");
//        return true;
//    }

    public boolean importFile(String path) {
        File file = new File(path + ".csv");
        List<BondSalesRecord> list = new ArrayList<>();
        int insert = 0;

        if (!file.exists()) {
            return false;
        }

        try {
            LineIterator iterator = FileUtils.lineIterator(file, "UTF-8");
            long start = System.currentTimeMillis();
            int i = 1;
            while (iterator.hasNext()) {
                String line = iterator.nextLine();
                String[] elements = line.split(",");
                BondSalesRecord record = new BondSalesRecord();
                record.setBondsName(elements[0]);
                record.setSalesName(elements[1]);
                record.setAmount(Integer.parseInt(elements[2]));
                record.setCreatedAt(formatDate(elements[3]));
                record.setUpdatedAt(formatDate(elements[4]));
                list.add(record);
                if (list.size() % 50000 == 0) {
                    int success = batchInsert(list);
                    insert+=success;
                    list.clear();
                    System.out.println("第"+(i++)+"批读取并插入完毕");
                }
            }
            if (list.size() > 0) {
                int success = batchInsert(list);
                insert+=success;
            }
            long end = System.currentTimeMillis();
            System.out.println("插入数据库完成， 用时" + (end - start) + "ms");
            System.out.println(insert + " records successfully inserted!");
        } catch (IOException e) {
            e.printStackTrace();
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
