package com.example.BondSalesManagementSystem.service.imp;

import com.example.BondSalesManagementSystem.dao.BondSalesRecordDao;
import com.example.BondSalesManagementSystem.model.BondSalesRecord;
import com.example.BondSalesManagementSystem.service.BondSalesRecordService;
import com.example.BondSalesManagementSystem.utils.BatchInsertThread;
import com.example.BondSalesManagementSystem.utils.DateFormatingUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;


import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class BondSalesRecordServiceImp implements BondSalesRecordService {

    @Autowired
    private BondSalesRecordDao bondSalesRecordDao;

    @Override
    public List<BondSalesRecord> getRecordByNameAndDate(String bondsName, String salesName, Date start, Date end) {
        return bondSalesRecordDao.findRecordByNameAndDate(bondsName, salesName, start, end);
    }

    @Override
    public int insertRecord(String bondsName, String salesName, int amount, Date createdAt) {

        BondSalesRecord record = new BondSalesRecord();
        // id自增
        record.setId(null);
        record.setBondsName(bondsName);
        record.setSalesName(salesName);
        record.setAmount(amount);
        record.setCreatedAt(createdAt);
        record.setUpdatedAt(null);

        return bondSalesRecordDao.insertRecord(record);
    }

    @Override
    public List<BondSalesRecord> listAll() {
        return bondSalesRecordDao.listAll();
    }



    @Override
    public boolean importFile(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        InputStreamReader inputStreamReader = new InputStreamReader(fis, "UTF-8");
        BufferedReader reader = new BufferedReader(inputStreamReader);
        LineIterator iterator = new LineIterator(reader);

        int fileIndex = 1;
        String splitFileName = "part-%s.csv";// 切割后的文件名
        String formatFilename = String.format(splitFileName, fileIndex);
        String line = "";
        int lineNum = 0;
        int fileLines = 1000000;    //一个子文件100w行数据

        FileOutputStream fos = new FileOutputStream(new File(formatFilename));
        OutputStreamWriter osw = new OutputStreamWriter(fos);
        BufferedWriter writer = new BufferedWriter(osw);

        List<String> fileNames = new ArrayList<>();

        while (iterator.hasNext()) {
            line = iterator.nextLine();
            if (lineNum > 0 && lineNum % fileLines == 0) {
                // 该换文件了
                writer.flush();
                writer.close();
                fileNames.add(formatFilename); //将当前子文件名添加入集合
                System.out.println("写入第" + fileIndex + "个文件");
                fileIndex++; // 文件索引加1
                formatFilename = String.format(splitFileName, fileIndex);
                writer = new BufferedWriter(new FileWriter(formatFilename));
            }
            writer.write(line + "\n");
            lineNum++;
        }
        fileNames.add(formatFilename); //将当前子文件名添加入集合
        writer.close();
        reader.close();

        // 开启多线程
        int len = fileNames.size();
        // 创建线程数
        int threadPoolSize = len;
        // 最多创建 4 个线程
        if (threadPoolSize > 4) {
            threadPoolSize = 4;
        }
        long start = System.currentTimeMillis();
        ExecutorService executor = Executors.newFixedThreadPool(threadPoolSize);

        for (int i = 0; i < len; i++) {
            String filename = fileNames.get(i);
            File temp = new File(filename);
            BatchInsertThread thread = new BatchInsertThread(temp, bondSalesRecordDao, i);
            executor.execute(thread);
        }
        executor.shutdown();

        long end = System.currentTimeMillis();
        System.out.println("总耗时：" + (end - start) + "ms");


        return false;
    }


    @Override
    public int batchInsert(List<BondSalesRecord> list) {
        return bondSalesRecordDao.batchInsert(list);
    }
}
