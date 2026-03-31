package com.xjtu.domain.strategy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author mlei@xjtu
 * @description 抽奖因子实体
 * @create 2026/3/11 21:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RaffleFactorEntity {
    /*用户id*/
    private String userId;
    /*策略id*/
    private Long strategyId;
    /*结束时间*/
    private Date endDateTime;
}
