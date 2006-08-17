select cc.component_name
     , cv.version_text
     , c.category_name as catalog_name
     , rp.review_phase_name as current_phase
     , pi1.end_date as reg_end_date
     , pi3.end_date as final_review_end_date
     , pr1.user_id as winner
     , pr2.user_id as second
     , p.project_type_id + 111 as phase_id
     , p.project_id
     , ph.description as phase
     , u1.handle_lower as winner_sort
     , u2.handle_lower as second_sort
     , (select count(*) from component_inquiry where project_id = p.project_id and rating = 0) as unrated_count
     , (select count(*) from component_inquiry where project_id = p.project_id and rating > 0) as rated_count
     , cc.component_id
     , cv.version
     , c.viewable
     , (select category_id from comp_categories where component_id = cc.component_id and category_id = 22774808) as aol_brand
  from project p
     , phase_instance pi1
     , phase_instance pi3
     , comp_catalog cc
     , comp_versions cv
     , categories c
     , phase_instance pi2
     , review_phase rp
     , outer (project_result pr1, user u1)
     , outer (project_result pr2, user u2)
     , phase ph
 where p.project_id = pi1.project_id
   and pi1.phase_id = 1
   and p.project_id = pi3.project_id
   and pi3.phase_id = 7
   and pi3.cur_version = 1
   and pr1.project_id = p.project_id 
   and pr1.placed = 1
   and pr2.project_id = p.project_id 
   and pr2.placed = 2
   and pi1.cur_version = 1
   and pr1.user_id = u1.user_id
   and pr2.user_id = u2.user_id
   and p.cur_version = 1
   and p.phase_instance_id = pi2.phase_instance_id
   and pi2.cur_version = 1
   and pi2.phase_id = rp.review_phase_id 
   and p.project_stat_id in (1,3)
   and p.project_type_id+111 = ph.phase_id
   and ph.phase_id = @ph@
   and p.comp_vers_id = cv.comp_vers_id
   and cv.component_id = cc.component_id
   and cc.root_category_id = c.category_id
   and c.category_id in  (8459260,5801776,5801777,5801778,5801779)
   and p.project_id not in (21298488, 21298522, 21306079, 21306123, 21505040, 21505247, 21542912, 21542960, 21632776, 21657411, 21657449, 21663385, 21663427, 21771067, 21771121)
   and pi1.start_date < current
 order by final_review_end_date asc
 
 
 
 	SELECT 
	(select value from project_info where project_id = p.project_id and project_info_type_id = 6) 
		as component_name,
	(select value from project_info where project_id = p.project_id and project_info_type_id = 7) 
		as version_text,
	(select category_name from categories where category_id = pci.value)
		as catalog_name	,
	pt.name 
		as current_phase,
    ph.scheduled_end_time as reg_end_date
    (SELECT MAX(scheduled_end_time)
		FROM phase
		WHERE project_id = p.project_id
		AND phase_type_id = (SELECT phase_type_id FROM phase_type_lu WHERE name = 'Final Review')
		as final_review_end_date,
	(select value from project_info where project_id = p.project_id and project_info_type_id = 23) 
		as winner,
	(select value from project_info where project_id = p.project_id and project_info_type_id = 24) 
		as second,		
     p.project_category_id + 111 
     	as phase_id,
     p.project_id,
     ph.name 
     	as phase,
     u1.handle_lower as winner_sort
     , u2.handle_lower as second_sort
	(select value from project_info where project_id = p.project_id and project_info_type_id = 2) 
      as component_id,     
	(select value from project_info where project_id = p.project_id and project_info_type_id = 3) 
      as version
     (select viewable from categories where category_id = pci.value) 
     	as viewable,     		
	(SELECT COUNT(*)
		FROM resource r
		INNER JOIN resource_info ri
		ON ri.resource_id = r.resource_id
		AND ri.resource_info_type_id = (SELECT resource_info_type_id FROM resource_info_type_lu WHERE name = 'Rating')
		WHERE r.project_id = p.project_id
		AND r.resource_role_id = (SELECT resource_role_id FROM resource_role_lu WHERE name = 'Submitter')
		AND ri.value <> 'Not Rated')
		as rated_count,		
	(SELECT COUNT(*)
		FROM resource r
		INNER JOIN resource_info ri
		ON ri.resource_id = r.resource_id
		AND ri.resource_info_type_id = (SELECT resource_info_type_id FROM resource_info_type_lu WHERE name = 'Rating')
		WHERE r.project_id = p.project_id
		AND r.resource_role_id = (SELECT resource_role_id FROM resource_role_lu WHERE name = 'Submitter')
		AND ri.value = 'Not Rated')
		as unrated_count,
  	(select 1 from categories where pci.value = 22774808) 
  		as aol_brand	
	FROM project p
	INNER JOIN phase ph
	ON p.project_id = ph.project_id
	AND ph.phase_status_id = 2
	AND ph.scheduled_start_time = (select min(scheduled_start_time) from phase where project_id = p.project_id and phase_status_id = 2)		
	INNER JOIN phase_type_lu pt
	ON ph.phase_type_id = pt.phase_type_id
	INNER JOIN project_info pi
	ON pi.project_id = p.project_id
	AND pi.project_info_type_id = (SELECT project_info_type_id FROM project_info_type_lu WHERE name = 'Public')
	AND pi.value = 'Yes'
	INNER JOIN project_info pci
	ON pci.project_id = p.project_id
	AND pci.project_info_type_id = (SELECT project_info_type_id FROM project_info_type_lu WHERE name = 'Root Catalog ID')
	WHERE p.project_status_id = (SELECT project_status_id FROM project_status_lu WHERE name = 'Active')
	AND p.project_category_id = 2
	AND pr.phase_status_id = (SELECT phase_status_id FROM phase_status_lu WHERE name = 'Open')
	
	
reg_end_date maybe should be modify to cur_due_date	
     , u1.handle_lower as winner_sort
     , u2.handle_lower as second_sort