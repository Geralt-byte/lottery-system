package com.xjtu.test.domain.activity;

import com.alibaba.fastjson.JSON;
import com.xjtu.domain.activity.model.entity.PartakeRaffleActivityEntity;
import com.xjtu.domain.activity.model.entity.UserRaffleOrderEntity;
import com.xjtu.domain.activity.service.IRaffleActivityPartakeService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author mlei@xjtu
 * @description 抽奖活动订单单测
 * @create 2026/3/29 23:08
 */
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class RaffleActivityPartakeServiceTest {

    @Resource
    private IRaffleActivityPartakeService iRaffleActivityPartakeService;

    @Test
    public void test_createOrder(){
        // 请求参数
        PartakeRaffleActivityEntity partakeRaffleActivityEntity = new PartakeRaffleActivityEntity();
        partakeRaffleActivityEntity.setUserId("mlei");
        partakeRaffleActivityEntity.setActivityId(100301L);
        // 调用接口
        UserRaffleOrderEntity userRaffleOrder = iRaffleActivityPartakeService.createOrder(partakeRaffleActivityEntity);
        log.info("请求参数：{}", JSON.toJSONString(partakeRaffleActivityEntity));
        log.info("测试结果：{}", JSON.toJSONString(userRaffleOrder));
    }
}
