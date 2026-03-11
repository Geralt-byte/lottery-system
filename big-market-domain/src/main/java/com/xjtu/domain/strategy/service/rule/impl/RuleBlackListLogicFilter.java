package com.xjtu.domain.strategy.service.rule.impl;

import com.xjtu.domain.strategy.model.entity.RuleActionEntity;
import com.xjtu.domain.strategy.model.entity.RuleMatterEntity;
import com.xjtu.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.xjtu.domain.strategy.repository.IStrategyRepository;
import com.xjtu.domain.strategy.service.annotation.LogicStrategy;
import com.xjtu.domain.strategy.service.rule.ILogicFilter;
import com.xjtu.domain.strategy.service.rule.factory.DefaultLogicFactory;
import com.xjtu.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author mlei@xjtu
 * @description 用户黑名单过滤
 * @create 2026/3/11 22:24
 */
@Slf4j
@Component
@LogicStrategy(logicMode = DefaultLogicFactory.LogicModel.RULE_BLACKLIST)
public class RuleBlackListLogicFilter implements ILogicFilter<RuleActionEntity.RaffleBeforeEntity> {

    @Resource
    private IStrategyRepository iStrategyRepository;

    /**黑名单过滤*/
    @Override
    public RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> filter(RuleMatterEntity ruleMatterEntity) {
        //日志
        log.info("规则过滤-黑名单 userId:{} strategyId:{} ruleModel:{}",
                ruleMatterEntity.getUserId(), ruleMatterEntity.getStrategyId(), ruleMatterEntity.getRuleModel());

        //用户id
        String userId = ruleMatterEntity.getUserId();

        //查询黑名单规则对应规则值
        String ruleValue = iStrategyRepository.queryStrategyRuleValueEntity(ruleMatterEntity.getStrategyId(),
                ruleMatterEntity.getRuleModel(), ruleMatterEntity.getAwardId());
        String[] splitRuleValue = ruleValue.split(Constants.COLON);
        Integer awardId = Integer.parseInt(splitRuleValue[0]);

        //当用户id位于黑名单时，奖品id返回黑名单规则内配置的固定奖品，code配置接管码
        String[] blackUserIds = splitRuleValue[1].split(Constants.SPLIT);
        for (String blackUserId : blackUserIds) {
            if (userId.equals(blackUserId)) {
                return RuleActionEntity.<RuleActionEntity.RaffleBeforeEntity>builder()
                        .code(RuleLogicCheckTypeVO.TAKE_OVER.getCode())
                        .info(RuleLogicCheckTypeVO.TAKE_OVER.getInfo())
                        .ruleModel(DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode())
                        .data(RuleActionEntity.RaffleBeforeEntity
                                .builder()
                                .strategyId(ruleMatterEntity.getStrategyId())
                                .awardId(awardId)
                                .build())
                        .build();
            }
        }

        //白名单，放行，code配置放行码
        return RuleActionEntity.<RuleActionEntity.RaffleBeforeEntity>builder()
                .code(RuleLogicCheckTypeVO.ALLOW.getCode())
                .info(RuleLogicCheckTypeVO.ALLOW.getInfo())
                .build();
    }
}
