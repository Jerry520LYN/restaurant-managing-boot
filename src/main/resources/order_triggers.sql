-- 订单结账时的约束检查触发器
DELIMITER $$
CREATE TRIGGER check_order_constraints_before_checkout
BEFORE UPDATE ON `order`
FOR EACH ROW
BEGIN
    -- 检查是否要更新为已结账状态
    IF NEW.status = '已结账' AND OLD.status != '已结账' THEN
        -- 检查顾客ID不能为空
        IF NEW.customer_id IS NULL OR NEW.customer_id = 0 THEN
            SIGNAL SQLSTATE '45000' 
            SET MESSAGE_TEXT = '结账时顾客ID不能为空';
        END IF;
        
        -- 检查总金额不能为空或为0
        IF NEW.total_amount IS NULL OR NEW.total_amount <= 0 THEN
            SIGNAL SQLSTATE '45000' 
            SET MESSAGE_TEXT = '结账时总金额不能为空或为0';
        END IF;
    END IF;
END$$
DELIMITER ;

-- 更新餐桌状态的触发器（已存在，这里重新确认）
-- 下单时触发，设置餐桌状态为"占用"
DELIMITER $$
CREATE TRIGGER update_table_status_on_order
AFTER INSERT ON `order`
FOR EACH ROW
BEGIN
    UPDATE dining_table
    SET table_status = '占用'
    WHERE table_id = NEW.table_id;
END$$
DELIMITER ;

-- 结账时触发，设置餐桌状态为"空"
DELIMITER $$
CREATE TRIGGER update_table_status_on_payment
AFTER UPDATE ON `order`
FOR EACH ROW
BEGIN
    IF NEW.status = '已结账' THEN
        UPDATE dining_table
        SET table_status = '空'
        WHERE table_id = NEW.table_id;
    END IF;
END$$
DELIMITER ; 