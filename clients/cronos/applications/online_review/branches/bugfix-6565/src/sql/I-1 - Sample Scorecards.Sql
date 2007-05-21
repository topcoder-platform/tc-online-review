INSERT INTO scorecard (scorecard_id, scorecard_status_id, scorecard_type_id, project_category_id, name, version, min_score, max_score, create_user, create_date, modify_user, modify_date)
  VALUES (101, 1, 1, 1, 'Sample Screening Component Design Scorecard', '1.0', 75.00, 100.0000000000, 'System', '2006-08-28 18:41:06.682', 'System', '2006-08-28 18:41:06.682');

INSERT INTO scorecard_group (scorecard_group_id, scorecard_id, name, weight, sort, create_user, create_date, modify_user, modify_date)
  VALUES (101, 101, 'Group Number 1', 95.00000000000, 0, 'System', '2006-08-28 18:41:06.979', 'System', '2006-08-28 18:41:06.979');
INSERT INTO scorecard_group (scorecard_group_id, scorecard_id, name, weight, sort, create_user, create_date, modify_user, modify_date)
  VALUES (102, 101, 'Group Number 2', 5.000000000000, 1, 'System', '2006-08-28 18:41:06.979', 'System', '2006-08-28 18:41:06.979');

INSERT INTO scorecard_section (scorecard_section_id, scorecard_group_id, name, weight, sort, create_user, create_date, modify_user, modify_date)
  VALUES (101, 101, 'Section 1 from Group 1.', 20.00000000000, 0, 'System', '2006-08-28 18:41:07.276', 'System', '2006-08-28 18:41:07.276');
INSERT INTO scorecard_section (scorecard_section_id, scorecard_group_id, name, weight, sort, create_user, create_date, modify_user, modify_date)
  VALUES (102, 101, 'Section 2 from Group 1.', 80.00000000000, 1, 'System', '2006-08-28 18:41:07.276', 'System', '2006-08-28 18:41:07.276');
INSERT INTO scorecard_section (scorecard_section_id, scorecard_group_id, name, weight, sort, create_user, create_date, modify_user, modify_date)
  VALUES (103, 102, 'Section 1 from Group 2.', 100.0000000000, 0, 'System', '2006-08-28 18:41:07.557', 'System', '2006-08-28 18:41:07.557');

INSERT INTO scorecard_question (scorecard_question_id, scorecard_question_type_id, scorecard_section_id, description, guideline, weight, sort, upload_document, upload_document_required, create_user, create_date, modify_user, modify_date)
  VALUES (101, 1, 101, 'Question 1 from Section 1 from Group 1.', 'Gudeline for Question 1. This is Scale (1-4) type of question.', 50.00000000000, 0, 0, 0, 'System', '2006-08-28 18:41:07.526', 'System', '2006-08-28 18:41:07.526');
INSERT INTO scorecard_question (scorecard_question_id, scorecard_question_type_id, scorecard_section_id, description, guideline, weight, sort, upload_document, upload_document_required, create_user, create_date, modify_user, modify_date)
  VALUES (102, 1, 101, 'Question 2 from Section 1 from Group 1.', 'Guideline for Question 2. This is Scale (1-4) type of question.', 15.00000000000, 1, 0, 0, 'System', '2006-08-28 18:41:07.526', 'System', '2006-08-28 18:41:07.526');
INSERT INTO scorecard_question (scorecard_question_id, scorecard_question_type_id, scorecard_section_id, description, guideline, weight, sort, upload_document, upload_document_required, create_user, create_date, modify_user, modify_date)
  VALUES (103, 2, 101, 'Question 3 from Section 1 from Group 1.', 'Guideline for Question 3. This is Scale (1-10) type of question.', 35.00000000000, 2, 0, 0, 'System', '2006-08-28 18:41:07.526', 'System', '2006-08-28 18:41:07.526');
