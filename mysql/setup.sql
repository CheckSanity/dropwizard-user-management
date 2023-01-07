CREATE DATABASE user_management_service;
USE user_management_service;
CREATE USER 'database_user'@'%' IDENTIFIED BY '123456';
GRANT ALL PRIVILEGES ON user_management_service.* TO 'database_user'@'%';

CREATE DATABASE user_management_service_test;
USE user_management_service_test;
CREATE USER 'database_user_test'@'%' IDENTIFIED BY '123456';
GRANT ALL PRIVILEGES ON user_management_service_test.* TO 'database_user_test'@'%';
