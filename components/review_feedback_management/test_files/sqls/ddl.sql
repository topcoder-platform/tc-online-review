create table 'informix'.project (
    project_id INT not null,
    project_status_id INT not null,
    project_category_id INT not null,
    project_studio_spec_id INTEGER,
    create_user VARCHAR(64) not null,
    create_date DATETIME YEAR TO FRACTION not null,
    modify_user VARCHAR(64) not null,
    modify_date DATETIME YEAR TO FRACTION not null,
    tc_direct_project_id INT
)
extent size 1000 next size 1000
lock mode row;

alter table 'informix'.project add constraint primary key
    (project_id)
    constraint pk_project;

CREATE TABLE "informix".review_feedback (
    review_feedback_id serial NOT NULL,
    project_id INT NOT NULL,
    comment lvarchar(4096),
    create_user VARCHAR(64) NOT NULL,
    create_date DATETIME YEAR TO FRACTION NOT NULL,
    modify_user VARCHAR(64) NOT NULL,
    modify_date DATETIME YEAR TO FRACTION NOT NULL
)
extent size 3000 next size 3000
lock mode row;

alter table 'informix'.review_feedback add constraint primary key
  (review_feedback_id)
  constraint pk_review_feedback;

alter table 'informix'.review_feedback add constraint foreign key
    (project_id)
    references 'informix'.project
    (project_id)
    constraint fk_reviewfeedback_project_projectid;

CREATE TABLE "informix".review_feedback_detail (
    review_feedback_id INT NOT NULL,
    reviewer_user_id DECIMAL(10,0) NOT NULL,
    score INTEGER NOT NULL,
    feedback_text lvarchar(4096) NOT NULL
)
extent size 3000 next size 3000
lock mode row;

alter table 'informix'.review_feedback_detail add constraint primary key
  (review_feedback_id, reviewer_user_id)
  constraint pk_review_feedback_detail;

alter table 'informix'.review_feedback_detail add constraint foreign key
    (review_feedback_id)
    references 'informix'.review_feedback
    (review_feedback_id)
    constraint fk_reviewfeedbackdetail_reviewfeedback_reviewfeedbackid;

create table 'informix'.audit_action_type_lu (
    audit_action_type_id INT not null,
    name VARCHAR(50) not null,
    description VARCHAR(50) not null,
    create_user VARCHAR(64) not null,
    create_date DATETIME YEAR TO FRACTION default CURRENT YEAR TO FRACTION not null,
    modify_user VARCHAR(64) not null,
    modify_date DATETIME YEAR TO FRACTION default CURRENT YEAR TO FRACTION not null
)
extent size 64 next size 64
lock mode row;

alter table 'informix'.audit_action_type_lu add constraint primary key
    (audit_action_type_id)
    constraint audit_action_type_lu_pkey;

CREATE TABLE "informix".review_feedback_audit (
    review_feedback_id INT NOT NULL,
    project_id INT NOT NULL,
    comment lvarchar(4096),
    audit_action_type_id INT NOT NULL,
    action_user VARCHAR(64) NOT NULL,
    action_date DATETIME YEAR TO FRACTION NOT NULL
)
extent size 3000 next size 3000
lock mode row;

alter table 'informix'.review_feedback_audit add constraint foreign key
    (review_feedback_id)
    references 'informix'.review_feedback
    (review_feedback_id)
    constraint fk_reviewfeedbackaudit_reviewfeedback_reviewfeedbackid;

alter table 'informix'.review_feedback_audit add constraint foreign key
    (audit_action_type_id)
    references 'informix'.audit_action_type_lu
    (audit_action_type_id)
    constraint fk_reviewfeedbackaudit_auditactiontypelu_auditactiontypeid;

CREATE TABLE "informix".review_feedback_detail_audit (
    review_feedback_id INT NOT NULL,
    reviewer_user_id DECIMAL(10,0) NOT NULL,
    score INTEGER,
    feedback_text lvarchar(4096),
    audit_action_type_id INT NOT NULL,
    action_user VARCHAR(64) NOT NULL,
    action_date DATETIME YEAR TO FRACTION NOT NULL
)
extent size 3000 next size 3000
lock mode row;

alter table 'informix'.review_feedback_detail_audit add constraint foreign key
    (review_feedback_id)
    references 'informix'.review_feedback
    (review_feedback_id)
    constraint fk_reviewfeedbackdetailaudit_reviewfeedback_reviewfeedbackid;

alter table 'informix'.review_feedback_detail_audit add constraint foreign key
    (audit_action_type_id)
    references 'informix'.audit_action_type_lu
    (audit_action_type_id)
    constraint fk_reviewfeedbackdetailaudit_auditactiontypelu_auditactiontypeid;

