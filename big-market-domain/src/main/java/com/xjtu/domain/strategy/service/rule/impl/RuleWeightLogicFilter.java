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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mlei@xjtu
 * @description 【抽奖前规则】根据权重返回可抽奖范围
 * @create 2026/3/11 22:25
 */
@Slf4j
@Component
@LogicStrategy(logicMode = DefaultLogicFactory.LogicModel.RULE_WEIGHT)
public class RuleWeightLogicFilter implements ILogicFilter<RuleActionEntity.RaffleBeforeEntity> {

    @Resource
    private IStrategyRepository iStrategyRepository;

    public Long userScore = 4500L;


    /**
     * 权重规则过滤；
     * 1. 权重规则格式；4000:102,103,104,105 5000:102,103,104,105,106,107 6000:102,103,104,105,106,107,108,109
     * 2. 解析数据格式；判断哪个范围符合用户的特定抽奖范围
     *
     * @param ruleMatterEntity 规则物料实体对象
     * @return 规则过滤结果
     */
    @Override
    public RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> filter(RuleMatterEntity ruleMatterEntity) {
        log.info("规则过滤-权重范围 userId:{} strategyId:{} ruleModel:{}",
                ruleMatterEntity.getUserId(),ruleMatterEntity.getStrategyId(),ruleMatterEntity.getRuleModel());

        //用户id
        String userId = ruleMatterEntity.getUserId();

        //查询权重规则对应规则值
        String ruleValue = iStrategyRepository.queryStrategyRuleValueEntity(ruleMatterEntity.getStrategyId(),
                ruleMatterEntity.getRuleModel(),ruleMatterEntity.getAwardId());

        //数据格式转化
        Map<Long, String> ruleValueMaps = getAnalyticalValue(ruleValue);

        //转化后集合列表为空，放行
        if(ruleValueMaps==null||ruleValueMaps.isEmpty()){
            return RuleActionEntity.<RuleActionEntity.RaffleBeforeEntity>builder()
                    .code(RuleLogicCheckTypeVO.ALLOW.getCode())
                    .info(RuleLogicCheckTypeVO.ALLOW.getInfo())
                    .build();
        }

        //取出key值，即4000，5000，6000，进行排序
        ArrayList<Long> ruleValueSortMaps = new ArrayList<>(ruleValueMaps.keySet());
        Collections.sort(ruleValueSortMaps);

        //找出最小符合的积分值，即对于4000~4999，会选择4000档，对于5000~5999，会选择5000档
        Long nextValue = ruleValueSortMaps
                .stream()
                .filter(key -> userScore >= key)
                .findFirst()
                .orElse(null);

        //查找到符合的积分值，code配置接管码，规则权重值返回积分值对应的奖品列表
        if(nextValue!=null){
            return RuleActionEntity.<RuleActionEntity.RaffleBeforeEntity>builder()
                    .code(RuleLogicCheckTypeVO.TAKE_OVER.getCode())
                    .info(RuleLogicCheckTypeVO.TAKE_OVER.getInfo())
                    .ruleModel(DefaultLogicFactory.LogicModel.RULE_WEIGHT.getCode())
                    .data(RuleActionEntity.RaffleBeforeEntity
                            .builder()
                            .strategyId(ruleMatterEntity.getStrategyId())
                            .ruleWeightValueKey(ruleValueMaps.get(nextValue))
                            .build())
                    .build();
        }

        //未查找到，放行
        return RuleActionEntity.<RuleActionEntity.RaffleBeforeEntity>builder()
                .code(RuleLogicCheckTypeVO.ALLOW.getCode())
                .info(RuleLogicCheckTypeVO.ALLOW.getInfo())
                .build();

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
}
