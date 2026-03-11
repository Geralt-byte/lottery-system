package com.xjtu.domain.strategy.service.raffle;

import com.xjtu.domain.strategy.model.entity.RaffleFactorEntity;
import com.xjtu.domain.strategy.model.entity.RuleActionEntity;
import com.xjtu.domain.strategy.model.entity.RuleMatterEntity;
import com.xjtu.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.xjtu.domain.strategy.repository.IStrategyRepository;
import com.xjtu.domain.strategy.service.armory.IStrategyDispatch;
import com.xjtu.domain.strategy.service.rule.ILogicFilter;
import com.xjtu.domain.strategy.service.rule.factory.DefaultLogicFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author mlei@xjtu
 * @description 默认抽奖策略实现
 * @create 2026/3/11 22:14
 */
@Slf4j
@Service
public class DefaultRaffleStrategy extends AbstractRaffleStrategy{

    @Resource
    private DefaultLogicFactory defaultLogicFactory;

    public DefaultRaffleStrategy(IStrategyRepository iStrategyRepository, IStrategyDispatch iStrategyDispatch) {
        super(iStrategyRepository, iStrategyDispatch);
    }

    @Override
    protected RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> doCheckRaffleBeforeLogic(RaffleFactorEntity raffleFactorEntity, String[] ruleModels) {
        Map<String, ILogicFilter<RuleActionEntity.RaffleBeforeEntity>> logicFilterMap = defaultLogicFactory.openLogicFilter();

        //黑名单规则过滤调用
        String ruleBlackList = Arrays.stream(ruleModels)
                .filter(ruleModel -> ruleModel.contains(DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode()))
                .findFirst()
                .orElse(null);

        if(StringUtils.isNotBlank(ruleBlackList)){
            ILogicFilter<RuleActionEntity.RaffleBeforeEntity> logicFilter = logicFilterMap.get(DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode());
            RuleMatterEntity ruleMatterEntity = RuleMatterEntity.builder()
                    .userId(raffleFactorEntity.getUserId())
                    .strategyId(raffleFactorEntity.getStrategyId())
                    .ruleModel(DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode())
                    .build();
            ruleMatterEntity.setRuleModel(ruleMatterEntity.getRuleModel());
            RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> ruleActionEntity = logicFilter.filter(ruleMatterEntity);
            if(!ruleActionEntity.getCode().equals(RuleLogicCheckTypeVO.ALLOW.getCode())){
                return ruleActionEntity;
            }
        }

        //按照顺序过滤剩余规则
        List<String> ruleLists = Arrays.stream(ruleModels)
                .filter(ruleModel -> !ruleModel.equals(DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode()))
                .collect(Collectors.toList());

        RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> ruleActionEntity=null;
        for (String ruleModel : ruleLists) {
            ILogicFilter<RuleActionEntity.RaffleBeforeEntity> logicFilter = logicFilterMap.get(ruleModel);
            RuleMatterEntity ruleMatterEntity=RuleMatterEntity.builder()
                    .userId(raffleFactorEntity.getUserId())
                    .strategyId(raffleFactorEntity.getStrategyId())
                    .ruleModel(ruleModel)
                    .build();
            ruleMatterEntity.setAwardId(ruleMatterEntity.getAwardId());
            ruleActionEntity = logicFilter.filter(ruleMatterEntity);
            //非放行结果按顺序过滤
            log.info("抽奖前规则过滤 userId:{} ruleModel: {} code: {} info: {}",
                    raffleFactorEntity.getUserId(),ruleModel,ruleActionEntity.getCode(),ruleActionEntity.getInfo());
            if(!ruleActionEntity.getCode().equals(RuleLogicCheckTypeVO.ALLOW.getCode())){
                return ruleActionEntity;
            }
        }
        return ruleActionEntity;
    }
}
