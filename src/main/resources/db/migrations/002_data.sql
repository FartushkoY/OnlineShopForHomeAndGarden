-- liquibase formatted sql
-- changeset maria:002

insert into categories (category_id, name, image_url)
values (1, 'Planting material', 'https://raw.githubusercontent.com/tel-ran-de/telran_backend_garden_shop/master/public/category_img/1.jpeg'),
       (2, 'Protective products and septic tanks', 'https://raw.githubusercontent.com/tel-ran-de/telran_backend_garden_shop/master/public/category_img/2.jpeg'),
       (3, 'Fertilizer', 'https://raw.githubusercontent.com/tel-ran-de/telran_backend_garden_shop/master/public/category_img/3.jpeg'),
       (4, 'Pots and planters', 'https://raw.githubusercontent.com/tel-ran-de/telran_backend_garden_shop/master/public/category_img/4.jpeg'),
       (5, 'Tools and equipment', 'https://raw.githubusercontent.com/tel-ran-de/telran_backend_garden_shop/master/public/category_img/5.jpeg');

insert into products (category_id, product_id, name, description, price, discount_price, image_url, created_at, updated_at)
values  (1, 1,'Tulip', 'Elevate your garden with our exquisite Tulip planting material. Embrace the beauty of nature as these premium Tulip bulbs promise vibrant blooms in an array of enchanting colors. Cultivate your own floral haven effortlessly with our high-quality tulip bulbs. Transform your outdoor space into a mesmerizing tapestry of blossoms with ease. Shop now and let the elegance of Tulips grace your garden.', 2.00, 0.00, 'https://www.almanac.com/sites/default/files/styles/or/public/image_nodes/tulips-multicolored_0.jpg?itok=5KFk7THG', current_timestamp(), current_timestamp()),
        (1, 2,'Сhamomile', 'Elevate your garden with our premium Chamomile planting material. Cultivate a serene oasis with these fragrant blooms. Shop now for a tranquil touch to your green space.', 1.7, 0.00,'https://files.nccih.nih.gov/chamomile-steven-foster-square.jpg', current_timestamp(), current_timestamp()),
        (1, 3,'Marigold', 'Ignite your garden with vibrant Marigold blooms. Our superior planting material ensures a burst of color and vitality. Elevate your garden aesthetic—shop Marigold today.', 3.00, 0.00,'https://www.gardendesign.com/pictures/images/900x705Max/dream-team-s-portland-garden_6/marigold-flowers-orange-pixabay_12708.jpg', current_timestamp(), current_timestamp()),
        (2, 4,'Diatomaceous Earth', 'Safeguard your garden with Diatomaceous Earth. A natural defense against pests, it protects plants while promoting soil health. Choose eco-friendly protection for your garden.', 10.00, 0.00, 'https://raw.githubusercontent.com/tel-ran-de/telran_backend_garden_shop/master/public/product_img/29.jpeg', current_timestamp(), current_timestamp()),
        (2, 5,'Happy Frog', 'Nurture your garden with Happy Frog — your plant\'s best friend. This protective product enhances soil structure and fertility, ensuring a thriving, joyful garden. Discover the Happy Frog difference.', 12.00, 0.00, 'https://raw.githubusercontent.com/tel-ran-de/telran_backend_garden_shop/master/public/product_img/26.jpeg', current_timestamp(), current_timestamp()),
        (2, 6,'Horticultural Charcoal', 'Enhance your gardening experience with Horticultural Charcoal. This natural product purifies soil, prevents root rot, and promotes plant health. Elevate your garden\'s resilience.', 13.00, 15.00, 'https://raw.githubusercontent.com/tel-ran-de/telran_backend_garden_shop/master/public/product_img/28.jpeg', current_timestamp(), current_timestamp()),
        (3, 7, 'Organic Perlite', 'Fuel your plants\' growth with Organic Perlite. This lightweight, porous medium enhances aeration and drainage, ensuring optimal conditions for your garden. Choose organic vitality.', 6.00, 0.00, 'https://raw.githubusercontent.com/tel-ran-de/telran_backend_garden_shop/master/public/product_img/24.jpeg', current_timestamp(), current_timestamp()),
        (3, 8, 'Ocean Forest', 'Dive into luNourish your plants with our Potting Mix fertilizer. Perfectly blended for optimal growth, it provides the essential nutrients your plants crave. Cultivate a flourishing garden today.', 7.00, 0.00, 'https://raw.githubusercontent.com/tel-ran-de/telran_backend_garden_shop/master/public/product_img/27.jpeg', current_timestamp(), current_timestamp()),
        (3, 9, 'Potting Mix', 'Nourish your plants with our Potting Mix fertilizer. Perfectly blended for optimal growth, it provides the essential nutrients your plants crave. Cultivate a flourishing garden today.', 8.50, 0.00, 'https://raw.githubusercontent.com/tel-ran-de/telran_backend_garden_shop/master/public/product_img/23.jpeg', current_timestamp(), current_timestamp()),
        (4, 10, 'Greek Pot', 'Add a touch of ancient elegance with our Greek Pot. Crafted for both style and durability, this planter elevates your plants, turning your garden into a timeless sanctuary.', 30.00, 10.00, 'https://raw.githubusercontent.com/tel-ran-de/telran_backend_garden_shop/master/public/product_img/1.jpeg', current_timestamp(), current_timestamp()),
        (4, 11,'Wicker Pot', 'Embrace natural aesthetics with our Wicker Pot. Stylish and functional, it complements any garden space. Elevate your plant display with this charming wicker planter.', 20.00, 0.00, 'https://raw.githubusercontent.com/tel-ran-de/telran_backend_garden_shop/master/public/product_img/12.jpeg', current_timestamp(), current_timestamp()),
        (4, 12,'Red Pot', 'Make a bold statement in your garden with our vibrant Red Pot. Durable and eye-catching, it adds a pop of color to your green haven. Shop now for a standout planter.', 25.00, 0.00, 'https://raw.githubusercontent.com/tel-ran-de/telran_backend_garden_shop/master/public/product_img/10.jpeg', current_timestamp(), current_timestamp()),
        (5, 13, 'Shovel', 'Dig into gardening with our sturdy Shovel. Designed for durability and comfort, it is an essential tool for planting and landscaping. Elevate your gardening experience with quality tools.', 40.00, 20.00, 'https://assets.leevalley.com/Size4/10115/PG107-radius-ergonomic-stainless-steel-shovel-u-0195.jpg', current_timestamp(), current_timestamp()),
        (5, 14,'Rake', 'Maintain a pristine garden with our reliable Rake. Perfect for leaf and debris removal, it ensures a tidy outdoor space. Upgrade your gardening arsenal with this essential tool.', 38.00, 0.00, 'https://images.ctfassets.net/zma7thmmcinb/46JNtlvxFdhCD2XPHHziLc/31fe4425eff26086a7eb884a4384d85b/find-the-right-rake-plastic-rake.jpg', current_timestamp(), current_timestamp()),
        (5, 15,'Gardening scissors', 'Precision meets functionality with our Gardening Scissors. Trim and shape your garden with ease. Elevate your gardening skills with these sharp and durable scissors.', 20.00, 0.00, 'https://cdn.thewirecutter.com/wp-content/uploads/2015/06/pruningshears-2x1-.jpg?auto=webp&quality=75&crop=2:1&width=1024&dpr=2', current_timestamp(), current_timestamp());

