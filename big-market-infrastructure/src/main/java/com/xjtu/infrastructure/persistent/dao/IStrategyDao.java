package com.xjtu.infrastructure.persistent.dao;

import com.xjtu.infrastructure.persistent.po.Strategy;
import org.apache.ibatis.annotations.Mapper;

/*策略表Dao*/

@Mapper
public interface IStrategyDao {
    /*根据策略id查询策略*/
    Strategy queryStrategyByStrategyId(Long strategyId);
}
