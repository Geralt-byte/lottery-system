package com.xjtu.infrastructure.persistent.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author mlei@xjtu
 * @description 抽奖活动账户表-月次数
 * @create 2026/3/28 00:17
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RaffleActivityAccountMonth {
    /*自增ID*/
    private Long id;
    /*用户ID*/
    private String userId;
    /*活动ID*/
    private Long activityId;
    /*月份（yyyy-mm）*/
    private String month;
    /*月次数*/
    private Integer monthCount;
    /*月次数-剩余*/
    private Integer monthCountSurplus;
    /*创建时间*/
    private Date createTime;
    /*更新时间*/
    private Date updateTime;

    private static final SimpleDateFormat dateFormatMonth = new SimpleDateFormat("yyyy-MM");

    public static String currentMonth() {
        return dateFormatMonth.format(new Date());
    }
}
