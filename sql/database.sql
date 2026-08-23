DROP DATABASE IF EXISTS hotel_database;
CREATE DATABASE IF NOT EXISTS hotel_database;

USE hotel_database;

CREATE TABLE customer
(
	customer_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    phone CHAR(10) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    points FLOAT NOT NULL DEFAULT 0,

    CONSTRAINT customer_requirements
        CHECK (first_name REGEXP '^[A-Z]+$' AND CHAR_LENGTH(first_name) >= 2),
        CHECK (last_name REGEXP '^[A-Z]+$' AND CHAR_LENGTH(last_name) >= 2),
        CHECK (phone REGEXP '[0-9]{10}' AND NOT phone REGEXP '^0'),
        CHECK (email REGEXP '\.com$' AND email LIKE '%@%'),
        CHECK (points >= 0.0)
);

CREATE TABLE employee
(
	employee_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    phone CHAR(10) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    is_manager BOOL NOT NULL,
    
    CONSTRAINT employee_requirements
        CHECK (first_name REGEXP '^[A-Z]+$' AND CHAR_LENGTH(first_name) >= 2),
        CHECK (last_name REGEXP '^[A-Z]+$' AND CHAR_LENGTH(last_name) >= 2),
        CHECK (phone REGEXP '[0-9]{10}' AND NOT phone REGEXP '^0'),
        CHECK (email REGEXP '\.com$' AND email LIKE '%@%')
);

CREATE TABLE room
(
	room_num SMALLINT PRIMARY KEY,
    occupied_by INT,
    room_type VARCHAR(6) NOT NULL,
    
    CONSTRAINT room_requirements
		FOREIGN KEY (occupied_by) REFERENCES customer (customer_id),
		CHECK (room_num >= 1),
        CHECK (room_type IN ('single', 'double', 'twin', 'quad'))
);

CREATE TABLE booking
(
	booking_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL,
	start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    party_count SMALLINT NOT NULL,
    book_date DATE NOT NULL,
    base_charge FLOAT NOT NULL,
    total_charge FLOAT NOT NULL,
    points_used FLOAT NOT NULL,
    status VARCHAR(11) NOT NULL,
    booked_by_employee INT,

	CONSTRAINT booking_requirements
		FOREIGN KEY (customer_id) REFERENCES customer (customer_id),
        FOREIGN KEY (booked_by_employee) REFERENCES employee (employee_id),
        CHECK (start_date < end_date),
        CHECK (book_date <= start_date),
        CHECK (party_count BETWEEN 1 AND 10),
        CHECK (total_charge >= 0.0),
        CHECK (points_used >= 0.0),
        CHECK (status IN ('cancelled', 'booked', 'checked_in', 'checked_out', 'overstayed'))
);

CREATE TRIGGER set_base_charge
BEFORE INSERT ON booking
FOR EACH ROW
SET NEW.base_charge = NEW.total_charge;


DELIMITER !

CREATE TRIGGER auto_update_after_checkout
AFTER UPDATE ON booking
FOR EACH ROW
IF (OLD.status = 'checked_in' OR OLD.status = 'overstayed') AND NEW.status = 'checked_out' THEN
	UPDATE customer
    SET points = points + (TIMESTAMPDIFF(DAY, NEW.start_date, NEW.end_date) * 40)
    WHERE customer.customer_id = NEW.customer_id;
    UPDATE room
    SET occupied_by = null
    WHERE occupied_by = NEW.customer_id;
END IF!

DELIMITER ;


# There's a better way to do this
INSERT INTO room(room_num, occupied_by, room_type) VALUES
	(1, null, 'single'),
    (2, null, 'single'),
    (3, null, 'single'),
    (4, null, 'single'),
    (5, null, 'single'),
    (6, null, 'double'),
    (7, null, 'double'),
    (8, null, 'double'),
    (9, null, 'double'),
    (10, null, 'double'),
    (11, null, 'twin'),
    (12, null, 'twin'),
    (13, null, 'twin'),
    (14, null, 'twin'),
    (15, null, 'twin'),
    (16, null, 'quad'),
    (17, null, 'quad'),
    (18, null, 'quad'),
    (19, null, 'quad'),
    (20, null, 'quad'),
    (21, null, 'single'),
    (22, null, 'single'),
    (23, null, 'single'),
    (24, null, 'single'),
    (25, null, 'single'),
    (26, null, 'double'),
    (27, null, 'double'),
    (28, null, 'double'),
    (29, null, 'double'),
    (30, null, 'double'),
    (31, null, 'twin'),
    (32, null, 'twin'),
    (33, null, 'twin'),
    (34, null, 'twin'),
    (35, null, 'twin'),
    (36, null, 'quad'),
    (37, null, 'quad'),
    (38, null, 'quad'),
    (39, null, 'quad'),
    (40, null, 'quad'),
	(41, null, 'single'),
    (42, null, 'single'),
    (43, null, 'single'),
    (44, null, 'single'),
    (45, null, 'single'),
    (46, null, 'double'),
    (47, null, 'double'),
    (48, null, 'double'),
    (49, null, 'double'),
    (50, null, 'double'),
    (51, null, 'twin'),
    (52, null, 'twin'),
    (53, null, 'twin'),
    (54, null, 'twin'),
    (55, null, 'twin'),
    (56, null, 'quad'),
    (57, null, 'quad'),
    (58, null, 'quad'),
    (59, null, 'quad'),
    (60, null, 'quad');