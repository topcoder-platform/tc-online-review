------ upload_type_lu
create table 'informix'.upload_type_lu (
    upload_type_id INT not null,
    name VARCHAR(64) not null,
    description VARCHAR(254) not null,
    create_user VARCHAR(64) not null,
    create_date DATETIME YEAR TO FRACTION not null,
    modify_user VARCHAR(64) not null,
    modify_date DATETIME YEAR TO FRACTION not null
)
extent size 64 next size 64
lock mode row;

revoke all on upload_type_lu from 'public';
grant select,insert,update,delete on upload_type_lu to public as 'informix';


------ upload_status_lu
create table 'informix'.upload_status_lu (
    upload_status_id INT not null,
    name VARCHAR(64) not null,
    description VARCHAR(254) not null,
    create_user VARCHAR(64) not null,
    create_date DATETIME YEAR TO FRACTION not null,
    modify_user VARCHAR(64) not null,
    modify_date DATETIME YEAR TO FRACTION not null
)
extent size 64 next size 64
lock mode row;

revoke all on upload_status_lu from 'public';
grant select,insert,update,delete on upload_status_lu to public as 'informix';


------ upload
create table 'informix'.upload (
    upload_id INT not null,
    project_id INT not null,
    project_phase_id INT,
    resource_id INT not null,
    upload_type_id INT not null,
    upload_status_id INT not null,
    parameter VARCHAR(254) not null,
    upload_desc VARCHAR(254),
    create_user VARCHAR(64) not null,
    create_date DATETIME YEAR TO FRACTION not null,
    modify_user VARCHAR(64) not null,
    modify_date DATETIME YEAR TO FRACTION not null
)
extent size 20000 next size 10000
lock mode row;

revoke all on upload from 'public';
grant select,insert,update,delete on upload to public as 'informix';


------ phase_status_lu
create table 'informix'.phase_status_lu (
    phase_status_id INT not null,
    name VARCHAR(64) not null,
    description VARCHAR(254) not null,
    create_user VARCHAR(64) not null,
    create_date DATETIME YEAR TO FRACTION not null,
    modify_user VARCHAR(64) not null,
    modify_date DATETIME YEAR TO FRACTION not null
)
extent size 64 next size 64
lock mode row;

revoke all on phase_status_lu from 'public';
grant select,insert,update,delete on phase_status_lu to public as 'informix';

------ project_phase
create table 'informix'.project_phase (
    project_phase_id INT not null,
    project_id INT not null,
    phase_type_id INT not null,
    phase_status_id INT not null,
    fixed_start_time DATETIME YEAR TO FRACTION,
    scheduled_start_time DATETIME YEAR TO FRACTION not null,
    scheduled_end_time DATETIME YEAR TO FRACTION not null,
    actual_start_time DATETIME YEAR TO FRACTION,
    actual_end_time DATETIME YEAR TO FRACTION,
    duration DECIMAL(16,0) not null,
    create_user VARCHAR(64) not null,
    create_date DATETIME YEAR TO FRACTION not null,
    modify_user VARCHAR(64) not null,
    modify_date DATETIME YEAR TO FRACTION not null
)
extent size 20000 next size 10000
lock mode row;

revoke all on project_phase from 'public';
grant select,insert,update,delete on project_phase to public as 'informix';



------ prize_type_lu
create table 'informix'.prize_type_lu (
    prize_type_id DECIMAL(5,0),
    prize_type_desc VARCHAR(64)
)
extent size 16 next size 16
lock mode row;

revoke all on prize_type_lu from 'public';
grant select,insert,update,delete on prize_type_lu to public as 'informix';


------ prize
create table 'informix'.prize (
  prize_id INTEGER not null,
  project_id INT,
  place INTEGER not null,
  prize_amount FLOAT not null,
  prize_type_id DECIMAL(5,0) not null,
  number_of_submissions INTEGER not null,
  create_user VARCHAR(64) not null,
  create_date DATETIME YEAR TO FRACTION(3) not null,
  modify_user VARCHAR(64) not null,
  modify_date DATETIME YEAR TO FRACTION(3) not null
)
extent size 16 next size 16
lock mode row;
revoke all on prize from 'public';
grant select,insert,update,delete on prize to public as 'informix';

------ submission_status_lu
create table 'informix'.submission_status_lu (
    submission_status_id INT not null,
    name VARCHAR(64) not null,
    description VARCHAR(254) not null,
    create_user VARCHAR(64) not null,
    create_date DATETIME YEAR TO FRACTION not null,
    modify_user VARCHAR(64) not null,
    modify_date DATETIME YEAR TO FRACTION not null
)
extent size 64 next size 64
lock mode row;

