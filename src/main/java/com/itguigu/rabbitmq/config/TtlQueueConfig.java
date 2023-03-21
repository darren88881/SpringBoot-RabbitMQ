package com.itguigu.rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

/**
 * 延迟队列
 *
 * @Author darren
 * @Date 2023/3/21 11:30
 */
@Configuration
public class TtlQueueConfig {
    public static final String X_EXCHANGE = "X";
    public static final String QUEUE_A = "QA";
    public static final String QUEUE_B = "QB";
    public static final String Y_DEAD_LETTER_EXCHANGE = "Y";
    public static final String DEAD_LETTER_QUEUE = "QD";

    @Bean("xExchange")
    public DirectExchange xExchange(){
        return new DirectExchange(X_EXCHANGE);
    }

    @Bean("yExchange")
    public DirectExchange yExchange(){
        return new DirectExchange(Y_DEAD_LETTER_EXCHANGE);
    }

    @Bean("queueA")
    public Queue queueA(){
        HashMap<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange",Y_DEAD_LETTER_EXCHANGE);
        args.put("x-dead-letter-routing-key","YD");
        args.put("x-message-ttl",10000);
        return QueueBuilder.durable(QUEUE_A).withArguments(args).build();
    }

    @Bean("queueB")
    public Queue queueB(){
        HashMap<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange",Y_DEAD_LETTER_EXCHANGE);
        args.put("x-dead-letter-routing-key","YD");
        args.put("x-message-ttl",40000);
        return QueueBuilder.durable(QUEUE_B).withArguments(args).build();
    }

    @Bean("queueD")
    public Queue queueD(){
        return new Queue(DEAD_LETTER_QUEUE);
    }

    @Bean
    public Binding queueABindingxExchange(@Qualifier("queueA") Queue queueA,
                                          @Qualifier("xExchange") DirectExchange xExchange){
        return BindingBuilder.bind(queueA).to(xExchange).with("XA");
    }

    @Bean
    public Binding queueBBindingxExchange(@Qualifier("queueB") Queue queueB,
            @Qualifier("xExchange") DirectExchange xExchange){
        return BindingBuilder.bind(queueB).to(xExchange).with("XB");
    }

    @Bean
    public Binding queueDBindingyExchange(@Qualifier("queueD") Queue queueD,
            @Qualifier("yExchange") DirectExchange yExchange){
        return BindingBuilder.bind(queueD).to(yExchange).with("YD");
    }
}
