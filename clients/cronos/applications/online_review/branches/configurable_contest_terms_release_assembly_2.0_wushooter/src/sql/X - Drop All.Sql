-- This scripts deletes (drops) all the tables in the database, but it doesn't
-- deletes the databse itself.  You'll need to recreate all the tables, and
-- repopulate them with the lookup data at least to be able to use the application.

DROP TABLE default_scorecard;


DROP TABLE screening_result;
DROP TABLE screening_response_lu;
DROP TABLE response_severity_lu;
DROP TABLE screening_task;
DROP TABLE screening_status_lu;


DROP TABLE notification;
DROP TABLE notification_type_lu;
DROP TABLE project_audit;
DROP TABLE deliverable_lu;
DROP TABLE review_item_comment;
DROP TABLE review_comment;
DROP TABLE review_item;
DROP TABLE review;
DROP TABLE comment_type_lu;
DROP TABLE resource_submission;
DROP TABLE submission;
DROP TABLE submission_status_lu;
DROP TABLE upload;
DROP TABLE upload_status_lu;
DROP TABLE upload_type_lu;
DROP TABLE resource_info;
DROP TABLE resource_info_type_lu;
DROP TABLE resource;
DROP TABLE resource_role_lu;
DROP TABLE phase_criteria;
DROP TABLE phase_criteria_type_lu;
DROP TABLE phase_dependency;
DROP TABLE project_phase;
DROP TABLE phase_type_lu;
DROP TABLE phase_status_lu;
DROP TABLE project_info;
DROP TABLE project_info_type_lu;
DROP TABLE project;
DROP TABLE project_status_lu;
DROP TABLE scorecard_question;
DROP TABLE scorecard_question_type_lu;
DROP TABLE scorecard_section;
DROP TABLE scorecard_group;
DROP TABLE scorecard;
DROP TABLE scorecard_status_lu;
DROP TABLE scorecard_type_lu;
DROP TABLE project_category_lu;
DROP TABLE project_type_lu;


DROP TABLE user_reliability;
DROP TABLE user_rating;
DROP TABLE email;
DROP TABLE user;


DROP TABLE id_sequences;
