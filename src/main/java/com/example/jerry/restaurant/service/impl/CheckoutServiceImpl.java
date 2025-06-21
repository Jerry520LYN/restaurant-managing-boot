package com.example.jerry.restaurant.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.jerry.restaurant.mapper.OrderMapper;
import com.example.jerry.restaurant.mapper.OrderDetailMapper;
import com.example.jerry.restaurant.mapper.MenuMapper;
import com.example.jerry.restaurant.mapper.DiningTableMapper;
import com.example.jerry.restaurant.mapper.CustomerMapper;
import com.example.jerry.restaurant.pojo.Checkout;
import com.example.jerry.restaurant.pojo.OrderDetail;
import com.example.jerry.restaurant.pojo.Menu;
import com.example.jerry.restaurant.pojo.Order;
import com.example.jerry.restaurant.pojo.DiningTable;
import com.example.jerry.restaurant.pojo.Customer;
import com.example.jerry.restaurant.pojo.Result;
import com.example.jerry.restaurant.service.CheckoutService;
import com.example.jerry.restaurant.pojo.CallingQueueManager;
import com.example.jerry.restaurant.pojo.CallingNumber;

@Service
public class CheckoutServiceImpl implements CheckoutService {

    @Autowired
    private OrderMapper orderMapper;
    
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    
    @Autowired
    private MenuMapper menuMapper;
    
    @Autowired
    private DiningTableMapper diningTableMapper;
    
    @Autowired
    private CustomerMapper customerMapper;

    // 折扣数组：顾客ID以0开头为1，1开头为0.95，2开头为0.9，3开头为0.85，4开头为0.8，5开头为0.75
    private final BigDecimal[] discounts = {
        BigDecimal.ONE,           // 0开头 - 无折扣
        new BigDecimal("0.95"),   // 1开头 - 95折
        new BigDecimal("0.90"),   // 2开头 - 9折
        new BigDecimal("0.85"),   // 3开头 - 85折
        new BigDecimal("0.80"),   // 4开头 - 8折
        new BigDecimal("0.75")    // 5开头 - 75折
    };

    // 叫号队列管理器
    private final CallingQueueManager queueManager = new CallingQueueManager();

    // 用于生成每日自增编号
    private static String lastOrderDate = "";
    private static int dailyOrderSeq = 0;

    private synchronized String generateOrderNumber(int tableId) {
        String dateStr = new java.text.SimpleDateFormat("yyyyMMdd").format(new java.util.Date());
        String tableStr = String.format("%02d", tableId);
        if (!dateStr.equals(lastOrderDate)) {
            lastOrderDate = dateStr;
            dailyOrderSeq = 1;
        } else {
            dailyOrderSeq++;
        }
        String seqStr = String.format("%06d", dailyOrderSeq);
        return dateStr + tableStr + seqStr;
    }


