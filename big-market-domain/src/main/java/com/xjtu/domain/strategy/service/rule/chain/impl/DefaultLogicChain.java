package com.xjtu.domain.strategy.service.rule.chain.impl;

import com.xjtu.domain.strategy.repository.IStrategyRepository;
import com.xjtu.domain.strategy.service.armory.IStrategyDispatch;
import com.xjtu.domain.strategy.service.rule.chain.AbstractLogicChain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author mlei@xjtu
 * @description 默认的责任链「作为最后一个链」
 * @create 2026/3/13 16:45
 */
@Slf4j
@Component("default")
public class DefaultLogicChain extends AbstractLogicChain {

    @Resource
    private IStrategyDispatch iStrategyDispatch;

    @Override
    public Integer logic(String userId, Long strategyId) {
        Integer awardId = iStrategyDispatch.getRandomAwardId(strategyId);
        log.info("抽奖责任链-默认处理 userId: {} strategyId: {}",userId,strategyId);
        return awardId;
    }

    @Override
    protected String ruleModel() {
        return "default";
    }
}
