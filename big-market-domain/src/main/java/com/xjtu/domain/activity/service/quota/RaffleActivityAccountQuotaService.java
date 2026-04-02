package com.xjtu.domain.activity.service.quota;

import com.xjtu.domain.activity.model.aggregate.CreateQuotaOrderAggregate;
import com.xjtu.domain.activity.model.entity.*;
import com.xjtu.domain.activity.model.valobj.ActivitySkuStockKeyVO;
import com.xjtu.domain.activity.model.valobj.OrderStateVO;
import com.xjtu.domain.activity.repository.IActivityRepository;
import com.xjtu.domain.activity.service.IRaffleActivitySkuStockService;
import com.xjtu.domain.activity.service.quota.rule.factory.DefaultActivityChainFactory;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author mlei@xjtu
 * @description 抽奖活动服务
 * @create 2026/3/24 04:35
 */
@Service
public class RaffleActivityAccountQuotaService extends AbstractRaffleActivityAccountQuota implements IRaffleActivitySkuStockService {

    public RaffleActivityAccountQuotaService(DefaultActivityChainFactory defaultActivityChainFactory, IActivityRepository iActivityRepository) {
        super(defaultActivityChainFactory, iActivityRepository);
    }

    @Override
    protected CreateQuotaOrderAggregate buildOrderAggregate(SkuRechargeEntity skuRechargeEntity, ActivitySkuEntity activitySkuEntity, ActivityEntity activityEntity, ActivityCountEntity activityCountEntity) {
        // 订单实体对象
        ActivityOrderEntity activityOrderEntity = new ActivityOrderEntity();
        activityOrderEntity.setUserId(skuRechargeEntity.getUserId());
        activityOrderEntity.setSku(skuRechargeEntity.getSku());
        activityOrderEntity.setActivityId(activityEntity.getActivityId());
        activityOrderEntity.setActivityName(activityEntity.getActivityName());
        activityOrderEntity.setStrategyId(activityEntity.getStrategyId());
        activityOrderEntity.setOrderId(RandomStringUtils.randomNumeric(12));
        activityOrderEntity.setOrderTime(new Date());
        activityOrderEntity.setTotalCount(activityCountEntity.getTotalCount());
        activityOrderEntity.setDayCount(activityCountEntity.getDayCount());
        activityOrderEntity.setMonthCount(activityCountEntity.getMonthCount());
        activityOrderEntity.setState(OrderStateVO.completed);
        activityOrderEntity.setOutBusinessNo(skuRechargeEntity.getOutBusinessNo());

        // 构建聚合对象
        return CreateQuotaOrderAggregate.builder()
                .userId(skuRechargeEntity.getUserId())
                .activityId(activitySkuEntity.getActivityId())
                .totalCount(activityCountEntity.getTotalCount())
                .dayCount(activityCountEntity.getDayCount())
                .monthCount(activityCountEntity.getMonthCount())
                .activityOrderEntity(activityOrderEntity)
                .build();
    }

    @Override
    protected void doSaveOrder(CreateQuotaOrderAggregate createQuotaOrderAggregate) {
        iActivityRepository.doSaveOrder(createQuotaOrderAggregate);
    }

    @Override
    public ActivitySkuStockKeyVO takeQueueValue() throws InterruptedException {
        return iActivityRepository.takeQueueValue();
    }

    @Override
    public void clearQueueValue() {
        iActivityRepository.clearQueueValue();
    }

    @Override
    public void updateActivitySkuStock(Long sku) {
        iActivityRepository.updateActivitySkuStock(sku);
    }

    @Override
    public void clearActivitySkuStock(Long sku) {
        iActivityRepository.clearActivitySkuStock(sku);
    }

    @Override
    public Integer queryRaffleActivityAccountDayPartakeCount(Long activityId, String userId) {
        return iActivityRepository.queryRaffleActivityAccountDayPartakeCount(activityId, userId);
    }

    @Override
    public ActivityAccountEntity queryActivityAccountEntity(Long activityId, String userId) {
        // 查询总账户额度
        ActivityAccountEntity activityAccountEntity = iActivityRepository.queryActivityAccountByUserId(userId, activityId);
        if (activityAccountEntity == null) {
            return ActivityAccountEntity.builder()
                    .userId(userId)
                    .activityId(activityId)
                    .totalCount(0)
                    .totalCountSurplus(0)
                    .dayCount(0)
                    .dayCountSurplus(0)
                    .monthCount(0)
                    .monthCountSurplus(0)
                    .build();
        }

        SimpleDateFormat dateFormatMonth = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat dateFormatDay = new SimpleDateFormat("yyyy-MM-dd");
        String month = dateFormatMonth.format(new Date());
        String day = dateFormatDay.format(new Date());

        // 查询月账户额度
        ActivityAccountMonthEntity activityAccountMonthEntity = iActivityRepository.queryActivityAccountMonthByUserId(userId, activityId, month);
        // 查询日账户额度
        ActivityAccountDayEntity activityAccountDayEntity = iActivityRepository.queryActivityAccountDayByUserId(userId, activityId, day);

        // 如果没有创建月账户，则从总账户中获取月总额度填充。「当新创建日账户时，会获得总账户额度」
        if (activityAccountMonthEntity != null) {
            activityAccountEntity.setMonthCount(activityAccountMonthEntity.getMonthCount());
            activityAccountEntity.setMonthCountSurplus(activityAccountMonthEntity.getMonthCountSurplus());
        }
        // 如果没有创建日账户，则从总账户中获取日总额度填充。「当新创建日账户时，会获得总账户额度」
        if (activityAccountDayEntity != null) {
            activityAccountEntity.setDayCount(activityAccountDayEntity.getDayCount());
            activityAccountEntity.setDayCountSurplus(activityAccountDayEntity.getDayCountSurplus());
        }

        return activityAccountEntity;
    }

    @Override
    public Integer queryRaffleActivityAccountPartakeCount(Long activityId, String userId) {
        return iActivityRepository.queryRaffleActivityAccountPartakeCount(activityId, userId);
    }
}
