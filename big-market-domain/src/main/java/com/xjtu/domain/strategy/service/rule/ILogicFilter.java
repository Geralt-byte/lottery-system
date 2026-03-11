package com.xjtu.domain.strategy.service.rule;

import com.xjtu.domain.strategy.model.entity.RuleActionEntity;
import com.xjtu.domain.strategy.model.entity.RuleMatterEntity;

/**
 * @author mlei@xjtu
 * @description 规则抽奖过滤接口
 * @create 2026/3/11 22:18
 */
public interface ILogicFilter<T extends RuleActionEntity.RaffleEntity>{

    /*根据规则物料实体对象过滤*/
    RuleActionEntity<T> filter(RuleMatterEntity ruleMatterEntity);
}
