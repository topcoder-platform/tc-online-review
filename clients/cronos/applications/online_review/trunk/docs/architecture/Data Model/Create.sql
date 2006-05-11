CREATE TABLE project_type_lu (
  project_type_id               INTEGER                     NOT NULL,
  name                          VARCHAR(64)                 NOT NULL,
  description                   VARCHAR(255)                NOT NULL,
  creation_user                 VARCHAR(64)                 NOT NULL,
  creation_date                 DATETIME YEAR TO SECOND     NOT NULL,
  modification_user             VARCHAR(64)                 NOT NULL,
  modification_date             DATETIME YEAR TO SECOND     NOT NULL,
  PRIMARY KEY(project_type_id)
);

CREATE TABLE project_category_lu (
  project_category_id           INTEGER                     NOT NULL,
  project_type_id               INTEGER                     NOT NULL,
  name                          VARCHAR(64)                 NOT NULL,
  description                   VARCHAR(255)                NOT NULL,
  creation_user                 VARCHAR(64)                 NOT NULL,
  creation_date                 DATETIME YEAR TO SECOND     NOT NULL,
  modification_user             VARCHAR(64)                 NOT NULL,
  modification_date             DATETIME YEAR TO SECOND     NOT NULL,
  PRIMARY KEY(project_category_id),
  FOREIGN KEY(project_type_id)
    REFERENCES project_type_lu(project_type_id)
);

CREATE TABLE scorecard_type_lu (
  scorecard_type_id             INTEGER                     NOT NULL,
  name                          VARCHAR(64)                 NOT NULL,
  description                   VARCHAR(255)                NOT NULL,
  creation_user                 VARCHAR(64)                 NOT NULL,
  creation_date                 DATETIME YEAR TO SECOND     NOT NULL,
  modification_user             VARCHAR(64)                 NOT NULL,
  modification_date             DATETIME YEAR TO SECOND     NOT NULL,
  PRIMARY KEY(scorecard_type_id)
);

CREATE TABLE scorecard_status_lu (
  scorecard_status_id           INTEGER                     NOT NULL,
  name                          VARCHAR(64)                 NOT NULL,
  description                   VARCHAR(255)                NOT NULL,
  creation_user                 VARCHAR(64)                 NOT NULL,
  creation_date                 DATETIME YEAR TO SECOND     NOT NULL,
  modification_user             VARCHAR(64)                 NOT NULL,
  modification_date             DATETIME YEAR TO SECOND     NOT NULL,
  PRIMARY KEY(scorecard_status_id)
);

CREATE TABLE scorecard (
  scorecard_id                  INTEGER                     NOT NULL,
  scorecard_status_id           INTEGER                     NOT NULL,
  scorecard_type_id             INTEGER                     NOT NULL,
  project_category_id           INTEGER                     NOT NULL,
  name                          VARCHAR(64)                 NOT NULL,
  version                       VARCHAR(16)                 NOT NULL,
  min_score                     FLOAT                       NOT NULL,
  max_score                     FLOAT                       NOT NULL,
  creation_user                 VARCHAR(64)                 NOT NULL,
  creation_date                 DATETIME YEAR TO SECOND     NOT NULL,
  modification_user             VARCHAR(64)                 NOT NULL,
  modification_date             DATETIME YEAR TO SECOND     NOT NULL,
  PRIMARY KEY(scorecard_id),
  FOREIGN KEY(scorecard_type_id)
    REFERENCES scorecard_type_lu(scorecard_type_id),
  FOREIGN KEY(project_category_id)
    REFERENCES project_category_lu(project_category_id),
  FOREIGN KEY(scorecard_status_id)
    REFERENCES scorecard_status_lu(scorecard_status_id)
);

CREATE TABLE scorecard_group (
  scorecard_group_id            INTEGER                     NOT NULL,
  scorecard_id                  INTEGER                     NOT NULL,
  name                          VARCHAR(64)                 NOT NULL,
  weight                        FLOAT                       NOT NULL,
  creation_user                 VARCHAR(64)                 NOT NULL,
  creation_date                 DATETIME YEAR TO SECOND     NOT NULL,
  modification_user             VARCHAR(64)                 NOT NULL,
  modification_date             DATETIME YEAR TO SECOND     NOT NULL,
  PRIMARY KEY(scorecard_group_id),
  FOREIGN KEY(scorecard_id)
    REFERENCES scorecard(scorecard_id)
);

