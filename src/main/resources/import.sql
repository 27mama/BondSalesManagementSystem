DROP TABLE IF EXISTS `bonds_sales_record`;
CREATE TABLE `bonds_sales_record` (
  `id` INT(11) AUTO_INCREMENT NOT NULL,
  `bonds_name` VARCHAR(255) DEFAULT NULL,
  `sales_name` VARCHAR(255) DEFAULT NULL,
  `amount` INT(11) DEFAULT NULL,
  `created_at` DATETIME DEFAULT NULL,
  `updated_at` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` INT(11) AUTO_INCREMENT NOT NULL,
  `name` VARCHAR(255),
  `pass` VARCHAR(255) NOT NULL,
  `created_at` DATETIME DEFAULT NULL,
  `updated_at` DATETIME DEFAULT NULL,
  `status` TINYINT(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
);

INSERT INTO bonds_sales_record (id, bonds_name,sales_name,amount,created_at,updated_at) VALUES (1,'A bounds','Sam',1000,'2018-12-30 21:50:50','2020-1-30 12:51:50');
INSERT INTO bonds_sales_record (id, bonds_name,sales_name,amount,created_at,updated_at) VALUES (2,'B bounds','Jack',4000,'2015-3-5 13:43:12','2021-5-4 10:12:52');
INSERT INTO bonds_sales_record (id, bonds_name,sales_name,amount,created_at,updated_at) VALUES (3,'C bounds','Terry',5000,'2019-1-12 18:00:10','2021-07-31 13:50:50');

INSERT INTO USER (id, NAME,pass,created_at,updated_at) VALUES (1,'Sam','123','2018-12-30 21:50:50','2020-1-30 12:51:50');
INSERT INTO USER (id, NAME,pass,created_at,updated_at) VALUES (2,'Jack','456','2015-3-5 13:43:12','2021-5-4 10:12:52');
INSERT INTO USER (id, NAME,pass,created_at,updated_at) VALUES (3,'Terry','789','2019-1-12 18:00:10','2021-07-31 13:50:50');