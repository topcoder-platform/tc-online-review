insert into project(project_id, project_status_id, project_category_id,create_user, create_date, modify_user, modify_date) values (1, 1, 1, 'user', '2010-09-15 00:22:50.312', 'user', '2010-09-15 00:22:50.312');
insert into project(project_id, project_status_id, project_category_id,create_user, create_date, modify_user, modify_date) values (2, 1, 1, 'user', '2010-09-15 00:22:50.312', 'user', '2010-09-15 00:22:50.312');

insert into comp_catalog(component_id, current_version, component_name,description, create_time, status_id) values (1, 1, 'Online Review Phases', 'Online Review Phases', '2010-09-15 00:22:50.312', 1);
insert into comp_catalog(component_id, current_version, component_name,description, create_time, status_id) values (2, 1, 'Online Review Phases 2', 'Online Review Phases 2', '2010-09-15 00:22:50.312', 1);

insert into comp_versions(comp_vers_id, component_id, version,version_text,create_time, phase_id, phase_time, price, comments) values (1, 1, 1, '1.0', '2010-09-15 00:22:50.312', 112, '2010-09-15 00:22:50.312', 500, 'Comments');
insert into comp_versions(comp_vers_id, component_id, version,version_text,create_time, phase_id, phase_time, price, comments) values (2, 2, 1, '1.0', '2010-09-15 00:22:50.312', 113, '2010-09-15 00:22:50.312', 500, 'Comments');

insert into project_info(project_id, project_info_type_id, value,create_user, create_date, modify_user, modify_date) values (1, 6, 'Late Deliverable Tracker', 'user', '2010-09-15 00:22:50.312', 'user', '2010-09-15 00:22:50.312');
insert into project_info(project_id, project_info_type_id, value,create_user, create_date, modify_user, modify_date) values (1, 7, '1.0.0', 'user', '2010-09-15 00:22:50.312', 'user', '2010-09-15 00:22:50.312');
insert into project_info(project_id, project_info_type_id, value,create_user, create_date, modify_user, modify_date) values (1, 45, 'true', 'user', '2010-09-15 00:22:50.312', 'user', '2010-09-15 00:22:50.312');

insert into project_info(project_id, project_info_type_id, value,create_user, create_date, modify_user, modify_date) values (2, 6, 'Late Deliverable Tracker', 'user', '2010-09-15 00:22:50.312', 'user', '2010-09-15 00:22:50.312');
insert into project_info(project_id, project_info_type_id, value,create_user, create_date, modify_user, modify_date) values (2, 7, '1.0.0', 'user', '2010-09-15 00:22:50.312', 'user', '2010-09-15 00:22:50.312');
insert into project_info(project_id, project_info_type_id, value,create_user, create_date, modify_user, modify_date) values (2, 45, 'true', 'user', '2010-09-15 00:22:50.312', 'user', '2010-09-15 00:22:50.312');

insert into project_phase(project_phase_id, project_id, phase_type_id, phase_status_id,scheduled_start_time, scheduled_end_time, duration, create_user, create_date, modify_user, modify_date)values (112, 1, 4, 2, '2010-09-12 00:22:50.312', '2010-09-13 00:22:50.312', 86400000, 'user', '2010-09-12 00:22:50.312', 'user', '2010-09-13 00:22:50.312');
insert into project_phase(project_phase_id, project_id, phase_type_id, phase_status_id,scheduled_start_time, scheduled_end_time, duration, create_user, create_date, modify_user, modify_date)values (113, 2, 9, 2, '2010-09-12 00:22:50.312', '2010-09-13 00:22:50.312', 86400000, 'user', '2010-09-12 00:22:50.312', 'user', '2010-09-13 00:22:50.312');
insert into project_phase(project_phase_id, project_id, phase_type_id, phase_status_id,scheduled_start_time, scheduled_end_time, duration, create_user, create_date, modify_user, modify_date)values (114, 2, 10, 3, '2010-09-12 00:22:50.312', '2010-09-13 00:22:50.312', 86400000, 'user', '2010-09-12 00:22:50.312', 'user', '2010-09-13 00:22:50.312');
insert into project_phase(project_phase_id, project_id, phase_type_id, phase_status_id,scheduled_start_time, scheduled_end_time, duration, create_user, create_date, modify_user, modify_date)values (115, 2, 9, 3, '2010-09-10 00:22:50.312', '2010-09-11 00:22:50.312', 86400000, 'user', '2010-09-12 00:22:50.312', 'user', '2010-09-13 00:22:50.312');

INSERT INTO resource (resource_id, resource_role_id, project_id, project_phase_id,create_user, create_date, modify_user, modify_date) VALUES (1, 4, 1, 112, 'user', '2010-09-15 00:22:50.312', 'user', '2010-09-15 00:22:50.312');
INSERT INTO resource (resource_id, resource_role_id, project_id, project_phase_id,create_user, create_date, modify_user, modify_date) VALUES (2, 1, 2, 113, 'user', '2010-09-15 00:22:50.312', 'user', '2010-09-15 00:22:50.312');
INSERT INTO resource (resource_id, resource_role_id, project_id, project_phase_id,create_user, create_date, modify_user, modify_date) VALUES (3, 9, 2, 113, 'user', '2010-09-15 00:22:50.312', 'user', '2010-09-15 00:22:50.312');
INSERT INTO resource (resource_id, resource_role_id, project_id, project_phase_id,create_user, create_date, modify_user, modify_date) VALUES (4, 1, 2, 115, 'user', '2010-09-15 00:22:50.312', 'user', '2010-09-15 00:22:50.312');
INSERT INTO resource (resource_id, resource_role_id, project_id, project_phase_id,create_user, create_date, modify_user, modify_date) VALUES (5, 9, 2, 115, 'user', '2010-09-15 00:22:50.312', 'user', '2010-09-15 00:22:50.312');

insert into resource_info(resource_id, resource_info_type_id, value,create_user, create_date, modify_user, modify_date) VALUES (1, 1, 1, 'user', '2010-09-15 00:22:50.312', 'user', '2010-09-15 00:22:50.312');
insert into resource_info(resource_id, resource_info_type_id, value,create_user, create_date, modify_user, modify_date) VALUES (2, 1, 1, 'user', '2010-09-15 00:22:50.312', 'user', '2010-09-15 00:22:50.312');

insert into user(user_id, first_name, last_name, handle, status)values (1, 'abc', 'xyz', 'wishingbone', 'ON');
insert into user(user_id, first_name, last_name, handle, status)values (2, 'efg', 'uvw', 'xxx', 'ON');

insert into user_rating(user_id, phase_id) values (1, 112);
insert into user_rating(user_id, phase_id) values (2, 113);

insert into email(user_id, email_id, address, primary_ind) values (1, 1, 'wuyb@topcoder.com', 1);
insert into email(user_id, email_id, address, primary_ind) values (2, 2, 'test2@topcoder.com', 1);

insert into phase_dependency(dependency_phase_id, dependent_phase_id, dependency_start, dependent_start, lag_time,create_user, create_date, modify_user, modify_date) values(114, 113, 1, 1, 86400000, 'user', '2010-09-15 00:22:50.312', 'user', '2010-09-15 00:22:50.312');
insert into phase_dependency(dependency_phase_id, dependent_phase_id, dependency_start, dependent_start, lag_time,create_user, create_date, modify_user, modify_date) values(115, 114, 1, 1, 86400000, 'user', '2010-09-15 00:22:50.312', 'user', '2010-09-15 00:22:50.312');