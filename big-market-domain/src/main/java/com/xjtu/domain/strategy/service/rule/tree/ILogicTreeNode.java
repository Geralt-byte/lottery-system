package com.xjtu.domain.strategy.service.rule.tree;

import com.xjtu.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;

import java.util.Date;

/**
 * @author mlei@xjtu
 * @description 规则树接口
 * @create 2026/3/14 23:10
 */
public interface ILogicTreeNode {

    DefaultTreeFactory.TreeActionEntity logic(String userId, Long strategyId, Integer awardId, String ruleValue, Date endDateTime);
}
