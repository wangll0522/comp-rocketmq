package com.wangll.comp.rocketmq.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class LocalServerConstant {
    private static Logger logger = LoggerFactory.getLogger(LocalServerConstant.class.getName());

    static {
        Properties prop = new Properties();
        try {

            InputStream inStream = LocalServerConstant.class
                    .getResourceAsStream("/wangll/app.properties");
            prop.load(inStream);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            logger.error("加载config.properties文件出错");
        }

        String save_queue_size = prop.getProperty("server.save.queuesize").trim();
        if("".equals(save_queue_size)){
            GPSDATA_SAVE_QUEUE_SIZE = Integer.parseInt(save_queue_size);
        } else {
            GPSDATA_SAVE_QUEUE_SIZE = 2000;
        }


    }

    /**
     * 内存线程中保存的gps数据队列，根据机器配置来指定队列数量
     */
    public static int GPSDATA_SAVE_QUEUE_SIZE;



}
