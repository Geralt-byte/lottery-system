/*****************************************************************/


20260313-mlei-raffle-rule-center分支主要完成了抽奖策略的中间规则过滤<br>
1.docs/dev-ops/environment/mysql/sql/big_market.sql中调整了数据库中的字段，增加了两个临时策略以便测试使用<br>
2.仓储层增加了一条查询语句，根据策略id，奖品id查询策略奖品的rule_models字段<br>
3.big-market-domain/src/main/java/com/xjtu/domain/strategy/model/entity/StrategyEntity中修复了bug，getRuleWeight()函数中增加判空，修复空指针异常<br>
4.big-market-domain/src/main/java/com/xjtu/domain/strategy/model/entity/RaffleFactorEntity增加奖品id字段，满足抽奖中规则的需求<br>
5.big-market-domain/src/main/java/com/xjtu/domain/strategy/model/valobj中增加值对象，存储规则模型值<br>
6.big-market-domain/src/main/java/com/xjtu/domain/strategy/service/rule/impl/RuleLockLogicFilter增加了抽奖次数规则过滤的实体类<br>
7.big-market-domain/src/main/java/com/xjtu/domain/strategy/service/rule/factory/DefaultLogicFactory的内部枚举类增加了类型字段和两个判断类型的函数<br>
8.※※※ big-market-domain/src/main/java/com/xjtu/domain/strategy/service/raffle模块在前置完成后，增加了对抽奖次数的规则过滤功能<br>
/*****************************************************************/