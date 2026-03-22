# StockMaster - 企业库存管理系统

## 项目简介

StockMaster 是一个功能完善的企业级库存管理系统，基于 Spring Boot 3.x 开发，采用模块化架构设计。

## 技术栈

- Java 17
- Spring Boot 3.2.3
- Spring Data JPA
- Spring Security
- MySQL 8.0
- JWT 认证

## 功能模块

### 用户管理模块
- 用户登录注册
- 个人信息修改
- 密码修改
- 头像上传
- 多角色用户管理
- 用户列表查询
- 用户批量删除
- 角色分配

### 仪表盘模块
- 商品总数统计
- 供应商数量统计
- 采购订单统计
- 库存预警统计
- 采购趋势图表分析
- 商品分类占比图表
- 采购与库存对比图表

### 商品管理模块
- 商品信息增删改查
- 商品分类管理
- 商品图片上传
- 商品条码管理
- 商品设置（零售价、成本价）
- 商品库存显示
- 商品上下架

### 商品分类模块
- 分类信息增删改查
- 分类树形结构

### 供应商管理模块
- 供应商信息增删改查
- 供应商状态启用禁用
- 供应商评分显示
- 供应商评价查看
- 供应商评价统计

### 供应商评价模块
- 供应商评价增删改查
- 多维度评分
- 评价内容编辑
- 关联采购订单
- 评价列表查询

### 采购订单模块
- 采购订单创建编辑
- 订单商品明细管理
- 订单状态管理
- 订单详情查看
- 自动更新库存

### 库存管理模块
- 库存信息查询
- 库存数量修改
- 库存预警设置

### 入库管理模块
- 入库记录增删改查
- 入库单号管理
- 入库商品选择
- 入库数量录入
- 入库记录批量删除

### 出库管理模块
- 出库记录增删改查
- 出库单号管理
- 出库商品选择
- 出库数量录入
- 出库记录批量删除

### 系统管理-角色模块
- 角色信息增删改查
- 角色权限设置
- 菜单权限分配
- 角色描述编辑
- 角色列表查询

### 系统管理-菜单模块
- 菜单信息增删改查
- 菜单树形结构
- 父子菜单管理
- 菜单路由配置
- 菜单图标设置
- 菜单显示隐藏
- 菜单排序
- 动态路由生成

### 系统管理-日志模块
- 系统日志查询
- 操作类型筛选
- 操作模块筛选
- 用户操作记录
- IP地址记录
- 操作时间范围查询
- 日志状态筛选
- 日志清空

### 权限管理模块
- 基于角色的权限控制
- 动态菜单加载
- 路由权限验证
- 操作权限控制
- 登录状态验证

## API 接口

所有API接口前缀为 `/api`

### 认证接口 `/auth`
- POST `/login` - 用户登录
- POST `/logout` - 用户登出
- GET `/info` - 获取当前用户信息
- POST `/register` - 用户注册

### 用户管理 `/system/users`
- GET `/` - 查询用户列表
- GET `/{id}` - 查询用户详情
- POST `/` - 创建用户
- PUT `/{id}` - 修改用户
- DELETE `/{id}` - 删除用户
- DELETE `/batch` - 批量删除用户
- PUT `/{id}/password` - 修改密码
- PUT `/{id}/avatar` - 上传头像
- PUT `/{id}/roles` - 分配角色
- PUT `/{id}/status` - 修改用户状态

### 角色管理 `/system/roles`
- GET `/` - 查询角色列表
- GET `/all` - 获取所有角色
- GET `/{id}` - 查询角色详情
- POST `/` - 创建角色
- PUT `/{id}` - 修改角色
- DELETE `/{id}` - 删除角色
- PUT `/{id}/menus` - 分配菜单权限

### 菜单管理 `/system/menus`
- GET `/` - 查询菜单树
- GET `/user` - 获取用户菜单
- GET `/{id}` - 查询菜单详情
- POST `/` - 创建菜单
- PUT `/{id}` - 修改菜单
- DELETE `/{id}` - 删除菜单

