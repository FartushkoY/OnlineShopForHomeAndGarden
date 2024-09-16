-- liquibase formatted sql
-- changeset yevheniia:007

insert into orders (order_id, user_id, created_at, delivery_address, contact_phone, delivery_method, status, updated_at)
values
    (26, 2, '2024-05-04 19:58:03', 'Friedrichstraße 123, 10117, Berlin, Germany', '+491668019467', 'STANDARD', 'PAID', '2024-05-05 15:58:03'),
    (27, 2, '2024-05-04 15:58:03', 'Friedrichstraße 123, 10117, Berlin, Germany', '+491668019467', 'STANDARD', 'DELIVERED', '2024-05-06 15:58:03'),
    (28, 2, '2024-05-04 15:58:03', 'Friedrichstraße 123, 10117, Berlin, Germany', '+491668019467', 'STANDARD', 'PAID', '2024-05-06 12:58:03'),
    (29, 2, '2024-05-04 15:58:03', 'Friedrichstraße 123, 10117, Berlin, Germany', '+491668019467', 'STANDARD', 'DELIVERED', '2024-05-06 12:38:03'),
    (30, 2, '2024-05-04 15:58:03', 'Friedrichstraße 123, 10117, Berlin, Germany', '+491668019467', 'STANDARD', 'ON_THE_WAY', '2024-06-06 15:58:03'),
    (31, 2, '2024-05-04 15:58:03', 'Friedrichstraße 123, 10117, Berlin, Germany', '+491668019467', 'STANDARD', 'ON_THE_WAY', '2024-06-06 17:58:03'),
    (32, 2, '2024-05-04 15:58:03', 'Friedrichstraße 123, 10117, Berlin, Germany', '+491668019467', 'STANDARD', 'ON_THE_WAY', '2024-06-06 17:58:03'),
    (33, 2, '2023-05-04 15:58:03', 'Friedrichstraße 123, 10117, Berlin, Germany', '+491668019467', 'STANDARD', 'ON_THE_WAY', '2023-06-06 17:58:03'),
    (34, 2, '2023-05-04 15:58:03', 'Friedrichstraße 123, 10117, Berlin, Germany', '+491668019467', 'STANDARD', 'ON_THE_WAY', '2023-06-06 17:58:03');

insert into order_items (order_item_id, order_id, product_id, quantity, price_at_purchase)
values
    (33, 26, 6, 5, 11.05),
    (34, 27, 4, 2, 10.00),
    (35, 28, 6, 1, 11.05),
    (36, 29, 4, 3, 10.00),
    (37, 30, 6, 5, 11.05),
    (38, 31, 4, 4, 10.00),
    (39, 32, 6, 2, 11.05),
    (40, 33, 4, 3, 10.00),
    (41, 34, 6, 1, 11.05);
