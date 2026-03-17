package com.xjtu.infrastructure.persistent.po;

import lombok.Data;

import java.util.Date;

/**
 * @author mlei@xjtu
 * @description 规则树
 * @create 2026/3/15 22:43
 */
@Data
public class RuleTree {
    /*自增ID*/
    private Long id;
    /*规则树ID*/
    private String treeId;
    /*规则树名称*/
    private String treeName;
    /*规则树描述*/
    private String treeDesc;
    /*规则树根节点规则*/
    private String treeNodeRuleKey;
    /*创建时间*/
    private Date createTime;
    /*更新时间*/
    private Date updateTime;
}
