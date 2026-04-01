package com.xjtu.domain.rebate.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mlei@xjtu
 * @description 日常行为返利配置值对象
 * @create 2026/4/1 08:44
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DailyBehaviorRebateVO {
    /*行为类型（sign 签到、openai_pay 支付）*/
    private String behaviorType;
    /*返利描述*/
    private String rebateDesc;
    /*返利类型（sku 活动库存充值商品、integral 用户活动积分）*/
    private String rebateType;
    /*返利配置*/
    private String rebateConfig;
}
