/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
SET NAMES utf8mb4;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE='NO_AUTO_VALUE_ON_ZERO', SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

#创建big_market_02数据库
CREATE database if not exists `big_market_02` default character set utf8mb4 collate utf8mb4_0900_ai_ci;

#切换big_market_02数据库
use big_market_02;

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

#创建raffle_activity_account_day-------------------------------------------------

DROP TABLE IF EXISTS `raffle_activity_account_day`;

CREATE TABLE `raffle_activity_account_day`
(
    `id`                bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `user_id`           varchar(32)         NOT NULL COMMENT '用户ID',
    `activity_id`       bigint(12)          NOT NULL COMMENT '活动ID',
    `day`               varchar(10)         NOT NULL COMMENT '日期（yyyy-mm-dd）',
    `day_count`         int(8)              NOT NULL COMMENT '日次数',
    `day_count_surplus` int(8)              NOT NULL COMMENT '日次数-剩余',
    `create_time`       datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`       datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_user_id_activity_id_day` (`user_id`, `activity_id`, `day`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='抽奖活动账户表-日次数';

#创建raffle_activity_account_month-------------------------------------------------

DROP TABLE IF EXISTS `raffle_activity_account_month`;

CREATE TABLE `raffle_activity_account_month`
(
    `id`                  bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `user_id`             varchar(32)         NOT NULL COMMENT '用户ID',
    `activity_id`         bigint(12)          NOT NULL COMMENT '活动ID',
    `month`               varchar(7)          NOT NULL COMMENT '月份（yyyy-mm）',
    `month_count`         int(8)              NOT NULL COMMENT '月次数',
    `month_count_surplus` int(8)              NOT NULL COMMENT '月次数-剩余',
    `create_time`         datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`         datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_user_id_activity_id_month` (`user_id`, `activity_id`, `month`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='抽奖活动账户表-月次数';

#创建raffle_activity_order_000-------------------------------------------------

DROP TABLE IF EXISTS `raffle_activity_order_000`;

CREATE TABLE `raffle_activity_order_000`
(
    `id`              bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `user_id`         varchar(32)         NOT NULL COMMENT '用户ID',
    `sku`             bigint(12)          NOT NULL COMMENT '商品sku',
    `activity_id`     bigint(12)          NOT NULL COMMENT '活动ID',
    `activity_name`   varchar(64)         NOT NULL COMMENT '活动名称',
    `strategy_id`     bigint(8)           NOT NULL COMMENT '抽奖策略ID',
    `order_id`        varchar(12)         NOT NULL COMMENT '订单ID',
    `order_time`      datetime            NOT NULL COMMENT '下单时间',
    `total_count`     int(8)              NOT NULL COMMENT '总次数',
    `day_count`       int(8)              NOT NULL COMMENT '日次数',
    `month_count`     int(8)              NOT NULL COMMENT '月次数',
    `state`           varchar(16)         NOT NULL DEFAULT 'completed' COMMENT '订单状态（completed）',
    `out_business_no` varchar(64)         NOT NULL COMMENT '业务仿重ID - 外部透传的，确保幂等',
    `create_time`     datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_order_id` (`order_id`),
    UNIQUE KEY `uq_out_business_no` (`out_business_no`),
    KEY `idx_user_id_activity_id` (`user_id`, `activity_id`, `state`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='抽奖活动单';

#创建raffle_activity_order_001-------------------------------------------------

DROP TABLE IF EXISTS `raffle_activity_order_001`;

CREATE TABLE `raffle_activity_order_001`
(
    `id`              bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `user_id`         varchar(32)         NOT NULL COMMENT '用户ID',
    `sku`             bigint(12)          NOT NULL COMMENT '商品sku',
    `activity_id`     bigint(12)          NOT NULL COMMENT '活动ID',
    `activity_name`   varchar(64)         NOT NULL COMMENT '活动名称',
    `strategy_id`     bigint(8)           NOT NULL COMMENT '抽奖策略ID',
    `order_id`        varchar(12)         NOT NULL COMMENT '订单ID',
    `order_time`      datetime            NOT NULL COMMENT '下单时间',
    `total_count`     int(8)              NOT NULL COMMENT '总次数',
    `day_count`       int(8)              NOT NULL COMMENT '日次数',
    `month_count`     int(8)              NOT NULL COMMENT '月次数',
    `state`           varchar(16)         NOT NULL DEFAULT 'completed' COMMENT '订单状态（completed）',
    `out_business_no` varchar(64)         NOT NULL COMMENT '业务仿重ID - 外部透传的，确保幂等',
    `create_time`     datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_order_id` (`order_id`),
    UNIQUE KEY `uq_out_business_no` (`out_business_no`),
    KEY `idx_user_id_activity_id` (`user_id`, `activity_id`, `state`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='抽奖活动单';

#创建raffle_activity_order_002-------------------------------------------------

DROP TABLE IF EXISTS `raffle_activity_order_002`;

CREATE TABLE `raffle_activity_order_002`
(
    `id`              bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `user_id`         varchar(32)         NOT NULL COMMENT '用户ID',
    `sku`             bigint(12)          NOT NULL COMMENT '商品sku',
    `activity_id`     bigint(12)          NOT NULL COMMENT '活动ID',
    `activity_name`   varchar(64)         NOT NULL COMMENT '活动名称',
    `strategy_id`     bigint(8)           NOT NULL COMMENT '抽奖策略ID',
    `order_id`        varchar(12)         NOT NULL COMMENT '订单ID',
    `order_time`      datetime            NOT NULL COMMENT '下单时间',
    `total_count`     int(8)              NOT NULL COMMENT '总次数',
    `day_count`       int(8)              NOT NULL COMMENT '日次数',
    `month_count`     int(8)              NOT NULL COMMENT '月次数',
    `state`           varchar(16)         NOT NULL DEFAULT 'completed' COMMENT '订单状态（completed）',
    `out_business_no` varchar(64)         NOT NULL COMMENT '业务仿重ID - 外部透传的，确保幂等',
    `create_time`     datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_order_id` (`order_id`),
    UNIQUE KEY `uq_out_business_no` (`out_business_no`),
    KEY `idx_user_id_activity_id` (`user_id`, `activity_id`, `state`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='抽奖活动单';

#创建raffle_activity_order_003-------------------------------------------------

DROP TABLE IF EXISTS `raffle_activity_order_003`;

CREATE TABLE `raffle_activity_order_003`
(
    `id`              bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `user_id`         varchar(32)         NOT NULL COMMENT '用户ID',
    `sku`             bigint(12)          NOT NULL COMMENT '商品sku',
    `activity_id`     bigint(12)          NOT NULL COMMENT '活动ID',
    `activity_name`   varchar(64)         NOT NULL COMMENT '活动名称',
    `strategy_id`     bigint(8)           NOT NULL COMMENT '抽奖策略ID',
    `order_id`        varchar(12)         NOT NULL COMMENT '订单ID',
    `order_time`      datetime            NOT NULL COMMENT '下单时间',
    `total_count`     int(8)              NOT NULL COMMENT '总次数',
    `day_count`       int(8)              NOT NULL COMMENT '日次数',
    `month_count`     int(8)              NOT NULL COMMENT '月次数',
    `state`           varchar(16)         NOT NULL DEFAULT 'completed' COMMENT '订单状态（completed）',
    `out_business_no` varchar(64)         NOT NULL COMMENT '业务仿重ID - 外部透传的，确保幂等',
    `create_time`     datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_order_id` (`order_id`),
    UNIQUE KEY `uq_out_business_no` (`out_business_no`),
    KEY `idx_user_id_activity_id` (`user_id`, `activity_id`, `state`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='抽奖活动单';

#创建task-------------------------------------------------

DROP TABLE IF EXISTS `task`;

CREATE TABLE `task`
(
    `id`          bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `user_id`     varchar(32)         NOT NULL COMMENT '用户id',
    `topic`       varchar(32)         NOT NULL COMMENT '消息主题',
    `message_id`  varchar(11)                  DEFAULT NULL COMMENT '消息编号',
    `message`     varchar(512)        NOT NULL COMMENT '消息主体',
    `state`       varchar(16)         NOT NULL DEFAULT 'create' COMMENT '任务状态；create-创建、completed-完成、fail-失败',
    `create_time` datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    unique key `uq_message_id` (`message_id`),
    key `idx_state` (`state`),
    key `idx_create_time` (`create_time`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='任务表，发送MQ';

#创建 user_award_record_000-------------------------------------------------

DROP TABLE IF EXISTS `user_award_record_000`;

CREATE TABLE `user_award_record_000`
(
    `id`          bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `user_id`     varchar(32)         NOT NULL COMMENT '用户ID',
    `activity_id` bigint(12)          NOT NULL COMMENT '活动ID',
    `strategy_id` bigint(8)           NOT NULL COMMENT '抽奖策略ID',
    `order_id`    varchar(12)         NOT NULL COMMENT '抽奖订单ID【作为幂等使用】',
    `award_id`    int(11)             NOT NULL COMMENT '奖品ID',
    `award_title` varchar(128)        NOT NULL COMMENT '奖品标题（名称）',
    `award_time`  datetime            NOT NULL COMMENT '中奖时间',
    `award_state` varchar(16)         NOT NULL DEFAULT 'create' COMMENT '奖品状态；create-创建、completed-发奖完成',
    `create_time` datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_order_id` (`order_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_activity_id` (`activity_id`),
    KEY `idx_strategy_id` (`strategy_id`),
    KEY `idx_award_id` (`award_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户中奖记录表';

#创建 user_award_record_001-------------------------------------------------

DROP TABLE IF EXISTS `user_award_record_001`;

CREATE TABLE `user_award_record_001`
(
    `id`          bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `user_id`     varchar(32)         NOT NULL COMMENT '用户ID',
    `activity_id` bigint(12)          NOT NULL COMMENT '活动ID',
    `strategy_id` bigint(8)           NOT NULL COMMENT '抽奖策略ID',
    `order_id`    varchar(12)         NOT NULL COMMENT '抽奖订单ID【作为幂等使用】',
    `award_id`    int(11)             NOT NULL COMMENT '奖品ID',
    `award_title` varchar(128)        NOT NULL COMMENT '奖品标题（名称）',
    `award_time`  datetime            NOT NULL COMMENT '中奖时间',
    `award_state` varchar(16)         NOT NULL DEFAULT 'create' COMMENT '奖品状态；create-创建、completed-发奖完成',
    `create_time` datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_order_id` (`order_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_activity_id` (`activity_id`),
    KEY `idx_strategy_id` (`strategy_id`),
    KEY `idx_award_id` (`award_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户中奖记录表';

#创建 user_award_record_002-------------------------------------------------

DROP TABLE IF EXISTS `user_award_record_002`;

CREATE TABLE `user_award_record_002`
(
    `id`          bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `user_id`     varchar(32)         NOT NULL COMMENT '用户ID',
    `activity_id` bigint(12)          NOT NULL COMMENT '活动ID',
    `strategy_id` bigint(8)           NOT NULL COMMENT '抽奖策略ID',
    `order_id`    varchar(12)         NOT NULL COMMENT '抽奖订单ID【作为幂等使用】',
    `award_id`    int(11)             NOT NULL COMMENT '奖品ID',
    `award_title` varchar(128)        NOT NULL COMMENT '奖品标题（名称）',
    `award_time`  datetime            NOT NULL COMMENT '中奖时间',
    `award_state` varchar(16)         NOT NULL DEFAULT 'create' COMMENT '奖品状态；create-创建、completed-发奖完成',
    `create_time` datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_order_id` (`order_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_activity_id` (`activity_id`),
    KEY `idx_strategy_id` (`strategy_id`),
    KEY `idx_award_id` (`award_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户中奖记录表';

#创建 user_award_record_003-------------------------------------------------

DROP TABLE IF EXISTS `user_award_record_003`;

CREATE TABLE `user_award_record_003`
(
    `id`          bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `user_id`     varchar(32)         NOT NULL COMMENT '用户ID',
    `activity_id` bigint(12)          NOT NULL COMMENT '活动ID',
    `strategy_id` bigint(8)           NOT NULL COMMENT '抽奖策略ID',
    `order_id`    varchar(12)         NOT NULL COMMENT '抽奖订单ID【作为幂等使用】',
    `award_id`    int(11)             NOT NULL COMMENT '奖品ID',
    `award_title` varchar(128)        NOT NULL COMMENT '奖品标题（名称）',
    `award_time`  datetime            NOT NULL COMMENT '中奖时间',
    `award_state` varchar(16)         NOT NULL DEFAULT 'create' COMMENT '奖品状态；create-创建、completed-发奖完成',
    `create_time` datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_order_id` (`order_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_activity_id` (`activity_id`),
    KEY `idx_strategy_id` (`strategy_id`),
    KEY `idx_award_id` (`award_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户中奖记录表';

#创建 user_raffle_order_000-------------------------------------------------

DROP TABLE IF EXISTS `user_raffle_order_000`;

CREATE TABLE `user_raffle_order_000`
(
    `id`            bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `user_id`       varchar(32)         NOT NULL COMMENT '用户ID',
    `activity_id`   bigint(12)          NOT NULL COMMENT '活动ID',
    `activity_name` varchar(64)         NOT NULL COMMENT '活动名称',
    `strategy_id`   bigint(8)           NOT NULL COMMENT '抽奖策略ID',
    `order_id`      varchar(12)         NOT NULL COMMENT '订单ID',
    `order_time`    datetime            NOT NULL COMMENT '下单时间',
    `order_state`   varchar(16)         NOT NULL DEFAULT 'create' COMMENT '订单状态；create-创建、used-已使用、cancel-已作废',
    `create_time`   datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_order_id` (`order_id`),
    KEY `idx_user_id_activity_id` (`user_id`, `activity_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户抽奖订单表';

#创建 user_raffle_order_001-------------------------------------------------

DROP TABLE IF EXISTS `user_raffle_order_001`;

CREATE TABLE `user_raffle_order_001`
(
    `id`            bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `user_id`       varchar(32)         NOT NULL COMMENT '用户ID',
    `activity_id`   bigint(12)          NOT NULL COMMENT '活动ID',
    `activity_name` varchar(64)         NOT NULL COMMENT '活动名称',
    `strategy_id`   bigint(8)           NOT NULL COMMENT '抽奖策略ID',
    `order_id`      varchar(12)         NOT NULL COMMENT '订单ID',
    `order_time`    datetime            NOT NULL COMMENT '下单时间',
    `order_state`   varchar(16)         NOT NULL DEFAULT 'create' COMMENT '订单状态；create-创建、used-已使用、cancel-已作废',
    `create_time`   datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_order_id` (`order_id`),
    KEY `idx_user_id_activity_id` (`user_id`, `activity_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户抽奖订单表';

#创建 user_raffle_order_002-------------------------------------------------

DROP TABLE IF EXISTS `user_raffle_order_002`;

CREATE TABLE `user_raffle_order_002`
(
    `id`            bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `user_id`       varchar(32)         NOT NULL COMMENT '用户ID',
    `activity_id`   bigint(12)          NOT NULL COMMENT '活动ID',
    `activity_name` varchar(64)         NOT NULL COMMENT '活动名称',
    `strategy_id`   bigint(8)           NOT NULL COMMENT '抽奖策略ID',
    `order_id`      varchar(12)         NOT NULL COMMENT '订单ID',
    `order_time`    datetime            NOT NULL COMMENT '下单时间',
    `order_state`   varchar(16)         NOT NULL DEFAULT 'create' COMMENT '订单状态；create-创建、used-已使用、cancel-已作废',
    `create_time`   datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_order_id` (`order_id`),
    KEY `idx_user_id_activity_id` (`user_id`, `activity_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户抽奖订单表';

#创建 user_raffle_order_003-------------------------------------------------

DROP TABLE IF EXISTS `user_raffle_order_003`;

CREATE TABLE `user_raffle_order_003`
(
    `id`            bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `user_id`       varchar(32)         NOT NULL COMMENT '用户ID',
    `activity_id`   bigint(12)          NOT NULL COMMENT '活动ID',
    `activity_name` varchar(64)         NOT NULL COMMENT '活动名称',
    `strategy_id`   bigint(8)           NOT NULL COMMENT '抽奖策略ID',
    `order_id`      varchar(12)         NOT NULL COMMENT '订单ID',
    `order_time`    datetime            NOT NULL COMMENT '下单时间',
    `order_state`   varchar(16)         NOT NULL DEFAULT 'create' COMMENT '订单状态；create-创建、used-已使用、cancel-已作废',
    `create_time`   datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_order_id` (`order_id`),
    KEY `idx_user_id_activity_id` (`user_id`, `activity_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户抽奖订单表';

# 创建 user_behavior_rebate_order_000-------------------------------------------------

DROP TABLE IF EXISTS `user_behavior_rebate_order_000`;

CREATE TABLE `user_behavior_rebate_order_000`
(
    `id`            int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `user_id`       varchar(32)      NOT NULL COMMENT '用户ID',
    `order_id`      varchar(12)      NOT NULL COMMENT '订单ID',
    `behavior_type` varchar(16)      NOT NULL COMMENT '行为类型（sign 签到、openai_pay 支付）',
    `rebate_desc`   varchar(128)     NOT NULL COMMENT '返利描述',
    `rebate_type`   varchar(16)      NOT NULL COMMENT '返利类型（sku 活动库存充值商品、integral 用户活动积分）',
    `rebate_config` varchar(32)      NOT NULL COMMENT '返利配置【sku值，积分值】',
    `biz_id`        varchar(64)      NOT NULL COMMENT '业务ID - 拼接的唯一值',
    `create_time`   datetime         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   datetime         NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_order_id` (`order_id`),
    UNIQUE KEY `uq_biz_id` (`biz_id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户行为返利流水订单表';

# 创建 user_behavior_rebate_order_001-------------------------------------------------

DROP TABLE IF EXISTS `user_behavior_rebate_order_001`;

CREATE TABLE `user_behavior_rebate_order_001`
(
    `id`            int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `user_id`       varchar(32)      NOT NULL COMMENT '用户ID',
    `order_id`      varchar(12)      NOT NULL COMMENT '订单ID',
    `behavior_type` varchar(16)      NOT NULL COMMENT '行为类型（sign 签到、openai_pay 支付）',
    `rebate_desc`   varchar(128)     NOT NULL COMMENT '返利描述',
    `rebate_type`   varchar(16)      NOT NULL COMMENT '返利类型（sku 活动库存充值商品、integral 用户活动积分）',
    `rebate_config` varchar(32)      NOT NULL COMMENT '返利配置【sku值，积分值】',
    `biz_id`        varchar(64)      NOT NULL COMMENT '业务ID - 拼接的唯一值',
    `create_time`   datetime         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   datetime         NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_order_id` (`order_id`),
    UNIQUE KEY `uq_biz_id` (`biz_id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户行为返利流水订单表';

# 创建 user_behavior_rebate_order_002-------------------------------------------------

DROP TABLE IF EXISTS `user_behavior_rebate_order_002`;

CREATE TABLE `user_behavior_rebate_order_002`
(
    `id`            int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `user_id`       varchar(32)      NOT NULL COMMENT '用户ID',
    `order_id`      varchar(12)      NOT NULL COMMENT '订单ID',
    `behavior_type` varchar(16)      NOT NULL COMMENT '行为类型（sign 签到、openai_pay 支付）',
    `rebate_desc`   varchar(128)     NOT NULL COMMENT '返利描述',
    `rebate_type`   varchar(16)      NOT NULL COMMENT '返利类型（sku 活动库存充值商品、integral 用户活动积分）',
    `rebate_config` varchar(32)      NOT NULL COMMENT '返利配置【sku值，积分值】',
    `biz_id`        varchar(64)      NOT NULL COMMENT '业务ID - 拼接的唯一值',
    `create_time`   datetime         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   datetime         NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_order_id` (`order_id`),
    UNIQUE KEY `uq_biz_id` (`biz_id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户行为返利流水订单表';

# 创建 user_behavior_rebate_order_003-------------------------------------------------

DROP TABLE IF EXISTS `user_behavior_rebate_order_003`;

CREATE TABLE `user_behavior_rebate_order_003`
(
    `id`            int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `user_id`       varchar(32)      NOT NULL COMMENT '用户ID',
    `order_id`      varchar(12)      NOT NULL COMMENT '订单ID',
    `behavior_type` varchar(16)      NOT NULL COMMENT '行为类型（sign 签到、openai_pay 支付）',
    `rebate_desc`   varchar(128)     NOT NULL COMMENT '返利描述',
    `rebate_type`   varchar(16)      NOT NULL COMMENT '返利类型（sku 活动库存充值商品、integral 用户活动积分）',
    `rebate_config` varchar(32)      NOT NULL COMMENT '返利配置【sku值，积分值】',
    `biz_id`        varchar(64)      NOT NULL COMMENT '业务ID - 拼接的唯一值',
    `create_time`   datetime         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   datetime         NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_order_id` (`order_id`),
    UNIQUE KEY `uq_biz_id` (`biz_id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户行为返利流水订单表';

/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;



