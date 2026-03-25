package com.xjtu.domain.activity.repository;

import com.xjtu.domain.activity.model.aggregate.CreateOrderAggregate;
import com.xjtu.domain.activity.model.entity.ActivityCountEntity;
import com.xjtu.domain.activity.model.entity.ActivityEntity;
import com.xjtu.domain.activity.model.entity.ActivitySkuEntity;
import com.xjtu.domain.activity.model.valobj.ActivitySkuStockKeyVO;

import java.util.Date;

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
    //缓存sku库存到redis
    void cacheActivitySkuStockCount(String cacheKey, Integer stockCount);
    //redis中的库存扣减
    boolean subtractionActivitySkuStock(Long sku, String cacheKey, Date endDateTime);
    //消费库存发送sku
    void activitySkuStockConsumeSendQueue(ActivitySkuStockKeyVO activitySkuStockKeyVO);
    //获取缓存队列
    ActivitySkuStockKeyVO takeQueueValue();
    //清空缓存队列
    void clearQueueValue();
    //更新库存
    void updateActivitySkuStock(Long sku);
    //清空库存
    void clearActivitySkuStock(Long sku);
}
