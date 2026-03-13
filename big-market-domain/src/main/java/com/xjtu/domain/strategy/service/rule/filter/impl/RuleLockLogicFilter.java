package com.xjtu.domain.strategy.service.rule.filter.impl;

import com.xjtu.domain.strategy.model.entity.RuleActionEntity;
import com.xjtu.domain.strategy.model.entity.RuleMatterEntity;
import com.xjtu.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.xjtu.domain.strategy.repository.IStrategyRepository;
import com.xjtu.domain.strategy.service.annotation.LogicStrategy;
import com.xjtu.domain.strategy.service.rule.filter.ILogicFilter;
import com.xjtu.domain.strategy.service.rule.filter.factory.DefaultLogicFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author mlei@xjtu
 * @description RuleLockLogicFilter
 * @create 2026/3/13 11:26
 */
@Slf4j
@Component
@LogicStrategy(logicMode = DefaultLogicFactory.LogicModel.RULE_LOCK)
public class RuleLockLogicFilter implements ILogicFilter<RuleActionEntity.RaffleCenterEntity> {

    @Resource
    private IStrategyRepository iStrategyRepository;

    /*用户抽奖次数*/
    private Long userRaffleCount=0L;

    /**抽奖次数规则过滤*/
    @Override
    public RuleActionEntity<RuleActionEntity.RaffleCenterEntity> filter(RuleMatterEntity ruleMatterEntity) {
        //日志
        log.info("规则过滤-抽奖次数 userId:{} strategyId:{} ruleModel:{} awardId:{}",
                ruleMatterEntity.getUserId(), ruleMatterEntity.getStrategyId(), ruleMatterEntity.getRuleModel(),ruleMatterEntity.getAwardId());

        //根据策略id，规则模型，奖品id查询抽奖次数限制要求
        String ruleValue = iStrategyRepository.queryStrategyRuleValueEntity(ruleMatterEntity.getStrategyId(),ruleMatterEntity.getRuleModel(),ruleMatterEntity.getAwardId());
        Long raffleCount=Long.parseLong(ruleValue);

        if(userRaffleCount>=raffleCount){
            return RuleActionEntity.<RuleActionEntity.RaffleCenterEntity>builder()
                    .code(RuleLogicCheckTypeVO.ALLOW.getCode())
                    .info(RuleLogicCheckTypeVO.ALLOW.getInfo())
                    .build();
        }

        return RuleActionEntity.<RuleActionEntity.RaffleCenterEntity>builder()
                .code(RuleLogicCheckTypeVO.TAKE_OVER.getCode())
                .info(RuleLogicCheckTypeVO.TAKE_OVER.getInfo())
                .build();
    }
}
