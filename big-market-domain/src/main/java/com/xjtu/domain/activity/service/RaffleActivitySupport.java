package com.xjtu.domain.activity.service;

import com.xjtu.domain.activity.model.entity.ActivityCountEntity;
import com.xjtu.domain.activity.model.entity.ActivityEntity;
import com.xjtu.domain.activity.model.entity.ActivitySkuEntity;
import com.xjtu.domain.activity.repository.IActivityRepository;
import com.xjtu.domain.activity.service.rule.chain.factory.DefaultActivityChainFactory;

/**
 * @author mlei@xjtu
 * @description 抽奖活动的支撑类
 * @create 2026/3/25 03:34
 */
public class RaffleActivitySupport {

    protected DefaultActivityChainFactory defaultActivityChainFactory;

    protected IActivityRepository iActivityRepository;

    public RaffleActivitySupport(DefaultActivityChainFactory defaultActivityChainFactory, IActivityRepository iActivityRepository) {
        this.defaultActivityChainFactory = defaultActivityChainFactory;
        this.iActivityRepository = iActivityRepository;
    }

    public ActivitySkuEntity queryActivitySku(Long sku){
        return iActivityRepository.queryActivitySku(sku);
    }

    public ActivityEntity queryRaffleActivityByActivityId(Long activityId){
        return iActivityRepository.queryRaffleActivityByActivityId(activityId);
    }

    public ActivityCountEntity queryRaffleActivityCountByActivityCountId(Long activityCountId){
        return iActivityRepository.queryRaffleActivityCountByActivityCountId(activityCountId);
    }
}
