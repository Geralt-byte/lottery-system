package com.xjtu.infrastructure.persistent.dao;

import com.xjtu.infrastructure.persistent.po.DailyBehaviorRebate;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author mlei@xjtu
 * @description IDailyBehaviorRebateDao
 * @create 2026/4/1 08:31
 */
@Mapper
public interface IDailyBehaviorRebateDao {

    List<DailyBehaviorRebate> queryDailyBehaviorRebateConfig(String behaviorType);
}
