-- ============================================================
-- Bank Management System - MySQL Database Schema
-- Run this file ONCE to set up the database
-- ============================================================

-- Step 1: Create the database
CREATE DATABASE IF NOT EXISTS bank_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

-- Step 2: Use the database
USE bank_db;

-- ============================================================
-- Table: users
-- Stores registered bank customers
-- ============================================================
CREATE TABLE IF NOT EXISTS users (
    id          BIGINT          NOT NULL AUTO_INCREMENT,
    name        VARCHAR(100)    NOT NULL,
    email       VARCHAR(150)    NOT NULL UNIQUE,
    password    VARCHAR(255)    NOT NULL,   -- BCrypt hashed, never plain text
    role        VARCHAR(20)     NOT NULL DEFAULT 'USER',  -- USER or ADMIN
    created_at  TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    INDEX idx_users_email (email)
);

-- ============================================================
-- Table: accounts
-- Each user can have multiple bank accounts
-- ============================================================
CREATE TABLE IF NOT EXISTS accounts (
    account_id      BIGINT          NOT NULL AUTO_INCREMENT,
    user_id         BIGINT          NOT NULL,
    account_number  VARCHAR(20)     NOT NULL UNIQUE,   -- e.g. ACC1234567
    balance         DECIMAL(15, 2)  NOT NULL DEFAULT 0.00,
    account_type    VARCHAR(20)     NOT NULL DEFAULT 'SAVINGS',  -- SAVINGS or CURRENT
    created_at      TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (account_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_accounts_user_id (user_id),
    INDEX idx_accounts_number (account_number)
);

-- ============================================================
-- Table: transactions
-- Records every financial operation
-- ============================================================
CREATE TABLE IF NOT EXISTS transactions (
    transaction_id  BIGINT          NOT NULL AUTO_INCREMENT,
    account_id      BIGINT          NOT NULL,
    type            VARCHAR(20)     NOT NULL,   -- DEPOSIT, WITHDRAWAL, TRANSFER_IN, TRANSFER_OUT
    amount          DECIMAL(15, 2)  NOT NULL,
    balance_after   DECIMAL(15, 2),             -- Account balance after this transaction
    description     VARCHAR(255),
    timestamp       TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (transaction_id),
    FOREIGN KEY (account_id) REFERENCES accounts(account_id) ON DELETE CASCADE,
    INDEX idx_transactions_account_id (account_id),
    INDEX idx_transactions_timestamp (timestamp)
);

-- ============================================================
-- Optional: Sample data for testing
-- Uncomment below if you want demo data
-- (Passwords are BCrypt hashes of "password123")
-- ============================================================

/*
INSERT INTO users (name, email, password) VALUES
    ('Alice Johnson', 'alice@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'),
    ('Bob Smith',     'bob@example.com',   '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy');

INSERT INTO accounts (user_id, account_number, balance, account_type) VALUES
    (1, 'ACC1000001', 50000.00, 'SAVINGS'),
    (1, 'ACC1000002', 15000.00, 'CURRENT'),
    (2, 'ACC1000003', 30000.00, 'SAVINGS');

INSERT INTO transactions (account_id, type, amount, balance_after, description) VALUES
    (1, 'DEPOSIT',    50000.00, 50000.00, 'Initial deposit'),
    (2, 'DEPOSIT',    15000.00, 15000.00, 'Opening balance'),
    (3, 'DEPOSIT',    30000.00, 30000.00, 'Initial deposit');
*/

-- ============================================================
-- Verify tables were created
-- ============================================================
SHOW TABLES;
