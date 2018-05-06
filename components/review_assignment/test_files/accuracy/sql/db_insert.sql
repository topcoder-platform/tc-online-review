database tcs_catalog;

INSERT INTO 'informix'.user(user_id,first_name,last_name,create_date,handle,last_login,status,password,activation_code,middle_name) VALUES (1,'userA','ALastName',current,'A',current,'Act','123456','activation1','middle');
INSERT INTO 'informix'.user(user_id,first_name,last_name,create_date,handle,last_login,status,password,activation_code,middle_name) VALUES (2,'userB','BLastName',current,'B',current,'Act','123456','activation2','middle');
INSERT INTO 'informix'.user(user_id,first_name,last_name,create_date,handle,last_login,status,password,activation_code,middle_name) VALUES (3,'userC','CLastName',current,'C',current,'Act','123456','activation3','middle');
INSERT INTO 'informix'.user(user_id,first_name,last_name,create_date,handle,last_login,status,password,activation_code,middle_name) VALUES (4,'userD','DLastName',current,'D',current,'Act','123456','activation3','middle');

INSERT INTO 'informix'.email(user_id,email_id,email_type_id,address,create_date,modify_date,primary_ind,status_id) VALUES (1,1001,1,'address1@topcoder.com',current,current,1,1);
INSERT INTO 'informix'.email(user_id,email_id,email_type_id,address,create_date,modify_date,primary_ind,status_id) VALUES (2,1002,1,'address2@topcoder.com',current,current,1,1);
INSERT INTO 'informix'.email(user_id,email_id,email_type_id,address,create_date,modify_date,primary_ind,status_id) VALUES (3,1003,1,'address3@topcoder.com',current,current,1,1);
INSERT INTO 'informix'.email(user_id,email_id,email_type_id,address,create_date,modify_date,primary_ind,status_id) VALUES (4,1004,1,'address3@topcoder.com',current,current,1,1);

INSERT INTO 'informix'.project_studio_specification(project_studio_spec_id,submitters_locked_between_rounds,create_user,create_date,modify_user,modify_date) VALUES (4001,'t','admin',current,'admin',current);

INSERT INTO 'informix'.project(project_id,project_status_id,project_category_id,project_studio_spec_id,create_user,create_date,modify_user,modify_date,tc_direct_project_id)VALUES (11,1,1,4001,'admin',current,'admin',current,5001);
INSERT INTO 'informix'.project(project_id,project_status_id,project_category_id,project_studio_spec_id,create_user,create_date,modify_user,modify_date,tc_direct_project_id)VALUES (12,1,1,4001,'admin',current,'admin',current,5002);
INSERT INTO 'informix'.project(project_id,project_status_id,project_category_id,project_studio_spec_id,create_user,create_date,modify_user,modify_date,tc_direct_project_id)VALUES (13,1,1,4001,'admin',current,'admin',current,5002);
INSERT INTO 'informix'.project(project_id,project_status_id,project_category_id,project_studio_spec_id,create_user,create_date,modify_user,modify_date,tc_direct_project_id)VALUES (14,1,1,4001,'admin',current,'admin',current,5002);
INSERT INTO 'informix'.project(project_id,project_status_id,project_category_id,project_studio_spec_id,create_user,create_date,modify_user,modify_date,tc_direct_project_id)VALUES (15,1,2,4001,'admin',current,'admin',current,5002);
INSERT INTO 'informix'.project(project_id,project_status_id,project_category_id,project_studio_spec_id,create_user,create_date,modify_user,modify_date,tc_direct_project_id)VALUES (16,1,2,4001,'admin',current,'admin',current,5002);
INSERT INTO 'informix'.project(project_id,project_status_id,project_category_id,project_studio_spec_id,create_user,create_date,modify_user,modify_date,tc_direct_project_id)VALUES (17,1,2,4001,'admin',current,'admin',current,5002);
INSERT INTO 'informix'.project(project_id,project_status_id,project_category_id,project_studio_spec_id,create_user,create_date,modify_user,modify_date,tc_direct_project_id)VALUES (18,1,2,4001,'admin',current,'admin',current,5002);
INSERT INTO 'informix'.project(project_id,project_status_id,project_category_id,project_studio_spec_id,create_user,create_date,modify_user,modify_date,tc_direct_project_id)VALUES (19,1,2,4001,'admin',current,'admin',current,5002);

