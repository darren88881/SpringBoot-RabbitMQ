package com.itguigu.rabbitmq.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 消费者
 *
 * @Author darren
 * @Date 2023/3/21 17:34
 */
@Component
@Slf4j
public class DeadLetterQueueConsumer {
    @RabbitListener(queues = "QD")
    public void receiveD(Message message) {
        String msg = new String(message.getBody());
        log.info("当前时间：{},收到死信队列信息：{}", new Date().toString(), msg);
    }

    @RabbitListener(queues = "delayed.queue")
    public void receiveDelayedQueue(Message message) {
        String msg = new String(message.getBody());
        log.info("当前时间：{},收到延交换机绑定队列的消息：{}", new Date().toString(), msg);
    }
}
