package com.itguigu.rabbitmq.confirm;

import com.itguigu.rabbitmq.util.ExchangeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 发布确认-消息回调类
 *
 * @Author darren
 * @Date 2023/3/21 22:38
 */
@Component
@Slf4j
public class MyCallBack implements RabbitTemplate.ConfirmCallback,RabbitTemplate.ReturnCallback {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    //依赖注入 rabbitTemplate 之后再设置它的回调对象
    @PostConstruct
    public void init(){
        // 确认回调
        rabbitTemplate.setConfirmCallback(this);
        // 返回回调
        rabbitTemplate.setReturnCallback(this);
    }

    /**
     * 交换机不管是否收到消息的一个回调方法
     *
     * @param correlationData: 消息相关数据
     * @param ack: 交换机是否收到消息
     * @param cause
     */
    @Override
    public void confirm(final CorrelationData correlationData, final boolean ack, final String cause) {
        String id = correlationData != null ? correlationData.getId() : "";
        if(ack){
            log.info("交换机确认回调方法已经收到id为:{} 的消息",id);
        }else{
            log.info("交换机确认回调方法还未收到id为:{} 消息, 由于原因:{}", id, cause);
        }
    }

    /**
     * 交换机返回消息的方法-消息未送达队列触发回调
     * 常用于交换机无法路由回退消息
     * 如果交换机绑定了备用交换机则是路由到备用交换机，此方法不回调。
     *
     * @param message the returned message.
     * @param replyCode the 回复 code.
     * @param replyText the 回复 text.
     * @param exchange the exchange.
     * @param routingKey the routing key.
     */
    @Override
    public void returnedMessage(final Message message, final int replyCode, final String replyText,
            final String exchange,
            final String routingKey) {
        // 排除调延迟交换机，因为消息在延迟交换机中延迟，并未送达到队列则出发了此函数回调
        if (!ExchangeUtil.DELAYED_EXCHANGE_NAME.equals(exchange)) {
            log.info("交换机返回消息的方法收到的消息:{} 交换机回复的内容:{}, 交换机是:{}, 路由 key:{}",
                    new String(message.getBody()),replyText, exchange, routingKey);
        }
    }
}
