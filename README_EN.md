# Big Market Large-Scale Raffle Marketing System

**[English](README_EN.md)** | **[中文](README.md)**

## Project Overview

Big Market is a large-scale raffle marketing system designed based on DDD (Domain-Driven Design) architecture, adopting microservice layered architecture and implementing strategic raffle activity management. The system supports multiple raffle strategies, rule engines, rebate mechanisms, and distributed deployment, providing complete marketing activity solutions for enterprises.

## Core Features

- **DDD Layered Architecture**: Strictly following domain-driven design principles, achieving four-layer architecture separation
- **Strategic Raffle**: Supporting multiple raffle strategy configurations, including weight, blacklist, unlock rules, etc.
- **Rule Engine**: Built-in responsibility chain rule engine and decision tree rule engine
- **Database Sharding**: Database sharding based on user ID, supporting high-concurrency scenarios
- **Message-Driven**: Integrated with RabbitMQ for asynchronous message processing
- **Cache Optimization**: Distributed cache solution based on Redisson
- **Dynamic Thread Pool**: Thread pool management supporting dynamic configuration and monitoring
- **Rebate Mechanism**: Supporting daily behavior rebates (check-in, payment, etc.)
- **Inventory Management**: Real-time inventory deduction and early warning mechanisms

## Technology Stack

### Core Frameworks
- **Spring Boot**: 2.7.12
- **Spring**: Complete solution based on Spring ecosystem
- **MyBatis**: 2.1.4 Persistence framework

### Data Storage
- **MySQL**: 8.0.22 Relational database
- **Redis**: Redisson 3.26.0 Distributed cache
- **RabbitMQ**: 3.2.0 Message queue

### Utility Libraries
- **Lombok**: 1.18.26 Simplify Java code
- **FastJSON**: 2.0.28 JSON processing
- **Guava**: 32.1.3-jre Google utility library
- **Apache Commons**: 3.9 Utility library

### Middleware
- **Dynamic Thread Pool**: Self-developed dynamic thread pool management component
- **Database Routing**: db-router-spring-boot-starter 1.0.2 Database sharding component

## Project Architecture

### DDD Four-Layer Architecture

```
big-market/
├── big-market-app/              # Application layer (startup entry, configuration)
├── big-market-trigger/          # Trigger layer (interfaces, event listeners)
├── big-market-domain/           # Domain layer (core business logic)
├── big-market-infrastructure/   # Infrastructure layer (data persistence)
├── big-market-api/              # API layer (interface definitions)
└── big-market-types/            # Types layer (common types, enums)
```

### Module Responsibilities

#### 1. big-market-app (Application Layer)
- **Responsibilities**: Application startup entry, configuration management, dependency assembly
- **Main Components**:
  - `Application.java`: Spring Boot startup class
  - `config/`: Configuration classes (Redis, thread pool, Guava, etc.)
- **Dependencies**: trigger, infrastructure

#### 2. big-market-trigger (Trigger Layer)
- **Responsibilities**: External interfaces, event listeners, scheduled tasks
- **Main Components**:
  - `http/`: REST controllers
    - `RaffleActivityController.java`: Activity raffle interface
    - `RaffleStrategyController.java`: Strategy management interface
  - `listener/`: Message listeners
    - `ActivitySkuStockZeroCustomer.java`: SKU inventory warning
    - `AwardStockCustomer.java`: Award inventory processing
    - `SendAwardCustomer.java`: Award sending message processing
  - `job/`: Scheduled tasks
    - `UpdateAwardStockJob.java`: Award inventory update
    - `UpdateActivitySkuStockJob.java`: Activity SKU inventory update
- **Dependencies**: domain, api, types

#### 3. big-market-domain (Domain Layer)
- **Responsibilities**: Core business logic, domain models, domain services
- **Subdomain Structure**:
  - `activity/`: Activity aggregate
    - `service/`: Activity services
    - `model/entity/`: Activity entities
    - `model/aggregate/`: Activity aggregates
    - `model/valobj/`: Value objects
    - `repository/`: Repository interfaces
  - `strategy/`: Strategy aggregate
    - `service/`: Strategy services
    - `service/rule/chain/`: Responsibility chain rule engine
    - `service/rule/tree/`: Decision tree rule engine
    - `service/armory/`: Strategy armory
    - `model/entity/`: Strategy entities
    - `model/valobj/`: Strategy value objects
  - `award/`: Award aggregate
  - `rebate/`: Rebate aggregate
  - `task/`: Task aggregate
