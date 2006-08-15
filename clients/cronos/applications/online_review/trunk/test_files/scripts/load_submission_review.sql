select sc.project_id,
        s.submitter_id as user_id,
        author_id as reviewer_id,
        sc.raw_score as raw_score,
        score as final_score,
        (select count(distinct appeal_id) from appeal where appealer_id = s.submitter_id and cur_version = 1
        and question_id in (select question_id from scorecard_question where scorecard_id = sc.scorecard_id)) as num_appeals,
        0 as num_successful_appeals,
        rur.r_resp_id as review_resp_id,
        scorecard_id,
        (select distinct template_id from question_template qt, scorecard_question sq
        where qt.q_template_v_id = sq.q_template_v_id and sq.cur_version = 1 and qt.cur_version = 1 and sq.scorecard_id = sc.scorecard_id)  as scorecard_template_id
        from scorecard sc, submission s, r_user_role rur
        where s.cur_version = 1
        and s.submission_id = sc.submission_id
        and rur.project_id = sc.project_id
        and rur.cur_version = 1
        and rur.login_id = sc.author_id
        and rur.r_role_id = 3
        and s.submission_type = 1 
        and s.is_removed = 0
        and sc.project_id = ?
        and sc.scorecard_type = 2
        and sc.is_completed = 1
        and sc.cur_version = 1
        and (sc.modify_date > ? OR s.modify_date > ? OR rur.modify_date > ?)
        
select  s.project_id,
        (select value from resource_info where resource_id = s.resource_id and resource_info_type_id = 1) as user_id,
        (select value from resource_info where resource_id = r.resource_id and resource_info_type_id = 1) as reviewer_id,
        (select value from resource_info where resource_id = s.resource_id and resource_info_type_id = 10) as raw_score, -- 10 Initial Score
        r.score as final_score,  
        (select count(review_item_comment_id) from review_item_comment where comment_type_id = 4 and review_item_id in (select review_item_id from review where review_id = r.review_id)) as num_appeals,  -- 4 Appeal
        (select count(review_item_comment_id) from review_item_comment where extra_info = 'Succeeded' and comment_type_id = 4 and review_item_id in (select review_item_id from review where review_id = r.review_id)) as num_successful_appeals,  -- 4 Appeal
		case when exist (select 1 from resource where resource_id = r.resource_id and resource_role_id = 7) then 1 
		else (case when exist (select 1 from resource where resource_id = r.resource_id and resource_role_id = 6) then 2,
				else (case when exist (select 1 from resource where resource_id = r.resource_id and resource_role_id = 5) then 3
						else null end) end) end as review_resp_id,
        r.review_id as scorecard_id,
        r.scorecard_id as scorecard_template_id
        from review r, submission s
        where r.submission_id = s.submission_id 
        	and r.resource_id in (select resource_id from resource where resource_role_id >= 4 && resource_role_id <= 7)      
        	and (r.modify_date > ?)
        	