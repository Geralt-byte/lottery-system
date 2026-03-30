package com.xjtu.domain.strategy.service.armory;

import com.xjtu.domain.strategy.model.entity.StrategyAwardEntity;
import com.xjtu.domain.strategy.model.entity.StrategyEntity;
import com.xjtu.domain.strategy.model.entity.StrategyRuleEntity;
import com.xjtu.domain.strategy.repository.IStrategyRepository;
import com.xjtu.types.common.Constants;
import com.xjtu.types.enums.ResponseCode;
import com.xjtu.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.*;

/**
 * 策略装配库实现类，初始化策略计算
 */

@Slf4j
@Service
public class StrategyArmoryDispatch implements IStrategyArmory,IStrategyDispatch {

    /**
     * 策略服务仓储接口注入
     */
    @Resource
    private IStrategyRepository iStrategyRepository;

    private final SecureRandom secureRandom=new SecureRandom();

    /**
     * 初始化抽奖策略配置，触发的时机可以为活动审核通过后进行调用
     */
    @Override
    public boolean assembleLotteryStrategy(Long strategyId) {
        //1.查询策略奖品配置
        List<StrategyAwardEntity> strategyAwardEntities = iStrategyRepository.queryStrategyAwardList(strategyId);

        //2.缓存奖品库存到redis
        for (StrategyAwardEntity strategyAwardEntity : strategyAwardEntities) {
            strategyAwardCountArmory(strategyId,strategyAwardEntity.getAwardId(),strategyAwardEntity.getAwardCount());
        }

        //3.非权重版本奖品redis初始化
        lotteryStrategyArmory(String.valueOf(strategyId),strategyAwardEntities);

        //4.1权重策略配置
        StrategyEntity strategyEntity = iStrategyRepository.queryStrategyEntityByStrategyId(strategyId);
        String ruleWeight=strategyEntity.getRuleWeight();
        if(ruleWeight==null) return true;

        //4.2根据策略id和对应规则模型去查询策略规则
        StrategyRuleEntity strategyRuleEntity = iStrategyRepository.queryStrategyRuleEntity(strategyId, ruleWeight);
        if(strategyRuleEntity==null){
            throw new AppException(ResponseCode.STRATEGY_RULE_WEIGHT_IS_NULL.getCode(),
                    ResponseCode.STRATEGY_RULE_WEIGHT_IS_NULL.getInfo());
        }
        //4.3获取权重规则值
        Map<String, List<Integer>> ruleWeightMaps = strategyRuleEntity.getRuleWeightValues();
        Set<String> keys = ruleWeightMaps.keySet();
        for (String key : keys) {
            //每条key对应一个权重，获得该权重对应的应抽到的奖品列表
            List<Integer> ruleWeightValues = ruleWeightMaps.get(key);
            //对从redis缓存中获得的策略奖品进行深拷贝
            ArrayList<StrategyAwardEntity> strategyAwardEntitiesClone = new ArrayList<>(strategyAwardEntities);
            //对不存在于奖品列表的奖品进行过滤移除
            strategyAwardEntitiesClone.removeIf(entity -> !ruleWeightValues.contains(entity.getAwardId()));
            //权重版本奖品redis初始化
            lotteryStrategyArmory(String.valueOf(strategyId).concat(Constants.UNDERLINE).concat(key),strategyAwardEntitiesClone);
        }
        return true;
    }

    @Override
    public boolean assembleLotteryStrategyByActivityId(Long activityId) {
        Long strategyId=iStrategyRepository.queryStrategyIdByActivityId(activityId);
        return assembleLotteryStrategy(strategyId);
    }

    /**
     * 实现策略奖品的redis初始化
     */
    private void lotteryStrategyArmory(String key, List<StrategyAwardEntity> strategyAwardEntities){
        //获取最小概率值
        BigDecimal minAwardRate = strategyAwardEntities.stream()
                .map(StrategyAwardEntity::getAwardRate)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

        //将最小的概率扩展10的x次方直到大于1，此时10的x次方可以当作概率范围，可以乘其他更大的概率值都为整数
        BigDecimal rateRange=BigDecimal.valueOf(1L);
        double min = minAwardRate.doubleValue();
        while (min<1){
            min*=10;
            rateRange=rateRange.multiply(BigDecimal.valueOf(10));
        }

        //生成策略奖品概率查找表，在list集合中存放奖品占位
        List<Integer> strategyAwardSearchRateTables = new ArrayList<>(rateRange.intValue());
        for (StrategyAwardEntity strategyAwardEntity : strategyAwardEntities) {
            Integer awardId = strategyAwardEntity.getAwardId();
            BigDecimal awardRate = strategyAwardEntity.getAwardRate();
            for (int i = 0; i < rateRange.multiply(awardRate).intValue(); i++) {
                strategyAwardSearchRateTables.add(awardId);
            }
        }

        //打乱顺序
        Collections.shuffle(strategyAwardSearchRateTables);

        //再放入map集合中，value是奖品id，key是概率索引
        Map<Integer, Integer> shuffleStrategyAwardSearchRateTables = new LinkedHashMap<>();
        for (int i = 0; i < strategyAwardSearchRateTables.size(); i++) {
            shuffleStrategyAwardSearchRateTables.put(i, strategyAwardSearchRateTables.get(i));
        }

        //存储策略奖品查找表到reids中
        iStrategyRepository.storeStrategyAwardSearchRateTable
                (key, shuffleStrategyAwardSearchRateTables.size(), shuffleStrategyAwardSearchRateTables);
    }

    /**缓存奖品库存到redis*/
    private void strategyAwardCountArmory(Long strategyId, Integer awardId, Integer awardCount){
        String cacheKey=Constants.RedisKey.STRATEGY_AWARD_COUNT_KEY+strategyId+Constants.UNDERLINE+awardId;
        iStrategyRepository.cacheStrategyAwardCount(cacheKey,awardCount);
    }

    /**抽奖行为，根据策略id进行抽奖*/
    @Override
    public Integer getRandomAwardId(Long strategyId) {
        //分布式部署下，不一定为当前应用做的策略装配。也就是值不一定会保存到本应用，而是分布式应用，所以需要从 Redis 中获取。
        int rateRange = iStrategyRepository.getRateRange(strategyId);
        // 通过生成的随机值，获取概率值奖品查找表的结果,返回值是奖品id
        return iStrategyRepository.getStrategyAwardAssemble(String.valueOf(strategyId), new SecureRandom().nextInt(rateRange));
    }

    /**抽奖行为，根据策略id和权重进行抽奖*/
    @Override
    public Integer getRandomAwardId(Long strategyId, String ruleWeightValue) {
        String key=String.valueOf(strategyId).concat(Constants.UNDERLINE).concat(ruleWeightValue);
        return getRandomAwardId(key);
    }

    @Override
    public Integer getRandomAwardId(String key) {
        //分布式部署下，不一定为当前应用做的策略装配。也就是值不一定会保存到本应用，而是分布式应用，所以需要从 Redis 中获取。
        int rateRange = iStrategyRepository.getRateRange(key);
        // 通过生成的随机值，获取概率值奖品查找表的结果,返回值是奖品id
        return iStrategyRepository.getStrategyAwardAssemble(key, new SecureRandom().nextInt(rateRange));
    }

    /**
     * 扣减库存操作
     * */
    @Override
    public Boolean subtractionAwardStock(Long strategyId, Integer awardId) {
        String cacheKey=Constants.RedisKey.STRATEGY_AWARD_COUNT_KEY+strategyId+Constants.UNDERLINE+awardId;
        return iStrategyRepository.subtractionAwardStock(cacheKey);
    }
}
