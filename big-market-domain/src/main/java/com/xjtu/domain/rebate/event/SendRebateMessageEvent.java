package com.xjtu.domain.rebate.event;

import com.xjtu.domain.rebate.model.entity.BehaviorRebateOrderEntity;
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
 * @description 发送返利消息事件
 * @create 2026/4/1 08:49
 */
@Component
public class SendRebateMessageEvent extends BaseEvent<SendRebateMessageEvent.RebateMessage> {
    @Value("${spring.rabbitmq.topic.send_rebate}")
    private String topic;

    @Override
    public EventMessage<RebateMessage> buildEventMessage(RebateMessage data) {
        return EventMessage.<SendRebateMessageEvent.RebateMessage>builder()
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
    public static class RebateMessage {
        /*用户ID*/
        private String userId;
        /*返利描述*/
        private String rebateDesc;
        /*返利类型（sku 活动库存充值商品、integral 用户活动积分）*/
        private String rebateType;
        /*返利配置*/
        private String rebateConfig;
        /*业务ID；签到则是日期字符串，支付则是外部的业务ID*/
        private String bizId;
    }
}
