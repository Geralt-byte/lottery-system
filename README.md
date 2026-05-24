# Big Market 大型抽奖营销系统

**[中文](README.md)** | **[English](README_EN.md)**

## 项目概述

Big Market是一个基于DDD（领域驱动设计）架构设计的大型抽奖营销系统，采用微服务分层架构，实现了策略化的抽奖活动管理。系统支持多种抽奖策略、规则引擎、返利机制和分布式部署，为企业提供完整的营销活动解决方案。

## 核心特性

- **DDD分层架构**：严格按照领域驱动设计原则，实现四层架构分离
- **策略化抽奖**：支持多种抽奖策略配置，包括权重、黑名单、解锁规则等
- **规则引擎**：内置责任链规则引擎和决策树规则引擎
- **分库分表**：基于用户ID的数据库分片，支持高并发场景
- **消息驱动**：集成RabbitMQ实现异步消息处理
- **缓存优化**：基于Redisson的分布式缓存方案
- **动态线程池**：支持动态配置和监控的线程池管理
- **返利机制**：支持日常行为返利（签到、支付等）
- **库存管理**：实时库存扣减和预警机制

## 技术栈

### 核心框架
- **Spring Boot**: 2.7.12
- **Spring**: 基于Spring生态的完整解决方案
- **MyBatis**: 2.1.4 持久层框架

### 数据存储
- **MySQL**: 8.0.22 关系型数据库
- **Redis**: Redisson 3.26.0 分布式缓存
- **RabbitMQ**: 3.2.0 消息队列

### 工具库
- **Lombok**: 1.18.26 简化Java代码
- **FastJSON**: 2.0.28 JSON处理
- **Guava**: 32.1.3-jre Google工具库
- **Apache Commons**: 3.9 工具类库

### 中间件
- **动态线程池**: 自研动态线程池管理组件
- **数据库路由**: db-router-spring-boot-starter 1.0.2 分库分表组件

## 项目架构

### DDD四层架构

```
big-market/
├── big-market-app/              # 应用层（启动入口、配置）
├── big-market-trigger/          # 触发层（接口、事件监听）
├── big-market-domain/           # 领域层（核心业务逻辑）
├── big-market-infrastructure/   # 基础设施层（数据持久化）
├── big-market-api/              # API层（接口定义）
└── big-market-types/            # 类型层（通用类型、枚举）
```

### 各模块职责

#### 1. big-market-app（应用层）
- **职责**：应用启动入口、配置管理、依赖组装
- **主要组件**：
  - `Application.java`: Spring Boot启动类
  - `config/`: 配置类（Redis、线程池、Guava等）
- **依赖**：trigger、infrastructure

#### 2. big-market-trigger（触发层）
- **职责**：对外接口、事件监听、定时任务
- **主要组件**：
  - `http/`: REST控制器
    - `RaffleActivityController.java`: 活动抽奖接口
    - `RaffleStrategyController.java`: 策略管理接口
  - `listener/`: 消息监听器
    - `ActivitySkuStockZeroCustomer.java`: SKU库存预警
    - `AwardStockCustomer.java`: 奖品库存处理
    - `SendAwardCustomer.java`: 发奖消息处理
  - `job/`: 定时任务
    - `UpdateAwardStockJob.java`: 奖品库存更新
    - `UpdateActivitySkuStockJob.java`: 活动SKU库存更新
- **依赖**：domain、api、types

#### 3. big-market-domain（领域层）
- **职责**：核心业务逻辑、领域模型、领域服务
- **子域结构**：
  - `activity/`: 活动聚合
    - `service/`: 活动服务
    - `model/entity/`: 活动实体
    - `model/aggregate/`: 活动聚合
    - `model/valobj/`: 值对象
    - `repository/`: 仓储接口
  - `strategy/`: 策略聚合
    - `service/`: 策略服务
    - `service/rule/chain/`: 责任链规则引擎
    - `service/rule/tree/`: 决策树规则引擎
    - `service/armory/`: 策略装配器
    - `model/entity/`: 策略实体
    - `model/valobj/`: 策略值对象
  - `award/`: 奖品聚合
  - `rebate/`: 返利聚合
  - `task/`: 任务聚合
