-- V2__update_customer_table_mysql.sql

-- 1. Ajustar tamanho do campo phone (E.164)
ALTER TABLE customer
    MODIFY phone VARCHAR(16);

-- 2. Adicionar coluna auxiliar para busca
ALTER TABLE customer
    ADD COLUMN phone_search VARCHAR(15);

-- 3. Migrar dados existentes (remove tudo que não é número)
UPDATE `customer`
SET phone_search = REGEXP_REPLACE(phone, '[^0-9]', '');

-- 4. Criar índice para busca performática
CREATE INDEX idx_customer_phone_search ON customer (phone_search);