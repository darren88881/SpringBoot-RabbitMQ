package com.itguigu.rabbitmq.config;

import com.itguigu.rabbitmq.util.ExchangeUtil;
import com.itguigu.rabbitmq.util.QueueUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 备份交换机及队列
 * @Author darren
 * @Date 2023/3/22 20:10
 */
@Configuration
@Slf4j
public class backupConfig {

    @Bean("backupExchange")
    public FanoutExchange getBackupExchange(){
        return ExchangeBuilder.fanoutExchange(ExchangeUtil.BACKUP_EXCHANGE_NAME).build();
    }

    @Bean("backupQueue")
    public Queue getBackupQueue(){
        return QueueBuilder.durable(QueueUtil.BACKUP_QUEUE_NAME).build();
    }

    @Bean("warningQueue")
    public Queue getWarningQueue(){
        return QueueBuilder.durable(QueueUtil.WARNING_QUEUE_NAME).build();
    }

    @Bean
    public Binding backupQueueBindingBackupExchange(
            @Qualifier("backupQueue") Queue backupQueue,
            @Qualifier("backupExchange") FanoutExchange backupExchange) {
        return BindingBuilder.bind(backupQueue).to(backupExchange);
    }

    @Bean
    public Binding warningQueueBindingBackupExchange(
            @Qualifier("warningQueue") Queue warningQueue,
            @Qualifier("backupExchange") FanoutExchange backupExchange) {
        return BindingBuilder.bind(warningQueue).to(backupExchange);
    }
}
