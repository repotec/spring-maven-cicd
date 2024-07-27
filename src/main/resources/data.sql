DROP TABLE IF EXISTS employees;

CREATE TABLE `employees` (
  `id` int PRIMARY KEY,
  `first_name` varchar(100) NOT NULL,
  `last_name` varchar(100) NOT NULL
);

INSERT INTO `employees` (`id`,`first_name`,`last_name`)
 VALUES (1, 'Ahmed','Mohammed');

INSERT INTO `employees` (`id`,`first_name`,`last_name`)
 VALUES (2, 'Sara', 'Gamal');
