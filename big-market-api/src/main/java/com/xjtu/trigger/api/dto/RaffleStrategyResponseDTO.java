package com.xjtu.trigger.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mlei@xjtu
 * @description 抽奖应答结果对象
 * @create 2026/3/19 01:54
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RaffleStrategyResponseDTO {

    /*奖品ID*/
    private Integer awardId;
    /*排序编号【策略奖品配置的奖品顺序编号】*/
    private Integer awardIndex;
}
