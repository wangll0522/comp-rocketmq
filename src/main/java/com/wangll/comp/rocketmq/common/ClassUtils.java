package com.wangll.comp.rocketmq.common;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClassUtils {
    public static Logger logger = LoggerFactory.getLogger(ClassUtils.class);

    public static Object getBean(String className) {
        Class clazz = null;
        try {
            clazz = Class.forName(className);
        } catch (Exception ex) {
            logger.error("找不到指定的类,类名：" + className);
        }
        if (clazz != null) {
            try {
                return clazz.newInstance();
            } catch (Exception ex) {
                logger.error("找不到指定的类,类名：" + className);
            }
        }
        return null;
    }

}

