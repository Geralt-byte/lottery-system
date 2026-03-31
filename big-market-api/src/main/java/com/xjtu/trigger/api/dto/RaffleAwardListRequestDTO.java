package com.xjtu.trigger.api.dto;

import lombok.Data;

/**
 * @author mlei@xjtu
 * @description 抽奖奖品列表请求对象
 * @create 2026/3/19 01:52
 */
@Data
public class RaffleAwardListRequestDTO {
    /*用户ID*/
    private String userId;
    /*抽奖活动ID*/
    private Long activityId;
}
