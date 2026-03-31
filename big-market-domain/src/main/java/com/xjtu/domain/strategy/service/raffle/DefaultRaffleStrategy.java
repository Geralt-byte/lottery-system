package com.xjtu.domain.strategy.service.raffle;

import com.xjtu.domain.strategy.model.entity.StrategyAwardEntity;
import com.xjtu.domain.strategy.model.valobj.RuleTreeVO;
import com.xjtu.domain.strategy.model.valobj.StrategyAwardRuleModelVO;
import com.xjtu.domain.strategy.model.valobj.StrategyAwardStockKeyVO;
import com.xjtu.domain.strategy.repository.IStrategyRepository;
import com.xjtu.domain.strategy.service.AbstractRaffleStrategy;
import com.xjtu.domain.strategy.service.IRaffleAward;
import com.xjtu.domain.strategy.service.IRaffleRule;
import com.xjtu.domain.strategy.service.IRaffleStock;
import com.xjtu.domain.strategy.service.armory.IStrategyDispatch;
import com.xjtu.domain.strategy.service.rule.chain.ILogicChain;
import com.xjtu.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import com.xjtu.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import com.xjtu.domain.strategy.service.rule.tree.factory.engine.IDecisionTreeEngine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author mlei@xjtu
 * @description 默认抽奖策略实现
 * @create 2026/3/11 22:14
 */
@Slf4j
@Service
public class DefaultRaffleStrategy extends AbstractRaffleStrategy implements IRaffleStock, IRaffleAward, IRaffleRule {

    public DefaultRaffleStrategy(IStrategyRepository iStrategyRepository, IStrategyDispatch iStrategyDispatch, DefaultChainFactory defaultChainFactory, DefaultTreeFactory defaultTreeFactory) {
        super(iStrategyRepository, iStrategyDispatch, defaultChainFactory, defaultTreeFactory);
    }


    @Override
    public DefaultChainFactory.StrategyAwardVO raffleLoginChain(String userId, Long strategyId) {
        ILogicChain iLogicChain = defaultChainFactory.openLogicChain(strategyId);
        return iLogicChain.logic(userId, strategyId);
    }

    @Override
    public DefaultTreeFactory.StrategyAwardVO raffleLogicTree(String userId, Long strategyId, Integer awardId) {
        return raffleLogicTree(userId,strategyId,awardId,null);
    }

    @Override
    public DefaultTreeFactory.StrategyAwardVO raffleLogicTree(String userId, Long strategyId, Integer awardId, Date endDateTime) {
        //抽奖中规则过滤，判断抽中特定奖品（需要抽奖次数解锁的奖品）此时用户的抽奖次数是否满足要求
        StrategyAwardRuleModelVO strategyAwardRuleModelVO = iStrategyRepository.queryStrategyAwardRuleModelVO(strategyId, awardId);
        //说明未在策略奖品表中查找到对应奖品有策略配置,直接返回奖品
        if (strategyAwardRuleModelVO == null) {
            return DefaultTreeFactory.StrategyAwardVO.builder().awardId(awardId).build();
        }

        RuleTreeVO ruleTreeVO = iStrategyRepository.queryRuleTreeVOByTreeId(strategyAwardRuleModelVO.getRuleModels());
        if (ruleTreeVO == null) {
            throw new RuntimeException("存在抽奖策略配置的规则模型 Key，未在库表 rule_tree、rule_tree_node、rule_tree_line 配置对应的规则树信息 "
                    + strategyAwardRuleModelVO.getRuleModels());
        }
        IDecisionTreeEngine treeEngine = defaultTreeFactory.openLogicTree(ruleTreeVO);
        return treeEngine.process(userId, strategyId, awardId,endDateTime);
    }

    @Override
    public StrategyAwardStockKeyVO takeQueueValue() throws InterruptedException{
        return iStrategyRepository.takeQueueValue();
    }

    @Override
    public void updateStrategyAwardStock(Long strategyId, Integer awardId) {
        iStrategyRepository.updateStrategyAwardStock(strategyId, awardId);
    }

    @Override
    public List<StrategyAwardEntity> queryRaffleStrategyAwardList(Long strategyId) {
        return iStrategyRepository.queryStrategyAwardList(strategyId);
    }

    @Override
    public List<StrategyAwardEntity> queryRaffleStrategyAwardListByActivityId(Long activityId) {
        Long strategyId = iStrategyRepository.queryStrategyIdByActivityId(activityId);
        return queryRaffleStrategyAwardList(strategyId);
    }

    @Override
    public Map<String, Integer> queryAwardRuleLockCount(String[] treeIds) {
        return iStrategyRepository.queryAwardRuleLockCount(treeIds);
    }
}
