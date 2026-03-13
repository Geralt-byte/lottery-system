package com.xjtu.test.domain;

import com.xjtu.domain.strategy.service.armory.IStrategyArmory;
import com.xjtu.domain.strategy.service.armory.IStrategyDispatch;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author mlei@xjtu
 * @description 策略领域测试
 * @create 2026/3/9 18:27
 */

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class StrategyTest {

    @Resource
    private IStrategyArmory iStrategyArmory;

    @Resource
    private IStrategyDispatch iStrategyDispatch;

    /**生成策略奖品表*/
    @Test
    public void strategyAwardSearchRateTableTest(){
        boolean flag1= iStrategyArmory.assembleLotteryStrategy(100001L);
        boolean flag2= iStrategyArmory.assembleLotteryStrategy(100002L);
        boolean flag3= iStrategyArmory.assembleLotteryStrategy(100003L);
        log.info("生成策略100001L的奖品配置表:{}",flag1);
        log.info("生成策略100002L的奖品配置表:{}",flag2);
        log.info("生成策略100003L的奖品配置表:{}",flag3);
    }

    /**无权重抽奖测试*/
    @Test
    public void getRandomAwardIdTest(){
        Integer randomAwardId1 = iStrategyDispatch.getRandomAwardId(100001L);
        log.info("抽奖1:{}",randomAwardId1);
        Integer randomAwardId2 = iStrategyDispatch.getRandomAwardId(100001L);
        log.info("抽奖2:{}",randomAwardId2);
        Integer randomAwardId3 = iStrategyDispatch.getRandomAwardId(100001L);
        log.info("抽奖3:{}",randomAwardId3);
    }

    /**无权重抽奖测试抽1000次*/
    @Test
    public void getRandomAwardIdTest1(){
        Map<Integer,Integer> maps=new HashMap<>();
        for (int i = 0; i < 1000; i++) {
            Integer randomAwardId = iStrategyDispatch.getRandomAwardId(100001L);
            maps.put(randomAwardId,maps.getOrDefault(randomAwardId,0)+1);
        }
        Set<Map.Entry<Integer, Integer>> entries = maps.entrySet();
        for (Map.Entry<Integer, Integer> entry : entries) {
            log.info("奖品id:{},中将次数:{}",entry.getKey(),entry.getValue());
        }
    }

    /**权重抽奖测试抽100次*/
    @Test
    public void getWeightRandomAwardIdTest(){
        log.info("4000积分抽奖:");
        Map<Integer,Integer> maps1=new HashMap<>();
        for (int i = 0; i < 100; i++) {
            Integer randomAwardId = iStrategyDispatch.getRandomAwardId(100001L,"4000:102,103,104,105");
            maps1.put(randomAwardId,maps1.getOrDefault(randomAwardId,0)+1);
        }
        Set<Map.Entry<Integer, Integer>> entries1 = maps1.entrySet();
        for (Map.Entry<Integer, Integer> entry : entries1) {
            log.info("奖品id:{},中将次数:{}",entry.getKey(),entry.getValue());
        }

        log.info("5000积分抽奖:");
        Map<Integer,Integer> maps2=new HashMap<>();
        for (int i = 0; i < 100; i++) {
            Integer randomAwardId = iStrategyDispatch.getRandomAwardId(100001L,"5000:102,103,104,105,106,107");
            maps2.put(randomAwardId,maps2.getOrDefault(randomAwardId,0)+1);
        }
        Set<Map.Entry<Integer, Integer>> entries2 = maps2.entrySet();
        for (Map.Entry<Integer, Integer> entry : entries2) {
            log.info("奖品id:{},中将次数:{}",entry.getKey(),entry.getValue());
        }

        log.info("6000积分抽奖:");
        Map<Integer,Integer> maps3=new HashMap<>();
        for (int i = 0; i < 100; i++) {
            Integer randomAwardId = iStrategyDispatch.getRandomAwardId(100001L,"6000:102,103,104,105,106,107,108,109");
            maps3.put(randomAwardId,maps3.getOrDefault(randomAwardId,0)+1);
        }
        Set<Map.Entry<Integer, Integer>> entries3 = maps3.entrySet();
        for (Map.Entry<Integer, Integer> entry : entries3) {
            log.info("奖品id:{},中将次数:{}",entry.getKey(),entry.getValue());
        }
    }
}
