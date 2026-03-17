package com.xjtu.infrastructure.persistent.po;

import lombok.Data;

import java.util.Date;

/**
 * @author mlei@xjtu
 * @description 规则边
 * @create 2026/3/15 22:49
 */
@Data
public class RuleTreeNodeLine {
    /*自增ID*/
    private Long id;
    /*规则树ID*/
    private String treeId;
    /*入节点*/
    private String ruleNodeFrom;
    /*出节点*/
    private String ruleNodeTo;
    /*限定类型*/
    private String ruleLimitType;
    /*限定值*/
    private String ruleLimitValue;
    /*创建时间*/
    private Date createTime;
    /*更新时间*/
    private Date updateTime;
}
