package com.xjtu.domain.strategy.service.rule.tree.impl;

import com.xjtu.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.xjtu.domain.strategy.service.rule.tree.ILogicTreeNode;
import com.xjtu.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author mlei@xjtu
 * @description 库存规则的叶子节点   决策树实现类
 * @create 2026/3/17 17:53
 */
@Slf4j
//@Component("rule_stock_success")
public class RuleStockSuccessLogicTreeNode implements ILogicTreeNode {

    @Override
    public DefaultTreeFactory.TreeActionEntity logic(String userId, Long strategyId, Integer awardId, String ruleValue) {
        //日志
        log.info("规则过滤-库存扣减成功的叶子节点 userId:{} strategyId:{} ruleModel:{} awardId:{} ruleValue:{}",
                userId, strategyId, "rule_stock", awardId,ruleValue);

        return DefaultTreeFactory.TreeActionEntity
                .builder()
                .ruleLogicCheckType(RuleLogicCheckTypeVO.TAKE_OVER)
                .strategyAwardVO(DefaultTreeFactory.StrategyAwardVO
                        .builder()
                        .awardId(awardId)
                        .awardRuleValue(ruleValue)
                        .build())
                .build();
    }
}
