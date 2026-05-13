-- V2__Sample_Data.sql
-- Sample data for RentFlow

-- Sample Customers
INSERT INTO customers (id, full_name, phone_number) VALUES
    ('C001', 'Ahmet Yılmaz', '0532 111 2222'),
    ('C002', 'Ayşe Demir', '0532 333 4444'),
    ('C003', 'Mehmet Kaya', '0532 555 6666'),
    ('C004', 'Fatma Çetin', '0532 777 8888'),
    ('C005', 'İbrahim Özdemir', '0532 999 0000')
ON CONFLICT DO NOTHING;

-- Sample Cars
INSERT INTO vehicles (plate, brand, model, year, daily_price, status, vehicle_type) VALUES
    ('34ABC123', 'Toyota', 'Corolla', 2022, 150.00, 'AVAILABLE', 'car'),
    ('34ABC456', 'Renault', 'Megane', 2021, 130.00, 'AVAILABLE', 'car'),
    ('06XYZ789', 'Volkswagen', 'Golf', 2023, 180.00, 'AVAILABLE', 'car'),
    ('35DEF012', 'Honda', 'Civic', 2020, 140.00, 'RENTED', 'car')
ON CONFLICT DO NOTHING;

INSERT INTO cars (plate, seat_count) VALUES
    ('34ABC123', 5),
    ('34ABC456', 5),
    ('06XYZ789', 5),
    ('35DEF012', 5)
ON CONFLICT DO NOTHING;

-- Sample Motorcycles
INSERT INTO vehicles (plate, brand, model, year, daily_price, status, vehicle_type) VALUES
    ('34MOR999', 'Yamaha', 'MT-09', 2023, 100.00, 'AVAILABLE', 'motorcycle'),
    ('34KWT888', 'Kawasaki', 'Ninja', 2022, 110.00, 'AVAILABLE', 'motorcycle'),
    ('34HDS777', 'Harley-Davidson', 'Street 750', 2021, 150.00, 'AVAILABLE', 'motorcycle')
ON CONFLICT DO NOTHING;

INSERT INTO motorcycles (plate, helmet_included) VALUES
    ('34MOR999', true),
    ('34KWT888', true),
    ('34HDS777', true)
ON CONFLICT DO NOTHING;
