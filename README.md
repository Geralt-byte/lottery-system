/*****************************************************************/


20260324-mlei-activity-order-flow分支完成了订单流水入库的基础流程设计<br>
1.数据库表增加了外部透传单号，用来实现幂等性，对应po对象和实体对象同步修改<br>
2.big-market-types/src/main/java/com/xjtu/types/enums/ResponseCode增加订单号重复异常字段<br>
3.仓储增加对应所需查询和更新接口<br>
4.big-market-domain/src/main/java/com/xjtu/domain/activity增加活动对应服务层功能实现，主要采用责任链完成前置规则校验<br>
/*****************************************************************/