--
-- This script populates the database tables with sample data for various projects to be
-- used for testing/review purposes only.
--

INSERT INTO link_type_lu (link_type_id, link_type_name) VALUES (1, 'Conceptualization Spec Review');
INSERT INTO link_type_lu (link_type_id, link_type_name) VALUES (2, 'Conceptualization Round 2');
INSERT INTO link_type_lu (link_type_id, link_type_name) VALUES (3, 'Module Architecture');
INSERT INTO link_type_lu (link_type_id, link_type_name) VALUES (4, 'Spawned Component');


-- Deleted project (Component Design)
INSERT INTO comp_catalog (component_id, current_version, short_desc, component_name, description, function_desc,
                          create_time, status_id, root_category_id, modify_date, public_ind)
       VALUES (1, 1, 'Deleted Component Short Desc', 'Deleted Component', 'Deleted Component Description',
              'Deleted Component Functional Description', CURRENT, 0, 5801779, CURRENT, 1);

INSERT INTO comp_versions (comp_vers_id, component_id, version, version_text, create_time, phase_id, phase_time,
                           price, comments, modify_date, suspended_ind)
       VALUES (1, 1, 1, '1.0', CURRENT, 112, CURRENT, 100.00, 'Comments', CURRENT, 0);

INSERT INTO project (project_id, project_status_id, project_category_id, create_user, create_date, modify_user,
                     modify_date)
       VALUES (1, 3, 1, 132456, CURRENT, 132456, CURRENT);

INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (1, 1, '1', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (1, 2, '1', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (1, 3, '1', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (1, 4, '0', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (1, 5, '5801779', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (1, 6, 'Deleted Component', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (1, 7, '1.0', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (1, 9, 'On', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (1, 10, 'On', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (1, 11, 'On', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (1, 12, 'Yes', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (1, 13, 'Yes', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (1, 14, 'Open', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (1, 16, '0.0', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (1, 26, 'On', 132456, CURRENT, 132456, CURRENT);

INSERT INTO project_phase (project_phase_id, project_id, phase_type_id, phase_status_id, fixed_start_time,
                           scheduled_start_time, scheduled_end_time, actual_start_time, actual_end_time, duration,
                           create_user, create_date, modify_user, modify_date)
       VALUES (1, 1, 1, 1, CURRENT, CURRENT, CURRENT, CURRENT, CURRENT, 1000, 132456, CURRENT, 132456, CURRENT);

INSERT INTO resource (resource_id, resource_role_id, project_id, project_phase_id, create_user, create_date,
                      modify_user, modify_date) VALUES (1, 13, 1, null, 132456, CURRENT, 132456, CURRENT);
INSERT INTO resource_info (resource_id, resource_info_type_id, value, create_user, create_date, modify_user,
                           modify_date)
       VALUES (1, 1, '132456', 132456, CURRENT, 132456, CURRENT);
INSERT INTO resource_info (resource_id, resource_info_type_id, value, create_user, create_date, modify_user,
                           modify_date)
       VALUES (1, 2, 'heffan', 132456, CURRENT, 132456, CURRENT);


-- Active project (Component Design)
INSERT INTO comp_catalog (component_id, current_version, short_desc, component_name, description, function_desc,
                          create_time, status_id, root_category_id, modify_date, public_ind)
       VALUES (2, 1, 'Active Component Short Desc', 'Active Component', 'Active Component Description',
              'Active Component Functional Description', CURRENT, 0, 5801779, CURRENT, 1);

INSERT INTO comp_versions (comp_vers_id, component_id, version, version_text, create_time, phase_id, phase_time,
                           price, comments, modify_date, suspended_ind)
       VALUES (2, 2, 1, '1.0', CURRENT, 112, CURRENT, 100.00, 'Comments', CURRENT, 0);

INSERT INTO project (project_id, project_status_id, project_category_id, create_user, create_date, modify_user,
                     modify_date)
       VALUES (2, 1, 1, 132456, CURRENT, 132456, CURRENT);

INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (2, 1, '2', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (2, 2, '2', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (2, 3, '1', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (2, 4, '0', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (2, 5, '5801779', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (2, 6, 'Active Component Design', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (2, 7, '1.0', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (2, 9, 'On', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (2, 10, 'On', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (2, 11, 'On', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (2, 12, 'Yes', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (2, 13, 'Yes', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (2, 14, 'Open', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (2, 16, '0.0', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (2, 26, 'On', 132456, CURRENT, 132456, CURRENT);

INSERT INTO project_phase (project_phase_id, project_id, phase_type_id, phase_status_id, fixed_start_time,
                           scheduled_start_time, scheduled_end_time, actual_start_time, actual_end_time, duration,
                           create_user, create_date, modify_user, modify_date)
       VALUES (2, 2, 1, 1, CURRENT, CURRENT, CURRENT, CURRENT, CURRENT, 1000, 132456, CURRENT, 132456, CURRENT);

INSERT INTO resource (resource_id, resource_role_id, project_id, project_phase_id, create_user, create_date,
                      modify_user, modify_date) VALUES (2, 13, 2, null, 132456, CURRENT, 132456, CURRENT);
INSERT INTO resource_info (resource_id, resource_info_type_id, value, create_user, create_date, modify_user,
                           modify_date)
       VALUES (2, 1, '132456', 132456, CURRENT, 132456, CURRENT);
INSERT INTO resource_info (resource_id, resource_info_type_id, value, create_user, create_date, modify_user,
                           modify_date)
       VALUES (2, 2, 'heffan', 132456, CURRENT, 132456, CURRENT);                             


-- Inactive project (Component Design)
INSERT INTO comp_catalog (component_id, current_version, short_desc, component_name, description, function_desc,
                          create_time, status_id, root_category_id, modify_date, public_ind)
       VALUES (3, 1, 'Inactive Component Short Desc', 'Inactive Component', 'Inactive Component Description',
              'Inactive Component Functional Description', CURRENT, 0, 5801779, CURRENT, 1);

INSERT INTO comp_versions (comp_vers_id, component_id, version, version_text, create_time, phase_id, phase_time,
                           price, comments, modify_date, suspended_ind)
       VALUES (3, 3, 1, '1.0', CURRENT, 112, CURRENT, 300.00, 'Comments', CURRENT, 0);

INSERT INTO project (project_id, project_status_id, project_category_id, create_user, create_date, modify_user,
                     modify_date)
       VALUES (3, 2, 1, 132456, CURRENT, 132456, CURRENT);

INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (3, 1, '3', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (3, 2, '3', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (3, 3, '1', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (3, 4, '0', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (3, 5, '5801779', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (3, 6, 'Inactive Component Design', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (3, 7, '1.0', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (3, 9, 'On', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (3, 10, 'On', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (3, 11, 'On', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (3, 12, 'Yes', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (3, 13, 'Yes', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (3, 14, 'Open', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (3, 16, '0.0', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (3, 26, 'On', 132456, CURRENT, 132456, CURRENT);

INSERT INTO project_phase (project_phase_id, project_id, phase_type_id, phase_status_id, fixed_start_time,
                           scheduled_start_time, scheduled_end_time, actual_start_time, actual_end_time, duration,
                           create_user, create_date, modify_user, modify_date)
       VALUES (3, 3, 1, 1, CURRENT, CURRENT, CURRENT, CURRENT, CURRENT, 1000, 132456, CURRENT, 132456, CURRENT);

INSERT INTO resource (resource_id, resource_role_id, project_id, project_phase_id, create_user, create_date,
                      modify_user, modify_date) VALUES (3, 13, 3, null, 132456, CURRENT, 132456, CURRENT);
INSERT INTO resource_info (resource_id, resource_info_type_id, value, create_user, create_date, modify_user,
                           modify_date)
       VALUES (3, 1, '132456', 132456, CURRENT, 132456, CURRENT);
INSERT INTO resource_info (resource_id, resource_info_type_id, value, create_user, create_date, modify_user,
                           modify_date)
       VALUES (3, 2, 'heffan', 132456, CURRENT, 132456, CURRENT);

-- Active project #2 (Component Design)
INSERT INTO comp_catalog (component_id, current_version, short_desc, component_name, description, function_desc,
                          create_time, status_id, root_category_id, modify_date, public_ind)
       VALUES (4, 1, 'Active Component #2 Short Desc', 'Active Component #2', 'Active Component Description #2',
              'Active Component Functional Description #2', CURRENT, 0, 5801779, CURRENT, 1);

INSERT INTO comp_versions (comp_vers_id, component_id, version, version_text, create_time, phase_id, phase_time,
                           price, comments, modify_date, suspended_ind)
       VALUES (4, 4, 1, '1.0', CURRENT, 112, CURRENT, 100.00, 'Comments', CURRENT, 0);

INSERT INTO project (project_id, project_status_id, project_category_id, create_user, create_date, modify_user,
                     modify_date)
       VALUES (4, 1, 1, 132456, CURRENT, 132456, CURRENT);

INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (4, 1, '4', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (4, 2, '4', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (4, 3, '1', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (4, 4, '0', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (4, 5, '5801779', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (4, 6, 'Active Component Design #2', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (4, 7, '1.0', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (4, 9, 'On', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (4, 10, 'On', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (4, 11, 'On', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (4, 12, 'Yes', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (4, 13, 'Yes', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (4, 14, 'Open', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (4, 16, '0.0', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (4, 26, 'On', 132456, CURRENT, 132456, CURRENT);

INSERT INTO project_phase (project_phase_id, project_id, phase_type_id, phase_status_id, fixed_start_time,
                           scheduled_start_time, scheduled_end_time, actual_start_time, actual_end_time, duration,
                           create_user, create_date, modify_user, modify_date)
       VALUES (4, 4, 1, 1, CURRENT, CURRENT, CURRENT, CURRENT, CURRENT, 1000, 132456, CURRENT, 132456, CURRENT);

INSERT INTO resource (resource_id, resource_role_id, project_id, project_phase_id, create_user, create_date,
                      modify_user, modify_date) VALUES (4, 13, 4, null, 132456, CURRENT, 132456, CURRENT);
INSERT INTO resource_info (resource_id, resource_info_type_id, value, create_user, create_date, modify_user,
                           modify_date)
       VALUES (4, 1, '132456', 132456, CURRENT, 132456, CURRENT);
INSERT INTO resource_info (resource_id, resource_info_type_id, value, create_user, create_date, modify_user,
                           modify_date)
       VALUES (4, 2, 'heffan', 132456, CURRENT, 132456, CURRENT);

-- Active project #3 (Component Design)
INSERT INTO comp_catalog (component_id, current_version, short_desc, component_name, description, function_desc,
                          create_time, status_id, root_category_id, modify_date, public_ind)
       VALUES (5, 1, 'Active Component #3 Short Desc', 'Active Component #3', 'Active Component Description #3',
              'Active Component Functional Description #3', CURRENT, 0, 5801779, CURRENT, 1);

INSERT INTO comp_versions (comp_vers_id, component_id, version, version_text, create_time, phase_id, phase_time,
                           price, comments, modify_date, suspended_ind)
       VALUES (5, 5, 1, '1.0', CURRENT, 112, CURRENT, 100.00, 'Comments', CURRENT, 0);

INSERT INTO project (project_id, project_status_id, project_category_id, create_user, create_date, modify_user,
                     modify_date)
       VALUES (5, 1, 1, 132456, CURRENT, 132456, CURRENT);

INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (5, 1, '5', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (5, 2, '5', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (5, 3, '1', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (5, 4, '0', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (5, 5, '5801779', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (5, 6, 'Active Component Design #3', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (5, 7, '1.0', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (5, 9, 'On', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (5, 10, 'On', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (5, 11, 'On', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (5, 12, 'Yes', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (5, 13, 'Yes', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (5, 14, 'Open', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (5, 16, '0.0', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (5, 26, 'On', 132456, CURRENT, 132456, CURRENT);

INSERT INTO project_phase (project_phase_id, project_id, phase_type_id, phase_status_id, fixed_start_time,
                           scheduled_start_time, scheduled_end_time, actual_start_time, actual_end_time, duration,
                           create_user, create_date, modify_user, modify_date)
       VALUES (5, 5, 1, 1, CURRENT, CURRENT, CURRENT, CURRENT, CURRENT, 1000, 132456, CURRENT, 132456, CURRENT);

INSERT INTO resource (resource_id, resource_role_id, project_id, project_phase_id, create_user, create_date,
                      modify_user, modify_date) VALUES (5, 13, 5, null, 132456, CURRENT, 132456, CURRENT);
INSERT INTO resource_info (resource_id, resource_info_type_id, value, create_user, create_date, modify_user,
                           modify_date)
       VALUES (5, 1, '132456', 132456, CURRENT, 132456, CURRENT);
INSERT INTO resource_info (resource_id, resource_info_type_id, value, create_user, create_date, modify_user,
                           modify_date)
       VALUES (5, 2, 'heffan', 132456, CURRENT, 132456, CURRENT);

-- Active project #4 (Component Design)
INSERT INTO comp_catalog (component_id, current_version, short_desc, component_name, description, function_desc,
                          create_time, status_id, root_category_id, modify_date, public_ind)
       VALUES (6, 1, 'Active Component #4 Short Desc', 'Active Component #4', 'Active Component Description #4',
              'Active Component Functional Description #4', CURRENT, 0, 5801779, CURRENT, 1);

INSERT INTO comp_versions (comp_vers_id, component_id, version, version_text, create_time, phase_id, phase_time,
                           price, comments, modify_date, suspended_ind)
       VALUES (6, 6, 1, '1.0', CURRENT, 112, CURRENT, 100.00, 'Comments', CURRENT, 0);

INSERT INTO project (project_id, project_status_id, project_category_id, create_user, create_date, modify_user,
                     modify_date)
       VALUES (6, 1, 1, 132456, CURRENT, 132456, CURRENT);

INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (6, 1, '6', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (6, 2, '6', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (6, 3, '1', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (6, 4, '0', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (6, 5, '5801779', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (6, 6, 'Active Component Design #4', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (6, 7, '1.0', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (6, 9, 'On', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (6, 10, 'On', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (6, 11, 'On', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (6, 12, 'Yes', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (6, 13, 'Yes', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (6, 14, 'Open', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (6, 16, '0.0', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (6, 26, 'On', 132456, CURRENT, 132456, CURRENT);

INSERT INTO project_phase (project_phase_id, project_id, phase_type_id, phase_status_id, fixed_start_time,
                           scheduled_start_time, scheduled_end_time, actual_start_time, actual_end_time, duration,
                           create_user, create_date, modify_user, modify_date)
       VALUES (6, 6, 1, 1, CURRENT, CURRENT, CURRENT, CURRENT, CURRENT, 1000, 132456, CURRENT, 132456, CURRENT);

INSERT INTO resource (resource_id, resource_role_id, project_id, project_phase_id, create_user, create_date,
                      modify_user, modify_date) VALUES (6, 13, 6, null, 132456, CURRENT, 132456, CURRENT);
INSERT INTO resource_info (resource_id, resource_info_type_id, value, create_user, create_date, modify_user,
                           modify_date)
       VALUES (6, 1, '132456', 132456, CURRENT, 132456, CURRENT);
INSERT INTO resource_info (resource_id, resource_info_type_id, value, create_user, create_date, modify_user,
                           modify_date)
       VALUES (6, 2, 'heffan', 132456, CURRENT, 132456, CURRENT);


-- Active project #5 (Component Design)
INSERT INTO comp_catalog (component_id, current_version, short_desc, component_name, description, function_desc,
                          create_time, status_id, root_category_id, modify_date, public_ind)
       VALUES (7, 1, 'Active Component #5 Short Desc', 'Active Component #5', 'Active Component Description #5',
              'Active Component Functional Description #5', CURRENT, 0, 5801779, CURRENT, 1);

INSERT INTO comp_versions (comp_vers_id, component_id, version, version_text, create_time, phase_id, phase_time,
                           price, comments, modify_date, suspended_ind)
       VALUES (7, 7, 1, '1.0', CURRENT, 112, CURRENT, 100.00, 'Comments', CURRENT, 0);

INSERT INTO project (project_id, project_status_id, project_category_id, create_user, create_date, modify_user,
                     modify_date)
       VALUES (7, 1, 1, 132456, CURRENT, 132456, CURRENT);

INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (7, 1, '7', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (7, 2, '7', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (7, 3, '1', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (7, 4, '0', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (7, 5, '5801779', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (7, 6, 'Active Component Design #5', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (7, 7, '1.0', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (7, 9, 'On', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (7, 10, 'On', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (7, 11, 'On', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (7, 12, 'Yes', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (7, 13, 'Yes', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (7, 14, 'Open', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (7, 16, '0.0', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (7, 26, 'On', 132456, CURRENT, 132456, CURRENT);

INSERT INTO project_phase (project_phase_id, project_id, phase_type_id, phase_status_id, fixed_start_time,
                           scheduled_start_time, scheduled_end_time, actual_start_time, actual_end_time, duration,
                           create_user, create_date, modify_user, modify_date)
       VALUES (7, 7, 1, 1, CURRENT, CURRENT, CURRENT, CURRENT, CURRENT, 1000, 132456, CURRENT, 132456, CURRENT);

INSERT INTO resource (resource_id, resource_role_id, project_id, project_phase_id, create_user, create_date,
                      modify_user, modify_date) VALUES (7, 13, 7, null, 132456, CURRENT, 132456, CURRENT);
INSERT INTO resource_info (resource_id, resource_info_type_id, value, create_user, create_date, modify_user,
                           modify_date)
       VALUES (7, 1, '132456', 132456, CURRENT, 132456, CURRENT);
INSERT INTO resource_info (resource_id, resource_info_type_id, value, create_user, create_date, modify_user,
                           modify_date)
       VALUES (7, 2, 'heffan', 132456, CURRENT, 132456, CURRENT);


-- Active project #6 (Component Design)
INSERT INTO comp_catalog (component_id, current_version, short_desc, component_name, description, function_desc,
                          create_time, status_id, root_category_id, modify_date, public_ind)
       VALUES (8, 1, 'Active Component #6 Short Desc', 'Active Component #6', 'Active Component Description #6',
              'Active Component Functional Description #6', CURRENT, 0, 5801779, CURRENT, 1);

INSERT INTO comp_versions (comp_vers_id, component_id, version, version_text, create_time, phase_id, phase_time,
                           price, comments, modify_date, suspended_ind)
       VALUES (8, 8, 1, '1.0', CURRENT, 112, CURRENT, 100.00, 'Comments', CURRENT, 0);

INSERT INTO project (project_id, project_status_id, project_category_id, create_user, create_date, modify_user,
                     modify_date)
       VALUES (8, 1, 1, 132456, CURRENT, 132456, CURRENT);

INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (8, 1, '8', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (8, 2, '8', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (8, 3, '1', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (8, 4, '0', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (8, 5, '5801779', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (8, 6, 'Active Component Design #6', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (8, 7, '1.0', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (8, 9, 'On', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (8, 10, 'On', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (8, 11, 'On', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (8, 12, 'Yes', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (8, 13, 'Yes', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (8, 14, 'Open', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (8, 16, '0.0', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (8, 26, 'On', 132456, CURRENT, 132456, CURRENT);

INSERT INTO project_phase (project_phase_id, project_id, phase_type_id, phase_status_id, fixed_start_time,
                           scheduled_start_time, scheduled_end_time, actual_start_time, actual_end_time, duration,
                           create_user, create_date, modify_user, modify_date)
       VALUES (8, 8, 1, 1, CURRENT, CURRENT, CURRENT, CURRENT, CURRENT, 1000, 132456, CURRENT, 132456, CURRENT);

INSERT INTO resource (resource_id, resource_role_id, project_id, project_phase_id, create_user, create_date,
                      modify_user, modify_date) VALUES (8, 13, 8, null, 132456, CURRENT, 132456, CURRENT);
INSERT INTO resource_info (resource_id, resource_info_type_id, value, create_user, create_date, modify_user,
                           modify_date)
       VALUES (8, 1, '132456', 132456, CURRENT, 132456, CURRENT);
INSERT INTO resource_info (resource_id, resource_info_type_id, value, create_user, create_date, modify_user,
                           modify_date)
       VALUES (8, 2, 'heffan', 132456, CURRENT, 132456, CURRENT);

-- Initially linked project #7 (Component Design)
INSERT INTO comp_catalog (component_id, current_version, short_desc, component_name, description, function_desc,
                          create_time, status_id, root_category_id, modify_date, public_ind)
       VALUES (9, 1, 'Initially Linked Project #7 Short Desc', 'Initially Linked Project #7', 'Initially Linked Project Description #7',
              'Initially Linked Project Functional Description #7', CURRENT, 0, 5801779, CURRENT, 1);

INSERT INTO comp_versions (comp_vers_id, component_id, version, version_text, create_time, phase_id, phase_time,
                           price, comments, modify_date, suspended_ind)
       VALUES (9, 9, 1, '1.0', CURRENT, 112, CURRENT, 100.00, 'Comments', CURRENT, 0);

INSERT INTO project (project_id, project_status_id, project_category_id, create_user, create_date, modify_user,
                     modify_date)
       VALUES (9, 1, 1, 132456, CURRENT, 132456, CURRENT);

INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (9, 1, '9', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (9, 2, '9', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (9, 3, '1', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (9, 4, '0', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (9, 5, '5801779', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (9, 6, 'Initially Linked Project #7', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (9, 7, '1.0', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (9, 9, 'On', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (9, 10, 'On', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (9, 11, 'On', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (9, 12, 'Yes', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (9, 13, 'Yes', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (9, 14, 'Open', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (9, 16, '0.0', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (9, 26, 'On', 132456, CURRENT, 132456, CURRENT);

INSERT INTO project_phase (project_phase_id, project_id, phase_type_id, phase_status_id, fixed_start_time,
                           scheduled_start_time, scheduled_end_time, actual_start_time, actual_end_time, duration,
                           create_user, create_date, modify_user, modify_date)
       VALUES (9, 9, 1, 1, CURRENT, CURRENT, CURRENT, CURRENT, CURRENT, 1000, 132456, CURRENT, 132456, CURRENT);

INSERT INTO resource (resource_id, resource_role_id, project_id, project_phase_id, create_user, create_date,
                      modify_user, modify_date) VALUES (9, 13, 9, null, 132456, CURRENT, 132456, CURRENT);
INSERT INTO resource_info (resource_id, resource_info_type_id, value, create_user, create_date, modify_user,
                           modify_date)
       VALUES (9, 1, '132456', 132456, CURRENT, 132456, CURRENT);
INSERT INTO resource_info (resource_id, resource_info_type_id, value, create_user, create_date, modify_user,
                           modify_date)
       VALUES (9, 2, 'heffan', 132456, CURRENT, 132456, CURRENT);

INSERT INTO linked_project_xref (source_project_id, dest_project_id, link_type_id) VALUES (2, 9, 1);


-- Initially linked project #8 (Component Design)
INSERT INTO comp_catalog (component_id, current_version, short_desc, component_name, description, function_desc,
                          create_time, status_id, root_category_id, modify_date, public_ind)
       VALUES (10, 1, 'Initially Linked Project #8 Short Desc', 'Initially Linked Project #8', 'Initially Linked Project Description #8',
              'Initially Linked Project Functional Description #8', CURRENT, 0, 5801779, CURRENT, 1);

INSERT INTO comp_versions (comp_vers_id, component_id, version, version_text, create_time, phase_id, phase_time,
                           price, comments, modify_date, suspended_ind)
       VALUES (10, 10, 1, '1.0', CURRENT, 112, CURRENT, 100.00, 'Comments', CURRENT, 0);

INSERT INTO project (project_id, project_status_id, project_category_id, create_user, create_date, modify_user,
                     modify_date)
       VALUES (10, 1, 1, 132456, CURRENT, 132456, CURRENT);

INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (10, 1, '10', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (10, 2, '10', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (10, 3, '1', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (10, 4, '0', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (10, 5, '5801779', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (10, 6, 'Initially Linked Project #8', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (10, 7, '1.0', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (10, 9, 'On', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (10, 10, 'On', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (10, 11, 'On', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (10, 12, 'Yes', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (10, 13, 'Yes', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (10, 14, 'Open', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (10, 16, '0.0', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (10, 26, 'On', 132456, CURRENT, 132456, CURRENT);

INSERT INTO project_phase (project_phase_id, project_id, phase_type_id, phase_status_id, fixed_start_time,
                           scheduled_start_time, scheduled_end_time, actual_start_time, actual_end_time, duration,
                           create_user, create_date, modify_user, modify_date)
       VALUES (10, 10, 1, 1, CURRENT, CURRENT, CURRENT, CURRENT, CURRENT, 1000, 132456, CURRENT, 132456, CURRENT);

INSERT INTO resource (resource_id, resource_role_id, project_id, project_phase_id, create_user, create_date,
                      modify_user, modify_date) VALUES (10, 13, 10, null, 132456, CURRENT, 132456, CURRENT);
INSERT INTO resource_info (resource_id, resource_info_type_id, value, create_user, create_date, modify_user,
                           modify_date)
       VALUES (10, 1, '132456', 132456, CURRENT, 132456, CURRENT);
INSERT INTO resource_info (resource_id, resource_info_type_id, value, create_user, create_date, modify_user,
                           modify_date)
       VALUES (10, 2, 'heffan', 132456, CURRENT, 132456, CURRENT);

INSERT INTO linked_project_xref (source_project_id, dest_project_id, link_type_id) VALUES (2, 10, 2);

-- Initially linked project #9 (Component Design)
INSERT INTO comp_catalog (component_id, current_version, short_desc, component_name, description, function_desc,
                          create_time, status_id, root_category_id, modify_date, public_ind)
       VALUES (11, 1, 'Initially Linked Project #9 Short Desc', 'Initially Linked Project #9', 'Initially Linked Project Description #9',
              'Initially Linked Project Functional Description #9', CURRENT, 0, 5801779, CURRENT, 1);

INSERT INTO comp_versions (comp_vers_id, component_id, version, version_text, create_time, phase_id, phase_time,
                           price, comments, modify_date, suspended_ind)
       VALUES (11, 11, 1, '1.0', CURRENT, 112, CURRENT, 100.00, 'Comments', CURRENT, 0);

INSERT INTO project (project_id, project_status_id, project_category_id, create_user, create_date, modify_user,
                     modify_date)
       VALUES (11, 1, 1, 132456, CURRENT, 132456, CURRENT);

INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (11, 1, '11', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (11, 2, '11', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (11, 3, '1', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (11, 4, '0', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (11, 5, '5801779', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (11, 6, 'Initially Linked Project #9', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (11, 7, '1.0', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (11, 9, 'On', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (11, 10, 'On', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (11, 11, 'On', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (11, 12, 'Yes', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (11, 13, 'Yes', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (11, 14, 'Open', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (11, 16, '0.0', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (11, 26, 'On', 132456, CURRENT, 132456, CURRENT);

INSERT INTO project_phase (project_phase_id, project_id, phase_type_id, phase_status_id, fixed_start_time,
                           scheduled_start_time, scheduled_end_time, actual_start_time, actual_end_time, duration,
                           create_user, create_date, modify_user, modify_date)
       VALUES (11, 11, 1, 1, CURRENT, CURRENT, CURRENT, CURRENT, CURRENT, 1000, 132456, CURRENT, 132456, CURRENT);

INSERT INTO resource (resource_id, resource_role_id, project_id, project_phase_id, create_user, create_date,
                      modify_user, modify_date) VALUES (11, 13, 11, null, 132456, CURRENT, 132456, CURRENT);
INSERT INTO resource_info (resource_id, resource_info_type_id, value, create_user, create_date, modify_user,
                           modify_date)
       VALUES (11, 1, '132456', 132456, CURRENT, 132456, CURRENT);
INSERT INTO resource_info (resource_id, resource_info_type_id, value, create_user, create_date, modify_user,
                           modify_date)
       VALUES (11, 2, 'heffan', 132456, CURRENT, 132456, CURRENT);

INSERT INTO linked_project_xref (source_project_id, dest_project_id, link_type_id) VALUES (2, 11, 3);


-- Initially linking project #10 (Component Design)
INSERT INTO comp_catalog (component_id, current_version, short_desc, component_name, description, function_desc,
                          create_time, status_id, root_category_id, modify_date, public_ind)
       VALUES (12, 1, 'Initially Linking Project #10 Short Desc', 'Initially Linking Project #10', 'Initially Linking Project Description #10',
              'Initially Linking Project Functional Description #10', CURRENT, 0, 5801779, CURRENT, 1);

INSERT INTO comp_versions (comp_vers_id, component_id, version, version_text, create_time, phase_id, phase_time,
                           price, comments, modify_date, suspended_ind)
       VALUES (12, 12, 1, '1.0', CURRENT, 112, CURRENT, 100.00, 'Comments', CURRENT, 0);

INSERT INTO project (project_id, project_status_id, project_category_id, create_user, create_date, modify_user,
                     modify_date)
       VALUES (12, 1, 1, 132456, CURRENT, 132456, CURRENT);

INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (12, 1, '12', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (12, 2, '12', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (12, 3, '1', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (12, 4, '0', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (12, 5, '5801779', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (12, 6, 'Initially Linking Project #10', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (12, 7, '1.0', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (12, 9, 'On', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (12, 10, 'On', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (12, 11, 'On', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (12, 12, 'Yes', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (12, 13, 'Yes', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (12, 14, 'Open', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (12, 16, '0.0', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (12, 26, 'On', 132456, CURRENT, 132456, CURRENT);

INSERT INTO project_phase (project_phase_id, project_id, phase_type_id, phase_status_id, fixed_start_time,
                           scheduled_start_time, scheduled_end_time, actual_start_time, actual_end_time, duration,
                           create_user, create_date, modify_user, modify_date)
       VALUES (12, 12, 1, 1, CURRENT, CURRENT, CURRENT, CURRENT, CURRENT, 1000, 132456, CURRENT, 132456, CURRENT);

INSERT INTO resource (resource_id, resource_role_id, project_id, project_phase_id, create_user, create_date,
                      modify_user, modify_date) VALUES (12, 13, 12, null, 132456, CURRENT, 132456, CURRENT);
INSERT INTO resource_info (resource_id, resource_info_type_id, value, create_user, create_date, modify_user,
                           modify_date)
       VALUES (12, 1, '132456', 132456, CURRENT, 132456, CURRENT);
INSERT INTO resource_info (resource_id, resource_info_type_id, value, create_user, create_date, modify_user,
                           modify_date)
       VALUES (12, 2, 'heffan', 132456, CURRENT, 132456, CURRENT);

INSERT INTO linked_project_xref (source_project_id, dest_project_id, link_type_id) VALUES (12, 2, 1);


-- Initially linking project #11 (Component Design)
INSERT INTO comp_catalog (component_id, current_version, short_desc, component_name, description, function_desc,
                          create_time, status_id, root_category_id, modify_date, public_ind)
       VALUES (13, 1, 'Initially Linking Project #11 Short Desc', 'Initially Linking Project #11', 'Initially Linking Project Description #11',
              'Initially Linking Project Functional Description #11', CURRENT, 0, 5801779, CURRENT, 1);

INSERT INTO comp_versions (comp_vers_id, component_id, version, version_text, create_time, phase_id, phase_time,
                           price, comments, modify_date, suspended_ind)
       VALUES (13, 13, 1, '1.0', CURRENT, 112, CURRENT, 100.00, 'Comments', CURRENT, 0);

INSERT INTO project (project_id, project_status_id, project_category_id, create_user, create_date, modify_user,
                     modify_date)
       VALUES (13, 1, 1, 132456, CURRENT, 132456, CURRENT);

INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (13, 1, '13', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (13, 2, '13', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (13, 3, '1', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (13, 4, '0', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (13, 5, '5801779', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (13, 6, 'Initially Linking Project #11', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (13, 7, '1.0', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (13, 9, 'On', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (13, 10, 'On', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (13, 11, 'On', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (13, 12, 'Yes', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (13, 13, 'Yes', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (13, 14, 'Open', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (13, 16, '0.0', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (13, 26, 'On', 132456, CURRENT, 132456, CURRENT);

INSERT INTO project_phase (project_phase_id, project_id, phase_type_id, phase_status_id, fixed_start_time,
                           scheduled_start_time, scheduled_end_time, actual_start_time, actual_end_time, duration,
                           create_user, create_date, modify_user, modify_date)
       VALUES (13, 13, 1, 1, CURRENT, CURRENT, CURRENT, CURRENT, CURRENT, 1000, 132456, CURRENT, 132456, CURRENT);

INSERT INTO resource (resource_id, resource_role_id, project_id, project_phase_id, create_user, create_date,
                      modify_user, modify_date) VALUES (13, 13, 13, null, 132456, CURRENT, 132456, CURRENT);
INSERT INTO resource_info (resource_id, resource_info_type_id, value, create_user, create_date, modify_user,
                           modify_date)
       VALUES (13, 1, '132456', 132456, CURRENT, 132456, CURRENT);
INSERT INTO resource_info (resource_id, resource_info_type_id, value, create_user, create_date, modify_user,
                           modify_date)
       VALUES (13, 2, 'heffan', 132456, CURRENT, 132456, CURRENT);

INSERT INTO linked_project_xref (source_project_id, dest_project_id, link_type_id) VALUES (13, 2, 2);


-- Initially linking project #12 (Component Design)
INSERT INTO comp_catalog (component_id, current_version, short_desc, component_name, description, function_desc,
                          create_time, status_id, root_category_id, modify_date, public_ind)
       VALUES (14, 1, 'Initially Linking Project #12 Short Desc', 'Initially Linking Project #12', 'Initially Linking Project Description #12',
              'Initially Linking Project Functional Description #12', CURRENT, 0, 5801779, CURRENT, 1);

INSERT INTO comp_versions (comp_vers_id, component_id, version, version_text, create_time, phase_id, phase_time,
                           price, comments, modify_date, suspended_ind)
       VALUES (14, 14, 1, '1.0', CURRENT, 112, CURRENT, 100.00, 'Comments', CURRENT, 0);

INSERT INTO project (project_id, project_status_id, project_category_id, create_user, create_date, modify_user,
                     modify_date)
       VALUES (14, 1, 1, 132456, CURRENT, 132456, CURRENT);

INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (14, 1, '14', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (14, 2, '14', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (14, 3, '1', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (14, 4, '0', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (14, 5, '5801779', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (14, 6, 'Initially Linking Project #12', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (14, 7, '1.0', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (14, 9, 'On', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (14, 10, 'On', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (14, 11, 'On', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (14, 12, 'Yes', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (14, 13, 'Yes', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (14, 14, 'Open', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (14, 16, '0.0', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (14, 26, 'On', 132456, CURRENT, 132456, CURRENT);

INSERT INTO project_phase (project_phase_id, project_id, phase_type_id, phase_status_id, fixed_start_time,
                           scheduled_start_time, scheduled_end_time, actual_start_time, actual_end_time, duration,
                           create_user, create_date, modify_user, modify_date)
       VALUES (14, 14, 1, 1, CURRENT, CURRENT, CURRENT, CURRENT, CURRENT, 1000, 132456, CURRENT, 132456, CURRENT);

INSERT INTO resource (resource_id, resource_role_id, project_id, project_phase_id, create_user, create_date,
                      modify_user, modify_date) VALUES (14, 13, 14, null, 132456, CURRENT, 132456, CURRENT);
INSERT INTO resource_info (resource_id, resource_info_type_id, value, create_user, create_date, modify_user,
                           modify_date)
       VALUES (14, 1, '132456', 132456, CURRENT, 132456, CURRENT);
INSERT INTO resource_info (resource_id, resource_info_type_id, value, create_user, create_date, modify_user,
                           modify_date)
       VALUES (14, 2, 'heffan', 132456, CURRENT, 132456, CURRENT);

INSERT INTO linked_project_xref (source_project_id, dest_project_id, link_type_id) VALUES (14, 2, 3);

-- Initially linking project #13 (Component Design)
INSERT INTO comp_catalog (component_id, current_version, short_desc, component_name, description, function_desc,
                          create_time, status_id, root_category_id, modify_date, public_ind)
       VALUES (15, 1, 'Initially Linking Project #13 Short Desc', 'Initially Linking Project #13', 'Initially Linking Project Description #13',
              'Initially Linking Project Functional Description #13', CURRENT, 0, 5801779, CURRENT, 1);

INSERT INTO comp_versions (comp_vers_id, component_id, version, version_text, create_time, phase_id, phase_time,
                           price, comments, modify_date, suspended_ind)
       VALUES (15, 15, 1, '1.0', CURRENT, 112, CURRENT, 100.00, 'Comments', CURRENT, 0);

INSERT INTO project (project_id, project_status_id, project_category_id, create_user, create_date, modify_user,
                     modify_date)
       VALUES (15, 1, 1, 132456, CURRENT, 132456, CURRENT);

INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (15, 1, '15', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (15, 2, '15', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (15, 3, '1', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (15, 4, '0', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (15, 5, '5801779', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (15, 6, 'Initially Linking Project #13', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (15, 7, '1.0', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (15, 9, 'On', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (15, 10, 'On', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (15, 11, 'On', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (15, 12, 'Yes', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (15, 13, 'Yes', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (15, 14, 'Open', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (15, 16, '0.0', 132456, CURRENT, 132456, CURRENT);
INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date)
       VALUES (15, 26, 'On', 132456, CURRENT, 132456, CURRENT);

INSERT INTO project_phase (project_phase_id, project_id, phase_type_id, phase_status_id, fixed_start_time,
                           scheduled_start_time, scheduled_end_time, actual_start_time, actual_end_time, duration,
                           create_user, create_date, modify_user, modify_date)
       VALUES (15, 15, 1, 1, CURRENT, CURRENT, CURRENT, CURRENT, CURRENT, 1000, 132456, CURRENT, 132456, CURRENT);

INSERT INTO resource (resource_id, resource_role_id, project_id, project_phase_id, create_user, create_date,
                      modify_user, modify_date) VALUES (15, 13, 15, null, 132456, CURRENT, 132456, CURRENT);
INSERT INTO resource_info (resource_id, resource_info_type_id, value, create_user, create_date, modify_user,
                           modify_date)
       VALUES (15, 1, '132456', 132456, CURRENT, 132456, CURRENT);
INSERT INTO resource_info (resource_id, resource_info_type_id, value, create_user, create_date, modify_user,
                           modify_date)
       VALUES (15, 2, 'heffan', 132456, CURRENT, 132456, CURRENT);

INSERT INTO linked_project_xref (source_project_id, dest_project_id, link_type_id) VALUES (15, 2, 4);

