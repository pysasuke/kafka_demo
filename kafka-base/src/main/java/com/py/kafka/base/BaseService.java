package com.py.kafka.base;


import com.py.kafka.kafka.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 服务基类
 *
 * @author pysasuke
 */
public abstract class BaseService implements MessageConsumerCallback, MessageProducerCallback {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * 从kafka中获取消息批次默认大小
     */
    private final static int DEFAULT_BATCH_SIZE = 1;

    /**
     * service配置
     */
    private ServiceConfig serviceConfig;

    /**
     * 启动服务
     */
    protected abstract void startService();

    /**
     * 设置服务配置
     */
    protected void setServiceConfig(ServiceConfig serviceConfig) {
        if (serviceConfig.getMaxPollRecords() <= 0) {
            serviceConfig.setMaxPollRecords(DEFAULT_BATCH_SIZE);
        }

        this.serviceConfig = serviceConfig;
        String bootstrapServers = serviceConfig.getBootstrapServers();
        if (StringUtils.isBlank(bootstrapServers) ||
                !isValidClusterAddress(bootstrapServers)) {
            logger.error("该分区的kafka broker地址没有正确配置： [" + bootstrapServers + "]");
            System.exit(1);
        }
    }

    /**
     * 创建producer
     */
    protected MessageProducer createProducer(String topic, String clientIdSuffix) {
        String clientId = "client-" + serviceConfig.getServiceIdFlag();
        if (!StringUtils.isBlank(clientIdSuffix)) {
            clientId += ("-" + clientIdSuffix);
        }

        MessageProducerConfig config = MessageProducerConfig.builder()
                .topic(topic)
                .producerCallback(this)
                .bootstrapServers(serviceConfig.getBootstrapServers())
                .clientId(clientId)
                .build();
        return new MessageProducerImpl(config);
    }

    /**
     * 创建consumer
     */
    protected MessageConsumer createConsumer(List<String> topics, String clientIdSuffix) {
        String clientId = "client-" + serviceConfig.getServiceIdFlag();
        if (!StringUtils.isBlank(clientIdSuffix)) {
            clientId += ("-" + clientIdSuffix);
        }

        MessageConsumerConfig config = MessageConsumerConfig.builder()
                .topics(topics)
                .consumerCallback(this)
                .bootstrapServers(serviceConfig.getBootstrapServers())
                .groupId("group-" + serviceConfig.getServiceIdFlag())
                .clientId(clientId)
                .maxPollRecords(serviceConfig.getMaxPollRecords())
                .build();
        return new MessageConsumerImpl(config);
    }

    /**
     * 校验是否是有效的集群地址格式("1.2.3.4:5678,host:7890")
     */
    public static boolean isValidClusterAddress(String clusterAddress) {
        String[] addressArray = clusterAddress.split(",");
        if (addressArray.length < 1) {
            return false;
        }

        String regex = "\\S{3,}:\\d{2,5}";
        for (String address : addressArray) {
            if (!address.trim().matches(regex)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 停止读取新任务
     */
    protected abstract void stopReadNewTask();
}
