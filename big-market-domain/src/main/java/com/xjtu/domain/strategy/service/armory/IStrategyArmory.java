package com.xjtu.domain.strategy.service.armory;

/**策略装配库接口，初始化策略计算*/

public interface IStrategyArmory {

    /**初始化抽奖策略配置，触发的时机可以为活动审核通过后进行调用*/
    boolean assembleLotteryStrategy(Long strategyId);
}
