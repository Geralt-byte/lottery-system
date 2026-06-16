package com.xjtu.infrastructure.persistent.po;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author mlei@xjtu
 * @description 用户积分表
 * @create 2026/4/21 23:04
 */
@Data
public class UserCreditAccount {
    /*自增ID*/
    private Long id;
    /*用户ID*/
    private String userId;
    /*总积分，显示总账户值，记录一个人获得的总积分*/
    private BigDecimal totalAmount;
    /*可用积分，每次扣减的值*/
    private BigDecimal availableAmount;
    /*账户状态【open - 可用，close - 冻结】*/
    private String accountStatus;
    /*创建时间*/
    private Date createTime;
    /*更新时间*/
    private Date updateTime;
}