INSERT INTO scorecard_question (scorecard_question_id, scorecard_question_type_id, scorecard_section_id, description, guideline, weight, sort, upload_document, upload_document_required, create_user, create_date, modify_user, modify_date)
  VALUES (104, 3, 102, 'Question 1 from Section 2 from Group 1.', 'Guideline for Question 2. This is Testcase type of question.', 40.00000000000, 0, 0, 0, 'System', '2006-08-28 18:41:07.557', 'System', '2006-08-28 18:41:07.557');
INSERT INTO scorecard_question (scorecard_question_id, scorecard_question_type_id, scorecard_section_id, description, guideline, weight, sort, upload_document, upload_document_required, create_user, create_date, modify_user, modify_date)
  VALUES (105, 4, 102, 'Question 2 from Section 2 from Group 1.', 'Guideline for Question 2. This is Yes/No type of question.', 60.00000000000, 1, 0, 0, 'System', '2006-08-28 18:41:07.557', 'System', '2006-08-28 18:41:07.557');
INSERT INTO scorecard_question (scorecard_question_id, scorecard_question_type_id, scorecard_section_id, description, guideline, weight, sort, upload_document, upload_document_required, create_user, create_date, modify_user, modify_date)
  VALUES (106, 3, 103, 'Question 1 from Section 1 from Group 2.', 'Guideline for Qestion 1. This is Testcase type of question.', 100.0000000000, 0, 0, 0, 'System', '2006-08-28 18:41:07.557', 'System', '2006-08-28 18:41:07.557');



INSERT INTO scorecard (scorecard_id, scorecard_status_id, scorecard_type_id, project_category_id, name, version, min_score, max_score, create_user, create_date, modify_user, modify_date)
  VALUES (201, 1, 2, 1, 'Sample Review Component Design Scorecard', '1.1', 75.00, 100.0000000000, 'System', '2006-08-28 18:41:06.682', 'System', '2006-08-28 18:41:06.682');

INSERT INTO scorecard_group (scorecard_group_id, scorecard_id, name, weight, sort, create_user, create_date, modify_user, modify_date)
  VALUES (201, 201, 'Group Number 1', 95.00000000000, 0, 'System', '2006-08-28 18:41:06.979', 'System', '2006-08-28 18:41:06.979');
INSERT INTO scorecard_group (scorecard_group_id, scorecard_id, name, weight, sort, create_user, create_date, modify_user, modify_date)
  VALUES (202, 201, 'Group Number 2', 5.000000000000, 1, 'System', '2006-08-28 18:41:06.979', 'System', '2006-08-28 18:41:06.979');

INSERT INTO scorecard_section (scorecard_section_id, scorecard_group_id, name, weight, sort, create_user, create_date, modify_user, modify_date)
  VALUES (201, 201, 'Section 1 from Group 1.', 20.00000000000, 0, 'System', '2006-08-28 18:41:07.276', 'System', '2006-08-28 18:41:07.276');
INSERT INTO scorecard_section (scorecard_section_id, scorecard_group_id, name, weight, sort, create_user, create_date, modify_user, modify_date)
  VALUES (202, 201, 'Section 2 from Group 1.', 80.00000000000, 1, 'System', '2006-08-28 18:41:07.276', 'System', '2006-08-28 18:41:07.276');
INSERT INTO scorecard_section (scorecard_section_id, scorecard_group_id, name, weight, sort, create_user, create_date, modify_user, modify_date)
  VALUES (203, 202, 'Section 1 from Group 2.', 100.0000000000, 0, 'System', '2006-08-28 18:41:07.557', 'System', '2006-08-28 18:41:07.557');

INSERT INTO scorecard_question (scorecard_question_id, scorecard_question_type_id, scorecard_section_id, description, guideline, weight, sort, upload_document, upload_document_required, create_user, create_date, modify_user, modify_date)
  VALUES (201, 1, 201, 'Question 1 from Section 1 from Group 1.', 'Gudeline for Question 1. This is Scale (1-4) type of question.', 50.00000000000, 0, 0, 0, 'System', '2006-08-28 18:41:07.526', 'System', '2006-08-28 18:41:07.526');
