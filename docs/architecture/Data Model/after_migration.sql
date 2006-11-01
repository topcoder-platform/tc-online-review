update  project_info set value = '0' where value = '0.0';
update  resource_info set value = '0' where value = '0.0';

update project_info set value = replace(value, '.0', '') where value
like '%.0';
update resource_info set value = replace(value, '.0', '') where
resource_info_type_id = 7 and value like '%.0'

update project_info set value=9926572 where value=0 and project_info_type_id=5;