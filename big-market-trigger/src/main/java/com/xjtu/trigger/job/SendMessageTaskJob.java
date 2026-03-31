package com.xjtu.trigger.job;

import cn.bugstack.middleware.db.router.strategy.IDBRouterStrategy;
import com.xjtu.domain.task.model.entity.TaskEntity;
import com.xjtu.domain.task.service.ITaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author mlei@xjtu
 * @description SendMessageTaskJob
 * @create 2026/3/30 04:01
 */
@Slf4j
@Component()
public class SendMessageTaskJob {

    @Resource
    private ITaskService iTaskService;
    @Resource
    private ThreadPoolExecutor executor;
    @Resource
    private IDBRouterStrategy dbRouter;

    @Scheduled(cron = "0/5 * * * * ?")
    public void exec() {
        try {
            // 获取分库数量
            int dbCount = dbRouter.dbCount();

            for (int dbIdx = 1; dbIdx < dbCount; dbIdx++) {
                int finalDbIdx = dbIdx;
                executor.execute(() -> {
                    try {
                        dbRouter.setDBKey(finalDbIdx);
                        dbRouter.setTBKey(0);

                        List<TaskEntity> taskEntities = iTaskService.queryNoSendMessageTaskList();
                        if (taskEntities.isEmpty()) return;

                        // 发送MQ消息
                        for (TaskEntity taskEntity : taskEntities) {
                            // 开启线程发送，提高发送效率。配置的线程池策略为 CallerRunsPolicy，
                            // 在 ThreadPoolConfig 配置中有4个策略。
                            executor.execute(() -> {
                                try {
                                    iTaskService.sendMessage(taskEntity);
                                    iTaskService.updateTaskSendMessageCompleted(taskEntity.getUserId(), taskEntity.getMessageId());
                                } catch (Exception e) {
                                    log.error("定时任务，发送MQ消息失败 userId: {} topic: {}", taskEntity.getUserId(), taskEntity.getTopic());
                                    iTaskService.updateTaskSendMessageFail(taskEntity.getUserId(), taskEntity.getTopic());
                                }
                            });
                        }
                    } finally {
                        dbRouter.clear();
                    }
                });
            }

        } catch (Exception e) {
            log.error("定时任务，扫描MQ任务表发送消息失败。", e);
        } finally {
            dbRouter.clear();
        }
    }
}
