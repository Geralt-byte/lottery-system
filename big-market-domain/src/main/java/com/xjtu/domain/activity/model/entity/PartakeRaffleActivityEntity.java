package com.xjtu.domain.activity.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mlei@xjtu
 * @description 参与抽奖活动实体对象
 * @create 2026/3/24 04:38
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PartakeRaffleActivityEntity {
    /** 用户ID */
    private String userId;
    /** 活动ID */
    private Long activityId;
}
