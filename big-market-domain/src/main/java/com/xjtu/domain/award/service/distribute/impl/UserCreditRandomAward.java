package com.xjtu.domain.award.service.distribute.impl;

import com.xjtu.domain.award.model.aggregate.GiveOutPrizesAggregate;
import com.xjtu.domain.award.model.entity.DistributeAwardEntity;
import com.xjtu.domain.award.model.entity.UserAwardRecordEntity;
import com.xjtu.domain.award.model.entity.UserCreditAwardEntity;
import com.xjtu.domain.award.model.valobj.AwardStateVO;
import com.xjtu.domain.award.repository.IAwardRepository;
import com.xjtu.domain.award.service.distribute.IDistributeAward;
import com.xjtu.types.common.Constants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.MathContext;

/**
 * @author mlei@xjtu
 * @description UserCreditRandomAward
 * @create 2026/4/21 23:13
 */
@Component("user_credit_random")
public class UserCreditRandomAward implements IDistributeAward {

    @Resource
    private IAwardRepository iAwardRepository;

    @Override
    public void getOutPrizes(DistributeAwardEntity distributeAwardEntity) {
        // 奖品ID
        Integer awardId = distributeAwardEntity.getAwardId();
        // 查询奖品ID 「优先走透传的随机积分奖品配置」
        String awardConfig = distributeAwardEntity.getAwardConfig();
        if (StringUtils.isBlank(awardConfig)) {
            awardConfig = iAwardRepository.queryAwardConfig(awardId);
        }

        String[] creditRange = awardConfig.split(Constants.SPLIT);
        if (creditRange.length != 2) {
            throw new RuntimeException("award_config 「" + awardConfig + "」配置不是一个范围值，如 1,100");
        }

        // 生成随机积分值
        BigDecimal creditAmount = generateRandom(new BigDecimal(creditRange[0]), new BigDecimal(creditRange[1]));

        // 构建聚合对象
        UserAwardRecordEntity userAwardRecordEntity = GiveOutPrizesAggregate.buildDistributeUserAwardRecordEntity(
                distributeAwardEntity.getUserId(),
                distributeAwardEntity.getOrderId(),
                distributeAwardEntity.getAwardId(),
                AwardStateVO.complete
        );

        UserCreditAwardEntity userCreditAwardEntity = GiveOutPrizesAggregate.buildDistributeUserCreditAwardEntity(
                distributeAwardEntity.getUserId(),
                creditAmount
        );

        GiveOutPrizesAggregate giveOutPrizesAggregate = new GiveOutPrizesAggregate();
        giveOutPrizesAggregate.setUserId(distributeAwardEntity.getUserId());
        giveOutPrizesAggregate.setUserAwardRecordEntity(userAwardRecordEntity);
        giveOutPrizesAggregate.setUserCreditAwardEntity(userCreditAwardEntity);

        // 存储发奖对象
        iAwardRepository.saveGiveOutPrizesAggregate(giveOutPrizesAggregate);
    }

    private BigDecimal generateRandom(BigDecimal min, BigDecimal max) {
        if (min.equals(max)) {
            return min;
        }
        BigDecimal randomBigDecimal = min.add(BigDecimal.valueOf(Math.random()).multiply(max.subtract(min)));
        return randomBigDecimal.round(new MathContext(3));
    }
}
