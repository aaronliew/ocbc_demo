DROP TABLE IF EXISTS `customer`;

CREATE TABLE `user` (
   `id` int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT,
   `name` varchar(255) DEFAULT NULL,
   `record_create_date` datetime DEFAULT NULL,
   `record_update_date` datetime DEFAULT NULL
)
