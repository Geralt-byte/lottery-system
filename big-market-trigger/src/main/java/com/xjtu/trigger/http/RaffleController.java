package com.xjtu.trigger.http;

import com.alibaba.fastjson.JSON;
import com.xjtu.domain.strategy.model.entity.RaffleAwardEntity;
import com.xjtu.domain.strategy.model.entity.RaffleFactorEntity;
import com.xjtu.domain.strategy.model.entity.StrategyAwardEntity;
import com.xjtu.domain.strategy.service.IRaffleAward;
import com.xjtu.domain.strategy.service.IRaffleStrategy;
import com.xjtu.domain.strategy.service.armory.IStrategyArmory;
import com.xjtu.trigger.api.IRaffleService;
import com.xjtu.trigger.api.dto.RaffleAwardListRequestDTO;
import com.xjtu.trigger.api.dto.RaffleAwardListResponseDTO;
import com.xjtu.trigger.api.dto.RaffleRequestDTO;
import com.xjtu.trigger.api.dto.RaffleResponseDTO;
import com.xjtu.types.enums.ResponseCode;
import com.xjtu.types.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mlei@xjtu
 * @description 接口服务接口实现类
 * @create 2026/3/19 02:07
 */
@Slf4j
@RestController
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/api/${app.config.api-version}/raffle/")
public class RaffleController implements IRaffleService {

    @Resource
    private IRaffleStrategy iRaffleStrategy;
    @Resource
    private IStrategyArmory iStrategyArmory;
    @Resource
    private IRaffleAward iRaffleAward;

    /**
     * 策略装配，将策略信息装配到缓存中
     * <a href="http://localhost:8091/api/v1/raffle/strategy_armory">/api/v1/raffle/strategy_armory</a>
     *
     * @param strategyId 策略ID
     * @return 装配结果
     */
    @GetMapping(value = "strategy_armory")
    @Override
    public Response<Boolean> strategyArmory(@RequestParam Long strategyId) {
        try {
            log.info("抽奖策略装配开始 strategyId: {}", strategyId);
            boolean armoryStatus = iStrategyArmory.assembleLotteryStrategy(strategyId);
            Response<Boolean> response = Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(armoryStatus)
                    .build();
            log.info("抽奖策略装配完成 strategyId: {} response: {}", strategyId, JSON.toJSONString(response));
            return response;
        } catch (Exception e) {
            log.error("抽奖策略装配失败 strategyId: {}", strategyId, e);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }

    /**
     * 查询奖品列表
     * <a href="http://localhost:8091/api/v1/raffle/query_raffle_award_list">/api/v1/raffle/query_raffle_award_list</a>
     * 请求参数 raw json
     *
     * @param requestDTO {"strategyId":1000001}
     * @return 奖品列表
     */
    @PostMapping(value = "query_raffle_award_list")
    @Override
    public Response<List<RaffleAwardListResponseDTO>> queryStrategyAwardList(@RequestBody RaffleAwardListRequestDTO requestDTO) {
        try {
            log.info("查询抽奖奖品列表开始 strategyId: {}", requestDTO.getStrategyId());
            List<StrategyAwardEntity> strategyAwardEntities = iRaffleAward.queryRaffleStrategyAwardList(requestDTO.getStrategyId());
            List<RaffleAwardListResponseDTO> responseDTOS = new ArrayList<>(strategyAwardEntities.size());
            for (StrategyAwardEntity strategyAwardEntity : strategyAwardEntities) {
                responseDTOS.add(RaffleAwardListResponseDTO
                        .builder()
                        .awardId(strategyAwardEntity.getAwardId())
                        .awardTitle(strategyAwardEntity.getAwardTitle())
                        .awardSubtitle(strategyAwardEntity.getAwardSubtitle())
                        .sort(strategyAwardEntity.getSort())
                        .build());
            }
            Response<List<RaffleAwardListResponseDTO>> response = Response.<List<RaffleAwardListResponseDTO>>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(responseDTOS)
                    .build();
            log.info("查询抽奖奖品列表完成 strategyId: {} response: {}", requestDTO.getStrategyId(), JSON.toJSONString(response));
            return response;
        } catch (Exception e) {
            log.error("查询抽奖奖品列表配置失败 strategyId: {}", requestDTO.getStrategyId(), e);
            return Response.<List<RaffleAwardListResponseDTO>>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }

    /**
     * 随机抽奖接口
     * <a href="http://localhost:8091/api/v1/raffle/random_raffle">/api/v1/raffle/random_raffle</a>
     *
     * @param requestDTO 请求参数 {"strategyId":1000001}
     * @return 抽奖结果
     */
    @PostMapping(value = "random_raffle")
    @Override
    public Response<RaffleResponseDTO> randomRaffle(@RequestBody RaffleRequestDTO requestDTO) {

        try {
            log.info("随机抽奖开始 strategyId: {}", requestDTO.getStrategyId());
            RaffleAwardEntity raffleAwardEntity = iRaffleStrategy.performRaffle(RaffleFactorEntity
                    .builder()
                    .userId("system")
                    .strategyId(requestDTO.getStrategyId())
                    .build());
            Response<RaffleResponseDTO> response = Response.<RaffleResponseDTO>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(RaffleResponseDTO.builder()
                            .awardId(raffleAwardEntity.getAwardId())
                            .awardIndex(raffleAwardEntity.getSort())
                            .build())
                    .build();
            log.info("随机抽奖完成 strategyId: {} response: {}", requestDTO.getStrategyId(), JSON.toJSONString(response));
            return response;
        } catch (Exception e) {
            log.error("随机抽奖失败 strategyId: {}", requestDTO.getStrategyId(), e);
            return Response.<RaffleResponseDTO>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }
}
