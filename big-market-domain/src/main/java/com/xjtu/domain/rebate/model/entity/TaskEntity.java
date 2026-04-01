package com.xjtu.domain.rebate.model.entity;

import com.xjtu.domain.rebate.event.SendRebateMessageEvent;
import com.xjtu.domain.rebate.model.valobj.TaskStateVO;
import com.xjtu.types.event.BaseEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mlei@xjtu
 * @description 任务实体对象
 * @create 2026/4/1 08:48
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
    private BaseEvent.EventMessage<SendRebateMessageEvent.RebateMessage> message;
    /*任务状态；create-创建、completed-完成、fail-失败*/
    private TaskStateVO state;
}
