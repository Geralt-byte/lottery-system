package com.xjtu.domain.strategy.service;

import com.xjtu.domain.strategy.model.entity.RaffleAwardEntity;
import com.xjtu.domain.strategy.model.entity.RaffleFactorEntity;

/**
 * @author mlei@xjtu
 * @description 抽奖策略接口
 * @create 2026/3/11 21:34
 */
public interface IRaffleStrategy {

    /**根据抽奖因子实体(用户id和策略Id)，返回抽奖奖品实体*/
    RaffleAwardEntity performRaffle(RaffleFactorEntity raffleFactorEntity);
}
