package com.xjtu.domain.activity.service.rule.chain.impl;

import com.xjtu.domain.activity.model.entity.ActivityCountEntity;
import com.xjtu.domain.activity.model.entity.ActivityEntity;
import com.xjtu.domain.activity.model.entity.ActivitySkuEntity;
import com.xjtu.domain.activity.service.rule.chain.AbstractIActionChain;
import com.xjtu.domain.activity.service.rule.chain.IActionChain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author mlei@xjtu
 * @description 活动规则过滤【日期、状态】
 * @create 2026/3/25 03:54
 */
@Slf4j
@Component("activity_base_action")
public class ActivityBaseActionChain extends AbstractIActionChain {

    @Override
    public boolean action(ActivitySkuEntity activitySkuEntity, ActivityEntity activityEntity, ActivityCountEntity activityCountEntity) {

        log.info("活动责任链-基础信息【有效期、状态】校验开始。");

        return next().action(activitySkuEntity, activityEntity, activityCountEntity);
    }
}
