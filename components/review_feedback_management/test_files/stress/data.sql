insert into project( project_id,project_status_id ,project_category_id ,project_studio_spec_id,create_user,create_date,modify_user,modify_date,tc_direct_project_id) values (1,1,1,1,'user1',CURRENT,'modifyuser1',CURRENT,1);
insert into audit_action_type_lu(audit_action_type_id, name, description, create_user, create_date, modify_user, modify_date) values (1,'type 1','type 1','user1',CURRENT,'user2',CURRENT);
insert into audit_action_type_lu(audit_action_type_id, name, description, create_user, create_date, modify_user, modify_date) values (2,'type 2','type 2','user1',CURRENT,'user2',CURRENT);
insert into audit_action_type_lu(audit_action_type_id, name, description, create_user, create_date, modify_user, modify_date) values (3,'type 3','type 3','user1',CURRENT,'user2',CURRENT);

