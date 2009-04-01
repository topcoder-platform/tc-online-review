-- Create one user
-- remember to change the user_is, and email_id
-- the login_id or user_id must be changed to proper value for OR 
-- the password: '3aP9HbGId8UQhwHEid8pew==' means 'password'

INSERT INTO security_user (login_id, user_id, password)
  VALUES (100133, 'test3', '3aP9HbGId8UQhwHEid8pew==');

INSERT INTO user (user_id, first_name, last_name, handle, status, password, timezone_id)
  VALUES (100133, 'John', 'Test3', 'test3', 'U', '3aP9HbGId8UQhwHEid8pew==', 143);

INSERT INTO email (user_id, email_id, email_type_id, address, create_date, modify_date, primary_ind, status_id)
  VALUES (100133, 100133, 1, 'test@gmail.com', CURRENT, CURRENT, 1, 1);

INSERT INTO user_rating (user_id, rating, phase_id, vol, rating_no_vol, num_ratings, mod_date_time, create_date_time, last_rated_project_id)
  VALUES (100133, 2275, 112, 512, 0, 1, CURRENT, CURRENT, 0);
INSERT INTO user_rating (user_id, rating, phase_id, vol, rating_no_vol, num_ratings, mod_date_time, create_date_time, last_rated_project_id)
  VALUES (100133, 1709, 113, 542, 0, 1, CURRENT, CURRENT, 0);

INSERT INTO user_reliability (user_id, rating, modify_date, create_date, phase_id)
  VALUES (100133, 1.00, CURRENT, CURRENT, 112);
INSERT INTO user_reliability (user_id, rating, modify_date, create_date, phase_id)
  VALUES (100133, 1.00, CURRENT, CURRENT, 113);

-- set user role
INSERT INTO resource (resource_id, resource_role_id, create_user, create_date, modify_user, modify_date) 
	VALUES(214, 3, 'System' ,current, 'System', current);
INSERT INTO resource_info (resource_id, resource_info_type_id, value, create_user, create_date, modify_user, modify_date) 
	VALUES(214 , 3, 100133,'System' ,Current, 'System' ,Current);