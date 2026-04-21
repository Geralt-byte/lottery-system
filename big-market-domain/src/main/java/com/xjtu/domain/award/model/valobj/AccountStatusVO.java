package com.xjtu.domain.award.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author mlei@xjtu
 * @description 积分账户状态枚举
 * @create 2026/4/21 23:42
 */
@Getter
@AllArgsConstructor
public enum AccountStatusVO {

    open("open","开启"),
    close("close","冻结"),
    ;

    private final String code;
    private final String desc;
}
