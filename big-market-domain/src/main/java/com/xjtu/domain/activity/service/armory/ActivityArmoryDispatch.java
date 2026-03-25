package com.xjtu.domain.activity.service.armory;

import com.xjtu.domain.activity.model.entity.ActivitySkuEntity;
import com.xjtu.domain.activity.repository.IActivityRepository;
import com.xjtu.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author mlei@xjtu
 * @description ActivityArmory
 * @create 2026/3/26 03:08
 */
@Slf4j
@Service
public class ActivityArmoryDispatch implements IActivityArmory,IActivityDispatch{

    @Resource
    private IActivityRepository iActivityRepository;

    @Override
    public boolean assembleActivitySku(Long sku) {
        // 预热活动sku库存
        ActivitySkuEntity activitySkuEntity = iActivityRepository.queryActivitySku(sku);
        cacheActivitySkuStockCount(sku,activitySkuEntity.getStockCount());

        // 预热活动【查询时预热到缓存】
        iActivityRepository.queryRaffleActivityByActivityId(activitySkuEntity.getActivityId());

        // 预热活动次数【查询时预热到缓存】
        iActivityRepository.queryRaffleActivityCountByActivityCountId(activitySkuEntity.getActivityCountId());

        return true;
    }

    private void cacheActivitySkuStockCount(Long sku, Integer stockCount) {
        String cacheKey= Constants.RedisKey.ACTIVITY_SKU_STOCK_COUNT_KEY +sku;
        iActivityRepository.cacheActivitySkuStockCount(cacheKey,stockCount);
    }

    @Override
    public boolean subtractionActivitySkuStock(Long sku, Date endDateTime) {
        String cacheKey= Constants.RedisKey.ACTIVITY_SKU_STOCK_COUNT_KEY +sku;
        return iActivityRepository.subtractionActivitySkuStock(sku,cacheKey,endDateTime);
    }
}
