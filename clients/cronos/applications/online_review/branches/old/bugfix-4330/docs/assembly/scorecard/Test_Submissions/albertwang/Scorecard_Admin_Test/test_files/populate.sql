----------------------------------
-- populate scorecards
----------------------------------

---------------------
-- Scorecard #1
--   Group#1 (30)
--     Section#1.1 (40)
--         Question#1.1.1 (40)
--         Question#1.1.2 (60)
--     Section#1.2 (60)
--   Group#2 (40)
--     Section#2.1 (100)
--         Question#2.1.1 (100)
--   Group#3 (30)
--     Section#3.1 (20)
--         Question#3.1.1 (100)
--     Section#3.2 (60)
--         Question#3.2.1 (100)
--     Section#3.3 (20)
--         Question#3.3.1 (100)
---------------------
INSERT INTO scorecard
(scorecard_id, scorecard_status_id, scorecard_type_id, project_category_id, name, version, min_score, max_score, create_user, create_date, modify_user, modify_date)
VALUES
-- Active, Screening, Design
(888, 1, 1, 1, 'Design Screening Scorecard', '1.1', 75, 100, 'System', CURRENT, 'System', CURRENT);

-- 3 groups
-- Group#1
INSERT INTO scorecard_group
(scorecard_group_id, scorecard_id, name, weight, sort, create_user, create_date, modify_user, modify_date)
VALUES
(8881, 888, 'Group#1', 30, 1, 'System', CURRENT, 'System', CURRENT);
-- 2 sections
INSERT INTO scorecard_section
(scorecard_section_id, scorecard_group_id, name, weight, sort, create_user, create_date, modify_user, modify_date)
VALUES
(88811, 8881, 'Section 1.1', 40, 1, 'System', CURRENT, 'System', CURRENT);
-- 2 questions
INSERT INTO scorecard_question
(scorecard_question_id, scorecard_question_type_id, scorecard_section_id, description, guideline, weight, sort, upload_document, upload_document_required, create_user, create_date, modify_user, modify_date)
VALUES
(888111, 1, 88811, 'Question 1.1.1', 'Guide line', 40, 1, 1, 1, 'System', CURRENT, 'System', CURRENT);
INSERT INTO scorecard_question
(scorecard_question_id, scorecard_question_type_id, scorecard_section_id, description, guideline, weight, sort, upload_document, upload_document_required, create_user, create_date, modify_user, modify_date)
VALUES
(888112, 1, 88811, 'Question 1.1.2', 'Guide line', 60, 1, 1, 1, 'System', CURRENT, 'System', CURRENT);

INSERT INTO scorecard_section
(scorecard_section_id, scorecard_group_id, name, weight, sort, create_user, create_date, modify_user, modify_date)
VALUES
(88812, 8881, 'Section 1.2', 60, 1, 'System', CURRENT, 'System', CURRENT);
-- 1 question
INSERT INTO scorecard_question
(scorecard_question_id, scorecard_question_type_id, scorecard_section_id, description, guideline, weight, sort, upload_document, upload_document_required, create_user, create_date, modify_user, modify_date)
VALUES
(888121, 1, 88812, 'Question 1.2.1', 'Guide line', 100, 1, 1, 1, 'System', CURRENT, 'System', CURRENT);

-- Group#2
INSERT INTO scorecard_group
(scorecard_group_id, scorecard_id, name, weight, sort, create_user, create_date, modify_user, modify_date)
VALUES
(8882, 888, 'Group#2', 40, 1, 'System', CURRENT, 'System', CURRENT);
-- 1 section
INSERT INTO scorecard_section
(scorecard_section_id, scorecard_group_id, name, weight, sort, create_user, create_date, modify_user, modify_date)
VALUES
(88821, 8882, 'Section 2.1', 100, 1, 'System', CURRENT, 'System', CURRENT);
-- 1 question
INSERT INTO scorecard_question
(scorecard_question_id, scorecard_question_type_id, scorecard_section_id, description, guideline, weight, sort, upload_document, upload_document_required, create_user, create_date, modify_user, modify_date)
VALUES
(888211, 1, 88821, 'Question 2.1.1', 'Guide line', 100, 1, 1, 1, 'System', CURRENT, 'System', CURRENT);

