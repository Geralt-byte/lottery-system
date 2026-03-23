package com.xjtu.domain.activity.service;

import com.alibaba.fastjson.JSON;
import com.xjtu.domain.activity.model.entity.*;
import com.xjtu.domain.activity.repository.IActivityRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * @author mlei@xjtu
 * @description 抽奖活动抽象类，定义标准的流程
 * @create 2026/3/24 04:35
 */
@Slf4j
public abstract class AbstractIRaffleActivity implements IRaffleOrder {

    protected IActivityRepository iActivityRepository;

    public AbstractIRaffleActivity(IActivityRepository iActivityRepository) {
        this.iActivityRepository = iActivityRepository;
    }

    @Override
    public ActivityOrderEntity createRaffleActivityOrder(ActivityShopCartEntity activityShopCartEntity) {
        // 1. 通过sku查询活动信息
        ActivitySkuEntity activitySkuEntity = iActivityRepository.queryActivitySku(activityShopCartEntity.getSku());
        // 2. 查询活动信息
        ActivityEntity activityEntity = iActivityRepository.queryRaffleActivityByActivityId(activitySkuEntity.getActivityId());
        // 3. 查询次数信息（用户在活动上可参与的次数）
        ActivityCountEntity activityCountEntity = iActivityRepository.queryRaffleActivityCountByActivityCountId(activitySkuEntity.getActivityCountId());

        log.info("查询结果：{} {} {}", JSON.toJSONString(activitySkuEntity), JSON.toJSONString(activityEntity), JSON.toJSONString(activityCountEntity));

        return ActivityOrderEntity.builder().build();
    }
}
