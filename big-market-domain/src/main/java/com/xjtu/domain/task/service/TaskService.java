package com.xjtu.domain.task.service;

import com.xjtu.domain.task.model.entity.TaskEntity;
import com.xjtu.domain.task.repository.ITaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author mlei@xjtu
 * @description 消息任务服务实现类
 * @create 2026/3/30 03:33
 */
@Slf4j
@Service
public class TaskService implements ITaskService {

    @Resource
    private ITaskRepository iTaskRepository;

    @Override
    public List<TaskEntity> queryNoSendMessageTaskList() {
        return iTaskRepository.queryNoSendMessageTaskList();
    }

    @Override
    public void sendMessage(TaskEntity taskEntity) {
        iTaskRepository.sendMessage(taskEntity);
    }

    @Override
    public void updateTaskSendMessageCompleted(String userId, String messageId) {
        iTaskRepository.updateTaskSendMessageCompleted(userId, messageId);
    }

    @Override
    public void updateTaskSendMessageFail(String userId, String messageId) {
        iTaskRepository.updateTaskSendMessageFail(userId, messageId);
    }
}
