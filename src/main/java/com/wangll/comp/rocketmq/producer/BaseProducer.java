package com.wangll.comp.rocketmq.producer;

import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.MessageQueueSelector;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.client.producer.SendStatus;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.common.message.MessageQueue;
import com.alibaba.rocketmq.remoting.exception.RemotingException;
import com.beyond.framework.utils.UUIDGenerator;
import com.wangll.comp.rocketmq.converter.IMessageConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @package: com.wangll.comp.rocketmq.producer.
 * Created by ll_wang on 2017/5/11.
 */
public abstract class BaseProducer<T> {

    protected DefaultMQProducer producer = null;
    protected IMessageConverter messageConverter = null;
    protected String topic;
    Map<String, Integer> topicQueueRef = null;

    public void init(DefaultMQProducer producer,IMessageConverter messageConverter, String topic) {
        this.producer = producer;
        this.messageConverter = messageConverter;
        this.topic = topic;
    }
    public DefaultMQProducer getMQProducer() {
        return this.producer;
    }

    public void setMQProducer(DefaultMQProducer producer) {
        this.producer = producer;
    }

    protected abstract List<T> filter(List<T> msgs);

    public long send(List<T> msgs){
        return send(msgs, null);
    }

    public long send(T message) {

        return send(message, null);
    }

    public long send(T message, String tags){
        List<T> messages = new ArrayList<T>();
        messages.add(message);
        return send(messages, tags);
    }

    public long send(List<T> msgs, String tags){
        msgs = this.filter(msgs);
        byte[] msgbt = this.messageConverter.toMessage(msgs);
        Message msg = new Message(this.topic, tags,
                UUIDGenerator.getUUID(), msgbt);
        SendResult sendResult = null;
        try {
            if (topicQueueRef != null) {
                sendResult = producer.send(msg, new MyMessageQueueSelector(), topicQueueRef);
            } else {
                sendResult = producer.send(msg);
            }

        } catch (Exception e) {
            //TODO 输出日志
            e.printStackTrace();
        }
        if (sendResult.getSendStatus() == SendStatus.SEND_OK) {
            return msgs.size();
        } else {
            return 0;
        }


    }

    public void setTopicQueueRef(Map<String, Integer> topicQueueRef) {
        this.topicQueueRef = topicQueueRef;
    }

    public void setMessageConverter(IMessageConverter messageConverter) {
        this.messageConverter = messageConverter;
    }

    public void shutdown() {
        this.shutdown();
    }

    public void start() throws MQClientException {
        this.start();
    }

    private class MyMessageQueueSelector implements MessageQueueSelector {

        @Override
        public MessageQueue select(List<MessageQueue> list, Message message, Object o) {
            //统一主题分入同一队列，确保消息顺序的正确性
            Map<String, Integer> hmap = (Map<String, Integer>) o;
            Integer ks = hmap.get(message.getTopic());
            if (ks != null) {
                ks = ks % list.size();
            }
            return list.get(ks);
        }
    }


}
