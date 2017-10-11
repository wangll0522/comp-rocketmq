package com.wangll.comp.rocketmq.producer.impl;
import com.wangll.comp.rocketmq.producer.BaseProducer;
import com.beyond.lbs.common.model.GpsData;

import java.util.List;

/**
 * @Description:
 * @package: com.wangll.comp.rocketmq.producer.impl.
 * Created by ll_wang on 2017/5/11.
 */
public class GpsDataProducer extends BaseProducer<GpsData> {

    @Override
    protected List<GpsData> filter(List<GpsData> msgs) {
        return msgs;
    }
}
