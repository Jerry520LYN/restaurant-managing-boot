-- 获取最受欢迎菜品的存储过程
DELIMITER $$
CREATE PROCEDURE get_popular_dishes(
    IN start_time DATETIME,
    IN end_time DATETIME
)
BEGIN
    SELECT 
        m.dish_name,
        SUM(od.quantity) AS total_quantity,
        COUNT(DISTINCT o.order_id) AS order_count,
        RANK() OVER (ORDER BY SUM(od.quantity) DESC) AS popularity_rank
    FROM menu m
    JOIN order_detail od ON m.dish_id = od.dish_id
    JOIN `order` o ON od.order_id = o.order_id
    WHERE o.order_time BETWEEN start_time AND end_time
    GROUP BY m.dish_id, m.dish_name
    ORDER BY total_quantity DESC;
END$$
DELIMITER ; 