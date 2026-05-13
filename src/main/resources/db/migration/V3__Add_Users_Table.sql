-- V3__Add_Users_Table.sql
-- Users table for authentication

CREATE TABLE users (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL CHECK (role IN ('ADMIN', 'USER')),
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_users_username ON users(username);

-- Insert default admin user (password: admin123, bcrypt encoded)
INSERT INTO users (username, password, full_name, email, role, enabled)
VALUES (
    'admin',
    '$2a$10$xWzW7VgqaXxI4NqZDLGwduakqOYI1vfMgxjLWjCH4zlZfXwSYbxZe',
    'System Administrator',
    'admin@rentflow.com',
    'ADMIN',
    TRUE
);

-- Insert default user (password: user123, bcrypt encoded)
INSERT INTO users (username, password, full_name, email, role, enabled)
VALUES (
    'user',
    '$2a$10$Qh5Km4ydNgJD8WV5xF7uFOxB3KxCCKGz0l5W3X2Y9Z7L4W1G7H6Hm',
    'Demo User',
    'user@rentflow.com',
    'USER',
    TRUE
);
