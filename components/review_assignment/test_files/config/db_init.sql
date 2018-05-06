database tcs_catalog;

--to retrieve the user
INSERT INTO 'informix'.user(user_id,first_name,last_name,create_date,modify_date,handle,last_login,status,password,activation_code,middle_name,handle_lower,timezone_id) VALUES (100001,'zhong1','qiang1',current,current,'Handler1',current,'ok1','pwd1','activation1','middle','handler1',1);
INSERT INTO 'informix'.user(user_id,first_name,last_name,create_date,modify_date,handle,last_login,status,password,activation_code,middle_name,handle_lower,timezone_id) VALUES (100002,'zhong2','qiang2',current,current,'Handler2',current,'ok2','pwd2','activation2','middle','handler2',2);
INSERT INTO 'informix'.user(user_id,first_name,last_name,create_date,modify_date,handle,last_login,status,password,activation_code,middle_name,handle_lower,timezone_id) VALUES (100003,'zhong3','qiang3',current,current,'Handler3',current,'ok3','pwd3','activation3','middle','handler3',3);
INSERT INTO 'informix'.user(user_id,first_name,last_name,create_date,modify_date,handle,last_login,status,password,activation_code,middle_name,handle_lower,timezone_id) VALUES (100004,'zhong4','qiang4',current,current,'Handler4',current,'ok4','pwd4','activation3','middle','handler4',3);
INSERT INTO 'informix'.user(user_id,first_name,last_name,create_date,modify_date,handle,last_login,status,password,activation_code,middle_name,handle_lower,timezone_id) VALUES (100005,'zhong5','qiang5',current,current,'Handler5',current,'ok5','pwd5','activation3','middle','handler5',3);
INSERT INTO 'informix'.user(user_id,first_name,last_name,create_date,modify_date,handle,last_login,status,password,activation_code,middle_name,handle_lower,timezone_id) VALUES (100006,'zhong6','qiang6',current,current,'Handler5',current,'ok5','pwd5','activation3','middle','handler5',3);
INSERT INTO 'informix'.user(user_id,first_name,last_name,create_date,modify_date,handle,last_login,status,password,activation_code,middle_name,handle_lower,timezone_id) VALUES (100007,'zhong7','qiang7',current,current,'Handler5',current,'ok5','pwd5','activation3','middle','handler5',3);
INSERT INTO 'informix'.user(user_id,first_name,last_name,create_date,modify_date,handle,last_login,status,password,activation_code,middle_name,handle_lower,timezone_id) VALUES (100008,'zhong8','qiang8',current,current,'Handler5',current,'ok5','pwd5','activation3','middle','handler5',3);
INSERT INTO 'informix'.user(user_id,first_name,last_name,create_date,modify_date,handle,last_login,status,password,activation_code,middle_name,handle_lower,timezone_id) VALUES (100009,'zhong9','qiang9',current,current,'Handler5',current,'ok5','pwd5','activation3','middle','handler5',3);
INSERT INTO 'informix'.user(user_id,first_name,last_name,create_date,modify_date,handle,last_login,status,password,activation_code,middle_name,handle_lower,timezone_id) VALUES (100010,'zhong10','qiang5',current,current,'Handler5',current,'ok5','pwd5','activation3','middle','handler5',3);
INSERT INTO 'informix'.user(user_id,first_name,last_name,create_date,modify_date,handle,last_login,status,password,activation_code,middle_name,handle_lower,timezone_id) VALUES (100011,'zhong11','qiang5',current,current,'Handler5',current,'ok5','pwd5','activation3','middle','handler5',3);
INSERT INTO 'informix'.user(user_id,first_name,last_name,create_date,modify_date,handle,last_login,status,password,activation_code,middle_name,handle_lower,timezone_id) VALUES (1000098,'zhong6','qiang6',current,current,'Handler6',current,'ok6','pwd6','activation3','middle','handler6',3);

