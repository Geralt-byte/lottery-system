package com.xjtu.domain.activity.service.partake;

import com.xjtu.domain.activity.model.aggregate.CreatePartakeOrderAggregate;
import com.xjtu.domain.activity.model.entity.*;
import com.xjtu.domain.activity.model.valobj.UserRaffleOrderStateVO;
import com.xjtu.domain.activity.repository.IActivityRepository;
import com.xjtu.types.enums.ResponseCode;
import com.xjtu.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author mlei@xjtu
 * @description RaffleActivityPartakeService
 * @create 2026/3/29 20:38
 */
@Slf4j
@Service
public class RaffleActivityPartakeService extends AbstractRaffleActivityPartake {

    private final SimpleDateFormat dateFormatMonth = new SimpleDateFormat("yyyy-MM");
    private final SimpleDateFormat dateFormatDay = new SimpleDateFormat("yyyy-MM-dd");

    protected RaffleActivityPartakeService(IActivityRepository iActivityRepository) {
        super(iActivityRepository);
    }

    @Override
    protected UserRaffleOrderEntity buildUserRaffleOrder(String userId, Long activityId, Date currentDate) {
        ActivityEntity activityEntity = iActivityRepository.queryRaffleActivityByActivityId(activityId);
        return UserRaffleOrderEntity.builder()
                .userId(userId)
                .activityId(activityId)
                .activityName(activityEntity.getActivityName())
                .strategyId(activityEntity.getStrategyId())
                .orderId(RandomStringUtils.randomNumeric(12))
                .orderTime(currentDate)
                .orderState(UserRaffleOrderStateVO.create)
                .build();
    }

    @Override
    protected CreatePartakeOrderAggregate doFilterAccount(String userId, Long activityId, Date currentDate) {
        //查询总账户额度
        ActivityAccountEntity activityAccountEntity = iActivityRepository.queryActivityAccountByUserId(userId, activityId);

        if (activityAccountEntity == null || activityAccountEntity.getTotalCountSurplus() <= 0) {
            throw new AppException(ResponseCode.ACCOUNT_QUOTA_ERROR.getCode(), ResponseCode.ACCOUNT_QUOTA_ERROR.getInfo());
        }

        String month = dateFormatMonth.format(currentDate);
        String day = dateFormatDay.format(currentDate);

        //查询月账户额度
        ActivityAccountMonthEntity activityAccountMonthEntity = iActivityRepository.queryActivityAccountMonthByUserId(userId, activityId, month);
        if (activityAccountMonthEntity != null && activityAccountMonthEntity.getMonthCountSurplus() <= 0) {
            throw new AppException(ResponseCode.ACCOUNT_MONTH_QUOTA_ERROR.getCode(), ResponseCode.ACCOUNT_MONTH_QUOTA_ERROR.getInfo());
        }

        // 创建月账户额度；true = 存在月账户、false = 不存在月账户
        boolean isExistAccountMonth = (activityAccountMonthEntity != null);
        if (!isExistAccountMonth) {
            activityAccountMonthEntity = ActivityAccountMonthEntity
                    .builder()
                    .userId(userId)
                    .activityId(activityId)
                    .month(month)
                    .monthCount(activityAccountEntity.getMonthCount())
                    .monthCountSurplus(activityAccountEntity.getMonthCountSurplus())
                    .build();
        }

        // 查询日账户额度
        ActivityAccountDayEntity activityAccountDayEntity = iActivityRepository.queryActivityAccountDayByUserId(userId, activityId, day);
        if (activityAccountDayEntity != null && activityAccountDayEntity.getDayCountSurplus() <= 0) {
            throw new AppException(ResponseCode.ACCOUNT_DAY_QUOTA_ERROR.getCode(), ResponseCode.ACCOUNT_DAY_QUOTA_ERROR.getInfo());
        }

        // 创建日账户额度；true = 存在日账户、false = 不存在日账户
        boolean isExistAccountDay = (activityAccountDayEntity != null);
        if (!isExistAccountDay) {
            activityAccountDayEntity=ActivityAccountDayEntity
                    .builder()
                    .userId(userId)
                    .activityId(activityId)
                    .day(day)
                    .dayCount(activityAccountEntity.getDayCount())
                    .dayCountSurplus(activityAccountEntity.getDayCountSurplus())
                    .build();
        }

        //构建对象
        return CreatePartakeOrderAggregate
                .builder()
                .userId(userId)
                .activityId(activityId)
                .activityAccountEntity(activityAccountEntity)
                .activityAccountMonthEntity(activityAccountMonthEntity)
                .activityAccountDayEntity(activityAccountDayEntity)
                .isExistAccountMonth(isExistAccountMonth)
                .isExistAccountDay(isExistAccountDay)
                .build();
    }
}
