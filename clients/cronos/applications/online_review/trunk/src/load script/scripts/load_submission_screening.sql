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

"select u.project_id " +
    ",(select value from resource_info where resource_id = u.resource_id and resource_info_type_id = 1) as user_id " +
    ",(select value from resource_info where resource_id = r.resource_id and resource_info_type_id = 1) as reviewer_id " +
    ",r.score as final_score " +
    ",r.review_id as scorecard_id " +
    ",r.scorecard_id as scorecard_template_id " +
    "from review r  " +
    ",	inner join submission s	on r.submission_id = s.submission_id " +
    ",  inner join upload u on u.upload_id = s.upload_id            " +
    ",  inner join resource res on res.resource_id = r.resource_id and resource_role_id in (2, 3)  " +
    "where (r.modify_date > ? OR s.modify_date > ?)	  "
        
	select template_id as scorecard_template_id, 
        scorecard_type as scorecard_type_id, 
        template_name as scorecard_type_desc  
        from scorecard_template where modify_date > ?;
        
	select scorecard_id as scorecard_template_id, 
        s.scorecard_type_id as scorecard_type_id, 
        stl.name as scorecard_type_desc  
        from scorecard s
        	inner join scorecard_type_lu stl on s.scorecard_type_id = stl.scorecard_type_id
        	where s.modify_date > ?;
                    