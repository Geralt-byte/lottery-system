package com.xjtu.domain.strategy.service.rule.chain.factory;

import com.xjtu.domain.strategy.model.entity.StrategyEntity;
import com.xjtu.domain.strategy.repository.IStrategyRepository;
import com.xjtu.domain.strategy.service.rule.chain.ILogicChain;
import lombok.*;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author mlei@xjtu
 * @description 责任链工厂
 * @create 2026/3/13 17:50
 */
@Service
public class DefaultChainFactory {

    private final Map<String, ILogicChain> logicChainGroup;
    protected IStrategyRepository iStrategyRepository;

    /*构造函数注入*/
    public DefaultChainFactory(Map<String, ILogicChain> logicChainGroup, IStrategyRepository iStrategyRepository) {
        this.logicChainGroup = logicChainGroup;
        this.iStrategyRepository = iStrategyRepository;
    }

    /*构建责任链*/
    public ILogicChain openLogicChain(Long strategyId) {

        //查询策略实体
        StrategyEntity strategyEntity = iStrategyRepository.queryStrategyEntityByStrategyId(strategyId);
        //数据格式转化
        String[] ruleModels = strategyEntity.ruleModels();

        //为空装填默认链返回
        if (ruleModels == null || ruleModels.length == 0) {
            return logicChainGroup.get(LogicModel.RULE_DEFAULT.getCode());
        }

        //装填规则链
        ILogicChain iLogicChain = logicChainGroup.get(ruleModels[0]);
        ILogicChain cur=iLogicChain;
        for (int i = 1; i < ruleModels.length; i++) {
            cur = cur.appendNext(logicChainGroup.get(ruleModels[i]));
        }
        //最后装填默认规则链
        cur.appendNext(logicChainGroup.get(LogicModel.RULE_DEFAULT.getCode()));

        return iLogicChain;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StrategyAwardVO{
        /** 抽奖奖品id*/
        private Integer awardId;
        /** 规则值*/
        private String logicModel;
    }

    @Getter
    @AllArgsConstructor
    public enum LogicModel{

        RULE_DEFAULT("rule_default","默认抽奖"),
        RULE_WEIGHT("rule_weight","权重抽奖"),
        RULE_BLACKLIST("rule_blacklist","黑名单抽奖"),
        ;

        private final String code;
        private final String info;
    }
}
