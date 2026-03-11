package com.xjtu.domain.strategy.model.entity;

import com.xjtu.types.common.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

/**
 * @author mlei@xjtu
 * @description StrategyRuleEntity
 * @create 2026/3/9 23:40
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StrategyRuleEntity {
    /*抽奖策略ID*/
    private Long strategyId;
    /*抽奖奖品ID【规则类型为策略，则不需要奖品ID】*/
    private Integer awardId;
    /*抽象规则类型；1-策略规则、2-奖品规则*/
    private Integer ruleType;
    /*抽奖规则类型【rule_random - 随机值计算、rule_lock - 抽奖几次后解锁、rule_luck_award - 幸运奖(兜底奖品)】*/
    private String ruleModel;
    /*抽奖规则比值*/
    private String ruleValue;
    /*抽奖规则描述*/
    private String ruleDesc;

    /**
     * 将规则权重的ruleValue拆分存入map中
     * 案例:4000:102,103,104,105 5000:102,103,104,105,106,107 6000:102,103,104,105,106,107,108,109
     */
    public Map<String, List<Integer>> getRuleWeightValues(){
        //检测到规则模型是权重模型，继续函数
        if(!ruleModel.equals("rule_weight")) return null;
        //根据空格拆分
        String[] ruleValueGroups = ruleValue.split(Constants.SPACE);
        Map<String, List<Integer>> resultMap=new HashMap<>();
        for (String ruleValueGroup : ruleValueGroups) {
            if(ruleValueGroup==null||ruleValueGroup.isEmpty()){
                return resultMap;
            }
            //根据冒号拆分
            String[] parts = ruleValueGroup.split(Constants.COLON);
            if(parts.length!=2){
                throw new IllegalArgumentException("rule_weight rule_rule invalid input format" + ruleValueGroup);
            }
            //根据逗号拆分
            String[] weightValues = parts[1].split(Constants.SPLIT);
            List<Integer> values=new ArrayList<>();
            for (String weightValue : weightValues) {
                values.add(Integer.valueOf(weightValue));
            }
            resultMap.put(ruleValueGroup,values);
        }
        return resultMap;
    }
}
