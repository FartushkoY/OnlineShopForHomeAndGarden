-- liquibase formatted sql
-- changeset yevheniia:005

UPDATE products
SET
    name = 'Camomile'
WHERE
    product_id = 2;
