package com.xjtu.infrastructure.persistent.repository;

import com.xjtu.domain.strategy.model.entity.StrategyAwardEntity;
import com.xjtu.domain.strategy.repository.IStrategyRepository;
import com.xjtu.infrastructure.persistent.dao.IStrategyAwardDao;
import com.xjtu.infrastructure.persistent.po.StrategyAward;
import com.xjtu.infrastructure.persistent.redis.IRedisService;
import com.xjtu.types.common.Constants;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**策略服务仓储实现*/

@Repository
public class StrategyRepository implements IStrategyRepository {

    /**mybatis层接口注入*/
    @Resource
    private IStrategyAwardDao iStrategyAwardDao;

    /**Redisson层接口注入*/
    @Resource
    private IRedisService iRedisService;

    /**从redis中查找策略id对应的奖品*/
    @Override
    public List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId) {
        //先查redis缓存
        String cacheKey= Constants.RedisKey.STRATEGY_AWARD_KEY+strategyId;
        List<StrategyAwardEntity> strategyAwardEntities = iRedisService.getValue(cacheKey);
        if(strategyAwardEntities!=null&&!strategyAwardEntities.isEmpty()) return strategyAwardEntities;

        //redis缓存为空，查询数据库
        List<StrategyAward> strategyAwards = iStrategyAwardDao.queryStrategyAwardListByStrategyId(strategyId);
        strategyAwardEntities=new ArrayList<>(strategyAwards.size());
        for (StrategyAward strategyAward : strategyAwards) {
            StrategyAwardEntity strategyAwardEntity=StrategyAwardEntity.builder()
                    .strategyId(strategyAward.getStrategyId())
                    .awardId(strategyAward.getAwardId())
                    .awardCount(strategyAward.getAwardCount())
                    .AwardCountSurplus(strategyAward.getAwardCountSurplus())
                    .awardRate(strategyAward.getAwardRate())
                    .build();
            strategyAwardEntities.add(strategyAwardEntity);
        }
        //存储策略奖品到redis中
        iRedisService.setValue(cacheKey,strategyAwardEntities);
        return strategyAwardEntities;
    }

    /**存储概率查找表到reids中*/
    @Override
    public void storeStrategyAwardSearchRateTable(Long strategyId, Integer rateRange, Map<Integer, Integer> strategyAwardSearchRateTable) {
        //将rateRange(本次抽奖范围值)存储到redis中，概率范围存储,分布式应用
        iRedisService.setValue(Constants.RedisKey.STRATEGY_RATE_RANGE_KEY+strategyId,rateRange);

        //存储概率查找表到redis中
        Map<Integer,Integer> cacheRateTable=iRedisService.getMap(Constants.RedisKey.STRATEGY_RATE_TABLE_KEY+strategyId);
        cacheRateTable.putAll(strategyAwardSearchRateTable);
    }

    /**根据策略id和随机数从redis中抽取奖品,返回值为奖品Id*/
    @Override
    public Integer getStrategyAwardAssemble(Long strategyId, Integer rateKey) {
        //根据概率值从redis中抽取唯一存在的一个奖品
        return iRedisService.getFromMap(Constants.RedisKey.STRATEGY_RATE_TABLE_KEY+strategyId,rateKey);
    }

    @Override
    public Integer getRateRange(Long strategyId) {
        //概率范围获取
        return iRedisService.getValue(Constants.RedisKey.STRATEGY_RATE_RANGE_KEY+strategyId);
    }
}
