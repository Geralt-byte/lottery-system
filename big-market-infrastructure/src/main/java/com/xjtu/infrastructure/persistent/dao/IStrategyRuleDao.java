package com.xjtu.infrastructure.persistent.dao;

import com.xjtu.infrastructure.persistent.po.StrategyRule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/*策略规则表Dao*/

@Mapper
public interface IStrategyRuleDao {
    /**根据策略id和规则模型查找策略规则*/
    StrategyRule queryStrategyRule(@Param("strategyId") Long strategyId, @Param("ruleModel") String ruleModel);

    /**根据策略id,规则模型,奖品id查询规则值*/
    String queryStrategyRuleValue(StrategyRule strategyRule);
}
