-- Note: this script will remove all information not only about all scorecard
--       templates, their groups, sections and headers, but all reviews, their
--       items and comments as well.  You'll need to repopulate the database
--       with that information using appropritate scripts.

DELETE review_item_comment;
DELETE review_item;
DELETE review_comment;
DELETE review;

DELETE FROM default_scorecard;
DELETE FROM scorecard_question;
DELETE FROM scorecard_section;
DELETE FROM scorecard_group;
DELETE FROM scorecard;
