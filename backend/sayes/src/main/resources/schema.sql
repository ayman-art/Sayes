-- -----------------------------------------------------
-- MySQL Workbench Forward Engineering
-- -----------------------------------------------------
SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema sayes
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `sayes` DEFAULT CHARACTER SET utf8 ;
USE `sayes` ;

-- -----------------------------------------------------
-- Table `sayes`.`Users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sayes`.`Users` (
  `user_id` BIGINT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(255) UNIQUE NOT NULL,
  `user_password` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`user_id`))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `sayes`.`lot_managers`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sayes`.`lot_managers` (
  `manager_id` BIGINT NOT NULL,
  `revenue` BIGINT NOT NULL,
  PRIMARY KEY (`manager_id`),
  CONSTRAINT `fk_lot_managers_Users`
    FOREIGN KEY (`manager_id`)
    REFERENCES `sayes`.`Users` (`user_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `sayes`.`Admins`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sayes`.`Admins` (
  `Admin_id` BIGINT NOT NULL,
  PRIMARY KEY (`Admin_id`),
  CONSTRAINT `fk_Admins_Users1`
    FOREIGN KEY (`Admin_id`)
    REFERENCES `sayes`.`Users` (`user_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `sayes`.`Drivers`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sayes`.`Drivers` (
  `Driver_id` BIGINT NOT NULL,
  `plate_number` VARCHAR(255) NOT NULL,
  `balance` BIGINT NOT NULL,
  `payment_method` VARCHAR(255) NULL,
  `license_number` BIGINT NOT NULL,
  PRIMARY KEY (`Driver_id`),
  UNIQUE INDEX `plate_number_UNIQUE` (`plate_number` ASC) VISIBLE,
  CONSTRAINT `fk_Drivers_Users1`
    FOREIGN KEY (`Driver_id`)
    REFERENCES `sayes`.`Users` (`user_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `sayes`.`Lots`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sayes`.`Lots` (
  `lot_id` BIGINT NOT NULL AUTO_INCREMENT,
  `manager` BIGINT NOT NULL,
  `longitude` DOUBLE NOT NULL,
  `latitude` DOUBLE NOT NULL,
  `revenue` BIGINT NOT NULL,
  `price` DOUBLE(15,2) NOT NULL,
  `lot_type` VARCHAR(255) NOT NULL,
  `penalty` DOUBLE(15,2) NOT NULL,
  `fee` DOUBLE(15,2) NOT NULL,
  `time` TIME NOT NULL,
  `details` VARCHAR(255),
  PRIMARY KEY (`lot_id`),
  INDEX `fk_Lots_lot_managers1_idx` (`manager` ASC) VISIBLE,
  CONSTRAINT `fk_Lots_lot_managers1`
    FOREIGN KEY (`manager`)
    REFERENCES `sayes`.`lot_managers` (`manager_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `sayes`.`spots`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sayes`.`spots` (
  `spot_id` BIGINT NOT NULL AUTO_INCREMENT,
  `lot_id` BIGINT NOT NULL,
  `state` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`spot_id`, `lot_id`),
  INDEX `fk_spots_Lots1_idx` (`lot_id` ASC) VISIBLE,
  CONSTRAINT `fk_spots_Lots1`
    FOREIGN KEY (`lot_id`)
    REFERENCES `sayes`.`Lots` (`lot_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `sayes`.`reserved_spots`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sayes`.`reserved_spots` (
  `spot_id` BIGINT NOT NULL,
  `lot_id` BIGINT NOT NULL,
  `start_time` DATETIME NOT NULL,
  `end_time` DATETIME NOT NULL,
  `state` VARCHAR(255) NULL,
  `driver_id` BIGINT NOT NULL,
  `price` DOUBLE(15,2) NOT NULL,
  PRIMARY KEY (`spot_id`, `lot_id`),
  INDEX `fk_reserved_spots_Lots1_idx` (`lot_id` ASC) VISIBLE,
  INDEX `fk_reserved_spots_Drivers1_idx` (`driver_id` ASC) VISIBLE,
  CONSTRAINT `fk_reserved_spots_spots1`
    FOREIGN KEY (`spot_id`)
    REFERENCES `sayes`.`spots` (`spot_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_reserved_spots_Lots1`
    FOREIGN KEY (`lot_id`)
    REFERENCES `sayes`.`Lots` (`lot_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_reserved_spots_Drivers1`
    FOREIGN KEY (`driver_id`)
    REFERENCES `sayes`.`Drivers` (`Driver_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `sayes`.`logs`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sayes`.`logs` (
  `driver_id` BIGINT NOT NULL,
  `reservation_time` DATETIME NOT NULL,
  `departure_time` DATETIME NOT NULL,
  `spot_id` BIGINT NOT NULL,
  `lot_id` BIGINT NOT NULL,
  INDEX `fk_logs_Lots1_idx` (`lot_id` ASC) VISIBLE,
  INDEX `fk_logs_spots1_idx` (`spot_id` ASC) VISIBLE,
  INDEX `fk_logs_Drivers1_idx` (`driver_id` ASC) VISIBLE,
  CONSTRAINT `fk_logs_Lots1`
    FOREIGN KEY (`lot_id`)
    REFERENCES `sayes`.`Lots` (`lot_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_logs_spots1`
    FOREIGN KEY (`spot_id`)
    REFERENCES `sayes`.`spots` (`spot_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_logs_Drivers1`
    FOREIGN KEY (`driver_id`)
    REFERENCES `sayes`.`Drivers` (`Driver_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;