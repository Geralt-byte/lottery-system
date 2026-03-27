package com.xjtu.infrastructure.persistent.po;

import lombok.Data;

import java.util.Date;

/**
 * @author mlei@xjtu
 * @description 抽奖活动账户表-月次数
 * @create 2026/3/28 00:17
 */
@Data
public class RaffleActivityAccountMonth {
    /*自增ID*/
    private Long id;
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
    /*创建时间*/
    private Date createTime;
    /*更新时间*/
    private Date updateTime;
}