INSERT INTO 'informix'.project_info(project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
  VALUES(11, 6, 'Test Project 11', 'admin', current, 'admin', current);
INSERT INTO 'informix'.project_info(project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
  VALUES(11, 7, '1.0', 'admin', current, 'admin', current);
INSERT INTO 'informix'.project_info(project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
  VALUES(12, 6, 'Test Project 12', 'admin', current, 'admin', current);
INSERT INTO 'informix'.project_info(project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
  VALUES(12, 7, '1.0', 'admin', current, 'admin', current);
INSERT INTO 'informix'.project_info(project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
  VALUES(13, 6, 'Test Project 13', 'admin', current, 'admin', current);
INSERT INTO 'informix'.project_info(project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
  VALUES(13, 7, '1.0', 'admin', current, 'admin', current);
INSERT INTO 'informix'.project_info(project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
  VALUES(14, 6, 'Test Project 14', 'admin', current, 'admin', current);
INSERT INTO 'informix'.project_info(project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
  VALUES(14, 7, '1.0', 'admin', current, 'admin', current);
INSERT INTO 'informix'.project_info(project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
  VALUES(15, 6, 'Test Project 15', 'admin', current, 'admin', current);
INSERT INTO 'informix'.project_info(project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
  VALUES(15, 7, '1.0', 'admin', current, 'admin', current);
INSERT INTO 'informix'.project_info(project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
  VALUES(16, 6, 'Test Project 16', 'admin', current, 'admin', current);
INSERT INTO 'informix'.project_info(project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
  VALUES(16, 7, '1.0', 'admin', current, 'admin', current);
INSERT INTO 'informix'.project_info(project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
  VALUES(17, 6, 'Test Project 17', 'admin', current, 'admin', current);
INSERT INTO 'informix'.project_info(project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
  VALUES(17, 7, '1.0', 'admin', current, 'admin', current);
INSERT INTO 'informix'.project_info(project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
  VALUES(18, 6, 'Test Project 18', 'admin', current, 'admin', current);
INSERT INTO 'informix'.project_info(project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
  VALUES(18, 7, '1.0', 'admin', current, 'admin', current);
INSERT INTO 'informix'.project_info(project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
  VALUES(19, 6, 'Test Project 19', 'admin', current, 'admin', current);
INSERT INTO 'informix'.project_info(project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
  VALUES(19, 7, '1.0', 'admin', current, 'admin', current);

INSERT INTO 'informix'.project_phase(project_phase_id,project_id,phase_type_id,phase_status_id,scheduled_start_time,scheduled_end_time,duration,create_user,create_date,modify_user,modify_date,actual_start_time,actual_end_time) VALUES (1,11,4,1,'2013-01-01 10:00:00','2013-01-02 10:00:00',100,'admin',current,'admin',current,'2013-01-03 12:00:00','2013-01-04 12:00:00');
INSERT INTO 'informix'.project_phase(project_phase_id,project_id,phase_type_id,phase_status_id,scheduled_start_time,scheduled_end_time,duration,create_user,create_date,modify_user,modify_date,actual_start_time,actual_end_time) VALUES (2,15,3,2,'2013-01-01 10:00:00','2013-01-02 10:00:00',100,'admin',current,'admin',current,'2013-01-03 12:00:00','2013-01-04 12:00:00');
INSERT INTO 'informix'.project_phase(project_phase_id,project_id,phase_type_id,phase_status_id,scheduled_start_time,scheduled_end_time,duration,create_user,create_date,modify_user,modify_date,actual_start_time,actual_end_time) VALUES (3,15,4,2,'2013-01-01 10:00:00','2013-01-02 10:00:00',100,'admin',current,'admin',current,'2013-01-03 12:00:00','2013-01-04 12:00:00');
INSERT INTO 'informix'.project_phase(project_phase_id,project_id,phase_type_id,phase_status_id,scheduled_start_time,scheduled_end_time,duration,create_user,create_date,modify_user,modify_date,actual_start_time,actual_end_time) VALUES (4,11,2,2,'2013-01-01 10:00:00','2013-01-02 10:00:00',100,'admin',current,'admin',current,'2013-01-03 12:00:00','2013-01-04 12:00:00');

INSERT INTO 'informix'.phase_criteria(project_phase_id, phase_criteria_type_id, parameter, create_user, create_date, modify_user, modify_date) VALUES (1, 6, '2', 'admin', current, 'admin', current);

INSERT INTO 'informix'.resource(resource_id,resource_role_id,project_id,project_phase_id,create_user,create_date,modify_user,modify_date) VALUES (2,4,11,1,'admin',current,'admin',current);
INSERT INTO 'informix'.resource(resource_id,resource_role_id,project_id,project_phase_id,create_user,create_date,modify_user,modify_date) VALUES (3,3,11,1,'admin',current,'admin',current);
INSERT INTO 'informix'.resource(resource_id,resource_role_id,project_id,project_phase_id,create_user,create_date,modify_user,modify_date) VALUES (4,1,15,2,'admin',current,'admin',current);

INSERT INTO 'informix'.resource_info(resource_id,resource_info_type_id,value,create_user,create_date,modify_user,modify_date) VALUES (2,1,'2','admin',current,'admin',current);
INSERT INTO 'informix'.resource_info(resource_id,resource_info_type_id,value,create_user,create_date,modify_user,modify_date) VALUES (3,1,'4','admin',current,'admin',current);
INSERT INTO 'informix'.resource_info(resource_id,resource_info_type_id,value,create_user,create_date,modify_user,modify_date) VALUES (4,1,'4','admin',current,'admin',current);

INSERT INTO 'informix'.review_feedback(review_feedback_id,project_id,reviewer_user_id,score,feedback_text,create_user,create_date) VALUES (1,11,1,95,'good','admin',current);
INSERT INTO 'informix'.review_feedback(review_feedback_id,project_id,reviewer_user_id,score,feedback_text,create_user,create_date) VALUES (2,12,1,90,'good','admin',current);
INSERT INTO 'informix'.review_feedback(review_feedback_id,project_id,reviewer_user_id,score,feedback_text,create_user,create_date) VALUES (3,15,1,70,'average','admin',current);
INSERT INTO 'informix'.review_feedback(review_feedback_id,project_id,reviewer_user_id,score,feedback_text,create_user,create_date) VALUES (4,16,1,50,'bad','admin',current);

INSERT INTO 'informix'.review_feedback(review_feedback_id,project_id,reviewer_user_id,score,feedback_text,create_user,create_date) VALUES (5,11,2,95,'good','admin',current);
INSERT INTO 'informix'.review_feedback(review_feedback_id,project_id,reviewer_user_id,score,feedback_text,create_user,create_date) VALUES (6,12,2,90,'good','admin',current);
INSERT INTO 'informix'.review_feedback(review_feedback_id,project_id,reviewer_user_id,score,feedback_text,create_user,create_date) VALUES (7,13,2,90,'good','admin',current);
INSERT INTO 'informix'.review_feedback(review_feedback_id,project_id,reviewer_user_id,score,feedback_text,create_user,create_date) VALUES (8,14,2,80,'average','admin',current);
INSERT INTO 'informix'.review_feedback(review_feedback_id,project_id,reviewer_user_id,score,feedback_text,create_user,create_date) VALUES (9,15,2,50,'average','admin',current);

INSERT INTO 'informix'.review_feedback(review_feedback_id,project_id,reviewer_user_id,score,feedback_text,create_user,create_date) VALUES (10,15,3,85,'average','admin',current);
INSERT INTO 'informix'.review_feedback(review_feedback_id,project_id,reviewer_user_id,score,feedback_text,create_user,create_date) VALUES (11,16,3,85,'average','admin',current);
INSERT INTO 'informix'.review_feedback(review_feedback_id,project_id,reviewer_user_id,score,feedback_text,create_user,create_date) VALUES (12,17,3,85,'average','admin',current);
INSERT INTO 'informix'.review_feedback(review_feedback_id,project_id,reviewer_user_id,score,feedback_text,create_user,create_date) VALUES (13,18,3,85,'average','admin',current);
INSERT INTO 'informix'.review_feedback(review_feedback_id,project_id,reviewer_user_id,score,feedback_text,create_user,create_date) VALUES (14,19,3,85,'average','admin',current);
INSERT INTO 'informix'.review_feedback(review_feedback_id,project_id,reviewer_user_id,score,feedback_text,create_user,create_date) VALUES (15,11,3,90,'good','admin',current);
INSERT INTO 'informix'.review_feedback(review_feedback_id,project_id,reviewer_user_id,score,feedback_text,create_user,create_date) VALUES (16,12,3,90,'good','admin',current);
INSERT INTO 'informix'.review_feedback(review_feedback_id,project_id,reviewer_user_id,score,feedback_text,create_user,create_date) VALUES (17,13,3,100,'excellent','admin',current);
INSERT INTO 'informix'.review_feedback(review_feedback_id,project_id,reviewer_user_id,score,feedback_text,create_user,create_date) VALUES (18,14,3,100,'excellent','admin',current);

