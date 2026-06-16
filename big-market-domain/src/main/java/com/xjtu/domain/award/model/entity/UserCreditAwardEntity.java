package com.xjtu.domain.award.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author mlei@xjtu
 * @description 用户积分奖品实体对象
 * @create 2026/4/21 23:09
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCreditAwardEntity {
    /*用户ID*/
    private String userId;
    /*积分值*/
    private BigDecimal creditAmount;
}
