package com.xjtu.domain.strategy.service.rule.chain;

/**
 * @author mlei@xjtu
 * @description 责任链装配
 * @create 2026/3/13 16:39
 */
public interface ILogicChainArmory {

    ILogicChain next();

    ILogicChain appendNext(ILogicChain next);
}
