create table 'informix'.email (
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

create index 'informix'.email_user_id_idx on 'informix'.email
	(
	user_id, 
	primary_ind
	);

alter table 'informix'.email add constraint primary key 
	(email_id)
	constraint u110_23;





create table 'informix'.user (
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

create index 'informix'.user_handle_idx on 'informix'.user
	(
	handle
	);

create index 'informix'.user_lower_handle_idx on 'informix'.user
	(
	handle_lower
	);

alter table 'informix'.user add constraint primary key 
	(user_id)
	constraint u124_45;




create table 'informix'.user_rating (
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

alter table 'informix'.user_rating add constraint primary key 
	(user_id, phase_id)
	constraint pk_user_rating;



create table 'informix'.user_reliability (
    user_id DECIMAL(10,0),
    rating DECIMAL(5,4),
    modify_date DATETIME YEAR TO FRACTION default CURRENT YEAR TO FRACTION,
    create_date DATETIME YEAR TO FRACTION default CURRENT YEAR TO FRACTION,
    phase_id DECIMAL(12,0)
)
extent size 32 next size 32
lock mode row;

alter table 'informix'.user_reliability add constraint primary key 
	(user_id, phase_id)
	constraint user_reliability_pkey;





create table 'informix'.comp_catalog (
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
)
extent size 1000 next size 500
lock mode row;

alter table 'informix'.comp_catalog add constraint primary key 
	(component_id)
	constraint pk_comp_catalog;





create table 'informix'.categories (
    category_id DECIMAL(12,0) not null,
    parent_category_id DECIMAL(12,0),
    category_name VARCHAR(100) not null,
    description VARCHAR(254) not null,
    status_id DECIMAL(12,0) not null,
    viewable DECIMAL(1,0) default 1
)
extent size 16 next size 16
lock mode row;

alter table 'informix'.categories add constraint primary key 
	(category_id)
	constraint pk_categories;







create table 'informix'.comp_versions (
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

create unique cluster index 'informix'.comp_versions_i2 on
'informix'.comp_versions
	(
	component_id, 
	version
	);

alter table 'informix'.comp_versions add constraint primary key 
	(comp_vers_id)
	constraint pk_comp_versions;

alter table 'informix'.comp_versions add constraint foreign key 
	(component_id)
	references 'informix'.comp_catalog
	(component_id) 
	constraint fk_comp_versions;





create table 'informix'.comp_forum_xref (
    comp_forum_id DECIMAL(12,0) not null,
    comp_vers_id DECIMAL(12,0),
    forum_id DECIMAL(12,0),
    forum_type DECIMAL(5) not null
)
extent size 250 next size 124
lock mode row;

create unique cluster index 'informix'.comp_forum_xref_i1 on
'informix'.comp_forum_xref
	(
	comp_vers_id, 
	forum_id
	);

alter table 'informix'.comp_forum_xref add constraint primary key 
	(comp_forum_id)
	constraint pk_comp_forum_xref;

alter table 'informix'.comp_forum_xref add constraint foreign key 
	(comp_vers_id)
	references 'informix'.comp_versions
	(comp_vers_id) 
	constraint fk_comp_forum2;


create table 'informix'.technology_types (
    technology_type_id DECIMAL(12,0) not null,
    technology_name VARCHAR(100) not null,
    description VARCHAR(254) not null,
    status_id DECIMAL(12,0) not null
)
extent size 16 next size 16
lock mode row;


alter table 'informix'.technology_types add constraint primary key 
	(technology_type_id)
	constraint pk_technology_type;
	
create table 'informix'.comp_technology (
    comp_tech_id DECIMAL(12,0) not null,
    comp_vers_id DECIMAL(12,0),
    technology_type_id DECIMAL(12,0)
)
extent size 250 next size 124
lock mode row;

create unique cluster index 'informix'.comp_tech_i1 on 'informix'.comp_technology
	(
	comp_vers_id, 
	technology_type_id
	);

alter table 'informix'.comp_technology add constraint primary key 
	(comp_tech_id)
	constraint pk_comp_technology;

alter table 'informix'.comp_technology add constraint foreign key 
	(comp_vers_id)
	references 'informix'.comp_versions
	(comp_vers_id) 
	constraint fk_comp_tech1;

alter table 'informix'.comp_technology add constraint foreign key 
	(technology_type_id)
	references 'informix'.technology_types
	(technology_type_id) 
	constraint fk_comp_tech2;


create table 'informix'.comp_jive_category_xref (
    comp_vers_id DECIMAL(12,0) not null,
    jive_category_id INT not null
)
extent size 250 next size 250
lock mode page;