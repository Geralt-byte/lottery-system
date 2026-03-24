package com.xjtu.domain.activity.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author mlei@xjtu
 * @description 抽奖活动单
 * @create 2026/3/22 02:51
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActivityOrderEntity {
    /**用户ID*/
    private String userId;
    /**商品sku - 把每一个组合当做一个商品*/
    private Long sku;
    /**活动ID*/
    private Long activityId;
    /**活动名称*/
    private String activityName;
    /**抽奖策略ID*/
    private Long strategyId;
    /**订单ID*/
    private String orderId;
    /**下单时间*/
    private Date orderTime;
    /**总次数*/
    private Integer totalCount;
    /**日次数*/
    private Integer dayCount;
    /**月次数*/
    private Integer monthCount;
    /**订单状态（not_used、used、expire）*/
    private String state;
    /**业务仿重ID - 外部透传的，确保幂等*/
    private String outBusinessNo;
}
