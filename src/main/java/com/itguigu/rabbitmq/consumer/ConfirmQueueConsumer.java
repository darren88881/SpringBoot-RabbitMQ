package com.itguigu.rabbitmq.consumer;

import com.itguigu.rabbitmq.util.QueueUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

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
    public void receiveConfirmQueue(Message message) {
        String msg = new String(message.getBody());
        log.info("消费者收到队列:{} 的消息:{}", QueueUtil.CONFIRM_QUEUE_NAME, msg);
    }
}
