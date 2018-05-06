# Database 'tcs_catalog';

INSERT INTO tcs_catalog:'informix'.project_type_lu(project_type_id, name, description, create_user, create_date, modify_user, modify_date) VALUES(1, 'project type 1', 'project type 1', 'admin', CURRENT, 'admin', CURRENT);

INSERT INTO tcs_catalog:'informix'.project_status_lu(project_status_id, name, description, create_user, create_date, modify_user, modify_date) VALUES(1, 'project status 1', 'project status 1', 'admin', CURRENT, 'admin', CURRENT);
INSERT INTO tcs_catalog:'informix'.project_status_lu(project_status_id, name, description, create_user, create_date, modify_user, modify_date) VALUES(7, 'project status 7', 'project status 7', 'admin', CURRENT, 'admin', CURRENT);

INSERT INTO tcs_catalog:'informix'.project_category_lu(project_category_id, project_type_id, name, description, create_user, create_date, modify_user, modify_date) VALUES(1, 1, 'project category 1', 'project category 1', 'admin', CURRENT, 'admin', CURRENT);

INSERT INTO tcs_catalog:'informix'.phase_type_lu(phase_type_id, name, description, create_user, create_date, modify_user, modify_date) VALUES(2, 'phase type 2', 'phase type 2', 'admin', CURRENT, 'admin', CURRENT);

INSERT INTO tcs_catalog:'informix'.phase_status_lu(phase_status_id, name, description, create_user, create_date, modify_user, modify_date) VALUES(3, 'phase status 3', 'phase status 3', 'admin', CURRENT, 'admin', CURRENT);

INSERT INTO tcs_catalog:'informix'.submission_type_lu(submission_type_id, name, description, create_user, create_date, modify_user, modify_date) VALUES(1, 'submission type 1', 'submission type 1', 'admin', CURRENT, 'admin', CURRENT);

INSERT INTO tcs_catalog:'informix'.resource_info_type_lu(resource_info_type_id, name, description, create_user, create_date, modify_user, modify_date) VALUES(1, 'resource info type 1', 'resource info type 1', 'admin', CURRENT, 'admin', CURRENT);
INSERT INTO tcs_catalog:'informix'.resource_info_type_lu(resource_info_type_id, name, description, create_user, create_date, modify_user, modify_date) VALUES(2, 'resource info type 2', 'resource info type 1', 'admin', CURRENT, 'admin', CURRENT);

INSERT INTO tcs_catalog:'informix'.resource_role_lu(resource_role_id, phase_type_id, name, description, create_user, create_date, modify_user, modify_date) VALUES(3, 2, 'resource role 3', 'resource role 3', 'admin', CURRENT, 'admin', CURRENT);
INSERT INTO tcs_catalog:'informix'.resource_role_lu(resource_role_id, phase_type_id, name, description, create_user, create_date, modify_user, modify_date) VALUES(4, 2, 'resource role 4', 'resource role 4', 'admin', CURRENT, 'admin', CURRENT);
INSERT INTO tcs_catalog:'informix'.resource_role_lu(resource_role_id, phase_type_id, name, description, create_user, create_date, modify_user, modify_date) VALUES(13, 2, 'resource role 13', 'resource role 13', 'admin', CURRENT, 'admin', CURRENT);

INSERT INTO tcs_catalog:'informix'.deliverable_lu(deliverable_id, phase_type_id, resource_role_id, name, description, required, create_user, create_date, modify_user, modify_date) VALUES(3, 2, 3, 'deliverable 3', 'deliverable 3', 1, 'admin', CURRENT, 'admin', CURRENT);
INSERT INTO tcs_catalog:'informix'.deliverable_lu(deliverable_id, phase_type_id, resource_role_id, name, description, required, create_user, create_date, modify_user, modify_date) VALUES(4, 2, 3, 'deliverable 4', 'deliverable 4', 1, 'admin', CURRENT, 'admin', CURRENT);

INSERT INTO tcs_catalog:'informix'.project(project_id, project_status_id, project_category_id, create_user, create_date, modify_user, modify_date, tc_direct_project_id) VALUES(100000, 7, 1, 'admin', CURRENT, 'admin', CURRENT, 1);
INSERT INTO tcs_catalog:'informix'.project(project_id, project_status_id, project_category_id, create_user, create_date, modify_user, modify_date, tc_direct_project_id) VALUES(100001, 1, 1, 'admin', CURRENT, 'admin', CURRENT, 2);

