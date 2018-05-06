INSERT INTO 'informix'.project_studio_specification VALUES (1, 'goals', 'target', 'branding', 'disliked', 'other', 'winning', 't', 'round_one', 'round_two', 'colors', 'fonts', 'layout', 'intro', 'desc', 'req' , 'feedback', 'System', current, 'System', current);

INSERT INTO 'informix'.project_type_lu(project_type_id,name,description,is_generic,create_user,create_date,modify_user,modify_date) VALUES (1, 'Component', 'Component', 'f', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000');

INSERT INTO 'informix'.project_category_lu(project_category_id,project_type_id,name,description,create_user,create_date,modify_user,modify_date, display, display_order) VALUES (1, 1, 'Design', 'Design', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000', 't', 15);
INSERT INTO 'informix'.project_category_lu(project_category_id,project_type_id,name,description,create_user,create_date,modify_user,modify_date, display, display_order) VALUES (2, 1, 'Development', 'Development', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000', 't', 16);
INSERT INTO 'informix'.project_category_lu(project_category_id,project_type_id,name,description,create_user,create_date,modify_user,modify_date, display, display_order) VALUES (3, 1, 'Security', 'Security', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000', null, null);
INSERT INTO 'informix'.project_category_lu(project_category_id,project_type_id,name,description,create_user,create_date,modify_user,modify_date, display, display_order) VALUES (4, 1, 'Process', 'Process', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000', null, null);

INSERT INTO 'informix'.project_status_lu(project_status_id,name,description,create_user,create_date,modify_user,modify_date) VALUES (1, 'Active', 'Active', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000');

INSERT INTO 'informix'.submission_status_lu(submission_status_id,name,description,create_user,create_date,modify_user,modify_date) VALUES (1, 'Active', 'Active', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000');
INSERT INTO 'informix'.submission_status_lu(submission_status_id,name,description,create_user,create_date,modify_user,modify_date) VALUES (2, 'Failed Screening', 'Failed Manual Screening', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000');
INSERT INTO 'informix'.submission_status_lu(submission_status_id,name,description,create_user,create_date,modify_user,modify_date) VALUES (7, 'Failed Milestone Review', 'Failed Milestone Review', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000');

INSERT INTO 'informix'.submission_type_lu(submission_type_id,name,description,create_user,create_date,modify_user,modify_date) VALUES (1, 'Contest Submission', 'Contest Submission', 'System', CURRENT, 'System', CURRENT);
INSERT INTO 'informix'.submission_type_lu(submission_type_id,name,description,create_user,create_date,modify_user,modify_date) VALUES (2, 'Specification Submission', 'Specification Submission', 'System', CURRENT, 'System', CURRENT);
INSERT INTO 'informix'.submission_type_lu(submission_type_id,name,description,create_user,create_date,modify_user,modify_date) VALUES (3, 'Milestone Submission', 'Milestone Submission', 'System', CURRENT, 'System', CURRENT);

INSERT INTO 'informix'.prize_type_lu(prize_type_id,prize_type_desc) VALUES (15, 'Contest Prize');
INSERT INTO 'informix'.phase_status_lu(phase_status_id,name,description,create_user,create_date,modify_user,modify_date) VALUES (2, 'Open', 'Open', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000');

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

INSERT INTO 'informix'.resource_role_lu(resource_role_id,phase_type_id,name,description,create_user,create_date,modify_user,modify_date) VALUES (1, NULL, 'Submitter', 'Submitter', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000');
INSERT INTO 'informix'.resource_role_lu(resource_role_id,phase_type_id,name,description,create_user,create_date,modify_user,modify_date) VALUES (2, 3, 'Primary Screener', 'Primary Screener', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000');
INSERT INTO 'informix'.resource_role_lu(resource_role_id,phase_type_id,name,description,create_user,create_date,modify_user,modify_date) VALUES (4, 4, 'Reviewer', 'Reviewer', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000');
INSERT INTO 'informix'.resource_role_lu(resource_role_id,phase_type_id,name,description,create_user,create_date,modify_user,modify_date) VALUES (5, 4, 'Accuracy Reviewer', 'Accuracy Reviewer', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000');
INSERT INTO 'informix'.resource_role_lu(resource_role_id,phase_type_id,name,description,create_user,create_date,modify_user,modify_date) VALUES (6, 4, 'Failure Reviewer', 'Failure Reviewer', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000');
INSERT INTO 'informix'.resource_role_lu(resource_role_id,phase_type_id,name,description,create_user,create_date,modify_user,modify_date) VALUES (7, 4, 'Stress Reviewer', 'Stress Reviewer', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000');
INSERT INTO 'informix'.resource_role_lu(resource_role_id,phase_type_id,name,description,create_user,create_date,modify_user,modify_date) VALUES (8, 7, 'Aggregator', 'Aggregator', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000');
INSERT INTO 'informix'.resource_role_lu(resource_role_id,phase_type_id,name,description,create_user,create_date,modify_user,modify_date) VALUES (9, 10, 'Final Reviewer', 'Final Reviewer', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000');
INSERT INTO 'informix'.resource_role_lu(resource_role_id,phase_type_id,name,description,create_user,create_date,modify_user,modify_date) VALUES (14, NULL, 'Copilot', 'Copilot', 'System', '2010-01-13 08:54:35.000', 'System', '2010-01-13 08:54:35.000');
INSERT INTO 'informix'.resource_role_lu(resource_role_id,phase_type_id,name,description,create_user,create_date,modify_user,modify_date) VALUES (18, 14, 'Specification Reviewer', 'Specification Reviewer', 'System', current, 'System', current);
INSERT INTO 'informix'.resource_role_lu(resource_role_id,phase_type_id,name,description,create_user,create_date,modify_user,modify_date) VALUES (19, 16, 'Milestone Screener', 'Milestone Screener', 'System', '2010-12-04 14:51:25.000', 'System', '2010-12-04 14:51:25.000');
INSERT INTO 'informix'.resource_role_lu(resource_role_id,phase_type_id,name,description,create_user,create_date,modify_user,modify_date) VALUES (20, 17, 'Milestone Reviewer', 'Milestone Reviewer', 'System', '2010-12-04 14:51:25.000', 'System', '2010-12-04 14:51:25.000');

INSERT INTO 'informix'.upload_type_lu(upload_type_id,name,description,create_user,create_date,modify_user,modify_date) VALUES (1, 'Submission', 'Submission', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000');
INSERT INTO 'informix'.upload_type_lu(upload_type_id,name,description,create_user,create_date,modify_user,modify_date) VALUES (2, 'Test Case', 'Test Case', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000');
INSERT INTO 'informix'.upload_type_lu(upload_type_id,name,description,create_user,create_date,modify_user,modify_date) VALUES (3, 'Final Fix', 'Final Fix', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000');
INSERT INTO 'informix'.upload_type_lu(upload_type_id,name,description,create_user,create_date,modify_user,modify_date) VALUES (4, 'Review Document', 'Review Document', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000');
INSERT INTO 'informix'.upload_status_lu(upload_status_id,name,description,create_user,create_date,modify_user,modify_date) VALUES (1, 'Active', 'Active', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000');
INSERT INTO 'informix'.upload_status_lu(upload_status_id,name,description,create_user,create_date,modify_user,modify_date) VALUES (2, 'Deleted', 'Deleted', 'System', '2006-11-02 20:14:24.000', 'System', '2006-11-02 20:14:24.000');

-- Project
INSERT INTO 'informix'.project(project_id,project_status_id,project_category_id,project_studio_spec_id,create_user,create_date,modify_user,modify_date,tc_direct_project_id) VALUES (1000, 1, 1, 1, 'System', current, 'System', current, 1);

INSERT INTO 'informix'.prize(prize_id,project_id,place,prize_amount,prize_type_id,number_of_submissions,create_user,create_date,modify_user,modify_date) VALUES (1, 1000, 1, 500, 15, 3, 'System', current, 'System', current);

INSERT INTO 'informix'.project_phase(project_phase_id,project_id,phase_type_id,phase_status_id,fixed_start_time,scheduled_start_time,scheduled_end_time,actual_start_time,actual_end_time,duration,create_user,create_date,modify_user,modify_date) VALUES (1, 1000, 4, 2, current, current, current, current, current, 1000, 'System', current, 'System', current);

INSERT INTO 'informix'.resource(resource_id,resource_role_id,project_id,project_phase_id,create_user,create_date,modify_user,modify_date) VALUES (1, 1, 1000, 1, 'System', current, 'System', current);

INSERT INTO 'informix'.upload(upload_id,project_id,project_phase_id,resource_id,upload_type_id,upload_status_id,parameter,upload_desc,create_user,create_date,modify_user,modify_date) VALUES (500, 1000, 1, 1, 1, 1, 'param', 'upload', 'System', current, 'System', current);
INSERT INTO 'informix'.upload(upload_id,project_id,project_phase_id,resource_id,upload_type_id,upload_status_id,parameter,upload_desc,create_user,create_date,modify_user,modify_date) VALUES (623, 1000, 1, 1, 1, 1, 'param', 'upload', 'System', current, 'System', current);

INSERT INTO 'informix'.submission (submission_id,upload_id,submission_status_id,submission_type_id,create_user,create_date,modify_user,modify_date) VALUES (1, 500, 1, 1, 'System', current, 'System', current);
INSERT INTO 'informix'.submission (submission_id,upload_id,submission_status_id,submission_type_id,create_user,create_date,modify_user,modify_date) VALUES (2, 623, 2, 1, 'System', current, 'System', current);

-- Component Design
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (1, 2, 0.0, 0.0, 0.01);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (1, 4, 0.0, 0.12, 0.05);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (1, 8, 10.0, 0.0, 0.0);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (1, 9, 0.0, 0.05, 0.0);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (1, 18, 30.0, 0.0, 0.0);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (1, 14, 300.0, 0.0, 0.0);

-- Component Development
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (2, 2, 0.0, 0.0, 0.02);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (2, 4, 0.0, 0.2, 0.05);
INSERT INTO 'informix'.default_project_payment(project_category_id,resource_role_id,fixed_amount,base_coefficient,incremental_coefficient) VALUES (2, 20, 15.0, 0.01, 0.03);


INSERT INTO 'informix'.project_payment_adjustment(project_id,resource_role_id,fixed_amount) VALUES (1000,2,60);
INSERT INTO 'informix'.project_payment_adjustment(project_id,resource_role_id,multiplier) VALUES (1000,5,3);
INSERT INTO 'informix'.project_payment_adjustment(project_id,resource_role_id,multiplier) VALUES (1000,8,3);
INSERT INTO 'informix'.project_payment_adjustment(project_id,resource_role_id) VALUES (1000,9);
