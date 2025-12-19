USE sys;

# ---------------------------------------------------------------------- #
# Target DBMS:           MySQL                                           #
# Project name:          BunnyBoutique                                   #
# ---------------------------------------------------------------------- #
DROP DATABASE IF EXISTS bunnyboutique;

CREATE DATABASE IF NOT EXISTS bunnyboutique;

USE bunnyboutique;

# ---------------------------------------------------------------------- #
# Tables                                                                 #
# ---------------------------------------------------------------------- #

CREATE TABLE users (
    user_id INT NOT NULL AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    hashed_password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    PRIMARY KEY (user_id)
);

CREATE TABLE profiles (
    user_id INT NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    email VARCHAR(200) NOT NULL,
    address VARCHAR(200) NOT NULL,
    city VARCHAR(50) NOT NULL,
    state VARCHAR(50) NOT NULL,
    zip VARCHAR(20) NOT NULL,
    PRIMARY KEY (user_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE categories (
    category_id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    PRIMARY KEY (category_id)
);

CREATE TABLE products (
    product_id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    category_id INT NOT NULL,
    description TEXT,
    subcategory VARCHAR(20),
    image_url VARCHAR(200),
    stock INT NOT NULL DEFAULT 0,
    featured BOOL NOT NULL DEFAULT 0,
    PRIMARY KEY (product_id),
    FOREIGN KEY (category_id) REFERENCES categories(category_id)
);

CREATE TABLE orders (
    order_id INT NOT NULL AUTO_INCREMENT,
    user_id INT NOT NULL,
    date DATETIME NOT NULL,
    address VARCHAR(100) NOT NULL,
    city VARCHAR(50) NOT NULL,
    state VARCHAR(50) NOT NULL,
    zip VARCHAR(20) NOT NULL,
    shipping_amount DECIMAL(10, 2) NOT NULL DEFAULT 0,
    PRIMARY KEY (order_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE order_line_items (
    order_line_item_id INT NOT NULL AUTO_INCREMENT,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    sales_price DECIMAL(10, 2) NOT NULL,
    quantity INT NOT NULL,
    discount DECIMAL(10, 2) NOT NULL DEFAULT 0,
    PRIMARY KEY (order_line_item_id),
    FOREIGN KEY (order_id) REFERENCES orders(order_id),
    FOREIGN KEY (product_id) REFERENCES products(product_id)
);

CREATE TABLE shopping_cart (
	user_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    PRIMARY KEY (user_id, product_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (product_id) REFERENCES products(product_id)
);

# ---------------------------------------------------------------------- #
# Insert                                                                 #
# ---------------------------------------------------------------------- #

/*  INSERT Users  */
INSERT INTO users (username, hashed_password, role) 
VALUES  ('user','$2a$10$NkufUPF3V8dEPSZeo1fzHe9ScBu.LOay9S3N32M84yuUM2OJYEJ/.','ROLE_USER'),
        ('admin','$2a$10$lfQi9jSfhZZhfS6/Kyzv3u3418IgnWXWDQDk7IbcwlCFPgxg9Iud2','ROLE_ADMIN'),
        ('george','$2a$10$lfQi9jSfhZZhfS6/Kyzv3u3418IgnWXWDQDk7IbcwlCFPgxg9Iud2','ROLE_USER');

/* INSERT Profiles */
INSERT INTO profiles (user_id, first_name, last_name, phone, email, address, city, state, zip)
VALUES  (1, 'Joe', 'Joesephus', '800-555-1234', 'joejoesephus@email.com', '789 Oak Avenue', 'Dallas', 'TX', '75051'),
        (2, 'Adam', 'Admamson', '800-555-1212', 'aaadamson@email.com', '456 Elm Street','Dallas','TX','75052'),
        (3, 'George', 'Jetson', '800-555-9876', 'george.jetson@email.com', '123 Birch Parkway','Dallas','TX','75051');

/* INSERT Categories */
INSERT INTO categories (name, description) 
VALUES  ('Tops', 'T-shirts, tank tops, blouses, and more.'),
		('Outerwear', 'Jackets, hoodies, coats, and more.'),
        ('Pants', 'Jeans, pants, cargos, shorts, and more.'),
        ('Skirts', 'Mini, midi, and maxi skirts.'),
        ('Dresses', 'Mini, midi, and maxi dresses.'),
        ('Shoes', 'Sneakers, heels, boots, dress shoes, and all footwear.');


/* INSERT Products (10 each) */

-- Tops (Category 1)
INSERT INTO products (name, price, category_id, description, image_url, stock, featured, subcategory) 
VALUES ('Black Lace Top', 32.99, 1, 'Sophisticated, intricate black lace top with bow and no sleeves.', 'black_lace_top.jpg', 25, 0, 'Black'),
       ('Blue Babydoll Top', 29.99, 1, 'Flowy, feminine babydoll silhouette with lace.', 'blue_babydoll_top.jpg', 40, 0, 'Blue'),
       ('Cream Bunny T-Shirt', 19.99, 1, 'Cream tee featuring a whimsical bunny graphic.', 'cream_bunny_tshirt.jpg', 80, 1, 'Cream'),
       ('Green Lace Top', 34.99, 1, 'Mint green lacy top with bow detail.', 'green_lace_top.jpg', 95, 0, 'Green'),
       ('Pink Bunny Top', 24.99, 1, 'Frilly pink top with bunny and bow detail.', 'pink_bunny_top.jpg', 40, 1, 'Pink'),
	   ('Pink Off-the-Shoulder', 44.99, 1, 'Romantic, off-the-shoulder, pink top with bow details', 'pink_off_the_shoulder.jpg', 75, 0, 'Pink'),
       ('Frilly Purple Top', 29.99, 1, 'Frilly purple top with bow detail.', 'purple_top.jpg', 60, 0, 'Purple'),
       ('White Blouse', 24.99, 1, 'White, lacy blouse with open neckline.', 'white_blouse.jpg', 45, 0, 'White'),
       ('White Strapless Top', 39.99, 1, 'White, lacy, strapless top with bow and slit detail.', 'white_strapless_top.jpg', 55, 0, 'White');
        
-- Outerwear (Category 2)
INSERT INTO products (name, price, category_id, description, image_url, stock, featured, subcategory) 
VALUES ('Black Maxi Coat', 149.99, 2, 'Black maxi coat with fur collar and capelet overlay.', 'black_maxi_coat.jpg', 45, 0, 'Black'),
	   ('Brown Wool Coat', 89.99, 2, 'Vintage-style brown coat with lace and bow detail.', 'brown_wool_coat.jpg', 35, 0, 'Brown'),
       ('Green Bunny Cardigan', 59.99, 2, 'Sage green cardigan with bunny and ruffle detail.', 'green_bunny_cardigan.jpg', 80, 1, 'Green'),
       ('Green Floral Jacket', 39.99, 2, 'Sage green zip-up hoodie with floral lace embroidery.', 'green_jacket.jpg', 90, 0, 'Green'),
       ('Pink Bunny Cardigan', 54.99, 2, 'Zip-up pink cardigan with white bunnies and snowflake pattern.', 'pink_bunny_cardigan.jpg', 60, 1, 'Pink'),
       ('Pink Lace Coat', 99.99, 2, 'Pink coat with white lace, fur, and bows.', 'pink_lace_coat.jpg', 40, 0, 'Pink'),
       ('Pink Wool Coat', 199.99, 2, 'Pastel pink coat with fur cuffs and collar.', 'pink_wool_coat.jpg', 25, 0, 'Pink'),
       ('Red Flared Coat', 119.99, 2, 'Deep burgundy flared coat with button and bow detail.', 'red_coat.jpg', 30, 0, 'Red'),
       ('White Bunny Poncho', 99.99, 2, 'White fleece poncho with bunny ears and heart-shaped pockets.', 'white_bunny_poncho.jpg', 75, 1, 'White'),
       ('White Lace Coat', 89.99, 2, 'White double-breasted coat with layered lace skirt.', 'white_lace_coat.jpg', 55, 0, 'White');

-- Pants (Category 3)
INSERT INTO products (name, price, category_id, description, image_url, stock, featured, subcategory) 
VALUES ('Black Bloomers', 29.99, 3, 'Ruffled black bloomers with lace and pink bows.', 'black_bloomers.jpg', 35, 0, 'Black'),
	   ('Black Flared Jeans', 69.99, 3, 'Black flared jeans with skirt and lace bow detail.', 'black_flared_jeans.jpg', 30, 0, 'Black'),
       ('Bow & Star Jeans', 54.99, 3, 'Star-print denim flares with pink bows and side slits.', 'bow_star_jeans.jpg', 75, 0, 'Blue'),
       ('Bunny Flared Jeans', 49.99, 3, 'Slim flare jeans with bunny pocket patches and ribbons.', 'bunny_flared_jeans.jpg', 60, 0, 'Blue'),
       ('Bunny Wide-leg Jeans', 49.99, 3, 'Wide-leg jeans with cute plush bunny patches.', 'bunny_wide_leg_jeans.jpg', 45, 0, 'Blue'),
       ('Gray Sweatpants', 34.99, 3, 'Baggy gray sweatpants with dainty white ribbon bows.', 'gray_sweatpants.jpg', 90, 0, 'Gray'),
       ('Green Flared Jeans', 59.99, 3, 'Green flared denim with brown side lace-up detailing.', 'green_jeans.jpg', 70, 0, 'Green'),
       ('Bow Jean Shorts', 32.99, 3, 'Light-wash denim shorts with pink ribbon & embroidery.', 'pink_bow_jean_shorts.jpg', 55, 0, 'Blue'),
       ('Bow Flared Jeans', 54.99, 3, 'Dark-wash flared jeans with pink ribbon on thigh.', 'pink_bow_jeans.jpg', 20, 0, 'Blue'),
       ('Pink Cargos', 44.99, 3, 'Wide-leg pink cargos with large bows.', 'pink_cargos.jpg', 85, 0, 'Pink');

-- Skirts (Category 4)
INSERT INTO products (name, price, category_id, description, image_url, stock, featured, subcategory) 
VALUES ('Black Mini Skirt', 34.99, 4, 'Black lace mini skirt with ruffled tiers and satin bow.', 'black_mini_skirt.jpg', 80, 1, 'Black'),
	   ('Blue Pleated Mini Skirt', 35.99, 4, 'Blue pinstripe pleated mini skirt with pink ribbons.', 'blue_pleated_skirt.jpg', 40, 0, 'Blue'),
       ('Blue & White Mini Skirt', 35.99, 4, 'Blue & white mini skirt with ruffled tiers and bow detail.', 'blue_white_mini_skirt.jpg', 30, 0, 'Blue'),
	   ('Brown Mini Skirt', 34.99, 4, 'Brown plaid mini skirt with layered ruffles and lace.', 'brown_mini_skirt.jpg', 45, 1, 'Brown'),
	   ('Cream Maxi Skirt', 54.99, 4, 'Cream lace maxi skirt with ruffled tiers and flowy silhouette.', 'cream_maxi_skirt.jpg', 55, 0, 'Cream'),
       ('Denim Mini Skirt', 39.99, 4, 'Ruffled denim mini skirt with pink ribbon side detail.', 'denim_mini_skirt.png', 50, 1, 'Blue'),
       ('Green Mini Skirt', 31.99, 4, 'Green wrap-style mini skirt with ruffled tiers and bow detail.', 'green_mini_skirt.jpg', 35, 0, 'Green'),
       ('Pink & Brown Mini Skirt', 32.99, 4, 'Pink plaid mini skirt with brown ruffled underlayer.', 'pink_brown_mini_skirt.jpg', 60, 0, 'Pink'),
       ('Pink Pleated Mini Skirt', 29.99, 4, 'Delicate pink pleated mini skirt with white lace waistband.', 'pink_mini_skirt.jpg', 25, 0, 'Pink'),
       ('White Mini Skirt', 29.99, 4, 'White layered mini skirt with ruffled tiers and bow detail.', 'white_mini_skirt.jpg', 75, 0, 'White');

-- Dresses (Category 5)
INSERT INTO products (name, price, category_id, description, image_url, stock, featured, subcategory) 
VALUES ('Black Mini Dress', 69.99, 5, 'Black mini dress with ribbon lacing and ruffled sleeves.', 'black_mini_dress.jpg', 40, 0, 'Black'),
	   ('Blue Mini Dress', 49.99, 5, 'Pastel blue dress with white lace and bow detail.', 'blue_mini_dress.jpg', 55, 0, 'Blue'),
       ('Brown Mini Dress', 59.99, 5, 'Brown patterned mini dress with ruffle layers and corset.', 'brown_mini_dress.jpg', 75, 0, 'Brown'),
       ('Green Mini Dress', 74.99, 5, 'Green hanfu-inspired mini dress with bamboo pattern.', 'green_mini_dress.jpg', 30, 0, 'Green'),
       ('Green Modern Hanfu Dress', 79.99, 5, 'Green modern hanfu dress with butterfly embroidery and ribbons.', 'green_modern_hanfu_dress.jpg', 65, 0, 'Green'),
       ('Pastel Pink Mini Dress', 39.99, 5, 'Pink dress with ruffled tiers and bow detail.', 'pastel_pink_mini_dress.jpg', 90, 0, 'Pink'),
       ('Pink Midi Dress', 74.99, 5, 'Asymmetrical pink midi dress with ruffled tiers.', 'pink_midi_dress.jpg', 30, 0, 'Pink'),
       ('Red Maxi Dress', 79.99, 5, 'Deep red maxi dress with ruffled tiers and lace-up detail.', 'red_maxi_dress.jpg', 45, 0, 'Red'),
       ('White Maxi Dress', 119.99, 5, 'White lace maxi dress with delicate floral patterns and ruffles.', 'white_maxi_dress.jpg', 30, 0, 'White'),
       ('White Mini Dress', 49.99, 5, 'Strapless white tiered mini dress with bow detail.', 'white_mini_dress.jpg', 90, 0, 'White');

-- Shoes (Category 6)
INSERT INTO products (name, price, category_id, description, image_url, stock, featured, subcategory) 
VALUES ('Black Princess Heels', 99.99, 6, 'Gothic princess heels with lace trim and bows.', 'black_princess_heels.jpg', 50, 0, 'Black'),
	   ('Brown Fuzzy Boots', 119.99, 6, 'Cozy suede ankle boots with faux fur trim.', 'brown_fuzzy_boots.jpg', 55, 0, 'Brown'),
       ('Green & Pink Sneakers', 64.99, 6, 'Chunky pastel sneakers featuring decorative floral charm.', 'green_pink_sneakers.jpg', 35, 0, 'Green'),
       ('Pink Ballerina Heels', 184.99, 6, 'Satin ballet-style pumps with ribbon ankle ties.', 'pink_ballerina_heels.jpg', 30, 0, 'Pink'),
       ('Pink Lily Heels', 199.99, 6, 'Strappy metallic sandals with realistic lily flower accents.', 'pink_lily_heels.jpg', 30, 0, 'Pink'),
       ('Pink Sneakers', 59.99, 6, 'Soft pink low-tops with delicate scalloped edges.', 'pink_sneakers.jpg', 90, 0, 'Pink'),
       ('Pink Vine Heels', 69.99, 6, 'Garden-inspired sandals with velvet roses and leaf straps.', 'pink_vine_heels.jpg', 80, 0, 'Pink'),
       ('Red Princess Heels', 99.99, 6, 'Elegant pointed heels with ruffled mesh and jewel accents.', 'red_princess_heels.jpg', 75, 0, 'Red'),
       ('Silver Pink Heels', 129.99, 6, 'Metallic heels with butterfly-inspired wing detailing.', 'silver_pink_heels.jpg', 60, 0, 'Silver'),
       ('White Bunny Boots', 149.99, 6, 'Playful platform boots with bunny ears and pom-poms.', 'white_bunny_boots.jpg', 60, 1, 'White');

-- add shopping cart items
INSERT INTO shopping_cart (user_id, product_id, quantity)
VALUES  (3, 8, 1),
        (3, 10, 1);