insert into users (user_id, name, email, phone_number, password_hash, role, refresh_token)
values (1, 'John Doe', 'john.doe@example.com', '+491708009466', ' $2a$12$ZkOLIJH9xR0jeTi3YelB1uwHQupQJuuqhgwhE.lQ90zNfHBOy53oq', 'CUSTOMER', null),
       (2, 'Jane Smith', 'jane.smith@example.com', '+491668019467', '$2a$12$wJIhvOy9kUIUUEptpXyXzeBxh0ZN61/v9rKCgM6XV/59cMgznOcXG', 'CUSTOMER', null),
       (3, 'Alice Jones', 'alice.jones@example.com', '+491678019468', '$2a$12$VYRWC6Np01HnYyAvTiUa3uQdjSqmU4.aSdoToxek2R/0vDvhE.mri', 'CUSTOMER', null),
       (4, 'Masha Voytovych', 'mary.voytovych@gmail.com', '+491688019469', '$2a$12$szp/Y0q.3HO4N6EdL0qrAeiUbHAH/ONJzWk.vsGjTnKujeoWCQ0GC', 'ADMINISTRATOR', null),
       (5, 'Iryna Kosiakovska', 'iryna@example.com', '+491658019460', '$2a$12$UmK2TKq9URh21WY4yNfBX.5JYzKz6eMAgkFioDks5RRfpOoMV8hyW', 'ADMINISTRATOR', null),
       (6, 'Yevgenia Fartushko', 'yevgenia@example.com', '+491648019471', '$2a$12$GWqZn6HoVnX8d0Jn.BHoxe3/0gVBxh5aWV3ApyFSp5x48ZARTUx7m', 'ADMINISTRATOR', null),
       (7, 'Michael Brown', 'michael.brown@example.com', '+491628029468', ' $2a$12$JoSktOr4JA8ktxD/UZb/M.QIuxa87Y4M1S7an/A1CnXeSNoiBNvWa', 'CUSTOMER', null),
       (8, 'Emily Davis', 'emily.davis@example.com', '+491608039469', '$2a$12$ZpZcThLJ3WwAMlKSyfe9weCGRnMot.SVKS9MX6a3yTjT/UQQPgPsG', 'CUSTOMER', null);

