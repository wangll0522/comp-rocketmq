package com.wangll.comp.rocketmq.converter.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.wangll.comp.rocketmq.converter.IMessageConverter;
import com.beyond.lbs.common.model.GpsData;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @package: com.wangll.comp.rocketmq.converter.impl.
 * Created by ll_wang on 2017/5/8.
 */
public class GpsMessageConverter implements IMessageConverter<GpsData> {
    @Override
    public byte[] toMessage(List<GpsData> var1) {
        String jsonStr = JSON.toJSONString(var1);
        try {
            return jsonStr.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return jsonStr.getBytes();
    }

    @Override
    public byte[] toMessage(GpsData var1) {
        List<GpsData> gpsDatas = new ArrayList<GpsData>();
        gpsDatas.add(var1);
        return toMessage(gpsDatas);
    }

    @Override
    public List<GpsData> fromMessage(byte[] var1) {
        String result;
        try {
            result = new String(var1, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            result = new String(var1);
            e.printStackTrace();
        }
        return JSONArray.parseArray(result, GpsData.class);
    }

}