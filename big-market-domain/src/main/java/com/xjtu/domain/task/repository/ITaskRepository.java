package com.xjtu.domain.task.repository;

import com.xjtu.domain.task.model.entity.TaskEntity;

import java.util.List;

/**
 * @author mlei@xjtu
 * @description 任务服务仓储接口
 * @create 2026/3/30 03:34
 */
public interface ITaskRepository {
    /*查询没有发送成功或超时的消息*/
    List<TaskEntity> queryNoSendMessageTaskList();
    /*发送消息*/
    void sendMessage(TaskEntity taskEntity);
    /*更新消息成功任务*/
    void updateTaskSendMessageCompleted(String userId, String messageId);
    /*更新消息失败任务*/
    void updateTaskSendMessageFail(String userId, String messageId);
}
