package com.xjtu.domain.strategy.service.armory;

/**
 * @author mlei@xjtu
 * @description 策略抽奖调度接口
 * @create 2026/3/9 22:28
 */
public interface IStrategyDispatch {

    /**
     * 抽奖行为，根据策略id进行抽奖
     * 返回值为奖品id
     */
    Integer getRandomAwardId(Long strategyId);

    /**
     * 抽奖加入权重规则
     * 返回值为奖品id
     */
    Integer getRandomAwardId(Long strategyId,String ruleWeightValue);
}
