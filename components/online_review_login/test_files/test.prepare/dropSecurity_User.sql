use orl;

alter table user_group_xref drop foreign key fk_user_grp_xref1;

alter table user_role_xref drop foreign key fk_user_role_xref1;

drop index security_user_i2 on security_user;

drop table if exists security_user;