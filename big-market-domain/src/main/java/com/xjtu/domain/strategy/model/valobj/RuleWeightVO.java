package com.xjtu.domain.strategy.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author mlei@xjtu
 * @description 权重规则值对象
 * @create 2026/4/2 02:15
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RuleWeightVO {
    /** 原始规则值配置*/
    private String ruleValue;
    /** 权重值*/
    private Integer weight;
    /** 奖品配置*/
    private List<Integer> awardIds;
    /** 奖品列表*/
    private List<Award> awardList;

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Award{
        /** 奖品ID*/
        private Integer awardId;
        /** 奖品标题*/
        private String awardTitle;
    }

}
