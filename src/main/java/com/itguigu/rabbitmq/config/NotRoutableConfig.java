package com.itguigu.rabbitmq.config;

import com.itguigu.rabbitmq.util.ExchangeUtil;
import com.itguigu.rabbitmq.util.QueueUtil;
import com.itguigu.rabbitmq.util.RoutingKeyUtil;
import lombok.extern.slf4j.Slf4j;
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
 * 不可路由的交换机和队列配置
 *
 * @Author darren
 * @Date 2023/3/23 20:02
 */
@Configuration
@Slf4j
public class NotRoutableConfig {

    @Bean("notRoutableExchange")
    public DirectExchange getNotRoutableExchange(){
        return ExchangeBuilder.directExchange(ExchangeUtil.NOT_ROUTABLE_EXCHANGE_NAME).build();
    }

    @Bean("notRoutableQueue")
    public Queue getNotRoutableQueue() {
        return QueueBuilder.durable(QueueUtil.NOT_ROUTABLE_QUEUE_NAME).build();
    }

    @Bean
    public Binding getBinding(
            @Qualifier("notRoutableQueue") Queue notRoutableQueue,
            @Qualifier("notRoutableExchange") DirectExchange notRoutableExchange){
        return BindingBuilder.bind(notRoutableQueue).to(notRoutableExchange).with(RoutingKeyUtil.NOT_ROUTABLE_RIGHT_ROUTING_KEY);
    }
}
