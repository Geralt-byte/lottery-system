package com.xjtu.domain.award.model.aggregate;

import com.xjtu.domain.award.model.entity.DistributeAwardEntity;
import com.xjtu.domain.award.model.entity.UserAwardRecordEntity;
import com.xjtu.domain.award.model.entity.UserCreditAwardEntity;
import com.xjtu.domain.award.model.valobj.AwardStateVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author mlei@xjtu
 * @description 发放奖品聚合对象
 * @create 2026/4/21 23:25
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GiveOutPrizesAggregate {
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 用户中奖记录
     */
    private UserAwardRecordEntity userAwardRecordEntity;
    /**
     * 用户积分记录
     */
    private UserCreditAwardEntity userCreditAwardEntity;

    public static UserAwardRecordEntity buildDistributeUserAwardRecordEntity(String userId, String orderId, Integer awardId, AwardStateVO awardState) {
        return UserAwardRecordEntity.builder()
                .userId(userId)
                .orderId(orderId)
                .awardId(awardId)
                .awardState(awardState)
                .build();
    }

    public static UserCreditAwardEntity buildDistributeUserCreditAwardEntity(String userId, BigDecimal creditAmount) {
        return UserCreditAwardEntity.builder()
                .userId(userId)
                .creditAmount(creditAmount)
                .build();
    }
}