- **Dependencies**: types

#### 4. big-market-infrastructure (Infrastructure Layer)
- **Responsibilities**: Data persistence, external service calls
- **Main Components**:
  - `persistent/`: Persistence
    - `dao/`: Data access objects
    - `po/`: Persistent objects
    - `redis/`: Redis service implementations
  - `repository/`: Repository implementations
  - `gateway/`: External gateways
- **Dependencies**: domain

#### 5. big-market-api (API Layer)
- **Responsibilities**: Interface definitions, data transfer objects
- **Main Components**:
  - `api/`: Service interface definitions
  - `dto/`: Data transfer objects

#### 6. big-market-types (Types Layer)
- **Responsibilities**: Common types, enums, exception definitions
- **Main Components**:
  - `enums/`: Enum definitions
  - `exception/`: Exception classes
  - `model/`: Common models (such as Response)

## Domain Modeling Analysis

### Core Domains

#### 1. Strategy Domain

**Core Concepts**:
- **Strategy**: Abstract collection of raffle rules
- **StrategyAward**: Specific award configuration under a strategy
- **StrategyRule**: Execution rules of a strategy

**Entity Relationships**:
```
Strategy (1) ──────── (*) StrategyAward
    │                        │
    │                        ├── Award (Award)
    │                        └── StrategyRule (Rule)
    │
    └── RuleTree (Rule Tree)
            ├── RuleTreeNode (Rule Node)
            └── RuleTreeNodeLine (Rule Connection)
```

**Key Services**:
- `IRaffleStrategy`: Core logic for executing raffle
- `IRaffleAward`: Award query and management
- `IRaffleRule`: Rule engine execution
- `IStrategyArmory`: Strategy assembly and cache warm-up

#### 2. Activity Domain

**Core Concepts**:
- **Activity**: Specific marketing activity
- **ActivitySku**: Stock keeping unit of activity products
- **ActivityAccount**: User activity account
- **ActivityOrder**: User participation record

**Entity Relationships**:
```
Activity (1) ──────── (*) ActivitySku
    │                    │
    │                    ├── ActivityCount (Count Configuration)
    │                    └── Strategy (Associated Strategy)
    │
    ├── ActivityAccount (User Account)
    │   ├── ActivityAccountDay (Daily Account)
    │   └── ActivityAccountMonth (Monthly Account)
    │
    └── UserRaffleOrder (User Raffle Order)
```

**Key Services**:
- `IRaffleActivityAccountQuotaService`: Account quota management
- `IRaffleActivityPartakeService`: Activity participation service
- `IRaffleActivitySkuStockService`: SKU inventory management

#### 3. Award Domain

**Core Concepts**:
- **Award**: Raffle award definition
- **UserAwardRecord**: User winning record

**Key Services**:
- `IAwardService`: Award distribution service

#### 4. Rebate Domain

**Core Concepts**:
- **BehaviorRebate**: User behavior rebate configuration
- **RebateOrder**: Rebate execution record

**Key Services**:
- `IBehaviorRebateService`: Behavior rebate service

### Design Pattern Applications

#### 1. Chain of Responsibility Pattern
- **Application Scenario**: Raffle rule execution
- **Implementation Classes**:
  - `ILogicChain`: Responsibility chain interface
  - `AbstractLogicChain`: Abstract responsibility chain
  - `BlackListLogicChain`: Blacklist rule
  - `RuleWeightLogicChain`: Weight rule
  - `DefaultLogicChain`: Default rule

#### 2. Factory Pattern
- **Application Scenario**: Rule chain and decision tree creation
- **Implementation Classes**:
  - `DefaultChainFactory`: Responsibility chain factory
  - `DefaultTreeFactory`: Decision tree factory

#### 3. Strategy Pattern
- **Application Scenario**: Different raffle strategy execution
- **Implementation**: Load different strategy configurations based on strategy ID

#### 4. Repository Pattern
- **Application Scenario**: Data access abstraction
- **Implementation**:
  - Domain layer defines repository interfaces
  - Infrastructure layer implements repositories

