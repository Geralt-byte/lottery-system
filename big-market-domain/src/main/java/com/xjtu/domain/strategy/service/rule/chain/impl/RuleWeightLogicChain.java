package com.xjtu.domain.strategy.service.rule.chain.impl;

import com.xjtu.domain.strategy.repository.IStrategyRepository;
import com.xjtu.domain.strategy.service.armory.IStrategyDispatch;
import com.xjtu.domain.strategy.service.rule.chain.AbstractLogicChain;
import com.xjtu.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import com.xjtu.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author mlei@xjtu
 * @description 权重抽奖责任链
 * @create 2026/3/13 17:22
 */
@Slf4j
@Component("rule_weight")
public class RuleWeightLogicChain extends AbstractLogicChain {

    @Resource
    private IStrategyRepository iStrategyRepository;

    @Resource
    private IStrategyDispatch iStrategyDispatch;


    /**
     * 权重规则过滤；
     * 1. 权重规则格式；4000:102,103,104,105 5000:102,103,104,105,106,107 6000:102,103,104,105,106,107,108,109
     * 2. 解析数据格式；判断哪个范围符合用户的特定抽奖范围
     */
    @Override
    public DefaultChainFactory.StrategyAwardVO logic(String userId, Long strategyId) {
        //日志
        log.info("抽奖责任链-权重开始 userId:{} strategyId:{} ruleModel:{}",userId, strategyId,ruleModel());


        //查询权重规则对应规则值
        String ruleValue = iStrategyRepository.queryStrategyRuleValueEntity(strategyId, ruleModel());

        //数据格式转化
        Map<Long, String> ruleValueMaps = getAnalyticalValue(ruleValue);

        //策略配置了权重规则，但是没有查询到规则值，报警，但是仍然放行
        if(ruleValueMaps==null||ruleValueMaps.isEmpty()){
            log.warn("抽奖责任链-权重警告【策略配置权重，但ruleValue未配置相应值】 userId:{} strategyId:{} ruleModel:{}",userId, strategyId,ruleModel());
            return this.next().logic(userId,strategyId);
        }

        //取出key值，即4000，5000，6000，进行排序
        ArrayList<Long> ruleValueSortMaps = new ArrayList<>(ruleValueMaps.keySet());
        ruleValueSortMaps.sort(Comparator.reverseOrder());

        Integer userScore =iStrategyRepository.queryActivityAccountTotalUseCount(userId, strategyId);
        //找出最小符合的积分值，即对于4000~4999，会选择4000档，对于5000~5999，会选择5000档
        Long nextValue = ruleValueSortMaps
                .stream()
                .filter(key -> userScore >= key)
                .findFirst()
                .orElse(null);

        //查找到符合的积分值，责任链返回抽奖结果
        if(nextValue!=null){
            Integer awardId = iStrategyDispatch.getRandomAwardId(strategyId, ruleValueMaps.get(nextValue));
            log.info("抽奖责任链-权重接管 userId:{} strategyId:{} ruleModel:{} awardId: {}",userId, strategyId,ruleModel(),awardId);
            return DefaultChainFactory.StrategyAwardVO.builder()
                    .awardId(awardId)
                    .logicModel(ruleModel())
                    .build();
        }

        //未查找到，放行
        log.info("抽奖责任链-权重放行 userId:{} strategyId:{} ruleModel:{}",userId, strategyId,ruleModel());
        return this.next().logic(userId,strategyId);
    }

    /**对规则值进行数据格式转化*/
    private Map<Long,String> getAnalyticalValue(String ruleValue){
        String[] ruleValueGroups = ruleValue.split(Constants.SPACE);
        Map<Long,String> ruleValueMaps=new HashMap<>();
        for (String ruleValueGroup : ruleValueGroups) {
            if(ruleValueGroup==null||ruleValueGroup.isEmpty()){
                return ruleValueMaps;
            }
            String[] parts = ruleValueGroup.split(Constants.COLON);
            if(parts.length!=2){
                throw new IllegalArgumentException("rule_weight rule_rule invalid input format" + ruleValueGroup);
            }
            ruleValueMaps.put(Long.parseLong(parts[0]),ruleValueGroup);
        }
        return ruleValueMaps;
    }

    @Override
    protected String ruleModel() {
        return DefaultChainFactory.LogicModel.RULE_WEIGHT.getCode();
    }
}
