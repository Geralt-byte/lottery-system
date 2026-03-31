package com.xjtu.domain.strategy.service.rule.tree.factory.engine;

import com.xjtu.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;

import java.util.Date;

/**
 * @author mlei@xjtu
 * @description 规则树组合接口
 * @create 2026/3/14 23:19
 */
public interface IDecisionTreeEngine {

    DefaultTreeFactory.StrategyAwardVO process(String userId, Long strategyId, Integer awardId, Date endDateTime);
}
