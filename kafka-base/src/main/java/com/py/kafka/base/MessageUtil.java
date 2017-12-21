package com.py.kafka.base;


import com.py.kafka.base.KafkaMessage.MessageReq;
import com.py.kafka.base.KafkaMessage.MessageRsp;
import com.py.kafka.base.KafkaMessage.MessageType;
import com.py.kafka.base.KafkaMessage.MessageWrap;

/**
 * 消息转换工具类
 *
 * @author pysasuke
 */
public class MessageUtil {


    /**
     * 构建请求消息
     *
     * @param reqId
     * @return
     */
    public static MessageWrap buildMessageReq(String reqId) {
        MessageReq messageReq = reqToMessage(reqId);

        return MessageWrap.newBuilder()
                .setMessageType(MessageType.MT_REQ)
                .setMessageReq(messageReq)
                .build();
    }

    /**
     * 构建应答消息
     *
     * @param rspId
     * @return
     */
    public static MessageWrap buildMessageRsp(String rspId) {
        MessageRsp messageRsp = rspToMessage(rspId);

        return MessageWrap.newBuilder()
                .setMessageType(MessageType.MT_RSP)
                .setMessageRsp(messageRsp)
                .build();
    }


    private static MessageReq reqToMessage(String reqId) {
        return MessageReq.newBuilder()
                .setReqId(reqId)
                .setMessage("reqToMessage")
                .build();
    }

    private static MessageRsp rspToMessage(String repId) {
        return MessageRsp.newBuilder()
                .setRspId(repId)
                .setMessage("rspToMessage")
                .build();
    }


}

