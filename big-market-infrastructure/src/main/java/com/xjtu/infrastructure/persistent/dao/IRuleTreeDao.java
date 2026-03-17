package com.xjtu.infrastructure.persistent.dao;

import com.xjtu.domain.strategy.model.valobj.RuleTreeVO;
import com.xjtu.infrastructure.persistent.po.RuleTree;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author mlei@xjtu
 * @description IRuleTreeDao
 * @create 2026/3/16 00:03
 */
@Mapper
public interface IRuleTreeDao {
    /**根据树id查询规则树*/
    RuleTree queryRuleTreeByTreeId(String treeId);
}
