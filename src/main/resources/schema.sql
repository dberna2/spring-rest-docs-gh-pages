DROP TABLE IF EXISTS `users`;
CREATE TABLE users (
    id  int  NOT NULL AUTO_INCREMENT,
    name VARCHAR(128) NOT NULL,
    lastname VARCHAR(128) NOT NULL,
    age INTEGER NOT NULL,
    email nvarchar(100) NOT NULL,
    PRIMARY KEY (id)
);