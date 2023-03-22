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
        return ExchangeBuilder.directExchange(ExchangeUtilInterface.X_EXCHANGE).build();
    }

    @Bean("yExchange")
    public DirectExchange yExchange(){
        return ExchangeBuilder.directExchange(ExchangeUtilInterface.Y_DEAD_LETTER_EXCHANGE).build();
    }

    @Bean("queueA")
    public Queue queueA(){
        HashMap<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange",ExchangeUtilInterface.Y_DEAD_LETTER_EXCHANGE);
        args.put("x-dead-letter-routing-key",RoutingKeyUtilInterface.YD_ROUTING_KEY);
        args.put("x-message-ttl",10000);
        return QueueBuilder.durable(QueueUtilInterface.QUEUE_A).withArguments(args).build();
    }

    @Bean("queueB")
    public Queue queueB(){
        HashMap<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange",ExchangeUtilInterface.Y_DEAD_LETTER_EXCHANGE);
        args.put("x-dead-letter-routing-key",RoutingKeyUtilInterface.YD_ROUTING_KEY);
        args.put("x-message-ttl",40000);
         return QueueBuilder.durable(QueueUtilInterface.QUEUE_B).withArguments(args).build();
    }

    @Bean("queueD")
    public Queue queueD(){
        return new Queue(QueueUtilInterface.QUEUE_D);
    }

    @Bean
    public Binding queueABindingxExchange(@Qualifier("queueA") Queue queueA,
                                          @Qualifier("xExchange") DirectExchange xExchange){
        return BindingBuilder.bind(queueA).to(xExchange).with(RoutingKeyUtilInterface.XA_ROUTING_KEY);
    }

    @Bean
    public Binding queueBBindingxExchange(@Qualifier("queueB") Queue queueB,
            @Qualifier("xExchange") DirectExchange xExchange){
        return BindingBuilder.bind(queueB).to(xExchange).with(RoutingKeyUtilInterface.XB_ROUTING_KEY);
    }

    @Bean
    public Binding queueDBindingyExchange(@Qualifier("queueD") Queue queueD,
            @Qualifier("yExchange") DirectExchange yExchange){
        return BindingBuilder.bind(queueD).to(yExchange).with(RoutingKeyUtilInterface.YD_ROUTING_KEY);
    }
}
