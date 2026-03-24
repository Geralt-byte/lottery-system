package com.xjtu.test.domain.activity;

import com.xjtu.domain.activity.model.entity.SkuRechargeEntity;
import com.xjtu.domain.activity.service.IRaffleOrder;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class RaffleOrderTest {

    @Resource
    private IRaffleOrder raffleOrder;

    @Test
    public void test_createRaffleActivityOrder() {
        SkuRechargeEntity skuRechargeEntity=SkuRechargeEntity.builder()
                .userId("mlei")
                .sku(9011L)
                .outBusinessNo("700091009111")
                .build();
        String orderId = raffleOrder.createSkuRechargeOrder(skuRechargeEntity);
        log.info("测试结果：{}", orderId);
    }
}
