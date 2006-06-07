INSERT INTO project_type_lu(project_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(1, 'Component', 'Component', 'System', CURRENT, 'System', CURRENT);
INSERT INTO project_type_lu(project_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(2, 'Application', 'Application', 'System', CURRENT, 'System', CURRENT);

INSERT INTO project_category_lu(project_category_id, project_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(1, 1, 'Design', 'Design', 'System', CURRENT, 'System', CURRENT);
INSERT INTO project_category_lu(project_category_id, project_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(2, 1, 'Development', 'Development', 'System', CURRENT, 'System', CURRENT);
INSERT INTO project_category_lu(project_category_id, project_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(3, 1, 'Security', 'Security', 'System', CURRENT, 'System', CURRENT);
INSERT INTO project_category_lu(project_category_id, project_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(4, 1, 'Process', 'Process', 'System', CURRENT, 'System', CURRENT);
INSERT INTO project_category_lu(project_category_id, project_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(5, 1, 'Testing Competition', 'Testing Competition', 'System', CURRENT, 'System', CURRENT);
INSERT INTO project_category_lu(project_category_id, project_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(6, 2, 'Specification', 'Specification', 'System', CURRENT, 'System', CURRENT);
INSERT INTO project_category_lu(project_category_id, project_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(7, 2, 'Architecture', 'Architecture', 'System', CURRENT, 'System', CURRENT);
INSERT INTO project_category_lu(project_category_id, project_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(8, 2, 'Component Production', 'Component Production', 'System', CURRENT, 'System', CURRENT);
INSERT INTO project_category_lu(project_category_id, project_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(9, 2, 'Quality Assurance', 'Quality Assurance', 'System', CURRENT, 'System', CURRENT);
INSERT INTO project_category_lu(project_category_id, project_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(10, 2, 'Deployment', 'Deployment', 'System', CURRENT, 'System', CURRENT);
INSERT INTO project_category_lu(project_category_id, project_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(11, 2, 'Security', 'Security', 'System', CURRENT, 'System', CURRENT);
INSERT INTO project_category_lu(project_category_id, project_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(12, 2, 'Process', 'Process', 'System', CURRENT, 'System', CURRENT);
INSERT INTO project_category_lu(project_category_id, project_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(13, 2, 'Testing Competition', 'Testing Competition', 'System', CURRENT, 'System', CURRENT);
INSERT INTO project_category_lu(project_category_id, project_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(14, 2, 'Assembly Competition', 'Assembly Competition', 'System', CURRENT, 'System', CURRENT);

INSERT INTO scorecard_type_lu(scorecard_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(1, 'Screening', 'Screening', 'System', CURRENT, 'System', CURRENT);
INSERT INTO scorecard_type_lu(scorecard_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(2, 'Review', 'Review', 'System', CURRENT, 'System', CURRENT);
INSERT INTO scorecard_type_lu(scorecard_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(3, 'Client Review', 'Client Review', 'System', CURRENT, 'System', CURRENT);

INSERT INTO scorecard_status_lu(scorecard_status_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(1, 'Active', 'Active', 'System', CURRENT, 'System', CURRENT);
INSERT INTO scorecard_status_lu(scorecard_status_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(2, 'Inactive', 'Inactive', 'System', CURRENT, 'System', CURRENT);
INSERT INTO scorecard_status_lu(scorecard_status_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(3, 'Deleted', 'Deleted', 'System', CURRENT, 'System', CURRENT);

INSERT INTO scorecard_question_type_lu(scorecard_question_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(1, 'Scale (1-4)', 'Scale (1-4)', 'System', CURRENT, 'System', CURRENT);
INSERT INTO scorecard_question_type_lu(scorecard_question_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(2, 'Scale (1-10)', 'Scale (1-10)', 'System', CURRENT, 'System', CURRENT);
INSERT INTO scorecard_question_type_lu(scorecard_question_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(3, 'Test Case', 'Test Case', 'System', CURRENT, 'System', CURRENT);
INSERT INTO scorecard_question_type_lu(scorecard_question_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(4, 'Yes/No', 'Yes/No', 'System', CURRENT, 'System', CURRENT);
INSERT INTO scorecard_question_type_lu(scorecard_question_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(5, 'Dynamic', 'Dynamic', 'System', CURRENT, 'System', CURRENT);

INSERT INTO project_status_lu(project_status_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(1, 'Active', 'Active', 'System', CURRENT, 'System', CURRENT);
INSERT INTO project_status_lu(project_status_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(2, 'Inactive', 'Inactive', 'System', CURRENT, 'System', CURRENT);
INSERT INTO project_status_lu(project_status_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(3, 'Deleted', 'Deleted', 'System', CURRENT, 'System', CURRENT);
INSERT INTO project_status_lu(project_status_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(4, 'Cancelled - Failed Review', 'Cancelled - Failed Review', 'System', CURRENT, 'System', CURRENT);
INSERT INTO project_status_lu(project_status_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(5, 'Cancelled - Failed Screening', 'Cancelled - Failed Screening', 'System', CURRENT, 'System', CURRENT);
INSERT INTO project_status_lu(project_status_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(6, 'Cancelled - Zero Submissions', 'Cancelled - Zero Submissions', 'System', CURRENT, 'System', CURRENT);
INSERT INTO project_status_lu(project_status_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(7, 'Completed', 'Completed', 'System', CURRENT, 'System', CURRENT);

INSERT INTO project_info_type_lu(project_info_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(1, 'External Reference ID', 'External Reference ID', 'System', CURRENT, 'System', CURRENT);
INSERT INTO project_info_type_lu(project_info_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(2, 'Component ID', 'Component ID', 'System', CURRENT, 'System', CURRENT);
INSERT INTO project_info_type_lu(project_info_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(3, 'Version ID', 'Version ID', 'System', CURRENT, 'System', CURRENT);
INSERT INTO project_info_type_lu(project_info_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(4, 'Developer Forum ID', 'Developer Forum ID', 'System', CURRENT, 'System', CURRENT);
INSERT INTO project_info_type_lu(project_info_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(5, 'Root Catalog ID', 'Root Catelog ID', 'System', CURRENT, 'System', CURRENT);
INSERT INTO project_info_type_lu(project_info_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(6, 'Project Name', 'Project Name', 'System', CURRENT, 'System', CURRENT);
INSERT INTO project_info_type_lu(project_info_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(7, 'Project Version', 'Project Version', 'System', CURRENT, 'System', CURRENT);
INSERT INTO project_info_type_lu(project_info_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(8, 'SVN Module', 'SVN Module', 'System', CURRENT, 'System', CURRENT);
INSERT INTO project_info_type_lu(project_info_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(9, 'Autopilot Option', 'Autopilot Option', 'System', CURRENT, 'System', CURRENT);
INSERT INTO project_info_type_lu(project_info_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(10, 'Status Notification', 'Status Notification', 'System', CURRENT, 'System', CURRENT);
INSERT INTO project_info_type_lu(project_info_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(11, 'Timeline Notification', 'Timeline Notification', 'System', CURRENT, 'System', CURRENT);
INSERT INTO project_info_type_lu(project_info_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(12, 'Public', 'Public', 'System', CURRENT, 'System', CURRENT);
INSERT INTO project_info_type_lu(project_info_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(13, 'Rated', 'Rated', 'System', CURRENT, 'System', CURRENT);
INSERT INTO project_info_type_lu(project_info_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(14, 'Eligibility', 'Eligibility', 'System', CURRENT, 'System', CURRENT);
INSERT INTO project_info_type_lu(project_info_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(15, 'Payments Required', 'Payments Required', 'System', CURRENT, 'System', CURRENT);
INSERT INTO project_info_type_lu(project_info_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(16, 'Notes', 'Notes', 'System', CURRENT, 'System', CURRENT);
INSERT INTO project_info_type_lu(project_info_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(17, 'Deactivated Timestamp', 'Deactivated Timestamp', 'System', CURRENT, 'System', CURRENT);
INSERT INTO project_info_type_lu(project_info_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(18, 'Deactivated Phase', 'Deactivated Phase', 'System', CURRENT, 'System', CURRENT);
INSERT INTO project_info_type_lu(project_info_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(19, 'Deactivated Reason', 'Deactivated Reason', 'System', CURRENT, 'System', CURRENT);

INSERT INTO scorecard_assignment_lu(scorecard_assignment_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(1, 'Screening Scorecard', 'Screening Scorecard', 'System', CURRENT, 'System', CURRENT);
INSERT INTO scorecard_assignment_lu(scorecard_assignment_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(2, 'Review Scorecard', 'Review Scorecard', 'System', CURRENT, 'System', CURRENT);
INSERT INTO scorecard_assignment_lu(scorecard_assignment_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(3, 'Approval Scorecard', 'Approval Scorecard', 'System', CURRENT, 'System', CURRENT);

INSERT INTO phase_type_lu(phase_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(1, 'Registration', 'Registration', 'System', CURRENT, 'System', CURRENT);
INSERT INTO phase_type_lu(phase_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(2, 'Submission', 'Submission', 'System', CURRENT, 'System', CURRENT);
INSERT INTO phase_type_lu(phase_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(3, 'Screening', 'Screening', 'System', CURRENT, 'System', CURRENT);
INSERT INTO phase_type_lu(phase_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(4, 'Review', 'Review', 'System', CURRENT, 'System', CURRENT);
INSERT INTO phase_type_lu(phase_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(5, 'Appeals', 'Appeals', 'System', CURRENT, 'System', CURRENT);
INSERT INTO phase_type_lu(phase_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(6, 'Appeals Response', 'Appeals Response', 'System', CURRENT, 'System', CURRENT);
INSERT INTO phase_type_lu(phase_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(7, 'Aggregation', 'Aggregation', 'System', CURRENT, 'System', CURRENT);
INSERT INTO phase_type_lu(phase_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(8, 'Aggregation Review', 'Aggregation Review', 'System', CURRENT, 'System', CURRENT);
INSERT INTO phase_type_lu(phase_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(9, 'Final Fix', 'Final Fix', 'System', CURRENT, 'System', CURRENT);
INSERT INTO phase_type_lu(phase_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(10, 'Final Review', 'Final Review', 'System', CURRENT, 'System', CURRENT);
INSERT INTO phase_type_lu(phase_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(11, 'Approval', 'Approval', 'System', CURRENT, 'System', CURRENT);

INSERT INTO phase_status_lu(phase_status_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(1, 'Scheduled', 'Scheduled', 'System', CURRENT, 'System', CURRENT);
INSERT INTO phase_status_lu(phase_status_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(2, 'Open', 'Open', 'System', CURRENT, 'System', CURRENT);
INSERT INTO phase_status_lu(phase_status_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(3, 'Closed', 'Closed', 'System', CURRENT, 'System', CURRENT);

INSERT INTO phase_criteria_type_lu(phase_criteria_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(1, 'Registration Number', 'Registration Number', 'System', CURRENT, 'System', CURRENT);
INSERT INTO phase_criteria_type_lu(phase_criteria_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(2, 'Submission Number', 'Submission Number', 'System', CURRENT, 'System', CURRENT);

INSERT INTO resource_role_lu(resource_role_id, phase_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(1, NULL, 'Submitter', 'Submitter', 'System', CURRENT, 'System', CURRENT);
INSERT INTO resource_role_lu(resource_role_id, phase_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(2, 3, 'Primary Screener', 'Primary Screener', 'System', CURRENT, 'System', CURRENT);
INSERT INTO resource_role_lu(resource_role_id, phase_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(3, 3, 'Screener', 'Screener', 'System', CURRENT, 'System', CURRENT);
INSERT INTO resource_role_lu(resource_role_id, phase_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(4, 4, 'Reviewer', 'Reviewer', 'System', CURRENT, 'System', CURRENT);
INSERT INTO resource_role_lu(resource_role_id, phase_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(5, 4, 'Accuracy Reviewer', 'Accuracy Reviewer', 'System', CURRENT, 'System', CURRENT);
INSERT INTO resource_role_lu(resource_role_id, phase_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(6, 4, 'Failure Reviewer', 'Failure Reviewer', 'System', CURRENT, 'System', CURRENT);
INSERT INTO resource_role_lu(resource_role_id, phase_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(7, 4, 'Stress Reviewer', 'Stress Reviewer', 'System', CURRENT, 'System', CURRENT);
INSERT INTO resource_role_lu(resource_role_id, phase_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(8, 7, 'Aggregator', 'Aggregator', 'System', CURRENT, 'System', CURRENT);
INSERT INTO resource_role_lu(resource_role_id, phase_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(9, 10, 'Final Reviewer', 'Final Reviewer', 'System', CURRENT, 'System', CURRENT);
INSERT INTO resource_role_lu(resource_role_id, phase_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(10, 11, 'Approver', 'Approver', 'System', CURRENT, 'System', CURRENT);
INSERT INTO resource_role_lu(resource_role_id, phase_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(11, NULL, 'Designer', 'Designer', 'System', CURRENT, 'System', CURRENT);
INSERT INTO resource_role_lu(resource_role_id, phase_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(12, NULL, 'Observer', 'Observer', 'System', CURRENT, 'System', CURRENT);
INSERT INTO resource_role_lu(resource_role_id, phase_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(13, NULL, 'Manager', 'Manager', 'System', CURRENT, 'System', CURRENT);

INSERT INTO resource_info_type_lu(resource_info_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(1, 'External Reference ID', 'External Reference ID', 'System', CURRENT, 'System', CURRENT);
INSERT INTO resource_info_type_lu(resource_info_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(2, 'Handle', 'Handle', 'System', CURRENT, 'System', CURRENT);
INSERT INTO resource_info_type_lu(resource_info_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(3, 'Email', 'Email', 'System', CURRENT, 'System', CURRENT);
INSERT INTO resource_info_type_lu(resource_info_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(4, 'Rating', 'Rating', 'System', CURRENT, 'System', CURRENT);
INSERT INTO resource_info_type_lu(resource_info_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(5, 'Reliability', 'Reliability', 'System', CURRENT, 'System', CURRENT);
INSERT INTO resource_info_type_lu(resource_info_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(6, 'Registration Date', 'Registration Date', 'System', CURRENT, 'System', CURRENT);
INSERT INTO resource_info_type_lu(resource_info_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(7, 'Payment', 'Payment', 'System', CURRENT, 'System', CURRENT);
INSERT INTO resource_info_type_lu(resource_info_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(8, 'Payment Status', 'Payment Status', 'System', CURRENT, 'System', CURRENT);
INSERT INTO resource_info_type_lu(resource_info_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(9, 'Screening Score', 'Screening Score', 'System', CURRENT, 'System', CURRENT);
INSERT INTO resource_info_type_lu(resource_info_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(10, 'Initial Score', 'Initial Score', 'System', CURRENT, 'System', CURRENT);
INSERT INTO resource_info_type_lu(resource_info_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(11, 'Final Score', 'Final Score', 'System', CURRENT, 'System', CURRENT);
INSERT INTO resource_info_type_lu(resource_info_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(12, 'Placement', 'Placement', 'System', CURRENT, 'System', CURRENT);

INSERT INTO upload_type_lu(upload_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(1, 'Submission', 'Submission', 'System', CURRENT, 'System', CURRENT);
INSERT INTO upload_type_lu(upload_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(2, 'Test Case', 'Test Case', 'System', CURRENT, 'System', CURRENT);
INSERT INTO upload_type_lu(upload_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(3, 'Final Fix', 'Final Fix', 'System', CURRENT, 'System', CURRENT);
INSERT INTO upload_type_lu(upload_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(4, 'Review Document', 'Review Document', 'System', CURRENT, 'System', CURRENT);

INSERT INTO upload_status_lu(upload_status_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(1, 'Active', 'Active', 'System', CURRENT, 'System', CURRENT);
INSERT INTO upload_status_lu(upload_status_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(2, 'Deleted', 'Deleted', 'System', CURRENT, 'System', CURRENT);

INSERT INTO submission_status_lu(submission_status_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(1, 'Active', 'Active', 'System', CURRENT, 'System', CURRENT);
INSERT INTO submission_status_lu(submission_status_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(2, 'Failed Screening', 'Failed Manual Screening', 'System', CURRENT, 'System', CURRENT);
INSERT INTO submission_status_lu(submission_status_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(3, 'Failed Review', 'Failed Review', 'System', CURRENT, 'System', CURRENT);
INSERT INTO submission_status_lu(submission_status_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(4, 'Completed Without Win', 'Completed Without Win', 'System', CURRENT, 'System', CURRENT);
INSERT INTO submission_status_lu(submission_status_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(5, 'Deleted', 'Deleted', 'System', CURRENT, 'System', CURRENT);

INSERT INTO comment_type_lu(comment_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(1, 'Comment', 'Comment', 'System', CURRENT, 'System', CURRENT);
INSERT INTO comment_type_lu(comment_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(2, 'Recommended', 'Recommended', 'System', CURRENT, 'System', CURRENT);
INSERT INTO comment_type_lu(comment_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(3, 'Required', 'Required', 'System', CURRENT, 'System', CURRENT);
INSERT INTO comment_type_lu(comment_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(4, 'Appeal', 'Appeal', 'System', CURRENT, 'System', CURRENT);
INSERT INTO comment_type_lu(comment_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(5, 'Appeal Response', 'Appeal Response', 'System', CURRENT, 'System', CURRENT);
INSERT INTO comment_type_lu(comment_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(6, 'Aggregation Comment', 'Aggregator Comment', 'System', CURRENT, 'System', CURRENT);
INSERT INTO comment_type_lu(comment_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(7, 'Aggregation Review Comment', 'Aggregator Comment', 'System', CURRENT, 'System', CURRENT);
INSERT INTO comment_type_lu(comment_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(8, 'Submitter Comment', 'Submitter Comment', 'System', CURRENT, 'System', CURRENT);
INSERT INTO comment_type_lu(comment_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(9, 'Final Fix Comment', 'Final Fix Comment', 'System', CURRENT, 'System', CURRENT);
INSERT INTO comment_type_lu(comment_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(10, 'Final Review Comment', 'Final Review Comment', 'System', CURRENT, 'System', CURRENT);
INSERT INTO comment_type_lu(comment_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(11, 'Manager Comment', 'Manager Comment', 'System', CURRENT, 'System', CURRENT);

INSERT INTO deliverable_lu(deliverable_id, phase_type_id, resource_role_id, name, description, per_submission, required, creation_user, creation_date, modification_user, modification_date)
  VALUES(1, 2, 1, 'Submission', 'Submission', 'F', 'F', 'System', CURRENT, 'System', CURRENT);
INSERT INTO deliverable_lu(deliverable_id, phase_type_id, resource_role_id, name, description, per_submission, required, creation_user, creation_date, modification_user, modification_date)
  VALUES(2, 3, 2, 'Screening Scorecard', 'Screening Scorecard', 'T', 'T', 'System', CURRENT, 'System', CURRENT);
INSERT INTO deliverable_lu(deliverable_id, phase_type_id, resource_role_id, name, description, per_submission, required, creation_user, creation_date, modification_user, modification_date)
  VALUES(3, 3, 3, 'Screening Scorecard', 'Screening Scorecard', 'T', 'T', 'System', CURRENT, 'System', CURRENT);
INSERT INTO deliverable_lu(deliverable_id, phase_type_id, resource_role_id, name, description, per_submission, required, creation_user, creation_date, modification_user, modification_date)
  VALUES(4, 4, 4, 'Review Scorecard', 'Review Scorecard', 'T', 'T', 'System', CURRENT, 'System', CURRENT);
INSERT INTO deliverable_lu(deliverable_id, phase_type_id, resource_role_id, name, description, per_submission, required, creation_user, creation_date, modification_user, modification_date)
  VALUES(5, 4, 5, 'Review Scorecard', 'Review Scorecard', 'T', 'T', 'System', CURRENT, 'System', CURRENT);
INSERT INTO deliverable_lu(deliverable_id, phase_type_id, resource_role_id, name, description, per_submission, required, creation_user, creation_date, modification_user, modification_date)
  VALUES(6, 4, 6, 'Review Scorecard', 'Review Scorecard', 'T', 'T', 'System', CURRENT, 'System', CURRENT);
INSERT INTO deliverable_lu(deliverable_id, phase_type_id, resource_role_id, name, description, per_submission, required, creation_user, creation_date, modification_user, modification_date)
  VALUES(7, 4, 7, 'Review Scorecard', 'Review Scorecard', 'T', 'T', 'System', CURRENT, 'System', CURRENT);
INSERT INTO deliverable_lu(deliverable_id, phase_type_id, resource_role_id, name, description, per_submission, required, creation_user, creation_date, modification_user, modification_date)
  VALUES(8, 4, 5, 'Accuracy Test Cases', 'Accuracy Test Cases', 'F', 'T', 'System', CURRENT, 'System', CURRENT);
INSERT INTO deliverable_lu(deliverable_id, phase_type_id, resource_role_id, name, description, per_submission, required, creation_user, creation_date, modification_user, modification_date)
  VALUES(9, 4, 6, 'Failure Test Cases', 'Failure Test Cases', 'F', 'T', 'System', CURRENT, 'System', CURRENT);
INSERT INTO deliverable_lu(deliverable_id, phase_type_id, resource_role_id, name, description, per_submission, required, creation_user, creation_date, modification_user, modification_date)
  VALUES(10, 4, 7, 'Stress Test Cases', 'Stress Test Cases', 'F', 'T', 'System', CURRENT, 'System', CURRENT);
INSERT INTO deliverable_lu(deliverable_id, phase_type_id, resource_role_id, name, description, per_submission, required, creation_user, creation_date, modification_user, modification_date)
  VALUES(11, 6, 4, 'Appeal Responses', 'Appeal Responses', 'F', 'T', 'System', CURRENT, 'System', CURRENT);
INSERT INTO deliverable_lu(deliverable_id, phase_type_id, resource_role_id, name, description, per_submission, required, creation_user, creation_date, modification_user, modification_date)
  VALUES(12, 6, 5, 'Appeal Responses', 'Appeal Responses', 'F', 'T', 'System', CURRENT, 'System', CURRENT);
INSERT INTO deliverable_lu(deliverable_id, phase_type_id, resource_role_id, name, description, per_submission, required, creation_user, creation_date, modification_user, modification_date)
  VALUES(13, 6, 6, 'Appeal Responses', 'Appeal Responses', 'F', 'T', 'System', CURRENT, 'System', CURRENT);
INSERT INTO deliverable_lu(deliverable_id, phase_type_id, resource_role_id, name, description, per_submission, required, creation_user, creation_date, modification_user, modification_date)
  VALUES(14, 6, 7, 'Appeal Responses', 'Appeal Responses', 'F', 'T', 'System', CURRENT, 'System', CURRENT);
INSERT INTO deliverable_lu(deliverable_id, phase_type_id, resource_role_id, name, description, per_submission, required, creation_user, creation_date, modification_user, modification_date)
  VALUES(15, 7, 8, 'Aggregation', 'Aggregation', 'F', 'T', 'System', CURRENT, 'System', CURRENT);
INSERT INTO deliverable_lu(deliverable_id, phase_type_id, resource_role_id, name, description, per_submission, required, creation_user, creation_date, modification_user, modification_date)
  VALUES(16, 8, 4, 'Aggregation Review', 'Aggregation Review', 'F', 'T', 'System', CURRENT, 'System', CURRENT);
INSERT INTO deliverable_lu(deliverable_id, phase_type_id, resource_role_id, name, description, per_submission, required, creation_user, creation_date, modification_user, modification_date)
  VALUES(17, 8, 5, 'Aggregation Review', 'Aggregation Review', 'F', 'T', 'System', CURRENT, 'System', CURRENT);
INSERT INTO deliverable_lu(deliverable_id, phase_type_id, resource_role_id, name, description, per_submission, required, creation_user, creation_date, modification_user, modification_date)
  VALUES(18, 8, 6, 'Aggregation Review', 'Aggregation Review', 'F', 'T', 'System', CURRENT, 'System', CURRENT);
INSERT INTO deliverable_lu(deliverable_id, phase_type_id, resource_role_id, name, description, per_submission, required, creation_user, creation_date, modification_user, modification_date)
  VALUES(19, 8, 7, 'Aggregation Review', 'Aggregation Review', 'F', 'T', 'System', CURRENT, 'System', CURRENT);
INSERT INTO deliverable_lu(deliverable_id, phase_type_id, resource_role_id, name, description, per_submission, required, creation_user, creation_date, modification_user, modification_date)
  VALUES(20, 9, 1, 'Final Fix', 'Final Fix', 'F', 'T', 'System', CURRENT, 'System', CURRENT);
INSERT INTO deliverable_lu(deliverable_id, phase_type_id, resource_role_id, name, description, per_submission, required, creation_user, creation_date, modification_user, modification_date)
  VALUES(21, 9, 1, 'Scorecard Comment', 'F', 'T', 'Scorecard Comment', 'System', CURRENT, 'System', CURRENT);
INSERT INTO deliverable_lu(deliverable_id, phase_type_id, resource_role_id, name, description, per_submission, required, creation_user, creation_date, modification_user, modification_date)
  VALUES(22, 10, 9, 'Final Review', 'F', 'T', 'Final Review', 'System', CURRENT, 'System', CURRENT);
INSERT INTO deliverable_lu(deliverable_id, phase_type_id, resource_role_id, name, description, per_submission, required, creation_user, creation_date, modification_user, modification_date)
  VALUES(23, 11, 10, 'Approval', 'Approval', 'T', 'T', 'System', CURRENT, 'System', CURRENT);

INSERT INTO notification_type_lu(notification_type_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(1, 'Timeline Notification', 'Timeline Notification', 'System', CURRENT, 'System', CURRENT);

INSERT INTO screening_status_lu(screening_status_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(1, 'Pending', 'Pending', 'System', CURRENT, 'System', CURRENT);
INSERT INTO screening_status_lu(screening_status_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(2, 'Screening', 'Screening', 'System', CURRENT, 'System', CURRENT);
INSERT INTO screening_status_lu(screening_status_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(3, 'Failed', 'Failed', 'System', CURRENT, 'System', CURRENT);
INSERT INTO screening_status_lu(screening_status_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(4, 'Passed', 'Passed', 'System', CURRENT, 'System', CURRENT);
INSERT INTO screening_status_lu(screening_status_id, name, description, creation_user, creation_date, modification_user, modification_date)
  VALUES(5, 'Passed with Warning', 'Passed with Warning', 'System', CURRENT, 'System', CURRENT);