- **依赖**：types

#### 4. big-market-infrastructure（基础设施层）
- **职责**：数据持久化、外部服务调用
- **主要组件**：
  - `persistent/`: 持久化
    - `dao/`: 数据访问对象
    - `po/`: 持久化对象
    - `redis/`: Redis服务实现
  - `repository/`: 仓储实现
  - `gateway/`: 外部网关
- **依赖**：domain

#### 5. big-market-api（API层）
- **职责**：接口定义、数据传输对象
- **主要组件**：
  - `api/`: 服务接口定义
  - `dto/`: 数据传输对象

#### 6. big-market-types（类型层）
- **职责**：通用类型、枚举、异常定义
- **主要组件**：
  - `enums/`: 枚举定义
  - `exception/`: 异常类
  - `model/`: 通用模型（如Response）

## 领域建模分析

### 核心领域

#### 1. 策略域（Strategy Domain）

**核心概念**：
- **策略（Strategy）**: 抽象的抽奖规则集合
- **策略奖品（StrategyAward）**: 策略下的具体奖品配置
- **策略规则（StrategyRule）**: 策略的执行规则

**实体关系**：
```
Strategy (1) ──────── (*) StrategyAward
    │                        │
    │                        ├── Award (奖品)
    │                        └── StrategyRule (规则)
    │
    └── RuleTree (规则树)
            ├── RuleTreeNode (规则节点)
            └── RuleTreeNodeLine (规则连线)
```

**关键服务**：
- `IRaffleStrategy`: 执行抽奖核心逻辑
- `IRaffleAward`: 奖品查询和管理
- `IRaffleRule`: 规则引擎执行
- `IStrategyArmory`: 策略装配和缓存预热

#### 2. 活动域（Activity Domain）

**核心概念**：
- **活动（Activity）**: 具体的营销活动
- **活动SKU（ActivitySku）**: 活动的商品库存单位
- **活动账户（ActivityAccount）**: 用户活动账户
- **活动订单（ActivityOrder）**: 用户参与记录

**实体关系**：
```
Activity (1) ──────── (*) ActivitySku
    │                    │
    │                    ├── ActivityCount (次数配置)
    │                    └── Strategy (关联策略)
    │
    ├── ActivityAccount (用户账户)
    │   ├── ActivityAccountDay (日账户)
    │   └── ActivityAccountMonth (月账户)
    │
    └── UserRaffleOrder (用户抽奖订单)
```

**关键服务**：
- `IRaffleActivityAccountQuotaService`: 账户额度管理
- `IRaffleActivityPartakeService`: 活动参与服务
- `IRaffleActivitySkuStockService`: SKU库存管理

#### 3. 奖品域（Award Domain）

**核心概念**：
- **奖品（Award）**: 抽奖奖品定义
- **用户中奖记录（UserAwardRecord）**: 用户中奖记录

**关键服务**：
- `IAwardService`: 奖品发放服务

#### 4. 返利域（Rebate Domain）

**核心概念**：
- **日常行为返利（BehaviorRebate）**: 用户行为返利配置
- **返利订单（RebateOrder）**: 返利执行记录

**关键服务**：
- `IBehaviorRebateService`: 行为返利服务

### 设计模式应用

#### 1. 责任链模式（Chain of Responsibility）
- **应用场景**：抽奖规则执行
- **实现类**：
  - `ILogicChain`: 责任链接口
  - `AbstractLogicChain`: 抽象责任链
  - `BlackListLogicChain`: 黑名单规则
  - `RuleWeightLogicChain`: 权重规则
  - `DefaultLogicChain`: 默认规则

#### 2. 工厂模式（Factory Pattern）
- **应用场景**：规则链和决策树创建
- **实现类**：
  - `DefaultChainFactory`: 责任链工厂
  - `DefaultTreeFactory`: 决策树工厂

#### 3. 策略模式（Strategy Pattern）
- **应用场景**：不同的抽奖策略执行
- **实现**：通过策略ID加载不同的策略配置

