package com.xjtu.domain.activity.model.aggregate;

import com.xjtu.domain.activity.model.entity.ActivityAccountEntity;
import com.xjtu.domain.activity.model.entity.ActivityOrderEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mlei@xjtu
 * @description 下单聚合对象
 * @create 2026/3/24 04:50
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderAggregate {
    /**用户ID*/
    private String userId;
    /**活动ID*/
    private Long activityId;
    /**总次数*/
    private Integer totalCount;
    /**日次数*/
    private Integer dayCount;
    /**月次数*/
    private Integer monthCount;
    /**活动订单实体*/
    private ActivityOrderEntity activityOrderEntity;
}
