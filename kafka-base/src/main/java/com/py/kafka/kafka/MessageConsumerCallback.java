package com.py.kafka.kafka;

/**
 * Kafka Consumer回调类
 *
 * @author pysasuke
 */
public interface MessageConsumerCallback {

    /**
     * 收到消息回调
     *
     * @param key
     * @param value
     * @param topic
     * @param partition
     */
    void onMessage(String key, byte[] value, String topic, int partition);
}
