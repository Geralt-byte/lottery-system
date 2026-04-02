package com.xjtu.test.infrastructure;

import com.xjtu.domain.strategy.model.valobj.StrategyAwardStockKeyVO;
import com.xjtu.domain.strategy.repository.IStrategyRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.concurrent.CountDownLatch;

/**
 * @author mlei@xjtu
 * @description AwardStockConsumeSendQueueTest
 * @create 2026/4/2 17:24
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class AwardStockConsumeSendQueueTest {

    @Resource
    private IStrategyRepository iStrategyRepository;

    @Test
    public void awardStockConsumeSendQueueTest() throws InterruptedException {
        StrategyAwardStockKeyVO strategyAwardStockKeyVO = new StrategyAwardStockKeyVO();
        strategyAwardStockKeyVO.setStrategyId(100006L);
        strategyAwardStockKeyVO.setAwardId(105);
        iStrategyRepository.awardStockConsumeSendQueue(strategyAwardStockKeyVO);
        new CountDownLatch(1).await();
    }
}
