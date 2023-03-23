package com.itguigu.rabbitmq;


import com.itguigu.rabbitmq.channelUtil.RabbitmqUtils;
import com.itguigu.rabbitmq.util.ExchangeUtil;
import org.junit.Test;

import java.io.IOException;

/**
 *
 * @Author darren
 * @Date 2023/3/21 23:32
 */
public class deleteExchangeTest {

    @Test
    public void createExchange() throws IOException {
        RabbitmqUtils.deleteExchange(ExchangeUtil.CONFIRM_EXCHANGE_NAME);
        RabbitmqUtils.closeChannelAndConnection();
        System.out.println("删除完成");

    }
}
