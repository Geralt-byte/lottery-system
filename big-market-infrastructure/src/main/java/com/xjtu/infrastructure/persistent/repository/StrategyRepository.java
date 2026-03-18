package com.xjtu.infrastructure.persistent.repository;

import com.xjtu.domain.strategy.model.entity.StrategyAwardEntity;
import com.xjtu.domain.strategy.model.entity.StrategyEntity;
import com.xjtu.domain.strategy.model.entity.StrategyRuleEntity;
import com.xjtu.domain.strategy.model.valobj.*;
import com.xjtu.domain.strategy.repository.IStrategyRepository;
import com.xjtu.infrastructure.persistent.dao.*;
import com.xjtu.infrastructure.persistent.po.*;
import com.xjtu.infrastructure.persistent.redis.IRedisService;
import com.xjtu.types.common.Constants;
import com.xjtu.types.enums.ResponseCode;
import com.xjtu.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**策略服务仓储实现*/

@Slf4j
@Repository
public class StrategyRepository implements IStrategyRepository {

    /**mybatis层接口注入*/
    @Resource
    private IStrategyAwardDao iStrategyAwardDao;
    @Resource
    private IStrategyDao iStrategyDao;
    @Resource
    private IStrategyRuleDao iStrategyRuleDao;
    @Resource
    private IRuleTreeDao iRuleTreeDao;
    @Resource
    private IRuleTreeNodeDao iRuleTreeNodeDao;
    @Resource
    private IRuleTreeNodeLineDao iRuleTreeNodeLineDao;

    /**Redisson层接口注入*/
    @Resource
    private IRedisService iRedisService;

