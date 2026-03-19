package com.xjtu.trigger.job;

import com.xjtu.domain.strategy.model.valobj.StrategyAwardStockKeyVO;
import com.xjtu.domain.strategy.service.IRaffleStock;
import com.xjtu.domain.strategy.service.IRaffleStrategy;
import com.xjtu.domain.strategy.service.armory.IStrategyDispatch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author mlei@xjtu
 * @description UpdateAwardStockJob
 * @create 2026/3/17 17:03
 */
@Slf4j
@Component
public class UpdateAwardStockJob {

    @Resource
    private IRaffleStock iRaffleStock;

    @Scheduled(cron = "0/5 * * * * ?")
    public void exec() {
        try {
//            log.info("定时任务，更新奖品消耗库存【延迟队列获取，降低对数据库的更新频次，不要产生竞争】");
            StrategyAwardStockKeyVO strategyAwardStockKeyVO = iRaffleStock.takeQueueValue();
            if(strategyAwardStockKeyVO==null) return;
            log.info("定时任务，更新奖品消耗库存 strategyId:{} awardId:{}", strategyAwardStockKeyVO.getStrategyId(), strategyAwardStockKeyVO.getAwardId());
            iRaffleStock.updateStrategyAwardStock(strategyAwardStockKeyVO.getStrategyId(), strategyAwardStockKeyVO.getAwardId());

        } catch (Exception e) {
            log.error("定时任务，更新奖品消耗库存失败", e);
        }
    }
}
