package com.xjtu.domain.strategy.service.rule.chain;

import lombok.extern.slf4j.Slf4j;

/**
 * @author mlei@xjtu
 * @description 抽奖策略责任链，判断走那种抽奖策略。如；默认抽象、权重抽奖、黑名单抽奖
 * @create 2026/3/13 16:38
 */
@Slf4j
public abstract class AbstractLogicChain implements ILogicChain{

    private ILogicChain next;

    @Override
    public ILogicChain next() {
        return next;
    }

    @Override
    public ILogicChain appendNext(ILogicChain next) {
        this.next=next;
        return next;
    }

    protected abstract String ruleModel();
}
