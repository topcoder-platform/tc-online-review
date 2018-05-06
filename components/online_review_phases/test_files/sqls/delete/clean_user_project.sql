database online_review;

-- FOR THE USER PROJECT DATA STORE COMPONENT

DELETE FROM comp_forum_xref;
DELETE FROM comp_versions;
DELETE FROM categories;
DELETE FROM comp_catalog;
DELETE FROM user_reliability;
DELETE FROM user_rating;
DELETE FROM user;
DELETE FROM email;

DROP TABLE comp_forum_xref;
DROP TABLE comp_versions;
DROP TABLE categories;
DROP TABLE comp_catalog;
DROP TABLE user_reliability;
DROP TABLE user_rating;
DROP TABLE user;
DROP TABLE email;

close database;
