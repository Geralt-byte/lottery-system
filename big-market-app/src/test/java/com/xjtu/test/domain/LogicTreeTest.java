package com.xjtu.test.domain;

import com.alibaba.fastjson.JSON;
import com.xjtu.domain.strategy.model.valobj.*;
import com.xjtu.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import com.xjtu.domain.strategy.service.rule.tree.factory.engine.IDecisionTreeEngine;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author mlei@xjtu
 * @description LogicTreeTest
 * @create 2026/3/15 00:26
 */
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class LogicTreeTest {

    @Resource
    private DefaultTreeFactory defaultTreeFactory;

    @Test
    public void logicTreeTest(){
        RuleTreeNodeVO rule_lock = RuleTreeNodeVO.builder()
                .treeId("tree_lock")
                .ruleKey("rule_lock")
                .ruleDesc("限定用户已完成N次抽奖后解锁")
                .ruleValue("1")
                .treeNodeLineVOList(new ArrayList<RuleTreeNodeLineVO>(){
                    {add(RuleTreeNodeLineVO.builder()
                            .treeId("tree_lock")
                            .ruleNodeFrom("rule_lock")
                            .ruleNodeTo("rule_luck_award")
                            .ruleLimitType(RuleLimitTypeVO.EQUAL)
                            .ruleLimitValue(RuleLogicCheckTypeVO.TAKE_OVER)
                            .build());

                    add(RuleTreeNodeLineVO.builder()
                            .treeId("tree_lock")
                            .ruleNodeFrom("rule_lock")
                            .ruleNodeTo("rule_stock")
                            .ruleLimitType(RuleLimitTypeVO.EQUAL)
                            .ruleLimitValue(RuleLogicCheckTypeVO.ALLOW)
                            .build());
                }})
                .build();

        RuleTreeNodeVO rule_luck_award = RuleTreeNodeVO.builder()
                .treeId("tree_lock")
                .ruleKey("rule_luck_award")
                .ruleDesc("发放幸运兜底奖")
                .ruleValue("1")
                .treeNodeLineVOList(null)
                .build();

        RuleTreeNodeVO rule_stock = RuleTreeNodeVO.builder()
                .treeId("tree_lock")
                .ruleKey("rule_stock")
                .ruleDesc("库存处理规则")
                .ruleValue("null")
                .treeNodeLineVOList(new ArrayList<RuleTreeNodeLineVO>(){
                    {add(RuleTreeNodeLineVO.builder()
                            .treeId("tree_lock")
                            .ruleNodeFrom("rule_stock")
                            .ruleNodeTo("rule_luck_award")
                            .ruleLimitType(RuleLimitTypeVO.EQUAL)
                            .ruleLimitValue(RuleLogicCheckTypeVO.TAKE_OVER)
                            .build());
                    }})
                .build();

        RuleTreeVO ruleTreeVO = new RuleTreeVO();
        ruleTreeVO.setTreeId("tree_lock");
        ruleTreeVO.setTreeName("决策树规则；增加dall-e-3画图模型");
        ruleTreeVO.setTreeDesc("决策树规则；增加dall-e-3画图模型");
        ruleTreeVO.setTreeRootRuleNode("rule_lock");

        HashMap<String, RuleTreeNodeVO> ruleTreeNodeVO = new HashMap<>();
        ruleTreeNodeVO.put("rule_lock",rule_lock);
        ruleTreeNodeVO.put("rule_luck_award",rule_luck_award);
        ruleTreeNodeVO.put("rule_stock",rule_stock);
        ruleTreeVO.setTreeNodeMap(ruleTreeNodeVO);

        IDecisionTreeEngine treeEngine = defaultTreeFactory.openLogicTree(ruleTreeVO);

        DefaultTreeFactory.StrategyAwardVO awardData = treeEngine.process("mlei", 100001L, 100);
        log.info("决策树测试: {}", JSON.toJSONString(awardData));
    }
}
