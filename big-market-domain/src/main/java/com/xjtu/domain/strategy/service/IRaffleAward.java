package com.xjtu.domain.strategy.service;

import com.xjtu.domain.strategy.model.entity.StrategyAwardEntity;

import java.util.List;

/**
 * @author mlei@xjtu
 * @description 策略奖品接口，用来查询奖品列表
 * @create 2026/3/19 02:11
 */
public interface IRaffleAward {

    /**
     *
     * @param strategyId 策略id
     * @return 策略奖品表
     */
    List<StrategyAwardEntity> queryRaffleStrategyAwardList(Long strategyId);

    /**
     *
     * @param activityId 活动id
     * @return 策略奖品表
     */
    List<StrategyAwardEntity> queryRaffleStrategyAwardListByActivityId(Long activityId);
}
