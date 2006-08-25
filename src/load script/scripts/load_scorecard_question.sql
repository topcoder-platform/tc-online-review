	select  qt.q_template_v_id as scorecard_question_id, 
        qt.template_id as scorecard_template_id, 
        qt.question_text, 
        qt.question_weight, 
        qt.section_id, 
        ss.section_name as section_desc, 
        ss.section_weight, 
        ss.group_id as section_group_id, 
        sg.group_name as section_group_desc,  
        round (sg.group_seq_loc) || \.\ || round(ss.section_seq_loc) || \.\ || round(qt.question_seq_loc)  as question_desc,  
        sg.group_seq_loc, ss.section_seq_loc, qt.question_seq_loc  
        from question_template qt, scorecard_section ss, sc_section_group sg  
        where qt.cur_version = 1  
        and ss.section_id = qt.section_id  
        and sg.group_id = ss.group_id  
        //we can't make this join because then if we load before a review fills out a new scorecard, we'll never get the new questions over
        //and exists (select q_template_v_id from scorecard_question where qt.q_template_v_id = q_template_v_id) 
        and (qt.modify_date > ?) 
        order by scorecard_template_id, sg.group_seq_loc, ss.section_seq_loc, qt.question_seq_loc 
        
	select qt.scorecard_question_id as scorecard_question_id
        ,sg.scorecard_id as scorecard_template_id
        ,qt.description || qt.guideline as question_text
        ,round(qt.weight) as question_weight
        ,qt.scorecard_section_id as section_id
        ,ss.name as section_desc
        ,round(ss.weight*sg.weight/100) as section_weight
        ,ss.scorecard_group_id as section_group_id
        ,sg.name as section_group_desc
        ,sg.sort || '.' || ss.sort || '.' || qt.sort  as question_desc
        ,sg.sort as group_seq_loc
        ,ss.sort as section_seq_loc 
        ,qt.sort as question_seq_loc
        from scorecard_question qt
        	inner join scorecard_section ss
        	on qt.scorecard_section_id = ss.scorecard_section_id  
        	inner join scorecard_group sg  
        	on ss.scorecard_group_id = sg.scorecard_group_id  
        where (qt.modify_date > ?) 
	        order by scorecard_template_id, group_seq_loc, section_seq_loc, question_seq_loc      