package com.xjtu.trigger.listener;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.xjtu.domain.strategy.event.SendAwardStockMessageEvent;
import com.xjtu.domain.strategy.service.IRaffleStock;
import com.xjtu.types.event.BaseEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author mlei@xjtu
 * @description 奖品库存更新
 * @create 2026/3/30 03:55
 */
@Slf4j
@Component
public class AwardStockCustomer {

    @Value("${spring.rabbitmq.topic.send_award_stock}")
    private String topic;

    @Resource
    private IRaffleStock iRaffleStock;

    @RabbitListener(queuesToDeclare = @Queue(value = "${spring.rabbitmq.topic.send_award_stock}"))
    public void listener(String message) {
        try {
            log.info("监听奖品库存消费发送消息 topic: {} message: {}", topic, message);
            // 解析消息
            BaseEvent.EventMessage<SendAwardStockMessageEvent.AwardStockMessage> eventMessage = JSON.parseObject
                    (message, new TypeReference<BaseEvent.EventMessage<SendAwardStockMessageEvent.AwardStockMessage>>() {
                    }.getType());
            SendAwardStockMessageEvent.AwardStockMessage awardStockMessage = eventMessage.getData();
            // 更新库存
            log.info("MQ消息更新奖品消耗库存 strategyId:{} awardId:{}", awardStockMessage.getActivityId(), awardStockMessage.getAwardId());
            iRaffleStock.updateStrategyAwardStock(awardStockMessage.getActivityId(), awardStockMessage.getAwardId());
        } catch (Exception e) {
            log.error("监听奖品库存消费发送消息，消费失败 topic: {} message: {}", topic, message);
            throw e;
        }
    }
}
