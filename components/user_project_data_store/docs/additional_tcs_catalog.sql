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
    modify_date DATETIME YEAR TO FRACTION default CURRENT YEAR TO FRACTION not null
)
extent size 1000 next size 500
lock mode row;

alter table 'informix'.comp_catalog add constraint primary key 
	(component_id)
	constraint pk_comp_catalog;

alter table 'informix'.comp_catalog add constraint foreign key 
	(status_id)
	references 'informix'.status
	(status_id) 
	constraint fk_comp_status;




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

alter table 'informix'.categories add constraint foreign key 
	(status_id)
	references 'informix'.status
	(status_id) 
	constraint fk_ctgy_status;





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
    modify_date DATETIME YEAR TO FRACTION default CURRENT YEAR TO FRACTION not null
)
extent size 500 next size 124
lock mode row;

create unique cluster index 'informix'.comp_versions_i2 on 'informix'.comp_versions
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

alter table 'informix'.comp_versions add constraint foreign key 
	(phase_id)
	references 'informix'.phase
	(phase_id) 
	constraint fk_comp_phase;




create table 'informix'.comp_forum_xref (
    comp_forum_id DECIMAL(12,0) not null,
    comp_vers_id DECIMAL(12,0),
    forum_id DECIMAL(12,0),
    forum_type DECIMAL(5) not null
)
extent size 250 next size 124
lock mode row;

create unique cluster index 'informix'.comp_forum_xref_i1 on 'informix'.comp_forum_xref
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

alter table 'informix'.comp_forum_xref add constraint foreign key 
	(forum_id)
	references 'informix'.forum_master
	(forum_id) 
	constraint fk_comp_forum1;




