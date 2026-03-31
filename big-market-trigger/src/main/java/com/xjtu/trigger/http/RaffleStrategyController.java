package com.xjtu.trigger.http;

import com.alibaba.fastjson.JSON;
import com.xjtu.domain.activity.service.IRaffleActivityAccountQuotaService;
import com.xjtu.domain.strategy.model.entity.RaffleAwardEntity;
import com.xjtu.domain.strategy.model.entity.RaffleFactorEntity;
import com.xjtu.domain.strategy.model.entity.StrategyAwardEntity;
import com.xjtu.domain.strategy.service.IRaffleAward;
import com.xjtu.domain.strategy.service.IRaffleRule;
import com.xjtu.domain.strategy.service.IRaffleStrategy;
import com.xjtu.domain.strategy.service.armory.IStrategyArmory;
import com.xjtu.trigger.api.IRaffleStrategyService;
import com.xjtu.trigger.api.dto.RaffleAwardListRequestDTO;
import com.xjtu.trigger.api.dto.RaffleAwardListResponseDTO;
import com.xjtu.trigger.api.dto.RaffleStrategyRequestDTO;
import com.xjtu.trigger.api.dto.RaffleStrategyResponseDTO;
import com.xjtu.types.enums.ResponseCode;
import com.xjtu.types.exception.AppException;
import com.xjtu.types.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author mlei@xjtu
 * @description 接口服务接口实现类
 * @create 2026/3/19 02:07
 */
@Slf4j
@RestController
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/api/${app.config.api-version}/raffle/strategy/")
public class RaffleStrategyController implements IRaffleStrategyService {

    @Resource
    private IRaffleStrategy iRaffleStrategy;
    @Resource
    private IStrategyArmory iStrategyArmory;
    @Resource
    private IRaffleAward iRaffleAward;
    @Resource
    private IRaffleRule iRaffleRule;
    @Resource
    private IRaffleActivityAccountQuotaService iRaffleActivityAccountQuotaService;

    /**
     * 策略装配，将策略信息装配到缓存中
     * <a href="http://localhost:8091/api/v1/raffle/strategy/strategy_armory">/api/v1/raffle/strategy_armory</a>
     *
     * @param strategyId 策略ID
     * @return 装配结果
     */
    @RequestMapping(value = "strategy_armory",method = RequestMethod.GET)
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
     * <a href="http://localhost:8091/api/v1/raffle/strategy/query_raffle_award_list">/api/v1/raffle/query_raffle_award_list</a>
     * 请求参数 raw json
     *
     *@param request {"activityId":100301,"userId":"mlei"}
     * @return 奖品列表
     */
    @RequestMapping(value = "query_raffle_award_list",method = RequestMethod.POST)
    @Override
    public Response<List<RaffleAwardListResponseDTO>> queryStrategyAwardList(@RequestBody RaffleAwardListRequestDTO request) {
        try {
            log.info("查询抽奖奖品列表开始 userId: {} activityId: {}", request.getUserId(), request.getActivityId());
            // 1. 参数校验
            if (StringUtils.isBlank(request.getUserId()) || request.getActivityId() == null) {
                throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
            }

            // 2. 查询奖品配置
            List<StrategyAwardEntity> strategyAwardEntities = iRaffleAward.queryRaffleStrategyAwardListByActivityId(request.getActivityId());

            // 3. 获取规则配置
            String[] treeIds = strategyAwardEntities.stream()
                    .map(StrategyAwardEntity::getRuleModels)
                    .filter(ruleModel -> ruleModel != null && !ruleModel.isEmpty())
                    .toArray(String[]::new);

            // 4. 查询规则配置 - 获取奖品的解锁限制，抽奖N次后解锁
            Map<String, Integer> ruleLockCountMap = iRaffleRule.queryAwardRuleLockCount(treeIds);

            // 5. 查询抽奖次数-用户已经参与的抽奖次数
            Integer dayPartakeCount = iRaffleActivityAccountQuotaService.queryRaffleActivityAccountDayPartakeCount(request.getActivityId(), request.getUserId());

            // 6. 填充数据
            List<RaffleAwardListResponseDTO> responseDTOS = new ArrayList<>(strategyAwardEntities.size());
            for (StrategyAwardEntity strategyAwardEntity : strategyAwardEntities) {
                Integer awardRuleLockCount = ruleLockCountMap.get(strategyAwardEntity.getRuleModels());
                responseDTOS.add(RaffleAwardListResponseDTO
                        .builder()
                        .awardId(strategyAwardEntity.getAwardId())
                        .awardTitle(strategyAwardEntity.getAwardTitle())
                        .awardSubtitle(strategyAwardEntity.getAwardSubtitle())
                        .sort(strategyAwardEntity.getSort())
                        .awardRuleLockCount(awardRuleLockCount)
                        .isAwardUnLock(awardRuleLockCount == null || dayPartakeCount >= awardRuleLockCount)
                        .waitUnlockCount(awardRuleLockCount == null || awardRuleLockCount <= dayPartakeCount ? 0 : awardRuleLockCount - dayPartakeCount)
                        .build());
            }
            Response<List<RaffleAwardListResponseDTO>> response = Response.<List<RaffleAwardListResponseDTO>>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(responseDTOS)
                    .build();
            log.info("查询抽奖奖品列表完成 userId:{} activityId：{} response: {}", request.getUserId(), request.getActivityId(), JSON.toJSONString(response));
            return response;
        } catch (Exception e) {
            log.error("查询抽奖奖品列表配置失败 userId:{} activityId：{}", request.getUserId(), request.getActivityId(), e);
            return Response.<List<RaffleAwardListResponseDTO>>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }

    /**
     * 随机抽奖接口
     * <a href="http://localhost:8091/api/v1/raffle/strategy/random_raffle">/api/v1/raffle/random_raffle</a>
     *
     * @param requestDTO 请求参数 {"strategyId":1000001}
     * @return 抽奖结果
     */
    @RequestMapping(value = "random_raffle",method = RequestMethod.POST)
    @Override
    public Response<RaffleStrategyResponseDTO> randomRaffle(@RequestBody RaffleStrategyRequestDTO requestDTO) {

        try {
            log.info("随机抽奖开始 strategyId: {}", requestDTO.getStrategyId());
            RaffleAwardEntity raffleAwardEntity = iRaffleStrategy.performRaffle(RaffleFactorEntity
                    .builder()
                    .userId("system")
                    .strategyId(requestDTO.getStrategyId())
                    .build());
            Response<RaffleStrategyResponseDTO> response = Response.<RaffleStrategyResponseDTO>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(RaffleStrategyResponseDTO.builder()
                            .awardId(raffleAwardEntity.getAwardId())
                            .awardIndex(raffleAwardEntity.getSort())
                            .build())
                    .build();
            log.info("随机抽奖完成 strategyId: {} response: {}", requestDTO.getStrategyId(), JSON.toJSONString(response));
            return response;
        } catch (Exception e) {
            log.error("随机抽奖失败 strategyId: {}", requestDTO.getStrategyId(), e);
            return Response.<RaffleStrategyResponseDTO>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }
}
