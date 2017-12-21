package com.py.kafka.kafka;

import java.util.concurrent.ExecutionException;

/**
 * 发送消息到kafka工具类
 *
 * @author pysasuke
 */
public interface MessageProducer {

    /**
     * 发送消息(异步)
     *
     * @param key   消息的key
     * @param value 消息内容
     */
    void sendMessageAsync(String key, byte[] value);

    /**
     * 发送消息(同步)
     *
     * @param key   消息的key
     * @param value 消息内容
     * @throws InterruptedException InterruptedException
     * @throws ExecutionException   ExecutionException
     */
    void sendMessageSync(String key, byte[] value) throws InterruptedException, ExecutionException;

    /**
     * 关闭producer
     */
    void close();

    /**
     * 获取Kafka producer配置
     */
    MessageProducerConfig getConfig();

    /**
     * 获取topic
     */
    String getTopic();
}
