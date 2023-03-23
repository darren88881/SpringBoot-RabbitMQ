package com.itguigu.rabbitmq.consumer;

import com.itguigu.rabbitmq.util.QueueUtil;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 延迟队列和延迟消息消费者
 *
 * @Author darren
 * @Date 2023/3/21 17:34
 */
@Component
@Slf4j
public class TtlQueueAndTtlMsgConsumer {

    @RabbitListener(queues = QueueUtil.QUEUE_D)
    public void receiveD(Message message, Channel channel) throws IOException {
        String msg = new String(message.getBody());
        log.info("消费者收到队列:{} 的消息:{}", QueueUtil.QUEUE_D, msg);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    }
}
