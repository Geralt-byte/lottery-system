package com.xjtu.domain.activity.repository;

import com.xjtu.domain.activity.model.aggregate.CreatePartakeOrderAggregate;
import com.xjtu.domain.activity.model.aggregate.CreateQuotaOrderAggregate;
import com.xjtu.domain.activity.model.entity.*;
import com.xjtu.domain.activity.model.valobj.ActivitySkuStockKeyVO;

import java.util.Date;
import java.util.List;

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
    void doSaveOrder(CreateQuotaOrderAggregate createQuotaOrderAggregate);
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
    //查询未被使用的抽奖订单
    UserRaffleOrderEntity queryNoUsedRaffleOrder(PartakeRaffleActivityEntity partakeRaffleActivityEntity);
    //查询活动账户总额度
    ActivityAccountEntity queryActivityAccountByUserId(String userId, Long activityId);
    //查询活动月账户额度
    ActivityAccountMonthEntity queryActivityAccountMonthByUserId(String userId, Long activityId, String month);
    //查询活动日账户额度
    ActivityAccountDayEntity queryActivityAccountDayByUserId(String userId, Long activityId, String day);
    //保存用户抽奖单
    void saveCreatePartakeOrderAggregate(CreatePartakeOrderAggregate createPartakeOrderAggregate);
    //根据活动id查询活动sku列表
    List<ActivitySkuEntity> queryActivitySkuListByActivityId(Long activityId);
    //查询当前用户今天已抽奖次数
    Integer queryRaffleActivityAccountDayPartakeCount(Long activityId, String userId);
    //查询活动账户总参与次数
    Integer queryRaffleActivityAccountPartakeCount(Long activityId, String userId);
}
