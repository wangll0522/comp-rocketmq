package com.wangll.comp.rocketmq.converter;

import java.util.List;

/**
 * @Description:
 * @package: com.wangll.comp.rocketmq.converter.
 * Created by ll_wang on 2017/5/8.
 */
public interface IMessageConverter<T> {
    byte[] toMessage(List<T> var1);

    byte[] toMessage(T var1);

    List<T> fromMessage(byte[] var1);
}