revoke all on submission_status_lu from 'public';
grant select,insert,update,delete on submission_status_lu to public as 'informix';

------ submission_type_lu
CREATE TABLE 'informix'.submission_type_lu (
    submission_type_id            INTEGER                         not null,
    name                          VARCHAR(64)                     not null,
    description                   VARCHAR(254)                    not null,
    create_user                   VARCHAR(64)                     not null,
    create_date                   DATETIME YEAR TO FRACTION       not null,
    modify_user                   VARCHAR(64)                     not null,
    modify_date                   DATETIME YEAR TO FRACTION       not null
)
extent size 16 next size 16
lock mode row;

revoke all on submission_type_lu from 'public';
grant select,insert,update,delete on submission_type_lu to public as 'informix';

------ submission
create table 'informix'.submission (
    submission_id INT not null,
    upload_id INT not null,
    submission_status_id INT not null,
    screening_score DECIMAL(5,2),
    initial_score DECIMAL(5,2),
    final_score DECIMAL(5,2),
    placement DECIMAL(3,0),
    submission_type_id INTEGER NOT NULL,
    create_user VARCHAR(64) not null,
    create_date DATETIME YEAR TO FRACTION not null,
    modify_user VARCHAR(64) not null,
    modify_date DATETIME YEAR TO FRACTION not null,
    user_rank DECIMAL(5,0),
    mark_for_purchase BOOLEAN(1),
    prize_id INTEGER,
    file_size DECIMAL(18,0),
    view_count DECIMAL(10,0),
    system_file_name varchar(254)
)
extent size 5000 next size 2000
lock mode row;

revoke all on submission from 'public';
grant select,insert,update,delete on submission to public as 'informix';



------ project_studio_specification
create table 'informix'.project_studio_specification (
  project_studio_spec_id INTEGER not null,
  goals LVARCHAR (2000),
  target_audience LVARCHAR (500),
  branding_guidelines LVARCHAR (500),
  disliked_design_websites LVARCHAR (500),
  other_instructions LVARCHAR (2000),
  winning_criteria LVARCHAR (2000),
  submitters_locked_between_rounds BOOLEAN,
  round_one_introduction LVARCHAR (2000),
  round_two_introduction LVARCHAR (2000),
  colors LVARCHAR (500),
  fonts LVARCHAR (500),
  layout_and_size LVARCHAR (500),
  contest_introduction LVARCHAR (2000),
  contest_description LVARCHAR (10000),
  content_requirements LVARCHAR (2000),
  general_feedback lvarchar(2000),
  create_user VARCHAR(64) not null,
  create_date DATETIME YEAR TO FRACTION(3) not null,
  modify_user VARCHAR(64) not null,
  modify_date DATETIME YEAR TO FRACTION(3) not null
)
extent size 16 next size 16
lock mode row;
revoke all on project_studio_specification from 'public';
grant select,insert,update,delete on project_studio_specification to public as 'informix';

------ project_status_lu
create table 'informix'.project_status_lu (
    project_status_id INT not null,
    name VARCHAR(64) not null,
    description VARCHAR(254) not null,
    create_user VARCHAR(64) not null,
    create_date DATETIME YEAR TO FRACTION not null,
    modify_user VARCHAR(64) not null,
    modify_date DATETIME YEAR TO FRACTION not null
)
extent size 64 next size 64
lock mode row;

revoke all on project_status_lu from 'public';
grant select,insert,update,delete on project_status_lu to public as 'informix';

------ project_catalog_lu
CREATE TABLE 'informix'.project_catalog_lu (
    project_catalog_id INT not null,
    name VARCHAR(64),
    description VARCHAR(254),
    create_user VARCHAR(64) not null,
    create_date DATETIME YEAR TO FRACTION not null,
    modify_user VARCHAR(64) not null,
    modify_date DATETIME YEAR TO FRACTION not null,
    display boolean(1),
    display_order INT,
    version DECIMAL(12,0) default 0 not null
)
extent size 64 next size 64
lock mode row;

revoke all on project_catalog_lu from 'public';
grant select,insert,update,delete on project_catalog_lu to public as 'informix';