INSERT INTO tcs_catalog:'informix'.project_phase(project_phase_id, project_id, phase_type_id, phase_status_id, scheduled_start_time, scheduled_end_time, actual_end_time, duration, create_user, create_date, modify_user, modify_date) VALUES(101, 100000, 2, 3, TO_DATE('2010-11-22 09:05:00', '%Y-%m-%d %H:%M:%S'), TO_DATE('2010-11-22 09:05:00', '%Y-%m-%d %H:%M:%S'), TO_DATE('2010-11-23 09:05:00', '%Y-%m-%d %H:%M:%S'), 4, 'admin', CURRENT, 'admin', CURRENT);
INSERT INTO tcs_catalog:'informix'.project_phase(project_phase_id, project_id, phase_type_id, phase_status_id, scheduled_start_time, scheduled_end_time, actual_end_time, duration, create_user, create_date, modify_user, modify_date) VALUES(102, 100001, 2, 3, TO_DATE('2010-11-25 12:00:00', '%Y-%m-%d %H:%M:%S'), TO_DATE('2010-11-25 12:00:00', '%Y-%m-%d %H:%M:%S'), TO_DATE('2010-11-26 12:00:00', '%Y-%m-%d %H:%M:%S'), 4, 'admin', CURRENT, 'admin', CURRENT);

INSERT INTO tcs_catalog:'informix'.resource(resource_id, resource_role_id, project_id, project_phase_id, create_user, create_date, modify_user, modify_date) VALUES(1001, 4, 100000, 101, 'admin', CURRENT, 'admin', CURRENT);
INSERT INTO tcs_catalog:'informix'.resource(resource_id, resource_role_id, project_id, project_phase_id, create_user, create_date, modify_user, modify_date) VALUES(1002, 3, 100001, 102, 'admin', CURRENT, 'admin', CURRENT);
INSERT INTO tcs_catalog:'informix'.resource(resource_id, resource_role_id, project_id, project_phase_id, create_user, create_date, modify_user, modify_date) VALUES(1003, 13, 100000, NULL, 'admin', CURRENT, 'admin', CURRENT);

INSERT INTO tcs_catalog:'informix'.resource_info(resource_id, resource_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES(1001, 1, 1, 'admin', CURRENT, 'admin', CURRENT);
INSERT INTO tcs_catalog:'informix'.resource_info(resource_id, resource_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES(1002, 1, 2, 'admin', CURRENT, 'admin', CURRENT);
INSERT INTO tcs_catalog:'informix'.resource_info(resource_id, resource_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES(1003, 1, 3, 'admin', CURRENT, 'admin', CURRENT);

INSERT INTO tcs_catalog:'informix'.resource_info(resource_id, resource_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES(1001, 2, 'user1', 'admin', CURRENT, 'admin', CURRENT);
INSERT INTO tcs_catalog:'informix'.resource_info(resource_id, resource_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES(1002, 2, 'user2', 'admin', CURRENT, 'admin', CURRENT);
INSERT INTO tcs_catalog:'informix'.resource_info(resource_id, resource_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES(1003, 2, 'user2', 'admin', CURRENT, 'admin', CURRENT);

INSERT INTO tcs_catalog:'informix'.late_deliverable_type_lu (late_deliverable_type_id, name, description) VALUES(1, 'Missed Deadline', 'Missed Deadline');
INSERT INTO tcs_catalog:'informix'.late_deliverable_type_lu (late_deliverable_type_id, name, description) VALUES(2, 'Rejected Final Fix', 'Rejected Final Fix');


INSERT INTO tcs_catalog:'informix'.late_deliverable(late_deliverable_id, late_deliverable_type_id, project_phase_id, resource_id, deliverable_id, deadline, create_date, forgive_ind, last_notified) VALUES(1, 1, 101, 1001, 4, TO_DATE('2010-11-22 09:05:00', '%Y-%m-%d %H:%M:%S'), TO_DATE('2010-11-20 09:05:00', '%Y-%m-%d %H:%M:%S'), 0, TO_DATE('2010-11-20 09:05:00', '%Y-%m-%d %H:%M:%S'));
INSERT INTO tcs_catalog:'informix'.late_deliverable(late_deliverable_id, late_deliverable_type_id, project_phase_id, resource_id, deliverable_id, deadline, create_date, forgive_ind, last_notified) VALUES(2, 1, 102, 1002, 3, TO_DATE('2010-11-25 12:00:00', '%Y-%m-%d %H:%M:%S'), TO_DATE('2010-11-23 12:00:00', '%Y-%m-%d %H:%M:%S'), 0, TO_DATE('2010-11-23 12:00:00', '%Y-%m-%d %H:%M:%S'));

# Database 'corporate_oltp';
INSERT INTO corporate_oltp:'informix'.tc_direct_project(project_id, name, user_id, create_date) VALUES(1, 'Project 1', 3, CURRENT);
INSERT INTO corporate_oltp:'informix'.tc_direct_project(project_id, name, user_id, create_date) VALUES(2, 'Project 2', 3, CURRENT);

INSERT INTO corporate_oltp:'informix'.user_permission_grant(user_permission_grant_id, user_id, resource_id, permission_type_id) VALUES(1, 3, 1, 1);
INSERT INTO corporate_oltp:'informix'.user_permission_grant(user_permission_grant_id, user_id, resource_id, permission_type_id) VALUES(2, 3, 2, 1);