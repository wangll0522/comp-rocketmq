package com.wangll.comp.rocketmq.config;

import com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;
import com.beyond.appbase.common.utils.XmlUtil;
import com.beyond.framework.exception.BeyondRuntimeException;
import com.wangll.comp.rocketmq.config.bean.ConsumerTopic;
import com.wangll.comp.rocketmq.config.bean.ProducerTopic;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @Description:
 * @package: com.wangll.comp.rocketmq.config.
 * Created by ll_wang on 2017/5/9.
 */
public class MessageConfig {
    public static String DY_DATA_GPS = "dy_data_gps";
    public static String DY_DATA_GPS_JT809 = "gps_jt809";
    public static String DY_DATA_GPS_JT808 = "gps_jt808";
    public static String DY_DATA_GPS_TCSP = "gps_tcsp";

    public static String DY_DATA_CAN_ELECTRICE = "dy_vst_can_electrice";
    public static String DY_DATA_CAN_STATIC = "dy_vst_can_static";
    public static String DY_DATA_OBD_EVENT = "dy_vst_obd_event";

    public static String ST_DATA_BUSLINE = "st_data_busline";


    public static String SYS_MONITOR_VIEW = "sys_monitor_view";

    private static String instance;

    private static Logger logger = Logger.getLogger(MessageConfig.class.getName());

    static {
        if (instance == null) {
            parseConfig();
        }
    }

    public static String NAMESRV_ADDR;
    public static Integer COMPRESS_OVER;
    public static Integer MAX_MESSAGE_SIZE;
    public static Integer HANDLE_OFFSET;
    public static ConsumeFromWhere RECEIVE_TYPE;
    public static String RECEIVE_TIMEOUT;
    public static Integer CACHE_HANDLE_SIZE;


    public static String CONSUMER_GROUP;
    public static String CONSUMER_INSTANCE;


    public static String PRODUCER_GROUP;
    public static String PRODUCER_INSTANCE;


    public static List<ConsumerTopic> consumerTopics;

    public static List<ProducerTopic> producerTopics;

    public static String CONSUMER_LISTENER;
    public static String CONSUMER_HANDER = "com.wangll.comp.rocketmq.consumer.impl.DefaultPushConsumer";