CREATE TABLE scorecard_section (
  scorecard_section_id          INTEGER                     NOT NULL,
  scorecard_group_id            INTEGER                     NOT NULL,
  name                          VARCHAR(64)                 NOT NULL,
  weight                        FLOAT                       NOT NULL,
  creation_user                 VARCHAR(64)                 NOT NULL,
  creation_date                 DATETIME YEAR TO SECOND     NOT NULL,
  modification_user             VARCHAR(64)                 NOT NULL,
  modification_date             DATETIME YEAR TO SECOND     NOT NULL,
  PRIMARY KEY(scorecard_section_id),
  FOREIGN KEY(scorecard_group_id)
    REFERENCES scorecard_group(scorecard_group_id)
);

CREATE TABLE scorecard_question_type_lu (
  scorecard_question_type_id    INTEGER                     NOT NULL,
  name                          VARCHAR(64)                 NOT NULL,
  description                   VARCHAR(255)                NOT NULL,
  creation_user                 VARCHAR(64)                 NOT NULL,
  creation_date                 DATETIME YEAR TO SECOND     NOT NULL,
  modification_user             VARCHAR(64)                 NOT NULL,
  modification_date             DATETIME YEAR TO SECOND     NOT NULL,
  PRIMARY KEY(scorecard_question_type_id)
);

CREATE TABLE scorecard_question (
  scorecard_question_id         INTEGER                     NOT NULL,
  scorecard_question_type_id    INTEGER                     NOT NULL,
  scorecard_section_id          INTEGER                     NOT NULL,
  description                   LVARCHAR(4096)              NOT NULL,
  guideline                     LVARCHAR(4096),
  weight                        FLOAT                       NOT NULL,
  upload_document               BOOLEAN                     NOT NULL,
  upload_document_required      BOOLEAN                     NOT NULL,
  creation_user                 VARCHAR(64)                 NOT NULL,
  creation_date                 DATETIME YEAR TO SECOND     NOT NULL,
  modification_user             VARCHAR(64)                 NOT NULL,
  modification_date             DATETIME YEAR TO SECOND     NOT NULL,
  PRIMARY KEY(scorecard_question_id),
  FOREIGN KEY(scorecard_section_id)
    REFERENCES scorecard_section(scorecard_section_id),
  FOREIGN KEY(scorecard_question_type_id)
    REFERENCES scorecard_question_type_lu(scorecard_question_type_id)
);

CREATE TABLE project_status_lu (
  project_status_id             INTEGER                     NOT NULL,
  name                          VARCHAR(64)                 NOT NULL,
  description                   VARCHAR(255)                NOT NULL,
  creation_user                 VARCHAR(64)                 NOT NULL,
  creation_date                 DATETIME YEAR TO SECOND     NOT NULL,
  modification_user             VARCHAR(64)                 NOT NULL,
  modification_date             DATETIME YEAR TO SECOND     NOT NULL,
  PRIMARY KEY(project_status_id)
);

CREATE TABLE project (
  project_id                    INTEGER                     NOT NULL,
  project_status_id             INTEGER                     NOT NULL,
  project_category_id           INTEGER                     NOT NULL,
  creation_user                 VARCHAR(64)                 NOT NULL,
  creation_date                 DATETIME YEAR TO SECOND     NOT NULL,
  modification_user             VARCHAR(64)                 NOT NULL,
  modification_date             DATETIME YEAR TO SECOND     NOT NULL,
  PRIMARY KEY(project_id),
  FOREIGN KEY(project_category_id)
    REFERENCES project_category_lu(project_category_id),
  FOREIGN KEY(project_status_id)
    REFERENCES project_status_lu(project_status_id)
);

CREATE TABLE project_info_type_lu (
  project_info_type_id          INTEGER                     NOT NULL,
  name                          VARCHAR(64)                 NOT NULL,
  description                   VARCHAR(25)                 NOT NULL,
  creation_user                 VARCHAR(64)                 NOT NULL,
  creation_date                 DATETIME YEAR TO SECOND     NOT NULL,
  modification_user             VARCHAR(64)                 NOT NULL,
  modification_date             DATETIME YEAR TO SECOND     NOT NULL,
  PRIMARY KEY(project_info_type_id)
);