-- Group#3
INSERT INTO scorecard_group
(scorecard_group_id, scorecard_id, name, weight, sort, create_user, create_date, modify_user, modify_date)
VALUES
(8883, 888, 'Group#3', 30, 1, 'System', CURRENT, 'System', CURRENT);
-- 3 sections
INSERT INTO scorecard_section
(scorecard_section_id, scorecard_group_id, name, weight, sort, create_user, create_date, modify_user, modify_date)
VALUES
(88831, 8883, 'Section 3.1', 20, 1, 'System', CURRENT, 'System', CURRENT);
-- 1 question
INSERT INTO scorecard_question
(scorecard_question_id, scorecard_question_type_id, scorecard_section_id, description, guideline, weight, sort, upload_document, upload_document_required, create_user, create_date, modify_user, modify_date)
VALUES
(888311, 1, 88831, 'Question 3.1.1', 'Guide line', 100, 1, 1, 1, 'System', CURRENT, 'System', CURRENT);

INSERT INTO scorecard_section
(scorecard_section_id, scorecard_group_id, name, weight, sort, create_user, create_date, modify_user, modify_date)
VALUES
(88832, 8883, 'Section 3.2', 60, 1, 'System', CURRENT, 'System', CURRENT);
-- 1 question
INSERT INTO scorecard_question
(scorecard_question_id, scorecard_question_type_id, scorecard_section_id, description, guideline, weight, sort, upload_document, upload_document_required, create_user, create_date, modify_user, modify_date)
VALUES
(888321, 1, 88832, 'Question 3.2.1', 'Guide line', 100, 1, 1, 1, 'System', CURRENT, 'System', CURRENT);

INSERT INTO scorecard_section
(scorecard_section_id, scorecard_group_id, name, weight, sort, create_user, create_date, modify_user, modify_date)
VALUES
(88833, 8883, 'Section 3.3', 20, 1, 'System', CURRENT, 'System', CURRENT);
-- 1 question
INSERT INTO scorecard_question
(scorecard_question_id, scorecard_question_type_id, scorecard_section_id, description, guideline, weight, sort, upload_document, upload_document_required, create_user, create_date, modify_user, modify_date)
VALUES
(888331, 1, 88833, 'Question 3.3.1', 'Guide line', 100, 1, 1, 1, 'System', CURRENT, 'System', CURRENT);

---------------------
-- Scorecard #2
--   Group#1 (30)
--     Section#1.1 (40)
--         Question#1.1.1 (40)
--         Question#1.1.2 (60)
--     Section#1.2 (60)
--   Group#2 (40)
--     Section#2.1 (100)
--         Question#2.1.1 (100)
--   Group#3 (30)
--     Section#3.1 (20)
--         Question#3.1.1 (100)
--     Section#3.2 (60)
--         Question#3.2.1 (100)
--     Section#3.3 (20)
--         Question#3.3.1 (100)
---------------------
INSERT INTO scorecard
(scorecard_id, scorecard_status_id, scorecard_type_id, project_category_id, name, version, min_score, max_score, create_user, create_date, modify_user, modify_date)
VALUES
-- Inactive, Review, Design
(889, 2, 2, 2, 'Development Review Scorecard', '1.2', 75, 100, 'System', CURRENT, 'System', CURRENT);

-- 3 groups
-- Group#1
INSERT INTO scorecard_group
(scorecard_group_id, scorecard_id, name, weight, sort, create_user, create_date, modify_user, modify_date)
VALUES
(8891, 889, 'Group#1', 30, 1, 'System', CURRENT, 'System', CURRENT);
-- 2 sections
INSERT INTO scorecard_section
(scorecard_section_id, scorecard_group_id, name, weight, sort, create_user, create_date, modify_user, modify_date)
VALUES
(88911, 8891, 'Section 1.1', 40, 1, 'System', CURRENT, 'System', CURRENT);
-- 1 question
INSERT INTO scorecard_question
(scorecard_question_id, scorecard_question_type_id, scorecard_section_id, description, guideline, weight, sort, upload_document, upload_document_required, create_user, create_date, modify_user, modify_date)
VALUES
(889111, 1, 88911, 'Question 1.1.1', 'Guide line', 100, 1, 1, 1, 'System', CURRENT, 'System', CURRENT);

