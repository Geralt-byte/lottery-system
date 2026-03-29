package com.xjtu.domain.activity.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author mlei@xjtu
 * @description UserRaffleOrderStateVO
 * @create 2026/3/29 19:27
 */
@Getter
@AllArgsConstructor
public enum UserRaffleOrderStateVO {

    create("create","创建"),
    used("used","已使用"),
    cancel("cancel","已作废"),
    ;

    private final String code;
    private final String info;
}