#### 4. 仓储模式（Repository Pattern）
- **应用场景**：数据访问抽象
- **实现**：
  - 领域层定义仓储接口
  - 基础设施层实现仓储

## 数据库设计

### 数据库分片策略

**分片规则**：
- 分库数量：2个（db01, db02）
- 分表数量：每个库4张表
- 分片键：userId
- 路由算法：userId哈希值 % 库数量

### 核心表结构

#### 1. 策略相关表

##### award（奖品表）
```sql
CREATE TABLE `award` (
    `id`           int(11) unsigned NOT NULL AUTO_INCREMENT,
    `award_id`     int(8)           NOT NULL COMMENT '奖品ID',
    `award_key`    varchar(32)      NOT NULL COMMENT '奖品标识',
    `award_config` varchar(32)      NOT NULL COMMENT '奖品配置',
    `award_desc`   varchar(128)     NOT NULL COMMENT '奖品描述',
    `create_time`  datetime         NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`  datetime         NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;
```

##### strategy（策略表）
```sql
CREATE TABLE `strategy` (
    `id`            bigint(11) unsigned NOT NULL AUTO_INCREMENT,
    `strategy_id`   bigint(8)           NOT NULL COMMENT '策略ID',
    `strategy_desc` varchar(128)        NOT NULL COMMENT '策略描述',
    `rule_models`   varchar(256)                 DEFAULT NULL COMMENT '规则模型',
    `create_time`   datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`   datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_strategy_id` (`strategy_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;
```

##### strategy_award（策略奖品表）
```sql
CREATE TABLE `strategy_award` (
    `id`                  bigint(11) unsigned NOT NULL AUTO_INCREMENT,
    `strategy_id`         bigint(8)           NOT NULL,
    `award_id`            int(8)              NOT NULL,
    `award_title`         varchar(128)        NOT NULL,
    `award_subtitle`      varchar(128)                 DEFAULT NULL,
    `award_count`         int(8)              NOT NULL DEFAULT '0',
    `award_count_surplus` int(8)              NOT NULL DEFAULT '0',
    `award_rate`          decimal(6, 4)       NOT NULL COMMENT '中奖概率',
    `rule_models`         varchar(256)                 DEFAULT NULL,
    `sort`                int(2)              NOT NULL DEFAULT '0',
    `create_time`         datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`         datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_strategy_id_award_id` (`strategy_id`, `award_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;
```

##### strategy_rule（策略规则表）
```sql
CREATE TABLE `strategy_rule` (
    `id`          bigint(11) unsigned NOT NULL AUTO_INCREMENT,
    `strategy_id` int(8)              NOT NULL,
    `award_id`    int(8)                       DEFAULT NULL,
    `rule_type`   tinyint(1)          NOT NULL DEFAULT '0',
    `rule_model`  varchar(16)         NOT NULL,
    `rule_value`  varchar(256)        NOT NULL,
    `rule_desc`   varchar(128)        NOT NULL,
    `create_time` datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_strategy_id_rule_model` (`strategy_id`,`rule_model`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;
```

##### 规则树相关表
```sql
-- 规则树表
CREATE TABLE `rule_tree` (
    `id`                 bigint(11) unsigned NOT NULL AUTO_INCREMENT,
    `tree_id`            varchar(32)         NOT NULL,
    `tree_name`          varchar(64)         NOT NULL,
    `tree_desc`          varchar(128)                 DEFAULT NULL,
    `tree_node_rule_key` varchar(32)         NOT NULL,
    `create_time`        datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`        datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_tree_id` (`tree_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- 规则树节点表
CREATE TABLE `rule_tree_node` (
    `id`          bigint(11) unsigned NOT NULL AUTO_INCREMENT,
    `tree_id`     varchar(32)         NOT NULL,
    `rule_key`    varchar(32)         NOT NULL,
    `rule_desc`   varchar(64)         NOT NULL,
    `rule_value`  varchar(128)                 DEFAULT NULL,
    `create_time` datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- 规则树节点边表
CREATE TABLE `rule_tree_node_line` (
    `id`               bigint(11) unsigned NOT NULL AUTO_INCREMENT,
    `tree_id`          varchar(32)         NOT NULL,
    `rule_node_from`   varchar(32)         NOT NULL,
    `rule_node_to`     varchar(32)         NOT NULL,
    `rule_limit_type`  varchar(8)          NOT NULL,
    `rule_limit_value` varchar(32)         NOT NULL,
    `create_time`      datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`      datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;
```

#### 2. 活动相关表

##### raffle_activity（活动表）
```sql
CREATE TABLE `raffle_activity` (
    `id`              bigint(11) unsigned NOT NULL AUTO_INCREMENT,
    `activity_id`     bigint(12)          NOT NULL,
    `activity_name`   varchar(64)         NOT NULL,
    `activity_desc`   varchar(128)        NOT NULL,
    `begin_date_time` datetime            NOT NULL,
    `end_date_time`   datetime            NOT NULL,
    `strategy_id`     bigint(8)           NOT NULL,
    `state`           varchar(8)          NOT NULL DEFAULT 'create',
    `create_time`     datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`     datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_activity_id` (`activity_id`),
    KEY `idx_begin_date_time` (`begin_date_time`),
    KEY `idx_end_date_time` (`end_date_time`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;
```

##### raffle_activity_sku（活动SKU表）
```sql
CREATE TABLE `raffle_activity_sku` (
    `id`                  bigint(11) unsigned NOT NULL AUTO_INCREMENT,
    `sku`                 bigint(12)          NOT NULL,
    `activity_id`         bigint(12)          NOT NULL,
    `activity_count_id`   bigint(12)          NOT NULL,
    `stock_count`         int(11)             NOT NULL,
    `stock_count_surplus` int(11)             NOT NULL,
    `create_time`         datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`         datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_sku` (`sku`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;
```

##### raffle_activity_account（用户活动账户表）
```sql
CREATE TABLE `raffle_activity_account` (
    `id`                  bigint(11) unsigned NOT NULL AUTO_INCREMENT,
    `user_id`             varchar(32)         NOT NULL,
    `activity_id`         bigint(12)          NOT NULL,
    `total_count`         int(8)              NOT NULL,
    `total_count_surplus` int(8)              NOT NULL,
    `day_count`           int(8)              NOT NULL,
    `day_count_surplus`   int(8)              NOT NULL,
    `month_count`         int(8)              NOT NULL,
    `month_count_surplus` int(8)              NOT NULL,
    `create_time`         datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`         datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_user_id_activity_id` (`user_id`, `activity_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;
```

##### raffle_activity_account_day（用户日账户表）
```sql
CREATE TABLE `raffle_activity_account_day` (
    `id`                bigint(11) unsigned NOT NULL AUTO_INCREMENT,
    `user_id`           varchar(32)         NOT NULL,
    `activity_id`       bigint(12)          NOT NULL,
    `day`               varchar(10)         NOT NULL,
    `day_count`         int(8)              NOT NULL,
    `day_count_surplus` int(8)              NOT NULL,
    `create_time`       datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`       datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_user_id_activity_id_day` (`user_id`, `activity_id`, `day`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;
```

#### 3. 返利相关表

##### daily_behavior_rebate（日常行为返利表）
```sql
CREATE TABLE `daily_behavior_rebate` (
    `id`            int(11) unsigned NOT NULL AUTO_INCREMENT,
    `behavior_type` varchar(16)      NOT NULL COMMENT '行为类型',
    `rebate_desc`   varchar(128)     NOT NULL,
    `rebate_type`   varchar(16)      NOT NULL COMMENT '返利类型',
    `rebate_config` varchar(32)      NOT NULL,
    `state`         varchar(12)      NOT NULL,
    `create_time`   datetime         NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`   datetime         NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_behavior_type` (`behavior_type`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;
```

## 如何使用

### 环境要求

- **JDK**: 1.8+
- **Maven**: 3.6+
- **MySQL**: 8.0+
- **Redis**: 5.0+
- **RabbitMQ**: 3.8+

### 安装步骤

#### 1. 克隆项目
```bash
git clone <repository-url>
cd big-market
```

#### 2. 数据库初始化

执行数据库脚本：
```bash
# 创建数据库
mysql -u root -p < docs/dev-ops/mysql/big_market.sql
mysql -u root -p < docs/dev-ops/mysql/big_market_01.sql
mysql -u root -p < docs/dev-ops/mysql/big_market_02.sql
```

#### 3. 配置修改

修改 `big-market-app/src/main/resources/application.yml` 中的配置：

```yaml
# 数据库配置
mini-db-router:
  jdbc:
    datasource:
      dbCount: 2        # 分库数量
      tbCount: 4        # 分表数量
      routerKey: userId # 路由键
      list: db01,db02
      db01:
        url: jdbc:mysql://your-host:13306/big_market_01?...
        username: your-username
        password: your-password
      db02:
        url: jdbc:mysql://your-host:13306/big_market_02?...
        username: your-username
        password: your-password

# Redis配置
redis:
  sdk:
    config:
      host: your-host
      port: 16379

# RabbitMQ配置
spring:
  rabbitmq:
    addresses: your-host
    port: 5672
    username: your-username
    password: your-password

# 动态线程池配置
dynamic:
  thread:
    pool:
      config:
        enabled: true
        host: your-host
        port: 16379
```

#### 4. 编译项目
```bash
mvn clean compile
```

#### 5. 启动应用
```bash
cd big-market-app
mvn spring-boot:run
```

或者直接运行主类：
```bash
java -jar target/big-market-app.jar
```

## 核心接口文档

### 活动相关接口

#### 1. 活动装配（数据预热）
```
GET /api/v1/raffle/activity/armory?activityId=100301
```

#### 2. 活动抽奖
```
POST /api/v1/raffle/activity/draw
Content-Type: application/json

{
  "userId": "mlei",
  "activityId": 100301
}
```

#### 3. 查询用户账户额度
```
POST /api/v1/raffle/activity/query_user_activity_account
Content-Type: application/json

{
  "userId": "mlei",
  "activityId": 100301
}
```

#### 4. 日历签到返利
```
POST /api/v1/raffle/activity/calendar_sign_rebate?userId=mlei
```

#### 5. 查询是否签到
```
POST /api/v1/raffle/activity/is_calendar_sign_rebate?userId=mlei
```

### 策略相关接口

#### 1. 策略装配
```
GET /api/v1/raffle/strategy/strategy_armory?strategyId=100001
```

#### 2. 查询奖品列表
```
POST /api/v1/raffle/strategy/query_raffle_award_list
Content-Type: application/json

{
  "userId": "mlei",
  "activityId": 100301
}
```

#### 3. 随机抽奖
```
POST /api/v1/raffle/strategy/random_raffle
Content-Type: application/json

{
  "strategyId": 100001
}
```

#### 4. 查询权重规则
```
POST /api/v1/raffle/strategy/query_raffle_strategy_rule_weight
Content-Type: application/json

{
  "userId": "mlei",
  "activityId": 100301
}
```

## 部署指南

### 开发环境部署

#### 1. 使用Maven打包
```bash
# 开发环境
mvn clean package -Pdev

# 测试环境
mvn clean package -Ptest
```

#### 2. 运行JAR包
```bash
java -jar big-market-app/target/big-market-app.jar --spring.profiles.active=dev
```

### 生产环境部署

#### 1. 生产环境打包
```bash
mvn clean package -Pprod
```

#### 2. 生产环境启动参数
```bash
java -Xms6G -Xmx6G -server \
     -XX:MaxPermSize=256M \
     -Xss256K \
     -Dspring.profiles.active=release \
     -XX:+DisableExplicitGC \
     -XX:+UseG1GC \
     -XX:LargePageSizeInBytes=128m \
     -XX:+UseFastAccessorMethods \
     -XX:+HeapDumpOnOutOfMemoryError \
     -XX:HeapDumpPath=/export/Logs/big-market-boot \
     -Xloggc:/export/Logs/big-market-boot/gc-big-market-boot.log \
     -XX:+PrintGCDetails \
     -XX:+PrintGCDateStamps \
     -jar big-market-app.jar
```

### Docker部署

#### 1. 构建Docker镜像
```dockerfile
FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY big-market-app/target/big-market-app.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

```bash
docker build -t big-market:latest .
```

#### 2. 运行容器
```bash
docker run -d \
  --name big-market \
  -p 8091:8091 \
  -e SPRING_PROFILES_ACTIVE=prod \
  big-market:latest
```

### 配置管理

#### 环境配置文件
- `application-dev.yml`: 开发环境配置
- `application-test.yml`: 测试环境配置
- `application-prod.yml`: 生产环境配置

#### JVM参数配置
各环境的JVM参数在根pom.xml中配置：
- **dev**: 1G内存，开发调试优化
- **test**: 1G内存，测试环境配置
- **prod**: 6G内存，生产环境优化

## 项目特性详解

### 1. 规则引擎

#### 责任链规则引擎
- 支持多个规则按顺序执行
- 每个规则独立判断是否通过
- 支持黑名单、权重等规则类型

#### 决策树规则引擎
- 基于树形结构的规则判断
- 支持复杂的条件分支
- 支持库存、解锁等业务规则

### 2. 分库分表

#### 分片策略
- **分库**: 按userId哈希值 % 2
- **分表**: 按userId哈希值 % 4
- **路由**: 自动根据userId选择数据库

#### 数据一致性
- 使用分布式事务保证数据一致性
- 支持跨库查询和事务

### 3. 缓存策略

#### Redis缓存
- 策略配置缓存
- 库存缓存
- 用户账户缓存

#### 缓存更新
- 主动更新策略配置
- 被动更新库存信息
- 定时刷新用户账户

### 4. 消息驱动

#### RabbitMQ使用
- 库存扣减消息
- 奖品发放消息
- 返利执行消息

#### 消息可靠性
- 消息持久化
- 重试机制
- 死信队列

### 5. 动态线程池

#### 线程池配置
- 核心线程数: 20
- 最大线程数: 50
- 队列大小: 5000
- 拒绝策略: CallerRunsPolicy

#### 动态调整
- 支持运行时调整参数
- 监控线程池状态
- 告警机制

## 测试指南

### 单元测试
```bash
# 运行所有测试
mvn test

# 运行指定测试类
mvn test -Dtest=RaffleActivityTest
```

### 接口测试

#### 使用curl测试
```bash
# 活动抽奖
curl -X POST http://localhost:8091/api/v1/raffle/activity/draw \
  -H "Content-Type: application/json" \
  -d '{"userId":"mlei","activityId":100301}'

# 查询账户
curl -X POST http://localhost:8091/api/v1/raffle/activity/query_user_activity_account \
  -H "Content-Type: application/json" \
  -d '{"userId":"mlei","activityId":100301}'
```

#### 使用Postman测试
导入接口文档，按接口说明进行测试。

## 监控与运维

### 日志配置
- 日志路径: `/export/Logs/big-market-boot/`
- 日志级别: INFO
- 日志格式: JSON格式

### 监控指标
- JVM内存使用
- 线程池状态
- 数据库连接池
- Redis连接状态
- 接口响应时间
- 错误率统计

### 告警配置
- 内存使用超过80%
- 线程池队列满
- 数据库连接超时
- 接口响应时间超过3s

## 常见问题

### 1. 数据库连接失败
检查数据库配置和网络连接，确保数据库服务正常。

### 2. Redis连接超时
检查Redis服务状态和网络配置，调整连接超时参数。

### 3. 分库分表路由错误
确保userId参数正确传递，检查路由算法配置。

### 4. 消息队列消费失败
检查RabbitMQ服务状态，确保消息格式正确。

## 扩展开发

### 添加新的抽奖规则
1. 在`rule/chain/impl/`下创建新的规则类
2. 继承`AbstractLogicChain`
3. 实现`logic()`方法
4. 在`DefaultChainFactory`中注册新规则

### 添加新的返利类型
1. 在`daily_behavior_rebate`表中添加配置
2. 在`BehaviorRebateService`中添加处理逻辑
3. 测试返利流程

### 扩展分库分表
1. 修改`application.yml`中的分库分表配置
2. 创建新的数据库和表
3. 更新路由算法