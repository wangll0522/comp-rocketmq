package com.wangll.comp.rocketmq.producer.impl;
import com.beyond.gis.commons.bean.bus.BusLineInfo;
import com.wangll.comp.rocketmq.producer.BaseProducer;
import com.beyond.lbs.common.model.GpsData;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @package: com.wangll.comp.rocketmq.producer.impl.
 * Created by ll_wang on 2017/5/11.
 */
public class BusLineProducer extends BaseProducer<BusLineInfo> {

    @Override
    protected List<BusLineInfo> filter(List<BusLineInfo> msgs) {
        return msgs;
    }
}
