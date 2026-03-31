package com.xjtu.test.infrastructure;

import com.alibaba.fastjson.JSON;
import com.xjtu.infrastructure.persistent.dao.IRuleTreeNodeDao;
import com.xjtu.infrastructure.persistent.po.RuleTreeNode;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author mlei@xjtu
 * @description RuleTreeNodeDaoTest
 * @create 2026/3/31 07:34
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class RuleTreeNodeDaoTest {

    @Resource
    private IRuleTreeNodeDao ruleTreeNodeDao;

    @Test
    public void test_queryRuleLocks() {
        List<RuleTreeNode> ruleTreeNodes = ruleTreeNodeDao.queryRuleLocks(new String[]{"tree_lock_1", "tree_lock_2","tree_luck_award"});
        log.info("测试结果:{}", JSON.toJSONString(ruleTreeNodes));
    }
}