INSERT INTO 'informix'.email(user_id,email_id,email_type_id,address,create_date,modify_date,primary_ind,status_id) VALUES (100001,1,3,'address1@topcoder.com',current,current,1,4);
INSERT INTO 'informix'.email(user_id,email_id,email_type_id,address,create_date,modify_date,primary_ind,status_id) VALUES (100002,2,6,'address2@topcoder.com',current,current,1,8);
INSERT INTO 'informix'.email(user_id,email_id,email_type_id,address,create_date,modify_date,primary_ind,status_id) VALUES (100003,3,7,'address3@topcoder.com',current,current,1,4);
INSERT INTO 'informix'.email(user_id,email_id,email_type_id,address,create_date,modify_date,primary_ind,status_id) VALUES (100004,4,7,'address4@topcoder.com',current,current,1,4);
INSERT INTO 'informix'.email(user_id,email_id,email_type_id,address,create_date,modify_date,primary_ind,status_id) VALUES (100005,5,7,'address5@topcoder.com',current,current,2,4);
INSERT INTO 'informix'.email(user_id,email_id,email_type_id,address,create_date,modify_date,primary_ind,status_id) VALUES (1000098,6,7,'address5@topcoder.com',current,current,1,4);

INSERT INTO 'informix'.project_studio_specification(project_studio_spec_id,submitters_locked_between_rounds,create_user,create_date,modify_user,modify_date) VALUES (200001,'t','projectCreator',current,'projectUpdator',current);


INSERT INTO 'informix'.project(project_id,project_status_id,project_category_id,project_studio_spec_id,create_user,create_date,modify_user,modify_date,tc_direct_project_id)VALUES (300001,1,1,200001,'projectCreator',current,'projectUpdator',current,5001);
INSERT INTO 'informix'.project(project_id,project_status_id,project_category_id,project_studio_spec_id,create_user,create_date,modify_user,modify_date,tc_direct_project_id)VALUES (300002,1,1,200001,'projectCreator',current,'projectUpdator',current,5002);
INSERT INTO 'informix'.project(project_id,project_status_id,project_category_id,project_studio_spec_id,create_user,create_date,modify_user,modify_date,tc_direct_project_id)VALUES (300003,1,1,200001,'projectCreator',current,'projectUpdator',current,5002);
INSERT INTO 'informix'.project(project_id,project_status_id,project_category_id,project_studio_spec_id,create_user,create_date,modify_user,modify_date,tc_direct_project_id)VALUES (300004,1,1,200001,'projectCreator',current,'projectUpdator',current,5002);
INSERT INTO 'informix'.project(project_id,project_status_id,project_category_id,project_studio_spec_id,create_user,create_date,modify_user,modify_date,tc_direct_project_id)VALUES (300005,1,2,200001,'projectCreator',current,'projectUpdator',current,5002);
INSERT INTO 'informix'.project(project_id,project_status_id,project_category_id,project_studio_spec_id,create_user,create_date,modify_user,modify_date,tc_direct_project_id)VALUES (300006,1,2,200001,'projectCreator',current,'projectUpdator',current,5002);
INSERT INTO 'informix'.project(project_id,project_status_id,project_category_id,project_studio_spec_id,create_user,create_date,modify_user,modify_date,tc_direct_project_id)VALUES (300007,1,2,200001,'projectCreator',current,'projectUpdator',current,5002);
INSERT INTO 'informix'.project(project_id,project_status_id,project_category_id,project_studio_spec_id,create_user,create_date,modify_user,modify_date,tc_direct_project_id)VALUES (300008,1,2,200001,'projectCreator',current,'projectUpdator',current,5002);
INSERT INTO 'informix'.project(project_id,project_status_id,project_category_id,project_studio_spec_id,create_user,create_date,modify_user,modify_date,tc_direct_project_id)VALUES (300009,1,2,200001,'projectCreator',current,'projectUpdator',current,5002);


