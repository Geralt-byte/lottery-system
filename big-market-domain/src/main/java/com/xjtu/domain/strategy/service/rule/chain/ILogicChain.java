package com.xjtu.domain.strategy.service.rule.chain;

import com.xjtu.domain.strategy.service.rule.chain.factory.DefaultChainFactory;

/**
 * @author mlei@xjtu
 * @description 抽奖策略规则责任链接口
 * @create 2026/3/13 16:36
 */
public interface ILogicChain extends ILogicChainArmory{

    /**责任链接口*/
    DefaultChainFactory.StrategyAwardVO logic(String userId, Long strategyId);
}
