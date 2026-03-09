package com.xjtu.domain.strategy.repository;

import com.xjtu.domain.strategy.model.entity.StrategyAwardEntity;

import java.util.List;
import java.util.Map;

/**策略服务仓储接口*/

public interface IStrategyRepository {

    /**从redis中查找策略id对应的奖品*/
    List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId);

    /**存储概率查找表到reids中*/
    void storeStrategyAwardSearchRateTable(Long strategyId, Integer rateRange, Map<Integer,Integer> strategyAwardSearchRateTable);

    /**根据策略id和随机数从redis中抽取奖品*/
    Integer getStrategyAwardAssemble(Long strategyId,Integer rateKey);

    Integer getRateRange(Long strategyId);
}
