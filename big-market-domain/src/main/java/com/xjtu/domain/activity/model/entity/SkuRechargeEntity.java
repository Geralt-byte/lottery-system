package com.xjtu.domain.activity.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mlei@xjtu
 * @description 活动商品充值实体对象
 * @create 2026/3/25 03:31
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SkuRechargeEntity {
    /** 用户ID */
    private String userId;
    /** 商品SKU - activity + activity count */
    private Long sku;
    /**业务仿重ID - 外部透传的，确保幂等*/
    private String outBusinessNo;
}
