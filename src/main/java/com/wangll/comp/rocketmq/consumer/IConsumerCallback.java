package com.wangll.comp.rocketmq.consumer;

import com.alibaba.rocketmq.common.message.MessageExt;
import com.wangll.comp.rocketmq.config.bean.ConsumerTopic;

import java.util.List;

/**
 * @Description:
 * @package: com.wangll.comp.rocketmq.consumer.
 * Created by ll_wang on 2017/5/11.
 */
public interface IConsumerCallback {
    void callback(List<ConsumerTopic> consumerTopics);
}
