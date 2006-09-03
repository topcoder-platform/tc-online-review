-- Note: this script will remove all information not only about all phases,
--       but about all submissions, uploads, resources, and notifications as
--       well.  You'll need to repopulate the database with that information
--       using appropritate scripts.

DELETE review_item_comment;
DELETE review_item;
DELETE review_comment;
DELETE review;

DELETE FROM submission;
DELETE FROM upload;

DELETE FROM notification;
DELETE FROM resource_info;
DELETE FROM resource;

DELETE FROM phase_criteria;
DELETE FROM phase_dependency;
DELETE FROM project_phase;
