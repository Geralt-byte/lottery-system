package com.xjtu.trigger.api.dto;

import lombok.Data;

/**
 * @author mlei@xjtu
 * @description 抽奖策略规则，权重配置，查询N次抽奖可解锁奖品范围，请求对象
 * @create 2026/4/2 01:51
 */
@Data
public class StrategyRuleWeightRequestDTO {
    /*用户ID*/
    private String userId;
    /*抽奖活动ID*/
    private Long activityId;
}
