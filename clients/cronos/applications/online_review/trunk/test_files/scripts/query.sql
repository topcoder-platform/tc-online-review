--delete from review_item_comment;
--delete from review_comment;
--delete from review_item;
--delete from review;

--delete from screening_result;
--delete from screening_task;

--delete from notification;
--delete from resource_submission;
--delete from resource_info;

--delete from submission;
--delete from upload;

--delete from phase_criteria;
--delete from phase_dependency;

--delete from project_audit;
--delete from project_info;
--delete from resource;
--delete from phase;
--delete from project;

--select count(*) project from project;
--select count(*) project_info from project_info;
--select count(*) project_audit from project_audit;

--select count(*) phase from phase;
--select count(*) phase_dependency from phase_dependency;
--select count(*) phase_criteria from phase_criteria;

--select count(*) submission from submission;
--select count(*) upload from upload;

--select count(*) resource from resource;
--select count(*) resource_info from resource_info;
--select count(*) resource_submission from resource_submission;
--select count(*) notification from notification;

--select count(*) screening_task from screening_task;
--select count(*) screening_result from screening_result;

--select count(*) review from review;
--select count(*) review_item from review_item;
--select count(*) review_comment from review_comment;
--select count(*) review_item_comment from review_item_comment;
--select max(scorecard_id) from scorecard;
--select max(scorecard_question_id) from scorecard_question;
--select * from scorecard_question;
--select * from scorecard_group;
--select * from scorecard_section;
-- update id_sequences set next_block_start = (--select max() from scorecard) where name='scorecard_id_seq';
-- update id_sequences set next_block_start = where name='scorecard_question_id_seq';
-- --delete from scorecard;
-- --select count(*) from scorecard;
-- --select count(*) from scorecard_question;

--select si.idxname, st.tabname from sysindexes si, systables st where si.tabid = st.tabid


--select first 5 * from project_result; and cd.comp_vers_id = pi_vi.value , comp_version_dates cd
--
--user_id,first_name,last_name,create_date,modify_date,handle,last_login,status,password,activation_code,middle_name,handle_lower,timezone_id
--1800075,first foo,last foo,2003-01-17 00:00:00.0,2006-07-13 16:00:36.0,mylar,2003-01-20 

--DELETE FROM review WHERE resource_id in (select resource_id from resource where project_id = 123213)
select * from scorecard;