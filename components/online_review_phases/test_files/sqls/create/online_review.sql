database online_review;

CREATE TABLE project_type_lu (
  project_type_id               INTEGER                         NOT NULL,
  name                          VARCHAR(64)                     NOT NULL,
  description                   VARCHAR(254)                    NOT NULL,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(project_type_id)
);
CREATE TABLE project_category_lu (
  project_category_id           INTEGER                         NOT NULL,
  project_type_id               INTEGER                         NOT NULL,
  name                          VARCHAR(64)                     NOT NULL,
  description                   VARCHAR(254)                    NOT NULL,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(project_category_id),
  FOREIGN KEY(project_type_id)
    REFERENCES project_type_lu(project_type_id)
);
CREATE TABLE scorecard_type_lu (
  scorecard_type_id             INTEGER                         NOT NULL,
  name                          VARCHAR(64)                     NOT NULL,
  description                   VARCHAR(254)                    NOT NULL,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(scorecard_type_id)
);
CREATE TABLE scorecard_status_lu (
  scorecard_status_id           INTEGER                         NOT NULL,
  name                          VARCHAR(64)                     NOT NULL,
  description                   VARCHAR(254)                    NOT NULL,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(scorecard_status_id)
);
CREATE TABLE scorecard (
  scorecard_id                  INTEGER                         NOT NULL,
  scorecard_status_id           INTEGER                         NOT NULL,
  scorecard_type_id             INTEGER                         NOT NULL,
  project_category_id           INTEGER                         NOT NULL,
  name                          VARCHAR(64)                     NOT NULL,
  version                       VARCHAR(16)                     NOT NULL,
  min_score                     FLOAT                           NOT NULL,
  max_score                     FLOAT                           NOT NULL,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(scorecard_id),
  FOREIGN KEY(scorecard_type_id)
    REFERENCES scorecard_type_lu(scorecard_type_id),
  FOREIGN KEY(project_category_id)
    REFERENCES project_category_lu(project_category_id),
  FOREIGN KEY(scorecard_status_id)
    REFERENCES scorecard_status_lu(scorecard_status_id)
);
CREATE TABLE scorecard_group (
  scorecard_group_id            INTEGER                         NOT NULL,
  scorecard_id                  INTEGER                         NOT NULL,
  name                          VARCHAR(64)                     NOT NULL,
  weight                        FLOAT                           NOT NULL,
  sort                          DECIMAL(3, 0)                   NOT NULL,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(scorecard_group_id),
  FOREIGN KEY(scorecard_id)
    REFERENCES scorecard(scorecard_id)
);
CREATE TABLE scorecard_section (
  scorecard_section_id          INTEGER                         NOT NULL,
  scorecard_group_id            INTEGER                         NOT NULL,
  name                          VARCHAR(64)                     NOT NULL,
  weight                        FLOAT                           NOT NULL,
  sort                          DECIMAL(3, 0)                   NOT NULL,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(scorecard_section_id),
  FOREIGN KEY(scorecard_group_id)
    REFERENCES scorecard_group(scorecard_group_id)
);
CREATE TABLE scorecard_question_type_lu (
  scorecard_question_type_id    INTEGER                         NOT NULL,
  name                          VARCHAR(64)                     NOT NULL,
  description                   VARCHAR(254)                    NOT NULL,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(scorecard_question_type_id)
);
CREATE TABLE scorecard_question (
  scorecard_question_id         INTEGER                         NOT NULL,
  scorecard_question_type_id    INTEGER                         NOT NULL,
  scorecard_section_id          INTEGER                         NOT NULL,
  description                   LVARCHAR(4096)                  NOT NULL,
  guideline                     LVARCHAR(4096),
  weight                        FLOAT                           NOT NULL,
  sort                          DECIMAL(3, 0)                   NOT NULL,
  upload_document               DECIMAL(1, 0)                   NOT NULL,
  upload_document_required      DECIMAL(1, 0)                   NOT NULL,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(scorecard_question_id),
  FOREIGN KEY(scorecard_section_id)
    REFERENCES scorecard_section(scorecard_section_id),
  FOREIGN KEY(scorecard_question_type_id)
    REFERENCES scorecard_question_type_lu(scorecard_question_type_id)
);
CREATE TABLE project_status_lu (
  project_status_id             INTEGER                         NOT NULL,
  name                          VARCHAR(64)                     NOT NULL,
  description                   VARCHAR(254)                    NOT NULL,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(project_status_id)
);
CREATE TABLE project (
  project_id                    INTEGER                         NOT NULL,
  project_status_id             INTEGER                         NOT NULL,
  project_category_id           INTEGER                         NOT NULL,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(project_id),
  FOREIGN KEY(project_category_id)
    REFERENCES project_category_lu(project_category_id),
  FOREIGN KEY(project_status_id)
    REFERENCES project_status_lu(project_status_id)
);
CREATE TABLE project_info_type_lu (
  project_info_type_id          INTEGER                         NOT NULL,
  name                          VARCHAR(64)                     NOT NULL,
  description                   VARCHAR(25)                     NOT NULL,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(project_info_type_id)
);
CREATE TABLE project_info (
  project_id                    INTEGER                         NOT NULL,
  project_info_type_id          INTEGER                         NOT NULL,
  value                         LVARCHAR(4096)                  NOT NULL,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(project_id, project_info_type_id),
  FOREIGN KEY(project_info_type_id)
    REFERENCES project_info_type_lu(project_info_type_id),
  FOREIGN KEY(project_id)
    REFERENCES project(project_id)
);
CREATE TABLE scorecard_assignment_lu (
  scorecard_assignment_id       INTEGER                         NOT NULL,
  name                          VARCHAR(64)                     NOT NULL,
  description                   VARCHAR(25)                     NOT NULL,
  scorecard_type_id             INTEGER                         NOT NULL,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(scorecard_assignment_id),
  FOREIGN KEY(scorecard_type_id)
    REFERENCES scorecard_type_lu(scorecard_type_id)
);
CREATE TABLE project_scorecard (
  project_id                    INTEGER                         NOT NULL,
  scorecard_id                  INTEGER                         NOT NULL,
  scorecard_assignment_id       INTEGER                         NOT NULL,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(project_id, scorecard_id, scorecard_assignment_id),
  FOREIGN KEY(project_id)
    REFERENCES project(project_id),
  FOREIGN KEY(scorecard_id)
    REFERENCES scorecard(scorecard_id),
  FOREIGN KEY(scorecard_assignment_id)
    REFERENCES scorecard_assignment_lu(scorecard_assignment_id)
);
CREATE TABLE phase_status_lu (
  phase_status_id               INTEGER                         NOT NULL,
  name                          VARCHAR(64)                     NOT NULL,
  description                   VARCHAR(254)                    NOT NULL,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(phase_status_id)
);
CREATE TABLE phase_type_lu (
  phase_type_id                 INTEGER                         NOT NULL,
  name                          VARCHAR(64)                     NOT NULL,
  description                   VARCHAR(254)                    NOT NULL,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(phase_type_id)
);
CREATE TABLE project_phase (
  project_phase_id              INTEGER                         NOT NULL,
  project_id                    INTEGER                         NOT NULL,
  phase_type_id                 INTEGER                         NOT NULL,
  phase_status_id               INTEGER                         NOT NULL,
  fixed_start_time              DATETIME YEAR TO FRACTION(3),
  scheduled_start_time          DATETIME YEAR TO FRACTION(3)    NOT NULL,
  scheduled_end_time            DATETIME YEAR TO FRACTION(3)    NOT NULL,
  actual_start_time             DATETIME YEAR TO FRACTION(3),
  actual_end_time               DATETIME YEAR TO FRACTION(3),
  duration 						DECIMAL(16, 0)					NOT NULL,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(project_phase_id),
  FOREIGN KEY(phase_type_id)
    REFERENCES phase_type_lu(phase_type_id),
  FOREIGN KEY(project_id)
    REFERENCES project(project_id),
  FOREIGN KEY(phase_status_id)
    REFERENCES phase_status_lu(phase_status_id)
);
CREATE TABLE phase_dependency (
  dependency_phase_id           INTEGER                         NOT NULL,
  dependent_phase_id            INTEGER                         NOT NULL,
  dependency_start              DECIMAL(1, 0)                   NOT NULL,
  dependent_start               DECIMAL(1, 0)                   NOT NULL,
  lag_time                      DECIMAL(16, 0)                  NOT NULL,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(dependency_phase_id, dependent_phase_id),
  FOREIGN KEY(dependency_phase_id)
    REFERENCES project_phase(project_phase_id),
  FOREIGN KEY(dependent_phase_id)
    REFERENCES project_phase(project_phase_id)
);
CREATE TABLE phase_criteria_type_lu (
  phase_criteria_type_id        INTEGER                         NOT NULL,
  name                          VARCHAR(64)                     NOT NULL,
  description                   VARCHAR(254)                    NOT NULL,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(phase_criteria_type_id)
);
CREATE TABLE phase_criteria (
  project_phase_id              INTEGER                         NOT NULL,
  phase_criteria_type_id        INTEGER                         NOT NULL,
  parameter                     VARCHAR(254)                    NOT NULL,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(project_phase_id, phase_criteria_type_id),
  FOREIGN KEY(project_phase_id)
    REFERENCES project_phase(project_phase_id),
  FOREIGN KEY(phase_criteria_type_id)
    REFERENCES phase_criteria_type_lu(phase_criteria_type_id)
);
CREATE TABLE resource_role_lu (
  resource_role_id              INTEGER                         NOT NULL,
  phase_type_id                 INTEGER,
  name                          VARCHAR(64)                     NOT NULL,
  description                   VARCHAR(254)                    NOT NULL,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(resource_role_id),
  FOREIGN KEY(phase_type_id)
    REFERENCES phase_type_lu(phase_type_id)
);
CREATE TABLE resource (
  resource_id                   INTEGER                         NOT NULL,
  resource_role_id              INTEGER                         NOT NULL,
  project_id                    INTEGER,
  project_phase_id              INTEGER,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(resource_id),
  FOREIGN KEY(project_id)
    REFERENCES project(project_id),
  FOREIGN KEY(resource_role_id)
    REFERENCES resource_role_lu(resource_role_id),
  FOREIGN KEY(project_phase_id)
    REFERENCES project_phase(project_phase_id)
);
CREATE TABLE resource_info_type_lu (
  resource_info_type_id         INTEGER                         NOT NULL,
  name                          VARCHAR(64)                     NOT NULL,
  description                   VARCHAR(254)                    NOT NULL,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(resource_info_type_id)
);
CREATE TABLE resource_info (
  resource_id                   INTEGER                         NOT NULL,
  resource_info_type_id         INTEGER                         NOT NULL,
  value                         LVARCHAR(4096)                  NOT NULL,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(resource_id, resource_info_type_id),
  FOREIGN KEY(resource_info_type_id)
    REFERENCES resource_info_type_lu(resource_info_type_id),
  FOREIGN KEY(resource_id)
    REFERENCES resource(resource_id)
);
CREATE TABLE upload_type_lu (
  upload_type_id                INTEGER                         NOT NULL,
  name                          VARCHAR(64)                     NOT NULL,
  description                   VARCHAR(254)                    NOT NULL,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(upload_type_id)
);
CREATE TABLE upload_status_lu (
  upload_status_id              INTEGER                         NOT NULL,
  name                          VARCHAR(64)                     NOT NULL,
  description                   VARCHAR(254)                    NOT NULL,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(upload_status_id)
);
CREATE TABLE upload (
  upload_id                     INTEGER                         NOT NULL,
  project_id                    INTEGER                         NOT NULL,
  resource_id                   INTEGER                         NOT NULL,
  upload_type_id                INTEGER                         NOT NULL,
  upload_status_id              INTEGER                         NOT NULL,
  parameter                     VARCHAR(254)                    NOT NULL,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(upload_id),
  FOREIGN KEY(upload_type_id)
    REFERENCES upload_type_lu(upload_type_id),
  FOREIGN KEY(upload_status_id)
    REFERENCES upload_status_lu(upload_status_id),
  FOREIGN KEY(resource_id)
    REFERENCES resource(resource_id),
  FOREIGN KEY(project_id)
    REFERENCES project(project_id)
);
CREATE TABLE submission_status_lu (
  submission_status_id          INTEGER                         NOT NULL,
  name                          VARCHAR(64)                     NOT NULL,
  description                   VARCHAR(254)                    NOT NULL,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(submission_status_id)
);
create table submission (
    submission_id INT not null,
    upload_id INT not null,
    submission_status_id INT not null,
    screening_score DECIMAL(5,2),
    initial_score DECIMAL(5,2),
    final_score DECIMAL(5,2),
    placement DECIMAL(3,0),
    create_user VARCHAR(64) not null,
    create_date DATETIME YEAR TO FRACTION not null,
    modify_user VARCHAR(64) not null,
    modify_date DATETIME YEAR TO FRACTION not null
)
extent size 5000 next size 2000
lock mode row;

