package com.xjtu.domain.activity.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author mlei@xjtu
 * @description 订单状态枚举值对象（用于描述对象属性的值，如枚举，不影响数据库操作的对象，无生命周期）
 * @create 2026/3/24 04:48
 */
@Getter
@AllArgsConstructor
public enum OrderStateVO {

    COMPLETED("completed","完成"),
    ;

    private final String code;
    private final String info;
}
