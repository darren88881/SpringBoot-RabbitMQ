package com.itguigu.rabbitmq.config;

import com.itguigu.rabbitmq.util.ExchangeUtilInterface;
import com.itguigu.rabbitmq.util.QueueUtilInterface;
import com.itguigu.rabbitmq.util.RoutingKeyUtilInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 2-延时消息
 * @Author darren
 * @Date 2023/3/21 18:00
 */
@Configuration
@Slf4j
public class MsgTtlQueueConfig {


    @Bean("queueC")
    public Queue queueC(){
        return QueueBuilder.durable(QueueUtilInterface.QUEUE_C)
                //声明当前队列绑定的死信交换机
                .deadLetterExchange(ExchangeUtilInterface.Y_DEAD_LETTER_EXCHANGE)
                //声明当前队列的死信路由 key
                .deadLetterRoutingKey(RoutingKeyUtilInterface.YD_ROUTING_KEY)
                .build();
    }

    @Bean
    public Binding queueCBindingExchange(@Qualifier("queueC") Queue queueC,
            @Qualifier("xExchange") DirectExchange xExchange){
        return BindingBuilder.bind(queueC).to(xExchange).with(RoutingKeyUtilInterface.XC_ROUTING_KEY);
    }

}
