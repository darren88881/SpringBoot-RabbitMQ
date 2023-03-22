package com.itguigu.rabbitmq.config;

import com.itguigu.rabbitmq.util.ExchangeUtilInterface;
import com.itguigu.rabbitmq.util.QueueUtilInterface;
import com.itguigu.rabbitmq.util.RoutingKeyUtilInterface;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 3-延时消息插件
 *
 * 原理：
 *  Exchange类型 x-delayed-message ，该类型消息支持延迟投递机制。
 *  接收到消息后并不会立即将消息投递至目标队列，而是存储在mnesia table(一个分布式数据库)中，
 *  然后检测消息延迟时间，如果达到可投递时间( 过期时间 )后，将其通过 x-delayed-type
 *  类型标记的交换机投递到目标队列中。
 * @Author darren
 * @Date 2023/3/21 20:56
 */
@Configuration
public class DelayedQueueConfig {

    /**
     * 自定义交换机-延迟交换机
     * @return
     */
    @Bean("delayedExchange")
    public CustomExchange delayedExchange(){
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange(
                ExchangeUtilInterface.DELAYED_EXCHANGE_NAME,
                "x-delayed-message", true, false, args);
    }

    /**
     * 普通队列
     *
     * @return
     */
    @Bean("delayedQueue")
    public Queue delayedQueue(){
        return QueueBuilder.durable(QueueUtilInterface.DELAYED_QUEUE_NAME).build();
    }

    /**
     * 队列绑定延时交换机
     * @param delayedQueue
     * @param delayedExchange
     * @return
     */
    @Bean
    public Binding delayedQueueBindingDelayedExchange(
            @Qualifier("delayedQueue") Queue delayedQueue,
            @Qualifier("delayedExchange") CustomExchange delayedExchange ){
        return BindingBuilder.bind(delayedQueue).to(delayedExchange)
                .with(RoutingKeyUtilInterface.DELAYED_ROUTING_KEY).noargs();
    }
}
