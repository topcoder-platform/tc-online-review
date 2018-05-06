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
CREATE TABLE submission_type_lu (
  submission_type_id          INTEGER                         NOT NULL,
  name                          VARCHAR(64)                     NOT NULL,
  description                   VARCHAR(254)                    NOT NULL,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(submission_type_id)
);
create table submission (
    submission_id INT not null,
    upload_id INT not null,
    submission_status_id INT not null,
    submission_type_id INT not null,
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
	(submission_type_id)
	references submission_type_lu
	(submission_type_id) 
	constraint fk_submission_submissiontypelu_submissiontypeid;

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
  submission_id                 INTEGER,
  scorecard_id                  INTEGER                         NOT NULL,
  committed                     DECIMAL(1, 0)                   NOT NULL,
  score                         FLOAT,
  initial_score  								FLOAT,
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
  required                      DECIMAL(1, 0)                   NOT NULL,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  submission_type_id       INTEGER,
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

create table email (
    user_id DECIMAL(10,0),
    email_id DECIMAL(10,0),
    email_type_id DECIMAL(5,0),
    address VARCHAR(100),
    create_date DATETIME YEAR TO FRACTION default CURRENT YEAR TO FRACTION,
    modify_date DATETIME YEAR TO FRACTION default CURRENT YEAR TO FRACTION,
    primary_ind DECIMAL(1,0),
    status_id DECIMAL(3,0)
)
extent size 7500 next size 2750
lock mode row;

create index email_user_id_idx on email
	(
	user_id, 
	primary_ind
	);

alter table email add constraint primary key 
	(email_id)
	constraint u110_23;



create table user (
    user_id DECIMAL(10,0) not null,
    first_name VARCHAR(64),
    last_name VARCHAR(64),
    create_date DATETIME YEAR TO FRACTION default CURRENT YEAR TO FRACTION,
    modify_date DATETIME YEAR TO FRACTION default CURRENT YEAR TO FRACTION,
    handle VARCHAR(50) not null,
    last_login DATETIME YEAR TO FRACTION,
    status VARCHAR(3) not null,
    password VARCHAR(15),
    activation_code VARCHAR(32),
    middle_name VARCHAR(64),
    handle_lower VARCHAR(50),
    timezone_id DECIMAL(5,0)
)
extent size 10000 next size 5000
lock mode row;

create index user_handle_idx on user
	(
	handle
	);

create index user_lower_handle_idx on user
	(
	handle_lower
	);

alter table user add constraint primary key 
	(user_id)
	constraint u124_45;




create table user_rating (
    user_id DECIMAL(10) not null,
    rating DECIMAL(10) default 0 not null,
    phase_id DECIMAL(3) not null,
    vol DECIMAL(10) default 0 not null,
    rating_no_vol DECIMAL(10) default 0 not null,
    num_ratings DECIMAL(5) default 0 not null,
    mod_date_time DATETIME YEAR TO FRACTION default CURRENT YEAR TO
    FRACTION not null,
    create_date_time DATETIME YEAR TO FRACTION default CURRENT YEAR TO
    FRACTION not null,
    last_rated_project_id DECIMAL(12,0)
);

alter table user_rating add constraint primary key 
	(user_id, phase_id)
	constraint pk_user_rating;



create table user_reliability (
    user_id DECIMAL(10,0),
    rating DECIMAL(5,4),
    modify_date DATETIME YEAR TO FRACTION default CURRENT YEAR TO FRACTION,
    create_date DATETIME YEAR TO FRACTION default CURRENT YEAR TO FRACTION,
    phase_id DECIMAL(12,0)
)
extent size 32 next size 32
lock mode row;

alter table user_reliability add constraint primary key 
	(user_id, phase_id)
	constraint user_reliability_pkey;





create table comp_catalog (
    component_id DECIMAL(12,0) not null,
    current_version DECIMAL(12,0) not null,
    short_desc lvarchar,
    component_name VARCHAR(254) not null,
    description lvarchar,
    function_desc lvarchar,
    create_time DATETIME YEAR TO FRACTION not null,
    status_id DECIMAL(12,0) not null,
    root_category_id DECIMAL(12,0),
    modify_date DATETIME YEAR TO FRACTION default CURRENT YEAR TO FRACTION 
    not null
);

alter table comp_catalog add constraint primary key 
	(component_id)
	constraint pk_comp_catalog;





create table categories (
    category_id DECIMAL(12,0) not null,
    parent_category_id DECIMAL(12,0),
    category_name VARCHAR(100) not null,
    description VARCHAR(254) not null,
    status_id DECIMAL(12,0) not null,
    viewable DECIMAL(1,0) default 1
)
extent size 16 next size 16
lock mode row;

alter table categories add constraint primary key 
	(category_id)
	constraint pk_categories;



create table comp_versions (
    comp_vers_id DECIMAL(12,0) not null,
    component_id DECIMAL(12,0),
    version DECIMAL(12,0) not null,
    version_text CHAR(20) not null,
    create_time DATETIME YEAR TO FRACTION not null,
    phase_id DECIMAL(12,0) not null,
    phase_time DATETIME YEAR TO FRACTION not null,
    price DECIMAL(10,2) not null,
    comments lvarchar,
    modify_date DATETIME YEAR TO FRACTION default CURRENT YEAR TO
    FRACTION not null
)
extent size 500 next size 124
lock mode row;

create unique cluster index comp_versions_i2 on
comp_versions
	(
	component_id, 
	version
	);

alter table comp_versions add constraint primary key 
	(comp_vers_id)
	constraint pk_comp_versions;

alter table comp_versions add constraint foreign key 
	(component_id)
	references comp_catalog
	(component_id) 
	constraint fk_comp_versions;





create table comp_forum_xref (
    comp_forum_id DECIMAL(12,0) not null,
    comp_vers_id DECIMAL(12,0),
    forum_id DECIMAL(12,0),
    forum_type DECIMAL(5) not null
)
extent size 250 next size 124
lock mode row;

create unique cluster index comp_forum_xref_i1 on
comp_forum_xref
	(
	comp_vers_id, 
	forum_id
	);

alter table comp_forum_xref add constraint primary key 
	(comp_forum_id)
	constraint pk_comp_forum_xref;

alter table comp_forum_xref add constraint foreign key 
	(comp_vers_id)
	references comp_versions
	(comp_vers_id) 
	constraint fk_comp_forum2;

delete from id_sequences;


-- create new lookup table for audit action types
create table audit_action_type_lu (
    audit_action_type_id INT not null,
    name VARCHAR(50) not null,
    description VARCHAR(50) not null,
    create_user VARCHAR(64) not null,
    create_date DATETIME YEAR TO FRACTION default CURRENT YEAR TO FRACTION not null,
    modify_user VARCHAR(64) not null,
    modify_date DATETIME YEAR TO FRACTION default CURRENT YEAR TO FRACTION not null
);

alter table audit_action_type_lu add constraint primary key 
	(audit_action_type_id)
	constraint audit_action_type_lu_pkey;


-- create audit table for project resources
create table project_user_audit  (
    project_user_audit_id DECIMAL(12,0) not null,
    project_id INT not null,
    resource_user_id DECIMAL(12,0) not null,
    resource_role_id INT not null,
    audit_action_type_id INT not null,
    action_date DATETIME YEAR TO FRACTION not null,
    action_user_id DECIMAL(12,0) not null
)
extent size 16 next size 16
lock mode row; 

alter table project_user_audit add constraint primary key 
	(project_user_audit_id)
	constraint project_user_audit_pkey;

-- enforce Referential Integrity.
alter table project_user_audit add constraint foreign key 
	(audit_action_type_id)
	references audit_action_type_lu
	(audit_action_type_id) 
	constraint project_user_audit_audit_action_type_lu_fk;

alter table project_user_audit add constraint foreign key 
	(project_id)
	references project
	(project_id) 
	constraint project_user_audit_project_fk;

alter table project_user_audit add constraint foreign key 
	(resource_role_id)
	references resource_role_lu
	(resource_role_id) 
	constraint project_user_audit_resource_role_lu_fk;

-- create sequence for project_user_audit
CREATE SEQUENCE PROJECT_USER_AUDIT_SEQ;


create table project_info_audit (
project_id int not null,
project_info_type_id int not null,
value varchar(255),
audit_action_type_id int not null,
action_date datetime year to fraction not null,
action_user_id decimal(12,0) not null
);

alter table project_info_audit add constraint foreign key 
(audit_action_type_id)
references audit_action_type_lu
(audit_action_type_id) 
constraint project_info_audit_audit_action_type_lu_fk;

alter table project_info_audit add constraint foreign key 
(project_id)
references project
(project_id) 
constraint project_info_audit_project_fk;

alter table project_info_audit add constraint foreign key 
(project_info_type_id)
references project_info_type_lu
(project_info_type_id) 
constraint project_info_audit_project_info_type_lu_fk;

------------------------------------------------------------------------------
create table project_phase_audit (
project_phase_id int not null,
scheduled_start_time datetime year to fraction,
scheduled_end_time datetime year to fraction,
audit_action_type_id int not null,
action_date datetime year to fraction not null,
action_user_id decimal(12,0) not null
);

alter table project_phase_audit add constraint foreign key 
(audit_action_type_id)
references audit_action_type_lu
(audit_action_type_id) 
constraint project_phase_audit_audit_action_type_lu_fk;

alter table project_phase_audit add constraint foreign key 
(project_phase_id)
references project_phase
(project_phase_id) 
constraint project_phase_audit_project_phase_fk;

-- Generic project type and category
ALTER TABLE project_type_lu ADD is_generic BOOLEAN DEFAULT 'f' NOT NULL;



create table link_type_lu (
link_type_id INT not null,
link_type_name VARCHAR(64) not null,
allow_overlap DECIMAL(1)
);


create table linked_project_xref (
source_project_id INT,
dest_project_id INT,
link_type_id INT
);

CREATE TABLE late_deliverable (
    late_deliverable_id serial NOT NULL,
    late_deliverable_type_id INTEGER NOT NULL,
    project_phase_id INTEGER NOT NULL,
    resource_id INTEGER NOT NULL,
    deliverable_id INTEGER NOT NULL,
    deadline DATETIME YEAR TO FRACTION,
    compensated_deadline DATETIME YEAR TO FRACTION,
    create_date DATETIME YEAR TO FRACTION NOT NULL,
    forgive_ind DECIMAL(1,0) NOT NULL,
    last_notified DATETIME YEAR TO FRACTION,
    delay DECIMAL(16,0),
    explanation lvarchar(4096),
    explanation_date DATETIME YEAR TO FRACTION,
    response lvarchar(4096),
    response_user VARCHAR(64),
    response_date DATETIME YEAR TO FRACTION
);

create table phase (
    phase_id DECIMAL(12,0) not null,
    description VARCHAR(254) not null
);

create table roles (
    role_id DECIMAL(12,0) not null,
    role_name VARCHAR(100) not null,
    description VARCHAR(254) not null,
    status_id DECIMAL(12,0) not null
);


CREATE TABLE late_deliverable_type_lu (
    late_deliverable_type_id INT NOT NULL,
    name VARCHAR(64) NOT NULL,
    description VARCHAR(254) NOT NULL
);

alter table late_deliverable_type_lu add constraint primary key
  (late_deliverable_type_id)
  constraint pk_late_deliverable_type_lu;