	"select a.appeal_id,  
        "sq.q_template_v_id as scorecard_question_id,  
        "s.scorecard_id,  
        (select submitter_id from submission where submission_id = s.submission_id and cur_version=1) as user_id,  
        "s.author_id as reviewer_id,  
        "s.project_id,  
        "tq.total_tests as final_num_tests,  
        "tq.total_pass as final_num_passed,  
        "a.appeal_text,  
        "a.appeal_response,  
        "a.raw_total_tests,  
        "a.raw_total_pass  
        "from appeal a, scorecard s, testcase_question tq, scorecard_question sq  
        "where   s.scorecard_id = sq.scorecard_id   
        "and sq.question_id = tq.question_id   
        "and tq.question_id = a.question_id  
        "and tq.cur_version = 1  
        "and s.cur_version = 1   
        "and a.cur_version = 1  
        "and a.is_resolved = 1  
        "and project_id = ?  
        "and (a.modify_date>? OR s.modify_date>? OR tq.modify_date>? OR sq.modify_date>?)

            
	select ric.review_item_comment_id as appeal_id,
		ri.review_item_id  as scorecard_question_id,  
        ri.review_id as scorecard_id,  
        (select value from resource_info where resource_id = s.resource_id and resource_info_type_id = 1) as user_id,
        (select value from resource_info where resource_id = r.resource_id and resource_info_type_id = 1) as reviewer_id,
        s.project_id,  
        ri.answer as answer,
        ric.content as appeal_text,
        (select content from review_item_comment ric_resp 
        	where ric_resp.review_item_id = ri.review_item_id and comment_type_id = 5) as appeal_response,
        (select extra_info from review_item_comment ric_resp 
        	where ric_resp.review_item_id = ri.review_item_id and comment_type_id = 5) as raw_answer
        from review_item_comment ric, submission s , 
        	review r 
        	inner join resource res
        	on r.resource_id = res.resource_id
        	and res.resource_role_id >= 2 and res.resource_role_id <= 7, 
        	review_item  ri
        	inner join scorecard_question sq
        	on ri.scorecard_question_id = sq.scorecard_question_id
        	and sq.scorecard_question_type_id = 3
        where ric.review_item_id = ri.review_item_id 
        and ri.review_id = r.review_id  
        and r.submission_id = s.submission_id
        and ric.comment_type_id = 4
        and (ric.modify_date > ? OR ri.modify_date > ? OR r.modify_date > ? OR s.modify_date > ?)
        
        
answer parse for final_num_passed/final_num_tests        
raw_answer parse for raw_num_passed/raw_num_tests

Notes: it's load with project one by one