package com.xjtu.domain.award.service;

import com.xjtu.domain.award.event.SendAwardMessageEvent;
import com.xjtu.domain.award.model.aggregate.UserAwardRecordAggregate;
import com.xjtu.domain.award.model.entity.TaskEntity;
import com.xjtu.domain.award.model.entity.UserAwardRecordEntity;
import com.xjtu.domain.award.model.valobj.TaskStateVO;
import com.xjtu.domain.award.repository.IAwardRepository;
import com.xjtu.types.event.BaseEvent;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author mlei@xjtu
 * @description 奖品服务
 * @create 2026/3/30 02:38
 */
@Service
public class AwardService implements IAwardService{

    @Resource
    private IAwardRepository iAwardRepository;
    @Resource
    private SendAwardMessageEvent sendAwardMessageEvent;

    @Override
    public void saveUserAwardRecord(UserAwardRecordEntity userAwardRecordEntity) {
        //构建消息对象
        SendAwardMessageEvent.SendAwardMessage sendAwardMessage=new SendAwardMessageEvent.SendAwardMessage();
        sendAwardMessage.setUserId(userAwardRecordEntity.getUserId());
        sendAwardMessage.setAwardId(userAwardRecordEntity.getAwardId());
        sendAwardMessage.setAwardTitle(userAwardRecordEntity.getAwardTitle());

        BaseEvent.EventMessage<SendAwardMessageEvent.SendAwardMessage> sendAwardMessageEventMessage = sendAwardMessageEvent.buildEventMessage(sendAwardMessage);

        //构建任务对象
        TaskEntity taskEntity=TaskEntity.builder()
                .userId(userAwardRecordEntity.getUserId())
                .topic(sendAwardMessageEvent.topic())
                .messageId(sendAwardMessageEventMessage.getId())
                .message(sendAwardMessageEventMessage)
                .state(TaskStateVO.create)
                .build();

        //构建聚合对象
        UserAwardRecordAggregate userAwardRecordAggregate=UserAwardRecordAggregate
                .builder()
                .taskEntity(taskEntity)
                .userAwardRecordEntity(userAwardRecordEntity)
                .build();

        // 存储聚合对象 - 一个事务下，用户的中奖记录
        iAwardRepository.saveUserAwardRecord(userAwardRecordAggregate);
    }
}
