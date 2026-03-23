package com.xjtu.domain.activity.service;

import com.xjtu.domain.activity.model.entity.ActivityOrderEntity;
import com.xjtu.domain.activity.model.entity.ActivityShopCartEntity;

/**
 * @author mlei@xjtu
 * @description 抽奖订单接口
 * @create 2026/3/24 04:34
 */
public interface IRaffleOrder {

    /**
     * 以sku创建抽奖活动订单，获得参与抽奖资格（可消耗的次数）
     *
     * @param activityShopCartEntity 活动sku实体，通过sku领取活动。
     * @return 活动参与记录实体
     * */
    ActivityOrderEntity createRaffleActivityOrder(ActivityShopCartEntity activityShopCartEntity);
}
