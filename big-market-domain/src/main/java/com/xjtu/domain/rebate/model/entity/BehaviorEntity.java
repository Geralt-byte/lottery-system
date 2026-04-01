package com.xjtu.domain.rebate.model.entity;

import com.xjtu.domain.rebate.model.valobj.BehaviorTypeVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mlei@xjtu
 * @description 行为实体对象
 * @create 2026/4/1 08:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BehaviorEntity {
    /*用户ID*/
    private String userId;
    /*行为类型；sign 签到、openai_pay 支付*/
    private BehaviorTypeVO behaviorTypeVO;
    /*业务ID；签到则是日期字符串，支付则是外部的业务ID*/
    private String outBusinessNo;
}