INSERT INTO scorecard_question (scorecard_question_id, scorecard_question_type_id, scorecard_section_id, description, guideline, weight, sort, upload_document, upload_document_required, create_user, create_date, modify_user, modify_date)
  VALUES (202, 1, 201, 'Question 2 from Section 1 from Group 1.', 'Guideline for Question 2. This is Scale (1-4) type of question.', 15.00000000000, 1, 1, 0, 'System', '2006-08-28 18:41:07.526', 'System', '2006-08-28 18:41:07.526');
INSERT INTO scorecard_question (scorecard_question_id, scorecard_question_type_id, scorecard_section_id, description, guideline, weight, sort, upload_document, upload_document_required, create_user, create_date, modify_user, modify_date)
  VALUES (203, 2, 201, 'Question 3 from Section 1 from Group 1.', 'Guideline for Question 3. This is Scale (1-10) type of question.', 35.00000000000, 2, 0, 0, 'System', '2006-08-28 18:41:07.526', 'System', '2006-08-28 18:41:07.526');
INSERT INTO scorecard_question (scorecard_question_id, scorecard_question_type_id, scorecard_section_id, description, guideline, weight, sort, upload_document, upload_document_required, create_user, create_date, modify_user, modify_date)
  VALUES (204, 3, 202, 'Question 1 from Section 2 from Group 1.', 'Guideline for Question 2. This is Testcase type of question.', 40.00000000000, 0, 0, 0, 'System', '2006-08-28 18:41:07.557', 'System', '2006-08-28 18:41:07.557');
INSERT INTO scorecard_question (scorecard_question_id, scorecard_question_type_id, scorecard_section_id, description, guideline, weight, sort, upload_document, upload_document_required, create_user, create_date, modify_user, modify_date)
  VALUES (205, 4, 202, 'Question 2 from Section 2 from Group 1.', 'Guideline for Question 2. This is Yes/No type of question.', 20.00000000000, 1, 1, 1, 'System', '2006-08-28 18:41:07.557', 'System', '2006-08-28 18:41:07.557');
INSERT INTO scorecard_question (scorecard_question_id, scorecard_question_type_id, scorecard_section_id, description, guideline, weight, sort, upload_document, upload_document_required, create_user, create_date, modify_user, modify_date)
  VALUES (206, 3, 203, 'Question 1 from Section 1 from Group 2.', 'Guideline for Qestion 1. This is Testcase type of question.', 100.0000000000, 0, 0, 0, 'System', '2006-08-28 18:41:07.557', 'System', '2006-08-28 18:41:07.557');



INSERT INTO scorecard (scorecard_id, scorecard_status_id, scorecard_type_id, project_category_id, name, version, min_score, max_score, create_user, create_date, modify_user, modify_date)
  VALUES (301, 1, 3, 1, 'Sample Client Review Component Design Scorecard', '1.0.2', 75.00, 100.0000000000, 'System', '2006-08-28 18:41:06.682', 'System', '2006-08-28 18:41:06.682');

INSERT INTO scorecard_group (scorecard_group_id, scorecard_id, name, weight, sort, create_user, create_date, modify_user, modify_date)
  VALUES (301, 301, 'Group Number 1', 95.00000000000, 0, 'System', '2006-08-28 18:41:06.979', 'System', '2006-08-28 18:41:06.979');
INSERT INTO scorecard_group (scorecard_group_id, scorecard_id, name, weight, sort, create_user, create_date, modify_user, modify_date)
  VALUES (302, 301, 'Group Number 2', 5.000000000000, 1, 'System', '2006-08-28 18:41:06.979', 'System', '2006-08-28 18:41:06.979');

INSERT INTO scorecard_section (scorecard_section_id, scorecard_group_id, name, weight, sort, create_user, create_date, modify_user, modify_date)
  VALUES (301, 301, 'Section 1 from Group 1.', 20.00000000000, 0, 'System', '2006-08-28 18:41:07.276', 'System', '2006-08-28 18:41:07.276');
