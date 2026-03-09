package com.xjtu.domain.strategy.model.entity;

import com.xjtu.types.common.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * @author mlei@xjtu
 * @description StrategyEntity
 * @create 2026/3/9 22:49
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StrategyEntity {
    /*抽奖策略ID*/
    private Long strategyId;
    /*抽奖策略描述*/
    private String strategyDesc;
    /*抽奖规则模型 rule_weight,rule_blacklist*/
    private String ruleModels;

    /*将规则分割存储到数组中*/
    public String[] ruleModels() {
        if (StringUtils.isBlank(ruleModels)) return null;
        return ruleModels.split(Constants.SPLIT);
    }

    /*选择权重规则返回*/
    public String getRuleWeight() {
        String[] ruleModels = this.ruleModels();
        for (String ruleModel : ruleModels) {
            if(ruleModel.equals("rule_weight")){
                return ruleModel;
            }
        }
        return null;
    }
}
