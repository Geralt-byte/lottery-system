package com.xjtu.domain.award.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author mlei@xjtu
 * @description 奖品状态枚举值对象 【值对象，用于描述对象属性的值，一个对象中，一个属性，有多个状态值。】
 * @create 2026/3/30 03:14
 */
@Getter
@AllArgsConstructor
public enum AwardStateVO {

    create("create","创建"),
    complete("complete", "发奖完成"),
    fail("fail", "发奖失败"),
    ;

    private final String code;
    private final String desc;
}
