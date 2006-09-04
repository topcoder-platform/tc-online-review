INSERT INTO review (review_id, resource_id, submission_id, scorecard_id, committed, create_user, create_date, modify_user, modify_date)
  VALUES (4000, 209, 3000, 201, 0, 'System', CURRENT, 'System', CURRENT);


INSERT INTO review_item (review_item_id, review_id, scorecard_question_id, answer, sort, create_user, create_date, modify_user, modify_date)
  VALUES (4101, 4000, 201, '4/4', 0, 'slion', CURRENT, 'slion', CURRENT);
INSERT INTO review_item (review_item_id, review_id, scorecard_question_id, answer, sort, create_user, create_date, modify_user, modify_date)
  VALUES (4102, 4000, 201, '3/4', 1, 'qiucx0161', CURRENT, 'qiucx0161', CURRENT);
INSERT INTO review_item (review_item_id, review_id, scorecard_question_id, answer, sort, create_user, create_date, modify_user, modify_date)
  VALUES (4103, 4000, 201, '2/4', 2, 'ivern', CURRENT, 'ivern', CURRENT);
INSERT INTO review_item (review_item_id, review_id, scorecard_question_id, answer, sort, create_user, create_date, modify_user, modify_date)
  VALUES (4104, 4000, 202, '4/4', 3, 'slion', CURRENT, 'slion', CURRENT);
INSERT INTO review_item (review_item_id, review_id, scorecard_question_id, answer, sort, create_user, create_date, modify_user, modify_date)
  VALUES (4105, 4000, 202, '3/4', 4, 'qiucx0161', CURRENT, 'qiucx0161', CURRENT);
INSERT INTO review_item (review_item_id, review_id, scorecard_question_id, answer, sort, create_user, create_date, modify_user, modify_date)
  VALUES (4106, 4000, 202, '2/4', 5, 'ivern', CURRENT, 'ivern', CURRENT);
INSERT INTO review_item (review_item_id, review_id, scorecard_question_id, answer, sort, create_user, create_date, modify_user, modify_date)
  VALUES (4107, 4000, 203, 10/10, 6, 'slion', CURRENT, 'slion', CURRENT);
INSERT INTO review_item (review_item_id, review_id, scorecard_question_id, answer, sort, create_user, create_date, modify_user, modify_date)
  VALUES (4108, 4000, 203, '8/10', 7, 'qiucx0161', CURRENT, 'qiucx0161', CURRENT);
INSERT INTO review_item (review_item_id, review_id, scorecard_question_id, answer, sort, create_user, create_date, modify_user, modify_date)
  VALUES (4109, 4000, 203, '5/10', 8, 'ivern', CURRENT, 'ivern', CURRENT);
INSERT INTO review_item (review_item_id, review_id, scorecard_question_id, answer, sort, create_user, create_date, modify_user, modify_date)
  VALUES (4110, 4000, 204, '50/50', 9, 'slion', CURRENT, 'slion', CURRENT);
INSERT INTO review_item (review_item_id, review_id, scorecard_question_id, answer, sort, create_user, create_date, modify_user, modify_date)
  VALUES (4111, 4000, 204, '34/54', 10, 'qiucx0161', CURRENT, 'qiucx0161', CURRENT);
INSERT INTO review_item (review_item_id, review_id, scorecard_question_id, answer, sort, create_user, create_date, modify_user, modify_date)
  VALUES (4112, 4000, 204, '50/100', 11, 'ivern', CURRENT, 'ivern', CURRENT);
INSERT INTO review_item (review_item_id, review_id, scorecard_question_id, answer, sort, create_user, create_date, modify_user, modify_date)
  VALUES (4113, 4000, 205, '1', 12, 'slion', CURRENT, 'slion', CURRENT);
INSERT INTO review_item (review_item_id, review_id, scorecard_question_id, answer, sort, create_user, create_date, modify_user, modify_date)
  VALUES (4114, 4000, 205, '1', 13, 'qiucx0161', CURRENT, 'qiucx0161', CURRENT);
INSERT INTO review_item (review_item_id, review_id, scorecard_question_id, answer, sort, create_user, create_date, modify_user, modify_date)
  VALUES (4115, 4000, 205, '0', 14, 'ivern', CURRENT, 'ivern', CURRENT);
INSERT INTO review_item (review_item_id, review_id, scorecard_question_id, answer, sort, create_user, create_date, modify_user, modify_date)
  VALUES (4116, 4000, 206, '64/65', 15, 'slion', CURRENT, 'slion', CURRENT);
INSERT INTO review_item (review_item_id, review_id, scorecard_question_id, answer, sort, create_user, create_date, modify_user, modify_date)
  VALUES (4117, 4000, 206, '45/56', 16, 'qiucx0161', CURRENT, 'qiucx0161', CURRENT);
INSERT INTO review_item (review_item_id, review_id, scorecard_question_id, answer, sort, create_user, create_date, modify_user, modify_date)
  VALUES (4118, 4000, 206, '23/46', 17, 'ivern', CURRENT, 'ivern', CURRENT);


INSERT INTO review_item_comment (review_item_comment_id, resource_id, review_item_id, comment_type_id, content, sort, create_user, create_date, modify_user, modify_date)
  VALUES (4201, 208, 4101, 1, 'fine.', 0, 'slion', CURRENT, 'slion', CURRENT);
