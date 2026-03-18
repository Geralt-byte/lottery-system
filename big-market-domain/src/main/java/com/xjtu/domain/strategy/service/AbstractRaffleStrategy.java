package com.xjtu.domain.strategy.service;

import com.xjtu.domain.strategy.model.entity.RaffleAwardEntity;
import com.xjtu.domain.strategy.model.entity.RaffleFactorEntity;
import com.xjtu.domain.strategy.model.entity.StrategyAwardEntity;
import com.xjtu.domain.strategy.repository.IStrategyRepository;
import com.xjtu.domain.strategy.service.armory.IStrategyDispatch;
import com.xjtu.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import com.xjtu.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
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
    /*引入抽奖责任链*/
    protected final DefaultChainFactory defaultChainFactory;
    /*引入抽奖规则树*/
    protected final DefaultTreeFactory defaultTreeFactory;

    /*构造函数注入*/
    public AbstractRaffleStrategy(IStrategyRepository iStrategyRepository, IStrategyDispatch iStrategyDispatch, DefaultChainFactory defaultChainFactory, DefaultTreeFactory defaultTreeFactory) {
        this.iStrategyRepository = iStrategyRepository;
        this.iStrategyDispatch = iStrategyDispatch;
        this.defaultChainFactory = defaultChainFactory;
        this.defaultTreeFactory = defaultTreeFactory;
    }

    @Override
    public RaffleAwardEntity performRaffle(RaffleFactorEntity raffleFactorEntity) {
        //参数校验
        String userId=raffleFactorEntity.getUserId();
        Long strategyId = raffleFactorEntity.getStrategyId();
        if(strategyId==null|| StringUtils.isBlank(userId)){
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }

        //调用责任链进行抽奖,获得责任链奖品值对象
        DefaultChainFactory.StrategyAwardVO chainStrategyAwardVO = this.raffleLoginChain(userId, strategyId);
        log.info("抽奖策略计算-责任链出口 userId: {} strategyId: {} ruleModel: {} awardId: {}",userId,strategyId,chainStrategyAwardVO.getLogicModel(),chainStrategyAwardVO.getAwardId());
        //当责任链出口是黑名单或权重时，不继续走规则树，直接返回结果
        if(!DefaultChainFactory.LogicModel.RULE_DEFAULT.getCode().equals(chainStrategyAwardVO.getLogicModel())){
            // TODO awardConfig 暂时为空。黑名单指定积分奖品，后续需要在库表中配置上对应的1积分值，并获取到。
            return buildRaffleAwardEntity(strategyId,chainStrategyAwardVO.getAwardId(),null);
        }

        //调用规则树进行抽奖，获得规则树奖品值对象
        DefaultTreeFactory.StrategyAwardVO treeStrategyAwardVO = this.raffleLogicTree(userId, strategyId, chainStrategyAwardVO.getAwardId());
        log.info("抽奖策略计算-规则树出口 userId: {} strategyId: {} ruleValue: {} awardId: {}",userId,strategyId,treeStrategyAwardVO.getAwardRuleValue(),treeStrategyAwardVO.getAwardId());

        //返回抽奖结果
        return buildRaffleAwardEntity(strategyId,treeStrategyAwardVO.getAwardId(),treeStrategyAwardVO.getAwardRuleValue());
    }

    private RaffleAwardEntity buildRaffleAwardEntity(Long strategyId,Integer awardId,String awardConfig){
        StrategyAwardEntity strategyAwardEntity= iStrategyRepository.queryStrategyEntity(strategyId,awardId);
        return RaffleAwardEntity.builder()
                .awardId(awardId)
                .awardConfig(awardConfig)
                .sort(strategyAwardEntity.getSort())
                .build();
    }

    /*责任链抽奖方法*/
    public abstract DefaultChainFactory.StrategyAwardVO raffleLoginChain(String userId,Long strategyId);

    /*规则树抽奖方法*/
    public abstract DefaultTreeFactory.StrategyAwardVO raffleLogicTree(String userId,Long strategyId,Integer awardId);
}