alter table submission add constraint primary key 
	(submission_id)
	constraint pk_submission;

alter table submission add constraint foreign key 
	(submission_status_id)
	references submission_status_lu
	(submission_status_id) 
	constraint fk_submission_submissionstatuslu_submissionstatusid;

alter table submission add constraint foreign key 
	(upload_id)
	references upload
	(upload_id) 
	constraint fk_submission_upload_uploadid;
CREATE TABLE resource_submission (
  resource_id                   INTEGER                         NOT NULL,
  submission_id                 INTEGER                         NOT NULL,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(resource_id, submission_id),
  FOREIGN KEY(submission_id)
    REFERENCES submission(submission_id),
  FOREIGN KEY(resource_id)
    REFERENCES resource(resource_id)
);
CREATE TABLE comment_type_lu (
  comment_type_id               INTEGER                         NOT NULL,
  name                          VARCHAR(64)                     NOT NULL,
  description                   VARCHAR(254)                    NOT NULL,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(comment_type_id)
);
CREATE TABLE review (
  review_id                     INTEGER                         NOT NULL,
  resource_id                   INTEGER                         NOT NULL,
  submission_id                 INTEGER                         NOT NULL,
  scorecard_id                  INTEGER                         NOT NULL,
  committed                     DECIMAL(1, 0)                   NOT NULL,
  score                         FLOAT,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(review_id),
  FOREIGN KEY(scorecard_id)
    REFERENCES scorecard(scorecard_id),
  FOREIGN KEY(submission_id)
    REFERENCES submission(submission_id),
  FOREIGN KEY(resource_id)
    REFERENCES resource(resource_id)
);
CREATE TABLE review_item (
  review_item_id                INTEGER                         NOT NULL,
  review_id                     INTEGER                         NOT NULL,
  scorecard_question_id         INTEGER                         NOT NULL,
  upload_id                     INTEGER,
  answer                        VARCHAR(254)                    NOT NULL,
  sort                          DECIMAL(3, 0)                   NOT NULL,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(review_item_id),
  FOREIGN KEY(review_id)
    REFERENCES review(review_id),
  FOREIGN KEY(scorecard_question_id)
    REFERENCES scorecard_question(scorecard_question_id),
  FOREIGN KEY(upload_id)
    REFERENCES upload(upload_id)
);
CREATE TABLE review_comment (
  review_comment_id             INTEGER                         NOT NULL,
  resource_id                   INTEGER                         NOT NULL,
  review_id                     INTEGER                         NOT NULL,
  comment_type_id               INTEGER                         NOT NULL,
  content                       LVARCHAR(4096)                  NOT NULL,
  extra_info                    VARCHAR(254),
  sort                          DECIMAL(3, 0)                   NOT NULL,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(review_comment_id),
  FOREIGN KEY(review_id)
    REFERENCES review(review_id),
  FOREIGN KEY(comment_type_id)
    REFERENCES comment_type_lu(comment_type_id),
  FOREIGN KEY(resource_id)
    REFERENCES resource(resource_id)
);
CREATE TABLE review_item_comment (
  review_item_comment_id        INTEGER                         NOT NULL,
  resource_id                   INTEGER                         NOT NULL,
  review_item_id                INTEGER                         NOT NULL,
  comment_type_id               INTEGER                         NOT NULL,
  content                       LVARCHAR(4096)                  NOT NULL,
  extra_info                    VARCHAR(254),
  sort                          DECIMAL(3, 0)                   NOT NULL,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(review_item_comment_id),
  FOREIGN KEY(review_item_id)
    REFERENCES review_item(review_item_id),
  FOREIGN KEY(comment_type_id)
    REFERENCES comment_type_lu(comment_type_id),
  FOREIGN KEY(resource_id)
    REFERENCES resource(resource_id)
);
CREATE TABLE deliverable_lu (
  deliverable_id                INTEGER                         NOT NULL,
  phase_type_id                 INTEGER                         NOT NULL,
  resource_role_id              INTEGER                         NOT NULL,
  name                          VARCHAR(64)                     NOT NULL,
  description                   VARCHAR(64)                     NOT NULL,
  per_submission                DECIMAL(1, 0)                   NOT NULL,
  required                      DECIMAL(1, 0)                   NOT NULL,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(deliverable_id),
  FOREIGN KEY(phase_type_id)
    REFERENCES phase_type_lu(phase_type_id),
  FOREIGN KEY(resource_role_id)
    REFERENCES resource_role_lu(resource_role_id)
);
CREATE TABLE project_audit (
  project_audit_id              INTEGER                         NOT NULL,
  project_id                    INTEGER                         NOT NULL,
  update_reason                 VARCHAR(254)                    NOT NULL,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(project_audit_id),
  FOREIGN KEY(project_id)
    REFERENCES project(project_id)
);
CREATE TABLE notification_type_lu (
  notification_type_id          INTEGER                         NOT NULL,
  name                          VARCHAR(64)                     NOT NULL,
  description                   VARCHAR(254)                    NOT NULL,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(notification_type_id)
);
CREATE TABLE notification (
  project_id                    INTEGER                         NOT NULL,
  external_ref_id               INTEGER                         NOT NULL,
  notification_type_id          INTEGER                         NOT NULL,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(project_id, external_ref_id, notification_type_id),
  FOREIGN KEY(project_id)
    REFERENCES project(project_id),
  FOREIGN KEY(notification_type_id)
    REFERENCES notification_type_lu(notification_type_id)
);


