	select sq.q_template_v_id  as scorecard_question_id,  
        s.scorecard_id,  
        (select submitter_id from submission where submission_id = s.submission_id and cur_version=1) as user_id,  
        s.author_id as reviewer_id,  
        s.project_id,  
        sq.evaluation_id  
        from scorecard_question sq, scorecard s  
        where s.scorecard_id = sq.scorecard_id  
        and s.cur_version = 1  
        and sq.cur_version = 1  
        and project_id = ? 
        and (sq.modify_date > ? OR s.modify_date > ?);
        
	select ri.review_item_id  as scorecard_question_id,  
        ri.review_id as scorecard_id,  
        (select value from resource_info where resource_id = u.resource_id and resource_info_type_id = 1) as user_id,
        (select value from resource_info where resource_id = r.resource_id and resource_info_type_id = 1) as reviewer_id,
        u.project_id,  
        ri.answer evaluation_id  
        from review_item  ri
        	inner join review r
        	on ri.review_id = r.review_id 
        	inner join resource res
        	on r.resource_id = res.resource_id
        	and res.resource_role_id in (2,3,4,5,6,7) 
        	inner join submission s  
        	on r.submission_id = s.submission_id
        	inner join upload u
        	on u.upload_id = s.upload_id
        where project_id = ? 
        and (ri.modify_date > ? OR r.modify_date > ? OR s.modify_date > ?)
        	
need convert answer	evaluation_id
    	1,null
    	1,5
    	1,6
    	2,null
    	2,1
    	2,2
    	2,3
    	2,4
    	3,null
    	4,null
    	4,7
    	4,8



    select sq.q_template_v_id as scorecard_question_id, 
                   s.scorecard_id, 
                   (select submitter_id from submission where submission_id = s.submission_id and cur_version=1) as user_id, 
                   s.author_id as reviewer_id, 
                   s.project_id, 
                   tq.total_tests as num_tests, 
                   tq.total_pass as num_passed 
            from testcase_question tq, scorecard s, scorecard_question sq 
            where s.scorecard_id = sq.scorecard_id 
            and sq.question_id = tq.question_id 
            and sq.cur_version = 1 
            and s.cur_version = 1  
            and tq.cur_version = 1 
            and (tq.modify_date > ? OR sq.modify_date > ? OR s.modify_date > ?)
            
	select ri.review_item_id  as scorecard_question_id,  
        ri.review_id as scorecard_id,  
        (select value from resource_info where resource_id = u.resource_id and resource_info_type_id = 1) as user_id,
        (select value from resource_info where resource_id = r.resource_id and resource_info_type_id = 1) as reviewer_id,
        u.project_id,  
        ri.answer answer 
        from review_item  ri
        	inner join review r
        	on ri.review_id = r.review_id 
        	inner join resource res
        	on r.resource_id = res.resource_id
        	and res.resource_role_id in (2,3,4,5,6,7) 
        	inner join submission s  
        	on r.submission_id = s.submission_id
        	inner join upload u
        	on u.upload_id = s.upload_id
        	inner join scorecard_question sq
        	on sq.scorecard_question_id = ri.scorecard_question_id
        	and sq.scorecard_question_type_id = 3
        where (ri.modify_date > ? OR r.modify_date > ? OR s.modify_date > ?)

num_tests/num_passed parsed from answer like 9/10
Yes/No to 7/8
1-4   	