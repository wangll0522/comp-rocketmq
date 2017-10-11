package com.wangll.comp.rocketmq;

import com.alibaba.fastjson.JSONException;
import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.log.ClientLogger;
import com.alibaba.rocketmq.common.message.MessageConst;
import com.alibaba.rocketmq.common.message.MessageExt;

import com.wangll.comp.rocketmq.cache.queue.MessageTopicCache;
import com.wangll.comp.rocketmq.common.ClassUtils;
import com.wangll.comp.rocketmq.config.MessageConfig;
import com.wangll.comp.rocketmq.config.bean.ConsumerTopic;
import com.wangll.comp.rocketmq.consumer.IConsumerCallback;
import com.wangll.comp.rocketmq.consumer.IConsumerHandler;
import com.wangll.comp.rocketmq.consumer.IPushConsumer;
import com.wangll.comp.rocketmq.converter.IMessageConverter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 获取单例消费者工厂类
 * @package: com.wangll.comp.rocketmq.
 * Created by ll_wang on 2017/5/9.
 */
public class ConsumerManager {

    private static Logger logger = LoggerFactory.getLogger(ConsumerManager.class);

    private static ConsumerManager instance = null;
    private Map<String, IPushConsumer> clzMap = null;
    private DefaultMQPushConsumer consumer = null;
    private List<ConsumerTopic> consumerTopicList = null;
    private Map<String, ConsumerTopic> consumerTopicMap = null;
    private MessageListenerConcurrently consumerListener = null;
    private IPushConsumer pushConsumer;
    private static MessageTopicCache datasource;


    private ConsumerManager() {
        try {
            ClientLogger.setLog(logger);
            consumerTopicMap = new HashMap<String, ConsumerTopic>();
            consumerTopicList = MessageConfig.consumerTopics;
            for (ConsumerTopic consumerTopic : consumerTopicList) {
                consumerTopicMap.put(consumerTopic.getType(), consumerTopic);
            }
            if (StringUtils.isNotEmpty(MessageConfig.CONSUMER_LISTENER)) {
                consumerListener = (MessageListenerConcurrently) ClassUtils.getBean(MessageConfig.CONSUMER_LISTENER);
            }
            if (StringUtils.isNotEmpty(MessageConfig.CONSUMER_HANDER)) {
                pushConsumer = (IPushConsumer) ClassUtils.getBean(MessageConfig.CONSUMER_HANDER);
            }

            consumer = new DefaultMQPushConsumer(MessageConfig.CONSUMER_GROUP);
            consumer.setNamesrvAddr(MessageConfig.NAMESRV_ADDR);
            consumer.setInstanceName(MessageConfig.CONSUMER_INSTANCE);
            consumer.setConsumeMessageBatchMaxSize(1);
            consumer.setConsumeFromWhere(MessageConfig.RECEIVE_TYPE);
//            consumer.setMessageModel(MessageModel.CLUSTERING);
            if (MessageConfig.RECEIVE_TIMEOUT != null) {
                consumer.setConsumeTimestamp(MessageConfig.RECEIVE_TIMEOUT);
            }

            for (ConsumerTopic consumerTopic : consumerTopicList) {
                consumer.subscribe(consumerTopic.getType(), consumerTopic.getTags());
            }
            if (pushConsumer != null) {
                pushConsumer.setMQConsumer(consumer);
            }

        } catch (Exception e) {
            //TODO 处理失败
            e.printStackTrace();
        }
    }

    public void startListener(IConsumerCallback consumerCallback) {
        if (consumerCallback != null) {
            consumerCallback.callback(consumerTopicList);
        }
        if (consumerListener != null) {
            consumer.registerMessageListener(consumerListener);
        } else {
            consumer.registerMessageListener(new MessageListenerConcurrently() {

                public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgList,
                                                                ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                    try {
                        logger.info("接收到数据AckIndex:{0}", consumeConcurrentlyContext.getAckIndex());
                        if (MessageConfig.HANDLE_OFFSET != null) {
                            long offset = msgList.get(0).getQueueOffset();
                            String maxOffset = msgList.get(0).getProperty(MessageConst.PROPERTY_MAX_OFFSET);
                            long diff = Long.parseLong(maxOffset) - offset;
                            if (diff > MessageConfig.HANDLE_OFFSET) { //消息堆积了10W情况的特殊处理，10w条以前的消息不作处理
                                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                            }
                        }
                        handleMsg(msgList);
                    } catch (JSONException e) {
                        logger.error("消息体格式（JSON）转换错误");
                    }finally {
                        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                    }
                }

            });
        }
        try {
            consumer.start();
            logger.info("已启动Conusmer【gruop:"+ MessageConfig.CONSUMER_GROUP +"，instance:"+ MessageConfig.CONSUMER_INSTANCE
                    +"】，监听TOPIC-{"+ consumerTopicList.toString() +"}");
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }



    private void handleMsg(List<MessageExt> msgList) throws Exception {
        MessageExt msg = null;
        ConsumerTopic consumerTopic = null;
        IConsumerHandler consumerHandler = null;
        IMessageConverter messageConverter = null;
        List converData = null;
        for (int i = 0; i < msgList.size(); i ++) {
            msg = msgList.get(i);
            consumerTopic = consumerTopicMap.get(msg.getTopic());
            if (StringUtils.isNotEmpty(consumerTopic.getHandle())) {
                consumerHandler = (IConsumerHandler)ClassUtils.getBean(consumerTopic.getHandle());
                if (StringUtils.isNotEmpty(consumerTopic.getConverter())) {
                    messageConverter = (IMessageConverter)ClassUtils.getBean(consumerTopic.getConverter());
                }
                if (messageConverter != null) {
                    converData = messageConverter.fromMessage(msg.getBody());
                    if (datasource != null) {
                        datasource.getCache(msg.getTopic()).put(converData);
                    }
                }
                if (consumerHandler != null && converData != null) {
                    consumerHandler.handle(converData);
                }
            }

        }
    }

    public static synchronized ConsumerManager getInstance() {
        if (instance == null) {
            instance = new ConsumerManager();
        }
        return instance;
    }

    public static synchronized ConsumerManager getInstance(MessageTopicCache topicCache) {
        if (instance == null) {
            instance = new ConsumerManager();
        }
        datasource = topicCache;
        return instance;
    }

    public synchronized IPushConsumer consumer() {
        return pushConsumer;
    }

}
