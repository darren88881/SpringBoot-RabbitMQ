package com.itguigu.rabbitmq.confirm;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * 发布确认-消息回调类
 *
 * @Author darren
 * @Date 2023/3/21 22:38
 */
@Component
@Slf4j
public class MyCallBack implements RabbitTemplate.ConfirmCallback {

    /**
     * 交换机不管是否收到消息的一个回调方法
     *
     * @param correlationData: 消息相关数据
     * @param ack: 交换机是否收到消息
     * @param cause
     */
    @Override
    public void confirm(final CorrelationData correlationData, final boolean ack, final String cause) {
        String id = correlationData != null ? correlationData.getId() : "";
        if(ack){
            log.info("交换机已经收到id为:{} 的消息",id);
        }else{
            log.info("交换机还未收到id为:{} 消息, 由于原因:{}", id, cause);
        }
    }
}
