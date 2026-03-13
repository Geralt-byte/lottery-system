package com.xjtu.domain.strategy.service.raffle;

import com.xjtu.domain.strategy.model.entity.RaffleFactorEntity;
import com.xjtu.domain.strategy.model.entity.RuleActionEntity;
import com.xjtu.domain.strategy.model.entity.RuleMatterEntity;
import com.xjtu.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.xjtu.domain.strategy.repository.IStrategyRepository;
import com.xjtu.domain.strategy.service.AbstractRaffleStrategy;
import com.xjtu.domain.strategy.service.armory.IStrategyDispatch;
import com.xjtu.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import com.xjtu.domain.strategy.service.rule.filter.ILogicFilter;
import com.xjtu.domain.strategy.service.rule.filter.factory.DefaultLogicFactory;
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
public class DefaultRaffleStrategy extends AbstractRaffleStrategy {

    @Resource
    private DefaultLogicFactory defaultLogicFactory;

    public DefaultRaffleStrategy(IStrategyRepository iStrategyRepository, IStrategyDispatch iStrategyDispatch, DefaultChainFactory defaultChainFactory) {
        super(iStrategyRepository, iStrategyDispatch,defaultChainFactory);
    }


    @Override
    protected RuleActionEntity<RuleActionEntity.RaffleCenterEntity> doCheckRaffleCenterLogic(RaffleFactorEntity raffleFactorEntity, String[] ruleModels) {
        if(ruleModels==null||ruleModels.length==0){
            return RuleActionEntity.<RuleActionEntity.RaffleCenterEntity>builder()
                    .code(RuleLogicCheckTypeVO.ALLOW.getCode())
                    .info(RuleLogicCheckTypeVO.ALLOW.getInfo())
                    .build();
        }

        //产生过滤器bean对象列表
        Map<String, ILogicFilter<RuleActionEntity.RaffleCenterEntity>> logicFilterMap = defaultLogicFactory.openLogicFilter();

        RuleActionEntity<RuleActionEntity.RaffleCenterEntity> ruleActionEntity=null;
        for (String ruleModel : ruleModels) {
            //调用对应于当前规则的过滤器
            ILogicFilter<RuleActionEntity.RaffleCenterEntity> logicFilter = logicFilterMap.get(ruleModel);
            RuleMatterEntity ruleMatterEntity=RuleMatterEntity.builder()
                    .userId(raffleFactorEntity.getUserId())
                    .strategyId(raffleFactorEntity.getStrategyId())
                    .ruleModel(ruleModel)
                    .awardId(raffleFactorEntity.getAwardId())
                    .build();
            //抽奖中过滤调用
            ruleActionEntity = logicFilter.filter(ruleMatterEntity);
            //非放行结果按顺序过滤
            log.info("抽奖中规则过滤 userId:{} ruleModel: {} code: {} info: {}",
                    raffleFactorEntity.getUserId(),ruleModel,ruleActionEntity.getCode(),ruleActionEntity.getInfo());
            if(!ruleActionEntity.getCode().equals(RuleLogicCheckTypeVO.ALLOW.getCode())){
                return ruleActionEntity;
            }
        }
        return ruleActionEntity;
    }
}
