package com.xjtu.trigger.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author mlei@xjtu
 * @description 抽奖策略规则，权重配置，查询N次抽奖可解锁奖品范围，应答对象
 * @create 2026/4/2 01:52
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StrategyRuleWeightResponseDTO {
    /*权重规则配置的抽奖次数*/
    private Integer ruleWeightCount;
    /*用户活动账户总抽奖次数*/
    private Integer userActivityAccountTotalUseCount;
    /*当前权重可抽奖范围*/
    private List<StrategyAward> strategyAwards;

    @Data
    public static class StrategyAward {
        /*奖品ID*/
        private Integer awardId;
        /*奖品标题*/
        private String awardTitle;
    }
}
