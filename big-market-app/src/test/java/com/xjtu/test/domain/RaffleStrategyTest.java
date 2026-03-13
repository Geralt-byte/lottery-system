package com.xjtu.test.domain;

import com.alibaba.fastjson2.JSON;
import com.xjtu.domain.strategy.model.entity.RaffleAwardEntity;
import com.xjtu.domain.strategy.model.entity.RaffleFactorEntity;
import com.xjtu.domain.strategy.service.IRaffleStrategy;
import com.xjtu.domain.strategy.service.rule.chain.impl.RuleWeightLogicChain;
import com.xjtu.domain.strategy.service.rule.filter.impl.RuleLockLogicFilter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author mlei@xjtu
 * @description 抽奖策略测试
 * @create 2026/3/12 00:43
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class RaffleStrategyTest {

    @Resource
    private IRaffleStrategy raffleStrategy;

    @Resource
    private RuleWeightLogicChain ruleWeightLogicChain;

    @Resource
    private RuleLockLogicFilter ruleLockLogicFilter;

    @Before
    public void set(){
        ReflectionTestUtils.setField(ruleWeightLogicChain,"userScore",5500L);
        ReflectionTestUtils.setField(ruleLockLogicFilter,"userRaffleCount",0L);
    }

    /*权重测试*/
    @Test
    public void performRaffleTest(){
        RaffleFactorEntity raffleFactorEntity = RaffleFactorEntity.builder()
                .userId("mlei")
                .strategyId(100001L)
                .build();
        RaffleAwardEntity raffleAwardEntity = raffleStrategy.performRaffle(raffleFactorEntity);

        log.info("请求参数: {}", JSON.toJSONString(raffleFactorEntity));
        log.info("测试结果: {}", JSON.toJSONString(raffleAwardEntity));
    }

    /*权重测试抽100次*/
    @Test
    public void performRaffleTest1(){
        RaffleFactorEntity raffleFactorEntity = RaffleFactorEntity.builder()
                .userId("mlei")
                .strategyId(100001L)
                .build();
        Map<Integer,Integer> map=new HashMap<>();
        for (int i = 0; i < 100; i++) {
            RaffleAwardEntity raffleAwardEntity = raffleStrategy.performRaffle(raffleFactorEntity);
            Integer awardId = raffleAwardEntity.getAwardId();
            map.put(awardId,map.getOrDefault(awardId,0)+1);
        }
        Set<Integer> keys = map.keySet();
        for (Integer key : keys) {
            log.info("奖品id: {} 中将次数: {}",key,map.get(key));
        }
    }

    /*黑名单测试*/
    @Test
    public void performRaffleTest2(){
        RaffleFactorEntity raffleFactorEntity = RaffleFactorEntity.builder()
                .userId("user003")
                .strategyId(100001L)
                .build();

        RaffleAwardEntity raffleAwardEntity = raffleStrategy.performRaffle(raffleFactorEntity);

        log.info("请求参数: {}", JSON.toJSONString(raffleFactorEntity));
        log.info("测试结果: {}", JSON.toJSONString(raffleAwardEntity));
    }

    /*抽奖次数限制测试*/
    @Test
    public void performRaffleTest3(){
        RaffleFactorEntity raffleFactorEntity = RaffleFactorEntity.builder()
                .userId("mlei")
                .strategyId(100003L)
                .build();

        RaffleAwardEntity raffleAwardEntity = raffleStrategy.performRaffle(raffleFactorEntity);

        log.info("请求参数: {}", JSON.toJSONString(raffleFactorEntity));
        log.info("测试结果: {}", JSON.toJSONString(raffleAwardEntity));
    }
}
