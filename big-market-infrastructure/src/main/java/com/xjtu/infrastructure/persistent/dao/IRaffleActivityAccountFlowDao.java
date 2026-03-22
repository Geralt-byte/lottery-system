package com.xjtu.infrastructure.persistent.dao;

import cn.bugstack.middleware.db.router.annotation.DBRouter;
import cn.bugstack.middleware.db.router.annotation.DBRouterStrategy;
import com.xjtu.infrastructure.persistent.po.RaffleActivityAccountFlow;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author mlei@xjtu
 * @description IRaffleActivityAccountFlowDao
 * @create 2026/3/23 03:40
 */
@Mapper
@DBRouterStrategy(splitTable = true)
public interface IRaffleActivityAccountFlowDao {

    @DBRouter(key = "userId")
    void insert(RaffleActivityAccountFlow raffleActivityAccountFlow);

    @DBRouter
    List<RaffleActivityAccountFlow> queryRaffleActivityAccountFlowByUserId(String userId);
}
