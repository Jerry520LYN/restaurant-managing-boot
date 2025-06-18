# 饭店买单系统使用说明

## 概述
本系统实现了完整的饭店买单功能，包括订单创建、菜品管理、结账和统计查询等功能。

## 主要功能

### 1. 订单管理
- **创建订单**: 根据餐桌ID、顾客ID和人数创建新订单
- **添加菜品**: 向订单中添加菜品及数量
- **移除菜品**: 从订单中删除指定菜品
- **修改数量**: 更新订单中菜品的数量

### 2. 结账功能
- **自动计算**: 根据订单明细自动计算总价
- **折扣计算**: 根据顾客ID自动应用折扣
  - 0开头: 无折扣 (1.0)
  - 1开头: 95折 (0.95)
  - 2开头: 9折 (0.90)
  - 3开头: 85折 (0.85)
  - 4开头: 8折 (0.80)
  - 5开头: 75折 (0.75)
- **餐桌释放**: 结账后自动释放餐桌

### 3. 查询统计
- **时间段查询**: 根据时间范围查询订单
- **订单详情**: 查看订单的详细菜品信息
- **热门菜品**: 统计指定时间段内最受欢迎的菜品
- **营收统计**: 计算指定时间段内的总营收

## API接口

### 订单管理接口

#### 1. 创建订单
```
POST /checkout/create-order
参数:
- tableId: 餐桌ID
- customerId: 顾客ID
- peopleCount: 人数
```

#### 2. 添加菜品
```
POST /checkout/add-dish
参数:
- orderId: 订单ID
- dishId: 菜品ID
- quantity: 数量
```

#### 3. 移除菜品
```
DELETE /checkout/remove-dish
参数:
- orderId: 订单ID
- dishId: 菜品ID
```

#### 4. 修改菜品数量
```
PUT /checkout/update-dish-quantity
参数:
- orderId: 订单ID
- dishId: 菜品ID
- quantity: 新数量
```

### 结账接口

#### 5. 结账
```
POST /checkout/checkout
参数:
- orderId: 订单ID
```

### 查询接口

#### 6. 时间段查询订单
```
GET /checkout/orders-by-time
参数:
- startTime: 开始时间 (yyyy-MM-dd HH:mm:ss)
- endTime: 结束时间 (yyyy-MM-dd HH:mm:ss)
```

#### 7. 获取订单详情
```
GET /checkout/order-details/{orderId}
```

#### 8. 获取热门菜品
```
GET /checkout/popular-dishes
参数:
- startTime: 开始时间 (yyyy-MM-dd HH:mm:ss)
- endTime: 结束时间 (yyyy-MM-dd HH:mm:ss)
```

#### 9. 获取营收统计
```
GET /checkout/revenue
参数:
- startTime: 开始时间 (yyyy-MM-dd HH:mm:ss)
- endTime: 结束时间 (yyyy-MM-dd HH:mm:ss)
```

## 数据库设计

### 核心表结构
1. **order表**: 订单主表
2. **order_detail表**: 订单明细表
3. **menu表**: 菜品表
4. **dining_table表**: 餐桌表
5. **customer表**: 顾客表

### 触发器
1. **check_order_constraints_before_checkout**: 结账前检查约束
2. **update_table_status_on_order**: 下单时更新餐桌状态
3. **update_table_status_on_payment**: 结账时更新餐桌状态

### 存储过程
1. **get_popular_dishes**: 获取最受欢迎菜品
2. **get_revenue_by_period**: 按时间段查询营收

## 订单编号生成规则
订单编号为16位，格式为：时间8位 + 桌子2位 + 人数2位 + 订单ID4位

例如：2023120112345601 表示2023年12月1日12点34分56秒，1号桌，1人，订单ID为0001

## 使用示例

### 创建订单
```bash
curl -X POST "http://localhost:8080/checkout/create-order" \
  -d "tableId=1" \
  -d "customerId=1001" \
  -d "peopleCount=4"
```

### 添加菜品
```bash
curl -X POST "http://localhost:8080/checkout/add-dish" \
  -d "orderId=1" \
  -d "dishId=1" \
  -d "quantity=2"
```

### 结账
```bash
curl -X POST "http://localhost:8080/checkout/checkout" \
  -d "orderId=1"
```

### 查询今日订单
```bash
curl -X GET "http://localhost:8080/checkout/orders-by-time" \
  -d "startTime=2023-12-01 00:00:00" \
  -d "endTime=2023-12-01 23:59:59"
```

## 注意事项
1. 创建订单前请确保餐桌状态为空
2. 结账前请确保订单中有菜品
3. 顾客ID的第一位数字决定了折扣率
4. 系统会自动处理餐桌状态的更新
5. 所有金额计算都使用BigDecimal确保精度 