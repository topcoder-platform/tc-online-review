--==============================================================
-- Component:  Security Manager
--==============================================================

drop table if exists GROUP_ROLE_XREF;

drop table if exists KEY_GENERATION;

drop table if exists SECURITY_GROUPS;

drop table if exists SECURITY_PERMS;

drop table if exists SECURITY_ROLES;

drop table if exists SECURITY_USER;

drop table if exists USER_GROUP_XREF;

drop table if exists USER_ROLE_XREF;

--==============================================================
-- Table: GROUP_ROLE_XREF
--==============================================================
create table GROUP_ROLE_XREF  (
  GROUP_ROLE_ID        DECIMAL(18,0) NOT NULL,
  GROUP_ID             DECIMAL(18,0),
  ROLE_ID              DECIMAL(18,0),
  constraint PK_GROUP_ROLE_XREF primary key (GROUP_ROLE_ID)
);

--==============================================================
-- Index: GROUP_ROLE_XREF_I2
--==============================================================
-- isv: Seems that MySQL does not support the cluster indexes
create unique index GROUP_ROLE_XREF_I2 on GROUP_ROLE_XREF (
        GROUP_ID asc, ROLE_ID asc
);

--==============================================================
-- Table: KEY_GENERATION
--==============================================================
create table KEY_GENERATION  (
  USER_DEF             CHAR(18) not null,
  HIGH_VALUE           DECIMAL(13,0)  not null,
  constraint PK_KEY_GENERATION primary key (USER_DEF)      
);

--==============================================================
-- Table: SECURITY_GROUPS
--==============================================================
create table SECURITY_GROUPS  (
  GROUP_ID             DECIMAL(18,0) not null,
  DESCRIPTION          VARCHAR(254)  not null,
  constraint PK_SECURITY_GROUPS primary key (GROUP_ID)      
);

--==============================================================
-- Table: SECURITY_PERMS
--==============================================================
create table SECURITY_PERMS  (
  ROLE_ID              DECIMAL(18,0) not null,
  PERMISSION           VARCHAR(254)  not null,
  constraint PK_SECURITY_PERMS primary key (ROLE_ID, PERMISSION)
);

--==============================================================
-- Table: SECURITY_ROLES
--==============================================================
create table SECURITY_ROLES  (
  ROLE_ID              DECIMAL(18,0) not null,
  DESCRIPTION          VARCHAR(254) not null,
  constraint PK_SECURITY_ROLES primary key (ROLE_ID)
);

--==============================================================
-- Table: SECURITY_USER
--==============================================================
create table SECURITY_USER  (
  LOGIN_ID             DECIMAL(18,0) not null,
  USER_ID              VARCHAR(50)   not null,
  PASSWORD             VARCHAR(50)   not null,
  constraint PK_SECURITY_USER primary key (LOGIN_ID)
);

--==============================================================
-- Index: SECURITY_USER_I2
--==============================================================
create unique index SECURITY_USER_I2 on SECURITY_USER (
        USER_ID asc
);

--==============================================================
-- Table: USER_GROUP_XREF
--==============================================================
create table USER_GROUP_XREF  (
  USER_GROUP_ID        DECIMAL(18,0) not null,
  LOGIN_ID             DECIMAL(18,0),
  GROUP_ID             DECIMAL(18,0),
  constraint PK_USER_GROUP_XREF primary key (USER_GROUP_ID)
);

--==============================================================
-- Index: USER_GRP_XREF_I2
--==============================================================
-- isv: Seems that MySQL does not support the cluster indexes
create unique index USER_GRP_XREF_I2 on USER_GROUP_XREF (
        LOGIN_ID asc,
        GROUP_ID asc
);

--==============================================================
-- Table: USER_ROLE_XREF
--==============================================================
create table USER_ROLE_XREF  (
  USER_ROLE_ID         DECIMAL(18,0) not null,
  LOGIN_ID             DECIMAL(18,0),
  ROLE_ID              DECIMAL(18,0),
  constraint PK_USER_ROLE_XREF primary key (USER_ROLE_ID)
);

--==============================================================
-- Index: USER_ROLE_XREF_I2
--==============================================================
-- isv: Seems that MySQL does not support the cluster indexes
create unique index USER_ROLE_XREF_I2 on USER_ROLE_XREF (
        LOGIN_ID asc,
        ROLE_ID  asc
);

alter table SECURITY_PERMS
   add constraint FK_SECURITY_PERM foreign key (ROLE_ID)
      references SECURITY_ROLES (ROLE_ID) on delete cascade;

alter table USER_GROUP_XREF
   add constraint FK_USER_GRP_XREF1 foreign key (LOGIN_ID)
      references SECURITY_USER (LOGIN_ID) on delete cascade;

alter table USER_GROUP_XREF
   add constraint FK_USER_GRP_XREF2 foreign key (GROUP_ID)
      references SECURITY_GROUPS (GROUP_ID) on delete cascade;

alter table USER_ROLE_XREF
   add constraint FK_USER_ROLE_XREF1 foreign key (LOGIN_ID)
      references SECURITY_USER (LOGIN_ID) on delete cascade;

alter table USER_ROLE_XREF
   add constraint FK_USER_ROLE_XREF2 foreign key (ROLE_ID)
      references SECURITY_ROLES (ROLE_ID) on delete cascade;