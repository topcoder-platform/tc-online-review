-- Note: this script will remove all information not only about all uploads and
--       submissions, but about all reviews, their items and comments, and also
--       about all auto screening results as well.  You'll need to repopulate
--       the database with that information using appropritate scripts.

DELETE FROM screening_result;
DELETE FROM screening_task;

DELETE FROM review_item_comment;
DELETE FROM review_item;
DELETE FROM review_comment;
DELETE FROM review;

DELETE FROM resource_submission;
DELETE FROM submission;
DELETE FROM upload;
