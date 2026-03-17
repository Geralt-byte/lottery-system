/*****************************************************************/


20260315-mlei-raffle-rule-flow分支初步在数据库中持久化存储了规则树<br>
1.数据库表增加若干策略，用于新增功能测试<br>
2.big-market-types/src/main/java/com/xjtu/types/common/Constants中增加规则树rides存储对象前缀<br>
3.仓储层增加规则树对应po对象，dao接口，以及根据treeId从数据库中构建规则树的方法<br>
4.big-market-domain/src/main/java/com/xjtu/domain/strategy/model/valobj中的树实体类的treeId字段从整数类型调整为string类型<br>
n.※※※ service进行了重构，删除了filter模块，将上一章节构建好的tree模块融入了抽奖流程代替filter模块<br>
/*****************************************************************/