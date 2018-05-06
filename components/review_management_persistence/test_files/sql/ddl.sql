CREATE TABLE scorecard_type_lu (
  scorecard_type_id INTEGER NOT NULL,
  PRIMARY KEY(scorecard_type_id)
);
CREATE TABLE scorecard (
  scorecard_id INTEGER NOT NULL,
  scorecard_type_id INTEGER NOT NULL,
  PRIMARY KEY(scorecard_id),
  FOREIGN KEY(scorecard_type_id)
    REFERENCES scorecard_type_lu(scorecard_type_id)
);
CREATE TABLE scorecard_question (
  scorecard_question_id INTEGER NOT NULL,
  PRIMARY KEY(scorecard_question_id)
);
CREATE TABLE resource (
  resource_id INTEGER NOT NULL,
  project_id INTEGER,
  PRIMARY KEY(resource_id)
);
CREATE TABLE upload (
  upload_id INTEGER NOT NULL,
  PRIMARY KEY(upload_id)
);
CREATE TABLE submission (
  submission_id INTEGER NOT NULL,
  PRIMARY KEY(submission_id)
);
CREATE TABLE comment_type_lu (
  comment_type_id INTEGER NOT NULL,
  name VARCHAR(64) NOT NULL,
  description VARCHAR(254) NOT NULL,
  create_user VARCHAR(64) NOT NULL,
  create_date DATETIME YEAR TO FRACTION(3) NOT NULL,
  modify_user VARCHAR(64) NOT NULL,
  modify_date DATETIME YEAR TO FRACTION(3) NOT NULL,
  PRIMARY KEY(comment_type_id)
);
CREATE TABLE review (
    review_id INT not null,
    resource_id INT not null,
    submission_id INT,
    scorecard_id INT not null,
    committed DECIMAL(1,0) not null,
    score FLOAT,
    initial_score DECIMAL(5,2),
    create_user VARCHAR(64) not null,
    create_date DATETIME YEAR TO FRACTION not null,
    modify_user VARCHAR(64) not null,
    modify_date DATETIME YEAR TO FRACTION not null,
  PRIMARY KEY(review_id),
  FOREIGN KEY(scorecard_id)
    REFERENCES scorecard(scorecard_id),
  FOREIGN KEY(submission_id)
    REFERENCES submission(submission_id),
  FOREIGN KEY(resource_id)
    REFERENCES resource(resource_id)
);
CREATE TABLE review_item (
    review_item_id INT not null,
    review_id INT not null,
    scorecard_question_id INT not null,
    upload_id INT,
    answer VARCHAR(254) not null,
    sort DECIMAL(3,0) not null,
    create_user VARCHAR(64) not null,
    create_date DATETIME YEAR TO FRACTION not null,
    modify_user VARCHAR(64) not null,
    modify_date DATETIME YEAR TO FRACTION not null,
  PRIMARY KEY(review_item_id),
  FOREIGN KEY(review_id)
    REFERENCES review(review_id),
  FOREIGN KEY(scorecard_question_id)
    REFERENCES scorecard_question(scorecard_question_id),
  FOREIGN KEY(upload_id)
    REFERENCES upload(upload_id)
);
CREATE TABLE review_comment (
    review_comment_id INT not null,
    resource_id INT not null,
    review_id INT not null,
    comment_type_id INT not null,
    content lvarchar(4096) not null,
    extra_info VARCHAR(254),
    sort DECIMAL(3,0) not null,
    create_user VARCHAR(64) not null,
    create_date DATETIME YEAR TO FRACTION not null,
    modify_user VARCHAR(64) not null,
    modify_date DATETIME YEAR TO FRACTION not null,
  PRIMARY KEY(review_comment_id),
  FOREIGN KEY(review_id)
    REFERENCES review(review_id),
  FOREIGN KEY(comment_type_id)
    REFERENCES comment_type_lu(comment_type_id),
  FOREIGN KEY(resource_id)
    REFERENCES resource(resource_id)
);
CREATE TABLE review_item_comment (
     review_item_comment_id INT not null,
    resource_id INT not null,
    review_item_id INT not null,
    comment_type_id INT not null,
    content lvarchar(4096) not null,
    extra_info VARCHAR(254),
    sort DECIMAL(3,0) not null,
    create_user VARCHAR(64) not null,
    create_date DATETIME YEAR TO FRACTION not null,
    modify_user VARCHAR(64) not null,
    modify_date DATETIME YEAR TO FRACTION not null,
  PRIMARY KEY(review_item_comment_id),
  FOREIGN KEY(review_item_id)
    REFERENCES review_item(review_item_id),
  FOREIGN KEY(comment_type_id)
    REFERENCES comment_type_lu(comment_type_id),
  FOREIGN KEY(resource_id)
    REFERENCES resource(resource_id)
);


create table id_sequences (
    name VARCHAR(254),
    next_block_start DECIMAL(12,0) not null,
    block_size DECIMAL(10,0) not null,
    exhausted DECIMAL(1,0) default 0 not null
);

INSERT INTO id_sequences(name, next_block_start, block_size, exhausted)
  VALUES('review_id_seq', 1, 20, 0);
INSERT INTO id_sequences(name, next_block_start, block_size, exhausted)
  VALUES('review_item_id_seq', 1, 20, 0);
INSERT INTO id_sequences(name, next_block_start, block_size, exhausted)
  VALUES('review_comment_id_seq', 1, 20, 0);
INSERT INTO id_sequences(name, next_block_start, block_size, exhausted)
  VALUES('review_item_comment_id_seq', 1, 20, 0);
  
alter table  review_item_comment lock mode (ROW);
alter table  review_comment lock mode (ROW);
alter table  review_item lock mode (ROW);
alter table  review lock mode (ROW);
alter table  scorecard lock mode (ROW);
alter table  scorecard_type_lu lock mode (ROW);
alter table  scorecard_question lock mode (ROW);
alter table  resource lock mode (ROW);
alter table  submission lock mode (ROW);
alter table  comment_type_lu lock mode (ROW);
alter table  upload lock mode (ROW);
alter table  id_sequences lock mode (ROW);