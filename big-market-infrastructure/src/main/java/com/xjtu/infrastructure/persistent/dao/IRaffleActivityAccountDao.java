package com.xjtu.infrastructure.persistent.dao;

import cn.bugstack.middleware.db.router.annotation.DBRouter;
import cn.bugstack.middleware.db.router.annotation.DBRouterStrategy;
import com.xjtu.infrastructure.persistent.po.RaffleActivityAccount;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author mlei@xjtu
 * @description IRaffleActivityAccountDao
 * @create 2026/3/23 03:39
 */
@Mapper
public interface IRaffleActivityAccountDao {

    int updateAccountQuota(RaffleActivityAccount raffleActivityAccount);

    void insert(RaffleActivityAccount raffleActivityAccount);
}
