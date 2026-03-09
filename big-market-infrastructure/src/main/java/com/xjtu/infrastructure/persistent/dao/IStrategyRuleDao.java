package com.xjtu.infrastructure.persistent.dao;

import com.xjtu.infrastructure.persistent.po.StrategyRule;
import org.apache.ibatis.annotations.Mapper;

/*策略规则表Dao*/

@Mapper
public interface IStrategyRuleDao {
    /**根据策略id和规则模型查找策略规则*/
    StrategyRule queryStrategyRule(Long strategyId, String ruleModel);
}
