-- Initial database setup for orders schema
CREATE TABLE IF NOT EXISTS `t_orders` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `customer_id` varchar(255) NOT NULL,
  `product_id` varchar(255) NOT NULL,
  `quantity` int NOT NULL,
  `status` varchar(50) DEFAULT 'PENDING',
  `tracking_number` varchar(50) DEFAULT NULL,
  `order_date` datetime(6) DEFAULT CURRENT_TIMESTAMP(6),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Insert some sample data
INSERT INTO `t_orders` (`customer_id`, `product_id`, `quantity`, `status`) VALUES
('CUST001', 'PROD100', 2, 'PENDING'),
('CUST002', 'PROD200', 1, 'SHIPPED'),
('CUST001', 'PROD300', 3, 'DELIVERED');
