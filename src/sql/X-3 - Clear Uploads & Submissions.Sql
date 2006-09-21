-- Note: this script will remove all information not only about all uploads and
--       submissions, but about all reviews, their items and comments, and also
--       about all auto screening results as well.  You'll need to repopulate
--       the database with that information using appropritate scripts.

DELETE screening_result;
DELETE screening_task;

DELETE review_item_comment;
DELETE review_item;
DELETE review_comment;
DELETE review;

DELETE FROM submission;
DELETE FROM upload;
