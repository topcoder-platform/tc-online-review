select p.project_id, cc.component_id, cc.component_name,
        case when exists (select 1 from component_inquiry where project_id = p.project_id) then
         (select count(*) from component_inquiry where project_id = p.project_id)
           else  (case when exists (select 1 from component_inquiry where component_id = cc.component_id and version = cv.version)  then
                   (select count(distinct user_id) from component_inquiry ci1 where component_id = cc.component_id and version = cv.version) else null end) end as num_registrations,
        case when exists  (select '1' from submission where cur_version = 1 and project_id = p.project_id and submission_type = 1 and is_removed = 0) then
            (select count(*) from submission where cur_version = 1 and project_id = p.project_id and submission_type = 1 and is_removed = 0)
        else (select count(*) from project_result pr where project_id = p.project_id and valid_submission_ind = 1) end
          as num_submissions, 
         (select count(*) from project_result pr where project_id = p.project_id and valid_submission_ind = 1 and not 
        exists (select * from submission where cur_version = 1 and project_id = p.project_id and submission_type = 1 and is_removed = 1 and submitter_id = pr.user_id)) 
           as num_valid_submissions,
         (select count(*) from project_result pr where project_id = p.project_id and pr.passed_review_ind = 1) as num_submissions_passed_review,
        (select avg(case when raw_score is null then 0 else raw_score end) from project_result where project_id = p.project_id and raw_score is not null) as avg_raw_score,
        (select avg(case when final_score is null then 0 else final_score end) from project_result where project_id = p.project_id and final_score is not null) as avg_final_score,
        case when p.project_type_id = 1 then 112 else 113 end as phase_id,
        (select description from phase where phase_id = (case when p.project_type_id = 1 then 112 else 113 end)) as phase_desc,
        cc.root_category_id as category_id,
        (select category_name from categories where category_id = case when cc.root_category_id in (5801776,5801778) then 5801776 when cc.root_category_id in (5801777,5801779) then 5801777 else cc.root_category_id end) as category_desc,
        (select start_date from phase_instance where phase_id = 1 and cur_version = 1 and project_id = p.project_id) as posting_date,
        (select end_date from phase_instance where phase_id = 1 and cur_version = 1 and project_id = p.project_id) as submitby_date,
        (select max(level_id) from comp_version_dates where comp_vers_id = p.comp_vers_id and phase_id = p.project_type_id + 111) as level_id,
        p.complete_date,
        rp.review_phase_id,
        rp.review_phase_name,
        ps.project_stat_id,
        ps.project_stat_name,
        cat.viewable,
        cv.version as version_id,
        cv.version_text as version_text,
        p.rating_date,
        p.winner_id
        from project p,
        comp_versions cv,
        comp_catalog cc," +
        categories cat,
        phase_instance pi,
        review_phase rp," +
        project_status ps
        where p.cur_version = 1 
        and cv.comp_vers_id = p.comp_vers_id
        and cc.component_id = cv.component_id
        and cc.root_category_id = cat.category_id
        and pi.cur_version = 1
        and pi.phase_instance_id = p.phase_instance_id
        and rp.review_phase_id = pi.phase_id
        and ps.project_stat_id = p.project_stat_id
        and (p.modify_date > ? OR cv.modify_date > ? OR cc.modify_date > ? OR pi.modify_date > ?
        
        
select p.project_id,
	(select value from project_info where project_id = p.project_id and project_info_type_id = 2) as component_id,
	(select value from project_info where project_id = p.project_id and project_info_type_id = 6) as component_name, -- 6 project_name
	(selec count(*) from resource where project_id = p.project_id and resource_role_id = 1) as num_registrations, -- 1 is submitter
	(selec count(*) from submission where project_id = p.project_id) as num_submissions,
	case when exist (select 1 from phase where project_id = p.project_id and phase_type_id = 3 and phase_status_id = 3)    -- 3 is screening, 3 is closed
		then (select count(*) from submission where project_id = p.project_id and (submission_status_id = 1 or submission_status_id = 3 or submission_status_id = 4))
	 	else 0 end as num_valid_submissions, -- screening phase should closed
	case when exist (select 1 from phase where project_id = p.project_id and phase_type_id = 4 and phase_status_id = 3)    -- 4 is review, 3 is closed
		then (select count(*) from submission where project_id = p.project_id and (submission_status_id = 1 or submission_status_id = 3 or submission_status_id = 4))
	 	else 0 end as num_submissions_passed_review, -- review phase should closed	 	
	(select avg(value) from resource_info where resource_info_type_id = 10 and resource_id in (select resource_id from resource where project_id = p.project_id)) as avg_raw_score,
	(select avg(value) from resource_info where resource_info_type_id = 11 and resource_id in (select resource_id from resource where project_id = p.project_id)) as avg_final_score,
	p.project_category_id,
	case when p.project_category_id = 1 then 112 else 113 end as phase_id,
	case when p.project_category_id = 1 then 'Design' else 'Development' end as phase_desc,
	p.project_category_id as category_id,
	(select name from project_category_lu where project_category_id = p.project_category_id) as category_desc,
	(select actual_start_date from phase where project_id = p.project_id and phase_type_id = 1) as posting_date,
	(select actual_end_date from phase where project_id = p.project_id and phase_type_id = 2) as submitby_date,
	1 as level_id,
	(select value from project_info where project_id = p.project_id and project_info_type_id = 21) as complete_date,
	(select min(phase_id) from phase where project_id = p.project_id and phase_status_id = 2) as review_phase_id,
	(select name from phase_type_lu where phase_type_id = (select phase_type_id from phase where phase_id =
		(select min(phase_id) from phase where project_id = p.project_id and phase_status_id = 2)))	as review_phase_name,
	p.project_status_id as project_stat_id,
	(select name from project_status_lu where project_status_id = p.project_status_id) as project_stat_name,
	(select viewable from categories where category_id = 
			(select value from project_info where project_id = p.project_id and project_info_type_id = 5))
	 as viewable, -- 5 is root catalog id
	(select value from project_info where project_id = p.project_id and project_info_type_id = 3) as version_id, -- 3 Version ID
	(select value from project_info where project_id = p.project_id and project_info_type_id = 7) as version_text, -- 7 Project Version
	(select value from project_info where project_id = p.project_id and project_info_type_id = 22) as rating_date, -- 22 Rated Timestamp
	(select value from project_info where project_id = p.project_id and project_info_type_id = 23) as winner_id -- 23 Winner External Reference ID
    from project p
    where p.modify_date > ?