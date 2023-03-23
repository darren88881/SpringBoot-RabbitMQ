package com.itguigu.rabbitmq.util;

/**
 *
 * @Author darren
 * @Date 2023/3/21 22:26
 */
public interface RoutingKeyUtil {
    String XA_ROUTING_KEY = "xa";
    String XB_ROUTING_KEY = "xb";
    String XC_ROUTING_KEY = "xc";
    String YD_ROUTING_KEY = "yd";
    String DELAYED_ROUTING_KEY = "delayed.routing.key";
    String CONFIRM_ROUTING_KEY = "confirm.routing.key";
    String CONFIRM_ERROR_ROUTING_KEY = "confirm.error.routing.key";
    String NOT_ROUTABLE_RIGHT_ROUTING_KEY = "not.routable.right.routing.key";
    String NOT_ROUTABLE_ERROR_ROUTING_KEY = "not.routable.error.routing.key";
}
