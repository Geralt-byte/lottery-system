package com.xjtu.domain.activity.service.quota.rule;

/**
 * @author mlei@xjtu
 * @description 下单规则责任链抽象类
 * @create 2026/3/25 03:50
 */
public abstract class AbstractIActionChain implements IActionChain{

    private IActionChain next;

    @Override
    public IActionChain next() {
        return this.next;
    }

    @Override
    public IActionChain appendNext(IActionChain next) {
        this.next=next;
        return next;
    }
}
