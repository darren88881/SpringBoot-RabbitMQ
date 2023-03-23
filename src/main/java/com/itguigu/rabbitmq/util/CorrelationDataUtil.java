package com.itguigu.rabbitmq.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;

/**
 * CorrelationData 工具类
 * @Author darren
 * @Date 2023/3/23 18:03
 */
@Slf4j
public class CorrelationDataUtil {

    /**
     * 获取CorrelationData
     * @return
     */
    public static CorrelationData getCorrelationData() {
        String uuid = UuidUtil.getUuid();
        log.info("UUID为："+uuid);
        CorrelationData correlationData = new CorrelationData(uuid);
        return correlationData;
    }
}
