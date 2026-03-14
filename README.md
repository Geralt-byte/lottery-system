/*****************************************************************/


20260314-raffle-chain-debug分支修复了若干bug<br>
1.big-market-domain/src/main/java/com/xjtu/domain/strategy/service/armory/StrategyArmoryDispatch中修改概率表的计算方法，使概率表不会出现小数<br>
2.big-market-domain/src/main/java/com/xjtu/domain/strategy/service/rule/chain/impl/RuleWeightLogicChain中对规则权重值增加判空操作<br>
3.big-market-domain/src/main/java/com/xjtu/domain/strategy/service/rule/filter/impl/RuleLockLogicFilter.java中对抽奖次数锁配置的规则值进行判空操作<br>
/*****************************************************************/