package com.xjtu.domain.activity.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author mlei@xjtu
 * @description 活动状态值对象
 * @create 2026/3/24 04:46
 */
@Getter
@AllArgsConstructor
public enum ActivityStateVO {

    CREATE("create","创建"),
    OPEN("create","开启"),
    CLOSE("close","关闭"),
    ;

    private final String code;
    private final String info;
}
