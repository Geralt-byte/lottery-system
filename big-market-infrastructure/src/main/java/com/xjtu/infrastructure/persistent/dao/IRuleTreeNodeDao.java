package com.xjtu.infrastructure.persistent.dao;

import com.xjtu.infrastructure.persistent.po.RuleTreeNode;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author mlei@xjtu
 * @description IRuleTreeNodeDao
 * @create 2026/3/16 00:12
 */
@Mapper
public interface IRuleTreeNodeDao {
    /**根据树id查询规则树节点*/
    List<RuleTreeNode> queryRuleTreeNodeListByTreeId(String treeId);

    List<RuleTreeNode> queryRuleLocks(String[] treeIds);
}