INSERT INTO scorecard_section (scorecard_section_id, scorecard_group_id, name, weight, sort, create_user, create_date, modify_user, modify_date)
  VALUES (302, 301, 'Section 2 from Group 1.', 80.00000000000, 1, 'System', '2006-08-28 18:41:07.276', 'System', '2006-08-28 18:41:07.276');
INSERT INTO scorecard_section (scorecard_section_id, scorecard_group_id, name, weight, sort, create_user, create_date, modify_user, modify_date)
  VALUES (303, 302, 'Section 1 from Group 2.', 100.0000000000, 0, 'System', '2006-08-28 18:41:07.557', 'System', '2006-08-28 18:41:07.557');

INSERT INTO scorecard_question (scorecard_question_id, scorecard_question_type_id, scorecard_section_id, description, guideline, weight, sort, upload_document, upload_document_required, create_user, create_date, modify_user, modify_date)
  VALUES (301, 1, 301, 'Question 1 from Section 1 from Group 1.', 'Gudeline for Question 1. This is Scale (1-4) type of question.', 50.00000000000, 0, 0, 0, 'System', '2006-08-28 18:41:07.526', 'System', '2006-08-28 18:41:07.526');
INSERT INTO scorecard_question (scorecard_question_id, scorecard_question_type_id, scorecard_section_id, description, guideline, weight, sort, upload_document, upload_document_required, create_user, create_date, modify_user, modify_date)
  VALUES (302, 1, 301, 'Question 2 from Section 1 from Group 1.', 'Guideline for Question 2. This is Scale (1-4) type of question.', 15.00000000000, 1, 0, 0, 'System', '2006-08-28 18:41:07.526', 'System', '2006-08-28 18:41:07.526');
INSERT INTO scorecard_question (scorecard_question_id, scorecard_question_type_id, scorecard_section_id, description, guideline, weight, sort, upload_document, upload_document_required, create_user, create_date, modify_user, modify_date)
  VALUES (303, 2, 301, 'Question 3 from Section 1 from Group 1.', 'Guideline for Question 3. This is Scale (1-10) type of question.', 35.00000000000, 2, 0, 0, 'System', '2006-08-28 18:41:07.526', 'System', '2006-08-28 18:41:07.526');
INSERT INTO scorecard_question (scorecard_question_id, scorecard_question_type_id, scorecard_section_id, description, guideline, weight, sort, upload_document, upload_document_required, create_user, create_date, modify_user, modify_date)
  VALUES (304, 3, 302, 'Question 1 from Section 2 from Group 1.', 'Guideline for Question 2. This is Testcase type of question.', 40.00000000000, 0, 0, 0, 'System', '2006-08-28 18:41:07.557', 'System', '2006-08-28 18:41:07.557');
INSERT INTO scorecard_question (scorecard_question_id, scorecard_question_type_id, scorecard_section_id, description, guideline, weight, sort, upload_document, upload_document_required, create_user, create_date, modify_user, modify_date)
  VALUES (305, 4, 302, 'Question 2 from Section 2 from Group 1.', 'Guideline for Question 2. This is Yes/No type of question.', 60.00000000000, 1, 0, 0, 'System', '2006-08-28 18:41:07.557', 'System', '2006-08-28 18:41:07.557');
INSERT INTO scorecard_question (scorecard_question_id, scorecard_question_type_id, scorecard_section_id, description, guideline, weight, sort, upload_document, upload_document_required, create_user, create_date, modify_user, modify_date)
  VALUES (306, 3, 303, 'Question 1 from Section 1 from Group 2.', 'Guideline for Qestion 1. This is Testcase type of question.', 100.0000000000, 0, 0, 0, 'System', '2006-08-28 18:41:07.557', 'System', '2006-08-28 18:41:07.557');


-- Very simple scorecards to speed up screening/reviewing processes

