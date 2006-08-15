select pr.project_id, pr.user_id, 
   case when exists(select '1' from submission s where s.cur_version = 1 and s.project_id = pr.project_id 
   and s.submitter_id = pr.user_id and submission_type = 1 and is_removed = 0) then 1 
   when exists(select '1' from submission s where s.cur_version = 1 and s.project_id = pr.project_id 
   and s.submitter_id = pr.user_id and submission_type = 1 and is_removed = 1) then 0 
   else pr.valid_submission_ind end as submit_ind, 
   case  when exists(select '1' from submission s where s.cur_version = 1 and s.project_id = pr.project_id 
   and s.submitter_id = pr.user_id and submission_type = 1 and is_removed = 1) then 0 
   else pr.valid_submission_ind  end as valid_submission_ind, 
   pr.raw_score, 
   pr.final_score, 
   case when exists (select create_time from component_inquiry where project_id = p.project_id and user_id = pr.user_id) then 
   (select min(create_time) from component_inquiry where project_id = p.project_id and user_id = pr.user_id) else 
   (select min(create_time) from component_inquiry where component_id = cc.component_id and user_id = pr.user_id) end as inquire_timestamp, 
   (select submission_date from submission s where s.cur_version = 1 and s.project_id = pr.project_id and s.submitter_id = pr.user_id and submission_type = 1 and is_removed = 0) as submit_timestamp, 
   (select max(pm_review_timestamp) from scorecard where scorecard_type = 2 and is_completed = 1 and submission_id = 
      (select submission_id from submission s where s.cur_version = 1 and s.project_id = pr.project_id and s.submitter_id = pr.user_id and submission_type = 1 and is_removed = 0) 
    and project_id = pr.project_id and cur_version = 1) as review_completed_timestamp, 
   (select count(*) from project_result pr where project_id = p.project_id and pr.passed_review_ind = 1) as num_submissions_passed_review, 
   pr.payment, pr.old_rating, pr.new_rating, 
   pr.old_reliability, pr.new_reliability, pr.placed, pr.rating_ind, pr.reliability_ind, pr.passed_review_ind, p.project_stat_id, pr.point_adjustment, pr.current_reliability_ind, pr.reliable_submission_ind 
   from project_result pr, 
   project p, 
   comp_versions cv, 
   comp_catalog cc 
   where p.project_id = pr.project_id 
   and p.cur_version = 1  
   and cv.comp_vers_id = p.comp_vers_id 
   and cc.component_id = cv.component_id 
   and (p.modify_date > ? OR cv.modify_date > ? OR cc.modify_date > ? OR pr.modify_date > ?)
   
   
select pr.* 
   from project_result pr, 
   project p
   where p.project_id = pr.project_id 
   and (p.modify_date > ?)
   
select pr.project_id, pr.user_id, 
   case when exists(select '1' from submission s where s.cur_version = 1 and s.project_id = pr.project_id 
   and s.submitter_id = pr.user_id and submission_type = 1 and is_removed = 0) then 1 
   when exists(select '1' from submission s where s.cur_version = 1 and s.project_id = pr.project_id 
   and s.submitter_id = pr.user_id and submission_type = 1 and is_removed = 1) then 0 
   else pr.valid_submission_ind end as submit_ind, 
   case  when exists(select '1' from submission s where s.cur_version = 1 and s.project_id = pr.project_id 
   and s.submitter_id = pr.user_id and submission_type = 1 and is_removed = 1) then 0 
   else pr.valid_submission_ind  end as valid_submission_ind, 
   pr.raw_score, 
   pr.final_score, 
   case when exists (select create_time from component_inquiry where project_id = p.project_id and user_id = pr.user_id) then 
   (select min(create_time) from component_inquiry where project_id = p.project_id and user_id = pr.user_id) else 
   (select min(create_time) from component_inquiry where component_id = cc.component_id and user_id = pr.user_id) end as inquire_timestamp, 
   (select submission_date from submission s where s.cur_version = 1 and s.project_id = pr.project_id and s.submitter_id = pr.user_id and submission_type = 1 and is_removed = 0) as submit_timestamp, 
   (select max(pm_review_timestamp) from scorecard where scorecard_type = 2 and is_completed = 1 and submission_id = 
      (select submission_id from submission s where s.cur_version = 1 and s.project_id = pr.project_id and s.submitter_id = pr.user_id and submission_type = 1 and is_removed = 0) 
    and project_id = pr.project_id and cur_version = 1) as review_completed_timestamp, 
   (select count(*) from project_result pr where project_id = p.project_id and pr.passed_review_ind = 1) as num_submissions_passed_review, 
   pr.payment, pr.old_rating, pr.new_rating, 
   pr.old_reliability, pr.new_reliability, pr.placed, pr.rating_ind, pr.reliability_ind, pr.passed_review_ind, p.project_stat_id, pr.point_adjustment, pr.current_reliability_ind, pr.reliable_submission_ind 
   from project_result pr, 
   project p, 
   comp_versions cv, 
   comp_catalog cc 
   where p.project_id = pr.project_id 
   and p.cur_version = 1  
   and cv.comp_vers_id = p.comp_vers_id 
   and cc.component_id = cv.component_id 
   and (p.modify_date > ? OR cv.modify_date > ? OR cc.modify_date > ? OR pr.modify_date > ?)   