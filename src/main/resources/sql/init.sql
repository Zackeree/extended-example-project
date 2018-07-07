CREATE DATABASE IF NOT EXISTS example;
USE example;

DROP TABLE IF EXISTS `person`;
CREATE TABLE person (
  id          INT PRIMARY KEY AUTO_INCREMENT,
  user_id     INT NOT NULL,
  first_name  VARCHAR(50),
  last_name   VARCHAR(50),

  INDEX first_name_idx(first_name),
  INDEX last_name_idx(last_name)
);

DROP TABLE IF EXISTS `user`;
CREATE TABLE user (
  id        INT PRIMARY KEY AUTO_INCREMENT,
  username  VARCHAR(100) NOT NULL,
  email     VARCHAR(100) NOT NULL,
  password  VARCHAR(100) NOT NULL DEFAULT '',

  UNIQUE (username),
  UNIQUE (email),
  INDEX password_idx(password)
);

DROP TABLE IF EXISTS `user_role`;
CREATE TABLE user_role (
  id      INT PRIMARY KEY AUTO_INCREMENT,
  user_id INT NOT NULL,
  role    VARCHAR(100) NOT NULL,

  INDEX user_id_idx (`user_id`),
  INDEX role_idx (`role`)
);