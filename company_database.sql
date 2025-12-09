CREATE DATABASE company_database;
use company_database;

CREATE TABLE login_credentials(
username varchar(10) PRIMARY KEY,
password varchar(20),
role ENUM('hr_admin', 'employee') );

INSERT INTO login_credentials(username, password, role)
VALUES ('hr_jameson', '123456', 'hr_admin'),
('emp_avery', '345678', 'employee'),
('emp_aaron', '456789', 'employee'),
('emp_percy', '012345', 'employee'),
('emp_mare', '678901', 'employee');

CREATE TABLE employees(
empid INT PRIMARY KEY,
fname varchar(20),
lname varchar(20),
dob DATE,
ssn char(9),
hire_date DATE );

INSERT INTO employees(empid, fname, lname, dob, ssn, hire_date)
VALUES (101, 'Avery', 'Grambs', '2000-11-10', '111111111', '2024-11-10'),
(102, 'Aaron', 'Warner', '2002-12-08', '222222222', '2024-12-08'),
(103, 'Percy', 'Jackson', '1998-09-12', '333333333', '2024-09-12'),
(104, 'Mare', 'Barrow', '2003-04-01', '444444444', '2024-04-01');

CREATE TABLE city(
city_id INT PRIMARY KEY,
city_name varchar(30)
);

INSERT INTO city(city_id, city_name)
VALUES (01, 'New York City'),
(02, 'Atlanta'),
(03, 'Miami'),
(04, 'Hoboken'),
(05, 'Cincinnati'),
(06, 'Detroit'),
(07, 'Sacramento'),
(08, 'Austin'),
(09, 'Kennesaw'),
(10, 'Mason'),
(11, 'Ann Arbor'),
(12, 'San Francisco'),
(13, 'Smyrna'),
(14, 'Marietta'),
(15, 'Seattle'),
(16, 'Chicago'),
(17, 'Houston'),
(18, 'Dallas'),
(19, 'Orlando'),
(20, 'Columbus');

CREATE TABLE state(
state_id INT PRIMARY KEY,
state_name varchar(30)
);

INSERT INTO state(state_id, state_name)
VALUES (1, 'Georgia'),
(2, 'Florida'),
(3, 'Alabama'),
(4, 'Tennessee'),
(5, 'South Carolina'),
(6, 'North Carolina'),
(7, 'Virginia'),
(8, 'West Virginia'),
(9, 'Kentucky'),
(10, 'Mississippi'),
(11, 'Louisiana'),
(12, 'Texas'),
(13, 'Arkansas'),
(14, 'Missouri'),
(15, 'Illinois'), 
(16, 'Indiana'),
(17, 'Ohio'),
(18, 'Michigan'),
(19, 'Wisconsin'),
(20, 'Minnesota'),
(21, 'Iowa'),
(22, 'Nebraska'),
(23, 'Kansas'),
(24, 'Oklahoma'),
(25, 'New Mexico'),
(26, 'Arizona'),
(27, 'California'),
(28, 'Nevada'),
(29, 'Utah'),
(30, 'Colorado'),
(31, 'Wyoming'),
(32, 'Montana'),
(33, 'Idaho'),
(34, 'Washington'),
(35, 'Oregon'),
(36, 'North Dakota'),
(37, 'South Dakota'),
(38, 'New York'),
(39, 'New Jersey'),
(40, 'Pennsylvania'),
(41, 'Delaware'),
(42, 'Maryland'),
(43, 'Connecticut'),
(44, 'Rhode Island'),
(45, 'Massachusetts'),
(46, 'Vermont'),
(47, 'New Hampshire'),
(48, 'Maine'),
(49, 'Hawaii'),
(50, 'Alaska');

CREATE TABLE address(
empid INT PRIMARY KEY,
street varchar(50),
city_id INT,
state_id INT,
zip_code char(5),
gender varchar(20),
identified_race varchar(30),
dob DATE, phone_number varchar(10), 

FOREIGN KEY (empid) REFERENCES employees(empid),
FOREIGN KEY (city_id) REFERENCES city(city_id),
FOREIGN KEY (state_id) REFERENCES state(state_id) );

INSERT INTO address(empid, street, city_id, state_id, zip_code, gender, identified_race, dob, phone_number)
VALUES (101, '404 Queens St.', 08, 12, 12345, 'female', 'White', '2000-11-10', '1234567890'),
(102, '223 Luc Rd.', 16, 15, 23456, 'male', 'Native American', '2002-12-08', '2345678901'),
(103, '657 Partie Corner', 01, 38, 34567, 'male', 'Black or African American', '1998-09-12', '3456789012'),
(104, '98 Pane St.', 12, 27, 45678, 'female', 'Asian', '2003-04-01', '4567890123');

CREATE TABLE division(
ID INT PRIMARY KEY,
division_name varchar(50)
);

INSERT INTO division(ID, division_name)
VALUES (01, 'IT'),
(02, 'Marketing'),
(03, 'Business'),
(04, 'Finance'),
(05, 'HR');

CREATE TABLE employee_division(
empid INT,
div_ID INT,

PRIMARY KEY (empid, div_ID),
FOREIGN KEY (empid) REFERENCES employees(empid),
FOREIGN KEY (div_ID) REFERENCES division(ID) );

INSERT INTO employee_division(empid, div_ID)
VALUES (101, 04),
(102, 03),
(103, 02),
(104, 01);

CREATE TABLE job_titles(
job_title_id INT PRIMARY KEY,
job_title varchar(20)
);

INSERT INTO job_titles(job_title_id, job_title)
VALUES (1, 'Software Engineer'),
(2, 'Accountant'),
(3, 'Product Manager'),
(4, 'Marketing Specialist'),
(5, 'Business Analyst');

CREATE TABLE employee_job_titles(
empid INT,
job_title_id INT,

PRIMARY KEY (empid, job_title_id),
FOREIGN KEY (empid) REFERENCES employees(empid),
FOREIGN KEY (job_title_id) REFERENCES job_titles(job_title_id)
);

INSERT INTO employee_job_titles(empid, job_title_id)
VALUES (101, 5),
(102, 3),
(103, 2),
(104, 1);

CREATE TABLE payments(
payment_id INT PRIMARY KEY AUTO_INCREMENT,
empid INT,
payment_date DATE,
total_pay DECIMAL(10, 2),
hours_worked DECIMAL(5,2),

FOREIGN KEY (empid) REFERENCES employees(empid)
);

INSERT INTO payments(empid, payment_date, total_pay, hours_worked)
VALUES (101, '2025-11-01', 5500, 40),
(101, '2025-11-15', 5900, 42),
(103, '2025-11-01', 1700, 38),
(103, '2025-11-15', 1720, 40);

