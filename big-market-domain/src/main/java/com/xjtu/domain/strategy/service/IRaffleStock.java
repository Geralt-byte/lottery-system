package com.xjtu.domain.strategy.service;

import com.xjtu.domain.strategy.model.valobj.StrategyAwardStockKeyVO;

/**
 * @author mlei@xjtu
 * @description 抽奖库存接口
 * @create 2026/3/17 15:18
 */
public interface IRaffleStock {

    /**
     * 获取奖品库存消耗队列
     *
     * @return 奖品库存Key信息
     * @throws InterruptedException 异常
     */
    StrategyAwardStockKeyVO takeQueueValue();

    /**
     * 更新奖品库存消耗记录
     *
     * @param strategyId 策略ID
     * @param awardId    奖品ID
     */
    void updateStrategyAwardStock(Long strategyId,Integer awardId);
}
