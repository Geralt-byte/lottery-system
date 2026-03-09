/*****************************************************************/


20260310-mlei-strategy-armory-rule-weight分支主要完成了抽奖策略的装配功能<br>
1.docs/dev-ops/environment/mysql/sql/big_market.sql中调整了数据库中的某些字段，包括抽奖概率、策略表增加规则模型字段等<br>
2.big-market-types/src/main/java/com/xjtu/types/common/Constants.java中增加了所需字段定义<br>
3.big-market-types/src/main/java/com/xjtu/types/exception和big-market-types/src/main/java/com/xjtu/types/enums中增加了所需异常返回值
3.根据数据库表字段，调整po中对应实体类<br>
4.dao层和mybatis的xml文件中增加所需查询函数<br>
5.big-market-infrastructure/src/main/java/com/xjtu/infrastructure/persistent/repository/StrategyRepository.java增加若干所需reids层查询存储函数，并重构上一个分支的部分代码<br>
6.entity增加若干所需实体类，在实体类中，还定义了若干和实体类强耦合的函数<br>
7.big-market-domain/src/main/java/com/xjtu/domain/strategy/service/armory中，对接口进行了拆分，策略奖品初始化和抽奖的接口分开<br>
8.※※※ big-market-domain/src/main/java/com/xjtu/domain/strategy/service/armory/StrategyArmoryDispatch中，实现了权重规则的抽奖，具体表现为，将权重规则下的抽奖奖品池存储到redis中，并定义抽奖函数<br>
/*****************************************************************/