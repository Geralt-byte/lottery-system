package com.xjtu.domain.award.model.entity;

import com.xjtu.domain.award.event.SendAwardMessageEvent;
import com.xjtu.domain.award.model.valobj.TaskStateVO;
import com.xjtu.types.event.BaseEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author mlei@xjtu
 * @description 任务实体对象
 * @create 2026/3/30 02:22
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskEntity {
    /*用户id*/
    private String userId;
    /*消息主题*/
    private String topic;
    /*消息编号*/
    private String messageId;
    /*消息主体*/
    private BaseEvent.EventMessage<SendAwardMessageEvent.SendAwardMessage> message;
    /*任务状态；create-创建、completed-完成、fail-失败*/
    private TaskStateVO state;
}
