package com.xjtu.domain.strategy.model.valobj;

import com.xjtu.domain.strategy.service.rule.filter.factory.DefaultLogicFactory;
import com.xjtu.types.common.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mlei@xjtu
 * @description StrategyAwardRuleModelVO
 * @create 2026/3/13 10:55
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StrategyAwardRuleModelVO {

    private String ruleModels;

    public String[] raffleCenterRuleModelList(){
        List<String> ruleModelList=new ArrayList<>();
        if(StringUtils.isBlank(ruleModels)) return null;
        String[] ruleModelValues = ruleModels.split(Constants.SPLIT);
        for (String ruleModelValue : ruleModelValues) {
            if(DefaultLogicFactory.LogicModel.isCenter(ruleModelValue)){
                ruleModelList.add(ruleModelValue);
            }
        }
        return ruleModelList.toArray(new String[0]);
    }

    public String[] raffleAfterRuleModelList(){
        List<String> ruleModelList=new ArrayList<>();
        if(StringUtils.isBlank(ruleModels)) return null;
        String[] ruleModelValues = ruleModels.split(Constants.SPLIT);
        for (String ruleModelValue : ruleModelValues) {
            if(DefaultLogicFactory.LogicModel.isAfter(ruleModelValue)){
                ruleModelList.add(ruleModelValue);
            }
        }
        return ruleModelList.toArray(new String[0]);
    }
}