CREATE TABLE project_info (
  project_id                    INTEGER                     NOT NULL,
  project_info_type_id          INTEGER                     NOT NULL,
  value                         VARCHAR(255)                NOT NULL,
  creation_user                 VARCHAR(64)                 NOT NULL,
  creation_date                 DATETIME YEAR TO SECOND     NOT NULL,
  modification_user             VARCHAR(64)                 NOT NULL,
  modification_date             DATETIME YEAR TO SECOND     NOT NULL,
  PRIMARY KEY(project_id, project_info_type_id),
  FOREIGN KEY(project_info_type_id)
    REFERENCES project_info_type_lu(project_info_type_id),
  FOREIGN KEY(project_id)
    REFERENCES project(project_id)
);

CREATE TABLE project_scorecard (
  project_id                    INTEGER                     NOT NULL,
  scorecard_id                  INTEGER                     NOT NULL,
  creation_user                 VARCHAR(64)                 NOT NULL,
  creation_date                 DATETIME YEAR TO SECOND     NOT NULL,
  modification_user             VARCHAR(64)                 NOT NULL,
  modification_date             DATETIME YEAR TO SECOND     NOT NULL,
  PRIMARY KEY(project_id, scorecard_id),
  FOREIGN KEY(project_id)
    REFERENCES project(project_id),
  FOREIGN KEY(scorecard_id)
    REFERENCES scorecard(scorecard_id)
);

CREATE TABLE phase_type_lu (
  phase_type_id                 INTEGER                     NOT NULL,
  name                          VARCHAR(64)                 NOT NULL,
  description                   VARCHAR(255)                NOT NULL,
  creation_user                 VARCHAR(64)                 NOT NULL,
  creation_date                 DATETIME YEAR TO SECOND     NOT NULL,
  modification_user             VARCHAR(64)                 NOT NULL,
  modification_date             DATETIME YEAR TO SECOND     NOT NULL,
  PRIMARY KEY(phase_type_id)
);

CREATE TABLE phase (
  phase_id                      INTEGER                     NOT NULL,
  project_id                    INTEGER                     NOT NULL,
  phase_type_id                 INTEGER                     NOT NULL,
  absolute_start_time           DATETIME YEAR TO SECOND,
  previous_phase_id             INTEGER,
  previous_phase_end            BOOLEAN,
  lag_time                      INTERVAL DAY TO SECOND,
  duration                      INTERVAL DAY TO SECOND      NOT NULL,
  creation_user                 VARCHAR(64)                 NOT NULL,
  creation_date                 DATETIME YEAR TO SECOND     NOT NULL,
  modification_user             VARCHAR(64)                 NOT NULL,
  modification_date             DATETIME YEAR TO SECOND     NOT NULL,
  PRIMARY KEY(phase_id),
  FOREIGN KEY(phase_type_id)
    REFERENCES phase_type_lu(phase_type_id)
);

CREATE TABLE phase_criteria (
  phase_criteria_id             INTEGER                     NOT NULL,
  phase_id                      INTEGER                     NOT NULL,
  parameter                     VARCHAR(255)                NOT NULL,
  creation_user                 VARCHAR(64)                 NOT NULL,
  creation_date                 DATETIME YEAR TO SECOND     NOT NULL,
  modification_user             VARCHAR(64)                 NOT NULL,
  modification_date             DATETIME YEAR TO SECOND     NOT NULL,
  PRIMARY KEY(phase_criteria_id),
  FOREIGN KEY(phase_id)
    REFERENCES phase(phase_id)
);

CREATE TABLE resource_role_lu (
  resource_role_id              INTEGER                     NOT NULL,
  phase_type_id                 INTEGER,
  name                          VARCHAR(64)                 NOT NULL,
  description                   VARCHAR(255)                NOT NULL,
  creation_user                 VARCHAR(64)                 NOT NULL,
  creation_date                 DATETIME YEAR TO SECOND     NOT NULL,
  modification_user             VARCHAR(64)                 NOT NULL,
  modification_date             DATETIME YEAR TO SECOND     NOT NULL,
  PRIMARY KEY(resource_role_id),
  FOREIGN KEY(phase_type_id)
    REFERENCES phase_type_lu(phase_type_id)
);

