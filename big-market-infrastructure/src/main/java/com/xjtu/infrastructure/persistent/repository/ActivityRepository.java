package com.xjtu.infrastructure.persistent.repository;

import cn.bugstack.middleware.db.router.strategy.IDBRouterStrategy;
import com.xjtu.domain.activity.model.aggregate.CreateOrderAggregate;
import com.xjtu.domain.activity.model.entity.*;
import com.xjtu.domain.activity.repository.IActivityRepository;
import com.xjtu.infrastructure.persistent.dao.*;
import com.xjtu.infrastructure.persistent.po.*;
import com.xjtu.infrastructure.persistent.redis.IRedisService;
import com.xjtu.types.common.Constants;
import com.xjtu.types.enums.ResponseCode;
import com.xjtu.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;

/**
 * @author mlei@xjtu
 * @description 活动仓储实现
 * @create 2026/3/24 04:57
 */
@Slf4j
@Repository
public class ActivityRepository implements IActivityRepository {

    @Resource
    private IRedisService iRedisService;
    @Resource
    private TransactionTemplate transactionTemplate;
    @Resource
    private IDBRouterStrategy dbRouter;

    @Resource
    private IRaffleActivityDao iRaffleActivityDao;
    @Resource
    private IRaffleActivityCountDao iRaffleActivityCountDao;
    @Resource
    private IRaffleActivityAccountDao iRaffleActivityAccountDao;
    @Resource
    private IRaffleActivityOrderDao iRaffleActivityOrderDao;
    @Resource
    private IRaffleActivitySkuDao iRaffleActivitySkuDao;

    @Override
    public ActivitySkuEntity queryActivitySku(Long sku) {
        RaffleActivitySku raffleActivitySku = iRaffleActivitySkuDao.queryActivitySku(sku);
        return ActivitySkuEntity.builder()
                .sku(raffleActivitySku.getSku())
                .activityId(raffleActivitySku.getActivityId())
                .activityCountId(raffleActivitySku.getActivityCountId())
                .stockCount(raffleActivitySku.getStockCount())
                .stockCountSurplus(raffleActivitySku.getStockCountSurplus())
                .build();
    }

    @Override
    public ActivityEntity queryRaffleActivityByActivityId(Long activityId) {
        //先读缓存
        String cacheKey= Constants.RedisKey.ACTIVITY_KEY+activityId;
        ActivityEntity activityEntity=iRedisService.getValue(cacheKey);
        if(activityEntity!=null) return activityEntity;

        //读数据库
        RaffleActivity raffleActivity = iRaffleActivityDao.queryRaffleActivityByActivityId(activityId);
        activityEntity=ActivityEntity.builder()
                .activityId(raffleActivity.getActivityId())
                .activityName(raffleActivity.getActivityName())
                .activityDesc(raffleActivity.getActivityDesc())
                .beginDateTime(raffleActivity.getBeginDateTime())
                .endDateTime(raffleActivity.getEndDateTime())
                .strategyId(raffleActivity.getStrategyId())
                .state(raffleActivity.getState())
                .build();
        //写回缓存
        iRedisService.setValue(cacheKey,activityEntity);
        return activityEntity;
    }

    @Override
    public ActivityCountEntity queryRaffleActivityCountByActivityCountId(Long activityCountId) {
        //优先读缓存
        String cacheKey=Constants.RedisKey.ACTIVITY_COUNT_KEY+activityCountId;
        ActivityCountEntity activityCountEntity=iRedisService.getValue(cacheKey);
        if(activityCountEntity!=null) return activityCountEntity;

        //读数据库
        RaffleActivityCount raffleActivityCount=iRaffleActivityCountDao.queryRaffleActivityCountByActivityId(activityCountId);
        activityCountEntity=ActivityCountEntity.builder()
                .activityCountId(raffleActivityCount.getActivityCountId())
                .totalCount(raffleActivityCount.getTotalCount())
                .dayCount(raffleActivityCount.getDayCount())
                .monthCount(raffleActivityCount.getMonthCount())
                .build();
        //写回缓存
        iRedisService.setValue(cacheKey,activityCountEntity);
        return activityCountEntity;
    }

    @Override
    public void doSaveOrder(CreateOrderAggregate createOrderAggregate) {
        try {
            // 订单对象
            ActivityOrderEntity activityOrderEntity=createOrderAggregate.getActivityOrderEntity();
            RaffleActivityOrder raffleActivityOrder = new RaffleActivityOrder();
            raffleActivityOrder.setUserId(activityOrderEntity.getUserId());
            raffleActivityOrder.setSku(activityOrderEntity.getSku());
            raffleActivityOrder.setActivityId(activityOrderEntity.getActivityId());
            raffleActivityOrder.setActivityName(activityOrderEntity.getActivityName());
            raffleActivityOrder.setStrategyId(activityOrderEntity.getStrategyId());
            raffleActivityOrder.setOrderId(activityOrderEntity.getOrderId());
            raffleActivityOrder.setOrderTime(activityOrderEntity.getOrderTime());
            raffleActivityOrder.setTotalCount(createOrderAggregate.getTotalCount());
            raffleActivityOrder.setDayCount(createOrderAggregate.getDayCount());
            raffleActivityOrder.setMonthCount(createOrderAggregate.getMonthCount());
            raffleActivityOrder.setState(activityOrderEntity.getState());
            raffleActivityOrder.setOutBusinessNo(activityOrderEntity.getOutBusinessNo());

            // 账户对象
            RaffleActivityAccount raffleActivityAccount=new RaffleActivityAccount();
            raffleActivityAccount.setUserId(createOrderAggregate.getUserId());
            raffleActivityAccount.setActivityId(createOrderAggregate.getActivityId());
            raffleActivityAccount.setTotalCount(createOrderAggregate.getTotalCount());
            raffleActivityAccount.setTotalCountSurplus(createOrderAggregate.getTotalCount());
            raffleActivityAccount.setDayCount(createOrderAggregate.getDayCount());
            raffleActivityAccount.setDayCountSurplus(createOrderAggregate.getDayCount());
            raffleActivityAccount.setMonthCount(createOrderAggregate.getMonthCount());
            raffleActivityAccount.setMonthCountSurplus(createOrderAggregate.getMonthCount());

            // 以用户ID作为切分键，通过 doRouter 设定路由【这样就保证了下面的操作，都是同一个链接下，也就保证了事务的特性】
            dbRouter.doRouter(createOrderAggregate.getUserId());

            transactionTemplate.execute(status -> {
                try {
                    // 1. 写入订单
                    iRaffleActivityOrderDao.insert(raffleActivityOrder);
                    // 2. 更新账户
                    int count=iRaffleActivityAccountDao.updateAccountQuota(raffleActivityAccount);
                    // 3. 创建账户 - 更新为0，则账户不存在，创新新账户。
                    if(count==0){
                        iRaffleActivityAccountDao.insert(raffleActivityAccount);
                    }
                    return 1;
                }catch (DuplicateKeyException e){
                    status.setRollbackOnly();
                    log.error("写入订单记录，唯一索引冲突 userId: {} activityId: {} sku: {}", activityOrderEntity.getUserId(),
                            activityOrderEntity.getActivityId(), activityOrderEntity.getSku(), e);
                    throw new AppException(ResponseCode.INDEX_DUP.getCode());
                }
            });
        } finally {
            dbRouter.clear();
        }
    }
}
