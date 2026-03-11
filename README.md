# StockMaster - 企业级库存管理系统

## 项目简介

StockMaster 是基于 Spring Boot 3.x 框架开发的企业级库存管理系统，旨在为商家提供高效、精准的库存管理能力，实现多维度库存查询、预警分析、多场景统一管理等核心功能。

## 核心功能

### 1. 多维度库存查询与管理
- 实时掌握库存状态
- 支持按产品、仓库、类别等多维度查询
- 库存数据实时更新与同步

### 2. 库存预警与数据分析
- 自动补货提醒
- 销量分析与趋势预测
- 库存积压预警
- 智能数据分析报表

### 3. 多场景库存统一管理
- 网店、实体店库存统一管理
- 多仓库库存调拨
- 跨区域库存协同

### 4. 完整库存操作流程
- 入库管理
- 出库管理
- 库存调拨
- 库存盘点
- 库存冻结与解冻

### 5. 系统安全与可控
- 基于角色的权限管理
- 操作日志全程记录
- 数据审计与追溯

## 技术架构

### 后端技术栈
- **Spring Boot 3.2.3** - 应用框架
- **Spring Security** - 安全框架
- **Spring Data JPA** - 数据持久化
- **MySQL 8.0** - 数据库
- **Redis** - 缓存与分布式锁
- **JWT** - 身份认证
- **AOP** - 切面编程

### 核心特性
- **实时性**：多终端实时查看库存数据
- **智能化**：自动补货提醒、销量分析
- **一体化**：统一管理多渠道、多仓库库存
- **可扩展**：基于 Spring Boot 架构，便于后续功能迭代

## 项目结构

```
com.stockmaster
├── common              # 公共模块
│   ├── aop            # 切面编程
│   ├── entity         # 基础实体类
│   └── enums          # 枚举类
├── config             # 配置类
├── modules            # 业务模块
│   ├── system         # 系统管理
│   │   ├── controller # 控制器
│   │   ├── entity     # 实体类
│   │   ├── repository # 数据访问
│   │   └── service    # 业务逻辑
│   └── stock          # 库存管理
│       ├── controller # 控制器
│       ├── entity     # 实体类
│       ├── repository # 数据访问
│       └── service    # 业务逻辑
└── StockMasterApplication.java # 启动类
```

## 快速开始

### 环境要求
- JDK 17+
- MySQL 8.0+
- Redis 6.0+
- Maven 3.8+

### 安装步骤

1. **克隆项目**
   ```bash
   git clone https://github.com/silencer-cmo/StockMaster.git
   cd StockMaster
   ```

2. **配置数据库**
   - 创建 MySQL 数据库：`stockmaster`
   - 修改 `application.yml` 中的数据库配置

3. **启动 Redis**
   ```bash
   redis-server
   ```

4. **启动项目**
   ```bash
   mvn spring-boot:run
   ```

5. **访问系统**
   ```
   http://localhost:8080/api
   ```

## API 文档

### 用户管理
- `POST /system/users` - 创建用户
- `GET /system/users/{id}` - 获取用户信息
- `PUT /system/users/{id}` - 更新用户信息
- `DELETE /system/users/{id}` - 删除用户

### 产品管理
- `POST /stock/products` - 创建产品
- `GET /stock/products` - 查询产品列表
- `GET /stock/products/{id}` - 获取产品详情
- `PUT /stock/products/{id}` - 更新产品信息
- `DELETE /stock/products/{id}` - 删除产品

### 库存管理
- `GET /stock/inventory` - 查询库存
- `POST /stock/inventory/in` - 入库操作
- `POST /stock/inventory/out` - 出库操作
- `POST /stock/inventory/transfer` - 库存调拨

## 开发计划

### 第一阶段（已完成）
- 基础项目架构搭建
- 用户管理模块
- 产品管理模块
- 库存管理模块
- 基础权限控制

### 第二阶段
- 库存预警系统
- 数据分析报表
- 多仓库管理
- 移动端适配

### 第三阶段
- 第三方系统集成
- 高级数据分析
- 智能补货算法
- 国际化支持

## 贡献指南

欢迎提交 Issue 和 Pull Request 来改进项目。

## 许可证

MIT License

## 联系方式

如有问题或建议，请通过以下方式联系：
- GitHub: https://github.com/silencer-cmo
- Email: developer@stockmaster.com