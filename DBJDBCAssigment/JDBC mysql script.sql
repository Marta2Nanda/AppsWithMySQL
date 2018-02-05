DROP DATABASE IF EXISTS JDBCProject;
CREATE DATABASE IF NOT EXISTS JDBCProject;
USE JDBCProject;

DROP TABLE IF EXISTS Devices;
DROP TABLE IF EXISTS user_preferences;
DROP TABLE IF EXISTS Sales;

CREATE TABLE Devices (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
	type VARCHAR(30) NOT NULL,
    feature VARCHAR(30) NOT NULL,
	rating INTEGER (3) NOT NULL

);

CREATE TABLE user_preferences (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
	gender VARCHAR(10) NOT NULL,
	wanted_feature VARCHAR (30) NOT NULL,
    rating INTEGER (3) NOT NULL

);
CREATE TABLE sales (
    sales_no INTEGER AUTO_INCREMENT PRIMARY KEY,
	device VARCHAR(30) NOT NULL,
	feature_selected Varchar (30) NOT NULL,
    quantity INTEGER (3) NOT NULL

);

INSERT INTO devices VALUES 
( null, 'Android', 'battery life', 56),
( null, 'Android', 'ease of use', 33),
( null, 'Android', 'touch screen', 35),
( null, 'Android', 'screen size', 37),
( null, 'Android', 'camera quality', 25),
( null, 'Android', 'speed', 23),
( null, 'iOS', 'battery life', 49),
( null, 'iOS', 'ease of use', 39),
( null, 'iOS', 'touch screen', 34),
( null, 'iOS', 'screen size', 22),
( null, 'iOS', 'camera quality', 19),
( null, 'iOS', 'speed', 22),
( null, 'Windows', 'battery life', 53),
( null, 'Windows', 'ease of use', 38),
( null, 'Windows', 'touch screen', 47),
( null, 'Windows', 'screen size', 34),
( null, 'Windows', 'camera quality', 23),
( null, 'Windows', 'speed', 22);

INSERT INTO user_preferences VALUES 
( null, 'female', 'battery life', 88),
( null, 'male', 'battery life', 90),
( null, 'female', 'ease of use', 64),
( null, 'male', 'ease of use', 50),
( null, 'female', 'operating system', 65),
( null, 'male', 'operating system', 71),
( null, 'female', 'touch screen', 25),
( null, 'male', 'touch screen', 16),
( null, 'female', 'screen size', 64),
( null, 'male', 'screen size', 56),
( null, 'female', 'camera quality', 60),
( null, 'male', 'camera quality', 70),
( null, 'female', 'speed', 74),
( null, 'male', 'speed', 61);

Select * from devices;
select * from sales;
select * from user_preferences;
