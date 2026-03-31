package com.xjtu.domain.strategy.repository;

import com.xjtu.domain.strategy.model.entity.StrategyAwardEntity;
import com.xjtu.domain.strategy.model.entity.StrategyEntity;
import com.xjtu.domain.strategy.model.entity.StrategyRuleEntity;
import com.xjtu.domain.strategy.model.valobj.RuleTreeVO;
import com.xjtu.domain.strategy.model.valobj.StrategyAwardRuleModelVO;
import com.xjtu.domain.strategy.model.valobj.StrategyAwardStockKeyVO;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**策略服务仓储接口*/

public interface IStrategyRepository {

    /**从redis中查找策略id对应的奖品*/
    List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId);

    /**从redis中查找单个奖品*/
    StrategyAwardEntity queryStrategyEntity(Long strategyId,Integer awardId);

    /**存储概率查找表到reids中*/
    void storeStrategyAwardSearchRateTable(String key, Integer rateRange, Map<Integer,Integer> strategyAwardSearchRateTable);

    /**将奖品的库存缓存到redis中*/
    void cacheStrategyAwardCount(String cacheKey,Integer awardCount);

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

    /**根据树id查询规则树*/
    RuleTreeVO queryRuleTreeVOByTreeId(String treeId);

    /**获取奖品库存消费队列*/
    StrategyAwardStockKeyVO takeQueueValue() throws InterruptedException;

    /**更新奖品库存消耗*/
    void updateStrategyAwardStock(Long strategyId, Integer awardId);

    /**扣减库存操作*/
    Boolean subtractionAwardStock(String cacheKey);

    /**扣减库存操作*/
    Boolean subtractionAwardStock(String cacheKey, Date endDateTime);

    /**写入奖品库存消费队列*/
    void awardStockConsumeSendQueue(StrategyAwardStockKeyVO strategyAwardStockKeyVO);
    /**根据活动id查询策略id*/
    Long queryStrategyIdByActivityId(Long activityId);
    /**查询用户当天的抽奖次数*/
    Integer queryTodayUserRaffleCount(String userId, Long strategyId);
    /**根据规则树ID集合查询奖品中加锁数量的配置「部分奖品需要抽奖N次解锁」*/
    Map<String, Integer> queryAwardRuleLockCount(String[] treeIds);
}
