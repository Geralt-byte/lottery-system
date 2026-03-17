package com.xjtu.domain.strategy.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author mlei@xjtu
 * @description 规则树值对象【不具有唯一ID，不需要改变数据库结果的对象，可以被定义为值对象】
 * @create 2026/3/14 23:25
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RuleTreeVO {
    /** 规则树Id*/
    private String treeId;
    /** 规则树名称*/
    private String treeName;
    /** 规则树描述*/
    private String treeDesc;
    /** 规则树根节点*/
    private String treeRootRuleNode;

    /** 规则链*/
    private Map<String,RuleTreeNodeVO> treeNodeMap;
}
