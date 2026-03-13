package com.xjtu.domain.strategy.service.raffle;

import com.xjtu.domain.strategy.model.entity.RaffleAwardEntity;
import com.xjtu.domain.strategy.model.entity.RaffleFactorEntity;
import com.xjtu.domain.strategy.model.entity.RuleActionEntity;
import com.xjtu.domain.strategy.model.entity.StrategyEntity;
import com.xjtu.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.xjtu.domain.strategy.model.valobj.StrategyAwardRuleModelVO;
import com.xjtu.domain.strategy.repository.IStrategyRepository;
import com.xjtu.domain.strategy.service.IRaffleStrategy;
import com.xjtu.domain.strategy.service.armory.IStrategyDispatch;
import com.xjtu.domain.strategy.service.rule.factory.DefaultLogicFactory;
import com.xjtu.types.enums.ResponseCode;
import com.xjtu.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author mlei@xjtu
 * @description 抽奖策略抽象类，定义抽奖的标准流程
 * @create 2026/3/11 21:45
 */
@Slf4j
public abstract class AbstractRaffleStrategy implements IRaffleStrategy {

    /*引入策略服务仓储接口*/
    protected IStrategyRepository iStrategyRepository;
    /*引入策略抽奖调度接口*/
    protected IStrategyDispatch iStrategyDispatch;

    /*构造函数注入*/
    public AbstractRaffleStrategy(IStrategyRepository iStrategyRepository, IStrategyDispatch iStrategyDispatch) {
        this.iStrategyRepository = iStrategyRepository;
        this.iStrategyDispatch = iStrategyDispatch;
    }

    @Override
    public RaffleAwardEntity performRaffle(RaffleFactorEntity raffleFactorEntity) {
        //参数校验
        String userId=raffleFactorEntity.getUserId();
        Long strategyId = raffleFactorEntity.getStrategyId();
        if(strategyId==null|| StringUtils.isBlank(userId)){
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }

        //查询策略实体
        StrategyEntity strategyEntity = iStrategyRepository.queryStrategyEntityByStrategyId(strategyId);

        //抽奖前规则过滤
        RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> ruleActionEntity = this.doCheckRaffleBeforeLogic(raffleFactorEntity, strategyEntity.ruleModels());

        //分别对黑名单拦截和权重拦截进行处理
        if(RuleLogicCheckTypeVO.TAKE_OVER.getCode().equals(ruleActionEntity.getCode())){
            //位于黑名单时，直接返回固定的奖品
            if(DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode().equals(ruleActionEntity.getRuleModel())){
                return RaffleAwardEntity.builder()
                        .awardId(ruleActionEntity.getData().getAwardId())
                        .build();
            }else if(DefaultLogicFactory.LogicModel.RULE_WEIGHT.getCode().equals(ruleActionEntity.getRuleModel())){
                //处于权重规则时，调用权重抽奖接口
                RuleActionEntity.RaffleBeforeEntity raffleBeforeEntity = ruleActionEntity.getData();
                String ruleWeightValueKey = raffleBeforeEntity.getRuleWeightValueKey();
                Integer awardId = iStrategyDispatch.getRandomAwardId(strategyId, ruleWeightValueKey);
                return RaffleAwardEntity.builder()
                        .awardId(awardId)
                        .build();
            }
        }

        //放行状态，调用普通抽奖接口
        Integer awardId = iStrategyDispatch.getRandomAwardId(strategyId);

        //抽奖中规则过滤，判断抽中特定奖品（需要抽奖次数解锁的奖品）此时用户的抽奖次数是否满足要求
        StrategyAwardRuleModelVO strategyAwardRuleModelVO=iStrategyRepository.queryStrategyAwardRuleModelVO(strategyId,awardId);

        //抽奖中规则过滤
        RuleActionEntity<RuleActionEntity.RaffleCenterEntity> raffleCenterEntity = this.doCheckRaffleCenterLogic(RaffleFactorEntity.builder()
                .userId(userId)
                .strategyId(strategyId)
                .awardId(awardId)
                .build()
                , strategyAwardRuleModelVO.raffleCenterRuleModelList());

        if(raffleCenterEntity.getCode().equals(RuleLogicCheckTypeVO.TAKE_OVER.getCode())){
            log.info("【临时日志】中奖中规则拦截，通过抽奖后规则 rule_luck_award 走兜底奖励。");
            return RaffleAwardEntity.builder()
                    .awardDesc("中奖中规则拦截，通过抽奖后规则 rule_luck_award 走兜底奖励。")
                    .build();
        }

        return RaffleAwardEntity.builder()
                .awardId(awardId)
                .build();
    }

    /*抽奖前根据策略对应的规则进行规则过滤*/
    protected abstract RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> doCheckRaffleBeforeLogic(RaffleFactorEntity raffleFactorEntity,String[] ruleModels);

    /*抽奖中根据策略对应的规则进行规则过滤*/
    protected abstract RuleActionEntity<RuleActionEntity.RaffleCenterEntity> doCheckRaffleCenterLogic(RaffleFactorEntity raffleFactorEntity, String[] ruleModels);
}
