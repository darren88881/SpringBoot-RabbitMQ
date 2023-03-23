package com.itguigu.rabbitmq;


import com.itguigu.rabbitmq.channelUtil.RabbitmqUtils;
import com.itguigu.rabbitmq.util.ExchangeUtil;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import org.junit.Test;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @Author darren
 * @Date 2023/3/21 23:32
 */
public class deleteExchangeTest {

    private static CountDownLatch cyclicBarrier = new CountDownLatch(2);

    private int num = 0;

    @Test
    public void createExchange() throws IOException {
        RabbitmqUtils.deleteExchange(ExchangeUtil.CONFIRM_EXCHANGE_NAME);
        RabbitmqUtils.closeChannelAndConnection();
        System.out.println("删除完成");

    }

    /**
     * 发送消息到优先级队列
     */
    @Test
    public void sendMsgToPriorityQueue() throws IOException {
        Channel channel = RabbitmqUtils.channel;
        RabbitmqUtils.deleteExchange("priority.exchange");
        RabbitmqUtils.deleteQueue("priority.queue");


        channel.exchangeDeclare("priority.exchange", BuiltinExchangeType.DIRECT,true);

        HashMap<String, Object> arguments = new HashMap<>();
        arguments.put("x-max-priority",10);
        channel.queueDeclare("priority.queue",true, false,false,arguments);

        channel.queueBind("priority.queue","priority.exchange", "priority.routing.key");

        for (int i = 0; i<10; i++) {
            String message = "info"+i;
            int i1 = new Random(i).nextInt(10);
            System.out.println("message:"+message+"priority:"+i1);
            AMQP.BasicProperties basicProperties =
                    new AMQP.BasicProperties().builder().priority(i1).build();
            channel.basicPublish("priority.exchange","priority.routing.key", basicProperties, message.getBytes());
        }
    }

    /**
     * 接受优先级消息
     *
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void acceptMessage() throws IOException, InterruptedException {
        Channel channel = RabbitmqUtils.channel;

        // 传递回调
        DeliverCallback deliverCallback = (consumerTag, message) ->{
            num += 1;
            String msg = new String(message.getBody(),"UTF-8");
            System.out.println("收到的消息："+ msg);
        };
        // 取消回调
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.print("cancelCallback consumerTag:"+consumerTag.toString());
        };


        channel.basicConsume("priority.queue",true, deliverCallback, cancelCallback);

       do {
           // 等待接受线程接受消息完毕后主线程再结束
           TimeUnit.SECONDS.sleep(1);
       } while (num < 10);

        System.out.println("接收完毕");
    }
}
