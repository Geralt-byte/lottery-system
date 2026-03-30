package com.xjtu.domain.activity.service.armory;

/**
 * @author mlei@xjtu
 * @description 活动装配接口
 * @create 2026/3/26 03:07
 */
public interface IActivityArmory {

    /* 装配活动*/
    boolean assembleActivitySku(Long sku);
    /* 通过活动id装配*/
    boolean assembleActivitySkuByActivityId(Long activityId);
}
