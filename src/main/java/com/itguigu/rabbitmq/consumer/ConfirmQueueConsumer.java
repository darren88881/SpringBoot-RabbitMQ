package com.itguigu.rabbitmq.consumer;

import com.itguigu.rabbitmq.util.QueueUtil;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 发布确认模式队列消费者
 *
 * @Author darren
 * @Date 2023/3/21 22:49
 */
@Component
@Slf4j
public class ConfirmQueueConsumer {

    @RabbitListener(queues = QueueUtil.CONFIRM_QUEUE_NAME)
    public void receiveConfirmQueue(Message message, Channel channel) throws IOException {
        String msg = new String(message.getBody());
        log.info("消费者收到队列:{} 的消息:{}", QueueUtil.CONFIRM_QUEUE_NAME, msg);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    }
}
