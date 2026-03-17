package com.xjtu.domain.strategy.service.rule.tree.factory;

import com.xjtu.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.xjtu.domain.strategy.model.valobj.RuleTreeVO;
import com.xjtu.domain.strategy.service.rule.tree.ILogicTreeNode;
import com.xjtu.domain.strategy.service.rule.tree.factory.engine.IDecisionTreeEngine;
import com.xjtu.domain.strategy.service.rule.tree.factory.engine.impl.DecisionTreeEngine;
import lombok.*;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author mlei@xjtu
 * @description 规则树工厂
 * @create 2026/3/14 23:18
 */
@Service
public class DefaultTreeFactory {

    private final Map<String, ILogicTreeNode> iLogicTreeNodeGroup;

    public DefaultTreeFactory(Map<String, ILogicTreeNode> iLogicTreeNodeGroup) {
        this.iLogicTreeNodeGroup = iLogicTreeNodeGroup;
    }

    public IDecisionTreeEngine openLogicTree(RuleTreeVO ruleTreeVO){
        return new DecisionTreeEngine(iLogicTreeNodeGroup,ruleTreeVO);
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TreeActionEntity{
        private RuleLogicCheckTypeVO ruleLogicCheckType;
        private StrategyAwardVO strategyAwardVO;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StrategyAwardVO {
        /** 抽奖奖品ID - 内部流转使用 */
        private Integer awardId;
        /** 抽奖奖品规则值 */
        private String awardRuleValue;
    }
}
