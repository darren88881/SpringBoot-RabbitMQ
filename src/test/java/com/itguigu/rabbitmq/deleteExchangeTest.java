package com.itguigu.rabbitmq;


import com.itguigu.rabbitmq.util.ExchangeUtilInterface;
import com.itguigu.rabbitmq.util.QueueUtilInterface;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @Author darren
 * @Date 2023/3/21 23:32
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class deleteExchangeTest {
    @Autowired
    AmqpAdmin amqpAdmin;

    @Test
    public void createExchange() {
        amqpAdmin.deleteExchange(ExchangeUtilInterface.X_EXCHANGE);
        amqpAdmin.deleteExchange(ExchangeUtilInterface.Y_DEAD_LETTER_EXCHANGE);
        amqpAdmin.deleteExchange(ExchangeUtilInterface.DELAYED_EXCHANGE_NAME);
        amqpAdmin.deleteExchange(ExchangeUtilInterface.CONFIRM_EXCHANGE_NAME);

        amqpAdmin.deleteQueue(QueueUtilInterface.QUEUE_A);
        amqpAdmin.deleteQueue(QueueUtilInterface.QUEUE_B);
        amqpAdmin.deleteQueue(QueueUtilInterface.QUEUE_C);
        amqpAdmin.deleteQueue(QueueUtilInterface.QUEUE_D);
        amqpAdmin.deleteQueue(QueueUtilInterface.DELAYED_QUEUE_NAME);
        amqpAdmin.deleteQueue(QueueUtilInterface.CONFIRM_QUEUE_NAME);
        System.out.println("删除完成");

    }
}
