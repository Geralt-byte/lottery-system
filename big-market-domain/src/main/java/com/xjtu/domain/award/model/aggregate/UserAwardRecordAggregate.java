package com.xjtu.domain.award.model.aggregate;

import com.xjtu.domain.award.model.entity.TaskEntity;
import com.xjtu.domain.award.model.entity.UserAwardRecordEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mlei@xjtu
 * @description 用户中奖记录聚合对象 【聚合代表一个事务操作】
 * @create 2026/3/30 02:58
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAwardRecordAggregate {

    private TaskEntity taskEntity;

    private UserAwardRecordEntity userAwardRecordEntity;
}