------ project_type_lu
create table 'informix'.project_type_lu (
    project_type_id INT not null,
    name VARCHAR(64) not null,
    description VARCHAR(254) not null,
    is_generic boolean default 'f'not null,
    create_user VARCHAR(64) not null,
    create_date DATETIME YEAR TO FRACTION not null,
    modify_user VARCHAR(64) not null,
    modify_date DATETIME YEAR TO FRACTION not null,
    version DECIMAL(12,0) default 0 not null
)
extent size 64 next size 64
lock mode row;

revoke all on project_type_lu from 'public';
grant select,insert,update,delete on project_type_lu to public as 'informix';

------ project_category_lu
create table 'informix'.project_category_lu (
    project_category_id INT not null,
    project_type_id INT not null,
    name VARCHAR(64) not null,
    description VARCHAR(254) not null,
    create_user VARCHAR(64) not null,
    create_date DATETIME YEAR TO FRACTION not null,
    modify_user VARCHAR(64) not null,
    modify_date DATETIME YEAR TO FRACTION not null,
    display boolean(1),
    display_order INT,
    project_catalog_id INT,
    version DECIMAL(12,0) default 0 not null
)
extent size 64 next size 64
lock mode row;

REVOKE ALL ON project_category_lu FROM 'public';
grant select,insert,update,delete on project_category_lu to public as 'informix';


------ project
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
revoke all on project from 'public';
grant select,insert,update,delete on project to public as 'informix';

------ phase_type_lu
create table 'informix'.phase_type_lu (
    phase_type_id INT not null,
    name VARCHAR(64) not null,
    description VARCHAR(254) not null,
    create_user VARCHAR(64) not null,
    create_date DATETIME YEAR TO FRACTION not null,
    modify_user VARCHAR(64) not null,
    modify_date DATETIME YEAR TO FRACTION not null
)
extent size 64 next size 64
lock mode row;

revoke all on phase_type_lu from 'public';

grant select,insert,update,delete on phase_type_lu to public as 'informix';

------ resource_role_lu
create table 'informix'.resource_role_lu (
    resource_role_id INT not null,
    phase_type_id INT,
    name VARCHAR(64) not null,
    description VARCHAR(254) not null,
    create_user VARCHAR(64) not null,
    create_date DATETIME YEAR TO FRACTION not null,
    modify_user VARCHAR(64) not null,
    modify_date DATETIME YEAR TO FRACTION not null
)
extent size 64 next size 64
lock mode row;

revoke all on resource_role_lu from 'public';

grant select,insert,update,delete on resource_role_lu to public as 'informix';

------ resource
create table 'informix'.resource (
    resource_id INT not null,
    resource_role_id INT not null,
    project_id INT,
    project_phase_id INT,
    create_user VARCHAR(64) not null,
    create_date DATETIME YEAR TO FRACTION not null,
    modify_user VARCHAR(64) not null,
    modify_date DATETIME YEAR TO FRACTION not null
)
extent size 15000 next size 10000
lock mode row;

revoke all on resource from 'public';

grant select,insert,update,delete on resource to public as 'informix';

------ project_payment_type_lu
create table 'informix'.project_payment_type_lu (
    project_payment_type_id serial NOT NULL,
    name VARCHAR(64) NOT NULL,
    mergeable boolean(1) NOT NULL
)
extent size 64 next size 64
lock mode row;

revoke all on project_payment_type_lu from 'public';

grant select,insert,update,delete on project_payment_type_lu to public as 'informix';

------ project_payment
create table 'informix'.project_payment (
    project_payment_id serial NOT NULL,
    project_payment_type_id INT NOT NULL,
    resource_id INT NOT NULL,
    submission_id INT,
    amount DECIMAL(12,2) NOT NULL,
    pacts_payment_id DECIMAL(10,0),
    create_date DATETIME YEAR TO FRACTION NOT NULL
)
extent size 400 next size 200
lock mode row;

revoke all on project_payment from 'public';

grant select,insert,update,delete on project_payment to public as 'informix';

------ project_payment_adjustment
create table 'informix'.project_payment_adjustment (
    project_id INT NOT NULL,
    resource_role_id INT NOT NULL,
    fixed_amount DECIMAL(12,2),
    multiplier FLOAT
)
extent size 400 next size 200
lock mode row;

revoke all on project_payment_adjustment from 'public';

grant select,insert,update,delete on project_payment_adjustment to public as 'informix';

------ default_project_payment
create table 'informix'.default_project_payment (
    project_category_id INT NOT NULL,
    resource_role_id INT NOT NULL,
    fixed_amount DECIMAL(12,2) NOT NULL,
    base_coefficient FLOAT NOT NULL,
    incremental_coefficient FLOAT NOT NULL
)
extent size 64 next size 64
lock mode row;

