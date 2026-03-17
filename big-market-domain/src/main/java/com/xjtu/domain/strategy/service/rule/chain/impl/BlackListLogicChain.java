package com.xjtu.domain.strategy.service.rule.chain.impl;

import com.xjtu.domain.strategy.repository.IStrategyRepository;
import com.xjtu.domain.strategy.service.rule.chain.AbstractLogicChain;
import com.xjtu.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import com.xjtu.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author mlei@xjtu
 * @description 黑名单责任链
 * @create 2026/3/13 16:45
 */
@Slf4j
@Component("rule_blacklist")
public class BlackListLogicChain extends AbstractLogicChain {

    @Resource
    private IStrategyRepository iStrategyRepository;

    @Override
    public DefaultChainFactory.StrategyAwardVO logic(String userId, Long strategyId) {
        //日志
        log.info("抽奖责任链-黑名单开始 userId:{} strategyId:{} ruleModel:{}",userId, strategyId,ruleModel());

        String ruleValue = iStrategyRepository.queryStrategyRuleValueEntity(strategyId, ruleModel());
        String[] splitRuleValue = ruleValue.split(Constants.COLON);
        Integer awardId = Integer.parseInt(splitRuleValue[0]);

        //当用户id位于黑名单时，奖品id返回黑名单规则内配置的固定奖品，code配置接管码
        String[] blackUserIds = splitRuleValue[1].split(Constants.SPLIT);
        for (String blackUserId : blackUserIds) {
            if (userId.equals(blackUserId)) {
                log.info("抽奖责任链-黑名单接管 userId:{} strategyId:{} ruleModel:{} awardId: {}",userId, strategyId,ruleModel(),awardId);
                return DefaultChainFactory.StrategyAwardVO.builder()
                        .awardId(awardId)
                        .logicModel(ruleModel())
                        .build();
            }
        }

        log.info("抽奖责任链-黑名单放行 userId:{} strategyId:{} ruleModel:{}",userId, strategyId,ruleModel());

        return this.next().logic(userId,strategyId);
    }

    @Override
    protected String ruleModel() {
        return DefaultChainFactory.LogicModel.RULE_BLACKLIST.getCode();
    }
}
