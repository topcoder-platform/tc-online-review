CREATE TABLE project_type_lu (
  project_type_id               INTEGER                         NOT NULL,
  name                          VARCHAR(64)                     NOT NULL,
  description                   VARCHAR(254)                    NOT NULL,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(project_type_id) CONSTRAINT PK_PROJECT_TYPE_LU
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
  PRIMARY KEY(project_category_id) CONSTRAINT PK_PROJECT_CATEGORY_LU,
  FOREIGN KEY(project_type_id)
    REFERENCES project_type_lu(project_type_id) CONSTRAINT FK_PROJECTCATEGORYLU_PROJECTTYPELU_PROJECTTYPEID
);
CREATE TABLE scorecard_type_lu (
  scorecard_type_id             INTEGER                         NOT NULL,
  name                          VARCHAR(64)                     NOT NULL,
  description                   VARCHAR(254)                    NOT NULL,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(scorecard_type_id) CONSTRAINT PK_SCORECARD_TYPE_LU
);
CREATE TABLE scorecard_status_lu (
  scorecard_status_id           INTEGER                         NOT NULL,
  name                          VARCHAR(64)                     NOT NULL,
  description                   VARCHAR(254)                    NOT NULL,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(scorecard_status_id) CONSTRAINT PK_SCORECARD_STATUS_LU
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
  PRIMARY KEY(scorecard_id) CONSTRAINT PK_SCORECARD,
  FOREIGN KEY(scorecard_type_id)
    REFERENCES scorecard_type_lu(scorecard_type_id) CONSTRAINT FK_SCORECARD_SCORECARDTYPELU_SCORECARDTYPEID,
  FOREIGN KEY(project_category_id)
    REFERENCES project_category_lu(project_category_id) CONSTRAINT FK_SCORECARD_PROJECTCATEGORYLU_PROJECTCATEGORYID,
  FOREIGN KEY(scorecard_status_id)
    REFERENCES scorecard_status_lu(scorecard_status_id) CONSTRAINT FK_SCORECARD_SCORECARDSTATUSLU_SCORECARDSTATUSID
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
  PRIMARY KEY(scorecard_group_id) CONSTRAINT PK_SCORECARD_GROUP,
  FOREIGN KEY(scorecard_id)
    REFERENCES scorecard(scorecard_id) CONSTRAINT FK_SCORECARDGROUP_SCORECARD_SCORECARDID
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
  PRIMARY KEY(scorecard_section_id) CONSTRAINT PK_SCORECARD_SECTION,
  FOREIGN KEY(scorecard_group_id)
    REFERENCES scorecard_group(scorecard_group_id) CONSTRAINT FK_SCORECARDSECTION_SCORECARDGROUP_SCORECARDGROUPID
);
CREATE TABLE scorecard_question_type_lu (
  scorecard_question_type_id    INTEGER                         NOT NULL,
  name                          VARCHAR(64)                     NOT NULL,
  description                   VARCHAR(254)                    NOT NULL,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(scorecard_question_type_id) CONSTRAINT PK_SCORECARD_QUESTION_TYPE_LU
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
  PRIMARY KEY(scorecard_question_id) CONSTRAINT PK_SCORECARD_QUESTION,
  FOREIGN KEY(scorecard_section_id)
    REFERENCES scorecard_section(scorecard_section_id) CONSTRAINT FK_SCORECARDQUESTION_SCORECARDSECTION_SCORECARDSECTIONID,
  FOREIGN KEY(scorecard_question_type_id)
    REFERENCES scorecard_question_type_lu(scorecard_question_type_id) CONSTRAINT FK_SCORECARDQUESTION_SCORECARDQUESTIONTYPELU_SCORECARDQUESTIONTYPEID
);
CREATE TABLE project_status_lu (
  project_status_id             INTEGER                         NOT NULL,
  name                          VARCHAR(64)                     NOT NULL,
  description                   VARCHAR(254)                    NOT NULL,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(project_status_id) CONSTRAINT PK_PROJECT_STATUS_LU
);
CREATE TABLE project (
  project_id                    INTEGER                         NOT NULL,
  project_status_id             INTEGER                         NOT NULL,
  project_category_id           INTEGER                         NOT NULL,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(project_id) CONSTRAINT PK_PROJECT,
  FOREIGN KEY(project_category_id)
    REFERENCES project_category_lu(project_category_id) CONSTRAINT FK_PROJECT_PROJECTCATEGORYLU_PROJECTCATEGORYID,
  FOREIGN KEY(project_status_id)
    REFERENCES project_status_lu(project_status_id) CONSTRAINT FK_PROJECT_PROJECTSTATUSLU_PROJECTSTATUSID
);
CREATE TABLE project_info_type_lu (
  project_info_type_id          INTEGER                         NOT NULL,
  name                          VARCHAR(64)                     NOT NULL,
  description                   VARCHAR(25)                     NOT NULL,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(project_info_type_id) CONSTRAINT PK_PROJECT_INFO_TYPE_LU
);
CREATE TABLE project_info (
  project_id                    INTEGER                         NOT NULL,
  project_info_type_id          INTEGER                         NOT NULL,
  value                         LVARCHAR(4096)                  NOT NULL,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(project_id, project_info_type_id) CONSTRAINT PK_PROJECT_INFO,
  FOREIGN KEY(project_info_type_id)
    REFERENCES project_info_type_lu(project_info_type_id) CONSTRAINT FK_PROJECTINFO_PROJECTINFOTYPELU_PROJECTINFOTYPEID,
  FOREIGN KEY(project_id)
    REFERENCES project(project_id) CONSTRAINT FK_PROJECTINFO_PROJECT_PROJECTID
);
CREATE TABLE phase_status_lu (
  phase_status_id               INTEGER                         NOT NULL,
  name                          VARCHAR(64)                     NOT NULL,
  description                   VARCHAR(254)                    NOT NULL,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(phase_status_id) CONSTRAINT PK_PHASE_STATUS_LU
);
CREATE TABLE phase_type_lu (
  phase_type_id                 INTEGER                         NOT NULL,
  name                          VARCHAR(64)                     NOT NULL,
  description                   VARCHAR(254)                    NOT NULL,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(phase_type_id) CONSTRAINT PK_PHASE_TYPE_LU
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
  duration                      DECIMAL(16, 0)                  NOT NULL,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(project_phase_id) CONSTRAINT PK_PROJECT_PHASE,
  FOREIGN KEY(phase_type_id)
    REFERENCES phase_type_lu(phase_type_id) CONSTRAINT FK_PROJECTPHASE_PHASETYPELU_PHASETYPEID,
  FOREIGN KEY(project_id)
    REFERENCES project(project_id) CONSTRAINT FK_PROJECTPHASE_PROJECT_PROJECTID,
  FOREIGN KEY(phase_status_id)
    REFERENCES phase_status_lu(phase_status_id) CONSTRAINT FK_PROJECTPHASE_PHASESTATUSLU_PHASESTATUSID
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
  PRIMARY KEY(dependency_phase_id, dependent_phase_id) CONSTRAINT PK_PHASE_DEPENDENCY,
  FOREIGN KEY(dependency_phase_id)
    REFERENCES project_phase(project_phase_id) CONSTRAINT FK_PHASEDEPENDENCY_PROJECTPHASE_DEPENDENCYPHASEID,
  FOREIGN KEY(dependent_phase_id)
    REFERENCES project_phase(project_phase_id) CONSTRAINT FK_PHASEDEPENDENCY_PROJECTPHASE_DEPENDENTPHASEID
);
CREATE TABLE phase_criteria_type_lu (
  phase_criteria_type_id        INTEGER                         NOT NULL,
  name                          VARCHAR(64)                     NOT NULL,
  description                   VARCHAR(254)                    NOT NULL,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(phase_criteria_type_id) CONSTRAINT PK_PHASE_CRITERIA_TYPE_LU
);
CREATE TABLE phase_criteria (
  project_phase_id              INTEGER                         NOT NULL,
  phase_criteria_type_id        INTEGER                         NOT NULL,
  parameter                     VARCHAR(254)                    NOT NULL,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(project_phase_id, phase_criteria_type_id) CONSTRAINT PK_PHASE_CRITERIA,
  FOREIGN KEY(project_phase_id)
    REFERENCES project_phase(project_phase_id) CONSTRAINT FK_PHASECRITERIA_PROJECTPHASE_PROJECTPHASEID,
  FOREIGN KEY(phase_criteria_type_id)
    REFERENCES phase_criteria_type_lu(phase_criteria_type_id) CONSTRAINT FK_PHASECRITERIA_PHASECRITERIATYPELU_PHASECRITERIATYPEID
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
  PRIMARY KEY(resource_role_id) CONSTRAINT PK_RESOURCE_ROLE_LU,
  FOREIGN KEY(phase_type_id)
    REFERENCES phase_type_lu(phase_type_id) CONSTRAINT FK_RESOURCEROLELU_PHASETYPELU_PHASETYPEID
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
  PRIMARY KEY(resource_id) CONSTRAINT PK_RESOURCE,
  FOREIGN KEY(project_id)
    REFERENCES project(project_id) CONSTRAINT FK_RESOURCE_PROJECT_PROJECTID,
  FOREIGN KEY(resource_role_id)
    REFERENCES resource_role_lu(resource_role_id) CONSTRAINT FK_RESOURCE_RESOURCEROLELU_RESOURCEROLEID,
  FOREIGN KEY(project_phase_id)
    REFERENCES project_phase(project_phase_id) CONSTRAINT FK_RESOURCE_PROJECTPHASE_PROJECTPHASEID
);
CREATE TABLE resource_info_type_lu (
  resource_info_type_id         INTEGER                         NOT NULL,
  name                          VARCHAR(64)                     NOT NULL,
  description                   VARCHAR(254)                    NOT NULL,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(resource_info_type_id) CONSTRAINT PK_RESOURCE_INFO_TYPE_LU
);
CREATE TABLE resource_info (
  resource_id                   INTEGER                         NOT NULL,
  resource_info_type_id         INTEGER                         NOT NULL,
  value                         LVARCHAR(4096)                  NOT NULL,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(resource_id, resource_info_type_id) CONSTRAINT PK_RESOURCE_INFO,
  FOREIGN KEY(resource_info_type_id)
    REFERENCES resource_info_type_lu(resource_info_type_id) CONSTRAINT FK_RESOURCEINFO_RESOURCEINFOTYPELU_RESOURCEINFOTYPEID,
  FOREIGN KEY(resource_id)
    REFERENCES resource(resource_id) CONSTRAINT FK_RESOURCEINFO_RESOURCE_RESOURCEID
);
CREATE TABLE upload_type_lu (
  upload_type_id                INTEGER                         NOT NULL,
  name                          VARCHAR(64)                     NOT NULL,
  description                   VARCHAR(254)                    NOT NULL,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(upload_type_id) CONSTRAINT PK_UPLOAD_TYPE_LU
);
CREATE TABLE upload_status_lu (
  upload_status_id              INTEGER                         NOT NULL,
  name                          VARCHAR(64)                     NOT NULL,
  description                   VARCHAR(254)                    NOT NULL,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(upload_status_id) CONSTRAINT PK_UPLOAD_STATUS_LU
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
  PRIMARY KEY(upload_id) CONSTRAINT PK_UPLOAD,
  FOREIGN KEY(upload_type_id)
    REFERENCES upload_type_lu(upload_type_id) CONSTRAINT FK_UPLOAD_UPLOADTYPELU_UPLOADTYPEID,
  FOREIGN KEY(upload_status_id)
    REFERENCES upload_status_lu(upload_status_id) CONSTRAINT FK_UPLOAD_UPLOADSTATUSLU_UPLOADSTATUSID,
  FOREIGN KEY(resource_id)
    REFERENCES resource(resource_id) CONSTRAINT FK_UPLOAD_RESOURCE_RESOURCEID,
  FOREIGN KEY(project_id)
    REFERENCES project(project_id) CONSTRAINT FK_UPLOAD_PROJECT_PROJECTID
);
CREATE TABLE submission_status_lu (
  submission_status_id          INTEGER                         NOT NULL,
  name                          VARCHAR(64)                     NOT NULL,
  description                   VARCHAR(254)                    NOT NULL,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(submission_status_id) CONSTRAINT PK_SUBMISSION_STATUS_LU
);
CREATE TABLE submission (
  submission_id                 INTEGER                         NOT NULL,
  upload_id                     INTEGER                         NOT NULL,
  submission_status_id          INTEGER                         NOT NULL,
  screening_score               DECIMAL(5,2),
  initial_score                 DECIMAL(5,2),
  final_score                   DECIMAL(5,2),
  placement                     DECIMAL(3,0),
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(submission_id) CONSTRAINT PK_SUBMISSION,
  FOREIGN KEY(submission_status_id)
    REFERENCES submission_status_lu(submission_status_id) CONSTRAINT FK_SUBMISSION_SUBMISSIONSTATUSLU_SUBMISSIONSTATUSID,
  FOREIGN KEY(upload_id)
    REFERENCES upload(upload_id) CONSTRAINT FK_SUBMISSION_UPLOAD_UPLOADID
);
CREATE TABLE resource_submission (
  resource_id                   INTEGER                         NOT NULL,
  submission_id                 INTEGER                         NOT NULL,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(resource_id, submission_id) CONSTRAINT PK_RESOURCE_SUBMISSION,
  FOREIGN KEY(submission_id)
    REFERENCES submission(submission_id) CONSTRAINT FK_RESOURCESUBMISSION_SUBMISSION_SUBMISSIONID,
  FOREIGN KEY(resource_id)
    REFERENCES resource(resource_id) CONSTRAINT FK_RESOURCESUBMISSION_RESOURCE_RESOURCEID
);
CREATE TABLE comment_type_lu (
  comment_type_id               INTEGER                         NOT NULL,
  name                          VARCHAR(64)                     NOT NULL,
  description                   VARCHAR(254)                    NOT NULL,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(comment_type_id) CONSTRAINT PK_COMMENT_TYPE_LU
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
  PRIMARY KEY(review_id) CONSTRAINT PK_REVIEW,
  FOREIGN KEY(scorecard_id)
    REFERENCES scorecard(scorecard_id) CONSTRAINT FK_REVIEW_SCORECARD_SCORECARDID,
  FOREIGN KEY(submission_id)
    REFERENCES submission(submission_id) CONSTRAINT FK_REVIEW_SUBMISSION_SUBMISSIONID,
  FOREIGN KEY(resource_id)
    REFERENCES resource(resource_id) CONSTRAINT FK_REVIEW_RESOURCE_RESOURCEID
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
  PRIMARY KEY(review_item_id) CONSTRAINT PK_REVIEW_ITEM,
  FOREIGN KEY(review_id)
    REFERENCES review(review_id) CONSTRAINT FK_REVIEWITEM_REVIEW_REVIEWID,
  FOREIGN KEY(scorecard_question_id)
    REFERENCES scorecard_question(scorecard_question_id) CONSTRAINT FK_REVIEWITEM_SCORECARDQUESTION_SCORECARDQUESTIONID,
  FOREIGN KEY(upload_id)
    REFERENCES upload(upload_id) CONSTRAINT FK_REVIEWITEM_UPLOAD_UPLOADID
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
  PRIMARY KEY(review_comment_id) CONSTRAINT PK_REVIEW_COMMENT,
  FOREIGN KEY(review_id)
    REFERENCES review(review_id) CONSTRAINT FK_REVIEWCOMMENT_REVIEW_REVIEWID,
  FOREIGN KEY(comment_type_id)
    REFERENCES comment_type_lu(comment_type_id) CONSTRAINT FK_REVIEWCOMMENT_COMMENTTYPELU_COMMENTTYPEID,
  FOREIGN KEY(resource_id)
    REFERENCES resource(resource_id) CONSTRAINT FK_REVIEWCOMMENT_RESOURCE_RESOURCEID
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
  PRIMARY KEY(review_item_comment_id) CONSTRAINT PK_REVIEW_ITEM_COMMENT,
  FOREIGN KEY(review_item_id)
    REFERENCES review_item(review_item_id) CONSTRAINT FK_REVIEWITEMCOMMENT_REVIEWITEM_REVIEWITEMID,
  FOREIGN KEY(comment_type_id)
    REFERENCES comment_type_lu(comment_type_id) CONSTRAINT FK_REVIEWITEMCOMMENT_COMMENTTYPELU_COMMENTTYPEID,
  FOREIGN KEY(resource_id)
    REFERENCES resource(resource_id) CONSTRAINT FK_REVIEWITEMCOMMENT_RESOURCE_RESOURCEID
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
  PRIMARY KEY(deliverable_id) CONSTRAINT PK_DELIVERABLE_LU,
  FOREIGN KEY(phase_type_id)
    REFERENCES phase_type_lu(phase_type_id) CONSTRAINT FK_DELIVERABLELU_PHASETYPELU_PHASETYPEID,
  FOREIGN KEY(resource_role_id)
    REFERENCES resource_role_lu(resource_role_id) CONSTRAINT FK_DELIVERABLELU_RESOURCEROLELU_RESOURCEROLEID
);
CREATE TABLE project_audit (
  project_audit_id              INTEGER                         NOT NULL,
  project_id                    INTEGER                         NOT NULL,
  update_reason                 VARCHAR(254)                    NOT NULL,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(project_audit_id) CONSTRAINT PK_PROJECT_AUDIT,
  FOREIGN KEY(project_id)
    REFERENCES project(project_id) CONSTRAINT FK_PROJECTAUDIT_PROJECT_PROJECTID
);
CREATE TABLE notification_type_lu (
  notification_type_id          INTEGER                         NOT NULL,
  name                          VARCHAR(64)                     NOT NULL,
  description                   VARCHAR(254)                    NOT NULL,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(notification_type_id) CONSTRAINT PK_NOTIFICATION_TYPE_LU
);
CREATE TABLE notification (
  project_id                    INTEGER                         NOT NULL,
  external_ref_id               INTEGER                         NOT NULL,
  notification_type_id          INTEGER                         NOT NULL,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(project_id, external_ref_id, notification_type_id) CONSTRAINT PK_NOTIFICATION,
  FOREIGN KEY(project_id)
    REFERENCES project(project_id) CONSTRAINT FK_NOTIFICATION_PROJECT_PROJECTID,
  FOREIGN KEY(notification_type_id)
    REFERENCES notification_type_lu(notification_type_id) CONSTRAINT FK_NOTIFICATION_NOTIFICATIONTYPELU_NOTIFICATIONTYPEID
);


