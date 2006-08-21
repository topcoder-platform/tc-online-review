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
        
select FIRST 5 p.project_id
	,(select value from project_info where project_id = p.project_id and project_info_type_id = 2) as component_id
	,(select value from project_info where project_id = p.project_id and project_info_type_id = 6) as component_name
	,(select count(*) from resource where project_id = p.project_id and resource_role_id = 1) 
		as num_registrations
	,(select count(*) from submission sub 
		inner join upload 
		on sub.upload_id = upload.upload_id 
		and upload.project_id = p.project_id
		where submission_status_id <> 5) 
		as num_submissions
	,(select count(*) from submission s
			inner join upload
			on upload.upload_id = s.upload_id
			where upload.project_id = p.project_id and submission_status_id in (1, 3, 4))
	 	as num_valid_submissions
	,(select count(*) from submission s
			inner join upload u
			on u.upload_id = s.upload_id
			where u.project_id = p.project_id and submission_status_id in (1, 4))
	 	as num_submissions_passed_review
	,p.project_category_id
	,case when p.project_category_id = 1 then 112 else 113 end 
			as phase_id
	,case when p.project_category_id = 1 then 'Design' else 'Development' end 
			as phase_desc
	,cat.category_id
	,cat.category_name as category_desc
	,case when ppd.actual_start_time is not null then ppd.actual_start_time
		else psd.actual_start_time
	end 
	 as posting_date
	,psd.actual_end_time as submitby_date
	,1 as level_id
	,pict.value as complete_date
	,(select phase_type_id from phase where phase_id =
		(select min(phase_id) from phase where project_id = p.project_id and phase_status_id = 2))
			as review_phase_id
	,(select name from phase_type_lu where phase_type_id = (select phase_type_id from phase where phase_id =
		(select min(phase_id) from phase where project_id = p.project_id and phase_status_id = 2)))	
			as review_phase_name
	,p.project_status_id 
		as project_stat_id
	,psl.name as project_stat_name
	,cat.viewable as viewable
	,pivi.value as version_id 
	,pivt.value as version_text
	,pirt.value as rating_date 
	,piwi.value as winner_id 

    from project p
    LEFT JOIN project_info pir
    ON pir.project_id = p.project_id 
    and pir.project_info_type_id = 5
    LEFT JOIN project_info pivi 
    ON pivi.project_id = p.project_id 
    and pivi.project_info_type_id = 3
    LEFT JOIN project_info pivt 
    ON pivt.project_id = p.project_id 
    and pivt.project_info_type_id = 7
    LEFT JOIN project_info pict 
    ON pict.project_id = p.project_id 
    and pict.project_info_type_id = 21
    LEFT JOIN project_info pirt 
    ON pirt.project_id = p.project_id 
    and pirt.project_info_type_id = 22
    LEFT JOIN project_info piwi 
    ON piwi.project_id = p.project_id 
    and piwi.project_info_type_id = 23
    LEFT JOIN categories cat
    ON cat.category_id = pir.value
    LEFT JOIN project_status_lu psl
    ON psl.project_status_id = p.project_status_id
    LEFT JOIN phase psd
    ON psd.project_id = p.project_id 
    and psd.phase_type_id = 2
    LEFT JOIN phase ppd
    ON ppd.project_id = p.project_id 
    and ppd.phase_type_id = 1
    
    
convert MM/dd/yyyy hh:mm a     
for rating_date/complete_date

version_id/winner_id long