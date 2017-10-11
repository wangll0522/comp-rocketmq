package com.wangll.comp.rocketmq.consumer;


import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.wangll.comp.rocketmq.config.MessageConfig;
import com.wangll.comp.rocketmq.converter.IMessageConverter;
import com.wangll.comp.rocketmq.converter.impl.GpsMessageConverter;
import com.beyond.lbs.common.model.GpsData;

import java.util.List;

/**
 * @Description:
 * @package: com.wangll.comp.rocketmq.consumer.
 * Created by ll_wang on 2017/5/9.
 */
public abstract class BaseMessageListener<T> implements MessageListenerConcurrently {
    private IMessageConverter messageConverter;

    public BaseMessageListener() {
        messageConverter = new GpsMessageConverter();
    }

    //在执行方法前加入解码器
    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
                                                    ConsumeConcurrentlyContext consumeConcurrentlyContext) {
        MessageExt msg = null;
        for (int i = 0; i < msgs.size(); i ++) {
            msg = msgs.get(i);
            if (msg.getTopic().equals(MessageConfig.DY_DATA_GPS)) {
                // 执行配置文件内的的消费逻辑
                return this.receiveMessage(messageConverter.fromMessage(msg.getBody()),
                        consumeConcurrentlyContext);
            }
        }

        return null;
    }

    public abstract ConsumeConcurrentlyStatus receiveMessage(List<T> list, ConsumeConcurrentlyContext consumeConcurrentlyContext);
}
