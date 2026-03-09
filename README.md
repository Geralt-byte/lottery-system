md5 scaffold-lite-1.0.pom > scaffold-lite-1.0.pom.md5
sha1sum scaffold-lite-1.0.pom > scaffold-lite-1.0.pom.sha1

/*****************************************************************/
20260307-mlei-strategy-armory分支主要完成了抽奖策略的装配功能

1.docs/dev-ops/environment/redis/docker-compose.yml中部署redis容器
2.big-market-types/src/main/java/com/xjtu/types/common/Constants.java中定义了redis实体类前缀名
3.big-market-infrastructure/src/main/java/com/xjtu/infrastructure/persistent/redis中定义Redisson服务
4.big-market-app/src/main/java/com/xjtu/config中定义redis连接配置属性
5.big-market-domain/src/main/java/com/xjtu/domain/strategy/model/entity/StrategyAwardEntity.java定义抽奖策略实体类对象
6.big-market-domain/src/main/java/com/xjtu/domain/strategy/repository/IStrategyRepository.java定义redis层抽奖策略仓储接口，并完成其实现类
7.big-market-domain/src/main/java/com/xjtu/domain/strategy/service/armory定义抽奖策略业务逻辑，主要完成:初始化策略抽奖奖品配置表，存入redis；完成抽奖函数
/*****************************************************************/