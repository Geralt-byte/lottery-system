package com.xjtu.trigger.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mlei@xjtu
 * @description 用户活动账户应答对象
 * @create 2026/4/2 01:08
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserActivityAccountResponseDTO {
    /**总次数*/
    private Integer totalCount;
    /**总次数-剩余*/
    private Integer totalCountSurplus;
    /**日次数*/
    private Integer dayCount;
    /**日次数-剩余*/
    private Integer dayCountSurplus;
    /**月次数*/
    private Integer monthCount;
    /**月次数-剩余*/
    private Integer monthCountSurplus;
}
