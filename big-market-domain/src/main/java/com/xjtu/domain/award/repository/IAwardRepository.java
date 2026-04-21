package com.xjtu.domain.award.repository;

import com.xjtu.domain.award.model.aggregate.GiveOutPrizesAggregate;
import com.xjtu.domain.award.model.aggregate.UserAwardRecordAggregate;

/**
 * @author mlei@xjtu
 * @description 奖品仓储服务接口
 * @create 2026/3/30 02:37
 */
public interface IAwardRepository {

    /**保存中奖记录*/
    void saveUserAwardRecord(UserAwardRecordAggregate userAwardRecordAggregate);
    /**查询奖品配置*/
    String queryAwardConfig(Integer awardId);
    /**保存配送奖品记录*/
    void saveGiveOutPrizesAggregate(GiveOutPrizesAggregate giveOutPrizesAggregate);
    /**查询奖品Key*/
    String queryAwardKey(Integer awardId);
}
