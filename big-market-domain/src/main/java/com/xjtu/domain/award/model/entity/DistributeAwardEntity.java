package com.xjtu.domain.award.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mlei@xjtu
 * @description 分发奖品实体
 * @create 2026/4/21 23:11
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DistributeAwardEntity {
    /*用户ID*/
    private String userId;
    /*抽奖订单ID*/
    private String orderId;
    /*奖品ID*/
    private Integer awardId;
    /*奖品配置*/
    private String awardConfig;
}
