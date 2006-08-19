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

select p.project_id
	,(select value from project_info where project_id = p.project_id and project_info_type_id = 2) as component_id
	,(select value from project_info where project_id = p.project_id and project_info_type_id = 6) as component_name
	,(select count(*) from resource where project_id = p.project_id and resource_role_id = 1) 
		as num_registrations
	,(select count(*) from submission sub 
		inner join upload 
		on sub.upload_id = upload.upload_id 
		and upload.project_id = p.project_id
		where submission_status_id <> 5) 
		as num_submissions
    from project p
    INNER JOIN resource r
    ON r.project_id = p.project_id
    INNER JOIN project_info pir
    ON pir.project_id = p.project_id 
    and pir.project_info_type_id = 5
    INNER JOIN project_info pivi 
    ON pivi.project_id = p.project_id 
    and pivi.project_info_type_id = 3
    INNER JOIN project_info pivt 
    ON pivt.project_id = p.project_id 
    and pivt.project_info_type_id = 7
    INNER JOIN project_info pict 
    ON pict.project_id = p.project_id 
    and pict.project_info_type_id = 21
    INNER JOIN project_info pirt 
    ON pirt.project_id = p.project_id 
    and pirt.project_info_type_id = 22
    INNER JOIN project_info piwi 
    ON piwi.project_id = p.project_id 
    and piwi.project_info_type_id = 23
    INNER JOIN categories cat
    ON cat.category_id = pir.value
    INNER JOIN project_status_lu psl
    ON psl.project_status_id = p.project_status_id
    INNER JOIN phase psd
    ON psd.project_id = p.project_id 
    and psd.phase_type_id = 2
    INNER JOIN phase ppd
    ON ppd.project_id = p.project_id 
    and ppd.phase_type_id = 1
    where p.project_id = 461