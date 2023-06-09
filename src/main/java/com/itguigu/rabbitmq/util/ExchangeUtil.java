package com.itguigu.rabbitmq.util;

/**
 *
 * @Author darren
 * @Date 2023/3/21 22:17
 */
public interface ExchangeUtil {
    String X_EXCHANGE = "X";
    String Y_DEAD_LETTER_EXCHANGE = "Y";
    String DELAYED_EXCHANGE_NAME = "delayed.exchange";
    String CONFIRM_EXCHANGE_NAME = "confirm.exchange";
    String BACKUP_EXCHANGE_NAME = "backup.exchange";
    String NOT_ROUTABLE_EXCHANGE_NAME = "not.routable.exchange";
}
