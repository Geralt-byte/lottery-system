package com.xjtu.domain.rebate.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author mlei@xjtu
 * @description 返利类型（sku 活动库存充值商品、integral 用户活动积分）
 * @create 2026/4/1 13:40
 */
@Getter
@AllArgsConstructor
public enum RebateTypeVO {

    SKU("sku","活动库存充值商品"),
    INTEGRAL("integral","用户活动积分"),
    ;

    private final String code;
    private final String info;
}
