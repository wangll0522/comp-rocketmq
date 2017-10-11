package com.wangll.comp.rocketmq.producer.impl;

import com.wangll.comp.rocketmq.producer.BaseProducer;

import java.util.List;

/**
 * Created by admin on 2017/6/4.
 *
 */
public class CanStaticDataProducer extends BaseProducer<CanStaticDataProducer> {
    @Override
    protected List filter(List msgs) {
        return msgs;
    }
}
