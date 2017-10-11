package com.wangll.comp.rocketmq.cache.queue;

import com.wangll.comp.rocketmq.common.LocalServerConstant;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @package: com.wangll.comp.cache.
 * Created by ll_wang on 2017/5/9.
 */
public class MessageTopicCache {
    private static MessageTopicCache _instance = null;
    private static Map<String ,Map<String, MessageTopicQueue>>  cacheNameSpaceMap = null;

    private static Map<String, MessageTopicQueue> defaultCache = null;
    private static Map<String, MessageTopicQueue> cacheMap = null;
    private static int queueSize = LocalServerConstant.GPSDATA_SAVE_QUEUE_SIZE;

    private MessageTopicCache () {
        cacheNameSpaceMap = new HashMap<String, Map<String, MessageTopicQueue>>();
        defaultCache = new HashMap<String, MessageTopicQueue>();
    }
    public synchronized static MessageTopicCache getInstance(){
        if(_instance==null){
            _instance = new MessageTopicCache();
        }

        return  _instance;
    }

    public synchronized MessageTopicQueue getCache(String type) {

        if (!defaultCache.containsKey(type)) {
            defaultCache.put(type, new MessageTopicQueue(queueSize));
        }
        return defaultCache.get(type);
    }

    public synchronized MessageTopicQueue getCache(String nameSpace, String type) {
        if (!cacheNameSpaceMap.containsKey(nameSpace)) {
            cacheNameSpaceMap.put(nameSpace, new HashMap<String, MessageTopicQueue>());
        }
        cacheMap = cacheNameSpaceMap.get(nameSpace);
        if (!cacheMap.containsKey(type)) {
            cacheMap.put(type, new MessageTopicQueue(queueSize));
        }
        return cacheMap.get(type);
    }

    public Integer cacheSize() {
        return defaultCache.size();
    }

}