INSERT INTO scorecard_section
(scorecard_section_id, scorecard_group_id, name, weight, sort, create_user, create_date, modify_user, modify_date)
VALUES
(88912, 8891, 'Section 1.2', 60, 1, 'System', CURRENT, 'System', CURRENT);
-- 1 question
INSERT INTO scorecard_question
(scorecard_question_id, scorecard_question_type_id, scorecard_section_id, description, guideline, weight, sort, upload_document, upload_document_required, create_user, create_date, modify_user, modify_date)
VALUES
(889121, 1, 88912, 'Question 1.2.1', 'Guide line', 100, 1, 1, 1, 'System', CURRENT, 'System', CURRENT);
-- Group#2
INSERT INTO scorecard_group
(scorecard_group_id, scorecard_id, name, weight, sort, create_user, create_date, modify_user, modify_date)
VALUES
(8892, 889, 'Group#2', 40, 1, 'System', CURRENT, 'System', CURRENT);
-- 1 section
INSERT INTO scorecard_section
(scorecard_section_id, scorecard_group_id, name, weight, sort, create_user, create_date, modify_user, modify_date)
VALUES
(88921, 8892, 'Section 2.1', 100, 1, 'System', CURRENT, 'System', CURRENT);
-- 1 question
INSERT INTO scorecard_question
(scorecard_question_id, scorecard_question_type_id, scorecard_section_id, description, guideline, weight, sort, upload_document, upload_document_required, create_user, create_date, modify_user, modify_date)
VALUES
(889211, 1, 88921, 'Question 2.1.1', 'Guide line', 100, 1, 1, 1, 'System', CURRENT, 'System', CURRENT);

-- Group#3
INSERT INTO scorecard_group
(scorecard_group_id, scorecard_id, name, weight, sort, create_user, create_date, modify_user, modify_date)
VALUES
(8893, 889, 'Group#3', 30, 1, 'System', CURRENT, 'System', CURRENT);
-- 3 sections
INSERT INTO scorecard_section
(scorecard_section_id, scorecard_group_id, name, weight, sort, create_user, create_date, modify_user, modify_date)
VALUES
(88931, 8893, 'Section 3.1', 20, 1, 'System', CURRENT, 'System', CURRENT);
-- 1 question
INSERT INTO scorecard_question
(scorecard_question_id, scorecard_question_type_id, scorecard_section_id, description, guideline, weight, sort, upload_document, upload_document_required, create_user, create_date, modify_user, modify_date)
VALUES
(889311, 1, 88931, 'Question 3.1.1', 'Guide line', 100, 1, 1, 1, 'System', CURRENT, 'System', CURRENT);

INSERT INTO scorecard_section
(scorecard_section_id, scorecard_group_id, name, weight, sort, create_user, create_date, modify_user, modify_date)
VALUES
(88932, 8893, 'Section 3.2', 60, 1, 'System', CURRENT, 'System', CURRENT);
-- 1 question
INSERT INTO scorecard_question
(scorecard_question_id, scorecard_question_type_id, scorecard_section_id, description, guideline, weight, sort, upload_document, upload_document_required, create_user, create_date, modify_user, modify_date)
VALUES
(889321, 1, 88932, 'Question 3.2.1', 'Guide line', 100, 1, 1, 1, 'System', CURRENT, 'System', CURRENT);

INSERT INTO scorecard_section
(scorecard_section_id, scorecard_group_id, name, weight, sort, create_user, create_date, modify_user, modify_date)
VALUES
(88933, 8893, 'Section 3.3', 20, 1, 'System', CURRENT, 'System', CURRENT);
-- 1 question
INSERT INTO scorecard_question
(scorecard_question_id, scorecard_question_type_id, scorecard_section_id, description, guideline, weight, sort, upload_document, upload_document_required, create_user, create_date, modify_user, modify_date)
VALUES
(889331, 1, 88933, 'Question 3.3.1', 'Guide line', 100, 1, 1, 1, 'System', CURRENT, 'System', CURRENT);
