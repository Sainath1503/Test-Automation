-- Database initialization script for Test Automation Framework
-- Creates products table and inserts sample data

CREATE DATABASE IF NOT EXISTS testautomation;
USE testautomation;

-- Drop table if exists
DROP TABLE IF EXISTS products;

-- Create products table
CREATE TABLE products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2),
    category VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Insert sample products
INSERT INTO products (name, description, price, category) VALUES
('Laptop', 'High-performance laptop with 16GB RAM and 512GB SSD', 999.99, 'Electronics'),
('Smartphone', 'Latest smartphone with advanced camera and 5G support', 699.99, 'Electronics'),
('Wireless Headphones', 'Premium noise-cancelling wireless headphones', 249.99, 'Accessories'),
('Gaming Mouse', 'Ergonomic gaming mouse with RGB lighting', 79.99, 'Accessories');

-- Verify data
SELECT * FROM products;
