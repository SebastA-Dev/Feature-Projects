
-- User table creation script
CREATE TABLE `register`.`User` (
  `idUser` INT NOT NULL auto_increment,
  `name` VARCHAR(60) NOT NULL,
  `surname` VARCHAR(60) NOT NULL,
  `email` VARCHAR(80) NOT NULL,
  `username` VARCHAR(60) NOT NULL,
  `password` VARCHAR(250) NOT NULL,
  `funds` DECIMAL(2) NULL DEFAULT 0.00,
  `phoneNumber` VARCHAR(15) NULL,
  `role` INT NOT NULL DEFAULT 1,
  PRIMARY KEY (`idUser`),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC) VISIBLE,
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE);

-- This query create IPAddress datatable for email validation
CREATE TABLE `register`.`UserIPAddress` (
  `idUserIPAddress` INT NOT NULL auto_increment,
  `userId` INT NOT NULL,
  `ipAddress` VARCHAR(60) NOT NULL,
  `DateTime` DATETIME NOT NULL,
  PRIMARY KEY (`idUserIPAddress`)
  );
