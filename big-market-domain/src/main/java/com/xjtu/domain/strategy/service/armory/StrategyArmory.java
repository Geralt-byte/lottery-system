package com.xjtu.domain.strategy.service.armory;

import com.xjtu.domain.strategy.model.entity.StrategyAwardEntity;
import com.xjtu.domain.strategy.repository.IStrategyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.*;

/**
 * 策略装配库实现类，初始化策略计算
 */

@Slf4j
@Service
public class StrategyArmory implements IStrategyArmory {

    /**
     * 策略服务仓储接口注入
     */
    @Resource
    private IStrategyRepository iStrategyRepository;

    /**
     * 初始化抽奖策略配置，触发的时机可以为活动审核通过后进行调用
     */
    @Override
    public boolean assembleLotteryStrategy(Long strategyId) {
        //查询策略奖品配置
        List<StrategyAwardEntity> strategyAwardEntities = iStrategyRepository.queryStrategyAwardList(strategyId);

        //获取最小概率值
        BigDecimal minAwardRate = strategyAwardEntities.stream()
                .map(StrategyAwardEntity::getAwardRate)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

        //获取概率总和
        BigDecimal totalAwardRate = strategyAwardEntities.stream()
                .map(StrategyAwardEntity::getAwardRate)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        //获取概率范围，使用概率总和
        BigDecimal rateRange = totalAwardRate.divide(minAwardRate, 0, RoundingMode.CEILING);

        //生成策略奖品概率查找表，在list集合中存放奖品占位
        List<Integer> strategyAwardSearchRateTables = new ArrayList<>(rateRange.intValue());
        for (StrategyAwardEntity strategyAwardEntity : strategyAwardEntities) {
            Integer awardId = strategyAwardEntity.getAwardId();
            BigDecimal awardRate = strategyAwardEntity.getAwardRate();
            for (int i = 0; i < rateRange
                    .multiply(awardRate.divide(BigDecimal.valueOf(100), 5, RoundingMode.CEILING))
                    .setScale(0, RoundingMode.CEILING).intValue(); i++) {
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
                (strategyId, shuffleStrategyAwardSearchRateTables.size(), shuffleStrategyAwardSearchRateTables);

        return true;
    }

    /**抽奖行为，根据策略id进行抽奖*/
    @Override
    public Integer getRandomAwardId(Long strategyId) {
        //分布式部署下，不一定为当前应用做的策略装配。也就是值不一定会保存到本应用，而是分布式应用，所以需要从 Redis 中获取。
        int rateRange = iStrategyRepository.getRateRange(strategyId);
        // 通过生成的随机值，获取概率值奖品查找表的结果,返回值是奖品id
        return iStrategyRepository.getStrategyAwardAssemble(strategyId, new SecureRandom().nextInt(rateRange));
    }
}
