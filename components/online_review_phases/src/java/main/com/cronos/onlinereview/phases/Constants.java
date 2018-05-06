package com.cronos.onlinereview.phases;

/**
 * <p>
 * A helper class used for storing constant values.
 * </p>
 * @author VolodymyrK, Vovka
 * @version 1.8.6
 */
final class Constants {

    // ------------------------ Project Status Names -----
    static final String PROJECT_STATUS_COMPLETED = "Completed";

    // ------------------------ Project Type Names -----
    static final String PROJECT_TYPE_STUDIO = "Studio";

    // ------------------------ Project Category Names -----
    static final String PROJECT_CATEGORY_COPILOT_POSTING = "Copilot Posting";

    // ------------------------ Phase Type Names -----
    static final String PHASE_SPECIFICATION_SUBMISSION = "Specification Submission";
    static final String PHASE_SPECIFICATION_REVIEW = "Specification Review";
    static final String PHASE_REGISTRATION = "Registration";
    static final String PHASE_SUBMISSION = "Submission";
    static final String PHASE_CHECKPOINT_SUBMISSION = "Checkpoint Submission";
    static final String PHASE_SCREENING = "Screening";
    static final String PHASE_CHECKPOINT_SCREENING = "Checkpoint Screening";
    static final String PHASE_REVIEW = "Review";
    static final String PHASE_CHECKPOINT_REVIEW = "Checkpoint Review";
    static final String PHASE_APPEALS = "Appeals";
    static final String PHASE_APPEALS_RESPONSE = "Appeals Response";
    static final String PHASE_AGGREGATION = "Aggregation";
    static final String PHASE_FINAL_FIX = "Final Fix";
    static final String PHASE_FINAL_REVIEW = "Final Review";
    static final String PHASE_APPROVAL = "Approval";
    static final String PHASE_POST_MORTEM = "Post-Mortem";
    static final String PHASE_ITERATIVE_REVIEW = "Iterative Review";

    // ------------------------ Phase Status Names -----
    static final String PHASE_STATUS_SCHEDULED = "Scheduled";
    static final String PHASE_STATUS_OPEN = "Open";
    static final String PHASE_STATUS_CLOSED = "Closed";

    // ------------------------ Resource Role Names -----
    static final String ROLE_SUBMITTER = "Submitter";
    static final String ROLE_PRIMARY_SCREENER = "Primary Screener";
    static final String ROLE_REVIEWER = "Reviewer";
    static final String ROLE_ACCURACY_REVIEWER = "Accuracy Reviewer";
    static final String ROLE_FAILURE_REVIEWER = "Failure Reviewer";
    static final String ROLE_STRESS_REVIEWER = "Stress Reviewer";
    static final String ROLE_AGGREGATOR = "Aggregator";
    static final String ROLE_FINAL_REVIEWER = "Final Reviewer";
    static final String ROLE_APPROVER = "Approver";
    static final String ROLE_DESIGNER = "Designer";
    static final String ROLE_OBSERVER = "Observer";
    static final String ROLE_MANAGER = "Manager";
    static final String ROLE_COPILOT = "Copilot";
    static final String ROLE_CLIENT_MANAGER = "Client Manager";
    static final String ROLE_POST_MORTEM_REVIEWER = "Post-Mortem Reviewer";
    static final String ROLE_SPECIFICATION_SUBMITTER = "Specification Submitter";
    static final String ROLE_SPECIFICATION_REVIEWER = "Specification Reviewer";
    static final String ROLE_CHECKPOINT_SCREENER = "Checkpoint Screener";
    static final String ROLE_CHECKPOINT_REVIEWER = "Checkpoint Reviewer";
    static final String ROLE_ITERATIVE_REVIEWER = "Iterative Reviewer";

