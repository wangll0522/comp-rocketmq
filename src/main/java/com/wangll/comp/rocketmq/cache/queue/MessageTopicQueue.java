package com.wangll.comp.rocketmq.cache.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @Description:
 * @package: com.wangll.comp.cache.
 * Created by ll_wang on 2017/5/9.
 */
public class MessageTopicQueue<T extends Object> {
    private ArrayBlockingQueue queue = null;
    private static Logger logger = LoggerFactory.getLogger(MessageTopicQueue.class);

    public MessageTopicQueue(Integer queueSize) {
        queue = new ArrayBlockingQueue<T>(queueSize);
    }
    public void put(T data) {
        try {
            queue.put(data);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void put(List<T> datas) {
        for (int i = 0; i < datas.size(); i ++) {
            this.put(datas.get(i));
        }
    }

    public List<Object> getList() {
        List<Object> list = new ArrayList<Object>();
        int total = queue.size();
        for (int i = 0; i <total; i++) {
            try {
                list.add(queue.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return list;
    }

    public List<Object> getList(int num) {
        List<Object> list = new ArrayList<Object>();
        int total = queue.size();
        if (total >= num) {
            total = num;
        }

        for (int i = 0; i < total; i++) {
            try {
                list.add(queue.take());
            } catch (InterruptedException e) {
                e.printStackTrace();

            }
        }

        return list;
    }

    public int size(){
        int count = queue.size();
        return  count;
    }

}
