package com.xjtu.domain.activity.model.aggregate;

import com.xjtu.domain.activity.model.entity.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mlei@xjtu
 * @description 参与活动订单聚合对象
 * @create 2026/3/29 19:22
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreatePartakeOrderAggregate {
    /**用户ID*/
    private String userId;
    /**活动ID*/
    private Long activityId;
    /**账户总额度*/
    private ActivityAccountEntity activityAccountEntity;
    /**账户月额度*/
    private ActivityAccountMonthEntity activityAccountMonthEntity;
    /**账户日额度*/
    private ActivityAccountDayEntity activityAccountDayEntity;
    /**是否存在月账户*/
    private boolean isExistAccountMonth=true;
    /**是否存在日账户*/
    private boolean isExistAccountDay=true;
    /**抽奖单实体*/
    private UserRaffleOrderEntity userRaffleOrderEntity;
}
