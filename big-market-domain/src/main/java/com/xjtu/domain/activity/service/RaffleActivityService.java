package com.xjtu.domain.activity.service;

import com.xjtu.domain.activity.model.entity.ActivityOrderEntity;
import com.xjtu.domain.activity.model.entity.ActivityShopCartEntity;
import com.xjtu.domain.activity.repository.IActivityRepository;
import org.springframework.stereotype.Service;

/**
 * @author mlei@xjtu
 * @description 抽奖活动服务
 * @create 2026/3/24 04:35
 */
@Service
public class RaffleActivityService extends AbstractIRaffleActivity{

    public RaffleActivityService(IActivityRepository iActivityRepository) {
        super(iActivityRepository);
    }
}
