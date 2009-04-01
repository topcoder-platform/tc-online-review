INSERT INTO project_phase (project_phase_id, project_id, phase_type_id, phase_status_id, fixed_start_time, scheduled_start_time, scheduled_end_time, actual_start_time, duration, create_user, create_date, modify_user, modify_date)
  VALUES (501, 1002, 1, 2, '2006-08-15 08:00:00.000', '2006-08-15 09:00:00.000', '2006-08-18 09:00:00.000', '2006-08-15 09:00:00.000', 259200000, 'System', CURRENT, 'System', CURRENT);
INSERT INTO project_phase (project_phase_id, project_id, phase_type_id, phase_status_id, fixed_start_time, scheduled_start_time, scheduled_end_time, actual_start_time, duration, create_user, create_date, modify_user, modify_date)
  VALUES (502, 1002, 2, 2, '2006-08-15 08:00:00.000', '2006-08-15 09:00:00.000', '2006-08-20 09:00:00.000', '2006-08-15 09:00:00.000', 432000000, 'System', CURRENT, 'System', CURRENT);
INSERT INTO project_phase (project_phase_id, project_id, phase_type_id, phase_status_id, scheduled_start_time, scheduled_end_time, duration, create_user, create_date, modify_user, modify_date)
  VALUES (503, 1002, 3, 1, '2006-08-20 09:00:00.000', '2006-08-21 09:00:00.000', 86400000, 'System', CURRENT, 'System', CURRENT);
INSERT INTO project_phase (project_phase_id, project_id, phase_type_id, phase_status_id, scheduled_start_time, scheduled_end_time, duration, create_user, create_date, modify_user, modify_date)
  VALUES (505, 1002, 4, 1, '2006-08-21 09:00:00.000', '2006-08-22 09:00:00.000', 86400000, 'System', CURRENT, 'System', CURRENT);
INSERT INTO project_phase (project_phase_id, project_id, phase_type_id, phase_status_id, scheduled_start_time, scheduled_end_time, duration, create_user, create_date, modify_user, modify_date)
  VALUES (506, 1002, 5, 1, '2006-08-22 09:00:00.000', '2006-08-22 21:00:00.000', 43200000, 'System', CURRENT, 'System', CURRENT);
INSERT INTO project_phase (project_phase_id, project_id, phase_type_id, phase_status_id, scheduled_start_time, scheduled_end_time, duration, create_user, create_date, modify_user, modify_date)
  VALUES (507, 1002, 6, 1, '2006-08-22 09:00:00.000', '2006-08-22 21:00:01.000', 43200000, 'System', CURRENT, 'System', CURRENT);
INSERT INTO project_phase (project_phase_id, project_id, phase_type_id, phase_status_id, scheduled_start_time, scheduled_end_time, duration, create_user, create_date, modify_user, modify_date)
  VALUES (508, 1002, 7, 1, '2006-08-22 21:00:01.000', '2006-08-23 09:00:00.000', 43200000, 'System', CURRENT, 'System', CURRENT);
INSERT INTO project_phase (project_phase_id, project_id, phase_type_id, phase_status_id, scheduled_start_time, scheduled_end_time, duration, create_user, create_date, modify_user, modify_date)
  VALUES (509, 1002, 8, 1, '2006-08-23 09:00:00.000', '2006-08-24 09:00:00.000', 86400000, 'System', CURRENT, 'System', CURRENT);
INSERT INTO project_phase (project_phase_id, project_id, phase_type_id, phase_status_id, scheduled_start_time, scheduled_end_time, duration, create_user, create_date, modify_user, modify_date)
  VALUES (510, 1002, 9, 1, '2006-08-24 09:00:00.000', '2006-08-25 09:00:00.000', 86400000, 'System', CURRENT, 'System', CURRENT);
INSERT INTO project_phase (project_phase_id, project_id, phase_type_id, phase_status_id, scheduled_start_time, scheduled_end_time, duration, create_user, create_date, modify_user, modify_date)
  VALUES (511, 1002, 10, 1, '2006-08-25 09:00:00.000', '2006-08-26 09:00:00.000', 86400000, 'System', CURRENT, 'System', CURRENT);
