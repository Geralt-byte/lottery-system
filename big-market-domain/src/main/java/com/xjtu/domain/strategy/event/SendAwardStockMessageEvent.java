package com.xjtu.domain.strategy.event;

import com.xjtu.domain.rebate.event.SendRebateMessageEvent;
import com.xjtu.types.event.BaseEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author mlei@xjtu
 * @description SendAwardStockMessageEvent
 * @create 2026/4/2 21:55
 */
@Component
public class SendAwardStockMessageEvent extends BaseEvent<SendAwardStockMessageEvent.AwardStockMessage> {
    @Value("${spring.rabbitmq.topic.send_award_stock}")
    private String topic;

    @Override
    public BaseEvent.EventMessage<SendAwardStockMessageEvent.AwardStockMessage> buildEventMessage(SendAwardStockMessageEvent.AwardStockMessage data) {
        return BaseEvent.EventMessage.<SendAwardStockMessageEvent.AwardStockMessage>builder()
                .id(RandomStringUtils.randomNumeric(11))
                .timestamp(new Date())
                .data(data)
                .build();
    }

    @Override
    public String topic() {
        return topic;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AwardStockMessage {
        /*策略id*/
        private Long activityId;
        /*奖品id*/
        private Integer awardId;
    }
}
