package com.itguigu.rabbitmq.controller;

import com.itguigu.rabbitmq.confirm.MyCallBack;
import com.itguigu.rabbitmq.util.ExchangeUtilInterface;
import com.itguigu.rabbitmq.util.RoutingKeyUtilInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.sql.SQLOutput;
import java.util.UUID;

/**
 * 发布确认模式-生产者
 * @Author darren
 * @Date 2023/3/21 22:12
 */
@RestController
@Slf4j
@RequestMapping("/confirm")
public class ProducerController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private MyCallBack myCallBack;

    //依赖注入 rabbitTemplate 之后再设置它的回调对象
    @PostConstruct
    public void init(){
        rabbitTemplate.setConfirmCallback(myCallBack);
    }

    /**
     * 发布消息
     *
     * 发到交换机正确的路由可以消费成功
     * 发到交换机错误的路由无法消费，但交换机可以收到消息，因无法路由所以到达不了队列
     * @param message
     * @return
     */
    @GetMapping("/sendConfirmMsg/{message}")
    public String sendConfirmMsg(@PathVariable String message) {
        UUID uuid = UUID.randomUUID();
        CorrelationData correlationData = new CorrelationData(uuid.toString());

        log.info("发送消息:{} 到交换机:{} ",message, ExchangeUtilInterface.CONFIRM_EXCHANGE_NAME);
        rabbitTemplate.convertAndSend(
                ExchangeUtilInterface.CONFIRM_EXCHANGE_NAME,
                RoutingKeyUtilInterface.CONFIRM_ROUTING_KEY,
                message,
                correlationData);

        rabbitTemplate.convertAndSend(
                ExchangeUtilInterface.CONFIRM_EXCHANGE_NAME,
                RoutingKeyUtilInterface.CONFIRM_ERROR_ROUTING_KEY,
                message,
                correlationData);
        return "发布确认模式发送消息成功";
    }
}
