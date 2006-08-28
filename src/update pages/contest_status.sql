 	SELECT 
	(select value from project_info where project_id = p.project_id and project_info_type_id = 6) 
		as component_name
	,(select value from project_info where project_id = p.project_id and project_info_type_id = 7) 
		as version_text
	,(select category_name from categories where category_id = pci.value)
		as catalog_name	
	,pt.name 
		as current_phase
    ,ph.scheduled_end_time as reg_end_date
    ,(SELECT MAX(scheduled_end_time)
		FROM phase
		WHERE project_id = p.project_id
		AND phase_type_id = 10)
		as final_review_end_date
	,(select value from project_info where project_id = p.project_id and project_info_type_id = 23) 
		as winner
	,(select value from project_info where project_id = p.project_id and project_info_type_id = 24) 
		as second	
     ,p.project_category_id + 111 
     	as phase_id
     ,p.project_id
     ,(select name from project_category_lu where project_category_id = p.project_category_id)
     	as phase
     ,(select handle_lower from user where user_id = 
     	(select value from project_info where project_id = p.project_id and project_info_type_id = 23)) as winner_sort
     ,(select handle_lower from user where user_id = 
     	(select value from project_info where project_id = p.project_id and project_info_type_id = 24)) as second_sort
	,(select value from project_info where project_id = p.project_id and project_info_type_id = 2) 
      as component_id
	,(select value from project_info where project_id = p.project_id and project_info_type_id = 3) 
      as version
     ,(select viewable from categories where category_id = pci.value) 
     	as viewable   		
	,(SELECT COUNT(*)
		FROM resource r
		INNER JOIN resource_info ri
		ON ri.resource_id = r.resource_id
		AND ri.resource_info_type_id = 4
		AND ri.value <> 'Not Rated'
		WHERE r.project_id = p.project_id
		AND r.resource_role_id = 1)
		as rated_count	
	,(SELECT COUNT(*)
		FROM resource r
		INNER JOIN resource_info ri
		ON ri.resource_id = r.resource_id
		AND ri.resource_info_type_id = 4
		AND ri.value = 'Not Rated'
		WHERE r.project_id = p.project_id
		AND r.resource_role_id = 1)
		as unrated_count
  	,(select category_id from comp_categories where component_id = pi_ci.value and category_id = 22774808)
  		as aol_brand	
	FROM project p
	INNER JOIN phase ph
	ON p.project_id = ph.project_id
	AND ph.phase_status_id = 2
	AND ph.scheduled_start_time = (select min(scheduled_start_time) from phase where project_id = p.project_id and phase_status_id = 2)	
	and ph.scheduled_start_time < current	
	AND ph.phase_type_id = 2
	INNER JOIN phase_type_lu pt
	ON ph.phase_type_id = pt.phase_type_id
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
	AND p.project_category_id + 111 = @ph@
 	order by final_review_end_date asc