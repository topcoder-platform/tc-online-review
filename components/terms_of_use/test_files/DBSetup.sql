create table 'informix'.id_sequences (
    name VARCHAR(254),
    next_block_start DECIMAL(12,0) not null,
    block_size DECIMAL(10,0) not null,
    exhausted DECIMAL(1,0) default 0 not null
)
extent size 64 next size 64
lock mode row;

revoke all on id_sequences from 'public';
create table 'informix'.timezone_lu (
    timezone_id DECIMAL(5,0),
    timezone_desc VARCHAR(100),
    create_date DATETIME YEAR TO FRACTION default CURRENT YEAR TO FRACTION
)
extent size 64 next size 64
lock mode row;

revoke all on timezone_lu from 'public';
create table 'informix'.user (
    user_id DECIMAL(10,0) not null,
    first_name VARCHAR(64),
    last_name VARCHAR(64),
    create_date DATETIME YEAR TO FRACTION default CURRENT YEAR TO FRACTION,
    modify_date DATETIME YEAR TO FRACTION default CURRENT YEAR TO FRACTION,
    handle VARCHAR(50) not null,
    last_login DATETIME YEAR TO FRACTION,
    status VARCHAR(3) not null,
    activation_code VARCHAR(32),
    middle_name VARCHAR(64),
    handle_lower VARCHAR(50),
    timezone_id DECIMAL(5,0),
    last_site_hit_date DATETIME YEAR TO FRACTION,
    name_in_another_language VARCHAR(64)
)
extent size 10000 next size 5000
lock mode row;

revoke all on user from 'public';
create table 'informix'.terms_of_use (
    terms_of_use_id DECIMAL(10,0) NOT NULL,
    terms_text TEXT,
    terms_of_use_type_id DECIMAL(5,0) NOT NULL,
    create_date DATETIME YEAR TO FRACTION default CURRENT YEAR TO FRACTION,
    modify_date DATETIME YEAR TO FRACTION default CURRENT YEAR TO FRACTION,
    title VARCHAR(50) NOT NULL,
    url VARCHAR(100),
    terms_of_use_agreeability_type_id DECIMAL(5,0) NOT NULL
)
extent size 512 next size 512
lock mode row;

revoke all on terms_of_use from 'public';
create table 'informix'.terms_of_use_agreeability_type_lu (
    terms_of_use_agreeability_type_id DECIMAL(5,0) NOT NULL,
    name VARCHAR(64) NOT NULL,
    description VARCHAR(100) NOT NULL
)
extent size 16 next size 16
lock mode row;

revoke all on terms_of_use_agreeability_type_lu from 'public';
create table 'informix'.terms_of_use_dependency (
    dependency_terms_of_use_id DECIMAL(10,0) NOT NULL,
    dependent_terms_of_use_id DECIMAL(10,0) NOT NULL
)
extent size 500 next size 250
lock mode row;

revoke all on terms_of_use_dependency from 'public';
create table 'informix'.terms_of_use_type (
    terms_of_use_type_id DECIMAL(5,0) NOT NULL,
    terms_of_use_type_desc VARCHAR(100),
    create_date DATETIME YEAR TO FRACTION default CURRENT YEAR TO FRACTION,
    modify_date DATETIME YEAR TO FRACTION default CURRENT YEAR TO FRACTION
)
extent size 64 next size 64
lock mode row;

revoke all on terms_of_use_type from 'public';
create table 'informix'.user_terms_of_use_xref (
    user_id DECIMAL(10,0) NOT NULL,
    terms_of_use_id DECIMAL(10,0) NOT NULL,
    create_date DATETIME YEAR TO FRACTION default CURRENT YEAR TO FRACTION,
    modify_date DATETIME YEAR TO FRACTION default CURRENT YEAR TO FRACTION
)
extent size 500 next size 250
lock mode row;

revoke all on user_terms_of_use_xref from 'public';
create table 'informix'.user_terms_of_use_ban_xref (
    user_id DECIMAL(10,0) NOT NULL,
    terms_of_use_id DECIMAL(10,0) NOT NULL,
    create_date DATETIME YEAR TO FRACTION default CURRENT YEAR TO FRACTION
)
extent size 128 next size 128
lock mode row;

revoke all on user_terms_of_use_ban_xref from 'public';
create table 'informix'.project_role_terms_of_use_xref (
    project_id INT not null,
    resource_role_id INT not null,
    terms_of_use_id DECIMAL(10,0) not null,
    create_date DATETIME YEAR TO FRACTION default CURRENT YEAR TO FRACTION,
    modify_date DATETIME YEAR TO FRACTION default CURRENT YEAR TO FRACTION,
    sort_order DECIMAL(1,0) default 1 not null,
    group_ind INT default 0 NOT NULL
)
extent size 2000 next size 2000
lock mode row;

revoke all on project_role_terms_of_use_xref from 'public';




grant update on id_sequences to 'public' as 'informix';

grant select on id_sequences to 'public' as 'informix';

grant index on id_sequences to 'public' as 'informix';

grant delete on id_sequences to 'public' as 'informix';

grant insert on id_sequences to 'public' as 'informix';

grant select on timezone_lu to 'public' as 'informix';

grant insert on timezone_lu to 'public' as 'informix';

grant index on timezone_lu to 'public' as 'informix';

grant delete on timezone_lu to 'public' as 'informix';

grant update on timezone_lu to 'public' as 'informix';

grant index on user to 'public' as 'informix';

