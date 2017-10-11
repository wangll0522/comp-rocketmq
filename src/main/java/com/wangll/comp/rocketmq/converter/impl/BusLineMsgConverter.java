package com.wangll.comp.rocketmq.converter.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.beyond.gis.commons.bean.bus.BusLineInfo;
import com.beyond.gis.commons.geometry.SpatialReference;
import com.wangll.comp.rocketmq.converter.IMessageConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @package: com.wangll.comp.rocketmq.converter.impl.
 * Created by ll_wang on 2017/5/11.
 */
public class BusLineMsgConverter implements IMessageConverter<BusLineInfo> {
    private static Logger logger = LoggerFactory.getLogger(BusLineMsgConverter.class);
    PropertyFilter propertyFilter = new PropertyFilter() {
        @Override
        public boolean apply(Object o, String s, Object o1) {
            if (s.equals("spatialReference")) {
                return false;
            }

            return true;
        }
    };

    @Override
    public byte[] toMessage(List<BusLineInfo> var1) {
        SerializeWriter sw = new SerializeWriter();
        JSONSerializer serializer = new JSONSerializer(sw);
        serializer.getPropertyFilters().add(propertyFilter);
        serializer.write(var1);
        String result = sw.toString();
        try {
            return result.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result.getBytes();
    }

    @Override
    public byte[] toMessage(BusLineInfo var1) {

        List<BusLineInfo> data = new ArrayList<BusLineInfo>();
        data.add(var1);
        return this.toMessage(data);
    }

    @Override
    public List<BusLineInfo> fromMessage(byte[] var1) {
        String jsonStr;
        try {
            jsonStr = new String(var1,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            jsonStr = new String(var1);
            e.printStackTrace();
        }
        return JSONArray.parseArray(jsonStr, BusLineInfo.class);
    }

}
