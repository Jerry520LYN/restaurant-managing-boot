package com.example.jerry.restaurant.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
import com.example.jerry.restaurant.pojo.order;
import com.example.jerry.restaurant.pojo.DiningTable;
import com.example.jerry.restaurant.pojo.Customer;
import com.example.jerry.restaurant.pojo.Result;
import com.example.jerry.restaurant.service.CheckoutService;

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

    @Override
    @Transactional
    public Result<Checkout> createOrder(int tableId, int customerId, int peopleCount) {
        try {
            // 检查餐桌是否存在且可用
            DiningTable table = diningTableMapper.getTableById(tableId);
            if (table == null) {
                return Result.error("餐桌不存在");
            }
            if (!"空".equals(table.getTableStatus())) {
                return Result.error("餐桌已被占用");
            }

            // 检查顾客是否存在
            Customer customer = customerMapper.getCustomerById(customerId);
            if (customer == null) {
                return Result.error("顾客不存在");
            }

            // 生成16位订单编号：时间8位 + 桌子2位 + 人数2位 + 订单ID4位
            String orderNumber = generateOrderNumber(tableId, peopleCount);
            
            // 创建订单
            order newOrder = new order();
            newOrder.setCustomerId(customerId);
            newOrder.setTableId(tableId);
            newOrder.setOrderTime(new java.sql.Date(System.currentTimeMillis()));
            newOrder.setTotalAmount(BigDecimal.ZERO);
            newOrder.setStatus("未结账");
            
            int orderId = orderMapper.addOrder(newOrder);
            
            // 创建Checkout对象返回
            Checkout checkout = new Checkout();
            checkout.setOrderId(orderId);
            checkout.setOrderNumber(orderNumber);
            checkout.setCustomerId(customerId);
            checkout.setTableId(tableId);
            checkout.setOrderTime(newOrder.getOrderTime());
            checkout.setTotalAmount(BigDecimal.ZERO);
            checkout.setStatus("未结账");
            
            // 根据顾客ID计算折扣
            BigDecimal discount = calculateDiscount(customerId);
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
            order order = orderMapper.getOrderById(orderId);
            if (order == null) {
                return Result.error("订单不存在");
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
            order order = orderMapper.getOrderById(orderId);
            if (order == null) {
                return Result.error("订单不存在");
            }
            
            // 删除菜品
            int result = orderDetailMapper.deleteOrderDetailByOrderAndDish(orderId, dishId);
            if (result > 0) {
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
            order order = orderMapper.getOrderById(orderId);
            if (order == null) {
                return Result.error("订单不存在");
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
            order order = orderMapper.getOrderById(orderId);
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
            orderMapper.checkoutOrder(orderId, finalAmount);
            
            // 创建Checkout对象返回
            Checkout checkout = new Checkout();
            checkout.setOrderId(orderId);
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
            List<order> orders = orderMapper.getOrdersByTimeRange(startTime, endTime);
            List<Checkout> checkouts = orders.stream().map(this::convertToCheckout).toList();
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
    public Result<List<Object>> getPopularDishes(Date startTime, Date endTime) {
        try {
            // 调用存储过程获取最受欢迎菜品
            List<Map<String, Object>> popularDishes = orderMapper.getPopularDishes(startTime, endTime);
            return Result.success(popularDishes.stream().map(dish -> (Object) dish).toList());
        } catch (Exception e) {
            return Result.error("获取热门菜品失败: " + e.getMessage());
        }
    }

    // 生成16位订单编号
    private String generateOrderNumber(int tableId, int peopleCount) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dateStr = sdf.format(new Date());
        String tableStr = String.format("%02d", tableId);
        String peopleStr = String.format("%02d", peopleCount);
        String orderIdStr = String.format("%04d", orderMapper.getOrderCount() + 1);
        
        return dateStr + tableStr + peopleStr + orderIdStr;
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

    // 将order转换为Checkout
    private Checkout convertToCheckout(order order) {
        Checkout checkout = new Checkout();
        checkout.setOrderId(order.getOrderId());
        checkout.setCustomerId(order.getCustomerId());
        checkout.setTableId(order.getTableId());
        checkout.setOrderTime(order.getOrderTime());
        checkout.setTotalAmount(order.getTotalAmount());
        checkout.setStatus(order.getStatus());
        checkout.setDiscount(calculateDiscount(order.getCustomerId()));
        checkout.setFinalAmount(order.getTotalAmount().multiply(calculateDiscount(order.getCustomerId())));
        return checkout;
    }
} 