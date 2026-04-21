package com.xjtu.domain.award.service.distribute;

import com.xjtu.domain.award.model.entity.DistributeAwardEntity;

/**
 * @author mlei@xjtu
 * @description 分发奖品接口
 * @create 2026/4/21 23:08
 */
public interface IDistributeAward {

    /**
     * @param distributeAwardEntity 分发奖品实体
     */
    void getOutPrizes(DistributeAwardEntity distributeAwardEntity) throws Exception;
}