INSERT INTO scorecard (scorecard_id, scorecard_status_id, scorecard_type_id, project_category_id, name, version, min_score, max_score, create_user, create_date, modify_user, modify_date)
  VALUES (1001, 1, 1, 1, 'Concise Screening Scorecard', '80.0', 80.0, 100.0, 'System', CURRENT, 'System', CURRENT);
INSERT INTO scorecard_group (scorecard_group_id, scorecard_id, name, weight, sort, create_user, create_date, modify_user, modify_date)
  VALUES (1001, 1001, 'Single Group', 100.0, 0, 'System', CURRENT, 'System', CURRENT);
INSERT INTO scorecard_section (scorecard_section_id, scorecard_group_id, name, weight, sort, create_user, create_date, modify_user, modify_date)
  VALUES (1001, 1001, 'Single Section of Group', 100.0, 0, 'System', CURRENT, 'System', CURRENT);
INSERT INTO scorecard_question (scorecard_question_id, scorecard_question_type_id, scorecard_section_id, description, guideline, weight, sort, upload_document, upload_document_required, create_user, create_date, modify_user, modify_date)
  VALUES (1001, 2, 1001, 'Rate me!', 'This is Scale (1-10) type of question.', 100.0, 0, 0, 0, 'System', CURRENT, 'System', CURRENT);

INSERT INTO scorecard (scorecard_id, scorecard_status_id, scorecard_type_id, project_category_id, name, version, min_score, max_score, create_user, create_date, modify_user, modify_date)
  VALUES (2001, 1, 2, 1, 'Concise Review Scorecard', '70.0', 70.0, 100.0, 'System', CURRENT, 'System', CURRENT);
INSERT INTO scorecard_group (scorecard_group_id, scorecard_id, name, weight, sort, create_user, create_date, modify_user, modify_date)
  VALUES (2001, 2001, 'Single Group', 100.0, 0, 'System', CURRENT, 'System', CURRENT);
INSERT INTO scorecard_section (scorecard_section_id, scorecard_group_id, name, weight, sort, create_user, create_date, modify_user, modify_date)
  VALUES (2001, 2001, 'Single Section of Group', 100.0, 0, 'System', CURRENT, 'System', CURRENT);
INSERT INTO scorecard_question (scorecard_question_id, scorecard_question_type_id, scorecard_section_id, description, guideline, weight, sort, upload_document, upload_document_required, create_user, create_date, modify_user, modify_date)
  VALUES (2001, 2, 2001, 'Rate me!', 'This is Scale (1-10) type of question.', 100.0, 0, 0, 0, 'System', CURRENT, 'System', CURRENT);

INSERT INTO scorecard (scorecard_id, scorecard_status_id, scorecard_type_id, project_category_id, name, version, min_score, max_score, create_user, create_date, modify_user, modify_date)
  VALUES (3001, 1, 3, 1, 'Concise Client Review (Approval) Scorecard', '90.0', 90.0, 100.0, 'System', CURRENT, 'System', CURRENT);
INSERT INTO scorecard_group (scorecard_group_id, scorecard_id, name, weight, sort, create_user, create_date, modify_user, modify_date)
  VALUES (3001, 3001, 'Single Group', 100.0, 0, 'System', CURRENT, 'System', CURRENT);
INSERT INTO scorecard_section (scorecard_section_id, scorecard_group_id, name, weight, sort, create_user, create_date, modify_user, modify_date)
  VALUES (3001, 3001, 'Single Section of Group', 100.0, 0, 'System', CURRENT, 'System', CURRENT);
INSERT INTO scorecard_question (scorecard_question_id, scorecard_question_type_id, scorecard_section_id, description, guideline, weight, sort, upload_document, upload_document_required, create_user, create_date, modify_user, modify_date)
  VALUES (3001, 2, 3001, 'Rate me!', 'This is Scale (1-10) type of question.', 100.0, 0, 0, 0, 'System', CURRENT, 'System', CURRENT);
