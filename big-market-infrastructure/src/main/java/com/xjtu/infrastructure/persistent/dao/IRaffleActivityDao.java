package com.xjtu.infrastructure.persistent.dao;

import com.xjtu.infrastructure.persistent.po.RaffleActivity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author mlei@xjtu
 * @description IRaffleActivityDao
 * @create 2026/3/23 03:38
 */
@Mapper
public interface IRaffleActivityDao {

    RaffleActivity queryRaffleActivityByActivityId(Long activityId);

    Long queryStrategyIdByActivityId(Long activityId);

    Long queryActivityIdByStrategyId(Long strategyId);
}
