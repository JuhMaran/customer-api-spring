CREATE TABLE customer
(
    id                CHAR(36)     NOT NULL PRIMARY KEY,
    version           BIGINT,
    full_name         VARCHAR(150) NOT NULL,
    email             VARCHAR(255) NOT NULL UNIQUE,
    phone             VARCHAR(15),
    registration_date TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_update       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    status            BOOLEAN      NOT NULL DEFAULT TRUE
);