package com.itguigu.rabbitmq.controller;

import com.itguigu.rabbitmq.util.CorrelationDataUtil;
import com.itguigu.rabbitmq.util.ExchangeUtil;
import com.itguigu.rabbitmq.util.QueueUtil;
import com.itguigu.rabbitmq.util.RoutingKeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 延迟消息-生产者
 * @Author darren
 * @Date 2023/3/23 17:49
 */
@RestController
@Slf4j
@RequestMapping("/xExchange")
public class XExchangeController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送消息到X交换机到延迟队列
     * @param message
     * @return
     */
    @GetMapping("/sendMsgToXExchangeToTtlQueue/{message}")
    public String sendMsgToXExchangeToTtlQueue(@PathVariable String message) {
        log.info("发送一条信息给两个TTL队列:{}和:{}",
                QueueUtil.QUEUE_A,
                QueueUtil.QUEUE_B);
        String prefix1 = "消息来自ttl为10S的" + QueueUtil.QUEUE_A + "队列:";
        String prefix2 = "消息来自ttl为40S的" + QueueUtil.QUEUE_B + "队列:";
        rabbitTemplate.convertAndSend(
                ExchangeUtil.X_EXCHANGE,
                RoutingKeyUtil.XA_ROUTING_KEY,
                prefix1 + message,
                CorrelationDataUtil.getCorrelationData());
        log.info(QueueUtil.QUEUE_A + "的消息为：" + prefix1 + message);
        rabbitTemplate.convertAndSend(
                ExchangeUtil.X_EXCHANGE,
                RoutingKeyUtil.XB_ROUTING_KEY,
                prefix2 + message,
                CorrelationDataUtil.getCorrelationData());
        log.info(QueueUtil.QUEUE_B + "的消息为：" + prefix2 + message);
        return "发送到X交换机成功";
    }

    /**
     * 发送延迟消息到X交换机
     *
     * @param message
     * @param ttlTime
     */
    @GetMapping("/sendTtlMsgToXExchange/{message}/{ttlTime}")
    public String sendTtlMsgToXExchange(@PathVariable String message, @PathVariable String ttlTime) {
        rabbitTemplate.convertAndSend(
                ExchangeUtil.X_EXCHANGE,
                RoutingKeyUtil.XC_ROUTING_KEY,
                message,
                messagePostProcessor -> {
                    messagePostProcessor.getMessageProperties().setExpiration(ttlTime);
                    return messagePostProcessor;
                },
                CorrelationDataUtil.getCorrelationData());
        log.info("发送一条时长：{}毫秒 TTL消息：{} 给队列：QC", ttlTime, message);
        return "发送个延迟消息到队列QC";
    }
}
