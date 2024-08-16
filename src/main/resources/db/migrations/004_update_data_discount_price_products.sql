-- liquibase formatted sql
-- changeset yevheniia:004

UPDATE products
SET
    discount_price = null
WHERE
    discount_price = 0;

UPDATE products
SET
    discount_price = 10.5
WHERE
    product_id = 6;
