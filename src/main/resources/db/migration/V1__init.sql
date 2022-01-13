DROP TABLE IF EXISTS `customer`;
DROP TABLE IF EXISTS `transaction`;

CREATE TABLE `user` (
   `id` int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT,
   `name` varchar(255) DEFAULT NULL,
   `balance` int(20) DEFAULT 0,
   `record_create_date` datetime DEFAULT NULL,
   `record_update_date` datetime DEFAULT NULL
);

CREATE TABLE `transaction` (
    `id` int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `sender_user_id` int(11) DEFAULT NULL,
    `recipient_user_id` int(11) DEFAULT NULL,
    `amount` int(20) DEFAULT NULL,
    `transaction_date` datetime DEFAULT NULL,
    `record_create_date` datetime DEFAULT NULL,
    `record_update_date` datetime DEFAULT NULL
);

CREATE TABLE `debt` (
   `id` int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT,
   `transaction_id` int(11) DEFAULT NULL,
   `sender_user_id` int(11) DEFAULT NULL,
   `recipient_user_id` int(11) DEFAULT NULL,
   `amount` int(20) DEFAULT NULL,
   `record_create_date` datetime DEFAULT NULL,
   `record_update_date` datetime DEFAULT NULL
);