revoke all on default_project_payment from 'public';

grant select,insert,update,delete on default_project_payment to public as 'informix';

------ CONSTRAINTS

------ primary keys
alter table 'informix'.phase_status_lu add constraint primary key
    (phase_status_id)
    constraint pk_phase_status_lu;

alter table 'informix'.project_phase add constraint primary key
    (project_phase_id)
    constraint pk_project_phase;

alter table 'informix'.upload_type_lu add constraint primary key
    (upload_type_id)
    constraint pk_upload_type_lu;

alter table 'informix'.upload_status_lu add constraint primary key
    (upload_status_id)
    constraint pk_upload_status_lu;

alter table 'informix'.upload add constraint primary key
    (upload_id)
    constraint pk_upload;

alter table 'informix'.prize_type_lu add constraint primary key
    (prize_type_id)
    constraint prize_type_lu_pkey;

alter table 'informix'.prize add constraint primary key
    (prize_id)
    constraint pk_prize;

alter table 'informix'.submission_status_lu add constraint primary key
    (submission_status_id)
    constraint pk_submission_status_lu;

alter table 'informix'.submission_type_lu add constraint primary key
    (submission_type_id)
    constraint pk_submission_type_lu;

alter table 'informix'.submission add constraint primary key
    (submission_id)
    constraint pk_submission;

alter table 'informix'.project_status_lu add constraint primary key
    (project_status_id)
    constraint pk_project_status_lu;

ALTER TABLE 'informix'.project_catalog_lu ADD CONSTRAINT PRIMARY KEY
    (project_catalog_id)
    CONSTRAINT pk_project_catalog_lu;

alter table 'informix'.project_type_lu add constraint primary key
    (project_type_id)
    constraint pk_project_type_lu;

alter table 'informix'.project_category_lu add constraint primary key
    (project_category_id)
    constraint pk_project_category_lu;

alter table 'informix'.project add constraint primary key
    (project_id)
    constraint pk_project;

alter table 'informix'.phase_type_lu add constraint primary key
    (phase_type_id)
    constraint pk_phase_type_lu;

alter table 'informix'.resource_role_lu add constraint primary key
    (resource_role_id)
    constraint pk_resource_role_lu;

alter table 'informix'.resource add constraint primary key
    (resource_id)
    constraint pk_resource;

alter table 'informix'.project_payment_type_lu add constraint primary key
    (project_payment_type_id)
    constraint project_payment_type_lu_pk;

alter table 'informix'.project_payment add constraint primary key
    (project_payment_id)
    constraint project_payment_pk;

alter table 'informix'.project_payment_adjustment add constraint primary key
    (project_id, resource_role_id)
    constraint project_payment_adjustment_pk;

alter table 'informix'.default_project_payment add constraint primary key
    (project_category_id, resource_role_id)
    constraint default_project_payment_pk;


------ foreign keys
alter table 'informix'.upload add constraint foreign key
    (upload_type_id)
    references 'informix'.upload_type_lu
    (upload_type_id)
    constraint fk_upload_uploadtypelu_uploadtypeid;

alter table 'informix'.upload add constraint foreign key
    (upload_status_id)
    references 'informix'.upload_status_lu
    (upload_status_id)
    constraint fk_upload_uploadstatuslu_uploadstatusid;

alter table 'informix'.upload add constraint foreign key
    (resource_id)
    references 'informix'.resource
    (resource_id)
    constraint fk_upload_resource_resourceid;

alter table 'informix'.upload add constraint foreign key
    (project_id)
    references 'informix'.project
    (project_id)
    constraint fk_upload_project_projectid;

alter table 'informix'.upload add constraint foreign key
    (project_phase_id)
    references 'informix'.project_phase
    (project_phase_id)
    constraint fk_upload_projectphase_projectphaseid;

alter table 'informix'.project_phase add constraint foreign key
    (phase_type_id)
    references 'informix'.phase_type_lu
    (phase_type_id)
    constraint fk_projectphase_phasetypelu_phasetypeid;

alter table 'informix'.project_phase add constraint foreign key
    (project_id)
    references 'informix'.project
    (project_id)
    constraint fk_projectphase_project_projectid;

alter table 'informix'.project_phase add constraint foreign key
    (phase_status_id)
    references 'informix'.phase_status_lu
    (phase_status_id)
    constraint fk_projectphase_phasestatuslu_phasestatusid;

