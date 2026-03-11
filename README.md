/*****************************************************************/


20260311-mlei-raffle-rule-before分支主要完成了抽奖策略的前置规则过滤<br>
1.docs/dev-ops/environment/mysql/sql/big_market.sql中调整了数据库中的字段，修改了黑名单策略规则的规则值,增加了黑名单奖品<br>
2.big-market-infrastructure/src/main/java/com/xjtu/infrastructure/persistent/po/StrategyRule中修复了bug，将策略id字段从Integer调整为Long<br>
3.big-market-domain/src/main/java/com/xjtu/domain/strategy/model/entity/StrategyRuleEntity中修复了bug，将策略id字段从Integer调整为Long<br>
4.仓储层增加了一条查询语句，根据策略id，规则模型，（奖品id）查询策略规则的rule_value字段<br>
5.增加了四个实体类抽奖奖品、抽奖因子、规则动作、规则物料实体对象<br>
6.big-market-domain/src/main/java/com/xjtu/domain/strategy/model/valobj增加了值对象，存储是否受前置规则接管的状态码<br>
7.big-market-domain/src/main/java/com/xjtu/domain/strategy/service/annotation增加了自定义注解<br>
8.※※※ big-market-domain/src/main/java/com/xjtu/domain/strategy/service增加raffle和rule模块<br>
9.raffle定义了抽奖流程，先进行过滤，被放行则进行普通抽奖，被接口则进行规则抽奖<br>
10.rule定义了规则过滤函数<br>
/*****************************************************************/