    // ------------------------ Submission Type Names -----
    static final String SUBMISSION_TYPE_CONTEST_SUBMISSION = "Contest Submission";
    static final String SUBMISSION_TYPE_SPECIFICATION_SUBMISSION = "Specification Submission";
    static final String SUBMISSION_TYPE_CHECKPOINT_SUBMISSION = "Checkpoint Submission";

    // ------------------------ Submission Status Names -----
    static final String SUBMISSION_STATUS_ACTIVE = "Active";
    static final String SUBMISSION_STATUS_FAILED_SCREENING = "Failed Screening";
    static final String SUBMISSION_STATUS_FAILED_REVIEW = "Failed Review";
    static final String SUBMISSION_STATUS_COMPLETED_WITHOUT_WIN = "Completed Without Win";
    static final String SUBMISSION_STATUS_DELETED = "Deleted";
    static final String SUBMISSION_STATUS_FAILED_CHECKPOINT_SCREENING = "Failed Checkpoint Screening";
    static final String SUBMISSION_STATUS_FAILED_CHECKPOINT_REVIEW = "Failed Checkpoint Review";

    // ------------------------ Upload Status Names -----
    static final String UPLOAD_STATUS_ACTIVE = "Active";
    static final String UPLOAD_STATUS_DELETED = "Deleted";

    // ------------------------ Upload Type Names -----
    static final String UPLOAD_TYPE_SUBMISSION = "Submission";
    static final String UPLOAD_TYPE_TEST_CASE = "Test Case";
    static final String UPLOAD_TYPE_FINAL_FIX = "Final Fix";
    static final String UPLOAD_TYPE_REVIEW_DOCUMENT = "Review Document";

    // ------------------------ Review Comment Type Names -----
    static final String COMMENT_TYPE_COMMENT = "Comment";
    static final String COMMENT_TYPE_RECOMMENDED = "Recommended";
    static final String COMMENT_TYPE_REQUIRED = "Required";
    static final String COMMENT_TYPE_APPEAL = "Appeal";
    static final String COMMENT_TYPE_APPEAL_RESPONSE = "Appeal Response";
    static final String COMMENT_TYPE_AGGREGATION_COMMENT = "Aggregation Comment";
    static final String COMMENT_TYPE_SUBMITTER_COMMENT = "Submitter Comment";
    static final String COMMENT_TYPE_FINAL_FIX_COMMENT = "Final Fix Comment";
    static final String COMMENT_TYPE_FINAL_REVIEW_COMMENT = "Final Review Comment";
    static final String COMMENT_TYPE_MANAGER_COMMENT = "Manager Comment";
    static final String COMMENT_TYPE_APPROVAL_REVIEW_COMMENT = "Approval Review Comment";
    static final String COMMENT_TYPE_APPROVAL_REVIEW_COMMENT_OTHER_FIXES = "Approval Review Comment - Other Fixes";
    static final String COMMENT_TYPE_SPECIFICATION_REVIEW_COMMENT = "Specification Review Comment";

    // ------------------------ Review Comment Values -----
    static final String COMMENT_VALUE_APPROVED = "Approved";
    static final String COMMENT_VALUE_ACCEPTED = "Accepted";
    static final String COMMENT_VALUE_REJECTED = "Rejected";
    static final String COMMENT_VALUE_REQUIRED = "Required";

    // ------------------------ Phase Criteria Type Names -----
    static final String PHASE_CRITERIA_SCORECARD_ID = "Scorecard ID";
    static final String PHASE_CRITERIA_REGISTRATION_NUMBER = "Registration Number";
    static final String PHASE_CRITERIA_REVIEWER_NUMBER = "Reviewer Number";

    // ------------------------ Review Type Names -----
    static final String REVIEW_TYPE_PEER = "PEER";

    // ------------------------ Dummy User IDs -----
    static final long USER_ID_COMPONENTS = 22719217;
    static final long USER_ID_APPLICATIONS = 22770213;
    static final long USER_ID_LCSUPPORT = 22873364;

    // ------------------------ Other -----
    static final long HOUR = 3600 * 1000;
}
