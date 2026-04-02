CREATE TABLE customer
(
    id                CHAR(36) PRIMARY KEY,
    version           INT                 NOT NULL DEFAULT 1,
    full_name         VARCHAR(150)        NOT NULL,
    email             VARCHAR(100) UNIQUE NOT NULL,
    phone             VARCHAR(20),
    registration_date TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_update       TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    status            BOOLEAN             NOT NULL DEFAULT TRUE,
    CHECK (CHAR_LENGTH(full_name) >= 3)
);