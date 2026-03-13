package com.xjtu.domain.strategy.service.rule.chain;

/**
 * @author mlei@xjtu
 * @description 抽奖策略规则责任链接口
 * @create 2026/3/13 16:36
 */
public interface ILogicChain extends ILogicChainArmory{

    /**责任链接口*/
    Integer logic(String userId,Long strategyId);
}
