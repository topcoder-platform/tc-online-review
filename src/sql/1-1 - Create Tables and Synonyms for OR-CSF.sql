-- Synonyms from csf_oltp. Connection JNDIName: InfomixDS
CREATE SYNONYM security_user FOR csf_oltp:security_user;
CREATE SYNONYM security_groups FOR csf_oltp:security_groups;
CREATE SYNONYM security_perms FOR csf_oltp:security_perms;
CREATE SYNONYM security_roles FOR csf_oltp:security_roles;
CREATE SYNONYM security_status_lu FOR csf_oltp:security_status_lu;
CREATE SYNONYM user_group_xref FOR csf_oltp:user_group_xref;
CREATE SYNONYM group_role_xref FOR csf_oltp:group_role_xref;
CREATE SYNONYM user_role_xref FOR csf_oltp:user_role_xref;

-- Synonyms from csf_oltp. Connection JNDIName: OLTP
-- isn't use: CREATE SYNONYM data_type_lu FOR csf_oltp:data_type_lu;
CREATE SYNONYM command FOR csf_oltp:command;
CREATE SYNONYM command_query_xref FOR csf_oltp:command_query_xref;
CREATE SYNONYM input_lu FOR csf_oltp:input_lu;
CREATE SYNONYM query_input_xref FOR csf_oltp:query_input_xref;
CREATE SYNONYM query FOR csf_oltp:query;
CREATE SYNONYM coder FOR csf_oltp:coder;
CREATE SYNONYM algo_rating FOR csf_oltp:algo_rating;
CREATE SYNONYM algo_rating_type_lu FOR csf_oltp:algo_rating_type_lu;
CREATE SYNONYM command_execution FOR csf_oltp:command_execution;

-- Synonyms from csf_oltp. Connection JNDIName: JTS_TCS_CATALOG/JTS_TCS_CATALOG_IDGEN
CREATE SYNONYM user FOR csf_oltp:user;
CREATE SYNONYM email FOR csf_oltp:email;
CREATE SYNONYM id_sequences FOR csf_oltp:id_sequences;

-- Synonyms from tcs_catalog. Connection JNDIName: JTS_TCS_CATALOG/JTS_TCS_CATALOG_IDGEN
create table user_rating (
    user_id DECIMAL(10) not null,
    rating DECIMAL(10) default 0 not null,
    phase_id DECIMAL(3) not null,
    vol DECIMAL(10) default 0 not null,
    rating_no_vol DECIMAL(10) default 0 not null,
    num_ratings DECIMAL(5) default 0 not null,
    mod_date_time DATETIME YEAR TO FRACTION default CURRENT YEAR TO FRACTION not null,
    create_date_time DATETIME YEAR TO FRACTION default CURRENT YEAR TO FRACTION not null,
    last_rated_project_id DECIMAL(12,0)
);
alter table user_rating add constraint primary key (user_id, phase_id)constraint pk_user_rating;

create table user_reliability (
    user_id DECIMAL(10,0),
    rating DECIMAL(5,4),
    modify_date DATETIME YEAR TO FRACTION default CURRENT YEAR TO FRACTION,
    create_date DATETIME YEAR TO FRACTION default CURRENT YEAR TO FRACTION,
    phase_id DECIMAL(12,0)
);
alter table user_reliability add constraint primary key (user_id, phase_id) constraint user_reliability_pkey;
alter table user_reliability add constraint foreign key (phase_id) references phase (phase_id) constraint userreliability_phase_fk;

create table project_result (
    user_id DECIMAL(10,0),
    project_id DECIMAL(10,0),
    old_rating DECIMAL(5,0),
    new_rating DECIMAL(5,0),
    old_reliability DECIMAL(5,4),
    new_reliability DECIMAL(5,4),
    raw_score DECIMAL(5,2),
    final_score DECIMAL(5,2),
    payment DECIMAL(10,2),
    placed DECIMAL(6,0),
    rating_ind DECIMAL(1,0),
    valid_submission_ind DECIMAL(1,0),
    reliability_ind DECIMAL(1,0),
    create_date DATETIME YEAR TO FRACTION default CURRENT YEAR TO FRACTION,
    modify_date DATETIME YEAR TO FRACTION default CURRENT YEAR TO FRACTION,
    reliable_submission_ind DECIMAL(1,0),
    passed_review_ind DECIMAL(1,0),
    point_adjustment DECIMAL(5,0) default 0,
    current_reliability_ind DECIMAL(1,0)
);
create index proj_result_idx1 on project_result (project_id);
alter table project_result add constraint primary key (user_id, project_id) constraint project_result_pkey;

