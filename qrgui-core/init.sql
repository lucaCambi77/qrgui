CREATE DATABASE IF NOT EXISTS test;
CREATE USER 'db_user'@'localhost' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON *.* TO 'db_user'@'localhost' WITH GRANT OPTION;
CREATE USER 'db_user'@'%' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON *.* TO 'db_user'@'%' WITH GRANT OPTION;
FLUSH PRIVILEGES;

use test;

CREATE TABLE IF NOT EXISTS GENERIC_TABLE (id int, name varchar(255));

TRUNCATE TABLE GENERIC_TABLE;

INSERT INTO GENERIC_TABLE VALUES (1, 'TEST_1');
INSERT INTO GENERIC_TABLE VALUES (2, 'TEST_2');
INSERT INTO GENERIC_TABLE VALUES (3, 'TEST_3');
INSERT INTO GENERIC_TABLE VALUES (4, 'TEST_4');
INSERT INTO GENERIC_TABLE VALUES (5, 'TEST_5');
INSERT INTO GENERIC_TABLE VALUES (6, 'TEST_6');
INSERT INTO GENERIC_TABLE VALUES (7, 'TEST_7');
INSERT INTO GENERIC_TABLE VALUES (8, 'TEST_8');
INSERT INTO GENERIC_TABLE VALUES (9, 'TEST_9');
INSERT INTO GENERIC_TABLE VALUES (10, 'TEST_10');
INSERT INTO GENERIC_TABLE VALUES (11, 'TEST_11');
INSERT INTO GENERIC_TABLE VALUES (12, 'TEST_12');
INSERT INTO GENERIC_TABLE VALUES (13, 'TEST_13');
INSERT INTO GENERIC_TABLE VALUES (14, 'TEST_14');
INSERT INTO GENERIC_TABLE VALUES (15, 'TEST_15');
INSERT INTO GENERIC_TABLE VALUES (16, 'TEST_16');