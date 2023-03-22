package com.itguigu.rabbitmq.config;

import com.itguigu.rabbitmq.util.ExchangeUtilInterface;
import com.itguigu.rabbitmq.util.QueueUtilInterface;
import com.itguigu.rabbitmq.util.RoutingKeyUtilInterface;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 发布确认-配置交换机和队列
 * @Author darren
 * @Date 2023/3/21 22:02
 */
@Configuration
public class ConfirmConfig {

    @Bean("confirmExchange")
    public DirectExchange confirmExchange() {
        return ExchangeBuilder.directExchange(ExchangeUtilInterface.CONFIRM_EXCHANGE_NAME).build();
    }

    @Bean("confirmQueue")
    public Queue confirmQueue() {
        return QueueBuilder.durable(QueueUtilInterface.CONFIRM_QUEUE_NAME).build();
    }

    @Bean
    public Binding confirmQueueBindingConfirmExchange(
            @Qualifier("confirmExchange") DirectExchange confirmExchange,
            @Qualifier("confirmQueue") Queue confirmQueue) {
        return BindingBuilder.bind(confirmQueue).to(confirmExchange).with(RoutingKeyUtilInterface.CONFIRM_ROUTING_KEY);
    }
}
