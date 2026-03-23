package com.xjtu.infrastructure.persistent.repository;

import com.xjtu.domain.activity.model.entity.ActivityAccountEntity;
import com.xjtu.domain.activity.model.entity.ActivityCountEntity;
import com.xjtu.domain.activity.model.entity.ActivityEntity;
import com.xjtu.domain.activity.model.entity.ActivitySkuEntity;
import com.xjtu.domain.activity.repository.IActivityRepository;
import com.xjtu.infrastructure.persistent.dao.*;
import com.xjtu.infrastructure.persistent.po.RaffleActivity;
import com.xjtu.infrastructure.persistent.po.RaffleActivityCount;
import com.xjtu.infrastructure.persistent.po.RaffleActivitySku;
import com.xjtu.infrastructure.persistent.redis.IRedisService;
import com.xjtu.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

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
}
