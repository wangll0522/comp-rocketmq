package com.wangll.comp.rocketmq.consumer.impl;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.*;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.wangll.comp.rocketmq.consumer.IPushConsumer;

/**
 * @Description:
 * @package: com.wangll.comp.rocketmq.consumer.impl.
 * Created by ll_wang on 2017/5/9.
 */
public class DefaultPushConsumer implements IPushConsumer {

    private DefaultMQPushConsumer consumer;

    public DefaultPushConsumer() {

    }

    public DefaultPushConsumer(DefaultMQPushConsumer consumer) {
        this.consumer = consumer;
    }

    public DefaultMQPushConsumer getMQConsumer() {
        return consumer;
    }

    public void setMQConsumer(DefaultMQPushConsumer consumer) {
        this.consumer = consumer;
    }

    public IPushConsumer subscribe(String topic, String tags) {
        try {
            consumer.subscribe(topic, tags);
        } catch (MQClientException e) {
            e.printStackTrace();
        }
        return this;
    }

    public IPushConsumer addListener(MessageListenerConcurrently messageListener) {
        if (this.consumer != null && messageListener != null) {
            this.consumer.registerMessageListener(messageListener);
        }
        return this;
    }

    public IPushConsumer addListener(MessageListenerOrderly messageListener) {
        if (this.consumer != null && messageListener != null) {
            this.consumer.registerMessageListener(messageListener);
        }
        return this;
    }


}
