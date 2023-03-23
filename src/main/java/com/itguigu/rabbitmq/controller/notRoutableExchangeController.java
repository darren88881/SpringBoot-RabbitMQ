package com.itguigu.rabbitmq.controller;

import com.itguigu.rabbitmq.util.CorrelationDataUtil;
import com.itguigu.rabbitmq.util.ExchangeUtil;
import com.itguigu.rabbitmq.util.RoutingKeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 不可路由交换机-生产者
 * @Author darren
 * @Date 2023/3/23 20:16
 */
@RestController
@Slf4j
@RequestMapping("/notRoutableExchange")
public class notRoutableExchangeController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RequestMapping("/sendMessage/{message}")
    public String sendNotRoutableExchangeMsg(@PathVariable("message") String message) {
        rabbitTemplate.convertAndSend(
                ExchangeUtil.NOT_ROUTABLE_EXCHANGE_NAME,
                RoutingKeyUtil.NOT_ROUTABLE_ERROR_ROUTING_KEY,
                message,
                CorrelationDataUtil.getCorrelationData());
        return "发送到不可路由交换机的消息成功";
    }
}
