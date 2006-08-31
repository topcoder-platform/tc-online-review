--select count(*) project from project where cur_version = 1;
--select count(*) project_template from project_template;
--select count(*) scorecard from scorecard where cur_version = 1;

--select count(*) submission from submission where cur_version = 1;
--select count(*) testcases from testcases where cur_version = 1;

--select count(*) phase_instance from phase_instance where cur_version = 1;

--select count(*) comp_catalog from comp_catalog;
--select count(*) comp_forum_xref from comp_forum_xref;
--select count(*) comp_version_dates from comp_version_dates;
--select count(*) comp_versions from comp_versions;

--select count(*) project_result from project_result;
--select count(*) payment_info from payment_info where cur_version = 1;
--select count(*) rboard_application from rboard_application;
--select count(*) r_user_role from r_user_role where cur_version = 1;

--select count(*) agg_response from agg_response where cur_version = 1;
--select count(*) agg_review from agg_review where cur_version = 1;
--select count(*) agg_worksheet from agg_worksheet where cur_version = 1;
--select count(*) appeal from appeal where cur_version = 1;
--select count(*) final_review from final_review where cur_version = 1;
--select count(*) fix_item from fix_item where cur_version = 1;
--select count(*) scorecard_question from scorecard_question where cur_version = 1;
--select count(*) subjective_resp from subjective_resp where cur_version = 1;
--select count(*) testcase_question from testcase_question where cur_version = 1;

--select count(*) screening_results from screening_results;

--select *  from scorecard where cur_version = 1 and project_id = 12034917;
--select *  from agg_worksheet where cur_version = 1 and project_id = 20186915;
--select *  from agg_review where cur_version = 1 and agg_worksheet_id = 13359224 ;
--select *  from final_review where cur_version = 1 and agg_worksheet_id = 13359224 ;
--select agg_response_id,subjective_resp_id,agg_resp_stat_id,agg_worksheet_id  from agg_response where cur_version = 1 and agg_worksheet_id = 13359224 ;
--select * from scorecard where cur_version = 1 and project_id = 12034917 and scorecard_id in (select scorecard_id from scorecad_question) ;
--select distinct scorecard_id from scorecard_question where question_id in (select question_id from

-- select distinct question_id from subjective_resp where subjective_resp_id = 13273629;
-- select distinct scorecard_id from scorecard_question where question_id=13273628;
--select submission_id, submission_date, project_id from submission where project_id = 22527753 and cur_version = 1;
--select scorecard_id, modify_date, project_id  from scorecard where cur_version = 1 and project_id = 22527753;
--select phase_instance_id,start_date,end_date,phase_id,project_id from phase_instance where cur_version = 1 and project_id = 22527753;
--select count(scorecard_id) from scorecard where cur_version = 1 and scorecard_type = 2 ;
--select * from project_result where project_id = 21816206;
--select * from scorecard where cur_version = 1 and project_id = 21816206;
--select * from submission where cur_version = 1 and project_id = 21816206;
--select * from agg_worksheet where cur_version = 1 and project_id = 21816206;

--select count(user_id) tt, max(project_id)  from project_result where new_rating is not null group by project_id having count(user_id) == 1 order by tt;
--select count(user_id) tt, max(project_id)  from project_result group by project_id having count(user_id) == 1 order by tt;
--select * from command;
-- select distinct evaluation_id from scorecard_question;
-- select distinct question_type from question_template;
-- select distinct q.question_type, evaluation_id, q.question_text from scorecard_question s, question_template q where s.cur_version = 1 and s.q_template_v_id = q.q_template_v_id order by q.question_type;
--select count(*) tt from scorecard_question where cur_version = 1 group by question_id order by tt;
--select count(appeal_id) from appeal a, scorecard_question sq where sq.question_id = a.question_id and sq.cur_version = 1 and a.cur_version = 1 group by a.question_id;
--select * from scorecard_template order by template_id > 2;

--select distinct old_reliability from project_result;
--select distinct handle_lower from user;

--select * from categories
--select first 5 * from project_result;
--select first 5 * from user_reliability;
--select first 5 * from contract_payment_xref;
--select distincat phase_id from phase_instance
--select * from phase;
--select name from query  ;

--select distinct s.project_id, author_id from scorecard s 
--where s.cur_version = 1 and author_id not in (select login_id from r_user_role r where r.cur_version = 1 and r.project_id = s.project_id)
--SELECT distinct r_role_id from r_user_role;
--select first 5 distinct aggregator_id from agg_worksheet;
--select first 5 * from project_result where payment is not null and placed is not null and rating_ind is not null order by project_id desc; 
--select distinct phase_id from rboard_application;
--select submission_v_id,submission_id, placement, submission_type,project_id from submission where cur_version = 1 and placement is not null and project_id = 8566974;
--user_id,project_id,old_rating,new_rating,old_reliability,new_reliability,raw_score,final_score,payment,placed,rating_ind,valid_submission_ind,reliability_ind,create_date,modify_date,reliable_submission_ind,passed_review_ind,point_adjustment
--select distinct template_id, phase_id from phase_instance where phase_id in (2,3) and cur_version = 1
--select count(*) tt, max(project_id) from scorecard where cur_version = 1 group by project_id order by tt;

--select * from project where project_id = 22764150 and cur_version =1;

--select sum(question_weight) yy, section_id from question_template where cur_version = 1 group by section_id order by yy;

--select * from project_template where project_id = 7437871;
--select * from scorecard where project_id = 7437871;
--select distinct project_id from project p where cur_version = 1 and not exists (select * from project_template pt where pt.project_id = p.project_id)
--select * from r_user_role where project_id = 7439266;
--select * from agg_worksheet where project_id = 7439266;
--select review_resp_id, review_resp_name, phase_id from review_resp
--select * from rboard_user;
select distinct phase_id from project;
