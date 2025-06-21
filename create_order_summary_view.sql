-- 创建order_summary视图
CREATE 
    ALGORITHM = UNDEFINED 
    DEFINER = `root`@`localhost` 
    SQL SECURITY DEFINER
VIEW `restaurant`.`order_summary` AS
    SELECT 
        `o`.`order_id` AS `order_id`,
        `c`.`name` AS `customer_name`,
        `d`.`table_id` AS `table_id`,
        `o`.`order_time` AS `order_time`,
        `o`.`total_amount` AS `total_amount`,
        `o`.`final_amount` AS `final_amount`,
        `o`.`discount` AS `discount`,
        GROUP_CONCAT(`m`.`dish_name`
            SEPARATOR ',') AS `dishes`
    FROM
        ((((`restaurant`.`orders` `o`
        JOIN `restaurant`.`customer` `c` ON ((`o`.`customer_id` = `c`.`customer_id`)))
        JOIN `restaurant`.`dining_table` `d` ON ((`o`.`table_id` = `d`.`table_id`)))
        JOIN `restaurant`.`order_detail` `od` ON ((`o`.`order_id` = `od`.`order_id`)))
        JOIN `restaurant`.`menu` `m` ON ((`od`.`dish_id` = `m`.`dish_id`)))
    GROUP BY `o`.`order_id`; 