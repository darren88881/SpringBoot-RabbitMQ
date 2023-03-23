package com.itguigu.rabbitmq.config;

import com.itguigu.rabbitmq.util.ExchangeUtil;
import com.itguigu.rabbitmq.util.QueueUtil;
import com.itguigu.rabbitmq.util.RoutingKeyUtil;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

/**
 * 1-延迟队列
 *
 * @Author darren
 * @Date 2023/3/21 11:30
 */
@Configuration
public class TtlQueueConfig {

    @Bean("xExchange")
    public DirectExchange xExchange() {
        return ExchangeBuilder.directExchange(ExchangeUtil.X_EXCHANGE).build();
    }

    @Bean("yExchange")
    public DirectExchange yExchange(){
        return ExchangeBuilder.directExchange(ExchangeUtil.Y_DEAD_LETTER_EXCHANGE).build();
    }

    @Bean("queueA")
    public Queue queueA(){
        return QueueBuilder
                .durable(QueueUtil.QUEUE_A)
                .deadLetterExchange(ExchangeUtil.Y_DEAD_LETTER_EXCHANGE)
                .deadLetterRoutingKey(RoutingKeyUtil.YD_ROUTING_KEY)
                .ttl(10000).build();
    }

    @Bean("queueB")
    public Queue queueB(){
         return QueueBuilder
                 .durable(QueueUtil.QUEUE_B)
                 .deadLetterExchange(ExchangeUtil.Y_DEAD_LETTER_EXCHANGE)
                 .deadLetterRoutingKey(RoutingKeyUtil.YD_ROUTING_KEY)
                 .ttl(40000).build();
    }

    @Bean("queueD")
    public Queue queueD(){
        return new Queue(QueueUtil.QUEUE_D);
    }

    @Bean
    public Binding queueABindingxExchange(@Qualifier("queueA") Queue queueA,
                                          @Qualifier("xExchange") DirectExchange xExchange){
        return BindingBuilder.bind(queueA).to(xExchange).with(RoutingKeyUtil.XA_ROUTING_KEY);
    }

    @Bean
    public Binding queueBBindingxExchange(@Qualifier("queueB") Queue queueB,
            @Qualifier("xExchange") DirectExchange xExchange){
        return BindingBuilder.bind(queueB).to(xExchange).with(RoutingKeyUtil.XB_ROUTING_KEY);
    }

    @Bean
    public Binding queueDBindingyExchange(@Qualifier("queueD") Queue queueD,
            @Qualifier("yExchange") DirectExchange yExchange){
        return BindingBuilder.bind(queueD).to(yExchange).with(RoutingKeyUtil.YD_ROUTING_KEY);
    }
}
