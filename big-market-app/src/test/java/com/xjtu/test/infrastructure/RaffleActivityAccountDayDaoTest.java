package com.xjtu.test.infrastructure;

import com.xjtu.infrastructure.persistent.dao.IRaffleActivityAccountDayDao;
import com.xjtu.infrastructure.persistent.po.RaffleActivityAccountDay;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author mlei@xjtu
 * @description 活动日账户DAO
 * @create 2026/3/31 07:32
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class RaffleActivityAccountDayDaoTest {

    @Resource
    private IRaffleActivityAccountDayDao raffleActivityAccountDayDao;

    @Test
    public void test_queryRaffleActivityAccountDayPartakeCount() {
        RaffleActivityAccountDay raffleActivityAccountDay = new RaffleActivityAccountDay();
        raffleActivityAccountDay.setActivityId(100301L);
        raffleActivityAccountDay.setUserId("mlei");
        raffleActivityAccountDay.setDay(RaffleActivityAccountDay.currentDay());
        Integer dayPartakeCount = raffleActivityAccountDayDao.queryRaffleActivityAccountDayPartakeCount(raffleActivityAccountDay);
        log.info("测试结果:{}", dayPartakeCount);
    }
}
