package com.xjtu.trigger.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mlei@xjtu
 * @description 用户活动账户请求对象
 * @create 2026/4/2 01:07
 */
@Data
public class UserActivityAccountRequestDTO {
    /*用户ID*/
    private String userId;
    /*抽奖活动ID*/
    private Long activityId;
}
