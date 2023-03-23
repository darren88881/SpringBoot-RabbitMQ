package com.itguigu.rabbitmq.util;

import java.util.UUID;

/**
 * UUID工具类
 * @Author darren
 * @Date 2023/3/23 17:53
 */
public class UuidUtil {
    public static String getUuid() {
        return UUID.randomUUID().toString();
    }
}
