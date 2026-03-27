package com.xjtu.infrastructure.persistent.po;

import lombok.Data;

import java.util.Date;

/**
 * @author mlei@xjtu
 * @description 用户抽奖订单表
 * @create 2026/3/28 00:26
 */
@Data
public class UserRaffleOrder {
    /*自增ID*/
    private Long id;
    /*用户ID*/
    private String userId;
    /*活动ID*/
    private Long activityId;
    /*活动名称*/
    private Long activityName;
    /*抽奖策略ID*/
    private Long strategyId;
    /*订单ID*/
    private String orderId;
    /*下单时间*/
    private Date orderTime;
    /*订单状态；create-创建、used-已使用、cancel-已作废*/
    private String orderState;
    /*创建时间*/
    private Date createTime;
    /*更新时间*/
    private Date updateTime;
}
