package com.xjtu.domain.strategy.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mlei@xjtu
 * @description 规则树节点指向线对象。用于衔接 from->to 节点链路关系
 * @create 2026/3/14 23:32
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RuleTreeNodeLineVO {
    /** 规则树Id*/
    private Integer treeId;
    /** 入边*/
    private String ruleNodeFrom;
    /** 出边*/
    private String ruleNodeTo;
    /** 枚举类型*/
    private RuleLimitTypeVO ruleLimitType;
    /** 限定值*/
    private RuleLogicCheckTypeVO ruleLimitValue;
}