INSERT INTO 'informix'.project_info(project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES(300001, 6, 'Project 11', 'project_infoCreator', current, 'project_infoUpdator', current);
INSERT INTO 'informix'.project_info(project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES(300002, 6, 'Project 12', 'project_infoCreator', current, 'project_infoUpdator', current);
INSERT INTO 'informix'.project_info(project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES(300003, 6, 'Project 13', 'project_infoCreator', current, 'project_infoUpdator', current);
INSERT INTO 'informix'.project_info(project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES(300004, 6, 'Project 14', 'project_infoCreator', current, 'project_infoUpdator', current);
INSERT INTO 'informix'.project_info(project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES(300005, 7, 'Project 15', 'project_infoCreator', current, 'project_infoUpdator', current);
INSERT INTO 'informix'.project_info(project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES(300006, 7, 'Project 16', 'project_infoCreator', current, 'project_infoUpdator', current);
INSERT INTO 'informix'.project_info(project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES(300007, 7, 'Project 17', 'project_infoCreator', current, 'project_infoUpdator', current);
INSERT INTO 'informix'.project_info(project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES(300008, 7, 'Project 18', 'project_infoCreator', current, 'project_infoUpdator', current);
INSERT INTO 'informix'.project_info(project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES(300009, 7, 'Project 19', 'project_infoCreator', current, 'project_infoUpdator', current);


INSERT INTO 'informix'.project_phase(project_phase_id,project_id,phase_type_id,phase_status_id,scheduled_start_time,scheduled_end_time,duration,create_user,create_date,modify_user,modify_date,actual_start_time,actual_end_time) VALUES (400001,300001,4,1,'2013-01-01 10:00:00','2013-01-02 10:00:00',100,'project_phaseCreator',current,'project_phaseUpdator',current,'2013-01-03 12:00:00','2013-01-04 12:00:00');
INSERT INTO 'informix'.project_phase(project_phase_id,project_id,phase_type_id,phase_status_id,scheduled_start_time,scheduled_end_time,duration,create_user,create_date,modify_user,modify_date,actual_start_time,actual_end_time) VALUES (400002,300002,3,2,'2013-01-01 10:00:00','2013-01-02 10:00:00',100,'project_phaseCreator',current,'project_phaseUpdator',current,'2013-01-03 12:00:00','2013-01-04 12:00:00');
INSERT INTO 'informix'.project_phase(project_phase_id,project_id,phase_type_id,phase_status_id,scheduled_start_time,scheduled_end_time,duration,create_user,create_date,modify_user,modify_date,actual_start_time,actual_end_time) VALUES (400003,300002,4,2,'2013-01-01 10:00:00','2013-01-02 10:00:00',100,'project_phaseCreator',current,'project_phaseUpdator',current,'2013-01-03 12:00:00','2013-01-04 12:00:00');
INSERT INTO 'informix'.project_phase(project_phase_id,project_id,phase_type_id,phase_status_id,scheduled_start_time,scheduled_end_time,duration,create_user,create_date,modify_user,modify_date,actual_start_time,actual_end_time) VALUES (400004,300003,2,2,'2013-01-01 10:00:00','2013-01-02 10:00:00',100,'project_phaseCreator',current,'project_phaseUpdator',current,'2013-01-03 12:00:00','2013-01-04 12:00:00');
INSERT INTO 'informix'.project_phase(project_phase_id,project_id,phase_type_id,phase_status_id,scheduled_start_time,scheduled_end_time,duration,create_user,create_date,modify_user,modify_date,actual_start_time,actual_end_time) VALUES (400005,300004,2,2,'2013-01-01 10:00:00','2013-01-02 10:00:00',100,'project_phaseCreator',current,'project_phaseUpdator',current,'2013-01-03 12:00:00','2013-01-04 12:00:00');
INSERT INTO 'informix'.project_phase(project_phase_id,project_id,phase_type_id,phase_status_id,scheduled_start_time,scheduled_end_time,duration,create_user,create_date,modify_user,modify_date,actual_start_time,actual_end_time) VALUES (400006,300005,2,2,'2013-01-01 10:00:00','2013-01-02 10:00:00',100,'project_phaseCreator',current,'project_phaseUpdator',current,'2013-01-03 12:00:00','2013-01-04 12:00:00');
INSERT INTO 'informix'.project_phase(project_phase_id,project_id,phase_type_id,phase_status_id,scheduled_start_time,scheduled_end_time,duration,create_user,create_date,modify_user,modify_date,actual_start_time,actual_end_time) VALUES (400007,300006,2,2,'2013-01-01 10:00:00','2013-01-02 10:00:00',100,'project_phaseCreator',current,'project_phaseUpdator',current,'2013-01-03 12:00:00','2013-01-04 12:00:00');
INSERT INTO 'informix'.project_phase(project_phase_id,project_id,phase_type_id,phase_status_id,scheduled_start_time,scheduled_end_time,duration,create_user,create_date,modify_user,modify_date,actual_start_time,actual_end_time) VALUES (400008,300007,2,2,'2013-01-01 10:00:00','2013-01-02 10:00:00',100,'project_phaseCreator',current,'project_phaseUpdator',current,'2013-01-03 12:00:00','2013-01-04 12:00:00');

INSERT INTO 'informix'.phase_criteria(project_phase_id, phase_criteria_type_id, parameter, create_user, create_date, modify_user, modify_date) VALUES (400001, 6, '2', 'resourceCreator', current, 'resourceCreator', current);
INSERT INTO 'informix'.phase_criteria(project_phase_id, phase_criteria_type_id, parameter, create_user, create_date, modify_user, modify_date) VALUES (400002, 6, '2', 'resourceCreator', current, 'resourceCreator', current);
INSERT INTO 'informix'.phase_criteria(project_phase_id, phase_criteria_type_id, parameter, create_user, create_date, modify_user, modify_date) VALUES (400003, 6, '2', 'resourceCreator', current, 'resourceCreator', current);

INSERT INTO 'informix'.resource(resource_id,resource_role_id,project_id,project_phase_id,create_user,create_date,modify_user,modify_date) VALUES (500001,1,300001,400001,'resourceCreator',current,'resourceUpdator',current);
INSERT INTO 'informix'.resource(resource_id,resource_role_id,project_id,project_phase_id,create_user,create_date,modify_user,modify_date) VALUES (500002,4,300001,400001,'resourceCreator',current,'resourceUpdator',current);
INSERT INTO 'informix'.resource(resource_id,resource_role_id,project_id,project_phase_id,create_user,create_date,modify_user,modify_date) VALUES (500003,3,300001,400001,'resourceCreator',current,'resourceUpdator',current);
INSERT INTO 'informix'.resource(resource_id,resource_role_id,project_id,project_phase_id,create_user,create_date,modify_user,modify_date) VALUES (500004,1,300002,400002,'resourceCreator',current,'resourceUpdator',current);
INSERT INTO 'informix'.resource(resource_id,resource_role_id,project_id,project_phase_id,create_user,create_date,modify_user,modify_date) VALUES (500005,1,300002,400003,'resourceCreator',current,'resourceUpdator',current);
INSERT INTO 'informix'.resource(resource_id,resource_role_id,project_id,project_phase_id,create_user,create_date,modify_user,modify_date) VALUES (500006,1,300002,400004,'resourceCreator',current,'resourceUpdator',current);
INSERT INTO 'informix'.resource(resource_id,resource_role_id,project_id,project_phase_id,create_user,create_date,modify_user,modify_date) VALUES (500007,1,300002,400005,'resourceCreator',current,'resourceUpdator',current);
INSERT INTO 'informix'.resource(resource_id,resource_role_id,project_id,project_phase_id,create_user,create_date,modify_user,modify_date) VALUES (500008,1,300002,400006,'resourceCreator',current,'resourceUpdator',current);

INSERT INTO 'informix'.resource_info(resource_id,resource_info_type_id,value,create_user,create_date,modify_user,modify_date) VALUES (500001,1,'my resource 1','resource_infoCreator',current,'resource_infoUpdator',current);
INSERT INTO 'informix'.resource_info(resource_id,resource_info_type_id,value,create_user,create_date,modify_user,modify_date) VALUES (500002,1,'my resource 2','resource_infoCreator',current,'resource_infoUpdator',current);
INSERT INTO 'informix'.resource_info(resource_id,resource_info_type_id,value,create_user,create_date,modify_user,modify_date) VALUES (500003,1,'my resource 3','resource_infoCreator',current,'resource_infoUpdator',current);
INSERT INTO 'informix'.resource_info(resource_id,resource_info_type_id,value,create_user,create_date,modify_user,modify_date) VALUES (500004,1,'my resource 4','resource_infoCreator',current,'resource_infoUpdator',current);
INSERT INTO 'informix'.resource_info(resource_id,resource_info_type_id,value,create_user,create_date,modify_user,modify_date) VALUES (500005,1,'my resource 5','resource_infoCreator',current,'resource_infoUpdator',current);
INSERT INTO 'informix'.resource_info(resource_id,resource_info_type_id,value,create_user,create_date,modify_user,modify_date) VALUES (500006,1,'my resource 6','resource_infoCreator',current,'resource_infoUpdator',current);
INSERT INTO 'informix'.resource_info(resource_id,resource_info_type_id,value,create_user,create_date,modify_user,modify_date) VALUES (500007,1,'my resource 7','resource_infoCreator',current,'resource_infoUpdator',current);
INSERT INTO 'informix'.resource_info(resource_id,resource_info_type_id,value,create_user,create_date,modify_user,modify_date) VALUES (500008,1,'my resource 8','resource_infoCreator',current,'resource_infoUpdator',current);


INSERT INTO 'informix'.review_feedback(review_feedback_id,project_id,reviewer_user_id,score,feedback_text,create_user,create_date) VALUES(600001, 300001, 100001, 90, 'good reviewer.', 'tc', Current);
INSERT INTO 'informix'.review_feedback(review_feedback_id,project_id,reviewer_user_id,score,feedback_text,create_user,create_date) VALUES(600002, 300001, 100002, 90, 'good reviewer.', 'tc', Current);
INSERT INTO 'informix'.review_feedback(review_feedback_id,project_id,reviewer_user_id,score,feedback_text,create_user,create_date) VALUES(600003, 300001, 100003, 70, 'bad reviewer.', 'tc2', Current);
INSERT INTO 'informix'.review_feedback(review_feedback_id,project_id,reviewer_user_id,score,feedback_text,create_user,create_date) VALUES(600004, 300002, 100001, 90, 'good reviewer.', 'tc3', Current);
INSERT INTO 'informix'.review_feedback(review_feedback_id,project_id,reviewer_user_id,score,feedback_text,create_user,create_date) VALUES(600005, 300002, 100002, 70, 'average reviewer.', 'tc', Current);
INSERT INTO 'informix'.review_feedback(review_feedback_id,project_id,reviewer_user_id,score,feedback_text,create_user,create_date) VALUES(600006, 300002, 100003, 40, 'bad reviewer.', 'tc2', Current);
INSERT INTO 'informix'.review_feedback(review_feedback_id,project_id,reviewer_user_id,score,feedback_text,create_user,create_date) VALUES(600007, 300003, 100004, 90, 'good reviewer.', 'tc3', Current);
INSERT INTO 'informix'.review_feedback(review_feedback_id,project_id,reviewer_user_id,score,feedback_text,create_user,create_date) VALUES(600008, 300003, 100005, 90, 'good reviewer.', 'tc3', Current);
INSERT INTO 'informix'.review_feedback(review_feedback_id,project_id,reviewer_user_id,score,feedback_text,create_user,create_date) VALUES(600009, 300003, 100006, 70, 'average reviewer.', 'tc3', Current);
INSERT INTO 'informix'.review_feedback(review_feedback_id,project_id,reviewer_user_id,score,feedback_text,create_user,create_date) VALUES(600010, 300004, 100007, 50, 'bad reviewer.', 'tc3', Current);
INSERT INTO 'informix'.review_feedback(review_feedback_id,project_id,reviewer_user_id,score,feedback_text,create_user,create_date) VALUES(600011, 300004, 100006, 50, 'bad reviewer.', 'tc3', Current);
INSERT INTO 'informix'.review_feedback(review_feedback_id,project_id,reviewer_user_id,score,feedback_text,create_user,create_date) VALUES(600012, 300004, 100009, 50, 'bad reviewer.', 'tc3', Current);


INSERT INTO id_sequences(name, next_block_start, block_size, exhausted) VALUES('resource_id_seq', 1000, 20, 0);
INSERT INTO id_sequences(name, next_block_start, block_size, exhausted) VALUES('resource_role_id_seq', 1000, 20, 0);
INSERT INTO id_sequences(name, next_block_start, block_size, exhausted) VALUES('notification_type_id_seq', 1000, 20, 0);
INSERT INTO id_sequences(name, next_block_start, block_size, exhausted) VALUES('phase_id_seq', 1000, 20, 0);
INSERT INTO id_sequences(name, next_block_start, block_size, exhausted) VALUES('project_id_seq', 1000, 20, 0);
INSERT INTO id_sequences(name, next_block_start, block_size, exhausted) VALUES('project_audit_id_seq', 1000, 20, 0);
INSERT INTO id_sequences(name, next_block_start, block_size, exhausted) VALUES('file_type_id_seq', 1000, 20, 0);
INSERT INTO id_sequences(name, next_block_start, block_size, exhausted) VALUES('prize_id_seq', 1000, 20, 0);
INSERT INTO id_sequences(name, next_block_start, block_size, exhausted) VALUES('studio_spec_id_seq', 1000, 20, 0);