-- liquibase formatted sql
-- changeset maria:001
-- -----------------------------------------------------
-- Schema OnlineApp
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `OnlineApp` DEFAULT CHARACTER SET utf8;
USE `OnlineApp`;

CREATE TABLE IF NOT EXISTS `OnlineApp`.`categories` (
`category_id` INT NOT NULL,
`name` VARCHAR(45) NOT NULL,
`image_url` VARCHAR(255) NOT NULL,
 PRIMARY KEY (`category_id`));

CREATE TABLE IF NOT EXISTS `OnlineApp`.`products` (
`product_id` INT NOT NULL AUTO_INCREMENT,
`category_id` INT NOT NULL,
`name` VARCHAR(100) NOT NULL,
`description` TEXT NOT NULL,
`price` DECIMAL NOT NULL,
`image_url` VARCHAR(255) NOT NULL,
`discount_price` DECIMAL NOT NULL,
`created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
`updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`product_id`),
    INDEX `fk_products_categories1_idx` (`category_id` ASC) VISIBLE,
    CONSTRAINT `fk_products_categories1`
    FOREIGN KEY (`category_id`)
    REFERENCES `OnlineApp`.`categories` (`category_id`)
);

CREATE TABLE IF NOT EXISTS `OnlineApp`.`users` (
`user_id` INT NOT NULL AUTO_INCREMENT,
`name` VARCHAR(45) NOT NULL,
`email` VARCHAR(45) NOT NULL,
`phone_number` VARCHAR(13) NOT NULL,
`password_hash` VARCHAR(45) NOT NULL,
`role` ENUM ('CUSTOMER', 'ADMINISTRATOR') NOT NULL,
    PRIMARY KEY (`user_id`),
    UNIQUE INDEX `Email_UNIQUE` (`email` ASC) VISIBLE);
--

CREATE TABLE IF NOT EXISTS `OnlineApp`.`cart` (
`cart_id` INT NOT NULL AUTO_INCREMENT,
`user_id` INT NOT NULL,
 PRIMARY KEY (`cart_id`),
    INDEX `fk_cart_users1_idx` (`user_id` ASC) VISIBLE,
    CONSTRAINT `fk_cart_users1`
    FOREIGN KEY (`user_id`)
    REFERENCES `OnlineApp`.`users` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
--

CREATE TABLE IF NOT EXISTS `OnlineApp`.`cart_items` (
`cart_item_id` INT NOT NULL AUTO_INCREMENT,
`product_id` INT NOT NULL,
`cart_id` INT NOT NULL,
`quantity` INT NOT NULL,
PRIMARY KEY (`cart_item_id`),
    INDEX `fk_cart_items_products1_idx` (`product_id` ASC) VISIBLE,
    INDEX `fk_CartItems_cart1_idx` (`cart_id` ASC) VISIBLE,
    CONSTRAINT `fk_cart_items_products1`
    FOREIGN KEY (`product_id`)
    REFERENCES `OnlineApp`.`products` (`product_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT `fk_cart_items_cart1`
    FOREIGN KEY (`cart_id`)
    REFERENCES `OnlineApp`.`cart` (`cart_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

--
CREATE TABLE IF NOT EXISTS `OnlineApp`.`favorites` (
`favorite_id` INT NOT NULL AUTO_INCREMENT,
`user_id` INT NOT NULL,
`product_id` INT NOT NULL,
 PRIMARY KEY (`favorite_id`),
    INDEX `fk_favorites_users_idx` (`user_id` ASC) VISIBLE,
    INDEX `fk_favorites_products1_idx` (`product_id` ASC) VISIBLE,
    CONSTRAINT `fk_favorites_users`
    FOREIGN KEY (`user_id`)
    REFERENCES `OnlineApp`.`users` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT `fk_favorites_products1`
    FOREIGN KEY (`product_id`)
    REFERENCES `OnlineApp`.`products` (`product_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


CREATE TABLE IF NOT EXISTS `OnlineApp`.`orders` (
`order_id` INT NOT NULL AUTO_INCREMENT,
`user_id` INT NOT NULL,
`created_at` TIMESTAMP NOT NULL,
`delivery_address` VARCHAR(100) NOT NULL,
`contact_phone` VARCHAR(13) NOT NULL,
`delivery_method` ENUM ('STANDARD', 'EXPRESS') NOT NULL,
`updated_at` TIMESTAMP NOT NULL,
`status` ENUM ('PENDING', 'PAID', 'ON_THE_WAY', 'DELIVERED', 'CANCELED') NOT NULL,
    PRIMARY KEY (`order_id`),
    INDEX `fk_orders_users1_idx` (`user_id` ASC) VISIBLE,
    CONSTRAINT `fk_orders_users1`
    FOREIGN KEY (`user_id`)
    REFERENCES `OnlineApp`.`users` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

--

CREATE TABLE IF NOT EXISTS `OnlineApp`.`order_items` (
`order_item_id` INT NOT NULL AUTO_INCREMENT,
`product_id` INT NOT NULL,
`order_id` INT NOT NULL,
`quantity` INT NOT NULL,
`price_at_purchase` DECIMAL NOT NULL,
    PRIMARY KEY (`order_item_id`),
    INDEX `fk_order_items_products1_idx` (`product_id` ASC) VISIBLE,
    INDEX `fk_order_items_orders1_idx` (`order_id` ASC) VISIBLE,
    CONSTRAINT `fk_order_items_products1`
    FOREIGN KEY (`product_id`)
    REFERENCES `OnlineApp`.`products` (`product_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT `fk_order_items_orders1`
    FOREIGN KEY (`order_id`)
    REFERENCES `OnlineApp`.`orders` (`order_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


