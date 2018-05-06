use orl;

drop table if exists group_role_xref;

drop table if exists key_generation;

drop table if exists user_group_xref;

drop table if exists user_role_xref;

drop table if exists security_groups;

drop table if exists security_perms;

drop table if exists security_roles;

drop table if exists security_user;

create table group_role_xref  (
  group_role_id        decimal(18,0) not null,
  group_id             decimal(18,0),
  role_id              decimal(18,0),
  constraint pk_group_role_xref primary key (group_role_id)
);


create unique index group_role_xref_i2 on group_role_xref (
        group_id asc, role_id asc
);

create table key_generation  (
  user_def             char(18) not null,
  high_value           decimal(13,0)  not null,
  constraint pk_key_generation primary key (user_def)      
);

create table security_groups  (
  group_id             decimal(18,0) not null,
  description          varchar(254)  not null,
  constraint pk_security_groups primary key (group_id)      
);

create table security_perms  (
  role_id              decimal(18,0) not null,
  permission           varchar(254)  not null,
  constraint pk_security_perms primary key (role_id, permission)
);

create table security_roles  (
  role_id              decimal(18,0) not null,
  description          varchar(254) not null,
  constraint pk_security_roles primary key (role_id)
);

create table security_user  (
  login_id             decimal(18,0) not null,
  user_id              varchar(50)   not null,
  password             varchar(50)   not null,
  constraint pk_security_user primary key (login_id)
);

create unique index security_user_i2 on security_user (
        user_id asc
);


create table user_group_xref  (
  user_group_id        decimal(18,0) not null,
  login_id             decimal(18,0),
  group_id             decimal(18,0),
  constraint pk_user_group_xref primary key (user_group_id)
);


create unique index user_grp_xref_i2 on user_group_xref (
        login_id asc,
        group_id asc
);

create table user_role_xref  (
  user_role_id         decimal(18,0) not null,
  login_id             decimal(18,0),
  role_id              decimal(18,0),
  constraint pk_user_role_xref primary key (user_role_id)
);

create unique index user_role_xref_i2 on user_role_xref (
        login_id asc,
        role_id  asc
);

alter table security_perms
   add constraint fk_security_perm foreign key (role_id)
      references security_roles (role_id) on delete cascade;

alter table user_group_xref
   add constraint fk_user_grp_xref1 foreign key (login_id)
      references security_user (login_id) on delete cascade;

alter table user_group_xref
   add constraint fk_user_grp_xref2 foreign key (group_id)
      references security_groups (group_id) on delete cascade;

alter table user_role_xref
   add constraint fk_user_role_xref1 foreign key (login_id)
      references security_user (login_id) on delete cascade;

alter table user_role_xref
   add constraint fk_user_role_xref2 foreign key (role_id)
      references security_roles (role_id) on delete cascade;