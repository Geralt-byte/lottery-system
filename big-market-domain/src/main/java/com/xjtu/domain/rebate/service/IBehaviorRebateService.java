package com.xjtu.domain.rebate.service;

import com.xjtu.domain.rebate.model.entity.BehaviorEntity;

import java.util.List;

/**
 * @author mlei@xjtu
 * @description 行为返利服务接口
 * @create 2026/4/1 09:04
 */
public interface IBehaviorRebateService {

    /**
     * @description 创建行为返利订单
     */
    List<String> createOrder(BehaviorEntity behaviorEntity);
}