    @Override
    @Transactional
    public Result<Checkout> createOrder(int tableId, int customerId, int peopleCount) {
        try {
            // 检查人数是否有效
            if (peopleCount <= 0) {
                return Result.error("人数必须大于0");
            }
            
            // 检查餐桌是否存在且可用
            DiningTable table = diningTableMapper.getTableById(tableId);
            if (table == null) {
                return Result.error("餐桌不存在");
            }
            if (!"空".equals(table.getTableStatus())) {
                return Result.error("餐桌已被占用");
            }
            
            // 检查人数是否超过餐桌容量
            if (peopleCount > table.getCapacity()) {
                return Result.error("人数超过餐桌容量，该餐桌最多可容纳" + table.getCapacity() + "人");
            }

            // 检查顾客是否存在
            Customer customer = customerMapper.getCustomerById(customerId);
            if (customer == null) {
                return Result.error("顾客不存在");
            }

            // 生成订单编号
            String orderNumber = generateOrderNumber(tableId);

            // 计算折扣
            BigDecimal discount = calculateDiscount(customerId);
            
            // 创建订单
            Order newOrder = new Order();
            newOrder.setOrderNumber(orderNumber);
            newOrder.setCustomerId(customerId);
            newOrder.setTableId(tableId);
            newOrder.setOrderTime(new java.sql.Date(System.currentTimeMillis()));
            newOrder.setTotalAmount(BigDecimal.ZERO);
            newOrder.setDiscount(discount);
            newOrder.setFinalAmount(BigDecimal.ZERO);
            newOrder.setStatus("未结账");
            
            // 调用addOrder后，MyBatis会将数据库生成的自增ID回填到newOrder对象中
            orderMapper.addOrder(newOrder);
            
            // 更新餐桌状态为"占用"
            diningTableMapper.updateStatus(tableId, "占用");
            
            
            // 创建Checkout对象返回
            Checkout checkout = new Checkout();
            // 从newOrder对象中获取真实的数据库ID
            checkout.setOrderId(newOrder.getOrderId());
            checkout.setOrderNumber(orderNumber);
            checkout.setCustomerId(customerId);
            checkout.setTableId(tableId);
            checkout.setOrderTime(newOrder.getOrderTime());
            checkout.setTotalAmount(BigDecimal.ZERO);
            checkout.setStatus("未结账");
            checkout.setDiscount(discount);
            checkout.setFinalAmount(BigDecimal.ZERO);
            
            return Result.success(checkout);
            
        } catch (Exception e) {
            return Result.error("创建订单失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Result<String> addDishToOrder(int orderId, int dishId, int quantity) {
        try {
            // 检查订单是否存在
            Order order = orderMapper.getOrderById(orderId);
            if (order == null) {
                return Result.error("订单不存在");
            }
            
            // 检查订单状态，如果已结账则不允许修改
            if ("已结账".equals(order.getStatus())) {
                return Result.error("订单已结账，无法修改菜品信息");
            }
            
            // 检查菜品是否存在
            Menu dish = menuMapper.getDishById(dishId);
            if (dish == null) {
                return Result.error("菜品不存在");
            }
            
            // 检查是否已存在该菜品
            int exists = orderDetailMapper.checkOrderDetailExists(orderId, dishId);
            if (exists > 0) {
                // 如果已存在，增加数量
                OrderDetail existingDetail = orderDetailMapper.getOrderDetailsByOrderId(orderId)
                    .stream()
                    .filter(detail -> detail.getDishId() == dishId)
                    .findFirst()
                    .orElse(null);
                
                if (existingDetail != null) {
                    int newQuantity = existingDetail.getQuantity() + quantity;
                    orderDetailMapper.updateOrderDetailQuantity(existingDetail.getDetailId(), newQuantity);
                }
            } else {
                // 如果不存在，新增记录
                OrderDetail orderDetail = new OrderDetail(orderId, dishId, quantity);
                orderDetailMapper.addOrderDetail(orderDetail);
            }
            
            // 重新计算订单总金额和最终金额
            updateOrderAmounts(orderId);
            
            return Result.success("添加菜品成功");
            
        } catch (Exception e) {
            return Result.error("添加菜品失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Result<String> removeDishFromOrder(int orderId, int dishId) {
        try {
            // 检查订单是否存在
            Order order = orderMapper.getOrderById(orderId);
            if (order == null) {
                return Result.error("订单不存在");
            }
            
            // 检查订单状态，如果已结账则不允许修改
            if ("已结账".equals(order.getStatus())) {
                return Result.error("订单已结账，无法修改菜品信息");
            }
            
            // 删除菜品
            int result = orderDetailMapper.deleteOrderDetailByOrderAndDish(orderId, dishId);
            if (result > 0) {
                // 重新计算订单总金额和最终金额
                updateOrderAmounts(orderId);
                return Result.success("移除菜品成功");
            } else {
                return Result.error("菜品不存在于订单中");
            }
            
        } catch (Exception e) {
            return Result.error("移除菜品失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Result<String> updateDishQuantity(int orderId, int dishId, int quantity) {
        try {
            // 检查订单是否存在
            Order order = orderMapper.getOrderById(orderId);
            if (order == null) {
                return Result.error("订单不存在");
            }
            
            // 检查订单状态，如果已结账则不允许修改
            if ("已结账".equals(order.getStatus())) {
                return Result.error("订单已结账，无法修改菜品信息");
            }
            
            if (quantity <= 0) {
                // 如果数量为0或负数，删除该菜品
                return removeDishFromOrder(orderId, dishId);
            }
            
            // 查找订单明细
            List<OrderDetail> details = orderDetailMapper.getOrderDetailsByOrderId(orderId);
            OrderDetail targetDetail = details.stream()
                .filter(detail -> detail.getDishId() == dishId)
                .findFirst()
                .orElse(null);
            
            if (targetDetail == null) {
                return Result.error("菜品不存在于订单中");
            }
            
            // 更新数量
            orderDetailMapper.updateOrderDetailQuantity(targetDetail.getDetailId(), quantity);
            
            // 重新计算订单总金额和最终金额
            updateOrderAmounts(orderId);
            
            return Result.success("更新菜品数量成功");
            
        } catch (Exception e) {
            return Result.error("更新菜品数量失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Result<Checkout> checkout(int orderId) {
        try {
            // 检查订单是否存在
            Order order = orderMapper.getOrderById(orderId);
            if (order == null) {
                return Result.error("订单不存在");
            }
            
            if ("已结账".equals(order.getStatus())) {
                return Result.error("订单已结账");
            }
            
            // 获取订单详情并计算总价
            List<OrderDetail> orderDetails = orderDetailMapper.getOrderDetailsWithDishInfo(orderId);
            if (orderDetails.isEmpty()) {
                return Result.error("订单中没有菜品");
            }
            
            BigDecimal totalAmount = BigDecimal.ZERO;
            for (OrderDetail detail : orderDetails) {
                Menu dish = menuMapper.getDishById(detail.getDishId());
                if (dish != null) {
                    BigDecimal price = dish.getPrice();
                    BigDecimal quantity = new BigDecimal(detail.getQuantity());
                    totalAmount = totalAmount.add(price.multiply(quantity));
                }
            }
            
            // 计算折扣
            BigDecimal discount = calculateDiscount(order.getCustomerId());
            BigDecimal finalAmount = totalAmount.multiply(discount);
            
            // 更新订单状态和总金额
            orderMapper.checkoutOrder(orderId, totalAmount, discount, finalAmount);
            
            // 创建Checkout对象返回
            Checkout checkout = new Checkout();
            checkout.setOrderId(orderId);
            checkout.setOrderNumber(generateOrderNumber(order.getTableId()));
            checkout.setCustomerId(order.getCustomerId());
            checkout.setTableId(order.getTableId());
            checkout.setOrderTime(order.getOrderTime());
            checkout.setTotalAmount(totalAmount);
            checkout.setStatus("已结账");
            checkout.setDiscount(discount);
            checkout.setFinalAmount(finalAmount);
            
            return Result.success(checkout);
            
        } catch (Exception e) {
            return Result.error("结账失败: " + e.getMessage());
        }
    }

    @Override
    public Result<List<Checkout>> getOrdersByTimeRange(Date startTime, Date endTime) {
        try {
            List<Order> orders = orderMapper.getOrdersByTimeRange(startTime, endTime);
            List<Checkout> checkouts = orders.stream().map(order -> convertToCheckout(order)).toList();
            return Result.success(checkouts);
        } catch (Exception e) {
            return Result.error("查询订单失败: " + e.getMessage());
        }
    }

    @Override
    public Result<List<OrderDetail>> getOrderDetails(int orderId) {
        try {
            List<OrderDetail> details = orderDetailMapper.getOrderDetailsWithDishInfo(orderId);
            return Result.success(details);
        } catch (Exception e) {
            return Result.error("获取订单详情失败: " + e.getMessage());
        }
    }

    @Override
    public Result<List<Map<String, Object>>> getPopularDishes(Date startTime, Date endTime) {
        try {
            List<Map<String, Object>> popularDishes = orderMapper.getPopularDishes(startTime, endTime);
            List<Map<String, Object>> result = popularDishes.stream()
                .map(dish -> Map.of(
                    "dish_name", dish.get("dish_name"),
                    "dish_id", dish.get("dish_id"),
                    "description", dish.get("description"),
                    "total_quantity", dish.get("total_quantity")
                )).toList();
            return Result.success(result);
        } catch (Exception e) {
            return Result.error("获取热门菜品失败: " + e.getMessage());
        }
    }

    // 根据顾客ID计算折扣
    private BigDecimal calculateDiscount(int customerId) {
        String customerIdStr = String.valueOf(customerId);
        if (customerIdStr.length() > 0) {
            char firstDigit = customerIdStr.charAt(0);
            int index = Character.getNumericValue(firstDigit);
            if (index >= 0 && index < discounts.length) {
                return discounts[index];
            }
        }
        return BigDecimal.ONE; // 默认无折扣
    }

    // 更新订单的总金额和最终金额
    private void updateOrderAmounts(int orderId) {
        try {
            // 获取订单详情并计算总价
            List<OrderDetail> orderDetails = orderDetailMapper.getOrderDetailsWithDishInfo(orderId);
            BigDecimal totalAmount = BigDecimal.ZERO;
            
            for (OrderDetail detail : orderDetails) {
                Menu dish = menuMapper.getDishById(detail.getDishId());
                if (dish != null) {
                    BigDecimal price = dish.getPrice();
                    BigDecimal quantity = new BigDecimal(detail.getQuantity());
                    totalAmount = totalAmount.add(price.multiply(quantity));
                }
            }
            
            // 获取订单信息以获取折扣
            Order order = orderMapper.getOrderById(orderId);
            if (order != null) {
                BigDecimal discount = order.getDiscount();
                BigDecimal finalAmount = totalAmount.multiply(discount);
                
                // 更新订单的总金额和最终金额
                orderMapper.updateOrder(orderId, order.getCustomerId(), order.getTableId(), 
                                     totalAmount, discount, finalAmount);
            }
        } catch (Exception e) {
            // 记录错误但不抛出异常，避免影响主要业务流程
            System.err.println("更新订单金额失败: " + e.getMessage());
        }
    }

    // 将order转换为Checkout
    private Checkout convertToCheckout(Order order) {
        Checkout checkout = new Checkout();
        checkout.setOrderId(order.getOrderId());
        checkout.setOrderNumber(generateOrderNumber(order.getTableId()));
        checkout.setCustomerId(order.getCustomerId());
        checkout.setTableId(order.getTableId());
        checkout.setOrderTime(order.getOrderTime());
        checkout.setTotalAmount(order.getTotalAmount());
        checkout.setStatus(order.getStatus());
        checkout.setDiscount(order.getDiscount());
        checkout.setFinalAmount(order.getFinalAmount());
        return checkout;
    }
    @Override
    public Result<List<Checkout>> getOrdersByTimeRangeAndStatus(Date startTime, Date endTime, String status) {
        try {
            List<Order> orders = orderMapper.getOrdersByTimeRangeAndStatus(startTime, endTime, status);
            List<Checkout> checkouts = orders.stream().map(this::convertToCheckout).collect(Collectors.toList());
            return Result.success(checkouts);
        } catch (Exception e) {
            return Result.error("查询订单失败: " + e.getMessage());
        }
    }

    @Override
    public Result<List<Checkout>> getAllOrdersAsCheckout() {
        try {
            List<Order> orders = orderMapper.getAllOrders();
            List<Checkout> checkouts = orders.stream().map(this::convertToCheckout).collect(Collectors.toList());
            return Result.success(checkouts);
        } catch (Exception e) {
            return Result.error("获取所有订单失败: " + e.getMessage());
        }
    }

    @Override
    public Result<List<Map<String, Object>>> getOrderIdWithDishId(int tableId) {
        try {
            List<Map<String, Object>> orderIdWithDishId = orderMapper.getOrderIdWithDishId(tableId);
            return Result.success(orderIdWithDishId);
        } catch (Exception e) {
            return Result.error("获取订单ID和菜品ID失败: " + e.getMessage());
        }
    }
} 