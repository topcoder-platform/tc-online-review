database tcs_catalog;

CREATE TABLE "informix".late_deliverable (
    late_deliverable_id serial NOT NULL,
    late_deliverable_type_id INTEGER NOT NULL,
    project_phase_id INTEGER NOT NULL,
    resource_id INTEGER NOT NULL,
    deliverable_id INTEGER NOT NULL,
    deadline DATETIME YEAR TO FRACTION NOT NULL,
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

CREATE TABLE 'informix'.project_phase (
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
);

CREATE TABLE 'informix'.project (
    project_id INT not null,
    project_status_id INT not null,
    project_category_id INT not null,
    create_user VARCHAR(64) not null,
    create_date DATETIME YEAR TO FRACTION not null,
    modify_user VARCHAR(64) not null,
    modify_date DATETIME YEAR TO FRACTION not null,
    tc_direct_project_id INT
);


CREATE TABLE 'informix'.resource_info (
    resource_id INT not null,
    resource_info_type_id INT not null,
    value VARCHAR(255) not null,
    create_user VARCHAR(64) not null,
    create_date DATETIME YEAR TO FRACTION not null,
    modify_user VARCHAR(64) not null,
    modify_date DATETIME YEAR TO FRACTION not null
);

CREATE TABLE 'informix'.resource (
    resource_id INT not null,
    resource_role_id INT not null,
    project_id INT,
    project_phase_id INT,
    create_user VARCHAR(64) not null,
    create_date DATETIME YEAR TO FRACTION not null,
    modify_user VARCHAR(64) not null,
    modify_date DATETIME YEAR TO FRACTION not null
);

CREATE TABLE 'informix'.resource_info_type_lu (
    resource_info_type_id INT not null,
    name VARCHAR(64) not null,
    description VARCHAR(254) not null,
    create_user VARCHAR(64) not null,
    create_date DATETIME YEAR TO FRACTION not null,
    modify_user VARCHAR(64) not null,
    modify_date DATETIME YEAR TO FRACTION not null
);

CREATE TABLE 'informix'.resource_role_lu (
    resource_role_id INT not null,
    phase_type_id INT,
    name VARCHAR(64) not null,
    description VARCHAR(254) not null,
    create_user VARCHAR(64) not null,
    create_date DATETIME YEAR TO FRACTION not null,
    modify_user VARCHAR(64) not null,
    modify_date DATETIME YEAR TO FRACTION not null
);

CREATE TABLE 'informix'.project_type_lu (
    project_type_id INT not null,
    name VARCHAR(64) not null,
    description VARCHAR(254) not null,
    is_generic boolean default 'f'not null,
    create_user VARCHAR(64) not null,
    create_date DATETIME YEAR TO FRACTION not null,
    modify_user VARCHAR(64) not null,
    modify_date DATETIME YEAR TO FRACTION not null,
    version DECIMAL(12,0) default 0 not null
);

CREATE TABLE 'informix'.project_status_lu (
    project_status_id INT not null,
    name VARCHAR(64) not null,
    description VARCHAR(254) not null,
    create_user VARCHAR(64) not null,
    create_date DATETIME YEAR TO FRACTION not null,
    modify_user VARCHAR(64) not null,
    modify_date DATETIME YEAR TO FRACTION not null
);

CREATE TABLE 'informix'.project_category_lu (
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
    version DECIMAL(12,0) default 0 not null
);

CREATE TABLE 'informix'.phase_type_lu (
    phase_type_id INT not null,
    name VARCHAR(64) not null,
    description VARCHAR(254) not null,
    create_user VARCHAR(64) not null,
    create_date DATETIME YEAR TO FRACTION not null,
    modify_user VARCHAR(64) not null,
    modify_date DATETIME YEAR TO FRACTION not null
);

CREATE TABLE 'informix'.phase_status_lu (
    phase_status_id INT not null,
    name VARCHAR(64) not null,
    description VARCHAR(254) not null,
    create_user VARCHAR(64) not null,
    create_date DATETIME YEAR TO FRACTION not null,
    modify_user VARCHAR(64) not null,
    modify_date DATETIME YEAR TO FRACTION not null
);

CREATE TABLE 'informix'.submission_type_lu (
    submission_type_id            INTEGER                         not null,
    name                          VARCHAR(64)                     not null,
    description                   VARCHAR(254)                    not null,
    create_user                   VARCHAR(64)                     not null,
    create_date                   DATETIME YEAR TO FRACTION       not null,
    modify_user                   VARCHAR(64)                     not null,
    modify_date                   DATETIME YEAR TO FRACTION       not null
);

CREATE TABLE 'informix'.deliverable_lu (
    deliverable_id INT not null,
    phase_type_id INT not null,
    resource_role_id INT not null,
    name VARCHAR(64) not null,
    description VARCHAR(64) not null,
    required DECIMAL(1,0) not null,
    submission_type_id INTEGER,
    create_user VARCHAR(64) not null,
    create_date DATETIME YEAR TO FRACTION not null,
    modify_user VARCHAR(64) not null,
    modify_date DATETIME YEAR TO FRACTION not null
);


CREATE TABLE "informix".late_deliverable_type_lu (
    late_deliverable_type_id INT NOT NULL,
    name VARCHAR(64) NOT NULL,
    description VARCHAR(254) NOT NULL
);

alter table 'informix'.late_deliverable_type_lu add constraint primary key
  (late_deliverable_type_id)
  constraint pk_late_deliverable_type_lu;


alter table 'informix'.project_type_lu add constraint primary key
        (project_type_id)
        constraint pk_project_type_lu;


alter table 'informix'.project_status_lu add constraint primary key
        (project_status_id)
        constraint pk_project_status_lu;


alter table 'informix'.project_category_lu add constraint primary key
        (project_category_id)
        constraint pk_project_category_lu;

alter table 'informix'.project_category_lu add constraint foreign key
        (project_type_id)
        references 'informix'.project_type_lu
        (project_type_id)
        constraint fk_projectcategorylu_projecttypelu_projecttypeid;


alter table 'informix'.project add constraint primary key
        (project_id)
        constraint pk_project;

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


alter table 'informix'.phase_type_lu add constraint primary key
        (phase_type_id)
        constraint pk_phase_type_lu;


alter table 'informix'.phase_status_lu add constraint primary key
        (phase_status_id)
        constraint pk_phase_status_lu;


alter table 'informix'.project_phase add constraint primary key
        (project_phase_id)
        constraint pk_project_phase;

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


alter table 'informix'.resource_role_lu add constraint primary key
        (resource_role_id)
        constraint pk_resource_role_lu;

alter table 'informix'.resource_role_lu add constraint foreign key
        (phase_type_id)
        references 'informix'.phase_type_lu
        (phase_type_id)
        constraint fk_resourcerolelu_phasetypelu_phasetypeid;


alter table 'informix'.resource add constraint primary key
        (resource_id)
        constraint pk_resource;

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


alter table 'informix'.resource_info_type_lu add constraint primary key
        (resource_info_type_id)
        constraint pk_resource_info_type_lu;


alter table 'informix'.resource_info add constraint primary key
        (resource_id, resource_info_type_id)
        constraint pk_resource_info;


alter table 'informix'.resource_info add constraint foreign key
        (resource_info_type_id)
        references 'informix'.resource_info_type_lu
        (resource_info_type_id)
        constraint fk_resourceinfo_resourceinfotypelu_resourceinfotypeid;

alter table 'informix'.resource_info add constraint foreign key
        (resource_id)
        references 'informix'.resource
        (resource_id)
        constraint fk_resourceinfo_resource_resourceid;


alter table 'informix'.submission_type_lu add constraint primary key
        (submission_type_id)
        constraint pk_submission_type_lu;


alter table 'informix'.deliverable_lu add constraint primary key
        (deliverable_id)
        constraint pk_deliverable_lu;

alter table 'informix'.deliverable_lu add constraint foreign key
        (phase_type_id)
        references 'informix'.phase_type_lu
        (phase_type_id)
        constraint fk_deliverablelu_phasetypelu_phasetypeid;

alter table 'informix'.deliverable_lu add constraint foreign key
        (resource_role_id)
        references 'informix'.resource_role_lu
        (resource_role_id)
        constraint fk_deliverablelu_resourcerolelu_resourceroleid;

alter table 'informix'.deliverable_lu add constraint foreign key
        (submission_type_id)
        references 'informix'.submission_type_lu
        (submission_type_id)
        constraint fk_deliverablelu_submissiontypelu_submissiontypeid;

alter table 'informix'.late_deliverable add constraint foreign key
    (late_deliverable_type_id)
    references 'informix'.late_deliverable_type_lu
    (late_deliverable_type_id)
    constraint fk_latedeliverable_latedeliverabletypelu_latedeliverabletypeid;

alter table 'informix'.late_deliverable add constraint primary key
  (late_deliverable_id)
  constraint pk_late_deliverable;

alter table 'informix'.late_deliverable add constraint foreign key
    (project_phase_id)
    references 'informix'.project_phase
    (project_phase_id)
    constraint fk_latedeliverable_projectphase_projectphaseid;

alter table 'informix'.late_deliverable add constraint foreign key
    (resource_id)
    references 'informix'.resource
    (resource_id) on delete cascade
    constraint fk_latedeliverable_resource_resource_id;

alter table 'informix'.late_deliverable add constraint foreign key
    (deliverable_id)
    references 'informix'.deliverable_lu
    (deliverable_id)
    constraint fk_latedeliverable_deliverablelu_deliverableid;
    
database corporate_oltp;

create table "informix".tc_direct_project 
  (
    project_id integer not null ,
    name varchar(200) not null ,
    description lvarchar(10000),
    project_status_id INT default 1 not null,
    user_id integer not null ,
    create_date datetime year to fraction(3) not null ,
    modify_date datetime year to fraction(3),
    primary key (project_id)  constraint "informix".tc_direct_project_pkey
  ); 
  
  
create table "informix".user_permission_grant 
  (
    user_permission_grant_id decimal(10) not null ,
    user_id decimal(10,0) not null ,
    resource_id decimal(10,0) not null ,
    permission_type_id decimal(10,0) not null ,
    is_studio smallint,
    primary key (user_permission_grant_id)  constraint "informix".pk_user_permission_grant_id
  );