### 日志管理 `/system/logs`
- GET `/` - 查询日志列表
- DELETE `/clear` - 清空日志
- DELETE `/batch` - 批量删除日志

### 商品管理 `/stock/products`
- GET `/` - 查询商品列表
- GET `/{id}` - 查询商品详情
- GET `/code/{code}` - 根据编码查询商品
- POST `/` - 创建商品
- PUT `/{id}` - 修改商品
- DELETE `/{id}` - 删除商品
- DELETE `/batch` - 批量删除商品
- PUT `/{id}/status` - 修改商品状态
- GET `/low-stock` - 查询低库存商品

### 分类管理 `/stock/categories`
- GET `/` - 查询分类树
- GET `/all` - 获取所有分类
- GET `/{id}` - 查询分类详情
- POST `/` - 创建分类
- PUT `/{id}` - 修改分类
- DELETE `/{id}` - 删除分类

### 库存管理 `/stock/inventory`
- GET `/` - 查询库存列表
- GET `/{id}` - 查询库存详情
- GET `/product/{productId}` - 根据商品查询库存
- PUT `/{id}/quantity` - 修改库存数量
- PUT `/{id}/warning` - 设置库存预警
- GET `/low-stock` - 查询低库存列表

### 入库管理 `/stock/inbound`
- GET `/` - 查询入库列表
- GET `/{id}` - 查询入库详情
- POST `/` - 创建入库记录
- PUT `/{id}` - 修改入库记录
- DELETE `/{id}` - 删除入库记录
- DELETE `/batch` - 批量删除入库记录

### 出库管理 `/stock/outbound`
- GET `/` - 查询出库列表
- GET `/{id}` - 查询出库详情
- POST `/` - 创建出库记录
- PUT `/{id}` - 修改出库记录
- DELETE `/{id}` - 删除出库记录
- DELETE `/batch` - 批量删除出库记录

### 供应商管理 `/purchase/suppliers`
- GET `/` - 查询供应商列表
- GET `/all` - 获取所有供应商
- GET `/{id}` - 查询供应商详情
- POST `/` - 创建供应商
- PUT `/{id}` - 修改供应商
- DELETE `/{id}` - 删除供应商
- PUT `/{id}/status` - 修改供应商状态

### 供应商评价 `/purchase/evaluations`
- GET `/` - 查询评价列表
- GET `/{id}` - 查询评价详情
- POST `/` - 创建评价
- PUT `/{id}` - 修改评价
- DELETE `/{id}` - 删除评价

### 采购订单 `/purchase/orders`
- GET `/` - 查询订单列表
- GET `/{id}` - 查询订单详情
- POST `/` - 创建采购订单
- PUT `/{id}` - 修改采购订单
- DELETE `/{id}` - 删除采购订单
- PUT `/{id}/submit` - 提交审核
- PUT `/{id}/approve` - 审核通过
- PUT `/{id}/reject` - 审核拒绝
- PUT `/{id}/cancel` - 取消订单
- PUT `/{id}/receive` - 订单收货

### 仪表盘 `/dashboard`
- GET `/stats` - 获取统计数据
- GET `/trend` - 获取采购趋势
- GET `/category-distribution` - 获取分类分布
- GET `/purchase-vs-stock` - 获取采购库存对比

## 启动说明

1. 确保已安装 Java 17+ 和 MySQL 8.0+
2. 创建数据库：`CREATE DATABASE stockmaster CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;`
3. 修改 `application.yml` 中的数据库连接配置
4. 运行 `mvn spring-boot:run` 或直接运行 `StockMasterApplication`

默认端口：8080
API前缀：/api

## 默认管理员账号

首次启动时，可通过注册接口创建用户，或手动在数据库中添加。

## 许可证

MIT License
