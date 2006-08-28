update project_info set value='N/A' 
where project_info_type_id = (select project_info_type_id from project_info_type_lu where name='Public')
and project_id in (21298488, 21298522, 21306079, 21306123, 21505040, 21505247, 21542912, 21542960, 21657411, 21657449)