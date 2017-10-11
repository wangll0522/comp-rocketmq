package com.wangll.comp.rocketmq.converter.impl;

import com.wangll.comp.activemq.model.BaseNoticeInfo;
import com.wangll.comp.activemq.model.impl.LineDisconnectInfo;
import com.wangll.comp.rocketmq.converter.IMessageConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @package: com.wangll.comp.rocketmq.converter.impl.
 * Created by ll_wang on 2017/5/18.
 */
public class MonitorNotifyConverter implements IMessageConverter<BaseNoticeInfo> {
    private static Logger logger = LoggerFactory.getLogger(MonitorNotifyConverter.class);
    @Override
    public byte[] toMessage(List<BaseNoticeInfo> var1) {
        return this.toByte(var1);
    }

    @Override
    public byte[] toMessage(BaseNoticeInfo var1) {
        List<BaseNoticeInfo> noticeInfos = new ArrayList<BaseNoticeInfo>();
        noticeInfos.add(var1);
        return this.toMessage(noticeInfos);
    }

    @Override
    public List<BaseNoticeInfo> fromMessage(byte[] var1) {
        List<BaseNoticeInfo> lists = new ArrayList<BaseNoticeInfo>();
        lists.add((BaseNoticeInfo)this.toObj(var1));
        return lists;
    }


    private Object toObj(byte[] var1) {
        Object obj = null;
        try {
            // bytearray to object
            ByteArrayInputStream bi = new ByteArrayInputStream(var1);
            ObjectInputStream oi = new ObjectInputStream(bi);

            obj = oi.readObject();
            bi.close();
            oi.close();
        } catch (Exception e) {
            logger.error("对象转换异常" + e.getMessage());
            e.printStackTrace();
        }
        return obj;
    }


    private byte[] toByte(Object obj) {
        byte[] bytes = null;
        try {
            // object to bytearray
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream oo = new ObjectOutputStream(bo);
            oo.writeObject(obj);
            bytes = bo.toByteArray();
            bo.close();
            oo.close();

        } catch (Exception e) {
            logger.error("对象转换异常" + e.getMessage());
            e.printStackTrace();
        }
        return bytes;
    }

    public static void main(String[] args) {
        LineDisconnectInfo info = new LineDisconnectInfo();
        List<BaseNoticeInfo> infoList = new ArrayList<BaseNoticeInfo>();
        List<LineDisconnectInfo> disList = new ArrayList<LineDisconnectInfo>();
        info.setConnName("fdsfsd");
        info.setReceiver("123123");
        info.setPlateformType(123123);
        info.setConnPhone("1231231");
        MonitorNotifyConverter m = new MonitorNotifyConverter();
        byte[] arr = m.toByte(info);
        infoList = m.fromMessage(arr);
        for (BaseNoticeInfo noticeInfo: infoList) {
            disList.add((LineDisconnectInfo)noticeInfo);
        }
        System.out.println(disList);
    }
}
