package com.xjtu.domain.activity.service.armory;

import java.util.Date;

/**
 * @author mlei@xjtu
 * @description 活动调度【库存扣减】
 * @create 2026/3/26 03:08
 */
public interface IActivityDispatch {

    /**
     * 根据策略ID和奖品ID，扣减奖品缓存库存
     * @param sku 互动SKU
     * @param endDateTime 活动结束时间，根据结束时间设置加锁的key为结束时间
     * @return 扣减结果
     */
    boolean subtractionActivitySkuStock(Long sku, Date endDateTime);
}
