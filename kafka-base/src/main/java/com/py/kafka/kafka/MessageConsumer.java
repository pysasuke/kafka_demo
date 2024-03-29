package com.py.kafka.kafka;

/**
 * 从kafka接收消息工具类
 *
 * @author pysasuke
 */
public interface MessageConsumer {

    /**
     * 开始接收消息（异步，非阻塞）
     */
    void start();

    /**
     * 停止接收，并关闭consumer
     */
    void close();
}
