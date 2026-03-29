package com.xjtu.domain.activity.service.quota.rule;

/**
 * @author mlei@xjtu
 * @description IActionChainArmory
 * @create 2026/3/25 03:48
 */
public interface IActionChainArmory {

    IActionChain next();

    IActionChain appendNext(IActionChain next);
}
