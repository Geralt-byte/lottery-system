package com.xjtu.domain.activity.service.quota.rule.factory;

import com.xjtu.domain.activity.service.quota.rule.IActionChain;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author mlei@xjtu
 * @description 责任链工厂
 * @create 2026/3/25 03:37
 */
@Service
public class DefaultActivityChainFactory {

    private final Map<String,IActionChain> iActionChainGroup;


    public DefaultActivityChainFactory(Map<String, IActionChain> iActionChainGroup) {
        this.iActionChainGroup = iActionChainGroup;
    }

    public IActionChain openActionChain(){
        IActionChain actionChain=iActionChainGroup.get(ActionModel.ACTIVITY_BASE_ACTION.getCode());
        actionChain.appendNext(iActionChainGroup.get(ActionModel.ACTIVITY_SKU_STOCK_ACTION.getCode()));
        return actionChain;
    }

    @Getter
    @AllArgsConstructor
    public enum ActionModel{

        ACTIVITY_BASE_ACTION("activity_base_action","活动的库存、时间校验"),
        ACTIVITY_SKU_STOCK_ACTION("activity_sku_stock_action","活动sku库存"),
        ;

        private final String code;
        private final String info;
    }
}
