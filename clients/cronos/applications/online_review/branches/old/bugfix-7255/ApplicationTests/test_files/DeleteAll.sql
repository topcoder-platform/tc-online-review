-- This scripts deletes (drops) all the tables in the database, but it doesn't
-- deletes the databse itself.  You'll need to recreate all the tables, and
-- repopulate them with the lookup data at least to be able to use the application.

DELETE FROM screening_result;
DELETE FROM screening_response_lu;
DELETE FROM response_severity_lu;
DELETE FROM screening_task;
DELETE FROM screening_status_lu;


DELETE FROM notification;
DELETE FROM notification_type_lu;
DELETE FROM project_audit;
DELETE FROM deliverable_lu;
DELETE FROM review_item_comment;
DELETE FROM review_comment;
DELETE FROM review_item;
DELETE FROM review;
DELETE FROM comment_type_lu;
DELETE FROM resource_submission;
DELETE FROM submission;
DELETE FROM submission_status_lu;
DELETE FROM upload;
DELETE FROM upload_status_lu;
DELETE FROM upload_type_lu;
DELETE FROM resource_info;
DELETE FROM resource_info_type_lu;
DELETE FROM resource;
DELETE FROM resource_role_lu;
DELETE FROM phase_criteria;
DELETE FROM phase_criteria_type_lu;
DELETE FROM phase_dependency;
DELETE FROM project_phase;
DELETE FROM phase_type_lu;
DELETE FROM phase_status_lu;
DELETE FROM project_info;
DELETE FROM project_info_type_lu;
DELETE FROM project;
DELETE FROM project_status_lu;
DELETE FROM scorecard_question;
DELETE FROM scorecard_question_type_lu;
DELETE FROM scorecard_section;
DELETE FROM scorecard_group;
DELETE FROM scorecard;
DELETE FROM scorecard_status_lu;
DELETE FROM scorecard_type_lu;
DELETE FROM project_category_lu;
DELETE FROM project_type_lu;

DELETE FROM user_reliability;
DELETE FROM user_rating;
DELETE FROM email;
DELETE FROM user;

DELETE FROM id_sequences;