CREATE TABLE resource (
  resource_id                   INTEGER                     NOT NULL,
  resource_role_id              INTEGER                     NOT NULL,
  project_id                    INTEGER                     NOT NULL,
  phase_id                      INTEGER,
  creation_user                 VARCHAR(64)                 NOT NULL,
  creation_date                 DATETIME YEAR TO SECOND     NOT NULL,
  modification_user             VARCHAR(64)                 NOT NULL,
  modification_date             DATETIME YEAR TO SECOND     NOT NULL,
  PRIMARY KEY(resource_id),
  FOREIGN KEY(project_id)
    REFERENCES project(project_id),
  FOREIGN KEY(resource_role_id)
    REFERENCES resource_role_lu(resource_role_id),
  FOREIGN KEY(phase_id)
    REFERENCES phase(phase_id)
);

CREATE TABLE resource_info_type_lu (
  resource_info_type_id         INTEGER                     NOT NULL,
  name                          VARCHAR(64)                 NOT NULL,
  description                   VARCHAR(255)                NOT NULL,
  creation_user                 VARCHAR(64)                 NOT NULL,
  creation_date                 DATETIME YEAR TO SECOND     NOT NULL,
  modification_user             VARCHAR(64)                 NOT NULL,
  modification_date             DATETIME YEAR TO SECOND     NOT NULL,
  PRIMARY KEY(resource_info_type_id)
);

CREATE TABLE resource_info (
  resource_id                   INTEGER                     NOT NULL,
  resource_info_type_id         INTEGER                     NOT NULL,
  value                         VARCHAR(255)                NOT NULL,
  creation_user                 VARCHAR(64)                 NOT NULL,
  creation_date                 DATETIME YEAR TO SECOND     NOT NULL,
  modification_user             VARCHAR(64)                 NOT NULL,
  modification_date             DATETIME YEAR TO SECOND     NOT NULL,
  PRIMARY KEY(resource_id, resource_info_type_id),
  FOREIGN KEY(resource_info_type_id)
    REFERENCES resource_info_type_lu(resource_info_type_id),
  FOREIGN KEY(resource_id)
    REFERENCES resource(resource_id)
);

CREATE TABLE upload_type_lu (
  upload_type_id                INTEGER                     NOT NULL,
  name                          VARCHAR(64)                 NOT NULL,
  description                   VARCHAR(255)                NOT NULL,
  creation_user                 VARCHAR(64)                 NOT NULL,
  creation_date                 DATETIME YEAR TO SECOND     NOT NULL,
  modification_user             VARCHAR(64)                 NOT NULL,
  modification_date             DATETIME YEAR TO SECOND     NOT NULL,
  PRIMARY KEY(upload_type_id)
);

CREATE TABLE upload (
  upload_id                     INTEGER                     NOT NULL,
  project_id                    INTEGER                     NOT NULL,
  resource_id                   INTEGER                     NOT NULL,
  upload_type_id                INTEGER                     NOT NULL,
  parameter                     VARCHAR(255)                NOT NULL,
  creation_user                 VARCHAR(64)                 NOT NULL,
  creation_date                 DATETIME YEAR TO SECOND     NOT NULL,
  modification_user             VARCHAR(64)                 NOT NULL,
  modification_date             DATETIME YEAR TO SECOND     NOT NULL,
  PRIMARY KEY(upload_id),
  FOREIGN KEY(upload_type_id)
    REFERENCES upload_type_lu(upload_type_id),
  FOREIGN KEY(resource_id)
    REFERENCES resource(resource_id),
  FOREIGN KEY(project_id)
    REFERENCES project(project_id)
);

CREATE TABLE submission_status_lu (
  submission_status_id          INTEGER                     NOT NULL,
  name                          VARCHAR(64)                 NOT NULL,
  description                   VARCHAR(255)                NOT NULL,
  creation_user                 VARCHAR(64)                 NOT NULL,
  creation_date                 DATETIME YEAR TO SECOND     NOT NULL,
  modification_user             VARCHAR(64)                 NOT NULL,
  modification_date             DATETIME YEAR TO SECOND     NOT NULL,
  PRIMARY KEY(submission_status_id)
);

