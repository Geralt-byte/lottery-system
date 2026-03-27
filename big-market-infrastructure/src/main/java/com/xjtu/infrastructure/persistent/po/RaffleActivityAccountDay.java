package com.xjtu.infrastructure.persistent.po;

import lombok.Data;

import java.util.Date;

/**
 * @author mlei@xjtu
 * @description 抽奖活动账户表-日次数
 * @create 2026/3/28 00:14
 */
@Data
public class RaffleActivityAccountDay {
    /*自增ID*/
    private Long id;
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
    /*创建时间*/
    private Date createTime;
    /*更新时间*/
    private Date updateTime;
}