    public static void parseConfig() {
        try {
            ClassPathResource resource = new ClassPathResource("beyond/msg.config.xml");
            Document doc = DocumentHelper.parseText(XmlUtil.Stream2String(resource.getInputStream()));
            Element root = doc.getRootElement();
            Element props = root.element("props");
            setProps(props);
            setConsumerTopics(root.element("consumer.topic"));
            setProducerTopics(root.element("producer.topic"));
            instance = "root";
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void setProps(Element props) {
        Element namesrv = props.element("namesrv");
        if (namesrv != null && namesrv.attribute("addr") != null
                && StringUtils.isNotEmpty(namesrv.attribute("addr").getValue())) {
            NAMESRV_ADDR = namesrv.attribute("addr").getValue();
        }
        Element producer = props.element("producer");
        if (producer != null && producer.attribute("compressOver") != null
                && StringUtils.isNotEmpty(producer.attribute("compressOver").getValue())) {
            COMPRESS_OVER = Integer.parseInt(producer.attribute("compressOver").getValue());
        }
        if (producer != null && producer.attribute("maxMessageSize") != null
                && StringUtils.isNotEmpty(producer.attribute("maxMessageSize").getValue())) {
            MAX_MESSAGE_SIZE = Integer.parseInt(producer.attribute("maxMessageSize").getValue());
        }
        if (props.element("consumer") != null) {
            setPropsConsumer(props.element("consumer"));
        }
        Element cache = props.element("cache");
        if (cache != null && cache.attribute("handleSize") != null
                && StringUtils.isNotEmpty(cache.attribute("handleSize").getValue())) {
            CACHE_HANDLE_SIZE = Integer.parseInt(cache.attribute("handleSize").getValue());
        }

    }

    private static void setPropsConsumer(Element cusumer) {
        if (cusumer.attribute("receiveType") != null) {
            String receiveType = cusumer.attribute("receiveType").getValue();

            if ("time".equals(receiveType))  {
                RECEIVE_TYPE = ConsumeFromWhere.CONSUME_FROM_TIMESTAMP;
                Element receiveTimeout = cusumer.element("receive.timeout");
                if (receiveTimeout != null
                        && StringUtils.isNotEmpty(receiveTimeout.getStringValue())) {
                    RECEIVE_TIMEOUT = receiveTimeout.getStringValue();
                }
            } else if ("now".equals(receiveType)) {
                RECEIVE_TYPE = ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET;
            } else if ("history".equals(receiveType)) {
                RECEIVE_TYPE = ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET;
            }
            Element handleOffset = cusumer.element("handle.offset");
            if (handleOffset != null
                    && StringUtils.isNotEmpty(handleOffset.getStringValue())) {
                HANDLE_OFFSET = Integer.parseInt(handleOffset.getStringValue());
            }
        }
    }

    private static void setConsumerTopics(Element element) {
        if (element == null) {
            throw new BeyondRuntimeException("consumer.topic");
        }
        if (element.attribute("group") != null
                && StringUtils.isNotEmpty(element.attribute("group").getValue())){
            CONSUMER_GROUP = element.attribute("group").getValue();
        }
        if (element.attribute("instance") != null
                && StringUtils.isNotEmpty(element.attribute("instance").getValue())){
            CONSUMER_INSTANCE = element.attribute("instance").getValue();
        }
        if (element.attribute("listener") != null
                && StringUtils.isNotEmpty(element.attribute("listener").getValue())){
            CONSUMER_LISTENER = element.attribute("listener").getValue();
        }
        if (element.attribute("handler") != null
                && StringUtils.isNotEmpty(element.attribute("handler").getValue())){
            CONSUMER_HANDER = element.attribute("handler").getValue();
        }

        List<Element> beans = element.elements("bean");
        ConsumerTopic consumerTopic = null;
        for (Element bean : beans) {
            consumerTopic = new ConsumerTopic();
            if (consumerTopics == null) {
                consumerTopics = new ArrayList<ConsumerTopic>();
            }
            if (bean.attribute("type") != null){
                consumerTopic.setType(bean.attribute("type").getValue());
            }
            if (bean.attribute("handle") != null){
                consumerTopic.setHandle(bean.attribute("handle").getValue());
            }
            if (bean.attribute("converter") != null){
                consumerTopic.setConverter(bean.attribute("converter").getValue());
            }
            if (bean.attribute("tags") != null){
                consumerTopic.setTags(bean.attribute("tags").getValue());
            }
            consumerTopics.add(consumerTopic);
        }

    }

    private static void setProducerTopics(Element element) {
        if (element == null) {
            throw new BeyondRuntimeException("consumer.topic");
        }
        if (element.attribute("group") != null
                && StringUtils.isNotEmpty(element.attribute("group").getValue())){
            PRODUCER_GROUP = element.attribute("group").getValue();
        }
        if (element.attribute("instance") != null
                && StringUtils.isNotEmpty(element.attribute("instance").getValue())){
            PRODUCER_INSTANCE = element.attribute("instance").getValue();
        }

        List<Element> beans = element.elements("bean");
        ProducerTopic producerTopic = null;
        for (Element bean : beans) {
            producerTopic = new ProducerTopic();
            if (producerTopics == null) {
                producerTopics = new ArrayList<ProducerTopic>();
            }
            if (bean.attribute("type") != null){
                producerTopic.setType(bean.attribute("type").getValue());
            }
            if (bean.attribute("handle") != null){
                producerTopic.setHandle(bean.attribute("handle").getValue());
            }
            if (bean.attribute("converter") != null){
                producerTopic.setConverter(bean.attribute("converter").getValue());
            }
            producerTopics.add(producerTopic);
        }
    }

    public static void main(String[] args) {
        System.out.println(MessageConfig.CONSUMER_GROUP);
        System.out.println(MessageConfig.NAMESRV_ADDR);
        System.out.println(MessageConfig.producerTopics);
        System.out.println(MessageConfig.consumerTopics);
    }

}
