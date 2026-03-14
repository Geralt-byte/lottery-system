package com.xjtu.domain.strategy.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author mlei@xjtu
 * @description 规则树节点值对象
 * @create 2026/3/14 23:29
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RuleTreeNodeVO {
    /** 规则树Id*/
    private Integer treeId;
    /** 规则树Key*/
    private String ruleKey;
    /** 规则树描述*/
    private String ruleDesc;
    /** 规则比值*/
    private String ruleValue;
    /** 规则连线*/
    private List<RuleTreeNodeLineVO> treeNodeLineVOList;
}
