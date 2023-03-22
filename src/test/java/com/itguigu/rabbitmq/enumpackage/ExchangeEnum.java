package com.itguigu.rabbitmq.enumpackage;

/**
 * 枚举类型定义交换机名称
 *
 * @Author darren
 * @Date 2023/3/22 18:16
 */
public enum ExchangeEnum {

    X_EXCHANGE("X");

    private String exchangeName;

    ExchangeEnum (String exchangeName){
        this.exchangeName = exchangeName;
    }
}