CREATE TABLE submission (
  submission_id                 INTEGER                     NOT NULL,
  upload_id                     INTEGER                     NOT NULL,
  submission_status_id          INTEGER                     NOT NULL,
  creation_user                 VARCHAR(64)                 NOT NULL,
  creation_date                 DATETIME YEAR TO SECOND     NOT NULL,
  modification_user             VARCHAR(64)                 NOT NULL,
  modification_date             DATETIME YEAR TO SECOND     NOT NULL,
  PRIMARY KEY(submission_id),
  FOREIGN KEY(submission_status_id)
    REFERENCES submission_status_lu(submission_status_id),
  FOREIGN KEY(upload_id)
    REFERENCES upload(upload_id)
);

CREATE TABLE resource_submission (
  resource_id                   INTEGER                     NOT NULL,
  submission_id                 INTEGER                     NOT NULL,
  creation_user                 VARCHAR(64)                 NOT NULL,
  creation_date                 DATETIME YEAR TO SECOND     NOT NULL,
  modification_user             VARCHAR(64)                 NOT NULL,
  modification_date             DATETIME YEAR TO SECOND     NOT NULL,
  PRIMARY KEY(resource_id, submission_id),
  FOREIGN KEY(submission_id)
    REFERENCES submission(submission_id),
  FOREIGN KEY(resource_id)
    REFERENCES resource(resource_id)
);

CREATE TABLE project_phase (
  project_id                    INTEGER                     NOT NULL,
  phase_id                      INTEGER                     NOT NULL,
  creation_user                 VARCHAR(64)                 NOT NULL,
  creation_date                 DATETIME YEAR TO SECOND     NOT NULL,
  modification_user             VARCHAR(64)                 NOT NULL,
  modification_date             DATETIME YEAR TO SECOND     NOT NULL,
  PRIMARY KEY(project_id, phase_id),
  FOREIGN KEY(project_id)
    REFERENCES project(project_id),
  FOREIGN KEY(phase_id)
    REFERENCES phase(phase_id)
);

CREATE TABLE submission_phase (
  submission_id                 INTEGER                     NOT NULL,
  phase_id                      INTEGER                     NOT NULL,
  creation_user                 VARCHAR(64)                 NOT NULL,
  creation_date                 DATETIME YEAR TO SECOND     NOT NULL,
  modification_user             VARCHAR(64)                 NOT NULL,
  modification_date             DATETIME YEAR TO SECOND     NOT NULL,
  PRIMARY KEY(submission_id, phase_id),
  FOREIGN KEY(submission_id)
    REFERENCES submission(submission_id),
  FOREIGN KEY(phase_id)
    REFERENCES phase(phase_id)
);

CREATE TABLE comment_type_lu (
  comment_type_id               INTEGER                     NOT NULL,
  name                          VARCHAR(64)                 NOT NULL,
  description                   VARCHAR(255)                NOT NULL,
  creation_user                 VARCHAR(64)                 NOT NULL,
  creation_date                 DATETIME YEAR TO SECOND     NOT NULL,
  modification_user             VARCHAR(64)                 NOT NULL,
  modification_date             DATETIME YEAR TO SECOND     NOT NULL,
  PRIMARY KEY(comment_type_id)
);

CREATE TABLE review (
  review_id                     INTEGER                     NOT NULL,
  resource_id                   INTEGER                     NOT NULL,
  submission_id                 INTEGER                     NOT NULL,
  scorecard_id                  INTEGER                     NOT NULL,
  committed                     BOOLEAN                     NOT NULL,
  score                         FLOAT,
  creation_user                 VARCHAR(64)                 NOT NULL,
  creation_date                 DATETIME YEAR TO SECOND     NOT NULL,
  modification_user             VARCHAR(64)                 NOT NULL,
  modification_date             DATETIME YEAR TO SECOND     NOT NULL,
  PRIMARY KEY(review_id),
  FOREIGN KEY(scorecard_id)
    REFERENCES scorecard(scorecard_id),
  FOREIGN KEY(submission_id)
    REFERENCES submission(submission_id),
  FOREIGN KEY(resource_id)
    REFERENCES resource(resource_id)
);

CREATE TABLE review_item (
  review_item_id                INTEGER                     NOT NULL,
  scorecard_question_id         INTEGER                     NOT NULL,
  score                         VARCHAR(255)                NOT NULL,
  creation_user                 VARCHAR(64)                 NOT NULL,
  creation_date                 DATETIME YEAR TO SECOND     NOT NULL,
  modification_user             VARCHAR(64)                 NOT NULL,
  modification_date             DATETIME YEAR TO SECOND     NOT NULL,
  PRIMARY KEY(review_item_id),
  FOREIGN KEY(scorecard_question_id)
    REFERENCES scorecard_question(scorecard_question_id)
);

