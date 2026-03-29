package com.xjtu.infrastructure.persistent.dao;

import cn.bugstack.middleware.db.router.annotation.DBRouter;
import com.xjtu.infrastructure.persistent.po.Task;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author mlei@xjtu
 * @description 任务表，发送MQ
 * @create 2026/3/28 00:21
 */
@Mapper
public interface ITaskDao {

    void insert(Task task);

    List<Task> queryNoSendMessageTaskList();

    @DBRouter
    void updateTaskSendMessageCompleted(Task taskReq);

    @DBRouter
    void updateTaskSendMessageFail(Task taskReq);
}
