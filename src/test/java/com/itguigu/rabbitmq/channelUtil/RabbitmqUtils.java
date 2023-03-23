package com.itguigu.rabbitmq.channelUtil;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * RabbitMQ 工具类
 * @author darren
 * @create 2021-11-06 17:48
 */
public class RabbitmqUtils {
    private static Logger logger = LoggerFactory.getLogger(RabbitmqUtils.class);
    // 队列持久化
    public static Connection connection;
    public static Channel   channel;

    static {
        // 创建链接工厂
        ConnectionFactory factory = new ConnectionFactory();

        factory.setHost("192.168.6.8");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("123");

        // 创建链接
        try {
            connection = factory.newConnection();
            channel = connection.createChannel();

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 关闭连接
     */
    public static void closeChannelAndConnection() {
        logger.info("close Channel And Connection begin...");
        try {
            channel.close();
            connection.close();
        } catch (IOException e) {
            logger.error("close Channel And Connection IOException error!", e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }
        logger.info("close Channel And Connection success!");
    }

    /**
     * 删除队列
     *
     * @param queueName 队列名称
     * @return
     * @throws IOException
     */
    public static Channel deleteQueue(String queueName) throws IOException {
        channel.queueDelete(queueName);
        return channel;
    }

    /**
     * 删除交换机
     * @param exchangeName
     * @return
     * @throws IOException
     */
    public static Channel deleteExchange(String exchangeName) throws IOException {
        channel.exchangeDelete(exchangeName);
        return channel;
    }
}
