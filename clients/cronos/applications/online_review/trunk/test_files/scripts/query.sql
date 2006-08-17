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

--select count(*) from project;
--select count(*) from project_info;
--select count(*) from project_audit;

--select count(*) from phase;
--select count(*) from phase_dependency;
--select count(*) from phase_criteria;

--select count(*) from submission;
--select count(*) from upload;

--select count(*) from resource;
--select count(*) from resource_info;
--select count(*) from resource_submission;
--select count(*) from notification;

--select count(*) from screening_task;
--select count(*) from screening_result;

--select count(*) from review;
--select count(*) from review_item;
--select count(*) from review_comment;
--select count(*) from review_item_comment;
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
select pt.name, pt.phase_type_id
from project p
INNER JOIN phase ph
ON p.project_id = ph.project_id
AND ph.phase_status_id = 2
AND ph.scheduled_start_time = (select min(scheduled_start_time) from phase where project_id = p.project_id and phase_status_id = 2)	
INNER JOIN phase_type_lu pt
ON ph.phase_type_id = pt.phase_type_id