insert into carts (cart_id, user_id)
values (1, 1),
       (2, 2),
       (3, 3),
       (4, 7),
       (5, 8);

insert into cart_items (cart_item_id, cart_id, product_id, quantity)
values (1, 2, 6, 5),
       (2, 1, 11, 3),
       (3, 3, 15, 1),
       (4, 4, 8, 2),
       (5, 5, 9, 4);

insert into favorites (favorite_id, user_id, product_id)
values (1, 2, 5),
       (2, 1, 10),
       (3, 3, 14);

insert into orders (order_id, user_id, created_at, delivery_address, contact_phone, delivery_method, status, updated_at)
values (1, 2, current_timestamp(), 'Friedrichstraße 123, 10117, Berlin, Germany', '+491668019467', 'STANDARD', 'PAID', current_timestamp()),
       (2, 1, current_timestamp(), 'Kurfürstendamm 45, 10719, Berlin, Germany', '+491708009466', 'EXPRESS', 'DELIVERED', current_timestamp()),
       (3, 3, current_timestamp(), 'Karl-Marx-Allee 50, 10243, Berlin, Germany', '+491678019468', 'STANDARD', 'CANCELED', current_timestamp()),
       (4, 7, current_timestamp(), 'Schlossstasse 45, 10719, Berlin, Germany', '+491707709466', 'STANDARD', 'ON_THE_WAY', current_timestamp()),
       (5, 8, current_timestamp(), 'Karslruherstrasse 11, 10243, Schwetzingen, Germany', '+491678044468', 'EXPRESS', 'PAID', current_timestamp()),
       (6, 2, current_timestamp(), 'Friedrichstraße 123, 10117, Berlin, Germany', '+491668019467', 'STANDARD', 'PAID', current_timestamp()),
       (7, 1, current_timestamp(), 'Kurfürstendamm 45, 10719, Berlin, Germany', '+491708009466', 'EXPRESS', 'DELIVERED', current_timestamp()),
       (8, 3, current_timestamp(), 'Karl-Marx-Allee 50, 10243, Berlin, Germany', '+491678019468', 'STANDARD', 'PENDING', current_timestamp()),
       (9, 7, current_timestamp(), 'Schlossstasse 45, 10719, Berlin, Germany', '+491707709466', 'STANDARD', 'ON_THE_WAY', current_timestamp()),
       (10, 8, current_timestamp(), 'Karslruherstrasse 11, 10243, Schwetzingen, Germany', '+491678044468', 'EXPRESS', 'PAID', current_timestamp()),
       (11, 2, current_timestamp(), 'Friedrichstraße 123, 10117, Berlin, Germany', '+491668019467', 'STANDARD', 'CANCELED', current_timestamp()),
       (12, 1, current_timestamp(), 'Kurfürstendamm 45, 10719, Berlin, Germany', '+491708009466', 'EXPRESS', 'CANCELED', current_timestamp()),
       (13, 3, current_timestamp(), 'Karl-Marx-Allee 50, 10243, Berlin, Germany', '+491678019468', 'STANDARD', 'CANCELED', current_timestamp()),
       (14, 7, current_timestamp(), 'Schlossstasse 45, 10719, Berlin, Germany', '+491707709466', 'STANDARD', 'CANCELED', current_timestamp()),
       (15, 8, current_timestamp(), 'Karslruherstrasse 11, 10243, Schwetzingen, Germany', '+491678044468', 'EXPRESS', 'CANCELED', current_timestamp()),
       (16, 2, current_timestamp(), 'Friedrichstraße 123, 10117, Berlin, Germany', '+491668019467', 'STANDARD', 'CANCELED', current_timestamp()),
       (17, 1, current_timestamp(), 'Kurfürstendamm 45, 10719, Berlin, Germany', '+491708009466', 'EXPRESS', 'CANCELED', current_timestamp()),
       (18, 3, current_timestamp(), 'Karl-Marx-Allee 50, 10243, Berlin, Germany', '+491678019468', 'STANDARD', 'CANCELED', current_timestamp()),
       (19, 7, current_timestamp(), 'Schlossstasse 45, 10719, Berlin, Germany', '+491707709466', 'STANDARD', 'CANCELED', current_timestamp()),
       (20, 8, current_timestamp(), 'Karslruherstrasse 11, 10243, Schwetzingen, Germany', '+491678044468', 'EXPRESS', 'CANCELED', current_timestamp()),
       (21, 7, current_timestamp(), 'Schlossstasse 45, 10719, Berlin, Germany', '+491707709466', 'STANDARD', 'CANCELED', current_timestamp()),
       (22, 8, current_timestamp(), 'Karslruherstrasse 11, 10243, Schwetzingen, Germany', '+491678044468', 'EXPRESS', 'CANCELED', current_timestamp());

