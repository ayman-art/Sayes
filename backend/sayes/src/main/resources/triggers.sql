DELIMITER $$

CREATE TRIGGER after_spot_update
AFTER UPDATE ON spots
FOR EACH ROW
BEGIN
    -- Only execute if the spot's state is updated to "Available"
    IF NEW.state = 'Available' THEN
        -- Delete the reservation
        DELETE FROM reserved_spots
        WHERE reserved_spots.spot_id = NEW.spot_id
          AND reserved_spots.lot_id = NEW.lot_id;
    END IF;
END$$

DELIMITER ;