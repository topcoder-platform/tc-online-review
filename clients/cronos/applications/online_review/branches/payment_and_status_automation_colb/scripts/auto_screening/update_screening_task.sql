
-- choose one project: 22894629
-- modify category rule for java_design, java_dev, csharp_design, csharp_dev

-- project_id,project_category_id,upload_id,parameter,value,name,screening_task_id,screener_id,start_timestamp,create_user,create_date,modify_user,modify_date
-- 22894629,1,82930,file:/home/apps/submissions/Submitter_22937695_2006-07-16-02-38-15-766.jar,132462,Passed with Warning,11939,0,2006-10-02 23:08:44.0,Converter,2006-10-02 23:08:44.0,Converter,2006-10-02 23:08:44.0

	user_id 132462
	upload_id 82930
	catalog: 5801776
	project_category_id 1
--  screening_task_id 11939
--	screening_task: screening_status_id [Passed with Warning] screener_id [0]
--	upload: parameter[file:/home/apps/submissions/Submitter_22937695_2006-07-16-02-38-15-766.jar]
-- add records: screening_result

select first_name, last_name, handle from user where user_id = 132462;	
select address from email where user_id = 132462;
select screening_result_id from screening_result where screening_task_id = 11939;

first_name,last_name,handle
first foo,last foo,duffy76

0 rows affected
address
foo@fooonyou.com

0 rows affected
screening_result_id
3250955
3250956
3250957

5 is java test file handler
22 is net test file handler
java catalog: 5801776
net catalog: 5801777


-- test for design java
update project set project_category_id = 1 where project_id = 22894629;
update project_info set value = '5801776' where project_id = 22894629 and project_info_type_id = 5;
update upload set parameter = 5 where upload_id = 82930;
update screening_task set screener_id = null, screening_status_id = 1 where screening_task_id = 11939;

-- test for develop java
update project set project_category_id = 2 where project_id = 22894629;
update project_info set value = '5801776' where project_id = 22894629 and project_info_type_id = 5;
update upload set parameter = 5 where upload_id = 82930;
update screening_task set screener_id = null, screening_status_id = 1 where screening_task_id = 11939;

-- test for design net
update project set project_category_id = 1 where project_id = 22894629;
update project_info set value = '5801777' where project_id = 22894629 and project_info_type_id = 5;
update upload set parameter = 22 where upload_id = 82930;
update screening_task set screener_id = null, screening_status_id = 1 where screening_task_id = 11939;

-- test for dev net
update project set project_category_id = 2 where project_id = 22894629;
update project_info set value = '5801777' where project_id = 22894629 and project_info_type_id = 5;
update upload set parameter = 22 where upload_id = 82930;
update screening_task set screener_id = null, screening_status_id = 1 where screening_task_id = 11939;