package com.xjtu.trigger.api;

import com.xjtu.trigger.api.dto.RaffleAwardListRequestDTO;
import com.xjtu.trigger.api.dto.RaffleAwardListResponseDTO;
import com.xjtu.trigger.api.dto.RaffleStrategyRequestDTO;
import com.xjtu.trigger.api.dto.RaffleStrategyResponseDTO;
import com.xjtu.types.model.Response;

import java.util.List;

/**
 * @author mlei@xjtu
 * @description 抽奖服务接口
 * @create 2026/3/19 01:52
 */
public interface IRaffleStrategyService {

    /**
     *
     * @param strategyId 策略id
     * @return 装配是否成功
     */
    Response<Boolean> strategyArmory(Long strategyId);

    /**
     *
     * @param requestDTO 查询奖品表请求参数
     * @return 奖品表
     */
    Response<List<RaffleAwardListResponseDTO>> queryStrategyAwardList(RaffleAwardListRequestDTO requestDTO);

    /**
     *
     * @param requestDTO 随机抽奖请求参数
     * @return 奖品
     */
    Response<RaffleStrategyResponseDTO> randomRaffle(RaffleStrategyRequestDTO requestDTO);
}
