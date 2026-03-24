package com.xjtu.domain.activity.service.rule.chain.impl;

import com.xjtu.domain.activity.model.entity.ActivityCountEntity;
import com.xjtu.domain.activity.model.entity.ActivityEntity;
import com.xjtu.domain.activity.model.entity.ActivitySkuEntity;
import com.xjtu.domain.activity.service.rule.chain.AbstractIActionChain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author mlei@xjtu
 * @description 商品库存规则节点
 * @create 2026/3/25 03:54
 */
@Slf4j
@Component("activity_sku_stock_action")
public class ActivitySkuStockActionChain extends AbstractIActionChain {

    @Override
    public boolean action(ActivitySkuEntity activitySkuEntity, ActivityEntity activityEntity, ActivityCountEntity activityCountEntity) {
        log.info("活动责任链-商品库存处理【校验&扣减】开始。");

        return true;
    }
}
