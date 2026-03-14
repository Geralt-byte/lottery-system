/*****************************************************************/


20260314-mlei-rule-tree分支初步构建了决策树<br>
1.big-market-domain/src/main/java/com/xjtu/domain/strategy/model/valobj中增量了若干所需值对象<br>
2.big-market-domain/src/main/java/com/xjtu/domain/strategy/service/rule/tree中增加决策树模块<br>
3.※※※ 实现一个接口定义逻辑决策树节点，并定义多个实体类实现；定义引擎接口和引擎实现类完成对决策树的执行流程，使用工厂装配决策树节点和引擎<br>
/*****************************************************************/