CREATE TABLE review_comment (
  review_comment_id             INTEGER                     NOT NULL,
  resource_id                   INTEGER                     NOT NULL,
  review_id                     INTEGER                     NOT NULL,
  comment_type_id               INTEGER                     NOT NULL,
  content                       LVARCHAR(4096)              NOT NULL,
  extra_info                    VARCHAR(255),
  creation_user                 VARCHAR(64)                 NOT NULL,
  creation_date                 DATETIME YEAR TO SECOND     NOT NULL,
  modification_user             VARCHAR(64)                 NOT NULL,
  modification_date             DATETIME YEAR TO SECOND     NOT NULL,
  PRIMARY KEY(review_comment_id),
  FOREIGN KEY(review_id)
    REFERENCES review(review_id),
  FOREIGN KEY(comment_type_id)
    REFERENCES comment_type_lu(comment_type_id),
  FOREIGN KEY(resource_id)
    REFERENCES resource(resource_id)
);

CREATE TABLE review_item_comment (
  review_item_comment_id        INTEGER                     NOT NULL,
  resource_id                   INTEGER                     NOT NULL,
  review_item_id                INTEGER                     NOT NULL,
  comment_type_id               INTEGER                     NOT NULL,
  content                       LVARCHAR(4096)              NOT NULL,
  extra_info                    VARCHAR(255),
  creation_user                 VARCHAR(64)                 NOT NULL,
  creation_date                 DATETIME YEAR TO SECOND     NOT NULL,
  modification_user             VARCHAR(64)                 NOT NULL,
  modification_date             DATETIME YEAR TO SECOND     NOT NULL,
  PRIMARY KEY(review_item_comment_id),
  FOREIGN KEY(review_item_id)
    REFERENCES review_item(review_item_id),
  FOREIGN KEY(comment_type_id)
    REFERENCES comment_type_lu(comment_type_id),
  FOREIGN KEY(resource_id)
    REFERENCES resource(resource_id)
);

CREATE TABLE deliverable_lu (
  deliverable_id                INTEGER                     NOT NULL,
  phase_type_id                 INTEGER                     NOT NULL,
  resource_role_id              INTEGER                     NOT NULL,
  name                          VARCHAR(64)                 NOT NULL,
  description                   VARCHAR(64)                 NOT NULL,
  creation_user                 VARCHAR(64)                 NOT NULL,
  creation_date                 DATETIME YEAR TO SECOND     NOT NULL,
  modification_user             VARCHAR(64)                 NOT NULL,
  modification_date             DATETIME YEAR TO SECOND     NOT NULL,
  PRIMARY KEY(deliverable_id),
  FOREIGN KEY(phase_type_id)
    REFERENCES phase_type_lu(phase_type_id),
  FOREIGN KEY(resource_role_id)
    REFERENCES resource_role_lu(resource_role_id)
);

CREATE TABLE project_audit (
  project_audit_id              INTEGER                     NOT NULL,
  project_id                    INTEGER                     NOT NULL,
  update_reason                 VARCHAR(255)                NOT NULL,
  creation_user                 VARCHAR(64)                 NOT NULL,
  creation_date                 DATETIME YEAR TO SECOND     NOT NULL,
  modification_user             VARCHAR(64)                 NOT NULL,
  modification_date             DATETIME YEAR TO SECOND     NOT NULL,
  PRIMARY KEY(project_audit_id),
  FOREIGN KEY(project_id)
    REFERENCES project(project_id)
);

CREATE TABLE timeline_notification (
  project_id                    INTEGER                     NOT NULL,
  handle                        VARCHAR(64)                 NOT NULL,
  creation_user                 VARCHAR(64)                 NOT NULL,
  creation_date                 DATETIME YEAR TO SECOND     NOT NULL,
  modification_user             VARCHAR(64)                 NOT NULL,
  modification_date             DATETIME YEAR TO SECOND     NOT NULL,
  PRIMARY KEY(project_id, handle),
  FOREIGN KEY(project_id)
    REFERENCES project(project_id)
);