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
        (select value from resource_info where resource_id = s.resource_id and resource_info_type_id = 1) as user_id,
        (select value from resource_info where resource_id = r.resource_id and resource_info_type_id = 1) as reviewer_id,
        s.project_id,  
        ri.answer evaluation_id  
        from review_item  ri, review r, submission s  
        where ri.review_id = r.review_id  
        and r.submission_id = s.submission_id
        and project_id = ? 
        and (ri.modify_date > ? OR r.modify_date > ? OR s.modify_date > ?)
        	and r.resource_id in (select resource_id from resource where resource_role_id >= 2 && resource_role_id <= 7)     
        	
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
        (select value from resource_info where resource_id = s.resource_id and resource_info_type_id = 1) as user_id,
        (select value from resource_info where resource_id = r.resource_id and resource_info_type_id = 1) as reviewer_id,
        s.project_id,  
        ri.answer answer  
        from review_item  ri, review r, submission s, scorecard_question sq
        where ri.review_id = r.review_id  
        and r.submission_id = s.submission_id
        and (ri.modify_date > ? OR r.modify_date > ? OR s.modify_date > ?)
    	and ri.scorecard_question_id = sq.scorecard_question_id
    	and sq.scorecard_question_type_id = 3
    	and r.resource_id in (select resource_id from resource where resource_role_id >= 2 && resource_role_id <= 7)             

num_tests/num_passed parsed from answer         	