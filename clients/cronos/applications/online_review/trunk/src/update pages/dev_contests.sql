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
		AND ri.resource_info_type_id = 4
		WHERE r.project_id = p.project_id
		AND r.resource_role_id = 1
		AND ri.value <> 'Not Rated')
		as total_rated_inquiries,		
	(SELECT COUNT(*)
		FROM resource r
		INNER JOIN resource_info ri
		ON ri.resource_id = r.resource_id
		AND ri.resource_info_type_id = 4
		WHERE r.project_id = p.project_id
		AND r.resource_role_id = 1
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
		AND ri.resource_info_type_id = 4
		WHERE r.project_id = p.project_id
		AND r.resource_role_id = 1)
		as total_inquiries,
	p.project_id,
	(select value from project_info where project_id = p.project_id and project_info_type_id = 7) 
		as version_text,
	(SELECT MAX(scheduled_end_time) FROM phase WHERE project_id = p.project_id AND phase_type_id = 1)
  		as reg_end_date,
  	0 as max_unrated_registrants,
  	(select count(*) from contest_project_xref where project_id = p.project_id) 
  		as tourny_project,
  	(select category_id from comp_categories where component_id = pi_ci.value and category_id = 22774808)
  		as aol_brand	
	FROM project p
	INNER JOIN phase ps
	ON ps.project_id = p.project_id
	AND ps.phase_type_id = 2
    and ps.scheduled_start_time <= CURRENT
    and ps.scheduled_end_time > CURRENT
	AND ps.phase_status_id = 2
	INNER JOIN project_info pi
	ON pi.project_id = p.project_id
	AND pi.project_info_type_id = 12
	AND pi.value = 'Yes'
	INNER JOIN project_info pi_ci
	ON pi_ci.project_id = p.project_id
	AND pi_ci.project_info_type_id = 2
	INNER JOIN project_info pci
	ON pci.project_id = p.project_id
	AND pci.project_info_type_id = 5
	and pci.value in (8459260,5801776,5801777,5801778,5801779)
	WHERE p.project_status_id = 1
	AND p.project_category_id = 2
	order by initial_submission_date, catalog_name, component_name