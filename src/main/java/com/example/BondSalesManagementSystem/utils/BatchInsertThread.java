package com.example.BondSalesManagementSystem.utils;


import com.example.BondSalesManagementSystem.dao.BondSalesRecordDao;
import com.example.BondSalesManagementSystem.model.BondSalesRecord;
import org.apache.commons.io.LineIterator;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BatchInsertThread implements Runnable {
    private File file;
    private BondSalesRecordDao bondSalesRecordDao;
    private int threadId;

    public BatchInsertThread(File file, BondSalesRecordDao bondSalesRecordDao, int threadId) {
        this.file = file;
        this.bondSalesRecordDao = bondSalesRecordDao;
        this.threadId = threadId;
    }

    @Override
    public void run() {
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader reader = null;

        try {
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis, "UTF-8");
            reader = new BufferedReader(isr);

            LineIterator iterator = new LineIterator(reader);
            List<BondSalesRecord> list = new ArrayList<>();
            // 'max_allowed_packet' = 4194304 byte = 4 MB
            // batchAmount = 20000 * (max_allowed_packet/2) = 40000
            // 一次批量插入的数据量
            int batchAmount = 40000;

            int insert = 0;
            int i = 0;
            String line;
            long start = System.currentTimeMillis();
            while (iterator.hasNext()) {
                line = iterator.nextLine();
                String[] elements = line.split(",");
                BondSalesRecord record = new BondSalesRecord();
                record.setBondsName(elements[0]);
                record.setSalesName(elements[1]);
                record.setAmount(Integer.parseInt(elements[2]));
                record.setCreatedAt(DateFormatingUtil.formatDate(elements[3]));
                record.setUpdatedAt(DateFormatingUtil.formatDate(elements[4]));
                list.add(record);
                if (list.size() % batchAmount == 0) {
                    int success = bondSalesRecordDao.batchInsert(list);
                    insert += success;
                    list.clear();
                    System.out.println("thread " + threadId + " " + "第" + (i++) + "批" + batchAmount + "条数据读取并插入完毕");
                }
            }
            if (list.size() > 0) {
                int success = bondSalesRecordDao.batchInsert(list);
                insert += success;
                list.clear();
                System.out.println("thread " + threadId + " " + "第" + (i++) + "批" + batchAmount + "条数据读取并插入完毕");
            }
            long end = System.currentTimeMillis();
            System.out.println("thread " + threadId + " " + "插入数据库完成， 用时" + (end - start) / 1000 + "s");
            System.out.println("thread " + threadId + " " + insert + " records successfully inserted!");

            if (this.file.exists() && this.file.isFile()) {
                this.file.delete();
            }
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
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

    }
}
