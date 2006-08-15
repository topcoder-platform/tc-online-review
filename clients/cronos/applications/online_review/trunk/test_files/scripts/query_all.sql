select phase_instance_id,winner_id, project_type_id,project_stat_id,level_id,autopilot_ind,response_during_appeals_ind from project where project_id = 15165711 and cur_version = 1;;
select modify_reason from project where  project_id = 15165711;
select scorecard_type,template_id from project_template where project_id = 15165711;

select distinct comp_versions.* from comp_versions, project where comp_versions.comp_vers_id = project.comp_vers_id and project_id = 15165711;
select distinct cf.* from comp_forum_xref cf, project p where cf.comp_vers_id = p.comp_vers_id and project_id = 15165711;
select distinct cc.* from comp_catalog cc, comp_versions cv, project p where cc.component_id = cv.component_id and cv.comp_vers_id = p.comp_vers_id and project_id = 15165711;
select distinct cv.* from comp_version_dates cv, project p where cv.comp_vers_id = p.comp_vers_id and project_id = 15165711;

select * from sc_section_group where template_id = 1 or template_id = 2
select * from scorecard_section where template_id = 1 or template_id = 2
select q_template_v_id,q_template_id,project_type,scorecard_type,question_weight,question_seq_loc,section_id,question_type,cur_version,template_id from question_template where template_id = 1 or template_id = 2

select phase_inst_v_id,phase_instance_id,phase_id from phase_instance where project_id = 15165711 and cur_version = 1

select r_user_role_id,r_role_id,login_id,payment_info_id,r_resp_id from r_user_role where project_id = 15165711 and cur_version = 1 order by login_id

select user_id,old_rating,new_rating,raw_score,final_score,payment,placed,rating_ind,valid_submission_ind,reliability_ind,reliable_submission_ind,passed_review_ind from project_result where project_id = 15165711;
select * from rboard_application where project_id = 15165711;
select scorecard_id,scorecard_type,is_completed,is_pm_reviewed,author_id,submission_id,score,raw_score from scorecard where project_id = 15165711 and cur_version = 1;
select distinct p.* from payment_info p,r_user_role r where p.payment_info_id = r.payment_info_id and r.project_id = 15165711 and p.cur_version = 1;

select submission_id,submission_type,submitter_id,is_removed,cur_version,final_score,placement,passed_screening,advanced_to_review,passed_auto_screening from submission where project_id = 15165711;
select testcases_id,reviewer_id,testcase_type,cur_version from testcases where project_id = 15165711;

select sr.* from screening_results sr, submission s where s.project_id = 22461113 and s.submission_v_id = sr.submission_v_id;

select distinct sq.* from scorecard_question sq, scorecard s where s.project_id = 15165711 and s.scorecard_id = sq.scorecard_id and sq.cur_version = 1;
select distinct ar.agg_review_v_id,ar.agg_review_id,ar.agg_approval_id,ar.reviewer_id,ar.cur_version from agg_review ar, agg_worksheet ag where ag.project_id = 15165711 and ar.agg_worksheet_id = ag.agg_worksheet_id;
select fr.* from final_review fr, agg_worksheet ag where ag.project_id = 15165711 and fr.agg_worksheet_id = ag.agg_worksheet_id;
select * from fix_item where final_review_id = 15231918 and cur_version = 1;





--delete from scorecard_question;
--delete from scorecard_section;
--delete from scorecard_group;
--delete from scorecard;
--select count(*) from scorecard;
--select count(*) from scorecard_group;
--select count(*) from scorecard_section;
--select count(*) from scorecard_question;
--select count(*) from scorecard_template;
--select count(*) from sc_section_group;
--select count(*) from scorecard_section;
--select count(*) from question_template;
select * from sc_section_group where template_id not in (select template_id from scorecard_template);


--delete from review_item_comment;
--delete from review_comment;
--delete from review_item;
--delete from review;

--delete from screening_result;
--delete from screening_task;

--delete from notification;
--delete from resource_submission;
--delete from resource_info;
--delete from resource;

--delete from submission;
--delete from upload;

--delete from phase_criteria;
--delete from phase_dependency;
--delete from phase;

--delete from project_audit;
--delete from project_info;
--delete from project;

select count(*) from project;
select count(*) from project_info;
select count(*) from project_audit;

select count(*) from phase;
select count(*) from phase_dependency;
select count(*) from phase_criteria;

select count(*) from submission;
select count(*) from upload;

select count(*) from resource;
select count(*) from resource_info;
select count(*) from resource_submission;
select count(*) from notification;

select count(*) from screening_task;
select count(*) from screening_result;

select count(*) from review;
select count(*) from review_item;
select count(*) from review_comment;
select count(*) from review_item_comment;