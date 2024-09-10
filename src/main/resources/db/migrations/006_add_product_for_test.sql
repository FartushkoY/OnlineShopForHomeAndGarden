-- liquibase formatted sql
-- changeset yevheniia:006

insert into products (category_id, product_id, name, description, price, discount_price, image_url, created_at,
                      updated_at)
values (4, 16, '222Greek Pot',
        'Add a touch of ancient elegance with our Greek Pot. Crafted for both style and durability, this planter elevates your plants, turning your garden into a timeless sanctuary.',
        30.00, 10.00,
        'https://raw.githubusercontent.com/tel-ran-de/telran_backend_garden_shop/master/public/product_img/1.jpeg',
        current_timestamp(), current_timestamp()),
(4, 17, '333Greek Pot', 'Add a touch of ancient elegance with our Greek Pot. Crafted for both style and durability, this planter elevates your plants, turning your garden into a timeless sanctuary.', 30.00, 10.00, 'https://raw.githubusercontent.com/tel-ran-de/telran_backend_garden_shop/master/public/product_img/1.jpeg', current_timestamp (), current_timestamp ());