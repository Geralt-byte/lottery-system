#设置字符集编码
SET NAMES utf8mb4;

#创建big_market_01数据库
CREATE database if not exists `big_market_01` default character set utf8mb4 collate utf8mb4_0900_ai_ci;

#切换big_market_01数据库
use big_market_01;

#创建raffle_activity_account-------------------------------------------------

DROP TABLE IF EXISTS `raffle_activity_account`;

CREATE TABLE `raffle_activity_account`
(
    `id`                  bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `user_id`             varchar(32)         NOT NULL COMMENT '用户ID',
    `activity_id`         bigint(12)          NOT NULL COMMENT '活动ID',
    `total_count`         int(8)              NOT NULL COMMENT '总次数',
    `total_count_surplus` int(8)              NOT NULL COMMENT '总次数-剩余',
    `day_count`           int(8)              NOT NULL COMMENT '日次数',
    `day_count_surplus`   int(8)              NOT NULL COMMENT '日次数-剩余',
    `month_count`         int(8)              NOT NULL COMMENT '月次数',
    `month_count_surplus` int(8)              NOT NULL COMMENT '月次数-剩余',
    `create_time`         datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`         datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_user_id_activity_id` (`user_id`, `activity_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='抽奖活动账户表';

#创建raffle_activity_account_flow-------------------------------------------------

DROP TABLE IF EXISTS `raffle_activity_account_flow`;

CREATE TABLE `raffle_activity_account_flow`
(
    `id`           bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `user_id`      varchar(32)         NOT NULL COMMENT '用户ID',
    `activity_id`  bigint(12)          NOT NULL COMMENT '活动ID',
    `total_count`  int(8)              NOT NULL COMMENT '总次数',
    `day_count`    int(8)              NOT NULL COMMENT '日次数',
    `month_count`  int(8)              NOT NULL COMMENT '月次数',
    `flow_id`      varchar(32)         NOT NULL COMMENT '流水ID - 生成的唯一ID',
    `flow_channel` varchar(12)         NOT NULL DEFAULT 'activity' COMMENT '流水渠道（activity-活动领取、sale-购买、redeem-兑换、free-免费赠送）',
    `biz_id`       varchar(12)         NOT NULL COMMENT '业务ID（外部透传，活动ID、订单ID）',
    `create_time`  datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_flow_id` (`flow_id`),
    UNIQUE KEY `uq_biz_id` (`biz_id`),
    KEY `idx_user_id_activity_id` (`user_id`, `activity_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT='抽奖活动账户流水表';

#创建raffle_activity_order-------------------------------------------------

DROP TABLE IF EXISTS `raffle_activity_order`;

CREATE TABLE `raffle_activity_order`
(
    `id`            bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `user_id`       varchar(32)         NOT NULL COMMENT '用户ID',
    `activity_id`   bigint(12)          NOT NULL COMMENT '活动ID',
    `activity_name` varchar(64)         NOT NULL COMMENT '活动名称',
    `strategy_id`   bigint(8)           NOT NULL COMMENT '抽奖策略ID',
    `order_id`      varchar(12)         NOT NULL COMMENT '订单ID',
    `order_time`    datetime            NOT NULL COMMENT '下单时间',
    `state`         varchar(8)          NOT NULL COMMENT '订单状态（not_used、used、expire）',
    `create_time`   datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_order_id` (`order_id`),
    KEY `idx_user_id_activity_id` (`user_id`, `activity_id`, `state`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='抽奖活动单';



