package com.xjtu.infrastructure.persistent.dao;

import com.xjtu.infrastructure.persistent.po.StrategyAward;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/*策略奖品表Dao*/

@Mapper
public interface IStrategyAwardDao {

    /*根据策略id查找策略奖品*/
    List<StrategyAward> queryStrategyAwardListByStrategyId(Long strategyId);

    /**根据策略id，奖品id查询策略奖品规则模型值*/
    String queryStrategyAwardRuleModels(StrategyAward strategyAward);

    /**更新奖品库存*/
    void updateStrategyAwardStock(StrategyAward strategyAward);
}
