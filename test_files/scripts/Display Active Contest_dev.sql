    SELECT cqx.query_id,
     q.text,
     q.name,
     q.ranking,
     q.column_index,
     cqx.sort_order 
    FROM command c, query q, command_query_xref cqx 
    WHERE (c.command_desc = 'active_contests' or c.command_desc = 'contest_status')
    AND cqx.command_id = c.command_id 
    AND q.query_id = cqx.query_id 
    ORDER BY cqx.sort_order ASC 
    
command_id,command_desc,command_group_id
26195,active_contests,13337
26300,contest_status,13337    

select pi1.end_date initial_submission_date
     , cvd.price
     , cc.component_name
     , cc.component_id
     , cv.phase_id
     , cl.description
     , pcat.category_name catalog_name
     , (select count(*) 
          from component_inquiry ci
         where ci.component_id = cc.component_id
           and ci.phase = cv.phase_id
           and ci.project_id = p.project_id
           and ci.version = cv.version
           and ci.rating > 0) as total_rated_inquiries
     , (select count(*) 
          from component_inquiry ci
         where ci.component_id = cc.component_id
           and ci.phase = cv.phase_id
           and ci.project_id = p.project_id
           and ci.version = cv.version
           and ci.rating = 0) as total_unrated_inquiries
      , (SELECT count(submission_id)
            FROM submission sb
           where sb.project_id = p.project_id
             and sb.cur_version = 1
             and sb.submission_type = 1
             and sb.is_removed = 0 ) as total_submissions
      , (select count(*) 
          from component_inquiry ci
         where ci.component_id = cc.component_id
           and ci.phase = cv.phase_id
           and ci.project_id = p.project_id
           and ci.version = cv.version) as total_inquiries
      , p.project_id
      , cv.version_text
      , (pi1.start_date + 3 UNITS DAY) as reg_end_date
      , p.max_unrated_registrants
      , (select count(*) from contest_project_xref where project_id = p.project_id) as tourny_project
      , (select category_id from comp_categories where component_id = cc.component_id and category_id = 22774808) as aol_brand
    from comp_versions cv
       , comp_catalog cc
       , categories pcat
       , project p
       , phase_instance pi1
       , comp_version_dates cvd
       , comp_level cl
   where cv.comp_vers_id = p.comp_vers_id
     and cv.phase_id-111 = p.project_type_id
     and p.cur_version = 1
     and cc.component_id = cv.component_id
     and cc.status_id = 102
     and pi1.start_date <= CURRENT
     and pi1.end_date > CURRENT
     and pcat.category_id = cc.root_category_id
     and pi1.project_id = p.project_id
     and pi1.phase_id = 1
     and pi1.cur_version = 1
     and p.PROJECT_STAT_ID in (1,3)
     and pcat.category_id in (8459260,5801776,5801777,5801778,5801779)
     and cvd.comp_vers_id = cv.comp_vers_id
     and cvd.phase_id = cv.phase_id
     and cvd.level_id = cl.level_id
     and cv.phase_id = 113
     and p.project_id not in (21298488, 21298522, 21306079, 21306123, 21505040, 21505247, 21542912, 21542960, 21657411, 21657449)
order by initial_submission_date, pcat.category_name, cc.component_name



	SELECT (SELECT MAX(scheduled_end_time) FROM phase WHERE project_id = p.project_id AND phase_type_id = 2) 
		as initial_submission_date,	
	(select value from project_info where project_id = p.project_id and project_info_type_id = 16) 
		as price,
	(select value from project_info where project_id = p.project_id and project_info_type_id = 6) 
		as component_name,
	(select category_name from categories where category_id = pci.value)
		as catalog_name	,
	(SELECT COUNT(*)
		FROM resource r
		INNER JOIN resource_info ri
		ON ri.resource_id = r.resource_id
		AND ri.resource_info_type_id = (SELECT resource_info_type_id FROM resource_info_type_lu WHERE name = 'Rating')
		WHERE r.project_id = p.project_id
		AND r.resource_role_id = (SELECT resource_role_id FROM resource_role_lu WHERE name = 'Submitter')
		AND ri.value <> 'Not Rated')
		as total_rated_inquiries,		
	(SELECT COUNT(*)
		FROM resource r
		INNER JOIN resource_info ri
		ON ri.resource_id = r.resource_id
		AND ri.resource_info_type_id = (SELECT resource_info_type_id FROM resource_info_type_lu WHERE name = 'Rating')
		WHERE r.project_id = p.project_id
		AND r.resource_role_id = (SELECT resource_role_id FROM resource_role_lu WHERE name = 'Submitter')
		AND ri.value = 'Not Rated')
		as total_unrated_inquiries,
	(SELECT COUNT(*)
		FROM submission s
		INNER JOIN upload u
		ON u.upload_id = s.upload_id
		WHERE u.project_id = p.project_id)
		as total_submissions,
	(SELECT COUNT(*)
		FROM resource r
		INNER JOIN resource_info ri
		ON ri.resource_id = r.resource_id
		AND ri.resource_info_type_id = (SELECT resource_info_type_id FROM resource_info_type_lu WHERE name = 'Rating')
		WHERE r.project_id = p.project_id
		AND r.resource_role_id = (SELECT resource_role_id FROM resource_role_lu WHERE name = 'Submitter'))
		as total_inquiries,
	p.project_id,
	(select value from project_info where project_id = p.project_id and project_info_type_id = 7) 
		as version_text,
	(SELECT MAX(scheduled_end_time) FROM phase WHERE project_id = p.project_id AND phase_type_id = 1)
  		as reg_end_date,
  	0 as max_unrated_registrants,
  	(select count(*) from contest_project_xref where project_id = p.project_id) 
  		as tourny_project,
  	(select 1 from categories where pci.value = 22774808) 
  		as aol_brand	
	FROM project p
	INNER JOIN phase pr
	ON pr.project_id = p.project_id
	AND pr.phase_type_id = (SELECT phase_type_id FROM phase_type_lu WHERE name = 'Registration')
	INNER JOIN phase ps
	ON ps.project_id = p.project_id
	AND ps.phase_type_id = (SELECT phase_type_id FROM phase_type_lu WHERE name = 'Submission')
	INNER JOIN project_info pi
	ON pi.project_id = p.project_id
	AND pi.project_info_type_id = (SELECT project_info_type_id FROM project_info_type_lu WHERE name = 'Public')
	AND pi.value = 'Yes'
	INNER JOIN project_info pci
	ON pci.project_id = p.project_id
	AND pci.project_info_type_id = (SELECT project_info_type_id FROM project_info_type_lu WHERE name = 'Root Catalog ID')
	WHERE p.project_status_id = (SELECT project_status_id FROM project_status_lu WHERE name = 'Active')
	AND p.project_category_id = 2
	AND (pr.phase_status_id = (SELECT phase_status_id FROM phase_status_lu WHERE name = 'Open')
	OR ps.phase_status_id = (SELECT phase_status_id FROM phase_status_lu WHERE name = 'Open'))

price is string type
no phase_id need

contest_project_xref table isn't migrate and hold original project_id, but it is used to retrieve tourny_project
hwo to retrieve max_unrated_registrants

project_category_id 1: design 2: development