package com.xjtu.domain.strategy.service.rule.tree.impl;

import com.xjtu.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.xjtu.domain.strategy.model.valobj.StrategyAwardStockKeyVO;
import com.xjtu.domain.strategy.repository.IStrategyRepository;
import com.xjtu.domain.strategy.service.armory.IStrategyDispatch;
import com.xjtu.domain.strategy.service.rule.tree.ILogicTreeNode;
import com.xjtu.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author mlei@xjtu
 * @description 库存规则决策树实现类
 * @create 2026/3/14 23:14
 */
@Slf4j
@Component("rule_stock")
public class RuleStockLogicTreeNode implements ILogicTreeNode {

    @Resource
    private IStrategyDispatch iStrategyDispatch;
    @Resource
    private IStrategyRepository iStrategyRepository;

    @Override
    public DefaultTreeFactory.TreeActionEntity logic(String userId, Long strategyId, Integer awardId, String ruleValue, Date endDateTime) {
        //日志
        log.info("规则过滤-库存扣减 userId:{} strategyId:{} ruleModel:{} awardId:{} ruleValue:{}",
                userId, strategyId, "rule_stock", awardId, ruleValue);

        //扣减库存
        Boolean status = iStrategyDispatch.subtractionAwardStock(strategyId, awardId, endDateTime);

        if (status) {
            log.info("规则过滤-库存扣减-成功 userId:{} strategyId:{} ruleModel:{} awardId:{} ruleValue:{}",
                    userId, strategyId, "rule_stock", awardId, ruleValue);

            // 写入延迟队列，延迟消费更新数据库记录。【在trigger的job；UpdateAwardStockJob 下消费队列，更新数据库记录】
            iStrategyRepository.awardStockConsumeSendQueue(StrategyAwardStockKeyVO
                    .builder()
                    .strategyId(strategyId)
                    .awardId(awardId)
                    .build());

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

        // 如果库存不足，则直接返回放行
        return DefaultTreeFactory.TreeActionEntity
                .builder()
                .ruleLogicCheckType(RuleLogicCheckTypeVO.ALLOW)
                .build();
    }
}
