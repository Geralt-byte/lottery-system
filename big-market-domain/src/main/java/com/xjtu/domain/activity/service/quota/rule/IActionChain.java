package com.xjtu.domain.activity.service.quota.rule;

import com.xjtu.domain.activity.model.entity.ActivityCountEntity;
import com.xjtu.domain.activity.model.entity.ActivityEntity;
import com.xjtu.domain.activity.model.entity.ActivitySkuEntity;

/**
 * @author mlei@xjtu
 * @description 下单规则过滤接口
 * @create 2026/3/25 03:29
 */
public interface IActionChain extends IActionChainArmory{

    boolean action(ActivitySkuEntity activitySkuEntity, ActivityEntity activityEntity, ActivityCountEntity activityCountEntity);
}
