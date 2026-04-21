package com.xjtu.domain.award.service;

import com.xjtu.domain.award.model.entity.DistributeAwardEntity;
import com.xjtu.domain.award.model.entity.UserAwardRecordEntity;

/**
 * @author mlei@xjtu
 * @description 奖品服务接口
 * @create 2026/3/30 02:37
 */
public interface IAwardService {

    /**保存中奖记录*/
    void saveUserAwardRecord(UserAwardRecordEntity userAwardRecordEntity);

    /**配送奖品*/
    void distributeAward(DistributeAwardEntity distributeAward);
}
