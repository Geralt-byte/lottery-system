package com.xjtu.domain.rebate.model.aggregate;

import com.xjtu.domain.rebate.model.entity.BehaviorRebateOrderEntity;
import com.xjtu.domain.rebate.model.entity.TaskEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mlei@xjtu
 * @description 行为返利聚合对象
 * @create 2026/4/1 08:47
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BehaviorRebateAggregate {
    /*用户ID*/
    private String userId;
    /*行为返利订单实体对象*/
    private BehaviorRebateOrderEntity behaviorRebateOrderEntity;
    /*任务实体对象*/
    private TaskEntity taskEntity;
}
