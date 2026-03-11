package com.xjtu.domain.strategy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mlei@xjtu
 * @description 规则物料实体对象，用于过滤规则的必要参数信息
 * @create 2026/3/11 22:20
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RuleMatterEntity {
    /*用户id*/
    private String userId;
    /*策略id*/
    private Long strategyId;
    /*奖品id*/
    private Integer awardId;
    /*抽象规则类型；1-策略规则、2-奖品规则*/
    private String ruleModel;
}
