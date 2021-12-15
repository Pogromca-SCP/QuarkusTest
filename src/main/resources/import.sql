INSERT INTO User (id, username, password, description) VALUES (UUID_TO_BIN('123e4567-e89b-12d3-a456-426614174000'), 'Andrzej', 'fnwafwa', 'KUUURłaaa');
INSERT INTO User (id, username, password, description) VALUES (UUID_TO_BIN('123e4567-e89b-12d3-a456-426614174026'), 'Stefan', 'knfwaf', NULL);


INSERT INTO Finding (id, author, title, url, description, points, postdate) VALUES (UUID_TO_BIN('123e4567-e89b-12d3-a456-426614174000'), UUID_TO_BIN('123e4567-e89b-12d3-a456-426614174000'), 'XD', 'gsmlkegnsg', 'ojojojoj', 34, '2020-12-12T12:00');
INSERT INTO Finding (id, author, title, url, description, points, postdate) VALUES (UUID_TO_BIN('123e4567-e89b-12d3-a456-426614174026'), UUID_TO_BIN('123e4567-e89b-12d3-a456-426614174000'), 'BRUH', 'onpsenes', 'HWDP', 6, '2021-12-12T16:00');
INSERT INTO Finding (id, author, title, url, description, points, postdate) VALUES (UUID_TO_BIN('123e4567-e89b-12d3-a456-426614174043'), UUID_TO_BIN('123e4567-e89b-12d3-a456-426614174026'), 'LOL', 'sgemgnsg', 'preseege', -2, '2021-12-12T12:00');

INSERT INTO Comment (id, finding, author, reply, content) VALUES (UUID_TO_BIN('123e4567-e89b-12d3-a456-426614174000'), UUID_TO_BIN('123e4567-e89b-12d3-a456-426614174000'), UUID_TO_BIN('123e4567-e89b-12d3-a456-426614174000'), NULL, 'XD');
INSERT INTO Comment (id, finding, author, reply, content) VALUES (UUID_TO_BIN('123e4567-e89b-12d3-a456-426614174026'), UUID_TO_BIN('123e4567-e89b-12d3-a456-426614174026'), UUID_TO_BIN('123e4567-e89b-12d3-a456-426614174026'), NULL, 'BRUH');
INSERT INTO Comment (id, finding, author, reply, content) VALUES (UUID_TO_BIN('123e4567-e89b-12d3-a456-426614174043'), UUID_TO_BIN('123e4567-e89b-12d3-a456-426614174043'), UUID_TO_BIN('123e4567-e89b-12d3-a456-426614174026'), UUID_TO_BIN('123e4567-e89b-12d3-a456-426614174000'), 'LMAO');