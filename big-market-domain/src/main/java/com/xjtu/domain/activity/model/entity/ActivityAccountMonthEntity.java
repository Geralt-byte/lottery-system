package com.xjtu.domain.activity.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mlei@xjtu
 * @description 活动账户（月）实体对象
 * @create 2026/3/29 19:18
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActivityAccountMonthEntity {
    /*用户ID*/
    private String userId;
    /*活动ID*/
    private Long activityId;
    /*月份（yyyy-mm）*/
    private String month;
    /*月次数*/
    private Integer monthCount;
    /*月次数-剩余*/
    private Integer monthCountSurplus;
}
