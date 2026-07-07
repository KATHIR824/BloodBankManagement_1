-- ============================================
-- Blood Bank Management System - Database Schema
-- MySQL
-- If you already have a database, just make sure your
-- table/column names match these (or edit the DAO files
-- to match YOUR existing column names).
-- ============================================

CREATE DATABASE IF NOT EXISTS bloodbank;
USE bloodbank;

-- Login table (for admin / staff login)
CREATE TABLE IF NOT EXISTS login (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(50) NOT NULL,
    role VARCHAR(20) DEFAULT 'admin'
);

INSERT INTO login (username, password, role)
VALUES ('admin', 'admin123', 'admin')
ON DUPLICATE KEY UPDATE username = username;

-- Donor table
CREATE TABLE IF NOT EXISTS donor (
    donor_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    gender VARCHAR(10),
    blood_group VARCHAR(5) NOT NULL,
    contact VARCHAR(15),
    address VARCHAR(200),
    last_donation_date DATE
);

-- Blood stock table (units available per blood group)
CREATE TABLE IF NOT EXISTS blood_stock (
    blood_group VARCHAR(5) PRIMARY KEY,
    units_available INT NOT NULL DEFAULT 0
);

INSERT INTO blood_stock (blood_group, units_available) VALUES
('A+', 0), ('A-', 0), ('B+', 0), ('B-', 0),
('AB+', 0), ('AB-', 0), ('O+', 0), ('O-', 0)
ON DUPLICATE KEY UPDATE blood_group = blood_group;

-- Blood request table (recipients requesting blood)
CREATE TABLE IF NOT EXISTS blood_request (
    request_id INT AUTO_INCREMENT PRIMARY KEY,
    patient_name VARCHAR(100) NOT NULL,
    blood_group VARCHAR(5) NOT NULL,
    units_required INT NOT NULL,
    contact VARCHAR(15),
    request_date DATE,
    status VARCHAR(20) DEFAULT 'Pending'
);
