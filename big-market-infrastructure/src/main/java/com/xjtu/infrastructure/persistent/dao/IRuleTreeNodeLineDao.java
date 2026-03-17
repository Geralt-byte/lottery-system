package com.xjtu.infrastructure.persistent.dao;


import com.xjtu.infrastructure.persistent.po.RuleTreeNodeLine;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author mlei@xjtu
 * @description IRuleTreeNodeLineDao
 * @create 2026/3/16 00:14
 */
@Mapper
public interface IRuleTreeNodeLineDao {
    /**根据树id查询规则树边*/
    List<RuleTreeNodeLine> queryRuleTreeNodeLineListByTreeId(String treeId);
}
