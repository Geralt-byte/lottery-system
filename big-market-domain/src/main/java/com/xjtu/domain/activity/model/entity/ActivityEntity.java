package com.xjtu.domain.activity.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author mlei@xjtu
 * @description 抽奖活动表
 * @create 2026/3/22 02:50
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActivityEntity {
    /**活动ID*/
    private Long activityId;
    /**活动名称*/
    private String activityName;
    /**活动描述*/
    private String activityDesc;
    /**开始时间*/
    private Date beginDateTime;
    /**结束时间*/
    private Date endDateTime;
    /**抽奖策略ID*/
    private Long strategyId;
    /**活动状态*/
    private String state;
}
