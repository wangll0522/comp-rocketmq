#Producer示例
<pre>
//获取具体主题生产者对象，具体类型在msg.config.xml中配置
BaseProducer<GpsData> producer = ProducerFactory.getInstance().factory(MessageConfig.DY_DATA_GPS);
//发送消息
producer.send(messageDatas, gpsData.getIndustry());
</pre>
#Consumer示例
<pre>
//如果配了缓存的数据源，则可以不需要配置处理类，直接从MessageTopicCache.getInstance().getCache(type)中取相对应主题的数据
ConsumerManager.getInstance(MessageTopicCache.getInstance())
        .startListener(new IConsumerCallback() {
    @Override
    public void callback(List&lt;ConsumerTopic&gt; consumerTopics) {
        //根据主题，启动不同的处理线程
        try {
            for (int i = 0; i &lt; consumerTopics.size(); i ++) {
                if (MessageConfig.DY_DATA_GPS.equals(consumerTopics.get(1).getType())) {
                    GpsDataStoreTimer.getInstance().startGpsSave();
                }
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
});
//相关配置请查看msg.config.xml配置文件


</pre>