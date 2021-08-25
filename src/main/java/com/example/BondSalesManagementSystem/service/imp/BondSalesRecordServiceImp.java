package com.example.BondSalesManagementSystem.service.imp;

import com.example.BondSalesManagementSystem.dao.BondSalesRecordDao;
import com.example.BondSalesManagementSystem.model.BondSalesRecord;
import com.example.BondSalesManagementSystem.service.BondSalesRecordService;
import com.example.BondSalesManagementSystem.utils.BatchInsertThread;
import org.apache.commons.io.LineIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
public class BondSalesRecordServiceImp implements BondSalesRecordService {

    @Autowired
    private BondSalesRecordDao bondSalesRecordDao;

    Logger logger = LoggerFactory.getLogger(getClass());

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
    public boolean importFile(MultipartFile file) throws IOException {
        List<String> fileNames = new ArrayList<>();

        InputStream inputStream = file.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(inputStreamReader);
        LineIterator iterator = new LineIterator(reader);

        int fileIndex = 1;
        String splitFileName = "part-%s.csv";// 切割后的文件名
        String formatFilename = String.format(splitFileName, fileIndex);
        String line;
        int lineNum = 0;
        int fileLines = 1000000;    //一个子文件100w行数据

        FileOutputStream fos = new FileOutputStream(new File(formatFilename));
        OutputStreamWriter osw = new OutputStreamWriter(fos);
        BufferedWriter writer = new BufferedWriter(osw);

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
        System.out.println("写入第" + fileIndex + "个文件");
        writer.close();
        reader.close();

        // 开启多线程
        int len = fileNames.size();
        // 创建线程数
        int threadPoolSize = len;
        // 最多创建 9 个线程
        if (threadPoolSize > 4) {
            threadPoolSize = 4;
        }
        long start = System.currentTimeMillis();
        ExecutorService executor = Executors.newFixedThreadPool(threadPoolSize);

        for (int i = 0; i < len; i++) {
            String filename = fileNames.get(i);
            BatchInsertThread thread = new BatchInsertThread(filename, bondSalesRecordDao, i);
            executor.execute(thread);
        }
        executor.shutdown();

//        try {
//            // awaitTermination返回false即超时会继续循环，返回true即线程池中的线程执行完成主线程跳出循环往下执行，每隔10秒循环一次
//            while (!executor.awaitTermination(10, TimeUnit.SECONDS));
//        } catch (InterruptedException e) {
//            logger.error("error", e);
//            return false;
//        }

        long end = System.currentTimeMillis();
        System.out.println("多线程导入数据总耗时：" + (end - start) + "ms");
        return true;
    }

    /**
     * 将文件写成子文件，并将子文件名保存在fileNames中
     *
     * @param file
     * @param fileNames
     * @throws IOException
     */
    private void writeSubFiles(MultipartFile file, List<String> fileNames) throws IOException {

    }


    @Override
    public int batchInsert(List<BondSalesRecord> list) {
        return bondSalesRecordDao.batchInsert(list);
    }
}
