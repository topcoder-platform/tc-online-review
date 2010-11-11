-- Note: this script will remove all information not only about all resources,
--       but about all submissions, uploads, automatic screening results,
--       reviews, their items and comments as well.  You'll need to repopulate
--       the database with that information using appropritate scripts.

DELETE screening_result;
DELETE screening_task;

DELETE review_item_comment;
DELETE review_item;
DELETE review_comment;
DELETE review;

DELETE FROM submission;
DELETE FROM upload;

DELETE FROM notification;
DELETE FROM resource_info;
DELETE FROM resource;
