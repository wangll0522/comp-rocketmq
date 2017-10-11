package com.wangll.comp.rocketmq.consumer;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.MessageListener;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerOrderly;
import com.wangll.comp.rocketmq.converter.IMessageConverter;

/**
 * @Description:
 * @package: com.wangll.comp.rocketmq.
 * Created by ll_wang on 2017/5/8.
 */
public interface IPushConsumer {

    //获取rocketmq 对象的Consumer
    DefaultMQPushConsumer getMQConsumer();

    void setMQConsumer(DefaultMQPushConsumer consumer);

    IPushConsumer subscribe(String topic, String tags);

    //注册监听
    IPushConsumer addListener(MessageListenerConcurrently messageListener);

    IPushConsumer addListener(MessageListenerOrderly messageListener);
}
