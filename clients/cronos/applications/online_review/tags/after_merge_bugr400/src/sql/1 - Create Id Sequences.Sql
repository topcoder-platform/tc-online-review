CREATE TABLE id_sequences (
  name                  VARCHAR(255)    NOT NULL,
  next_block_start      INTEGER         NOT NULL,
  block_size            INTEGER         NOT NULL,
  exhausted             INTEGER         NOT NULL,
  PRIMARY KEY (name)
);

INSERT INTO id_sequences(name, next_block_start, block_size, exhausted)
  VALUES('scorecard_id_seq', 100001, 20, 0);
INSERT INTO id_sequences(name, next_block_start, block_size, exhausted)
  VALUES('scorecard_group_id_seq', 100001, 20, 0);
INSERT INTO id_sequences(name, next_block_start, block_size, exhausted)
  VALUES('scorecard_section_id_seq', 100001, 20, 0);
INSERT INTO id_sequences(name, next_block_start, block_size, exhausted)
  VALUES('scorecard_question_id_seq', 100001, 20, 0);
INSERT INTO id_sequences(name, next_block_start, block_size, exhausted)
  VALUES('project_id_seq', 100001, 20, 0);
INSERT INTO id_sequences(name, next_block_start, block_size, exhausted)
  VALUES('project_phase_id_seq', 100001, 20, 0);
INSERT INTO id_sequences(name, next_block_start, block_size, exhausted)
  VALUES('resource_id_seq', 100001, 20, 0);
INSERT INTO id_sequences(name, next_block_start, block_size, exhausted)
  VALUES('resource_role_id_seq', 100001, 20, 0);
INSERT INTO id_sequences(name, next_block_start, block_size, exhausted)
  VALUES('notification_type_id_seq', 100001, 20, 0);
INSERT INTO id_sequences(name, next_block_start, block_size, exhausted)
  VALUES('upload_id_seq', 100001, 20, 0);
INSERT INTO id_sequences(name, next_block_start, block_size, exhausted)
  VALUES('upload_type_id_seq', 100001, 20, 0);
INSERT INTO id_sequences(name, next_block_start, block_size, exhausted)
  VALUES('upload_status_id_seq', 100001, 20, 0);
INSERT INTO id_sequences(name, next_block_start, block_size, exhausted)
  VALUES('submission_id_seq', 100001, 20, 0);
INSERT INTO id_sequences(name, next_block_start, block_size, exhausted)
  VALUES('submission_status_id_seq', 100001, 20, 0);
INSERT INTO id_sequences(name, next_block_start, block_size, exhausted)
  VALUES('review_id_seq', 100001, 20, 0);
INSERT INTO id_sequences(name, next_block_start, block_size, exhausted)
  VALUES('review_item_id_seq', 100001, 20, 0);
INSERT INTO id_sequences(name, next_block_start, block_size, exhausted)
  VALUES('review_comment_id_seq', 100001, 20, 0);
INSERT INTO id_sequences(name, next_block_start, block_size, exhausted)
  VALUES('review_item_comment_id_seq', 100001, 20, 0);
INSERT INTO id_sequences(name, next_block_start, block_size, exhausted)
  VALUES('project_audit_id_seq', 100001, 20, 0);
INSERT INTO id_sequences(name, next_block_start, block_size, exhausted)
  VALUES('screening_task_id_seq', 100001, 20, 0);
INSERT INTO id_sequences(name, next_block_start, block_size, exhausted)
  VALUES('screening_result_id_seq', 100001, 20, 0);




CREATE TABLE sequence_object
(
  name                  VARCHAR(255),
  id                    INTEGER,
  current_value         INTEGER
);

INSERT INTO sequence_object(name, id, current_value)
  VALUES('main_sequence', 100001, 100001);




CREATE TABLE component_inquiry 
( 
  component_inquiry_id  DECIMAL(12, 0) PRIMARY KEY,
  component_id          DECIMAL(12, 0),
  user_id               DECIMAL(12, 0),
  comment               VARCHAR(254),
  agreed_to_terms       DECIMAL(1, 0),
  rating                DECIMAL(5, 0),
  phase                 DECIMAL(12, 0),
  tc_user_id            DECIMAL(12, 0),
  version               DECIMAL(12, 0),
  create_time           DATETIME YEAR TO FRACTION(3),
  project_id            DECIMAL(10, 0)
); 




CREATE TABLE comp_version_dates
(
  comp_version_dates_id             DECIMAL(12)                   NOT NULL,
  comp_vers_id                      DECIMAL(12),
  phase_id                          DECIMAL(12),
  posting_date                      DATETIME YEAR TO FRACTION(3),
  initial_submission_date           DATETIME YEAR TO FRACTION(3),
  winner_announced_date             DATETIME YEAR TO FRACTION(3),
  final_submission_date             DATETIME YEAR TO FRACTION(3),
  estimated_dev_date                DATETIME YEAR TO FRACTION(3),
  price                             DECIMAL(10, 2),
  total_submissions                 DECIMAL(12),
  status_id                         DECIMAL(12),
  create_time                       DATETIME YEAR TO FRACTION(3),
  level_id                          DECIMAL(12),
  screening_complete_date           DATETIME YEAR TO FRACTION(3),
  review_complete_date              DATETIME YEAR TO FRACTION(3),
  aggregation_complete_date         DATETIME YEAR TO FRACTION(3),
  phase_complete_date               DATETIME YEAR TO FRACTION(3),
  production_date                   DATETIME YEAR TO FRACTION(3),
  aggregation_complete_date_comment VARCHAR(254),
  phase_complete_date_comment       VARCHAR(254),
  review_complete_date_comment      VARCHAR(254),
  winner_announced_date_comment     VARCHAR(254),
  initial_submission_date_comment   VARCHAR(254),
  screening_complete_date_comment   VARCHAR(254),
  final_submission_date_comment     VARCHAR(254),
  production_date_comment           VARCHAR(254),
  modify_date                       DATETIME YEAR TO FRACTION(3),
  PRIMARY KEY(comp_version_dates_id) CONSTRAINT PK_COMPONENT_VERSION_DATES
);

CREATE INDEX compverdates_idx2 ON comp_version_dates(comp_vers_id, phase_id);
CREATE INDEX compverdates_idx1 ON comp_version_dates(status_id);




CREATE TABLE rboard_payment
(
  project_id                        DECIMAL(10)                   NOT NULL,
  phase_id                          DECIMAL(5)                    NOT NULL,
  primary_ind                       DECIMAL(1)                    NOT NULL,
  amount                            DECIMAL(7, 2),
  modify_date                       DATETIME YEAR TO FRACTION(3),
  create_date                       DATETIME YEAR TO FRACTION(3),
  PRIMARY KEY (project_id, phase_id, primary_ind) CONSTRAINT RBOARD_PAYMENT_PK
);
