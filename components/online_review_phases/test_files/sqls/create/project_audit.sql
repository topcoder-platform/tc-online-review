database online_review;

-- create new lookup table for audit action types
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

-- initialize this lookup table
insert into audit_action_type_lu values (1, "Create", "Create", "System", current, "System", current);
insert into audit_action_type_lu values (2, "Delete", "Delete", "System", current, "System", current);

-- create audit table for project resources
create table 'informix'.project_user_audit  (
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

alter table 'informix'.project_user_audit add constraint primary key 
	(project_user_audit_id)
	constraint project_user_audit_pkey;

-- enforce Referential Integrity.
alter table 'informix'.project_user_audit add constraint foreign key 
	(audit_action_type_id)
	references 'informix'.audit_action_type_lu
	(audit_action_type_id) 
	constraint project_user_audit_audit_action_type_lu_fk;

alter table 'informix'.project_user_audit add constraint foreign key 
	(project_id)
	references 'informix'.project
	(project_id) 
	constraint project_user_audit_project_fk;

alter table 'informix'.project_user_audit add constraint foreign key 
	(resource_role_id)
	references 'informix'.resource_role_lu
	(resource_role_id) 
	constraint project_user_audit_resource_role_lu_fk;

-- create sequence for project_user_audit
CREATE SEQUENCE PROJECT_USER_AUDIT_SEQ;
