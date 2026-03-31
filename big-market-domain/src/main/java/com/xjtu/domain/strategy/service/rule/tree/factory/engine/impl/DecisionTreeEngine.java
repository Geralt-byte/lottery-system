package com.xjtu.domain.strategy.service.rule.tree.factory.engine.impl;

import com.xjtu.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.xjtu.domain.strategy.model.valobj.RuleTreeNodeLineVO;
import com.xjtu.domain.strategy.model.valobj.RuleTreeNodeVO;
import com.xjtu.domain.strategy.model.valobj.RuleTreeVO;
import com.xjtu.domain.strategy.service.rule.tree.ILogicTreeNode;
import com.xjtu.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import com.xjtu.domain.strategy.service.rule.tree.factory.engine.IDecisionTreeEngine;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author mlei@xjtu
 * @description 规则树组合接口实现类
 * @create 2026/3/14 23:19
 */
@Slf4j
public class DecisionTreeEngine implements IDecisionTreeEngine {

    private final Map<String, ILogicTreeNode> iLogicTreeNodeGroup;

    private final RuleTreeVO ruleTreeVO;

    public DecisionTreeEngine(Map<String, ILogicTreeNode> iLogicTreeNodeGroup, RuleTreeVO ruleTreeVO) {
        this.iLogicTreeNodeGroup = iLogicTreeNodeGroup;
        this.ruleTreeVO = ruleTreeVO;
    }

    @Override
    public DefaultTreeFactory.StrategyAwardVO process(String userId, Long strategyId, Integer awardId, Date endDateTime) {

        DefaultTreeFactory.StrategyAwardVO strategyAwardVO = null;

        //获取基础信息
        String node = ruleTreeVO.getTreeRootRuleNode();
        Map<String, RuleTreeNodeVO> treeNodeMap = ruleTreeVO.getTreeNodeMap();

        //获取起始节点
        RuleTreeNodeVO ruleTreeNodeVO = treeNodeMap.get(node);

        while (node != null) {
            //获取决策节点
            ILogicTreeNode iLogicTreeNode = iLogicTreeNodeGroup.get(ruleTreeNodeVO.getRuleKey());
            String ruleValue = ruleTreeNodeVO.getRuleValue();

            //决策节点计算
            DefaultTreeFactory.TreeActionEntity logicEntity = iLogicTreeNode.logic(userId, strategyId, awardId, ruleValue,endDateTime);
            RuleLogicCheckTypeVO ruleLogicCheckType = logicEntity.getRuleLogicCheckType();
            strategyAwardVO = logicEntity.getStrategyAwardVO();
            log.info("决策树引擎【{}】 treeId: {} node: {} code: {}", ruleTreeVO.getTreeName(), ruleTreeVO.getTreeId(), node, ruleLogicCheckType.getCode());

            //获取下个节点
            node = nextNode(ruleLogicCheckType.getCode(), ruleTreeNodeVO.getTreeNodeLineVOList());
            ruleTreeNodeVO = treeNodeMap.get(node);
        }
        return strategyAwardVO;
    }

    private String nextNode(String matterValue, List<RuleTreeNodeLineVO> treeNodeLineVOList) {
        if (treeNodeLineVOList == null || treeNodeLineVOList.isEmpty()) {
            return null;
        }
        for (RuleTreeNodeLineVO nodeLine : treeNodeLineVOList) {
            if (decisionLogic(matterValue, nodeLine)) {
                return nodeLine.getRuleNodeTo();
            }
        }
//        throw new RuntimeException("决策树引擎，nextNode 计算失败，未找到可执行节点！");

        //bug
        return null;
    }

    private boolean decisionLogic(String matterValue, RuleTreeNodeLineVO nodeLine) {
        switch (nodeLine.getRuleLimitType()) {
            case EQUAL:
                return matterValue.equals(nodeLine.getRuleLimitValue().getCode());
            case GT:
            case LT:
            case GE:
            case LE:
            default:
                return false;
        }
    }
}
