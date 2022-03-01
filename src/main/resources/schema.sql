DROP TABLE IF EXISTS `users`;
CREATE TABLE users (
    id  int  NOT NULL AUTO_INCREMENT,
    name VARCHAR(128) NOT NULL,
    lastname VARCHAR(128) NOT NULL,
    age INTEGER NOT NULL,
    email nvarchar(100) NOT NULL,
    PRIMARY KEY (id)
);

DROP TABLE IF EXISTS `accounts`;
CREATE TABLE accounts (
   id  int  NOT NULL AUTO_INCREMENT,
   type VARCHAR(128) NOT NULL,
   number VARCHAR(128) NOT NULL,
   balance decimal(6, 2),
   user_id int NOT NULL,
   PRIMARY KEY (id)
);