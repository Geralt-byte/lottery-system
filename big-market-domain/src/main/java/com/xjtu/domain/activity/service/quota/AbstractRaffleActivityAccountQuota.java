package com.xjtu.domain.activity.service.quota;

import com.xjtu.domain.activity.model.aggregate.CreateQuotaOrderAggregate;
import com.xjtu.domain.activity.model.entity.*;
import com.xjtu.domain.activity.repository.IActivityRepository;
import com.xjtu.domain.activity.service.IRaffleActivityAccountQuotaService;
import com.xjtu.domain.activity.service.quota.rule.IActionChain;
import com.xjtu.domain.activity.service.quota.rule.factory.DefaultActivityChainFactory;
import com.xjtu.types.enums.ResponseCode;
import com.xjtu.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author mlei@xjtu
 * @description 抽奖活动抽象类，定义标准的流程
 * @create 2026/3/24 04:35
 */
@Slf4j
public abstract class AbstractRaffleActivityAccountQuota extends RaffleActivityAccountQuotaSupport implements IRaffleActivityAccountQuotaService {

    public AbstractRaffleActivityAccountQuota(DefaultActivityChainFactory defaultActivityChainFactory, IActivityRepository iActivityRepository) {
        super(defaultActivityChainFactory, iActivityRepository);
    }

    @Override
    public String createOrder(SkuRechargeEntity skuRechargeEntity) {
        // 1.参数校验
        String userId = skuRechargeEntity.getUserId();
        Long sku = skuRechargeEntity.getSku();
        String outBusinessNo = skuRechargeEntity.getOutBusinessNo();
        if (sku == null || StringUtils.isBlank(userId) || StringUtils.isBlank(outBusinessNo)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }

        // 2.查询基础信息
        // 2.1 通过sku查询活动信息
        ActivitySkuEntity activitySkuEntity = this.queryActivitySku(sku);
        // 2.2 查询活动信息
        ActivityEntity activityEntity = this.queryRaffleActivityByActivityId(activitySkuEntity.getActivityId());
        // 2.3 查询活动次数
        ActivityCountEntity activityCountEntity = this.queryRaffleActivityCountByActivityCountId(activitySkuEntity.getActivityCountId());

        // 3 活动动作规则校验
        IActionChain actionChain = defaultActivityChainFactory.openActionChain();
        boolean success = actionChain.action(activitySkuEntity, activityEntity, activityCountEntity);

        // 4 构建订单聚合对象
        CreateQuotaOrderAggregate createQuotaOrderAggregate = buildOrderAggregate(skuRechargeEntity, activitySkuEntity, activityEntity, activityCountEntity);

        // 5 保存订单
        doSaveOrder(createQuotaOrderAggregate);

        // 6 返回单号
        return createQuotaOrderAggregate.getActivityOrderEntity().getOrderId();
    }

    protected abstract CreateQuotaOrderAggregate buildOrderAggregate(SkuRechargeEntity skuRechargeEntity, ActivitySkuEntity activitySkuEntity, ActivityEntity activityEntity, ActivityCountEntity activityCountEntity);

    protected abstract void doSaveOrder(CreateQuotaOrderAggregate createQuotaOrderAggregate);
}
