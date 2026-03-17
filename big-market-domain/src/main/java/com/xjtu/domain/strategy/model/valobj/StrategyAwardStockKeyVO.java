package com.xjtu.domain.strategy.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mlei@xjtu
 * @description 抽奖库存值对象
 * @create 2026/3/17 15:19
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StrategyAwardStockKeyVO {

    private Long strategyId;

    private Integer awardId;
}
