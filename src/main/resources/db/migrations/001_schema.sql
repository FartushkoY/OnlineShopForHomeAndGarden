-- liquibase formatted sql
-- changeset maria:001


CREATE TABLE IF NOT EXISTS `categories` (
`category_id` INT NOT NULL AUTO_INCREMENT,
`name` VARCHAR(45) NOT NULL,
`image_url` VARCHAR(255) NOT NULL,
 PRIMARY KEY (`category_id`));

CREATE TABLE IF NOT EXISTS `products` (
`product_id` INT NOT NULL AUTO_INCREMENT,
`category_id` INT,
`name` VARCHAR(100) NOT NULL,
`description` TEXT NOT NULL,
`price` DECIMAL(7,2) NOT NULL,
`image_url` VARCHAR(255) NOT NULL,
`discount_price` DECIMAL NOT NULL,
`created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
`updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`product_id`),
    INDEX `fk_products_categories1_idx` (`category_id` ASC) VISIBLE,
    CONSTRAINT `fk_products_categories1`
    FOREIGN KEY (`category_id`)
    REFERENCES `categories` (`category_id`)
);

CREATE TABLE IF NOT EXISTS `users` (
`user_id` INT NOT NULL AUTO_INCREMENT,
`name` VARCHAR(45) NOT NULL,
`email` VARCHAR(45) NOT NULL,
`phone_number` VARCHAR(13) NOT NULL,
`password_hash` VARCHAR(45) NOT NULL,
`role` ENUM ('CUSTOMER', 'ADMINISTRATOR') NOT NULL,
    PRIMARY KEY (`user_id`),
    UNIQUE INDEX `Email_UNIQUE` (`email` ASC) VISIBLE);
--

CREATE TABLE IF NOT EXISTS `carts` (
`cart_id` INT NOT NULL AUTO_INCREMENT,
`user_id` INT NOT NULL,
 PRIMARY KEY (`cart_id`),
    INDEX `fk_carts_users1_idx` (`user_id` ASC) VISIBLE,
    CONSTRAINT `fk_carts_users1`
    FOREIGN KEY (`user_id`)
    REFERENCES `users` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
--

CREATE TABLE IF NOT EXISTS `cart_items` (
`cart_item_id` INT NOT NULL AUTO_INCREMENT,
`product_id` INT NOT NULL,
`cart_id` INT NOT NULL,
`quantity` INT NOT NULL,
PRIMARY KEY (`cart_item_id`),
    INDEX `fk_cart_items_products1_idx` (`product_id` ASC) VISIBLE,
    INDEX `fk_CartItems_cart1_idx` (`cart_id` ASC) VISIBLE,
    CONSTRAINT `fk_cart_items_products1`
    FOREIGN KEY (`product_id`)
    REFERENCES `products` (`product_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT `fk_cart_items_carts1`
    FOREIGN KEY (`cart_id`)
    REFERENCES `carts` (`cart_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

--
CREATE TABLE IF NOT EXISTS `favorites` (
`favorite_id` INT NOT NULL AUTO_INCREMENT,
`user_id` INT NOT NULL,
`product_id` INT NOT NULL,
 PRIMARY KEY (`favorite_id`),
    INDEX `fk_favorites_users_idx` (`user_id` ASC) VISIBLE,
    INDEX `fk_favorites_products1_idx` (`product_id` ASC) VISIBLE,
    CONSTRAINT `fk_favorites_users`
    FOREIGN KEY (`user_id`)
    REFERENCES `users` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT `fk_favorites_products1`
    FOREIGN KEY (`product_id`)
    REFERENCES `products` (`product_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


CREATE TABLE IF NOT EXISTS `orders` (
`order_id` INT NOT NULL AUTO_INCREMENT,
`user_id` INT NOT NULL,
`created_at` TIMESTAMP NOT NULL,
`delivery_address` VARCHAR(100) NOT NULL,
`contact_phone` VARCHAR(13) NULL,
`delivery_method` ENUM ('STANDARD', 'EXPRESS') NOT NULL,
`updated_at` TIMESTAMP NOT NULL,
`status` ENUM ('PENDING', 'PAID', 'ON_THE_WAY', 'DELIVERED', 'CANCELED') NULL,
    PRIMARY KEY (`order_id`),
    INDEX `fk_orders_users1_idx` (`user_id` ASC) VISIBLE,
    CONSTRAINT `fk_orders_users1`
    FOREIGN KEY (`user_id`)
    REFERENCES `users` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

--

CREATE TABLE IF NOT EXISTS `order_items` (
`order_item_id` INT NOT NULL AUTO_INCREMENT,
`product_id` INT NOT NULL,
`order_id` INT NOT NULL,
`quantity` INT NOT NULL,  CHECK (quantity > 0),
`price_at_purchase` DECIMAL(7,2) NULL,
    PRIMARY KEY (`order_item_id`),
    INDEX `fk_order_items_products1_idx` (`product_id` ASC) VISIBLE,
    INDEX `fk_order_items_orders1_idx` (`order_id` ASC) VISIBLE,
    CONSTRAINT `fk_order_items_products1`
    FOREIGN KEY (`product_id`)
    REFERENCES `products` (`product_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT `fk_order_items_orders1`
    FOREIGN KEY (`order_id`)
    REFERENCES `orders` (`order_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


