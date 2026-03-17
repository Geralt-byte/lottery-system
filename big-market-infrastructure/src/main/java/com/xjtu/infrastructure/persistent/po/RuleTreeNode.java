package com.xjtu.infrastructure.persistent.po;

import lombok.Data;

import java.util.Date;

/**
 * @author mlei@xjtu
 * @description 规则树节点
 * @create 2026/3/15 22:47
 */
@Data
public class RuleTreeNode {
    /*自增ID*/
    private Long id;
    /*规则树ID*/
    private String treeId;
    /*规则key*/
    private String ruleKey;
    /*规则描述*/
    private String ruleDesc;
    /*规则比值*/
    private String ruleValue;
    /*创建时间*/
    private Date createTime;
    /*更新时间*/
    private Date updateTime;
}
