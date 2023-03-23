package com.itguigu.rabbitmq.controller;

import com.itguigu.rabbitmq.util.CorrelationDataUtil;
import com.itguigu.rabbitmq.util.ExchangeUtil;
import com.itguigu.rabbitmq.util.RoutingKeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 延时交换机-生产者
 *
 * @Author darren
 * @Date 2023/3/21 17:20
 */
@RestController
@Slf4j
@RequestMapping("/delayedExchange")
public class DelayedExchangeController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送到延时交换机
     * 消息在交换机中具有按时间排序的功能
     * @return
     */
    @GetMapping("/sendMessage/{message}/{delayTime}")
    public String sendMessage(@PathVariable String message,
            @PathVariable Integer delayTime) {
        rabbitTemplate.convertAndSend(
                ExchangeUtil.DELAYED_EXCHANGE_NAME,
                RoutingKeyUtil.DELAYED_ROUTING_KEY,
                message,
                messagePostProcessor -> {
                    messagePostProcessor.getMessageProperties().setDelay(delayTime);
                    return messagePostProcessor;
                },
                CorrelationDataUtil.getCorrelationData());
        log.info("发送一条延迟:{} 毫秒的消息:{} 给交换机:{}",
                delayTime, message, ExchangeUtil.DELAYED_EXCHANGE_NAME);
        return "发送延时交换机的消息成功";
    }
}
