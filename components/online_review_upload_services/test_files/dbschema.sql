CREATE TABLE id_sequences (
  name       VARCHAR(255)    NOT NULL,
  value      INTEGER         NOT NULL,
  PRIMARY KEY (name)
);

create table project (
    project_id INT,
    project_status_id INT,
    project_category_id INT,
    create_user VARCHAR(64),
    create_date DATETIME YEAR TO FRACTION,
    modify_user VARCHAR(64),
    modify_date DATETIME YEAR TO FRACTION,
    tc_direct_project_id INT,
    PRIMARY KEY(project_id)
);

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
    point_adjustment FLOAT,
    current_reliability_ind DECIMAL(1,0),
    rating_order INT
);

create table component_inquiry (
    component_inquiry_id DECIMAL(12,0),
    component_id DECIMAL(12,0),
    user_id DECIMAL(12,0),
    comment VARCHAR(254),
    agreed_to_terms DECIMAL(1),
    rating DECIMAL(5),
    phase DECIMAL(12,0),
    tc_user_id DECIMAL(12,0),
    version DECIMAL(12,0),
    create_time DATETIME YEAR TO FRACTION default CURRENT YEAR TO FRACTION,
    project_id DECIMAL(10,0)
);

-- schema from user_project_data_store 
create table email (
    email_id DECIMAL(10,0),
    user_id DECIMAL(10,0),    
    email_type_id DECIMAL(5,0),
    address VARCHAR(100),
    create_date DATETIME YEAR TO FRACTION default CURRENT YEAR TO FRACTION,
    modify_date DATETIME YEAR TO FRACTION default CURRENT YEAR TO FRACTION,
    primary_ind DECIMAL(1,0),
    status_id DECIMAL(3,0)
);

create index email_user_id_idx on email
	(
	user_id, 
	primary_ind
	);

alter table email add constraint primary key
	(email_id)
	constraint pk_email;

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
)
extent size 250 next size 124
lock mode row;

alter table user_rating add constraint primary key 
	(user_id, phase_id)
	constraint pk_user_rating;


create table user_reliability (
    user_id DECIMAL(10,0),
    rating DECIMAL(5,4),
    modify_date DATETIME YEAR TO FRACTION default CURRENT YEAR TO FRACTION,
    create_date DATETIME YEAR TO FRACTION default CURRENT YEAR TO FRACTION,
    phase_id DECIMAL(12,0)
);

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
);

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
);

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
);

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


create table technology_types (
    technology_type_id DECIMAL(12,0) not null,
    technology_name VARCHAR(100) not null,
    description VARCHAR(254) not null,
    status_id DECIMAL(12,0) not null
);


alter table technology_types add constraint primary key 
	(technology_type_id)
	constraint pk_technology_type;
	
create table comp_technology (
    comp_tech_id DECIMAL(12,0) not null,
    comp_vers_id DECIMAL(12,0),
    technology_type_id DECIMAL(12,0)
);

create unique cluster index comp_tech_i1 on comp_technology
	(
	comp_vers_id, 
	technology_type_id
	);

alter table comp_technology add constraint primary key 
	(comp_tech_id)
	constraint pk_comp_technology;

alter table comp_technology add constraint foreign key 
	(comp_vers_id)
	references comp_versions
	(comp_vers_id) 
	constraint fk_comp_tech1;

alter table comp_technology add constraint foreign key 
	(technology_type_id)
	references technology_types
	(technology_type_id) 
	constraint fk_comp_tech2;


