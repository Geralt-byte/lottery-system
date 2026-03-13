package com.xjtu.domain.strategy.repository;

import com.xjtu.domain.strategy.model.entity.StrategyAwardEntity;
import com.xjtu.domain.strategy.model.entity.StrategyEntity;
import com.xjtu.domain.strategy.model.entity.StrategyRuleEntity;
import com.xjtu.domain.strategy.model.valobj.StrategyAwardRuleModelVO;

import java.util.List;
import java.util.Map;

/**策略服务仓储接口*/

public interface IStrategyRepository {

    /**从redis中查找策略id对应的奖品*/
    List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId);

    /**存储概率查找表到reids中*/
    void storeStrategyAwardSearchRateTable(String key, Integer rateRange, Map<Integer,Integer> strategyAwardSearchRateTable);

    /**根据策略id和随机数从redis中抽取奖品*/
    Integer getStrategyAwardAssemble(String key,Integer rateKey);

    Integer getRateRange(Long strategyId);

    Integer getRateRange(String key);

    /**根据策略id查询策略实体*/
    StrategyEntity queryStrategyEntityByStrategyId(Long strategyId);

    /**根据策略id和规则模型查询策略规则*/
    StrategyRuleEntity queryStrategyRuleEntity(Long strategyId, String ruleModel);

    /**根据策略id,规则模型,奖品id查询规则值*/
    String queryStrategyRuleValueEntity(Long strategyId, String ruleModel);
    String queryStrategyRuleValueEntity(Long strategyId, String ruleModel,Integer awardId);

    /**根据策略id，奖品id查询策略奖品规则模型值*/
    StrategyAwardRuleModelVO queryStrategyAwardRuleModelVO(Long strategyId, Integer awardId);
}
