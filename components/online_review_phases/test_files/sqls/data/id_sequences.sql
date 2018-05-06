database online_review;

delete from id_sequences;

-- FOR resource management

INSERT INTO id_sequences(name, next_block_start, block_size, exhausted)
  VALUES('resource_id_seq', 1, 1, 0); 
INSERT INTO id_sequences(name, next_block_start, block_size, exhausted)
  VALUES('notification_type_id_seq', 1, 1, 0); 
INSERT INTO id_sequences(name, next_block_start, block_size, exhausted)
  VALUES('resource_role_id_seq', 1, 1, 0); 

-- FOR project management

INSERT INTO id_sequences(name, next_block_start, block_size, exhausted)
  VALUES('project_id_seq', 1, 1, 0);
INSERT INTO id_sequences(name, next_block_start, block_size, exhausted)
  VALUES('project_audit_id_seq', 1, 1, 0);

-- FOR phase_management

INSERT INTO id_sequences(name, next_block_start, block_size, exhausted)
  VALUES('phase_id_seq', 1000, 1, 0);

-- FOR deliverable_management

INSERT INTO id_sequences(name, next_block_start, block_size, exhausted)
  VALUES('upload_id_seq', 1, 1, 0);
INSERT INTO id_sequences(name, next_block_start, block_size, exhausted)
  VALUES('upload_type_id_seq', 1, 1, 0);
INSERT INTO id_sequences(name, next_block_start, block_size, exhausted)
  VALUES('upload_status_id_seq', 1, 1, 0);
INSERT INTO id_sequences(name, next_block_start, block_size, exhausted)
  VALUES('submission_id_seq', 1, 1, 0);
INSERT INTO id_sequences(name, next_block_start, block_size, exhausted)
  VALUES('submission_status_id_seq', 1, 1, 0);

-- FOR scorecard management

INSERT INTO id_sequences(name, next_block_start, block_size, exhausted)
  VALUES('scorecard_id_seq', 1, 1, 0);
INSERT INTO id_sequences(name, next_block_start, block_size, exhausted)
  VALUES('scorecard_group_id_seq', 1, 1, 0);
INSERT INTO id_sequences(name, next_block_start, block_size, exhausted)
  VALUES('scorecard_section_id_seq', 1, 1, 0);
INSERT INTO id_sequences(name, next_block_start, block_size, exhausted)
  VALUES('scorecard_question_id_seq', 1, 1, 0);

-- FOR screening management

INSERT INTO id_sequences(name, next_block_start, block_size, exhausted)
  VALUES('screening_task_id_seq', 1, 1, 0);
INSERT INTO id_sequences(name, next_block_start, block_size, exhausted)
  VALUES('screening_result_id_seq', 1, 1, 0);

-- FOR review management

INSERT INTO id_sequences(name, next_block_start, block_size, exhausted)
  VALUES('review_id_seq', 1, 1, 0);
INSERT INTO id_sequences(name, next_block_start, block_size, exhausted)
  VALUES('review_item_id_seq', 1, 1, 0);
INSERT INTO id_sequences(name, next_block_start, block_size, exhausted)
  VALUES('review_comment_id_seq', 1, 1, 0);
INSERT INTO id_sequences(name, next_block_start, block_size, exhausted)
  VALUES('review_item_comment_id_seq', 1, 1, 0);
  
close database;
