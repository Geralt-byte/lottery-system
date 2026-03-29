package com.xjtu.domain.task.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String message;
}
