--select count(*) tt, project_id from upload group by project_id order by tt
--select * from submission;
--select project_id from upload where upload_id = 1400
--select count(*) from review;
--select count(*) from review_comment;
--select count(*) from review_item;
--select count(*) from review_item_comment;
-- select * from project_result;

	select first 5 ric.review_item_comment_id as appeal_id
		,ri.review_item_id  as scorecard_question_id  
        ,ri.review_id as scorecard_id
        ,(select value from resource_info where resource_id = u.resource_id and resource_info_type_id = 1) as user_id
        ,(select value from resource_info where resource_id = r.resource_id and resource_info_type_id = 1) as reviewer_id
        ,u.project_id
        ,ri.answer as answer
        ,ric.content as appeal_text
        ,ric_resp.content as appeal_response
        ,ric_resp.extra_info as raw_answer
        from review_item_comment ric
        	inner join review_item  ri
        	on ric.review_item_id = ri.review_item_id 
        	inner join review r 
        	on ri.review_id = r.review_id  
        	inner join submission s
        	on r.submission_id = s.submission_id        	
        	inner join upload u
        	on u.upload_id = s.upload_id
        	inner join resource res
        	on r.resource_id = res.resource_id
        	and res.resource_role_id in (2, 3, 4, 5, 6, 7)
        	inner join scorecard_question sq
        	on ri.scorecard_question_id = sq.scorecard_question_id
        	and sq.scorecard_question_type_id = 3
        	inner join review_item_comment ric_resp
        	on ric_resp.review_item_id = ri.review_item_id 
        	and ric_resp.comment_type_id = 5
        where ric.comment_type_id = 4