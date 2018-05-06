
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