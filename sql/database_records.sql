INSERT INTO customer(first_name, last_name, phone, email, points) VALUES
	('DANIEL', 'MURPHY', 1235550101, 'danielmurphy@email.com', 0),
    ('HAILEY', 'GREEN', 4565550102, 'haileygreen@email.com', 1000);
INSERT INTO employee(first_name, last_name, phone, email, is_manager) VALUES
	('EMMA', 'CARTER', 4155550101, 'emmacarter@email.com', false),
    ('LIAM', 'BROOKS', 2125550102, 'liambrooks@email.com', false),
    ('SOPHIA', 'NGUYEN', 3105550103, 'sophianguyen@email.com', false),
    ('NOAH', 'BENNETT', 6175550104, 'noahbennett@email.com', true),
    ('AVA', 'MITCHELL', 5035550105, 'avamitchell@email.com', false),
    ('ETHAN', 'PARKER', 7025550106, 'ethanparker@email.com', false),
    ('MIA', 'THOMPSON', 4045550107, 'miathompson@email.com', false),
    ('LUCAS', 'RIVERA', 2065550108, 'lucasrivera@email.com', true),
    ('ISABELLA', 'REED', 3055550109, 'isabellareed@email.com', false),
    ('MASON', 'COLLINS', 7135550110, 'masoncollins@email.com', false),
    ('CHARLOTTE', 'FOSTER', 4805550111, 'charlottefoster@gemail.com', false),
    ('BENJAMIN', 'HAYES', 6025550112, 'benjaminhayes@email.com', true),
    ('AMELIA', 'SCOTT', 9195550113, 'ameliascott@email.com', false),
    ('JAMES', 'TURNER', 7205550114, 'jamesturner@email.com', false),
    ('HARPER', 'MORRIS', 8165550115, 'harpermorris@email.com', false),
    ('HENRY', 'PRICE', 9015550116, 'henryprice@email.com', true),
    ('EVELYN', 'WARD', 3145550117, 'evelynward@email.com', false),
    ('ALEXANDER', 'COOK', 8085550118, 'alexandercook@email.com', false),
    ('GRACE', 'SIMMONS', 2485550119, 'gracesimmons@email.com', false),
    ('MARIE', 'WILLIAMS', 9715550120, 'mariewilliams@email.com', true);
INSERT INTO booking(customer_id, start_date, end_date, party_count, book_date, total_charge, points_used, status, booked_by_employee) VALUES
	(1, '2027-09-01', '2027-10-01', 1, '2026-08-01', 150.0, 0, 'booked', NULL),
    (2, '2026-08-01', '2026-09-01', 10, '2026-08-01', 900.0, 0, 'checked_in', NULL);

UPDATE room
SET occupied_by = 2
WHERE room_num = 10;