CREATE TABLE screening_status_lu (
  screening_status_id           INTEGER                         NOT NULL,
  name                          VARCHAR(64)                     NOT NULL,
  description                   VARCHAR(254)                    NOT NULL,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(screening_status_id) CONSTRAINT PK_SCREENING_STATUS_LU
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
  PRIMARY KEY(screening_task_id) CONSTRAINT PK_SCREENING_TASK,
  FOREIGN KEY(upload_id)
    REFERENCES upload(upload_id) CONSTRAINT FK_SCREENINGTASK_UPLOAD_UPLOADID,
  FOREIGN KEY(screening_status_id)
    REFERENCES screening_status_lu(screening_status_id) CONSTRAINT FK_SCREENINGTASK_SCREENINGSTATUSLU_SCREENINGSTATUSID
);
CREATE TABLE response_severity_lu (
  response_severity_id          INTEGER                         NOT NULL,
  name                          VARCHAR(64)                     NOT NULL,
  description                   VARCHAR(254)                    NOT NULL,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(response_severity_id) CONSTRAINT PK_RESPONSE_SEVERITY_LU
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
  PRIMARY KEY(screening_response_id) CONSTRAINT PK_SCREENING_RESPONSE_LU,
  FOREIGN KEY(response_severity_id)
    REFERENCES response_severity_lu(response_severity_id) CONSTRAINT FK_SCREENINGRESPONSELU_RESPONSESEVERITYLU_RESPONSESEVERITYID
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
  PRIMARY KEY(screening_result_id) CONSTRAINT PK_SCREENING_RESULT,
  FOREIGN KEY(screening_task_id)
    REFERENCES screening_task(screening_task_id) CONSTRAINT FK_SCREENINGRESULT_SCREENINGTASK_SCREENINGTASKID,
  FOREIGN KEY(screening_response_id)
    REFERENCES screening_response_lu(screening_response_id) CONSTRAINT FK_SCREENINGRESULT_SCREEINGRESPONSELU_SCREENINGRESPONSEID
);
CREATE TABLE default_scorecard (
  project_category_id           INTEGER                         NOT NULL,
  scorecard_type_id             INTEGER                         NOT NULL,
  scorecard_id                  INTEGER                         NOT NULL,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(project_category_id, scorecard_type_id) CONSTRAINT PK_DEFAULT_SCORECARD,
  FOREIGN KEY(project_category_id)
    REFERENCES project_category_lu(project_category_id) CONSTRAINT FK_DEFAULTSCORECARD_PROJECTCATEGORYLU_PROJECTCATEGORYID,
  FOREIGN KEY(scorecard_type_id)
    REFERENCES scorecard_type_lu(scorecard_type_id) CONSTRAINT FK_DEFAULTSCORECARD_SCORECARDTYPELU_SCORECARDTYPEID,
  FOREIGN KEY(scorecard_id)
    REFERENCES scorecard(scorecard_id) CONSTRAINT FK_DEFAULTSCORECARD_SCORECARD_SCORECARDID
);
CREATE TABLE project_result
(
   user_id                      DECIMAL(10),
   project_id                   DECIMAL(10),
   old_rating                   DECIMAL(5),
   new_rating                   DECIMAL(5),
   old_reliability              DECIMAL(5, 4),
   new_reliability              DECIMAL(5, 4),
   raw_score                    DECIMAL(5, 2),
   final_score                  DECIMAL(5, 2),
   payment                      DECIMAL(10, 2),
   placed                       DECIMAL(6),
   rating_ind                   DECIMAL(1),
   valid_submission_ind         DECIMAL(1),
   reliability_ind              DECIMAL(1),
   create_date                  DATETIME YEAR TO FRACTION(3),
   modify_date                  DATETIME YEAR TO FRACTION(3),
   reliable_submission_ind      DECIMAL(1),
   passed_review_ind            DECIMAL(1),
   point_adjustment             DECIMAL(5),
   current_reliability_ind      DECIMAL(1),
   PRIMARY KEY(user_id, project_id) CONSTRAINT PK_PROJECT_RESULT
);
CREATE INDEX proj_result_idx1 ON project_result(project_id);