create table component_inquiry (
    component_inquiry_id DECIMAL(12,0) not null,
    component_id DECIMAL(12,0) not null,
    user_id DECIMAL(12,0) not null,
    comment VARCHAR(254),
    agreed_to_terms DECIMAL(1) not null,
    rating DECIMAL(5) not null,
    phase DECIMAL(12,0) not null,
    tc_user_id DECIMAL(12,0) not null,
    version DECIMAL(12,0) not null,
    create_time DATETIME YEAR TO FRACTION default CURRENT YEAR TO FRACTION not null,
    project_id DECIMAL(10,0)
);
create index compinquiry_idx1 on component_inquiry (component_id, phase, version);
create index compinquiry_idx2 on component_inquiry (user_id);
create index compinquiry_idx3 on component_inquiry (project_id);
create index compinquiry_idx4 on component_inquiry(tc_user_id);
alter table component_inquiry add constraint primary key (component_inquiry_id)	constraint pk_component_inquiry_id;

create table comp_version_dates (
    comp_version_dates_id DECIMAL(12,0) not null,
    comp_vers_id DECIMAL(12,0) not null,
    phase_id DECIMAL(12,0) not null,
    posting_date DATETIME YEAR TO DAY not null,
    initial_submission_date DATETIME YEAR TO DAY not null,
    winner_announced_date DATETIME YEAR TO DAY not null,
    final_submission_date DATETIME YEAR TO DAY not null,
    estimated_dev_date DATETIME YEAR TO DAY,
    price DECIMAL(10,2) not null,
    total_submissions DECIMAL(12,0) default 0 not null,
    status_id DECIMAL(12,0) default 301 not null,
    create_time DATETIME YEAR TO FRACTION default CURRENT YEAR TO FRACTION not null,
    level_id DECIMAL(12,0),
    screening_complete_date DATETIME YEAR TO DAY,
    review_complete_date DATETIME YEAR TO DAY,
    aggregation_complete_date DATETIME YEAR TO DAY,
    phase_complete_date DATETIME YEAR TO DAY,
    production_date DATETIME YEAR TO DAY,
    aggregation_complete_date_comment VARCHAR(254),
    phase_complete_date_comment VARCHAR(254),
    review_complete_date_comment VARCHAR(254),
    winner_announced_date_comment VARCHAR(254),
    initial_submission_date_comment VARCHAR(254),
    screening_complete_date_comment VARCHAR(254),
    final_submission_date_comment VARCHAR(254),
    production_date_comment VARCHAR(254),
    modify_date DATETIME YEAR TO FRACTION default CURRENT YEAR TO FRACTION not null
);
create index compverdates_idx1 on comp_version_dates (status_id);
create index compverdates_idx2 on comp_version_dates (comp_vers_id, phase_id);
alter table comp_version_dates add constraint primary key (comp_version_dates_id)constraint pk_comp_version_dates_id;

-- to open screening
create table rboard_payment (
    project_id DECIMAL(10,0) not null,
    phase_id DECIMAL(5,0) not null,
    primary_ind DECIMAL(1,0) not null,
    amount DECIMAL(7,2) not null,
    modify_date DATETIME YEAR TO FRACTION default CURRENT YEAR TO FRACTION,
    create_date DATETIME YEAR TO FRACTION default CURRENT YEAR TO FRACTION
);
alter table rboard_payment add constraint primary key (project_id, phase_id, primary_ind)constraint rboard_payment_pk;
	
-- Synonyms from common_oltp. Connection JNDIName: JTS_TCS_CATALOG/JTS_TCS_CATALOG_IDGEN
-- for create project
create table sequence_object (
    name VARCHAR(25) not null,
    id DECIMAL(3,0),
    current_value DECIMAL(12,0)
);
create index sequence_object_idx1 on sequence_object (id);
alter table sequence_object add constraint primary key (name) constraint sequence_object_pkey;
insert into sequence_object (name, id, current_value) values ('main_sequence', 200, 1);