alter table 'informix'.prize add constraint foreign key
    (prize_type_id)
    references 'informix'.prize_type_lu
    (prize_type_id)
    constraint prize_file_type_fk;

alter table 'informix'.prize add constraint foreign key
    (project_id)
    references 'informix'.project
    (project_id)
    constraint prize_project_fk;

alter table 'informix'.submission add constraint foreign key
    (submission_status_id)
    references 'informix'.submission_status_lu
    (submission_status_id)
    constraint fk_submission_submissionstatuslu_submissionstatusid;

alter table 'informix'.submission add constraint foreign key
        (submission_type_id)
    references 'informix'.submission_type_lu
    (submission_type_id)
    constraint fk_submission_submissiontypelu_submissiontypeid;

alter table 'informix'.submission add constraint foreign key
    (upload_id)
    references 'informix'.upload
    (upload_id)
    constraint fk_submission_upload_uploadid;

alter table 'informix'.submission add constraint foreign key
    (prize_id)
    references 'informix'.prize
    (prize_id)
    constraint prize_submission_fk;

alter table 'informix'.project_studio_specification add constraint primary key
    (project_studio_spec_id)
    constraint pk_project_studio_spec;

ALTER TABLE 'informix'.project_category_lu ADD CONSTRAINT FOREIGN KEY
    (project_catalog_id)
    REFERENCES 'informix'.project_catalog_lu
    (project_catalog_id)
    CONSTRAINT fk_projectcategorylu_projectcataloglu_projectcatalogid;

alter table 'informix'.project_category_lu add constraint foreign key
    (project_type_id)
    references 'informix'.project_type_lu
    (project_type_id)
    constraint fk_projectcategorylu_projecttypelu_projecttypeid;

alter table 'informix'.project add constraint foreign key
    (project_category_id)
    references 'informix'.project_category_lu
    (project_category_id)
    constraint fk_project_projectcategorylu_projectcategoryid;

alter table 'informix'.project add constraint foreign key
    (project_status_id)
    references 'informix'.project_status_lu
    (project_status_id)
    constraint fk_project_projectstatuslu_projectstatusid;

alter table 'informix'.project add constraint foreign key
    (project_studio_spec_id)
    references 'informix'.project_studio_specification
    (project_studio_spec_id)
    constraint project_project_studio_spec_fk;

alter table 'informix'.resource_role_lu add constraint foreign key
    (phase_type_id)
    references 'informix'.phase_type_lu
    (phase_type_id)
    constraint fk_resourcerolelu_phasetypelu_phasetypeid;

alter table 'informix'.resource add constraint foreign key
    (project_id)
    references 'informix'.project
    (project_id)
    constraint fk_resource_project_projectid;

alter table 'informix'.resource add constraint foreign key
    (resource_role_id)
    references 'informix'.resource_role_lu
    (resource_role_id)
    constraint fk_resource_resourcerolelu_resourceroleid;

alter table 'informix'.resource add constraint foreign key
    (project_phase_id)
    references 'informix'.project_phase
    (project_phase_id)
    constraint fk_resource_projectphase_projectphaseid;

alter table 'informix'.project_payment add constraint foreign key
    (project_payment_type_id)
    references 'informix'.project_payment_type_lu
    (project_payment_type_id)
    constraint projectpayment_projectpaymenttypelu_fk;

alter table 'informix'.project_payment add constraint foreign key
    (resource_id)
    references 'informix'.resource
    (resource_id)
    constraint projectpayment_resource_fk;

alter table 'informix'.project_payment add constraint foreign key
    (submission_id)
    references 'informix'.submission
    (submission_id)
    constraint projectpayment_submission_fk;

alter table 'informix'.project_payment_adjustment add constraint foreign key
    (project_id)
    references 'informix'.project
    (project_id)
    constraint projectpaymentadjustment_project_fk;

alter table 'informix'.project_payment_adjustment add constraint foreign key
    (resource_role_id)
    references 'informix'.resource_role_lu
    (resource_role_id)
    constraint projectpaymentadjustment_resourcerolelu_fk;

alter table 'informix'.default_project_payment add constraint foreign key
    (project_category_id)
    references 'informix'.project_category_lu
    (project_category_id)
    constraint defaultprojectpayment_projectcategorylu_fk;

alter table 'informix'.default_project_payment add constraint foreign key
    (resource_role_id)
    references 'informix'.resource_role_lu
    (resource_role_id)
    constraint defaultprojectpayment_resourcerolelu_fk;