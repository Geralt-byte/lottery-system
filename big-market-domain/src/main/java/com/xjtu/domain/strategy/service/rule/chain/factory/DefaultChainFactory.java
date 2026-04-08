package com.xjtu.domain.strategy.service.rule.chain.factory;

import com.xjtu.domain.strategy.model.entity.StrategyEntity;
import com.xjtu.domain.strategy.repository.IStrategyRepository;
import com.xjtu.domain.strategy.service.rule.chain.ILogicChain;
import lombok.*;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author mlei@xjtu
 * @description 责任链工厂
 * @create 2026/3/13 17:50
 */
@Service
public class DefaultChainFactory {

    // 原型模式获取对象
    private final ApplicationContext applicationContext;

    private final Map<Long, ILogicChain> logicChainGroup;
    protected IStrategyRepository iStrategyRepository;

    /*构造函数注入*/
    public DefaultChainFactory(ApplicationContext applicationContext, IStrategyRepository iStrategyRepository) {
        this.applicationContext = applicationContext;
        this.logicChainGroup = new ConcurrentHashMap<>();
        this.iStrategyRepository = iStrategyRepository;
    }

    /*构建责任链*/
    public ILogicChain openLogicChain(Long strategyId) {

        ILogicChain cacheLogicChain = logicChainGroup.get(strategyId);
        if(cacheLogicChain!=null) return cacheLogicChain;

        //查询策略实体
        StrategyEntity strategyEntity = iStrategyRepository.queryStrategyEntityByStrategyId(strategyId);
        //数据格式转化
        String[] ruleModels = strategyEntity.ruleModels();

        //为空装填默认链返回
        if (ruleModels == null || ruleModels.length == 0) {
            ILogicChain ruleDefaultLogicChain=applicationContext.getBean(LogicModel.RULE_DEFAULT.getCode(),ILogicChain.class);
            // 写入缓存
            logicChainGroup.put(strategyId,ruleDefaultLogicChain);
            return ruleDefaultLogicChain;
        }

        // 按照配置顺序装填用户配置的责任链；rule_blacklist、rule_weight
        // 「注意此数据从Redis缓存中获取，如果更新库表，记得在测试阶段手动处理缓存」
        ILogicChain iLogicChain=applicationContext.getBean(ruleModels[0],ILogicChain.class);
        ILogicChain cur=iLogicChain;
        for (int i = 1; i < ruleModels.length; i++) {
            ILogicChain nextChain = applicationContext.getBean(ruleModels[i], ILogicChain.class);
            cur = cur.appendNext(nextChain);
        }
        //最后装填默认规则链
        cur.appendNext(applicationContext.getBean(LogicModel.RULE_DEFAULT.getCode(),ILogicChain.class));
        //写入缓存
        logicChainGroup.put(strategyId,iLogicChain);

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
