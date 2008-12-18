INSERT INTO upload (upload_id, project_id, resource_id, upload_type_id, upload_status_id, parameter, create_user, create_date, modify_user, modify_date)
  VALUES (2000, 1002, 200, 1, 1, 'file on disk for submitter 1', 'System', CURRENT, 'System', CURRENT);
INSERT INTO upload (upload_id, project_id, resource_id, upload_type_id, upload_status_id, parameter, create_user, create_date, modify_user, modify_date)
  VALUES (2001, 1002, 201, 1, 1, 'file on disk for submitter 2', 'System', CURRENT, 'System', CURRENT);
INSERT INTO upload (upload_id, project_id, resource_id, upload_type_id, upload_status_id, parameter, create_user, create_date, modify_user, modify_date)
  VALUES (2002, 1002, 202, 1, 1, 'file on disk for submitter 3', 'System', CURRENT, 'System', CURRENT);

INSERT INTO submission (submission_id, upload_id, submission_status_id, create_user, create_date, modify_user, modify_date)
  VALUES (3000, 2000, 1, 'System', CURRENT, 'System', CURRENT);
INSERT INTO submission (submission_id, upload_id, submission_status_id, create_user, create_date, modify_user, modify_date)
  VALUES (3001, 2001, 1, 'System', CURRENT, 'System', CURRENT);
INSERT INTO submission (submission_id, upload_id, submission_status_id, create_user, create_date, modify_user, modify_date)
  VALUES (3002, 2002, 1, 'System', CURRENT, 'System', CURRENT);

INSERT INTO resource_submission (resource_id, submission_id, create_user, create_date, modify_user, modify_date)
  VALUES (203, 3000, 'System', CURRENT, 'System', CURRENT);
INSERT INTO resource_submission (resource_id, submission_id, create_user, create_date, modify_user, modify_date)
  VALUES (204, 3001, 'System', CURRENT, 'System', CURRENT);
INSERT INTO resource_submission (resource_id, submission_id, create_user, create_date, modify_user, modify_date)
  VALUES (205, 3002, 'System', CURRENT, 'System', CURRENT);
