-- src/test/resources/db/migration/h2/V1__create_customer_table.sql

CREATE TABLE customer
(
    id                CHAR(36)     NOT NULL PRIMARY KEY,
    version           BIGINT,
    full_name         VARCHAR(150) NOT NULL,
    email             VARCHAR(255) NOT NULL UNIQUE,
    phone             VARCHAR(15),
    registration_date TIMESTAMP    DEFAULT CURRENT_TIMESTAMP NOT NULL,
    last_update       TIMESTAMP    DEFAULT CURRENT_TIMESTAMP NOT NULL,
    status            BOOLEAN      DEFAULT TRUE NOT NULL
);