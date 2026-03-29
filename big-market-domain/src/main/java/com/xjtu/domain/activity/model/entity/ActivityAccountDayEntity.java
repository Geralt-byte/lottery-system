package com.xjtu.domain.activity.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mlei@xjtu
 * @description 活动账户（日）实体对象
 * @create 2026/3/29 19:17
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActivityAccountDayEntity {
    /*用户ID*/
    private String userId;
    /*活动ID*/
    private Long activityId;
    /*日期（yyyy-mm-dd）*/
    private String day;
    /*日次数*/
    private Integer dayCount;
    /*日次数-剩余*/
    private Integer dayCountSurplus;
}
