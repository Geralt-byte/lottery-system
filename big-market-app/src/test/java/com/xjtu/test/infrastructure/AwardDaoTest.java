package com.xjtu.test.infrastructure;

import com.alibaba.fastjson.JSON;
import com.xjtu.infrastructure.persistent.dao.IAwardDao;
import com.xjtu.infrastructure.persistent.po.Award;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class AwardDaoTest {

    @Resource
    private IAwardDao iAwardDao;

    @Test
    public void awardDaoTest() {
        List<Award> awards = iAwardDao.queryAll();
        log.info("测试结果:{}", JSON.toJSONString(awards));
    }
}
