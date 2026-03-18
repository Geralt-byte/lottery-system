/*****************************************************************/


20260318-mlei-raffle-controller分支完成了controller接口<br>
1.数据库表中树相关表有调整,增加树种类和树节点、边种类<br>
2.big-market-types/src/main/java/com/xjtu/types中增加若干所需字段或实体类<br>
3.StrategyAward、RaffleAwardEntity、RaffleFactorEntity、StrategyAwardEntity调整了若干字段<br>
4.仓储层增加根据策略和奖品查询奖品信息的方法<br>
5.配置文件配合修改<br>
6.新增big-market-api模块，用于向外部开放接口<br>
7.big-market-trigger/src/main/java/com/xjtu/trigger/http/RaffleController对外部接口进行实现<br>
/*****************************************************************/