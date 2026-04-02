package com.xjtu.trigger.listener;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.xjtu.domain.activity.model.entity.SkuRechargeEntity;
import com.xjtu.domain.activity.service.IRaffleActivityAccountQuotaService;
import com.xjtu.domain.rebate.event.SendRebateMessageEvent;
import com.xjtu.domain.rebate.model.valobj.RebateTypeVO;
import com.xjtu.types.enums.ResponseCode;
import com.xjtu.types.event.BaseEvent;
import com.xjtu.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author mlei@xjtu
 * @description 监听；行为返利消息
 * @create 2026/4/1 10:18
 */
@Slf4j
@Component
public class RebateMessageCustomer {

    @Resource
    private IRaffleActivityAccountQuotaService iRaffleActivityAccountQuotaService;

    @Value("${spring.rabbitmq.topic.send_rebate}")
    private String topic;

    @RabbitListener(queuesToDeclare = @Queue(value = "${spring.rabbitmq.topic.send_rebate}"))
    public void listener(String message) {
        try {
            log.info("监听用户行为返利消息 topic: {} message: {}", topic, message);
            // 1. 解析消息
            BaseEvent.EventMessage<SendRebateMessageEvent.RebateMessage> eventMessage = JSON.parseObject
                    (message, new TypeReference<BaseEvent.EventMessage<SendRebateMessageEvent.RebateMessage>>() {
                    }.getType());
            SendRebateMessageEvent.RebateMessage rebateMessage = eventMessage.getData();
            if (!rebateMessage.getRebateType().equals(RebateTypeVO.SKU.getCode())) {
                log.info("监听用户行为返利消息 - 非sku奖励暂不处理 topic: {} message: {}", topic, message);
                return;
            }
            // 2. 入账奖励
            SkuRechargeEntity skuRechargeEntity = new SkuRechargeEntity();
            skuRechargeEntity.setUserId(rebateMessage.getUserId());
            skuRechargeEntity.setSku(Long.valueOf(rebateMessage.getRebateConfig()));
            skuRechargeEntity.setOutBusinessNo(rebateMessage.getBizId());
            iRaffleActivityAccountQuotaService.createOrder(skuRechargeEntity);
            log.info("监听用户行为返利消息 - sku奖励入账成功 topic: {} message: {}", topic, message);
        } catch (AppException e) {
            if (ResponseCode.INDEX_DUP.getCode().equals(e.getCode())) {
                log.warn("监听用户行为返利消息，消费重复 topic: {} message: {}", topic, message, e);
                return;
            }
            throw  e;
        } catch (Exception e) {
            log.error("监听用户行为返利消息，消费失败 topic: {} message: {}", topic, message, e);
            throw e;
        }
    }
}
