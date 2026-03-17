/*****************************************************************/


20260316-mlei-rule-stock分支更新了库存扣减的操作<br>
1.数据库表中树节点的规则值有调整，增加了新树节点(库存扣减成功的叶节点)和节点边<br>
2.big-market-types/src/main/java/com/xjtu/types/common/Constants中增加库存和库存消费队列rides存储对象前缀<br>
3.big-market-trigger/src/main/java/com/xjtu/trigger/job中增加Job定时任务，用于从redis中读取库存消费数据并定时扣减库存<br>
4.big-market-infrastructure/src/main/java/com/xjtu/infrastructure/persistent/redis中增加了存储原子数和读取原子数的操作，用于配合库存操作；增加了setNx函数用于产生阻塞队列<br>
5.big-market-infrastructure/src/main/java/com/xjtu/infrastructure/persistent/dao/IStrategyAwardDao中增加更新库存操作<br>
6.在生成抽奖时，同时生成了库存表，存入redis中<br>
7.big-market-domain/src/main/java/com/xjtu/domain/strategy/service/rule/tree/impl中实现树节点的具体逻辑<br>
8.※※※ 本节关键是实现了异步扣减库存，从而减轻对数据库的压力，首先读取redis中的库存量，如果容量够，获取分布式锁<br>
此时将库存消费数据加入到redis缓存队列中，由定时任务按一定时间间隔进行消费，扣减库存，如果不能 获得分布式锁，那么就会进入规则树的幸运奖处理<br>
/*****************************************************************/