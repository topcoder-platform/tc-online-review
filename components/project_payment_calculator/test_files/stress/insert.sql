INSERT INTO 'informix'.phase_type_lu(phase_type_id,name,description,create_user,create_date,modify_user,modify_date) VALUES (1, 'Registration', 'Registration', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000');
INSERT INTO 'informix'.phase_type_lu(phase_type_id,name,description,create_user,create_date,modify_user,modify_date) VALUES (2, 'Submission', 'Submission', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000');
INSERT INTO 'informix'.phase_type_lu(phase_type_id,name,description,create_user,create_date,modify_user,modify_date) VALUES (3, 'Screening', 'Screening', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000');
INSERT INTO 'informix'.phase_type_lu(phase_type_id,name,description,create_user,create_date,modify_user,modify_date) VALUES (4, 'Review', 'Review', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000');
INSERT INTO 'informix'.phase_type_lu(phase_type_id,name,description,create_user,create_date,modify_user,modify_date) VALUES (5, 'Appeals', 'Appeals', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000');
INSERT INTO 'informix'.phase_type_lu(phase_type_id,name,description,create_user,create_date,modify_user,modify_date) VALUES (6, 'Appeals Response', 'Appeals Response', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000');
INSERT INTO 'informix'.phase_type_lu(phase_type_id,name,description,create_user,create_date,modify_user,modify_date) VALUES (7, 'Aggregation', 'Aggregation', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000');
INSERT INTO 'informix'.phase_type_lu(phase_type_id,name,description,create_user,create_date,modify_user,modify_date) VALUES (8, 'Aggregation Review', 'Aggregation Review', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000');
INSERT INTO 'informix'.phase_type_lu(phase_type_id,name,description,create_user,create_date,modify_user,modify_date) VALUES (9, 'Final Fix', 'Final Fix', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000');
INSERT INTO 'informix'.phase_type_lu(phase_type_id,name,description,create_user,create_date,modify_user,modify_date) VALUES (10, 'Final Review', 'Final Review', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000');
INSERT INTO 'informix'.phase_type_lu(phase_type_id,name,description,create_user,create_date,modify_user,modify_date) VALUES (11, 'Approval', 'Approval', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000');
INSERT INTO 'informix'.phase_type_lu(phase_type_id,name,description,create_user,create_date,modify_user,modify_date) VALUES (12, 'Post-Mortem', 'Post-Mortem', 'System', current, 'System', current);
INSERT INTO 'informix'.phase_type_lu(phase_type_id,name,description,create_user,create_date,modify_user,modify_date) VALUES (13, 'Specification Submission', 'Specification Submission', 'System', current, 'System', current);
INSERT INTO 'informix'.phase_type_lu(phase_type_id,name,description,create_user,create_date,modify_user,modify_date) VALUES (14, 'Specification Review', 'Specification Review', 'System', current, 'System', current);
INSERT INTO 'informix'.phase_type_lu(phase_type_id,name,description,create_user,create_date,modify_user,modify_date) VALUES (15, 'Milestone Submission', 'Milestone Submission', 'System', current, 'System', current);
INSERT INTO 'informix'.phase_type_lu(phase_type_id,name,description,create_user,create_date,modify_user,modify_date) VALUES (16, 'Milestone Screening', 'Milestone Screening', 'System', current, 'System', current);
INSERT INTO 'informix'.phase_type_lu(phase_type_id,name,description,create_user,create_date,modify_user,modify_date) VALUES (17, 'Milestone Review', 'Milestone Review', 'System', current, 'System', current);

INSERT INTO 'informix'.phase_status_lu(phase_status_id,name,description,create_user,create_date,modify_user,modify_date) VALUES (2, 'Open', 'Open', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000');

INSERT INTO 'informix'.resource_role_lu(resource_role_id,phase_type_id,name,description,create_user,create_date,modify_user,modify_date) VALUES (1, NULL, 'Submitter', 'Submitter', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000');
INSERT INTO 'informix'.resource_role_lu(resource_role_id,phase_type_id,name,description,create_user,create_date,modify_user,modify_date) VALUES (2, 3, 'Primary Screener', 'Primary Screener', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000');
INSERT INTO 'informix'.resource_role_lu(resource_role_id,phase_type_id,name,description,create_user,create_date,modify_user,modify_date) VALUES (3, 3, 'Screener', 'Screener', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000');
INSERT INTO 'informix'.resource_role_lu(resource_role_id,phase_type_id,name,description,create_user,create_date,modify_user,modify_date) VALUES (4, 4, 'Reviewer', 'Reviewer', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000');
INSERT INTO 'informix'.resource_role_lu(resource_role_id,phase_type_id,name,description,create_user,create_date,modify_user,modify_date) VALUES (5, 4, 'Accuracy Reviewer', 'Accuracy Reviewer', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000');
INSERT INTO 'informix'.resource_role_lu(resource_role_id,phase_type_id,name,description,create_user,create_date,modify_user,modify_date) VALUES (6, 4, 'Failure Reviewer', 'Failure Reviewer', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000');
INSERT INTO 'informix'.resource_role_lu(resource_role_id,phase_type_id,name,description,create_user,create_date,modify_user,modify_date) VALUES (7, 4, 'Stress Reviewer', 'Stress Reviewer', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000');
INSERT INTO 'informix'.resource_role_lu(resource_role_id,phase_type_id,name,description,create_user,create_date,modify_user,modify_date) VALUES (8, 7, 'Aggregator', 'Aggregator', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000');
INSERT INTO 'informix'.resource_role_lu(resource_role_id,phase_type_id,name,description,create_user,create_date,modify_user,modify_date) VALUES (9, 10, 'Final Reviewer', 'Final Reviewer', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000');
INSERT INTO 'informix'.resource_role_lu(resource_role_id,phase_type_id,name,description,create_user,create_date,modify_user,modify_date) VALUES (10, NULL, 'Approver', 'Approver', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000');
INSERT INTO 'informix'.resource_role_lu(resource_role_id,phase_type_id,name,description,create_user,create_date,modify_user,modify_date) VALUES (11, NULL, 'Designer', 'Designer', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000');
INSERT INTO 'informix'.resource_role_lu(resource_role_id,phase_type_id,name,description,create_user,create_date,modify_user,modify_date) VALUES (12, NULL, 'Observer', 'Observer', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000');
INSERT INTO 'informix'.resource_role_lu(resource_role_id,phase_type_id,name,description,create_user,create_date,modify_user,modify_date) VALUES (13, NULL, 'Manager', 'Manager', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000');
INSERT INTO 'informix'.resource_role_lu(resource_role_id,phase_type_id,name,description,create_user,create_date,modify_user,modify_date) VALUES (14, NULL, 'Copilot', 'Copilot', 'System', '2010-01-13 08:54:35.000', 'System', '2010-01-13 08:54:35.000');
INSERT INTO 'informix'.resource_role_lu(resource_role_id,phase_type_id,name,description,create_user,create_date,modify_user,modify_date) VALUES (15, NULL, 'Client Manager', 'Client Manager', 'System', '2010-01-13 08:54:35.000', 'System', '2010-01-13 08:54:35.000');
INSERT INTO 'informix'.resource_role_lu(resource_role_id,phase_type_id,name,description,create_user,create_date,modify_user,modify_date) VALUES (16, NULL, 'Post-Mortem Reviewer', 'Post-Mortem Reviewer', 'System', current, 'System', current);
INSERT INTO 'informix'.resource_role_lu(resource_role_id,phase_type_id,name,description,create_user,create_date,modify_user,modify_date) VALUES (17, NULL, 'Specification Submitter', 'Specification Submitter', 'System', current, 'System', current);
INSERT INTO 'informix'.resource_role_lu(resource_role_id,phase_type_id,name,description,create_user,create_date,modify_user,modify_date) VALUES (18, 14, 'Specification Reviewer', 'Specification Reviewer', 'System', current, 'System', current);
INSERT INTO 'informix'.resource_role_lu(resource_role_id,phase_type_id,name,description,create_user,create_date,modify_user,modify_date) VALUES (1001, NULL, 'Team Captain', 'Team Captain', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000');
INSERT INTO 'informix'.resource_role_lu(resource_role_id,phase_type_id,name,description,create_user,create_date,modify_user,modify_date) VALUES (1002, NULL, 'Free Agent', 'Free Agent', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000');
INSERT INTO 'informix'.resource_role_lu(resource_role_id,phase_type_id,name,description,create_user,create_date,modify_user,modify_date) VALUES (1003, NULL, 'Payment Manager', 'Payment Manager', 'System', '2009-03-16 20:27:00.000', 'System', '2009-03-16 20:27:00.000');
INSERT INTO 'informix'.resource_role_lu(resource_role_id,phase_type_id,name,description,create_user,create_date,modify_user,modify_date) VALUES (86, NULL, 'Deactivated', 'Deactivated', 'System', '2008-12-04 14:51:25.000', 'System', '2008-12-04 14:51:25.000');
INSERT INTO 'informix'.resource_role_lu(resource_role_id,phase_type_id,name,description,create_user,create_date,modify_user,modify_date) VALUES (19, 16, 'Milestone Screener', 'Milestone Screener', 'System', '2010-12-04 14:51:25.000', 'System', '2010-12-04 14:51:25.000');
INSERT INTO 'informix'.resource_role_lu(resource_role_id,phase_type_id,name,description,create_user,create_date,modify_user,modify_date) VALUES (20, 17, 'Milestone Reviewer', 'Milestone Reviewer', 'System', '2010-12-04 14:51:25.000', 'System', '2010-12-04 14:51:25.000');

INSERT INTO 'informix'.project_type_lu(project_type_id,name,description,is_generic,create_user,create_date,modify_user,modify_date) VALUES (1, 'Component', 'Component', 'f', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000');

INSERT INTO 'informix'.project_category_lu(project_category_id,project_type_id,name,description,create_user,create_date,modify_user,modify_date, display, display_order) VALUES (1, 1, 'Design', 'Design', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000', 't', 15);
INSERT INTO 'informix'.project_category_lu(project_category_id,project_type_id,name,description,create_user,create_date,modify_user,modify_date, display, display_order) VALUES (2, 1, 'Development', 'Development', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000', 't', 16);
INSERT INTO 'informix'.project_category_lu(project_category_id,project_type_id,name,description,create_user,create_date,modify_user,modify_date, display, display_order) VALUES (6, 1, 'Security', 'Security', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000', null, null);
INSERT INTO 'informix'.project_category_lu(project_category_id,project_type_id,name,description,create_user,create_date,modify_user,modify_date, display, display_order) VALUES (7, 1, 'Process', 'Process', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000', null, null);
INSERT INTO 'informix'.project_category_lu(project_category_id,project_type_id,name,description,create_user,create_date,modify_user,modify_date, display, display_order) VALUES (9, 1, 'Bug Hunt', 'Bug Hunt', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000', null, null);
INSERT INTO 'informix'.project_category_lu(project_category_id,project_type_id,name,description,create_user,create_date,modify_user,modify_date, display, display_order) VALUES (13, 1, 'Test Suites', 'Test Suites', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000', null, null);
INSERT INTO 'informix'.project_category_lu(project_category_id,project_type_id,name,description,create_user,create_date,modify_user,modify_date, display, display_order) VALUES (14, 1, 'Assembly', 'Assembly', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000', null, null);
INSERT INTO 'informix'.project_category_lu(project_category_id,project_type_id,name,description,create_user,create_date,modify_user,modify_date, display, display_order) VALUES (19, 1, 'UI Prototype', 'UI Prototype', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000', null, null);
INSERT INTO 'informix'.project_category_lu(project_category_id,project_type_id,name,description,create_user,create_date,modify_user,modify_date, display, display_order) VALUES (23, 1, 'Conceptualization', 'Conceptualization', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000', null, null);
INSERT INTO 'informix'.project_category_lu(project_category_id,project_type_id,name,description,create_user,create_date,modify_user,modify_date, display, display_order) VALUES (24, 1, 'RIA Build', 'RIA Build', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000', null, null);
INSERT INTO 'informix'.project_category_lu(project_category_id,project_type_id,name,description,create_user,create_date,modify_user,modify_date, display, display_order) VALUES (26, 1, 'Test Scenarios', 'Test Scenarios', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000', null, null);
INSERT INTO 'informix'.project_category_lu(project_category_id,project_type_id,name,description,create_user,create_date,modify_user,modify_date, display, display_order) VALUES (35, 1, 'RIA Build', 'RIA Build', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000', null, null);
INSERT INTO 'informix'.project_category_lu(project_category_id,project_type_id,name,description,create_user,create_date,modify_user,modify_date, display, display_order) VALUES (37, 1, 'Marathon Match', 'Marathon Match', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000', null, null);
INSERT INTO 'informix'.project_category_lu(project_category_id,project_type_id,name,description,create_user,create_date,modify_user,modify_date, display, display_order) VALUES (16, 1, 'Banners/Icons', 'Banners/Icons', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000', null, null);
INSERT INTO 'informix'.project_category_lu(project_category_id,project_type_id,name,description,create_user,create_date,modify_user,modify_date, display, display_order) VALUES (17, 1, 'Web Design', 'Web Design', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000', null, null);
INSERT INTO 'informix'.project_category_lu(project_category_id,project_type_id,name,description,create_user,create_date,modify_user,modify_date, display, display_order) VALUES (18, 1, 'Wireframes', 'Wireframes', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000', null, null);
INSERT INTO 'informix'.project_category_lu(project_category_id,project_type_id,name,description,create_user,create_date,modify_user,modify_date, display, display_order) VALUES (20, 1, 'Logo Design', 'Logo Design', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000', null, null);
INSERT INTO 'informix'.project_category_lu(project_category_id,project_type_id,name,description,create_user,create_date,modify_user,modify_date, display, display_order) VALUES (21, 1, 'Print/Presentation', 'Print/Presentation', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000', null, null);
INSERT INTO 'informix'.project_category_lu(project_category_id,project_type_id,name,description,create_user,create_date,modify_user,modify_date, display, display_order) VALUES (22, 1, 'Idea Generation', 'Idea Generation', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000', null, null);
INSERT INTO 'informix'.project_category_lu(project_category_id,project_type_id,name,description,create_user,create_date,modify_user,modify_date, display, display_order) VALUES (30, 1, 'Widget or Mobile Screen Design', 'Widget or Mobile Screen Design', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000', null, null);
INSERT INTO 'informix'.project_category_lu(project_category_id,project_type_id,name,description,create_user,create_date,modify_user,modify_date, display, display_order) VALUES (31, 1, 'Front-End Flash', 'Front-End Flash', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000', null, null);
INSERT INTO 'informix'.project_category_lu(project_category_id,project_type_id,name,description,create_user,create_date,modify_user,modify_date, display, display_order) VALUES (32, 1, 'Application Front-End Design', 'Application Front-End Design', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000', null, null);
INSERT INTO 'informix'.project_category_lu(project_category_id,project_type_id,name,description,create_user,create_date,modify_user,modify_date, display, display_order) VALUES (34, 1, 'Studio Other', 'Studio Other', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000', null, null);

INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (1, 2, 0.0, 0.0, 0.01);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (1, 4, 0.0, 0.12, 0.05);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (1, 8, 10.0, 0.12, 0.05);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (1, 9, 0.0, 0.05, 0.0);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (1, 18, 30.0, 0.0, 0.0);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (1, 14, 300.0, 0.0, 0.0);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (1, 1, 10.0, 1, 0.5);

INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (2, 2, 0.0, 0.0, 0.02);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (2, 4, 0.0, 0.2, 0.05);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (2, 5, 0.0, 0.2, 0.05);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (2, 6, 0.0, 0.2, 0.05);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (2, 7, 0.0, 0.2, 0.05);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (2, 8, 10.0, 0.0, 0.0);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (2, 9, 0.0, 0.05, 0.0);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (2, 18, 30.0, 0.0, 0.0);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (2, 14, 300.0, 0.0, 0.0);

INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (6, 2, 0.0, 0.0, 0.01);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (6, 4, 0.0, 0.08, 0.03);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (6, 8, 10.0, 0.0, 0.0);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (6, 9, 0.0, 0.03, 0.0);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (6, 18, 40.0, 0.0, 0.0);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (6, 14, 600.0, 0.0, 0.0);

INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (7, 2, 0.0, 0.0, 0.01);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (7, 4, 0.0, 0.12, 0.05);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (7, 8, 10.0, 0.0, 0.0);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (7, 9, 0.0, 0.05, 0.0);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (7, 18, 50.0, 0.0, 0.0);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (7, 14, 600.0, 0.0, 0.0);

INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (9, 18, 50.0, 0.0, 0.0);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (9, 14, 600.0, 0.0, 0.0);

INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (13, 2, 0.0, 0.0, 0.01);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (13, 4, 0.0, 0.12, 0.05);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (13, 8, 10.0, 0.0, 0.0);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (13, 9, 0.0, 0.03, 0.0);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (13, 18, 50.0, 0.0, 0.0);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (13, 14, 300.0, 0.0, 0.0);

INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (14, 2, 0.0, 0.0, 0.01);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (14, 4, 0.0, 0.13, 0.05);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (14, 8, 10.0, 0.0, 0.0);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (14, 9, 0.0, 0.05, 0.0);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (14, 18, 50.0, 0.0, 0.0);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (14, 14, 600.0, 0.0, 0.0);

INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (19, 2, 0.0, 0.0, 0.01);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (19, 4, 0.0, 0.08, 0.03);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (19, 8, 10.0, 0.0, 0.0);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (19, 9, 0.0, 0.03, 0.0);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (19, 18, 50.0, 0.0, 0.0);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (19, 14, 600.0, 0.0, 0.0);

INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (23, 2, 0.0, 0.0, 0.01);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (23, 4, 0.0, 0.12, 0.03);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (23, 8, 10.0, 0.0, 0.0);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (23, 9, 0.0, 0.05, 0.0);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (23, 18, 30.0, 0.0, 0.0);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (23, 14, 600.0, 0.0, 0.0);

INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (24, 2, 0.0, 0.0, 0.01);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (24, 4, 0.0, 0.08, 0.03);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (24, 8, 10.0, 0.0, 0.0);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (24, 9, 0.0, 0.03, 0.0);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (24, 18, 50.0, 0.0, 0.0);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (24, 14, 600.0, 0.0, 0.0);

INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (26, 2, 0.0, 0.0, 0.01);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (26, 4, 0.0, 0.12, 0.05);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (26, 8, 10.0, 0.0, 0.0);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (26, 9, 0.0, 0.03, 0.0);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (26, 18, 50.0, 0.0, 0.0);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (26, 14, 300.0, 0.0, 0.0);

INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (35, 2, 0.0, 0.0, 0.01);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (35, 4, 0.0, 0.08, 0.03);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (35, 8, 10.0, 0.0, 0.0);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (35, 9, 0.0, 0.03, 0.0);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (35, 18, 50.0, 0.0, 0.0);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (35, 14, 600.0, 0.0, 0.0);

INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (37, 14, 600.0, 0.0, 0.0);

INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (16, 2, 100.0, 0.0, 0.0);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (16, 18, 75.0, 0.0, 0.0);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (16, 14, 600.0, 0.0, 0.0);

INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (17, 2, 100.0, 0.0, 0.0);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (17, 18, 75.0, 0.0, 0.0);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (17, 14, 600.0, 0.0, 0.0);

INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (18, 2, 100.0, 0.0, 0.0);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (18, 18, 75.0, 0.0, 0.0);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (18, 14, 600.0, 0.0, 0.0);

INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (20, 2, 150.0, 0.0, 0.0);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (20, 18, 75.0, 0.0, 0.0);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (20, 14, 600.0, 0.0, 0.0);

INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (21, 2, 100.0, 0.0, 0.0);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (21, 18, 75.0, 0.0, 0.0);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (21, 14, 600.0, 0.0, 0.0);

INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (22, 2, 100.0, 0.0, 0.0);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (22, 18, 75.0, 0.0, 0.0);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (22, 14, 600.0, 0.0, 0.0);

INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (30, 2, 100.0, 0.0, 0.0);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (30, 18, 75.0, 0.0, 0.0);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (30, 14, 600.0, 0.0, 0.0);

INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (31, 2, 100.0, 0.0, 0.0);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (31, 18, 75.0, 0.0, 0.0);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (31, 14, 600.0, 0.0, 0.0);

INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (32, 2, 100.0, 0.0, 0.0);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (32, 18, 75.0, 0.0, 0.0);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (32, 14, 600.0, 0.0, 0.0);

INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (34, 2, 100.0, 0.0, 0.0);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (34, 18, 75.0, 0.0, 0.0);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (34, 14, 600.0, 0.0, 0.0);

INSERT INTO 'informix'.project_status_lu(project_status_id,name,description,create_user,create_date,modify_user,modify_date) VALUES (1, 'Active', 'Active', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000');

INSERT INTO 'informix'.project(project_id, project_status_id, project_category_id, create_user, create_date, modify_user, modify_date, tc_direct_project_id) VALUES(1, 1, 1, 'tc', CURRENT, 'tc', CURRENT, 1);
INSERT INTO 'informix'.project(project_id, project_status_id, project_category_id, create_user, create_date, modify_user, modify_date, tc_direct_project_id) VALUES(2, 1, 2, 'tc', CURRENT, 'tc', CURRENT, 1);

INSERT INTO 'informix'.project_payment_adjustment(project_id, resource_role_id, fixed_amount, multiplier) VALUES(1, 1, 200, null);
INSERT INTO 'informix'.project_payment_adjustment(project_id, resource_role_id, fixed_amount, multiplier) VALUES(1, 2, 50, null);
INSERT INTO 'informix'.project_payment_adjustment(project_id, resource_role_id, fixed_amount, multiplier) VALUES(1, 4, null, 300);
INSERT INTO 'informix'.project_payment_adjustment(project_id, resource_role_id, fixed_amount, multiplier) VALUES(2, 1, 200, null);

INSERT INTO 'informix'.project_phase(project_phase_id,project_id,phase_type_id,phase_status_id,fixed_start_time,scheduled_start_time,scheduled_end_time,actual_start_time,actual_end_time,duration,create_user,create_date,modify_user,modify_date) VALUES (111, 1, 4, 2, current, current, current, current, current, 1000, 'System', current, 'System', current);

INSERT INTO 'informix'.resource(resource_id, resource_role_id, project_id, project_phase_id ,create_user,create_date,modify_user,modify_date) VALUES (2, 4, 1, 111, 'tc', CURRENT, 'tc', CURRENT);
INSERT INTO 'informix'.resource(resource_id, resource_role_id, project_id, project_phase_id ,create_user,create_date,modify_user,modify_date) VALUES (3, 2, 1, 111, 'tc', CURRENT, 'tc', CURRENT);

INSERT INTO 'informix'.submission_status_lu(submission_status_id,name,description,create_user,create_date,modify_user,modify_date) VALUES (1, 'Active', 'Active', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000');
INSERT INTO 'informix'.submission_status_lu(submission_status_id,name,description,create_user,create_date,modify_user,modify_date) VALUES (2, 'Failed Screening', 'Failed Manual Screening', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000');
INSERT INTO 'informix'.submission_status_lu(submission_status_id,name,description,create_user,create_date,modify_user,modify_date) VALUES (7, 'Failed Milestone Review', 'Failed Milestone Review', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000');

INSERT INTO 'informix'.submission_type_lu(submission_type_id,name,description,create_user,create_date,modify_user,modify_date) VALUES (1, 'Contest Submission', 'Contest Submission', 'System', CURRENT, 'System', CURRENT);
INSERT INTO 'informix'.upload_type_lu(upload_type_id,name,description,create_user,create_date,modify_user,modify_date) VALUES (1, 'Submission', 'Submission', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000');
INSERT INTO 'informix'.upload_status_lu(upload_status_id,name,description,create_user,create_date,modify_user,modify_date) VALUES (1, 'Active', 'Active', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000');

INSERT INTO 'informix'.upload(upload_id,project_id,project_phase_id,resource_id,upload_type_id,upload_status_id,parameter,upload_desc,create_user,create_date,modify_user,modify_date) VALUES (1, 1, 111, 2, 1, 1, 'param', 'upload', 'System', current, 'System', current);
INSERT INTO 'informix'.upload(upload_id,project_id,project_phase_id,resource_id,upload_type_id,upload_status_id,parameter,upload_desc,create_user,create_date,modify_user,modify_date) VALUES (2, 2, 111, 3, 1, 1, 'param', 'upload', 'System', current, 'System', current);

INSERT INTO 'informix'.submission (submission_id, upload_id, submission_status_id, screening_score, initial_score, final_score, placement, submission_type_id, create_user, create_date, modify_user, modify_date) VALUES(1, 1, 1, 100, 90, 99.33, 1, 1, 'tc', CURRENT, 'tc', CURRENT);
INSERT INTO 'informix'.submission (submission_id, upload_id, submission_status_id, screening_score, initial_score, final_score, placement, submission_type_id, create_user, create_date, modify_user, modify_date) VALUES(2, 2, 1, 100, 90, 99.33, 1, 1, 'tc', CURRENT, 'tc', CURRENT);