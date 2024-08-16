-- liquibase formatted sql
-- changeset yevheniia:003

ALTER TABLE products
    MODIFY COLUMN discount_price DECIMAL NULL;
