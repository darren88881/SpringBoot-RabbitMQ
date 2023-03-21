package com.itguigu.rabbitmq.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

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
        log.info("当前时间：{},发送一条信息给两个 TTL 队列:{}", new Date(), message);
        String prefix1 = "消息来自 ttl 为 10S 的队列: ";
        String prefix2 = "消息来自 ttl 为 40S 的队列: ";
        rabbitTemplate.convertAndSend("X","XA",prefix1+message);
        rabbitTemplate.convertAndSend("X","XB",prefix2+message);
        return "发送到X交换机成功";
    }

    /**
     * 发送延迟消息
     * @param message
     * @param ttlTime
     */
    @GetMapping("/sendExpirationMsg/{message}/{ttlTime}")
    public String sendMsg(@PathVariable String message,@PathVariable String ttlTime) {
        rabbitTemplate.convertAndSend("X", "XC", message, correlationData ->{
            correlationData.getMessageProperties().setExpiration(ttlTime);
            return correlationData;
        });
        log.info("当前时间：{},发送一条时长：{}毫秒 TTL消息：{} 给队列：QC", new Date(),ttlTime, message);
        return "发送个延迟消息到队列QC";
    }
}
