package com.wangll.comp.rocketmq.config.bean;

/**
 * @Description:
 * @package: com.wangll.comp.rocketmq.config.bean.
 * Created by ll_wang on 2017/5/11.
 */
public class ConsumerTopic {
    private String type;
    private String tags;
    private String handle;
    private String converter;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getConverter() {
        return converter;
    }

    public void setConverter(String converter) {
        this.converter = converter;
    }

    @Override
    public String toString() {
        return "ConsumerTopic{" +
                "type='" + type + '\'' +
                ", tags='" + tags + '\'' +
                ", handle='" + handle + '\'' +
                ", converter='" + converter + '\'' +
                '}';
    }
}
