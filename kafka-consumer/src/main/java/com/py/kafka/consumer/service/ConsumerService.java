package com.py.kafka.consumer.service;

import com.py.kafka.base.BaseService;
import com.py.kafka.base.KafkaMessage.MessageType;
import com.py.kafka.base.KafkaMessage.MessageWrap;
import com.py.kafka.base.MessageUtil;
import com.py.kafka.base.ServiceConfig;
import com.py.kafka.base.TopicName;
import com.py.kafka.kafka.MessageConsumer;
import com.py.kafka.kafka.MessageProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;

import static com.py.kafka.kafka.MessageConsumerImpl.getExceptionStack;

/**
 * 状态更新服务
 */
@Service
public class ConsumerService extends BaseService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 请求暂停服务
     */
    private volatile boolean stopServiceRequested = false;

    /**
     * 服务标识
     */
    private final static String SERVICE_ID_FLAG = "kafka-consumer";

    /**
     * 消息队列consumer
     */
    private MessageConsumer messageConsumer;

    /**
     * 消息处理线程数
     */
    private int messageThreadNumber = 10;

    /**
     * 消息队列
     */
    private ArrayBlockingQueue[] messageQueue;
    /**
     * 应答队列producer
     */
    private MessageProducer rspProducer;

    @Autowired
    public ConsumerService() {
        ServiceConfig serviceConfig = ServiceConfig.builder()
                .serviceIdFlag(SERVICE_ID_FLAG)
                .bootstrapServers("xx.xx.x.xxx:9092")
                .build();
        setServiceConfig(serviceConfig);

        rspProducer = createProducer(TopicName.KAFKA_RSP, "consumer");

        messageConsumer = createConsumer(Arrays.asList(TopicName.KAFKA_REQ,
                "test"), null);
        messageConsumer.start();

        startService();
    }

    /**
     * 启动服务
     */
    @Override
    public void startService() {
        messageQueue = new ArrayBlockingQueue[messageThreadNumber];
        for (int i = 0; i < messageThreadNumber; i++) {
            messageQueue[i] = new ArrayBlockingQueue(1);
            Thread thread = new Thread(new StateHandler(i));
            thread.setName("Thread-state_handler-" + i);
            thread.start();
        }
    }

    /**
     * 收到消息回调
     */
    @Override
    @SuppressWarnings(value = {"unchecked"})
    public void onMessage(String key, byte[] value, String topic, int partition) {
        try {
            int mod = Math.abs(key.hashCode() % messageThreadNumber);
            messageQueue[mod].put(value);
        } catch (Exception e) {
            logger.error(getExceptionStack(e));
        }
    }

    @Override
    public void onSuccess(MessageProducer producer, String key, byte[] value, int partition, long offset) {

    }

    @Override
    public void onFailure(MessageProducer producer, String key, byte[] value, Exception e) {

    }

    /**
     * 状态处理
     */
    private class StateHandler implements Runnable {
        private int index;

        StateHandler(int index) {
            this.index = index;
        }

        @Override
        public void run() {
            while (!stopServiceRequested) {
                try {
                    MessageWrap wrap = MessageWrap.parseFrom((byte[]) messageQueue[index].take());
                    if (wrap.getMessageType() == MessageType.MT_REQ) {
                        System.out.println(wrap.getMessageReq().getReqId() + wrap.getMessageReq().getMessage());
                        sendRspMessage(wrap.getMessageReq().getReqId());
                    }
                } catch (Exception e) {
                    logger.error(getExceptionStack(e));
                }
            }
        }
    }

    /**
     * 停止读取新任务
     */
    @Override
    protected void stopReadNewTask() {
        messageConsumer.close();
        stopServiceRequested = true;
    }

    /**
     * 发送发票数据同步请求消息
     */
    private void sendRspMessage(String rspId) {
        MessageWrap messageReq = MessageUtil.buildMessageRsp(rspId);
        rspProducer.sendMessageAsync(rspId, messageReq.toByteArray());
    }

}
