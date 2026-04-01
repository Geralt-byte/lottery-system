package com.xjtu.infrastructure.persistent.repository;

import cn.bugstack.middleware.db.router.strategy.IDBRouterStrategy;
import com.alibaba.fastjson.JSON;
import com.xjtu.domain.rebate.model.aggregate.BehaviorRebateAggregate;
import com.xjtu.domain.rebate.model.entity.BehaviorRebateOrderEntity;
import com.xjtu.domain.rebate.model.entity.TaskEntity;
import com.xjtu.domain.rebate.model.valobj.BehaviorTypeVO;
import com.xjtu.domain.rebate.model.valobj.DailyBehaviorRebateVO;
import com.xjtu.domain.rebate.repository.IBehaviorRebateRepository;
import com.xjtu.infrastructure.event.EventPublisher;
import com.xjtu.infrastructure.persistent.dao.IDailyBehaviorRebateDao;
import com.xjtu.infrastructure.persistent.dao.ITaskDao;
import com.xjtu.infrastructure.persistent.dao.IUserBehaviorRebateOrderDao;
import com.xjtu.infrastructure.persistent.po.DailyBehaviorRebate;
import com.xjtu.infrastructure.persistent.po.Task;
import com.xjtu.infrastructure.persistent.po.UserBehaviorRebateOrder;
import com.xjtu.infrastructure.persistent.redis.IRedisService;
import com.xjtu.types.common.Constants;
import com.xjtu.types.enums.ResponseCode;
import com.xjtu.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mlei@xjtu
 * @description 行为返利服务仓储实现
 * @create 2026/4/1 09:03
 */
@Slf4j
@Repository
public class BehaviorRebateRepository implements IBehaviorRebateRepository {

    @Resource
    private IDailyBehaviorRebateDao iDailyBehaviorRebateDao;
    @Resource
    private IUserBehaviorRebateOrderDao iUserBehaviorRebateOrderDao;
    @Resource
    private ITaskDao iTaskDao;

    @Resource
    private IRedisService iRedisService;
    @Resource
    private IDBRouterStrategy dbRouter;
    @Resource
    private TransactionTemplate transactionTemplate;
    @Resource
    private EventPublisher eventPublisher;


    @Override
    public List<DailyBehaviorRebateVO> queryDailyBehaviorRebateConfig(BehaviorTypeVO behaviorTypeVO) {
        // 先查缓存
        String cacheKey = Constants.RedisKey.REBATE_CONFIG_LIST_KEY + behaviorTypeVO.getCode();
        List<DailyBehaviorRebateVO> dailyBehaviorRebateVOS = iRedisService.getValue(cacheKey);
        if (dailyBehaviorRebateVOS != null && !dailyBehaviorRebateVOS.isEmpty()) return dailyBehaviorRebateVOS;

        // 从数据库查询
        List<DailyBehaviorRebate> dailyBehaviorRebates = iDailyBehaviorRebateDao.queryDailyBehaviorRebateConfig(behaviorTypeVO.getCode());
        dailyBehaviorRebateVOS = new ArrayList<>(dailyBehaviorRebates.size());
        for (DailyBehaviorRebate dailyBehaviorRebate : dailyBehaviorRebates) {
            DailyBehaviorRebateVO dailyBehaviorRebateVO = DailyBehaviorRebateVO.builder()
                    .behaviorType(dailyBehaviorRebate.getBehaviorType())
                    .rebateDesc(dailyBehaviorRebate.getRebateDesc())
                    .rebateType(dailyBehaviorRebate.getRebateType())
                    .rebateConfig(dailyBehaviorRebate.getRebateConfig())
                    .build();
            dailyBehaviorRebateVOS.add(dailyBehaviorRebateVO);
        }
        iRedisService.setValue(cacheKey, dailyBehaviorRebateVOS);

        return dailyBehaviorRebateVOS;
    }

    @Override
    public void saveUserRebateRecord(String userId, List<BehaviorRebateAggregate> behaviorRebateAggregates) {
        try {
            dbRouter.doRouter(userId);
            transactionTemplate.execute(status -> {
                try {
                    for (BehaviorRebateAggregate behaviorRebateAggregate : behaviorRebateAggregates) {
                        BehaviorRebateOrderEntity behaviorRebateOrderEntity = behaviorRebateAggregate.getBehaviorRebateOrderEntity();
                        //构建po对象
                        UserBehaviorRebateOrder userBehaviorRebateOrder = new UserBehaviorRebateOrder();
                        userBehaviorRebateOrder.setUserId(behaviorRebateOrderEntity.getUserId());
                        userBehaviorRebateOrder.setOrderId(behaviorRebateOrderEntity.getOrderId());
                        userBehaviorRebateOrder.setBehaviorType(behaviorRebateOrderEntity.getBehaviorType());
                        userBehaviorRebateOrder.setRebateDesc(behaviorRebateOrderEntity.getRebateDesc());
                        userBehaviorRebateOrder.setRebateType(behaviorRebateOrderEntity.getRebateType());
                        userBehaviorRebateOrder.setRebateConfig(behaviorRebateOrderEntity.getRebateConfig());
                        userBehaviorRebateOrder.setBizId(behaviorRebateOrderEntity.getBizId());
                        //保存
                        iUserBehaviorRebateOrderDao.insert(userBehaviorRebateOrder);

                        //任务对象
                        TaskEntity taskEntity = behaviorRebateAggregate.getTaskEntity();
                        Task task = new Task();
                        task.setUserId(taskEntity.getUserId());
                        task.setTopic(taskEntity.getTopic());
                        task.setMessageId(taskEntity.getMessageId());
                        task.setMessage(JSON.toJSONString(taskEntity.getMessage()));
                        task.setState(taskEntity.getState().getCode());
                        iTaskDao.insert(task);
                    }
                    return 1;
                } catch (DuplicateKeyException e) {
                    status.setRollbackOnly();
                    log.error("写入返利记录，唯一索引冲突 userId: {}", userId, e);
                    throw new AppException(ResponseCode.INDEX_DUP.getCode(), e);
                }
            });
        } finally {
            dbRouter.clear();
        }

        // 同步发送MQ消息
        for (BehaviorRebateAggregate behaviorRebateAggregate : behaviorRebateAggregates) {
            TaskEntity taskEntity = behaviorRebateAggregate.getTaskEntity();
            Task task = new Task();
            task.setUserId(taskEntity.getUserId());
            task.setMessageId(taskEntity.getMessageId());
            try {
                // 发送MQ消息
                eventPublisher.publish(taskEntity.getTopic(), taskEntity.getMessage());
                iTaskDao.updateTaskSendMessageCompleted(task);
            } catch (Exception e) {
                log.error("写入返利记录,发送MQ消息失败 userId: {} topic: {}", taskEntity.getUserId(), taskEntity.getTopic());
                iTaskDao.updateTaskSendMessageFail(task);
            }
        }
    }
}
