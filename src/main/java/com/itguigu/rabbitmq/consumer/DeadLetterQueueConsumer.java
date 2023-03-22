package com.itguigu.rabbitmq.consumer;

import com.itguigu.rabbitmq.util.QueueUtilInterface;
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

    @RabbitListener(queues = QueueUtilInterface.QUEUE_D)
    public void receiveD(Message message) {
        String msg = new String(message.getBody());
        log.info("当前时间：{},收到死信队列:{} 的信息：{}",
                new Date().toString(),
                QueueUtilInterface.QUEUE_D,
                msg);
    }

    @RabbitListener(queues = QueueUtilInterface.DELAYED_QUEUE_NAME)
    public void receiveDelayedQueue(Message message) {
        String msg = new String(message.getBody());
        log.info("当前时间：{},收到延交换机绑定队列:{} 的消息：{}",
                new Date().toString(),
                QueueUtilInterface.DELAYED_QUEUE_NAME,
                msg);
    }
}
