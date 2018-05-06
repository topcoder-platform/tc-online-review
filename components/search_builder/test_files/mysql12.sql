create table searchRole (
    roleId       integer NOT NULL,
    name         varchar(64) NOT NULL,
    age          integer NOT NULL,
    date         datetime NOT NULL,
    weight         integer NOT NULL, 
    PRIMARY KEY (roleId)
);

INSERT INTO searchRole (roleId, name, age, weight, date) VALUES (1, 'r1', 3, 10, '1910-10-10');
INSERT INTO searchRole (roleId, name, age, weight, date) VALUES (2, 'r2', 5, 15, '1920-11-01');
INSERT INTO searchRole (roleId, name, age, weight, date) VALUES (3, 'r3', 10, 31, '1920-01-30');
INSERT INTO searchRole (roleId, name, age, weight, date) VALUES (4, 'r4', 20, 60, '1930-10-10');
INSERT INTO searchRole (roleId, name, age, weight, date) VALUES (5, 'r5', 40, 65, '1940-04-07');
INSERT INTO searchRole (roleId, name, age, weight, date) VALUES (6, 'p%6', 76, 55, '1960-09-09');
INSERT INTO searchRole (roleId, name, age, weight, date) VALUES (7, 'p%7', 55, 70, '1980-09-09');
INSERT INTO searchRole (roleId, name, age, weight, date) VALUES (8, 'p_8', 10, 27, '1990-09-09');
INSERT INTO searchRole (roleId, name, age, weight, date) VALUES (9, '\p_9', 3, 12, '2001-09-09');