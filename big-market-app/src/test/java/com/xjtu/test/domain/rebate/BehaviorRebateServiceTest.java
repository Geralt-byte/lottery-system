package com.xjtu.test.domain.rebate;

import com.alibaba.fastjson.JSON;
import com.xjtu.domain.rebate.model.entity.BehaviorEntity;
import com.xjtu.domain.rebate.model.valobj.BehaviorTypeVO;
import com.xjtu.domain.rebate.service.IBehaviorRebateService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author mlei@xjtu
 * @description BehaviorRebateServiceTest
 * @create 2026/4/1 10:21
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class BehaviorRebateServiceTest {

    @Resource
    private IBehaviorRebateService iBehaviorRebateService;

    @Test
    public void test() throws InterruptedException {
        BehaviorEntity behaviorEntity = new BehaviorEntity();
        behaviorEntity.setUserId("mlei");
        behaviorEntity.setBehaviorTypeVO(BehaviorTypeVO.SIGN);
        // 重复的 OutBusinessNo 会报错唯一索引冲突，这也是保证幂等的手段，确保不会多记账
        behaviorEntity.setOutBusinessNo("20260401");
        List<String> order = iBehaviorRebateService.createOrder(behaviorEntity);
        log.info("请求参数: {}", JSON.toJSONString(behaviorEntity));
        log.info("测试结果: {}", JSON.toJSONString(order));
        new CountDownLatch(1).await();
    }
}
