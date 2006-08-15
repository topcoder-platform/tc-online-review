select pr.project_id,
    pr.user_id,
    sc.author_id as reviewer_id,
    sc.score as final_score,
    sc.scorecard_id,
             (select distinct template_id from question_template qt, scorecard_question sq
             where qt.q_template_v_id = sq.q_template_v_id and sq.cur_version = 1 and qt.cur_version = 1 and sq.scorecard_id = sc.scorecard_id)  as scorecard_template_id
    from project_result pr, submission s, scorecard sc
    where s.cur_version = 1
	    and s.project_id = pr.project_id
	    and s.submitter_id = pr.user_id
	    and s.submission_type = 1
	    and s.is_removed = 0
	    and sc.scorecard_type = 1
	    and sc.is_completed = 1
	    and sc.submission_id = s.submission_id
	    and sc.project_id = pr.project_id
	    and sc.cur_version = 1
	    and (pr.modify_date > ? OR sc.modify_date > ?)

select r.project_id,
    (select value from resource_info where resource_id = s.resource_id and resource_info_type_id = 1) as user_id,
    (select value from resource_info where resource_id = r.resource_id and resource_info_type_id = 1) as reviewer_id,
    r.score as final_score,
    r.review_id as scorecard_id,
    r.scorecard_id as scorecard_template_id
    from review r, submission s
    where r.submission_id = s.submission_id 
	    and (r.modify_date > ? OR s.modify_date > ?)	 
        and r.resource_id in (select resource_id from resource where resource_role_id = 2 && resource_role_id = 3)      
        
        
        
	select template_id as scorecard_template_id, 
        scorecard_type as scorecard_type_id, 
        template_name as scorecard_type_desc  
        from scorecard_template where modify_date > ?;
        
	select scorecard_id as scorecard_template_id, 
        scorecard_type_id as scorecard_type_id, 
        (select name from scorecard_type_lu where scorecard_type_id = scorecard.scorecard_type_id) as scorecard_type_desc  
        from scorecard where modify_date > ?;
                    