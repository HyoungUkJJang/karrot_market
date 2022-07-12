DROP TABLE IF EXISTS reply;
DROP TABLE IF EXISTS heart;
DROP TABLE IF EXISTS trade;
DROP TABLE IF EXISTS member_image;
DROP TABLE IF EXISTS product_image;
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS member;
DROP TABLE IF EXISTS category;

CREATE TABLE category
(
  category_id varchar(100) PRIMARY KEY,
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),
  value varchar(100) UNIQUE
);

CREATE TABLE member
(
    member_id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    email varchar(50) NOT NULL UNIQUE,
    password varchar(255) NOT NULL,
    name varchar(30) NOT NULL,
    phone varchar(20) NOT NULL,
    nick_name varchar(255) NOT NULL,
    member_role varchar(255),
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP()
);

CREATE TABLE member_image
(
    member_image_id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    member_id BIGINT,
    file_path varchar(255),
    original_file_name varchar(255),
    server_file_name varchar(255),
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    FOREIGN KEY (member_id) REFERENCES member (member_id)
);

CREATE TABLE product
(
    product_id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    member_id BIGINT,
    category_id varchar(255),
    title varchar(30),
    description TEXT,
    price INT,
    heart_count INT,
    reply_count INT,
    product_status varchar(255),
    category varchar(255),
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    FOREIGN KEY (member_id) REFERENCES member (member_id),
    FOREIGN KEY (category_id) REFERENCES category (category_id)
);

CREATE TABLE product_image
(
    product_image_id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    product_id BIGINT,
    file_path varchar(255),
    original_file_name varchar(255),
    server_file_name varchar(255),
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    FOREIGN KEY (product_id) REFERENCES product (product_id)
);

CREATE TABLE heart
(
  heart_id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  member_id BIGINT,
  product_id BIGINT,
  product_info BIGINT,
  FOREIGN KEY(member_id) REFERENCES member (member_id),
  FOREIGN KEY(product_id) REFERENCES product (product_id)
);

CREATE TABLE reply
(
    reply_id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    member_id BIGINT,
    product_id BIGINT,
    comment TEXT,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    FOREIGN KEY (member_id) REFERENCES member (member_id),
    FOREIGN KEY (product_id) REFERENCES product (product_id)
);

CREATE TABLE trade
(
    trade_id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    member_id BIGINT,
    trade_quantity INT,
    donation_quantity INT,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    FOREIGN KEY (member_id) REFERENCES member (member_id)
);