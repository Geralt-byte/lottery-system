package com.xjtu.domain.rebate.repository;

import com.xjtu.domain.rebate.model.aggregate.BehaviorRebateAggregate;
import com.xjtu.domain.rebate.model.valobj.BehaviorTypeVO;
import com.xjtu.domain.rebate.model.valobj.DailyBehaviorRebateVO;

import java.util.List;

/**
 * @author mlei@xjtu
 * @description 行为返利服务仓储接口
 * @create 2026/4/1 08:58
 */
public interface IBehaviorRebateRepository {

    /**
     * @description 查询行为返利配置
     */
    List<DailyBehaviorRebateVO> queryDailyBehaviorRebateConfig(BehaviorTypeVO behaviorTypeVO);

    /**
     * @description 保存用户行为返利记录
     */
    void saveUserRebateRecord(String userId, List<BehaviorRebateAggregate> behaviorRebateAggregates);
}
