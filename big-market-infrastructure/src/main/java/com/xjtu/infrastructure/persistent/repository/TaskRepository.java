package com.xjtu.infrastructure.persistent.repository;

import com.xjtu.domain.task.model.entity.TaskEntity;
import com.xjtu.domain.task.repository.ITaskRepository;
import com.xjtu.infrastructure.event.EventPublisher;
import com.xjtu.infrastructure.persistent.dao.ITaskDao;
import com.xjtu.infrastructure.persistent.po.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mlei@xjtu
 * @description 任务服务仓储
 * @create 2026/3/30 03:35
 */
@Slf4j
@Repository
public class TaskRepository implements ITaskRepository {

    @Resource
    private ITaskDao iTaskDao;
    @Resource
    private EventPublisher eventPublisher;

    @Override
    public List<TaskEntity> queryNoSendMessageTaskList() {

        List<Task> tasks = iTaskDao.queryNoSendMessageTaskList();
        List<TaskEntity> taskEntities = new ArrayList<>(tasks.size());
        for (Task task : tasks) {
            TaskEntity taskEntity = TaskEntity.builder()
                    .userId(task.getUserId())
                    .topic(task.getTopic())
                    .messageId(task.getMessageId())
                    .message(task.getMessage())
                    .build();
            taskEntities.add(taskEntity);
        }
        return taskEntities;
    }

    @Override
    public void sendMessage(TaskEntity taskEntity) {
        eventPublisher.publish(taskEntity.getTopic(),taskEntity.getMessage());
    }

    @Override
    public void updateTaskSendMessageCompleted(String userId, String messageId) {
        Task taskReq=new Task();
        taskReq.setUserId(userId);
        taskReq.setMessageId(messageId);
        iTaskDao.updateTaskSendMessageCompleted(taskReq);
    }

    @Override
    public void updateTaskSendMessageFail(String userId, String messageId) {
        Task taskReq=new Task();
        taskReq.setUserId(userId);
        taskReq.setMessageId(messageId);
        iTaskDao.updateTaskSendMessageFail(taskReq);
    }
}
