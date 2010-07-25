INSERT INTO screening_task (screening_task_id, upload_id, screening_status_id, create_user, create_date, modify_user, modify_date)
  VALUES (1000, 2000, 5, 'System', CURRENT, 'System', CURRENT);

INSERT INTO screening_result (screening_result_id, screening_task_id, screening_response_id, dynamic_response_text, create_user, create_date, modify_user, modify_date)
  VALUES (1001, 1000, 1, 'Dummy text, empty not supported', 'System', CURRENT, 'System', CURRENT);
INSERT INTO screening_result (screening_result_id, screening_task_id, screening_response_id, dynamic_response_text, create_user, create_date, modify_user, modify_date)
  VALUES (1002, 1000, 14, 'Dynamic Text 1', 'System', CURRENT, 'System', CURRENT);
INSERT INTO screening_result (screening_result_id, screening_task_id, screening_response_id, dynamic_response_text, create_user, create_date, modify_user, modify_date)
  VALUES (1003, 1000, 14, 'Dynamic Text 2', 'System', CURRENT, 'System', CURRENT);