INSERT INTO project_phase (project_phase_id, project_id, phase_type_id, phase_status_id, scheduled_start_time, scheduled_end_time, duration, create_user, create_date, modify_user, modify_date)
  VALUES (512, 1002, 11, 1, '2006-08-26 09:00:00.000', '2006-08-27 09:00:00.000', 86400000, 'System', CURRENT, 'System', CURRENT);

INSERT INTO phase_dependency (dependency_phase_id, dependent_phase_id, dependency_start, dependent_start, lag_time, create_user, create_date, modify_user, modify_date)
  VALUES (501, 502, 1, 1, 0, 'System', CURRENT, 'System', CURRENT);
INSERT INTO phase_dependency (dependency_phase_id, dependent_phase_id, dependency_start, dependent_start, lag_time, create_user, create_date, modify_user, modify_date)
  VALUES (502, 503, 0, 1, 0, 'System', CURRENT, 'System', CURRENT);
INSERT INTO phase_dependency (dependency_phase_id, dependent_phase_id, dependency_start, dependent_start, lag_time, create_user, create_date, modify_user, modify_date)
  VALUES (503, 505, 0, 1, 0, 'System', CURRENT, 'System', CURRENT);
INSERT INTO phase_dependency (dependency_phase_id, dependent_phase_id, dependency_start, dependent_start, lag_time, create_user, create_date, modify_user, modify_date)
  VALUES (505, 506, 0, 1, 0, 'System', CURRENT, 'System', CURRENT);
INSERT INTO phase_dependency (dependency_phase_id, dependent_phase_id, dependency_start, dependent_start, lag_time, create_user, create_date, modify_user, modify_date)
  VALUES (506, 507, 1, 1, 0, 'System', CURRENT, 'System', CURRENT);
INSERT INTO phase_dependency (dependency_phase_id, dependent_phase_id, dependency_start, dependent_start, lag_time, create_user, create_date, modify_user, modify_date)
  VALUES (507, 508, 0, 1, 0, 'System', CURRENT, 'System', CURRENT);
INSERT INTO phase_dependency (dependency_phase_id, dependent_phase_id, dependency_start, dependent_start, lag_time, create_user, create_date, modify_user, modify_date)
  VALUES (508, 509, 0, 1, 0, 'System', CURRENT, 'System', CURRENT);
INSERT INTO phase_dependency (dependency_phase_id, dependent_phase_id, dependency_start, dependent_start, lag_time, create_user, create_date, modify_user, modify_date)
  VALUES (509, 510, 0, 1, 0, 'System', CURRENT, 'System', CURRENT);
INSERT INTO phase_dependency (dependency_phase_id, dependent_phase_id, dependency_start, dependent_start, lag_time, create_user, create_date, modify_user, modify_date)
  VALUES (510, 511, 0, 1, 0, 'System', CURRENT, 'System', CURRENT);
INSERT INTO phase_dependency (dependency_phase_id, dependent_phase_id, dependency_start, dependent_start, lag_time, create_user, create_date, modify_user, modify_date)
  VALUES (511, 512, 0, 1, 0, 'System', CURRENT, 'System', CURRENT);

INSERT INTO phase_criteria (project_phase_id, phase_criteria_type_id, parameter, create_user, create_date, modify_user, modify_date)
  VALUES (503, 1, '101', 'System', CURRENT, 'System', CURRENT);
INSERT INTO phase_criteria (project_phase_id, phase_criteria_type_id, parameter, create_user, create_date, modify_user, modify_date)
  VALUES (505, 1, '201', 'System', CURRENT, 'System', CURRENT);
INSERT INTO phase_criteria (project_phase_id, phase_criteria_type_id, parameter, create_user, create_date, modify_user, modify_date)
  VALUES (512, 1, '301', 'System', CURRENT, 'System', CURRENT);
