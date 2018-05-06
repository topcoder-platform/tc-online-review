INSERT INTO issue_types (id, name) VALUES (1001, 'issue1');
INSERT INTO issue_types (id, name) VALUES (1002, 'issue2');

INSERT INTO issues (issue_type_id, user_id, priority, start_date) VALUES (1001, 101, 1, CURRENT_TIMESTAMP);
INSERT INTO issues (issue_type_id, user_id, priority, start_date) VALUES (1002, 101, 1, CURRENT_TIMESTAMP);
INSERT INTO issues (issue_type_id, user_id, priority, start_date) VALUES (1002, 102, 0, CURRENT_TIMESTAMP);
INSERT INTO issues (issue_type_id, user_id, priority, start_date) VALUES (1002, 102, null, null);