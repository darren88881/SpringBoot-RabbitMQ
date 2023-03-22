package com.itguigu.rabbitmq.controller;

import com.itguigu.rabbitmq.util.ExchangeUtilInterface;
import com.itguigu.rabbitmq.util.QueueUtilInterface;
import com.itguigu.rabbitmq.util.RoutingKeyUtilInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

/**
 *
 * @Author darren
 * @Date 2023/3/21 17:20
 */
@RestController
@Slf4j
@RequestMapping("/ttl")
public class SendMsgController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送到延迟队列
     * @param message
     * @return
     */
    @GetMapping("/sendMsg/{message}")
    public String sendMsg(@PathVariable String message) {
        UUID uuid = UUID.randomUUID();
        CorrelationData correlationData = new CorrelationData(uuid.toString());

        log.info("发送一条信息给两个TTL队列:{}和:{}",
                QueueUtilInterface.QUEUE_A,
                QueueUtilInterface.QUEUE_B);
        String prefix1 = "消息来自ttl为10S的"+QueueUtilInterface.QUEUE_A+"队列:";
        String prefix2 = "消息来自ttl为40S的"+QueueUtilInterface.QUEUE_B+"队列:";
        rabbitTemplate.convertAndSend(
                ExchangeUtilInterface.X_EXCHANGE,
                RoutingKeyUtilInterface.XA_ROUTING_KEY,
                prefix1 + message,
                correlationData);
        log.info(QueueUtilInterface.QUEUE_A+"的消息为："+prefix1 + message);
        rabbitTemplate.convertAndSend(
                ExchangeUtilInterface.X_EXCHANGE,
                RoutingKeyUtilInterface.XB_ROUTING_KEY,
                prefix2 + message,
                correlationData);
        log.info(QueueUtilInterface.QUEUE_B+"的消息为："+prefix2 + message);
        return "发送到X交换机成功";
    }

    /**
     * 发送延迟消息
     * @param message
     * @param ttlTime
     */
    @GetMapping("/sendExpirationMsg/{message}/{ttlTime}")
    public String sendExpirationMsg(@PathVariable String message, @PathVariable String ttlTime) {
        UUID uuid = UUID.randomUUID();
        CorrelationData correlationData = new CorrelationData(uuid.toString());

        rabbitTemplate.convertAndSend(
                ExchangeUtilInterface.X_EXCHANGE,
                RoutingKeyUtilInterface.XC_ROUTING_KEY,
                message,
                messagePostProcessor -> {
                    messagePostProcessor.getMessageProperties().setExpiration(ttlTime);
                    return messagePostProcessor;
                },
                correlationData);
        log.info("发送一条时长：{}毫秒 TTL消息：{} 给队列：QC", ttlTime, message);
        return "发送个延迟消息到队列QC";
    }

    /**
     * 发送到延时交换机
     * 消息在交换机中具有按时间排序的功能
     * @return
     */
    @GetMapping("/sendDelayedExchangeMsg/{message}/{delayTime}")
    public String sendDelayedExchangeMsg(@PathVariable String message,
            @PathVariable Integer delayTime) {
        UUID uuid = UUID.randomUUID();
        CorrelationData correlationData = new CorrelationData(uuid.toString());
        rabbitTemplate.convertAndSend(
                ExchangeUtilInterface.DELAYED_EXCHANGE_NAME,
                RoutingKeyUtilInterface.DELAYED_ROUTING_KEY,
                message,
                messagePostProcessor -> {
                    messagePostProcessor.getMessageProperties().setDelay(delayTime);
                    return messagePostProcessor;
                },
                correlationData);
        log.info("发送一条延迟:{} 毫秒的消息:{} 给交换机:{}",
                delayTime, message, ExchangeUtilInterface.DELAYED_EXCHANGE_NAME);
        return "发送延时交换机的消息成功";
    }
}
