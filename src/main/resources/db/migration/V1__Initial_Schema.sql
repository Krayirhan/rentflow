-- V1__Initial_Schema.sql
-- PostgreSQL schema for RentFlow

CREATE TABLE vehicles (
    plate VARCHAR(255) PRIMARY KEY,
    brand VARCHAR(255) NOT NULL,
    model VARCHAR(255) NOT NULL,
    year INT NOT NULL,
    daily_price DECIMAL(10, 2) NOT NULL,
    status VARCHAR(50) NOT NULL CHECK (status IN ('AVAILABLE', 'RENTED')),
    vehicle_type VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE cars (
    plate VARCHAR(255) PRIMARY KEY REFERENCES vehicles(plate) ON DELETE CASCADE,
    seat_count INT NOT NULL
);

CREATE TABLE motorcycles (
    plate VARCHAR(255) PRIMARY KEY REFERENCES vehicles(plate) ON DELETE CASCADE,
    helmet_included BOOLEAN NOT NULL
);

CREATE TABLE customers (
    id VARCHAR(255) PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE rentals (
    id VARCHAR(255) PRIMARY KEY,
    customer_id VARCHAR(255) NOT NULL REFERENCES customers(id),
    vehicle_plate VARCHAR(255) NOT NULL REFERENCES vehicles(plate),
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    status VARCHAR(50) NOT NULL CHECK (status IN ('ACTIVE', 'COMPLETED', 'CANCELLED')),
    actual_return_date DATE,
    late_fee DECIMAL(10, 2) DEFAULT 0,
    final_price DECIMAL(10, 2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_rentals_customer_id ON rentals(customer_id);
CREATE INDEX idx_rentals_vehicle_plate ON rentals(vehicle_plate);
CREATE INDEX idx_rentals_status ON rentals(status);
CREATE INDEX idx_vehicles_status ON vehicles(status);
