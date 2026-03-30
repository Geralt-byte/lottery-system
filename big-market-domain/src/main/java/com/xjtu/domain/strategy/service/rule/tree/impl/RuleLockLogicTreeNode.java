package com.xjtu.domain.strategy.service.rule.tree.impl;

import com.xjtu.domain.strategy.model.entity.RuleActionEntity;
import com.xjtu.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.xjtu.domain.strategy.repository.IStrategyRepository;
import com.xjtu.domain.strategy.service.rule.tree.ILogicTreeNode;
import com.xjtu.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author mlei@xjtu
 * @description 次数规则锁决策树实现类
 * @create 2026/3/14 23:12
 */
@Slf4j
@Component("rule_lock")
public class RuleLockLogicTreeNode implements ILogicTreeNode {

    @Resource
    private IStrategyRepository iStrategyRepository;

    /**
     * 抽奖次数规则过滤
     */
    @Override
    public DefaultTreeFactory.TreeActionEntity logic(String userId, Long strategyId, Integer awardId, String ruleValue) {
        //日志
        log.info("规则过滤-抽奖次数 userId:{} strategyId:{} ruleModel:{} awardId:{} ruleValue:{}",
                userId, strategyId, "rule_lock", awardId,ruleValue);

        long raffleCount = 0L;
        try {
            raffleCount = Long.parseLong(ruleValue);
        } catch (Exception e) {
            throw new RuntimeException("规则过滤-次数锁异常 ruleValue: " + ruleValue + " 配置不正确");
        }

        Integer userRaffleCount=iStrategyRepository.queryTodayUserRaffleCount(userId,strategyId);

        //用户抽奖次数大于限定值，走库存
        if (userRaffleCount >= raffleCount) {
            log.info("规则过滤-次数锁【放行】 userId:{} strategyId:{} awardId:{} raffleCount:{} userRaffleCount:{}",
                    userId, strategyId, awardId, raffleCount, userRaffleCount);
            return DefaultTreeFactory
                    .TreeActionEntity
                    .builder()
                    .ruleLogicCheckType(RuleLogicCheckTypeVO.ALLOW)
                    .build();
        }


        log.info("规则过滤-次数锁【拦截】 userId:{} strategyId:{} awardId:{} raffleCount:{} userRaffleCount:{}",
                userId, strategyId, awardId, raffleCount, userRaffleCount);
        //走兜底
        return DefaultTreeFactory.TreeActionEntity
                .builder()
                .ruleLogicCheckType(RuleLogicCheckTypeVO.TAKE_OVER)
                .build();
    }
}
