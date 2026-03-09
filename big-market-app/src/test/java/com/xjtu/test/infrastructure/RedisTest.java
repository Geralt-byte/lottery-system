package com.xjtu.test.infrastructure;

import com.xjtu.infrastructure.persistent.redis.IRedisService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RMap;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {
    @Resource
    private IRedisService iRedisService;

    @Test
    public void test(){
        RMap<Object, Object> map = iRedisService.getMap("strategy_id_100001");
        map.put(1,101);
        map.put(2,101);
        map.put(3,101);
        map.put(4,102);
        map.put(5,102);
        map.put(6,102);
        map.put(7,103);
        map.put(8,103);
        map.put(9,103);
        log.info("测试结果{}",iRedisService.getFromMap("strategy_id_100001",9).toString());
    }
}
