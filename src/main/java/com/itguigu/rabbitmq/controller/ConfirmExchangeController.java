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
 * 发布确认模式-生产者
 * @Author darren
 * @Date 2023/3/21 22:12
 */
@RestController
@Slf4j
@RequestMapping("/confirmExchange")
public class ConfirmExchangeController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发布消息
     *
     * 发到交换机正确的路由可以消费成功
     * 发到交换机错误的路由无法消费，但交换机可以收到消息，因无法路由所以到达不了队列
     * @param message
     * @return
     */
    @GetMapping("/sendMessage/{message}")
    public String sendMessage(@PathVariable String message) {

        log.info("发送消息:{} 到交换机:{} ",message, ExchangeUtil.CONFIRM_EXCHANGE_NAME);
        rabbitTemplate.convertAndSend(
                ExchangeUtil.CONFIRM_EXCHANGE_NAME,
                RoutingKeyUtil.CONFIRM_ROUTING_KEY,
                message,
                CorrelationDataUtil.getCorrelationData());

        rabbitTemplate.convertAndSend(
                ExchangeUtil.CONFIRM_EXCHANGE_NAME,
                RoutingKeyUtil.CONFIRM_ERROR_ROUTING_KEY,
                message,
                CorrelationDataUtil.getCorrelationData());
        return "发布确认模式发送消息成功";
    }

}
