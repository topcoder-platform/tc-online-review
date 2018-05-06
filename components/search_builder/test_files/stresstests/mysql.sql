CREATE TABLE people (
       peopleID       integer NOT NULL,
       peopleName     varchar(64) NOT NULL,
       age            integer NOT NULL,
       weight         integer NOT NULL, 
       PRIMARY KEY (peopleID)
);
INSERT INTO people (peopleID, peopleName, age, weight) VALUES (1, 'p1', 3, 10);
INSERT INTO people (peopleID, peopleName, age, weight) VALUES (2, 'p2', 5, 15);
INSERT INTO people (peopleID, peopleName, age, weight) VALUES (3, 'p3', 10, 31);
INSERT INTO people (peopleID, peopleName, age, weight) VALUES (4, 'p4', 20, 60);
INSERT INTO people (peopleID, peopleName, age, weight) VALUES (5, 'p5', 40, 65);
INSERT INTO people (peopleID, peopleName, age, weight) VALUES (6, 'p6', 76, 55);
INSERT INTO people (peopleID, peopleName, age, weight) VALUES (7, 'p7', 55, 70);
INSERT INTO people (peopleID, peopleName, age, weight) VALUES (8, 'p8', 10, 27);
INSERT INTO people (peopleID, peopleName, age, weight) VALUES (9, 'p9', 3, 12);

CREATE TABLE Orders (productId integer, seller VARCHAR(128), buyer VARCHAR(128), price INTEGER);
INSERT INTO Orders VALUES (1, 'a', 'a', 1);
INSERT INTO Orders VALUES (2, 'b', 'b', 2);
INSERT INTO Orders VALUES (3, 'c', 'c', 3);
INSERT INTO Orders VALUES (4, 'd', 'd', 4);
INSERT INTO Orders VALUES (5, 'e', 'e', 5);
INSERT INTO Orders VALUES (6, 'f', 'f', 6);
INSERT INTO Orders VALUES (7, 'g', 'g', 7);
INSERT INTO Orders VALUES (8, 'h', 'h', 8);
INSERT INTO Orders VALUES (9, 'i', 'i', 9);
INSERT INTO Orders VALUES (10, 'j', null, 10);
