package com.xjtu.trigger.api.dto;

import lombok.Data;

/**
 * @author mlei@xjtu
 * @description 活动抽奖请求对象
 * @create 2026/3/31 00:28
 */
@Data
public class ActivityDrawRequestDTO {
    /*用户ID*/
    private String userId;
    /*活动ID*/
    private Long activityId;
}
