database corporate_oltp;
DROP TABLE 'informix'.tc_direct_project;
DROP TABLE 'informix'.user_permission_grant;


database tcs_catalog;
DROP TABLE 'informix'.late_deliverable;
DROP TABLE 'informix'.project_phase;
DROP TABLE 'informix'.project;
DROP TABLE 'informix'.resource_info;
DROP TABLE 'informix'.resource;


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



database tcs_catalog;

CREATE TABLE "informix".late_deliverable (
    late_deliverable_id serial NOT NULL,
    project_phase_id INTEGER NOT NULL,
    resource_id INTEGER NOT NULL,
    deliverable_id INTEGER NOT NULL,
    deadline DATETIME YEAR TO FRACTION NOT NULL,
    create_date DATETIME YEAR TO FRACTION NOT NULL,
    forgive_ind DECIMAL(1,0) NOT NULL,
    last_notified DATETIME YEAR TO FRACTION,
    delay DECIMAL(16,0),
    explanation lvarchar(4096),
    response lvarchar(4096)
);

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
);

create table 'informix'.project (
    project_id INT not null,
    project_status_id INT not null,
    project_category_id INT not null,
    create_user VARCHAR(64) not null,
    create_date DATETIME YEAR TO FRACTION not null,
    modify_user VARCHAR(64) not null,
    modify_date DATETIME YEAR TO FRACTION not null,
    tc_direct_project_id INT
);


create table 'informix'.resource_info (
    resource_id INT not null,
    resource_info_type_id INT not null,
    value VARCHAR(255) not null,
    create_user VARCHAR(64) not null,
    create_date DATETIME YEAR TO FRACTION not null,
    modify_user VARCHAR(64) not null,
    modify_date DATETIME YEAR TO FRACTION not null
);

create table 'informix'.resource (
    resource_id INT not null,
    resource_role_id INT not null,
    project_id INT,
    project_phase_id INT,
    create_user VARCHAR(64) not null,
    create_date DATETIME YEAR TO FRACTION not null,
    modify_user VARCHAR(64) not null,
    modify_date DATETIME YEAR TO FRACTION not null
);