## Database Design

### Database Sharding Strategy

**Sharding Rules**:
- Number of databases: 2 (db01, db02)
- Number of tables per database: 4
- Sharding key: userId
- Routing algorithm: userId hash value % number of databases

### Core Table Structures

#### 1. Strategy Related Tables

##### award (Award Table)
```sql
CREATE TABLE `award` (
    `id`           int(11) unsigned NOT NULL AUTO_INCREMENT,
    `award_id`     int(8)           NOT NULL COMMENT 'Award ID',
    `award_key`    varchar(32)      NOT NULL COMMENT 'Award identifier',
    `award_config` varchar(32)      NOT NULL COMMENT 'Award configuration',
    `award_desc`   varchar(128)     NOT NULL COMMENT 'Award description',
    `create_time`  datetime         NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`  datetime         NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;
```

##### strategy (Strategy Table)
```sql
CREATE TABLE `strategy` (
    `id`            bigint(11) unsigned NOT NULL AUTO_INCREMENT,
    `strategy_id`   bigint(8)           NOT NULL COMMENT 'Strategy ID',
    `strategy_desc` varchar(128)        NOT NULL COMMENT 'Strategy description',
    `rule_models`   varchar(256)                 DEFAULT NULL COMMENT 'Rule models',
    `create_time`   datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`   datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_strategy_id` (`strategy_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;
```

##### strategy_award (Strategy Award Table)
```sql
CREATE TABLE `strategy_award` (
    `id`                  bigint(11) unsigned NOT NULL AUTO_INCREMENT,
    `strategy_id`         bigint(8)           NOT NULL,
    `award_id`            int(8)              NOT NULL,
    `award_title`         varchar(128)        NOT NULL,
    `award_subtitle`      varchar(128)                 DEFAULT NULL,
    `award_count`         int(8)              NOT NULL DEFAULT '0',
    `award_count_surplus` int(8)              NOT NULL DEFAULT '0',
    `award_rate`          decimal(6, 4)       NOT NULL COMMENT 'Winning probability',
    `rule_models`         varchar(256)                 DEFAULT NULL,
    `sort`                int(2)              NOT NULL DEFAULT '0',
    `create_time`         datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`         datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_strategy_id_award_id` (`strategy_id`, `award_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;
```

##### strategy_rule (Strategy Rule Table)
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

##### Rule Tree Related Tables
```sql
-- Rule Tree Table
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

-- Rule Tree Node Table
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

-- Rule Tree Node Line Table
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

#### 2. Activity Related Tables

##### raffle_activity (Activity Table)
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

##### raffle_activity_sku (Activity SKU Table)
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

##### raffle_activity_account (User Activity Account Table)
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

##### raffle_activity_account_day (User Daily Account Table)
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

#### 3. Rebate Related Tables

##### daily_behavior_rebate (Daily Behavior Rebate Table)
```sql
CREATE TABLE `daily_behavior_rebate` (
    `id`            int(11) unsigned NOT NULL AUTO_INCREMENT,
    `behavior_type` varchar(16)      NOT NULL COMMENT 'Behavior type',
    `rebate_desc`   varchar(128)     NOT NULL,
    `rebate_type`   varchar(16)      NOT NULL COMMENT 'Rebate type',
    `rebate_config` varchar(32)      NOT NULL,
    `state`         varchar(12)      NOT NULL,
    `create_time`   datetime         NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`   datetime         NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_behavior_type` (`behavior_type`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;
```

## How to Use

### Environment Requirements

- **JDK**: 1.8+
- **Maven**: 3.6+
- **MySQL**: 8.0+
- **Redis**: 5.0+
- **RabbitMQ**: 3.8+

### Installation Steps

#### 1. Clone Project
```bash
git clone <repository-url>
cd big-market
```

#### 2. Database Initialization

Execute database scripts:
```bash
# Create databases
mysql -u root -p < docs/dev-ops/mysql/big_market.sql
mysql -u root -p < docs/dev-ops/mysql/big_market_01.sql
mysql -u root -p < docs/dev-ops/mysql/big_market_02.sql
```

#### 3. Configuration Modification

Modify the configuration in `big-market-app/src/main/resources/application.yml`:

```yaml
# Database configuration
mini-db-router:
  jdbc:
    datasource:
      dbCount: 2        # Number of databases
      tbCount: 4        # Number of tables per database
      routerKey: userId # Routing key
      list: db01,db02
      db01:
        url: jdbc:mysql://your-host:13306/big_market_01?...
        username: your-username
        password: your-password
      db02:
        url: jdbc:mysql://your-host:13306/big_market_02?...
        username: your-username
        password: your-password

# Redis configuration
redis:
  sdk:
    config:
      host: your-host
      port: 16379

# RabbitMQ configuration
spring:
  rabbitmq:
    addresses: your-host
    port: 5672
    username: your-username
    password: your-password

# Dynamic thread pool configuration
dynamic:
  thread:
    pool:
      config:
        enabled: true
        host: your-host
        port: 16379
```

#### 4. Compile Project
```bash
mvn clean compile
```

#### 5. Start Application
```bash
cd big-market-app
mvn spring-boot:run
```

Or run the main class directly:
```bash
java -jar target/big-market-app.jar
```

## Core API Documentation

### Activity Related APIs

#### 1. Activity Assembly (Data Warm-up)
```
GET /api/v1/raffle/activity/armory?activityId=100301
```

#### 2. Activity Raffle
```
POST /api/v1/raffle/activity/draw
Content-Type: application/json

{
  "userId": "mlei",
  "activityId": 100301
}
```

#### 3. Query User Account Quota
```
POST /api/v1/raffle/activity/query_user_activity_account
Content-Type: application/json

{
  "userId": "mlei",
  "activityId": 100301
}
```

#### 4. Calendar Check-in Rebate
```
POST /api/v1/raffle/activity/calendar_sign_rebate?userId=mlei
```

#### 5. Query Check-in Status
```
POST /api/v1/raffle/activity/is_calendar_sign_rebate?userId=mlei
```

### Strategy Related APIs

#### 1. Strategy Assembly
```
GET /api/v1/raffle/strategy/strategy_armory?strategyId=100001
```

#### 2. Query Award List
```
POST /api/v1/raffle/strategy/query_raffle_award_list
Content-Type: application/json

{
  "userId": "mlei",
  "activityId": 100301
}
```

#### 3. Random Raffle
```
POST /api/v1/raffle/strategy/random_raffle
Content-Type: application/json

{
  "strategyId": 100001
}
```

#### 4. Query Weight Rules
```
POST /api/v1/raffle/strategy/query_raffle_strategy_rule_weight
Content-Type: application/json

{
  "userId": "mlei",
  "activityId": 100301
}
```

## Deployment Guide

### Development Environment Deployment

#### 1. Package with Maven
```bash
# Development environment
mvn clean package -Pdev

# Test environment
mvn clean package -Ptest
```

#### 2. Run JAR Package
```bash
java -jar big-market-app/target/big-market-app.jar --spring.profiles.active=dev
```

### Production Environment Deployment

#### 1. Production Environment Packaging
```bash
mvn clean package -Pprod
```

#### 2. Production Environment Startup Parameters
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

### Docker Deployment

#### 1. Build Docker Image
```dockerfile
FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY big-market-app/target/big-market-app.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

```bash
docker build -t big-market:latest .
```

#### 2. Run Container
```bash
docker run -d \
  --name big-market \
  -p 8091:8091 \
  -e SPRING_PROFILES_ACTIVE=prod \
  big-market:latest
```

### Configuration Management

#### Environment Configuration Files
- `application-dev.yml`: Development environment configuration
- `application-test.yml`: Test environment configuration
- `application-prod.yml`: Production environment configuration

#### JVM Parameter Configuration
JVM parameters for each environment are configured in the root pom.xml:
- **dev**: 1G memory, development debugging optimization
- **test**: 1G memory, test environment configuration
- **prod**: 6G memory, production environment optimization

## Project Features Details

### 1. Rule Engine

#### Responsibility Chain Rule Engine
- Supports multiple rules executing in sequence
- Each rule independently judges whether to pass
- Supports blacklist, weight, and other rule types

#### Decision Tree Rule Engine
- Rule judgment based on tree structure
- Supports complex conditional branching
- Supports inventory, unlock, and other business rules

### 2. Database Sharding

#### Sharding Strategy
- **Database sharding**: userId hash value % 2
- **Table sharding**: userId hash value % 4
- **Routing**: Automatically select database based on userId

#### Data Consistency
- Use distributed transactions to ensure data consistency
- Support cross-database queries and transactions

### 3. Cache Strategy

#### Redis Cache
- Strategy configuration cache
- Inventory cache
- User account cache

#### Cache Update
- Active update of strategy configuration
- Passive update of inventory information
- Scheduled refresh of user accounts

### 4. Message-Driven

#### RabbitMQ Usage
- Inventory deduction messages
- Award distribution messages
- Rebate execution messages

#### Message Reliability
- Message persistence
- Retry mechanism
- Dead letter queue

### 5. Dynamic Thread Pool

#### Thread Pool Configuration
- Core threads: 20
- Maximum threads: 50
- Queue size: 5000
- Rejection policy: CallerRunsPolicy

#### Dynamic Adjustment
- Support runtime parameter adjustment
- Monitor thread pool status
- Alert mechanism

## Testing Guide

### Unit Testing
```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=RaffleActivityTest
```

### API Testing

#### Testing with curl
```bash
# Activity raffle
curl -X POST http://localhost:8091/api/v1/raffle/activity/draw \
  -H "Content-Type: application/json" \
  -d '{"userId":"mlei","activityId":100301}'

# Query account
curl -X POST http://localhost:8091/api/v1/raffle/activity/query_user_activity_account \
  -H "Content-Type: application/json" \
  -d '{"userId":"mlei","activityId":100301}'
```

#### Testing with Postman
Import API documentation and test according to interface instructions.

## Monitoring & Operations

### Log Configuration
- Log path: `/export/Logs/big-market-boot/`
- Log level: INFO
- Log format: JSON format

### Monitoring Metrics
- JVM memory usage
- Thread pool status
- Database connection pool
- Redis connection status
- API response time
- Error rate statistics

### Alert Configuration
- Memory usage exceeds 80%
- Thread pool queue full
- Database connection timeout
- API response time exceeds 3s

## Common Issues

### 1. Database Connection Failed
Check database configuration and network connection, ensure database service is normal.

### 2. Redis Connection Timeout
Check Redis service status and network configuration, adjust connection timeout parameters.

### 3. Database Sharding Routing Error
Ensure userId parameter is passed correctly, check routing algorithm configuration.

### 4. Message Queue Consumption Failed
Check RabbitMQ service status, ensure message format is correct.

## Extension Development

### Adding New Raffle Rules
1. Create new rule class in `rule/chain/impl/`
2. Extend `AbstractLogicChain`
3. Implement `logic()` method
4. Register new rule in `DefaultChainFactory`

### Adding New Rebate Types
1. Add configuration in `daily_behavior_rebate` table
2. Add processing logic in `BehaviorRebateService`
3. Test rebate process

### Extending Database Sharding
1. Modify database sharding configuration in `application.yml`
2. Create new databases and tables
3. Update routing algorithm

## Technical Highlights

1. **Strict DDD Architecture**: Clear separation of four-layer architecture, pure domain model
2. **Flexible Rule Engine**: Supports both responsibility chain and decision tree modes
3. **High-Performance Database Sharding**: User ID-based horizontal sharding, supporting high concurrency
4. **Comprehensive Cache Strategy**: Multi-level cache to improve performance
5. **Reliable Message-Driven**: Asynchronous processing improves system throughput
6. **Dynamic Thread Pool Management**: Runtime adjustment to adapt to different loads
7. **Comprehensive Monitoring and Alerting**: Ensure stable system operation

## Development Team

- **Developer**: xiaofuge
- **Email**: 184172133@qq.com
- **Organization**: fuzhengwei
- **GitHub**: https://github.com/fuzhengwei

## License

Apache License, Version 2.0

## Changelog

### v1.0-SNAPSHOT
- Initial version release
- Implemented core raffle functionality
- Support for multiple raffle strategies
- Implemented database sharding
- Integrated message queue and cache
- Support for rebate mechanism

## Contact

For questions or suggestions, please contact via:
- Submit an Issue
- Send email to 184172133@qq.com
- Follow GitHub project updates

---

**Note**: This project is for learning and reference purposes. Please conduct sufficient testing and security assessment before using in production environments.