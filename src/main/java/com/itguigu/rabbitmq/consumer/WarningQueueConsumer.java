package com.itguigu.rabbitmq.consumer;

import com.itguigu.rabbitmq.util.QueueUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 报警队列消费者
 *
 * @Author darren
 * @Date 2023/3/23 16:16
 */
@Component
@Slf4j
public class WarningQueueConsumer {

    @RabbitListener(queues = QueueUtil.WARNING_QUEUE_NAME)
    public void receiveWarningQueueMessage(Message message) {
        String msg = new String(message.getBody());
        log.info("消费者收到队列:{} 的消息:{}", QueueUtil.WARNING_QUEUE_NAME, msg);
    }
}
