package com.xjtu.domain.strategy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mlei@xjtu
 * @description 抽奖奖品实体
 * @create 2026/3/11 21:35
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RaffleAwardEntity {

    /*奖品id*/
    private Integer awardId;
    /** 抽奖奖品标题 */
    private String awardTitle;
    /*奖品配置信息*/
    private String awardConfig;
    /*奖品顺序号*/
    private Integer sort;
}
