DELIMITER $$

DROP PROCEDURE IF EXISTS ReserveSpot $$
CREATE PROCEDURE ReserveSpot(
    IN lot_id BIGINT,
    IN driver_id BIGINT,
    IN end_time TIMESTAMP,
    IN price DOUBLE,
    OUT spot_id BIGINT
)
BEGIN
    DECLARE time_now TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
    DECLARE spot_found BIGINT;

    START TRANSACTION;

    SELECT spots.spot_id
    INTO spot_found
    FROM spots
    WHERE lot_id = spots.lot_id AND spots.state = 'Available'
    LIMIT 1
    FOR UPDATE;

    IF spot_found IS NULL THEN
        ROLLBACK;
        SET spot_id = NULL;
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'No available spot found';
    END IF;

    UPDATE spots
    SET spots.state = 'Reserved'
    WHERE spots.spot_id = spot_found;

    INSERT INTO reserved_spots (spot_id, lot_id, start_time, end_time, state, driver_id, price)
    VALUES (spot_found, lot_id, time_now, end_time, 'Reserved', driver_id, price);

    COMMIT;

    SET spot_id = spot_found;

END $$

DROP PROCEDURE IF EXISTS RegisterDriver $$
CREATE PROCEDURE RegisterDriver(
    IN user_name VARCHAR(255),
    IN user_password VARCHAR(255),
    IN plate_number VARCHAR(255),
    IN license_number BIGINT,
    OUT driver_id BIGINT
)
BEGIN
    DECLARE new_user_id BIGINT;

    START TRANSACTION;

    INSERT INTO users (username, user_password)
    VALUES (user_name, user_password);

    SET new_user_id = LAST_INSERT_ID();

    INSERT INTO drivers (Driver_id, plate_number,balance, payment_method, license_number)
    VALUES (new_user_id, plate_number, 0, null, license_number);

    COMMIT;

    SET driver_id = new_user_id;
END $$

DROP PROCEDURE IF EXISTS RegisterManager $$
CREATE PROCEDURE RegisterManager(
    IN user_name VARCHAR(255),
    IN user_password VARCHAR(255),
    OUT manager_id BIGINT
)
BEGIN
    DECLARE new_user_id BIGINT;

    START TRANSACTION;

    INSERT INTO users (username, user_password)
    VALUES (user_name, user_password);

    SET new_user_id = LAST_INSERT_ID();

    INSERT INTO lot_managers (manager_id, revenue)
    VALUES (new_user_id, 0);

    COMMIT;

    SET manager_id = new_user_id;
END $$

DROP PROCEDURE IF EXISTS LoginUser $$
CREATE PROCEDURE LoginUser(
    IN user_name VARCHAR(255),
    IN user_password VARCHAR(255),
    OUT is_authenticated BOOLEAN
)
BEGIN
    DECLARE db_password VARCHAR(255);

    SELECT users.user_password INTO db_password
    FROM users
    WHERE username = user_name;

    IF db_password = user_password THEN
        SET is_authenticated = TRUE;
    ELSE
        SET is_authenticated = FALSE;
    END IF;
END $$

DELIMITER ;