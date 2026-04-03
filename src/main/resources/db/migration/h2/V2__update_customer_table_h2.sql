-- V2__update_customer_table_h2.sql

-- 1. Ajustar tamanho do campo phone
ALTER TABLE customer
    ALTER COLUMN phone VARCHAR(16);

-- 2. Adicionar coluna auxiliar
ALTER TABLE customer
    ADD COLUMN phone_search VARCHAR(15);

-- 3. Migração simplificada (remove apenas '+')
UPDATE customer
SET phone_search = REPLACE(phone, '+', '');

-- 4. Criar índice
CREATE INDEX idx_customer_phone_search ON customer(phone_search);