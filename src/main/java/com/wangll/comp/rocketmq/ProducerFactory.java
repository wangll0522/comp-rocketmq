package com.wangll.comp.rocketmq;

import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.log.ClientLogger;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.wangll.comp.rocketmq.common.ClassUtils;
import com.wangll.comp.rocketmq.config.MessageConfig;
import com.wangll.comp.rocketmq.config.bean.ProducerTopic;
import com.wangll.comp.rocketmq.converter.IMessageConverter;
import com.wangll.comp.rocketmq.exception.MyRuntimeException;
import com.wangll.comp.rocketmq.producer.BaseProducer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:获取单例消息生成者工厂类
 * @package: com.wangll.comp.rocketmq.
 * Created by ll_wang on 2017/5/9.
 */
public class ProducerFactory {
    private static Logger logger = LoggerFactory.getLogger(ProducerFactory.class);
    private static ProducerFactory instance = null;
    private static DefaultMQProducer producer = null;
    private Map<String, BaseProducer> clzMap = null;
    private Map<String, Integer> topicQueueRef = null;
    private List<ProducerTopic> producerTopicList = null;

    private ProducerFactory() {
        ClientLogger.setLog(logger);
        clzMap = new HashMap<String, BaseProducer>();
        loadConfig();
        producer = new DefaultMQProducer(MessageConfig.PRODUCER_GROUP);
        producer.setNamesrvAddr(MessageConfig.NAMESRV_ADDR);
        producer.setInstanceName(MessageConfig.PRODUCER_INSTANCE);
        producer.setCompressMsgBodyOverHowmuch(MessageConfig.COMPRESS_OVER);
        producer.setMaxMessageSize(MessageConfig.MAX_MESSAGE_SIZE);


        try {
            producer.start();
        } catch (MQClientException e) {
            //TODO 日志输出
            e.printStackTrace();
        }
    }

    private void loadConfig() {
        producerTopicList = MessageConfig.producerTopics;
        topicQueueRef = new HashMap<String, Integer>();
        for (int i = 0; i < producerTopicList.size(); i ++) {
            topicQueueRef.put(producerTopicList.get(i).getType(), i);
        }
    }

    public static synchronized ProducerFactory getInstance() {
        if (instance == null) {
            instance = new ProducerFactory();
        }
        return instance;
    }

    public BaseProducer factory(Class clz) {
        String clzName = clz.getName();
        //获取单例
        try {
            if (!clzMap.containsKey(clz.getName())) {
                clzMap.put(clzName, (BaseProducer) clz.newInstance());
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return clzMap.get(clzName);
    }

    public BaseProducer factory(String type) {
        BaseProducer getBaseProducer;
        //获取单例
        if (!clzMap.containsKey(type)) {
            getBaseProducer = this.getNewProducer(type);
            getBaseProducer.setTopicQueueRef(this.topicQueueRef);
            clzMap.put(type, getBaseProducer);
        }
        return clzMap.get(type);
    }

    private ProducerTopic getProducer(String type) throws MyRuntimeException {
        for (ProducerTopic topic : producerTopicList) {
            if (type.equals(topic.getType())) {
                return topic;
            }
        }
        throw new MyRuntimeException("主题类型未配置");
    }

    private BaseProducer getNewProducer(String type) {
        BaseProducer result = null;
        IMessageConverter messageConverter = null;
        ProducerTopic producerTopic = this.getProducer(type);
        String topicType = null;
        try {
            if (StringUtils.isNotEmpty(producerTopic.getConverter())) {
                messageConverter = (IMessageConverter) ClassUtils.getBean(producerTopic.getConverter());
            }
            if(StringUtils.isNotEmpty(producerTopic.getHandle())) {
                result = (BaseProducer) ClassUtils.getBean(producerTopic.getHandle());
            } else {
                result = new BaseProducer() {
                    @Override
                    protected List filter(List msgs) {
                        return msgs;
                    }
                };
            }
            if (producerTopic != null) {
                topicType = producerTopic.getType();
            }
            result.init(this.producer, messageConverter, topicType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result == null) {
            result = new BaseProducer() {
                @Override
                protected List filter(List msgs) {
                    return msgs;
                }
            };
        }
        return result;
    }

}
