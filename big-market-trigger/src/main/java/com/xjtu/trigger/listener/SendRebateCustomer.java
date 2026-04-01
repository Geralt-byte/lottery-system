package com.xjtu.trigger.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author mlei@xjtu
 * @description BehaviorRebateCustomer
 * @create 2026/4/1 10:18
 */
@Slf4j
@Component
public class SendRebateCustomer {
    @Value("${spring.rabbitmq.topic.send_rebate}")
    private String topic;

    @RabbitListener(queuesToDeclare = @Queue(value = "${spring.rabbitmq.topic.send_rebate}"))
    public void listener(String message) {
        try {
            log.info("监听用户活动返利发送消息 topic: {} message: {}", topic, message);
        } catch (Exception e) {
            log.error("监听用户活动返利发送消息，消费失败 topic: {} message: {}", topic, message);
            throw e;
        }
    }
}
