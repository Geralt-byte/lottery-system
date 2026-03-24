package com.xjtu.domain.activity.repository;

import com.xjtu.domain.activity.model.aggregate.CreateOrderAggregate;
import com.xjtu.domain.activity.model.entity.ActivityCountEntity;
import com.xjtu.domain.activity.model.entity.ActivityEntity;
import com.xjtu.domain.activity.model.entity.ActivitySkuEntity;

/**
 * @author mlei@xjtu
 * @description 活动仓储接口
 * @create 2026/3/24 04:33
 */
public interface IActivityRepository {

    //查找活动sku
    ActivitySkuEntity queryActivitySku(Long sku);
    //查找抽奖活动
    ActivityEntity queryRaffleActivityByActivityId(Long activityId);
    //查询抽奖活动次数
    ActivityCountEntity queryRaffleActivityCountByActivityCountId(Long activityCountId);
    //保存订单
    void doSaveOrder(CreateOrderAggregate createOrderAggregate);
}