    /**从redis中查找策略id对应的奖品列表*/
    @Override
    public List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId) {
        //先查redis缓存
        String cacheKey= Constants.RedisKey.STRATEGY_AWARD_LIST_KEY +strategyId;
        List<StrategyAwardEntity> strategyAwardEntities = iRedisService.getValue(cacheKey);
        if(strategyAwardEntities!=null&&!strategyAwardEntities.isEmpty()) return strategyAwardEntities;

        //redis缓存为空，查询数据库
        List<StrategyAward> strategyAwards = iStrategyAwardDao.queryStrategyAwardListByStrategyId(strategyId);
        strategyAwardEntities=new ArrayList<>(strategyAwards.size());
        for (StrategyAward strategyAward : strategyAwards) {
            StrategyAwardEntity strategyAwardEntity=StrategyAwardEntity.builder()
                    .strategyId(strategyAward.getStrategyId())
                    .awardId(strategyAward.getAwardId())
                    .awardTitle(strategyAward.getAwardTitle())
                    .awardSubtitle(strategyAward.getAwardSubtitle())
                    .awardCount(strategyAward.getAwardCount())
                    .awardCountSurplus(strategyAward.getAwardCountSurplus())
                    .awardRate(strategyAward.getAwardRate())
                    .sort(strategyAward.getSort())
                    .build();
            strategyAwardEntities.add(strategyAwardEntity);
        }
        //存储策略奖品到redis中
        iRedisService.setValue(cacheKey,strategyAwardEntities);
        return strategyAwardEntities;
    }

    /**从redis中查找策略id对应的单个奖品*/
    @Override
    public StrategyAwardEntity queryStrategyEntity(Long strategyId, Integer awardId) {
        //先查redis缓存
        String cacheKey= Constants.RedisKey.STRATEGY_AWARD_KEY +strategyId;
        StrategyAwardEntity strategyAwardEntity = iRedisService.getValue(cacheKey);
        if(strategyAwardEntity!=null) return strategyAwardEntity;

        //redis缓存为空，查询数据库
        StrategyAward strategyAwardReq=new StrategyAward();
        strategyAwardReq.setStrategyId(strategyId);
        strategyAwardReq.setAwardId(awardId);
        StrategyAward strategyAward = iStrategyAwardDao.queryStrategyAward(strategyAwardReq);

        strategyAwardEntity=StrategyAwardEntity
                .builder()
                .strategyId(strategyAward.getStrategyId())
                .awardId(strategyAward.getAwardId())
                .awardTitle(strategyAward.getAwardTitle())
                .awardSubtitle(strategyAward.getAwardSubtitle())
                .awardCount(strategyAward.getAwardCount())
                .awardCountSurplus(strategyAward.getAwardCountSurplus())
                .awardRate(strategyAward.getAwardRate())
                .sort(strategyAward.getSort())
                .build();
        //存储策略奖品到redis中
        iRedisService.setValue(cacheKey,strategyAwardEntity);
        return strategyAwardEntity;
    }

    /**存储概率查找表到reids中*/
    @Override
    public void storeStrategyAwardSearchRateTable(String key, Integer rateRange, Map<Integer, Integer> strategyAwardSearchRateTable) {
        //将rateRange(本次抽奖范围值)存储到redis中，概率范围存储,分布式应用
        iRedisService.setValue(Constants.RedisKey.STRATEGY_RATE_RANGE_KEY+key,rateRange);

        //存储概率查找表到redis中
        Map<Integer,Integer> cacheRateTable=iRedisService.getMap(Constants.RedisKey.STRATEGY_RATE_TABLE_KEY+key);
        cacheRateTable.putAll(strategyAwardSearchRateTable);
    }

    /**将奖品的库存缓存到redis中*/
    @Override
    public void cacheStrategyAwardCount(String cacheKey, Integer awardCount) {
        if(iRedisService.isExists(cacheKey)) return;
        iRedisService.setAtomicLong(cacheKey,awardCount);
    }

    /**根据策略id和随机数从redis中抽取奖品,返回值为奖品Id*/
    @Override
    public Integer getStrategyAwardAssemble(String key, Integer rateKey) {
        //根据概率值从redis中抽取唯一存在的一个奖品
        return iRedisService.getFromMap(Constants.RedisKey.STRATEGY_RATE_TABLE_KEY+key,rateKey);
    }

    @Override
    public Integer getRateRange(Long strategyId) {
        //概率范围获取
        return getRateRange(String.valueOf(strategyId));
    }

    @Override
    public Integer getRateRange(String key) {
        String cacheKey=Constants.RedisKey.STRATEGY_RATE_RANGE_KEY+key;
        if(!iRedisService.isExists(cacheKey)){
            throw new AppException(ResponseCode.UN_ASSEMBLED_STRATEGY_ARMORY.getCode(), cacheKey +
                    Constants.COLON + ResponseCode.UN_ASSEMBLED_STRATEGY_ARMORY.getInfo());
        }
        //概率范围获取
        return iRedisService.getValue(cacheKey);
    }

    /**根据策略id查询策略实体*/
    @Override
    public StrategyEntity queryStrategyEntityByStrategyId(Long strategyId) {
        //先查redis缓存
        String cacheKey= Constants.RedisKey.STRATEGY_KEY+strategyId;
        StrategyEntity strategyEntity = iRedisService.getValue(cacheKey);
        if(strategyEntity!=null) return strategyEntity;
        Strategy strategy = iStrategyDao.queryStrategyByStrategyId(strategyId);
        strategyEntity=StrategyEntity.builder()
                .strategyId(strategy.getStrategyId())
                .strategyDesc(strategy.getStrategyDesc())
                .ruleModels(strategy.getRuleModels())
                .build();
        iRedisService.setValue(cacheKey,strategyEntity);
        return strategyEntity;
    }

    /**查询权重对应的规则实体*/
    @Override
    public StrategyRuleEntity queryStrategyRuleEntity(Long strategyId, String ruleModel) {
        StrategyRule strategyRule = iStrategyRuleDao.queryStrategyRule(strategyId, ruleModel);
        return StrategyRuleEntity.builder()
                .strategyId(strategyRule.getStrategyId())
                .awardId(strategyRule.getAwardId())
                .ruleType(strategyRule.getRuleType())
                .ruleModel(strategyRule.getRuleModel())
                .ruleValue(strategyRule.getRuleValue())
                .ruleDesc(strategyRule.getRuleDesc())
                .build();
    }

    /**查询权重和黑名单的规则值*/
    @Override
    public String queryStrategyRuleValueEntity(Long strategyId, String ruleModel) {
        return queryStrategyRuleValueEntity(strategyId,ruleModel,null);
    }

    /**根据策略id,规则模型,奖品id查询规则值*/
    @Override
    public String queryStrategyRuleValueEntity(Long strategyId, String ruleModel, Integer awardId) {
        StrategyRule strategyRule=new StrategyRule();
        strategyRule.setStrategyId(strategyId);
        strategyRule.setRuleModel(ruleModel);
        strategyRule.setAwardId(awardId);
        return iStrategyRuleDao.queryStrategyRuleValue(strategyRule);
    }

    /**根据策略id，奖品id查询策略奖品规则模型值*/
    @Override
    public StrategyAwardRuleModelVO queryStrategyAwardRuleModelVO(Long strategyId, Integer awardId) {
        StrategyAward strategyAward=new StrategyAward();
        strategyAward.setStrategyId(strategyId);
        strategyAward.setAwardId(awardId);
        String ruleModels=iStrategyAwardDao.queryStrategyAwardRuleModels(strategyAward);
        if(ruleModels==null) return null;
        return StrategyAwardRuleModelVO.builder().ruleModels(ruleModels).build();
    }

    /**根据树id查询规则树*/
    @Override
    public RuleTreeVO queryRuleTreeVOByTreeId(String treeId) {
        //先查redis缓存
        String cacheKey= Constants.RedisKey.RULE_TREE_VO_KEY+treeId;
        RuleTreeVO ruleTreeVOCache = iRedisService.getValue(cacheKey);
        if(ruleTreeVOCache!=null) return ruleTreeVOCache;

        //从数据库中获取
        RuleTree ruleTree = iRuleTreeDao.queryRuleTreeByTreeId(treeId);
        List<RuleTreeNode> ruleTreeNodes = iRuleTreeNodeDao.queryRuleTreeNodeListByTreeId(treeId);
        List<RuleTreeNodeLine> ruleTreeNodeLines = iRuleTreeNodeLineDao.queryRuleTreeNodeLineListByTreeId(treeId);

        //tree_node_line 转化为map
        Map<String, List<RuleTreeNodeLineVO>> ruleTreeNodeLineVOMap=new HashMap<>();
        for (RuleTreeNodeLine ruleTreeNodeLine : ruleTreeNodeLines) {
            RuleTreeNodeLineVO ruleTreeNodeLineVO = RuleTreeNodeLineVO
                    .builder()
                    .treeId(ruleTreeNodeLine.getTreeId())
                    .ruleNodeFrom(ruleTreeNodeLine.getRuleNodeFrom())
                    .ruleNodeTo(ruleTreeNodeLine.getRuleNodeTo())
                    .ruleLimitType(RuleLimitTypeVO.valueOf(ruleTreeNodeLine.getRuleLimitType()))
                    .ruleLimitValue(RuleLogicCheckTypeVO.valueOf(ruleTreeNodeLine.getRuleLimitValue()))
                    .build();

            List<RuleTreeNodeLineVO> ruleTreeNodeLineVOList = ruleTreeNodeLineVOMap.
                    computeIfAbsent(ruleTreeNodeLineVO.getRuleNodeFrom(), k -> new ArrayList<>());
            ruleTreeNodeLineVOList.add(ruleTreeNodeLineVO);
        }

        //tree_node转化为map
        Map<String,RuleTreeNodeVO> ruleTreeNodeVOMap =new HashMap<>();
        for (RuleTreeNode ruleTreeNode : ruleTreeNodes) {
            RuleTreeNodeVO ruleTreeNodeVO = RuleTreeNodeVO
                    .builder()
                    .treeId(ruleTreeNode.getTreeId())
                    .ruleKey(ruleTreeNode.getRuleKey())
                    .ruleDesc(ruleTreeNode.getRuleDesc())
                    .ruleValue(ruleTreeNode.getRuleValue())
                    .treeNodeLineVOList(ruleTreeNodeLineVOMap.get(ruleTreeNode.getRuleKey()))
                    .build();
            ruleTreeNodeVOMap.put(ruleTreeNode.getRuleKey(),ruleTreeNodeVO);
        }

        //构建rule_tree
        RuleTreeVO ruleTreeVO=RuleTreeVO
                .builder()
                .treeId(ruleTree.getTreeId())
                .treeName(ruleTree.getTreeName())
                .treeDesc(ruleTree.getTreeDesc())
                .treeRootRuleNode(ruleTree.getTreeNodeRuleKey())
                .treeNodeMap(ruleTreeNodeVOMap)
                .build();

        iRedisService.setValue(cacheKey,ruleTreeVO);
        return ruleTreeVO;
    }

    /**获取奖品库存消费队列*/
    @Override
    public StrategyAwardStockKeyVO takeQueueValue() {
        String cacheKey = Constants.RedisKey.STRATEGY_AWARD_COUNT_QUERY_KEY;
        RBlockingQueue<StrategyAwardStockKeyVO> destinationQueue = iRedisService.getBlockingQueue(cacheKey);
        return destinationQueue.poll();
    }

    /**更新奖品库存消耗*/
    @Override
    public void updateStrategyAwardStock(Long strategyId, Integer awardId) {
        StrategyAward strategyAward=new StrategyAward();
        strategyAward.setStrategyId(strategyId);
        strategyAward.setAwardId(awardId);
        iStrategyAwardDao.updateStrategyAwardStock(strategyAward);
    }



    /**扣减库存操作*/
    @Override
    public Boolean subtractionAwardStock(String cacheKey) {
        long surplus=iRedisService.decr(cacheKey);
        if(surplus<0){
            //库存为0，恢复为0
            iRedisService.setAtomicLong(cacheKey,0);
            return false;
        }
        String lockKey = cacheKey + Constants.UNDERLINE + surplus;
        Boolean lock = iRedisService.setNx(lockKey);
        if(!lock){
            log.info("策略奖品库存加锁失败 {}", lockKey);
        }
        return lock;
    }

    /**写入奖品库存消费队列*/
    @Override
    public void awardStockConsumeSendQueue(StrategyAwardStockKeyVO strategyAwardStockKeyVO) {
        String cacheKey=Constants.RedisKey.STRATEGY_AWARD_COUNT_QUERY_KEY;
        RBlockingQueue<StrategyAwardStockKeyVO> blockingQueue = iRedisService.getBlockingQueue(cacheKey);
        RDelayedQueue<StrategyAwardStockKeyVO> delayedQueue = iRedisService.getDelayedQueue(blockingQueue);
        delayedQueue.offer(strategyAwardStockKeyVO,3, TimeUnit.SECONDS);
    }
}
