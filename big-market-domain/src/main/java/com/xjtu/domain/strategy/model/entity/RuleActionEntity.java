package com.xjtu.domain.strategy.model.entity;

import com.xjtu.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import lombok.*;

/**
 * @author mlei@xjtu
 * @description 规则动作实体
 * @create 2026/3/11 21:56
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RuleActionEntity<T extends RuleActionEntity.RaffleEntity> {

    private String code= RuleLogicCheckTypeVO.ALLOW.getCode();
    private String info=RuleLogicCheckTypeVO.ALLOW.getInfo();
    private String ruleModel;
    private T data;

    public static class RaffleEntity{

    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    /*抽奖前*/
    public static class RaffleBeforeEntity extends RaffleEntity{
        /*策略id*/
        private Long strategyId;
        /*规则权重值*/
        private String ruleWeightValueKey;
        /*奖品id*/
        private Integer awardId;
    }

    /*抽奖中*/
    public static class RaffleCenterEntity extends RaffleEntity{

    }

    /*抽奖后*/
    public static class RaffleAfterEntity extends RaffleEntity{

    }

}
