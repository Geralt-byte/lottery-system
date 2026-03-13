/*****************************************************************/


20260313-mlei-raffle-chain分支使用责任链重构了抽奖规则的运行流程<br>
1.docs/dev-ops/environment/mysql/sql/big_market.sql中调整了数据库中的字段，将策略100001对应的rule_model的字段调整顺序<br>
2.仓储层增加了一条查询语句，根据策略id，规则模型查询策略规则值，对应于黑名单和权重规则的策略规则值<br>
3.big-market-domain/src/main/java/com/xjtu/domain/strategy/model/valobj中修复了bug，增加判空，修复空指针异常,100黑名单奖品有空字段<br>
4.big-market-domain/src/main/java/com/xjtu/domain/strategy/service/rule增加了新模块chain，将抽奖前规则使用责任链进行重构<br>
5.big-market-domain/src/main/java/com/xjtu/domain/strategy/service/rule/filter/factory/DefaultLogicFactory的枚举类中删除两个字段，对应抽奖前规则<br>
6.big-market-domain/src/main/java/com/xjtu/domain/strategy/service/rule/filter/impl中删除两个抽奖前的实体类<br>
7.※※※ big-market-domain/src/main/java/com/xjtu/domain/strategy/service/rule/chain中构建责任链，将抽奖前规则使用责任链控制<br>
/*****************************************************************/