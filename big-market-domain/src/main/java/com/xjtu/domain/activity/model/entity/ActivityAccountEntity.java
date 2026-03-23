package com.xjtu.domain.activity.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author mlei@xjtu
 * @description 抽奖活动账户表
 * @create 2026/3/22 02:50
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActivityAccountEntity {
    /**用户ID*/
    private String userId;
    /**活动ID*/
    private Long activityId;
    /**总次数*/
    private Integer totalCount;
    /**总次数-剩余*/
    private Integer totalCountSurplus;
    /**日次数*/
    private Integer dayCount;
    /**日次数-剩余*/
    private Integer dayCountSurplus;
    /**月次数*/
    private Integer monthCount;
    /**月次数-剩余*/
    private Integer monthCountSurplus;
}
