package com.xjtu.domain.award.service;

import com.xjtu.domain.award.event.SendAwardMessageEvent;
import com.xjtu.domain.award.model.aggregate.UserAwardRecordAggregate;
import com.xjtu.domain.award.model.entity.DistributeAwardEntity;
import com.xjtu.domain.award.model.entity.TaskEntity;
import com.xjtu.domain.award.model.entity.UserAwardRecordEntity;
import com.xjtu.domain.award.model.valobj.TaskStateVO;
import com.xjtu.domain.award.repository.IAwardRepository;
import com.xjtu.domain.award.service.distribute.IDistributeAward;
import com.xjtu.types.event.BaseEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author mlei@xjtu
 * @description 奖品服务
 * @create 2026/3/30 02:38
 */
@Slf4j
@Service
public class AwardService implements IAwardService {

    private final IAwardRepository iAwardRepository;
    private final SendAwardMessageEvent sendAwardMessageEvent;
    private final Map<String, IDistributeAward> distributeAwardMap;

    public AwardService(IAwardRepository iAwardRepository, SendAwardMessageEvent sendAwardMessageEvent, Map<String, IDistributeAward> distributeAwardMap) {
        this.iAwardRepository = iAwardRepository;
        this.sendAwardMessageEvent = sendAwardMessageEvent;
        this.distributeAwardMap = distributeAwardMap;
    }

    @Override
    public void saveUserAwardRecord(UserAwardRecordEntity userAwardRecordEntity) {
        //构建消息对象
        SendAwardMessageEvent.SendAwardMessage sendAwardMessage = new SendAwardMessageEvent.SendAwardMessage();
        sendAwardMessage.setUserId(userAwardRecordEntity.getUserId());
        sendAwardMessage.setOrderId(userAwardRecordEntity.getOrderId());
        sendAwardMessage.setAwardId(userAwardRecordEntity.getAwardId());
        sendAwardMessage.setAwardTitle(userAwardRecordEntity.getAwardTitle());
        sendAwardMessage.setAwardConfig(userAwardRecordEntity.getAwardConfig());

        BaseEvent.EventMessage<SendAwardMessageEvent.SendAwardMessage> sendAwardMessageEventMessage = sendAwardMessageEvent.buildEventMessage(sendAwardMessage);

        //构建任务对象
        TaskEntity taskEntity = TaskEntity.builder().userId(userAwardRecordEntity.getUserId()).topic(sendAwardMessageEvent.topic()).messageId(sendAwardMessageEventMessage.getId()).message(sendAwardMessageEventMessage).state(TaskStateVO.create).build();

        //构建聚合对象
        UserAwardRecordAggregate userAwardRecordAggregate = UserAwardRecordAggregate.builder().taskEntity(taskEntity).userAwardRecordEntity(userAwardRecordEntity).build();

        // 存储聚合对象 - 一个事务下，用户的中奖记录
        iAwardRepository.saveUserAwardRecord(userAwardRecordAggregate);
    }

    @Override
    public void distributeAward(DistributeAwardEntity distributeAward) {
        // 奖品Key
        String awardKey = iAwardRepository.queryAwardKey(distributeAward.getAwardId());
        if (StringUtils.isBlank(awardKey)) {
            log.error("分发奖品，奖品ID不存在。awardKey:{}", awardKey);
            return;
        }

        // 奖品服务
        IDistributeAward iDistributeAward = distributeAwardMap.get(awardKey);

        if (iDistributeAward == null) {
            log.error("分发奖品，奖品ID不存在。awardKey:{}", awardKey);
            throw new RuntimeException("分发奖品，奖品" + awardKey + "对应的服务不存在");
        }

        // 发放奖品
        iDistributeAward.getOutPrizes(distributeAward);
    }
}
