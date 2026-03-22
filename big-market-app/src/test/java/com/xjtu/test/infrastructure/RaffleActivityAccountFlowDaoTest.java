package com.xjtu.test.infrastructure;

import com.alibaba.fastjson.JSON;
import com.xjtu.infrastructure.persistent.dao.IRaffleActivityAccountFlowDao;
import com.xjtu.infrastructure.persistent.po.RaffleActivityAccountFlow;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.jeasy.random.EasyRandom;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author mlei@xjtu
 * @description RaffleActivityAccountFlowDaoTest
 * @create 2026/3/23 04:11
 */
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class RaffleActivityAccountFlowDaoTest {

    @Resource
    private IRaffleActivityAccountFlowDao iRaffleActivityAccountFlowDao;

    private final EasyRandom easyRandom = new EasyRandom();

    @Test
    public void test_insert_random() {
        for (int i = 0; i < 100; i++) {
            RaffleActivityAccountFlow raffleActivityAccountFlow = new RaffleActivityAccountFlow();
            // EasyRandom 可以通过指定对象类的方式，随机生成对象值。如；easyRandom.nextObject(String.class)、easyRandom.nextObject(RaffleActivityOrder.class)
            raffleActivityAccountFlow.setUserId(easyRandom.nextObject(String.class));
            raffleActivityAccountFlow.setActivityId(100301L);
            raffleActivityAccountFlow.setTotalCount(easyRandom.nextObject(Integer.class));
            raffleActivityAccountFlow.setDayCount(easyRandom.nextObject(Integer.class));
            raffleActivityAccountFlow.setMonthCount(easyRandom.nextObject(Integer.class));
            raffleActivityAccountFlow.setFlowId(RandomStringUtils.randomNumeric(32));
            raffleActivityAccountFlow.setFlowChannel("activity");
            raffleActivityAccountFlow.setBizId(RandomStringUtils.randomNumeric(12));
            // 插入数据
            iRaffleActivityAccountFlowDao.insert(raffleActivityAccountFlow);
        }
    }

    @Test
    public void test_insert() {

        RaffleActivityAccountFlow raffleActivityAccountFlow = new RaffleActivityAccountFlow();
        raffleActivityAccountFlow.setUserId("mlei");
        raffleActivityAccountFlow.setActivityId(100301L);
        raffleActivityAccountFlow.setTotalCount(500);
        raffleActivityAccountFlow.setDayCount(100);
        raffleActivityAccountFlow.setMonthCount(20);
        raffleActivityAccountFlow.setFlowId(RandomStringUtils.randomNumeric(32));
        raffleActivityAccountFlow.setFlowChannel("activity");
        raffleActivityAccountFlow.setBizId(RandomStringUtils.randomNumeric(12));

        iRaffleActivityAccountFlowDao.insert(raffleActivityAccountFlow);
    }

    @Test
    public void test_queryRaffleActivityOrderByUserId() {
        String userId = "mlei";
        List<RaffleActivityAccountFlow> raffleActivityAccountFlows = iRaffleActivityAccountFlowDao.queryRaffleActivityAccountFlowByUserId(userId);
        log.info("测试结果：{}", JSON.toJSONString(raffleActivityAccountFlows));
    }
}