insert into order_items (order_item_id, order_id, product_id, quantity, price_at_purchase)
values (1, 1, 6, 5, 11.05),
       (2, 2, 11, 3, 20.00),
       (3, 3, 15, 54, 20.00),
       (4, 4, 8, 2, 7.00),
       (5, 5, 9, 4, 8.50),
       (6, 6, 1, 11, 2.00),
       (7, 6, 3, 24, 3.00),
       (8, 8, 5, 1, 12.00),
       (9, 9, 12, 34, 25.00),
       (10, 10, 15, 74, 20.00),
       (11, 10, 2, 24, 1.7),
       (12, 10, 13, 40, 40.00),
       (13, 11, 2, 1, 1.7),
       (14, 12, 8, 1, 7.00),
       (15, 13, 10, 1, 30.00),
       (16, 13, 11, 1, 20.00),
       (17, 14, 14, 1, 20.00),
       (18, 14, 8, 1, 7.00),
       (19, 15, 9, 1, 8.50),
       (20, 16, 4, 1, 10.00),
       (21, 17, 3, 2, 3.00),
       (22, 18, 5, 5, 12.00),
       (23, 19, 4, 3, 10.00),
       (24, 20, 14, 1, 20.00),
       (25, 20, 10, 1, 30.00),
       (26, 21, 13, 2, 40.00),
       (27, 22, 5, 5, 12.00),
       (28, 22, 12, 3, 25.00),
       (29, 22, 14, 4, 38.00);
