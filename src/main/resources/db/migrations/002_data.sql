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

insert into users (user_id, name, email, phone_number, password_hash, role)
values (1, 'John Doe', 'john.doe@example.com', '+491708009466', 'strawberry_123', 'CUSTOMER'),
       (2, 'Jane Smith','jane.smith@example.com', '+491668019467','raspberry_456', 'CUSTOMER'),
       (3, 'Alice Jones','alice.jones@example.com', '+491678019468', 'blueberry_789',  'CUSTOMER'),
       (4, 'Masha Voytovych', 'mary.voytovych@gmail.com', '+491688019469', 'tomato_113', 'ADMINISTRATOR'),
       (5, 'Iryna Kosiakovska', 'iryna@example.com', '+491658019460', 'potato_112', 'ADMINISTRATOR'),
       (6, 'Yevgenia Fartushko','yevgenia@example.com', '+491648019471', 'cucumber_111', 'ADMINISTRATOR'),
       (7, 'Michael Brown', 'michael.brown@example.com', '+491628029468', 'Tomato_789', 'CUSTOMER'),
       (8, 'Emily Davis', 'emily.davis@example.com', '+491608039469', 'blacKberry_101', 'CUSTOMER');

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
       (5, 8, current_timestamp(), 'Karslruherstrasse 11, 10243, Schwetzingen, Germany', '+491678044468', 'EXPRESS', 'PENDING', current_timestamp()),
       (6, 2, current_timestamp(), 'Friedrichstraße 123, 10117, Berlin, Germany', '+491668019467', 'STANDARD', 'PAID', current_timestamp()),
       (7, 1, current_timestamp(), 'Kurfürstendamm 45, 10719, Berlin, Germany', '+491708009466', 'EXPRESS', 'DELIVERED', current_timestamp()),
       (8, 3, current_timestamp(), 'Karl-Marx-Allee 50, 10243, Berlin, Germany', '+491678019468', 'STANDARD', 'ON_THE_WAY', current_timestamp()),
       (9, 7, current_timestamp(), 'Schlossstasse 45, 10719, Berlin, Germany', '+491707709466', 'STANDARD', 'ON_THE_WAY', current_timestamp()),
       (10, 8, current_timestamp(), 'Karslruherstrasse 11, 10243, Schwetzingen, Germany', '+491678044468', 'EXPRESS', 'PENDING', current_timestamp());

insert into order_items (order_item_id, order_id, product_id, quantity, price_at_purchase)
values (1, 1, 6, 5, 11.05),
       (2, 2, 11, 3, 20.00),
       (3, 3, 15, 1, 20.00),
       (4, 4, 8, 2, 7.00),
       (5, 5, 9, 4, 8.50),
       (6, 6, 1, 11, 8.50),
       (7, 6, 3, 24, 8.50),
       (8, 8, 5, 54, 8.50),
       (9, 9, 12, 34, 8.50),
       (10, 10, 15, 74, 8.50),
       (11, 10, 2, 24, 18.50),
       (12, 10, 7, 40, 8.50);
