package com.xjtu.domain.strategy.service.rule.factory;

import com.xjtu.domain.strategy.model.entity.RuleActionEntity;
import com.xjtu.domain.strategy.service.annotation.LogicStrategy;
import com.xjtu.domain.strategy.service.rule.ILogicFilter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author mlei@xjtu
 * @description 规则工厂
 * @create 2026/3/11 22:17
 */
@Service
public class DefaultLogicFactory {

    //创建线程安全的map
    public Map<String, ILogicFilter<?>> logicFilterMap = new ConcurrentHashMap<>();

    //此处为构造函数注入，省略了Autowired注入ILogicFilter的实现类
    public DefaultLogicFactory(List<ILogicFilter<?>> logicFilters) {
        logicFilters.forEach(logicFilter -> {
            LogicStrategy strategy = AnnotationUtils.findAnnotation(logicFilter.getClass(), LogicStrategy.class);
            if (strategy != null) {
                logicFilterMap.put(strategy.logicMode().getCode(), logicFilter);
            }
        });
    }

    /*将注入的ILogicFilter的实现类以map形式返回*/
    public <T extends RuleActionEntity.RaffleEntity> Map<String,ILogicFilter<T>> openLogicFilter(){
        return (Map<String, ILogicFilter<T>>) (Map<?,?>) logicFilterMap;
    }

    @Getter
    @AllArgsConstructor
    public enum LogicModel {

        RULE_WEIGHT("rule_weight", "【抽奖前规则】根据抽奖权重返回可抽奖范围KEY","before"),
        RULE_BLACKLIST("rule_blacklist", "【抽奖前规则】黑名单规则过滤，命中黑名单则直接返回","before"),
        RULE_LOCK("rule_lock", "【抽奖中规则】抽奖n次后，对应奖品可解锁抽奖","center"),
        RULE_LUCK_AWARD("rule_lock_award", "【抽奖后规则】抽奖n次后，对应奖品可解锁抽奖","after"),
        ;

        private final String code;
        private final String info;
        private final String type;

        public static boolean isCenter(String code){
            return "center".equals(LogicModel.valueOf(code.toUpperCase()).type);
        }

        public static boolean isAfter(String code){
            return "after".equals(LogicModel.valueOf(code.toUpperCase()).type);
        }
    }
}
