package com.xjtu.trigger.job;

import com.xjtu.domain.activity.model.valobj.ActivitySkuStockKeyVO;
import com.xjtu.domain.activity.service.IRaffleActivitySkuStockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author mlei@xjtu
 * @description 更新活动sku库存任务
 * @create 2026/3/26 04:57
 */
@Slf4j
@Component
public class UpdateActivitySkuStockJob {

    @Resource
    private IRaffleActivitySkuStockService iRaffleActivitySkuStockService;

    @Scheduled(cron = "0/5 * * * * ?")
    public void exec() {
        try {
            log.info("定时任务，更新活动sku库存【延迟队列获取，降低对数据库的更新频次，不要产生竞争】");
            ActivitySkuStockKeyVO activitySkuStockKeyVO = iRaffleActivitySkuStockService.takeQueueValue();
            if(activitySkuStockKeyVO==null) return;
            log.info("定时任务，更新活动sku库存 sku:{} activityId:{}", activitySkuStockKeyVO.getSku(), activitySkuStockKeyVO.getActivityId());
            iRaffleActivitySkuStockService.updateActivitySkuStock(activitySkuStockKeyVO.getSku());
        } catch (Exception e) {
            log.error("定时任务，更新活动sku库存失败", e);
        }
    }
}