grant delete on user to 'public' as 'informix';

grant select on user to 'public' as 'informix';

grant update on user to 'public' as 'informix';

grant insert on user to 'public' as 'informix';

grant index on terms_of_use to 'public' as 'informix';

grant delete on terms_of_use to 'public' as 'informix';

grant update on terms_of_use to 'public' as 'informix';

grant insert on terms_of_use to 'public' as 'informix';

grant select on terms_of_use to 'public' as 'informix';

grant insert on terms_of_use_type to 'public' as 'informix';

grant select on terms_of_use_type to 'public' as 'informix';

grant delete on terms_of_use_type to 'public' as 'informix';

grant update on terms_of_use_type to 'public' as 'informix';

grant index on terms_of_use_type to 'public' as 'informix';

grant delete on user_terms_of_use_xref to 'public' as 'informix';

grant insert on user_terms_of_use_xref to 'public' as 'informix';

grant select on user_terms_of_use_xref to 'public' as 'informix';

grant update on user_terms_of_use_xref to 'public' as 'informix';

grant index on user_terms_of_use_xref to 'public' as 'informix';

grant delete on user_terms_of_use_ban_xref to 'public' as 'informix';

grant insert on user_terms_of_use_ban_xref to 'public' as 'informix';

grant select on user_terms_of_use_ban_xref to 'public' as 'informix';

grant update on user_terms_of_use_ban_xref to 'public' as 'informix';

grant index on user_terms_of_use_ban_xref to 'public' as 'informix';

grant delete on project_role_terms_of_use_xref to 'public' as 'informix';

grant update on project_role_terms_of_use_xref to 'public' as 'informix';

grant insert on project_role_terms_of_use_xref to 'public' as 'informix';

grant select on project_role_terms_of_use_xref to 'public' as 'informix';

grant index on project_role_terms_of_use_xref to 'public' as 'informix';




alter table 'informix'.id_sequences add constraint primary key 
    (name)
    constraint id_sequences_pkey;
    
alter table 'informix'.timezone_lu add constraint primary key
    (timezone_id)
    constraint timezone_lu_pk;

alter table 'informix'.user add constraint primary key
    (user_id)
    constraint u175_45;

alter table 'informix'.terms_of_use add constraint primary key
    (terms_of_use_id)
    constraint u114_27;

alter table 'informix'.terms_of_use_agreeability_type_lu add constraint primary key
    (terms_of_use_agreeability_type_id)
    constraint terms_of_use_agreeability_type_lu_pk;

alter table 'informix'.terms_of_use_dependency add constraint primary key
    (dependency_terms_of_use_id, dependent_terms_of_use_id)
    constraint terms_of_use_dependency_pk;

alter table 'informix'.terms_of_use_type add constraint primary key
    (terms_of_use_type_id)
    constraint u115_28;

alter table 'informix'.user_terms_of_use_xref add constraint primary key
    (user_id, terms_of_use_id)
    constraint u116_29;

alter table 'informix'.user_terms_of_use_ban_xref add constraint primary key
        (user_id, terms_of_use_id)
        constraint user_terms_of_use_ban_xref_pk;

alter table 'informix'.project_role_terms_of_use_xref add constraint primary key
    (project_id, resource_role_id, terms_of_use_id, group_ind)
    constraint pk_project_role_terms_of_use_xref;



alter table 'informix'.user add constraint foreign key
    (timezone_id)
    references 'informix'.timezone_lu
    (timezone_id)
    constraint user_timezonelu_fk;

alter table 'informix'.terms_of_use add constraint foreign key
    (terms_of_use_type_id)
    references 'informix'.terms_of_use_type
    (terms_of_use_type_id)
    constraint termsofuse_termsofusetype_fk;

alter table 'informix'.terms_of_use add constraint foreign key
    (terms_of_use_agreeability_type_id)
    references 'informix'.terms_of_use_agreeability_type_lu
    (terms_of_use_agreeability_type_id)
    constraint terms_of_use_terms_of_use_agreeability_type_fk;

alter table 'informix'.terms_of_use_dependency add constraint foreign key
    (dependency_terms_of_use_id)
    references 'informix'.terms_of_use
    (terms_of_use_id)
    constraint terms_of_use_dependency_fk;

alter table 'informix'.terms_of_use_dependency add constraint foreign key
    (dependent_terms_of_use_id)
    references 'informix'.terms_of_use
    (terms_of_use_id)
    constraint terms_of_use_dependent_fk;

alter table 'informix'.user_terms_of_use_xref add constraint foreign key
    (terms_of_use_id)
    references 'informix'.terms_of_use
    (terms_of_use_id)
    constraint userterms_terms_fk;

alter table 'informix'.user_terms_of_use_xref add constraint foreign key
    (user_id)
    references 'informix'.user
    (user_id)
    constraint usertermsofusexref_user_fk;

alter table 'informix'.user_terms_of_use_ban_xref add constraint foreign key
        (terms_of_use_id)
        references 'informix'.terms_of_use
        (terms_of_use_id)
        constraint usertermsofusebanxref_termsofuse_fk;

alter table 'informix'.user_terms_of_use_ban_xref add constraint foreign key
        (user_id)
        references 'informix'.user
        (user_id)
        constraint usertermsofusebanxref_user_fk;

alter table 'informix'.project_role_terms_of_use_xref add constraint foreign key
    (terms_of_use_id)
    references 'informix'.terms_of_use
    (terms_of_use_id)
    constraint project_role_terms_terms_fk;