INSERT INTO review_item_comment (review_item_comment_id, resource_id, review_item_id, comment_type_id, content, sort, create_user, create_date, modify_user, modify_date)
  VALUES (4202, 206, 4102, 3, 'aaa', 0, 'qiucx0161', CURRENT, 'qiucx0161', CURRENT);
INSERT INTO review_item_comment (review_item_comment_id, resource_id, review_item_id, comment_type_id, content, sort, create_user, create_date, modify_user, modify_date)
  VALUES (4203, 207, 4103, 3, 'half-score comment 1.', 0, 'ivern', CURRENT, 'ivern', CURRENT);
INSERT INTO review_item_comment (review_item_comment_id, resource_id, review_item_id, comment_type_id, content, sort, create_user, create_date, modify_user, modify_date)
  VALUES (4204, 208, 4104, 1, 'seems ok.', 0, 'slion', CURRENT, 'slion', CURRENT);
INSERT INTO review_item_comment (review_item_comment_id, resource_id, review_item_id, comment_type_id, content, sort, create_user, create_date, modify_user, modify_date)
  VALUES (4205, 206, 4105, 3, 'bbb', 0, 'qiucx0161', CURRENT, 'qiucx0161', CURRENT);
INSERT INTO review_item_comment (review_item_comment_id, resource_id, review_item_id, comment_type_id, content, sort, create_user, create_date, modify_user, modify_date)
  VALUES (4206, 207, 4106, 3, 'half-score comment 2.', 0, 'ivern', CURRENT, 'ivern', CURRENT);
INSERT INTO review_item_comment (review_item_comment_id, resource_id, review_item_id, comment_type_id, content, sort, create_user, create_date, modify_user, modify_date)
  VALUES (4207, 208, 4107, 1, 'ok.', 0, 'slion', CURRENT, 'slion', CURRENT);
INSERT INTO review_item_comment (review_item_comment_id, resource_id, review_item_id, comment_type_id, content, sort, create_user, create_date, modify_user, modify_date)
  VALUES (4208, 206, 4108, 3, 'ccc', 0, 'qiucx0161', CURRENT, 'qiucx0161', CURRENT);
INSERT INTO review_item_comment (review_item_comment_id, resource_id, review_item_id, comment_type_id, content, sort, create_user, create_date, modify_user, modify_date)
  VALUES (4209, 207, 4109, 3, 'half-score comment 3.', 0, 'ivern', CURRENT, 'ivern', CURRENT);
INSERT INTO review_item_comment (review_item_comment_id, resource_id, review_item_id, comment_type_id, content, sort, create_user, create_date, modify_user, modify_date)
  VALUES (4210, 208, 4110, 1, 'nice.', 0, 'slion', CURRENT, 'slion', CURRENT);
INSERT INTO review_item_comment (review_item_comment_id, resource_id, review_item_id, comment_type_id, content, sort, create_user, create_date, modify_user, modify_date)
  VALUES (4211, 206, 4111, 3, 'ddd', 0, 'qiucx0161', CURRENT, 'qiucx0161', CURRENT);
INSERT INTO review_item_comment (review_item_comment_id, resource_id, review_item_id, comment_type_id, content, sort, create_user, create_date, modify_user, modify_date)
  VALUES (4212, 207, 4112, 3, 'half-score comment 4.', 0, 'ivern', CURRENT, 'ivern', CURRENT);
INSERT INTO review_item_comment (review_item_comment_id, resource_id, review_item_id, comment_type_id, content, sort, create_user, create_date, modify_user, modify_date)
  VALUES (4213, 208, 4113, 1, 'works.', 0, 'slion', CURRENT, 'slion', CURRENT);
INSERT INTO review_item_comment (review_item_comment_id, resource_id, review_item_id, comment_type_id, content, sort, create_user, create_date, modify_user, modify_date)
  VALUES (4214, 206, 4114, 3, 'eee', 0, 'qiucx0161', CURRENT, 'qiucx0161', CURRENT);
INSERT INTO review_item_comment (review_item_comment_id, resource_id, review_item_id, comment_type_id, content, sort, create_user, create_date, modify_user, modify_date)
  VALUES (4215, 207, 4115, 3, 'No score comment.', 0, 'ivern', CURRENT, 'ivern', CURRENT);
INSERT INTO review_item_comment (review_item_comment_id, resource_id, review_item_id, comment_type_id, content, sort, create_user, create_date, modify_user, modify_date)
  VALUES (4216, 208, 4116, 1, '.....', 0, 'slion', CURRENT, 'slion', CURRENT);
INSERT INTO review_item_comment (review_item_comment_id, resource_id, review_item_id, comment_type_id, content, sort, create_user, create_date, modify_user, modify_date)
  VALUES (4217, 206, 4117, 3, 'fff', 0, 'qiucx0161', CURRENT, 'qiucx0161', CURRENT);
INSERT INTO review_item_comment (review_item_comment_id, resource_id, review_item_id, comment_type_id, content, sort, create_user, create_date, modify_user, modify_date)
  VALUES (4218, 207, 4118, 1, 'half-score comment 5.', 0, 'ivern', CURRENT, 'ivern', CURRENT);
