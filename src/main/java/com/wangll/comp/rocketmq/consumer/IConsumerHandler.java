package com.wangll.comp.rocketmq.consumer;

import java.util.List;

/**
 * @Description:
 * @package: com.wangll.comp.rocketmq.consumer.
 * Created by ll_wang on 2017/5/11.
 */
public interface IConsumerHandler<T> {
    void handle(List<T> data);
}
