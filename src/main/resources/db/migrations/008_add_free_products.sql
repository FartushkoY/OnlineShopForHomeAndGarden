-- liquibase formatted sql
-- changeset yevheniia:008

insert into products (category_id, product_id, name, description, price, discount_price, image_url, created_at, updated_at)
values  (1, 20,'Free Product 1', 'Product not added to cart, favorites and order', 2.00, 0.00, 'https://www.almanac.com/sites/default/files/styles/or/public/image_nodes/tulips-multicolored_0.jpg?itok=5KFk7THG', current_timestamp(), current_timestamp()),
        (1, 21,'Free Product 2', 'Product not added to cart, favorites and order', 2.00, 0.00, 'https://www.almanac.com/sites/default/files/styles/or/public/image_nodes/tulips-multicolored_0.jpg?itok=5KFk7THG', current_timestamp(), current_timestamp());