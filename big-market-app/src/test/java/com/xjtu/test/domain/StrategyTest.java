package com.xjtu.test.domain;

import com.xjtu.domain.strategy.service.armory.IStrategyArmory;
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

    /**生成策略奖品表*/
    @Test
    public void strategyAwardSearchRateTableTest(){
        boolean flag= iStrategyArmory.assembleLotteryStrategy(100001L);
        log.info("生成策略100001L的奖品配置表:{}",flag);
    }

    /**抽奖测试*/
    @Test
    public void getRandomAwardIdTest(){
        Integer randomAwardId1 = iStrategyArmory.getRandomAwardId(100001L);
        log.info("抽奖1:{}",randomAwardId1);
        Integer randomAwardId2 = iStrategyArmory.getRandomAwardId(100001L);
        log.info("抽奖2:{}",randomAwardId2);
        Integer randomAwardId3 = iStrategyArmory.getRandomAwardId(100001L);
        log.info("抽奖3:{}",randomAwardId3);
    }

    /**抽奖测试1*/
    @Test
    public void getRandomAwardIdTest1(){
        Map<Integer,Integer> maps=new HashMap<>();
        for (int i = 0; i < 1000; i++) {
            Integer randomAwardId = iStrategyArmory.getRandomAwardId(100001L);
            maps.put(randomAwardId,maps.getOrDefault(randomAwardId,0)+1);
        }
        Set<Map.Entry<Integer, Integer>> entries = maps.entrySet();
        for (Map.Entry<Integer, Integer> entry : entries) {
            log.info("奖品id:{},中将次数:{}",entry.getKey(),entry.getValue());
        }
    }
}
