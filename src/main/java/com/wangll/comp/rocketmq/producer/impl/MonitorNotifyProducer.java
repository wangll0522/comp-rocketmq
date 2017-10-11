package com.wangll.comp.rocketmq.producer.impl;

import com.wangll.comp.activemq.model.BaseNoticeInfo;
import com.wangll.comp.rocketmq.producer.BaseProducer;

import java.util.List;

/**
 * @Description:
 * @package: com.wangll.comp.rocketmq.producer.impl.
 * Created by ll_wang on 2017/5/18.
 */
public class MonitorNotifyProducer extends BaseProducer<BaseNoticeInfo> {
    @Override
    protected List<BaseNoticeInfo> filter(List<BaseNoticeInfo> msgs) {
        return msgs;
    }
}