CREATE TABLE screening_status_lu (
  screening_status_id           INTEGER                         NOT NULL,
  name                          VARCHAR(64)                     NOT NULL,
  description                   VARCHAR(254)                    NOT NULL,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(screening_status_id)
);
CREATE TABLE screening_task (
  screening_task_id             INTEGER                         NOT NULL,
  upload_id                     INTEGER                         NOT NULL,
  screening_status_id           INTEGER                         NOT NULL,
  screener_id                   INTEGER,
  start_timestamp               DATETIME YEAR TO FRACTION(3),
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(screening_task_id),
  FOREIGN KEY(upload_id)
    REFERENCES upload(upload_id),
  FOREIGN KEY(screening_status_id)
    REFERENCES screening_status_lu(screening_status_id)
);
CREATE TABLE response_severity_lu (
  response_severity_id          INTEGER                         NOT NULL,
  name                          VARCHAR(64)                     NOT NULL,
  description                   VARCHAR(254)                    NOT NULL,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(response_severity_id)
);
CREATE TABLE screening_response_lu (
  screening_response_id         INTEGER                         NOT NULL,
  response_severity_id          INTEGER                         NOT NULL,
  response_code                 VARCHAR(16)                     NOT NULL,
  response_text                 VARCHAR(254)                    NOT NULL,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(screening_response_id),
  FOREIGN KEY(response_severity_id)
    REFERENCES response_severity_lu(response_severity_id)
);
CREATE TABLE screening_result (
  screening_result_id           INTEGER                         NOT NULL,
  screening_task_id             INTEGER                         NOT NULL,
  screening_response_id         INTEGER                         NOT NULL,
  dynamic_response_text         LVARCHAR(4096)                  NOT NULL,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(screening_result_id),
  FOREIGN KEY(screening_task_id)
    REFERENCES screening_task(screening_task_id),
  FOREIGN KEY(screening_response_id)
    REFERENCES screening_response_lu(screening_response_id)
);


CREATE TABLE id_sequences (
  name                  VARCHAR(255)    NOT NULL,
  next_block_start      INTEGER         NOT NULL,
  block_size            INTEGER         NOT NULL,
  exhausted             INTEGER         NOT NULL,
  PRIMARY KEY (name)
);

close database;