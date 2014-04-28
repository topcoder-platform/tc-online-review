/*
 * Copyright (C) 2004 - 2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview;

/**
 * This class contains various constants used throughout the application.
 * <p>
 * This class is thread safe as it contains only immutable data.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class Constants {

    /**
     * This member variable is a string constant that defines the name of the Submitter role.
     * Submitter role is assigned as a project resource when they are registered (through another
     * application).  Submitters are able to upload submission, view their own screening and
     * review after they have been committed, place appeals, approve aggregation and upload
     * final fixes.  If a submitter does not have an active submission at some point (deleted,
     * failed screening, failed review, completed without winning) they are not required to
     * fulfill the responsibilities.
     */
    public static final String SUBMITTER_ROLE_NAME = "Submitter";

    /**
     * This member variable is a string constant that defines the name of the Screener role.
     * Screener role is assigned in the screening phase on a per-submission basis when manual
     * screening is required.  Screeners can perform screening during the screening phase for
     * submission(s) assigned to them.
     *
     * @see #PRIMARY_SCREENER_ROLE_NAME
     */
    public static final String SCREENER_ROLE_NAME = "Screener";

    /**
     * This member variable is a string constant that defines the name of the Primary Screener role.
     * Primary Screener role is assigned in the screening phase when manual screening is required.
     * Primary Screeners can perform screening during the screening phase for all submissions.
     *
     * @see #SCREENER_ROLE_NAME
     */
    public static final String PRIMARY_SCREENER_ROLE_NAME = "Primary Screener";

    /**
     * This member variable is a constant array that holds names of different screener roles.
     *
     * @see #SCREENER_ROLE_NAME
     * @see #PRIMARY_SCREENER_ROLE_NAME
     */
    public static final String[] SCREENER_ROLE_NAMES = new String[] {SCREENER_ROLE_NAME, PRIMARY_SCREENER_ROLE_NAME};

    /**
     * This member variable is a string constant that defines the name of the Reviewer role.
     * Reviewer role is assigned in the review phase.  Reviewer can perform review during the
     * review phase and resolve appeals in the following appeal response phase, perform
     * aggregation review in the following aggregation review phase.
     *
     * @see #ACCURACY_REVIEWER_ROLE_NAME
     * @see #FAILURE_REVIEWER_ROLE_NAME
     * @see #STRESS_REVIEWER_ROLE_NAME
     */
    public static final String REVIEWER_ROLE_NAME = "Reviewer";

    /**
     * This member variable is a string constant that defines the name of the Accuracy Reviewer
     * role.  Accuracy Reviewer role is one of the Test Case Reviewer roles and has the same
     * permission and responsibilities as normal Reviewer role.  In addition they are required
     * to upload their test cases during the review phase.
     *
     * @see #REVIEWER_ROLE_NAME
     * @see #FAILURE_REVIEWER_ROLE_NAME
     * @see #STRESS_REVIEWER_ROLE_NAME
     */
    public static final String ACCURACY_REVIEWER_ROLE_NAME = "Accuracy Reviewer";

    /**
     * This member variable is a string constant that defines the name of the Failure Reviewer
     * role.
     *
     * @see #REVIEWER_ROLE_NAME
     * @see #ACCURACY_REVIEWER_ROLE_NAME
     * @see #STRESS_REVIEWER_ROLE_NAME
     */
    public static final String FAILURE_REVIEWER_ROLE_NAME = "Failure Reviewer";

    /**
     * This member variable is a string constant that defines the name of the Stress Reviewer role.
     *
     * @see #REVIEWER_ROLE_NAME
     * @see #ACCURACY_REVIEWER_ROLE_NAME
     * @see #FAILURE_REVIEWER_ROLE_NAME
     */
    public static final String STRESS_REVIEWER_ROLE_NAME = "Stress Reviewer";

    /**
     * <p>A <code>String</code> providing the name of <code>Specification Reviewer</code> resource role name.</p>
     *
     */
    public static final String SPECIFICATION_REVIEWER_ROLE_NAME = "Specification Reviewer";

    /**
     * This member variable is a constant array that holds names of different reviewer roles.
     *
     * @see #REVIEWER_ROLE_NAME
     * @see #ACCURACY_REVIEWER_ROLE_NAME
     * @see #FAILURE_REVIEWER_ROLE_NAME
     * @see #STRESS_REVIEWER_ROLE_NAME
     */
    public static final String[] REVIEWER_ROLE_NAMES = new String[] {
        REVIEWER_ROLE_NAME, ACCURACY_REVIEWER_ROLE_NAME, FAILURE_REVIEWER_ROLE_NAME, STRESS_REVIEWER_ROLE_NAME};

    /**
     * This member variable is a string constant that defines the name of the Aggregator role.
     * Aggregator role is assigned in the aggregation phase.  Aggregators can perform aggregation
     * during the aggregation phase.
     */
    public static final String AGGREGATOR_ROLE_NAME = "Aggregator";

    /**
     * This member variable is a string constant that defines the name of the Final Reviewer role.
     * Final Reviewer role is assigned in the final review phase. Final reviewers can perform final
     * review during the final review phase.
     */
    public static final String FINAL_REVIEWER_ROLE_NAME = "Final Reviewer";

    /**
     * This member variable is a string constant that defines the name of the Approver role.
     * Approver role is assigned in the approval phase.  Approvers can perform approval during
     * the approval phase.
     */
    public static final String APPROVER_ROLE_NAME = "Approver";

    /**
     * This member variable is a string constant that defines the name of the Post-Mortem Reviewer role.
     * Post-Mortem Reviewer role is assigned in the Post-Mortem phase. Post-Mortem Reviewers can perform review during
     * the Post-Mortem phase.
     */
    public static final String POST_MORTEM_REVIEWER_ROLE_NAME = "Post-Mortem Reviewer";

    /**
     * This member variable is a string constant that defines the name of the Public role.
     */
    public static final String PUBLIC_ROLE_NAME = "Public";

    /**
     * This member variable is a string constant that defines the name of the Designer role.
     * Designer role is assigned as a project resource.  Designers do not have much difference
     * from the Public view.  The purpose is for the designer to view private projects and to
     * receive payments.
     */
//    public static final String DESIGNER_ROLE_NAME = "Designer";

    /**
     * This member variable is a string constant that defines the name of the Observer role.
     * Observer role is assigned as a project resource.  Observer will have read only access
     * to more details than Public view has.  This role will be assigned to customers, so
     * that they can monitor the production process.
     */
    public static final String OBSERVER_ROLE_NAME = "Observer";

    /**
     * This member variable is a string constant that defines the name of the Manager role.
     * Manager role can either be assigned on a project basis or as on the global level.
     * Managers have permission to view and edit anything, including creating new projects.
     */
    public static final String MANAGER_ROLE_NAME = "Manager";

    /**
     * This member variable is a string constant that defines the name of the Global Manager
     * role.  This role is a logical one, i.e. there is no such role defined in the database.
     * This role will be assigned to users that have their own resources with role Manager and
     * no project assigned to that resource.
     */
    public static final String GLOBAL_MANAGER_ROLE_NAME = "Global Manager";

    /**
     * <p>A <code>String</code> providing the name for <code>Cockpit Project User</code> role.</p>
     *
     */
    public static final String COCKPIT_PROJECT_USER_ROLE_NAME = "Cockpit Project User";

    /**
     * This member variable is a string constant that defines the name of the Payment Manager role.
     * Payment Manager role can either be assigned on a project basis or as on the global level.
     * Payment Managers have permission to create payments.
     */
    public static final String PAYMENT_MANAGER_ROLE_NAME = "Payment Manager";

    /**
     * This member variable is a string constant that defines the name of the Global Payment Manager
     * role.  This role is a logical one, i.e. there is no such role defined in the database.
     * This role will be assigned to users that have their own resources with role Payment Manager and
     * no project assigned to that resource.
     */
    public static final String GLOBAL_PAYMENT_MANAGER_ROLE_NAME = "Global Payment Manager";

    /**
     * This member variable is a string constant that defines the name of the Admin role.
     * This role is a logical one, i.e. there is no such role defined in the database.
     */
    public static final String ADMIN_ROLE_NAME = "Admin";

    /**
     * This member variable is a constant array that holds names of different manager roles.
     *
     * @see #MANAGER_ROLE_NAME
     * @see #GLOBAL_MANAGER_ROLE_NAME
     */
    public static final String[] MANAGER_ROLE_NAMES = new String[] {MANAGER_ROLE_NAME, GLOBAL_MANAGER_ROLE_NAME,
        COCKPIT_PROJECT_USER_ROLE_NAME, ADMIN_ROLE_NAME};

    /**
     * <p>This member variable is a string constant that defines the name of the <code>Client Manager</code>
     * role. This role is a logical one, i.e. there is no such role defined in the database. This role will be assigned
     * to users that have their own resources with role <code>Client Manager</code> and project assigned to that
     * resource.</p>
     */
    public static final String CLIENT_MANAGER_ROLE_NAME = "Client Manager";

    /**
     * <p>This member variable is a string constant that defines the name of the <code>Co-Pilot</code> role. This role
     * is a logical one, i.e. there is no such role defined in the database. This role will be assigned to users that
     * have their own resources with role <code>Co-Pilot</code> and project assigned to that resource.</p>
     *
     */
    public static final String COPILOT_ROLE_NAME = "Copilot";

    /**
     * <p>A <code>String</code> providing the name for <code>Iterative Reviewer</code> role.</p>
     */
    public static final String ITERATIVE_REVIEWER_ROLE_NAME = "Iterative Reviewer";

    /**
     * <p>A <code>String</code> providing the name for the <code>Checkpoint Screener</code> resource role.</p>
     *
     */
    public static final String CHECKPOINT_SCREENER_ROLE_NAME = "Checkpoint Screener";

    /**
     * <p>A <code>String</code> providing the name for the <code>Checkpoint Reviewer</code> resource role.</p>
     *
     */
    public static final String CHECKPOINT_REVIEWER_ROLE_NAME = "Checkpoint Reviewer";


    /**
     * <p>This member variable is a constant array that holds names of different roles which are granted access to
     * <code>Project Management Console</code> functionalities.</p>
     *
     * @see #MANAGER_ROLE_NAME
     * @see #CLIENT_MANAGER_ROLE_NAME
     * @see #COPILOT_ROLE_NAME
     */
//    public static final String[] PROJECT_MANAGEMENT_CONSOLE_ROLE_NAMES
//        = new String[] {MANAGER_ROLE_NAME, CLIENT_MANAGER_ROLE_NAME, COPILOT_ROLE_NAME};

    // --------------------------------------------------------------------- Permission names -----

    /**
     * This member variable is a string constant that defines the name of the Create Project
     * permission.
     */
    public static final String CREATE_PROJECT_PERM_NAME = "Create Project";

    /**
     * This member variable is a string constant that defines the name of the Edit Project Details
     * permission.
     */
    public static final String EDIT_PROJECT_DETAILS_PERM_NAME = "Edit Project Details";

    /**
     * This member variable is a string constant that defines the name of the
     * Set Timeline Notifications permission.
     */
    public static final String SET_TL_NOTIFY_PERM_NAME = "Set Timeline Notifications";

    /**
     * This member variable is a string constant that defines the name of the View Projects
     * permission.
     */
    public static final String VIEW_PROJECTS_PERM_NAME = "View Projects";

    /**
     * This member variable is a string constant that defines the name of the View My Projects
     * permission.
     */
    public static final String VIEW_MY_PROJECTS_PERM_NAME = "View My Projects";

    /**
     * This member variable is a string constant that defines the name of the View Projects Draft
     * permission.
     */
    public static final String VIEW_PROJECTS_DRAFT_PERM_NAME = "View Projects Draft";

    /**
     * This member variable is a string constant that defines the name of the View Project Detail
     * permission.
     */
    public static final String VIEW_PROJECT_DETAIL_PERM_NAME = "View Project Detail";

    /**
     * This member variable is a string constant that defines the name of the View Project Resources
     * permission.
     */
    public static final String VIEW_PROJECT_RESOURCES_PERM_NAME = "View Project Resources";

    /**
     * This member variable is a string constant that defines the name of the View SVN Link
     * permission.
     */
    public static final String VIEW_SVN_LINK_PERM_NAME = "View SVN Link";

    /**
     * This member variable is a string constant that defines the name of the View Autopilot Status
     * permission.
     */
    public static final String VIEW_AUTOPILOT_STATUS_PERM_NAME = "View Autopilot Status";

    /**
     * This member variable is a string constant that defines the name of the
     * View All Payment Information permission.
     */
    public static final String VIEW_ALL_PAYMENT_INFO_PERM_NAME = "View All Payment Information";

    /**
     * This member variable is a string constant that defines the name of the
     * View My Payment Information permission.
     */
    public static final String VIEW_MY_PAY_INFO_PERM_NAME = "View My Payment Information";

    /**
     * This member variable is a string constant that defines the name of the
     * Contact Project Managers permission.
     */
    public static final String CONTACT_PM_PERM_NAME = "Contact Project Managers";

    /**
     * This member variable is a string constant that defines the name of the View Registrations
     * permission.
     */
    public static final String VIEW_REGISTRATIONS_PERM_NAME = "View Registrations";

    /**
     * This member variable is a string constant that defines the name of the Perform Contest Submission
     * permission.
     */
    public static final String PERFORM_SUBM_PERM_NAME = "Perform Contest Submission";

    /**
     * <p>A <code>String</code> providing the name for <code>Perform Specification Submission</code> permission.</p>
     *
     */
    public static final String PERFORM_SPECIFICATION_SUBMISSION_PERM_NAME = "Perform Specification Submission";

    /**
     * <p>A <code>String</code> providing the name for <code>Perform Checkpoint Submission</code> permission.</p>
     *
     */
    public static final String PERFORM_CHECKPOINT_SUBMISSION_PERM_NAME = "Perform Checkpoint Submission";

    /**
     * <p>A <code>String</code> providing the name for <code>Perform Specification Review</code> permission.</p>
     *
     */
    public static final String PERFORM_SPECIFICATION_REVIEW_PERM_NAME = "Perform Specification Review";

    /**
     * This member variable is a string constant that defines the name of the View All Submissions
     * permission.
     */
    public static final String VIEW_ALL_SUBM_PERM_NAME = "View All Submissions";

    /**
     * <p>A <code>String</code> providing the name for <code>View All Specification Submissions</code> permission.</p>
     *
     */
    public static final String VIEW_ALL_SPECIFICATION_SUBMISSIONS_PERM_NAME = "View All Specification Submissions";

    /**
     * <p>A <code>String</code> providing the name for <code>View All Checkpoint Submissions</code> permission.</p>
     *
     */
    public static final String VIEW_ALL_CHECKPOINT_SUBMISSIONS_PERM_NAME = "View All Checkpoint Submissions";

    /**
     * This member variable is a string constant that defines the name of the View My Submissions
     * permission.
     */
    public static final String VIEW_MY_SUBM_PERM_NAME = "View My Submissions";

    /**
     * <p>A <code>String</code> providing the name for <code>View My Specification Submissions</code> permission.</p>
     *
     */
    public static final String VIEW_MY_SPECIFICATION_SUBMISSIONS_PERM_NAME = "View My Specification Submissions";

    /**
     * <p>A <code>String</code> providing the name for <code>View My Checkpoint Submissions</code> permission.</p>
     *
     */
    public static final String VIEW_MY_CHECKPOINT_SUBMISSIONS_PERM_NAME = "View My Checkpoint Submissions";

    /**
     * This member variable is a string constant that defines the name of the
     * View Screener Submission permission.
     */
    public static final String VIEW_SCREENER_SUBM_PERM_NAME = "View Screener Submission";

    /**
     * <p>A <code>String</code> providing the name for <code>View Screener Checkpoint Submission</code> permission.</p>
     *
     */
    public static final String VIEW_SCREENER_CHECKPOINT_SUBMISSION_PERM_NAME = "View Screener Checkpoint Submission";

    /**
     * This member variable is a string constant that defines the name of the
     * View Most Recent Submissions permission.
     */
    public static final String VIEW_RECENT_SUBM_PERM_NAME = "View Most Recent Submissions";

    /**
     * <p>A <code>String</code> providing the name for <code>View Current Iterative Review Submission</code>
     * permission.</p>
     */
    public static final String VIEW_CURRENT_ITERATIVE_REVIEW_SUBMISSION = "View Current Iterative Review Submission";

    /**
     * <p>A <code>String</code> providing the name for <code>VIEW_RECENT_SPECIFICATION_SUBMISSIONS_PERM_NAME</code>
     * permission.</p>
     */
    public static final String VIEW_RECENT_SPECIFICATION_SUBMISSIONS_PERM_NAME
        = "View Most Recent Specification Submissions";

    /**
     * <p>A <code>String</code> providing the name for <code>View Most Recent Checkpoint Submissions</code> permission.
     * </p>
     */
    public static final String VIEW_RECENT_CHECKPOINT_SUBMISSIONS_PERM_NAME
        = "View Most Recent Checkpoint Submissions";

    /**
     * <p>A <code>String</code> providing the name for <code>View Most Recent Checkpoint Submissions After Review</code>
     * permission.</p>
     */
    public static final String VIEW_RECENT_CHECKPOINT_SUBMISSIONS_AFTER_REVIEW_PERM_NAME
        = "View Most Recent Checkpoint Submissions After Review";

    /**
     * This member variable is a string constant that defines the name of the
     * View Winning Submission permission.
     */
    public static final String VIEW_WINNING_SUBM_PERM_NAME = "View Winning Submission";

    /**
     * <p>A <code>String</code> providing the name for <code>View Winning Checkpoint Submission</code> permission.</p>
     *
     */
    public static final String VIEW_WINNING_CHECKPOINT_SUBMISSION_PERM_NAME = "View Winning Checkpoint Submission";

    /**
     * This member variable is a string constant that defines the name of the
     * Download Custom Submission permission.
     */
    public static final String DOWNLOAD_CUSTOM_SUBM_PERM_NAME = "Download Custom Submission";

    /**
     * <p>A <code>String</code> providing the name for <code>Download Custom Checkpoint Submission</code>
     *  permission.</p>
     *
     */
    public static final String DOWNLOAD_CUSTOM_CHECKPOINT_SUBMISSION_PERM_NAME = "Download Custom Checkpoint Submission";

    /**
     * This member variable is a string constant that defines the name of the
     * View Most Recent after Appeals Response permission.
     */
    public static final String VIEW_RECENT_SUBM_AAR_PERM_NAME = "View Most Recent after Appeals Response";

    /**
     * This member variable is a string constant that defines the name of the Remove Submission
     * permission.
     */
    public static final String REMOVE_SUBM_PERM_NAME = "Remove Submission";

    /**
     * This member variable is a string constant that defines the name of the Perform Screening
     * permission.
     */
    public static final String PERFORM_SCREENING_PERM_NAME = "Perform Screening";

    /**
     * <p>A <code>String</code> providing the name for <code>Perform Checkpoint Screening</code> permission.</p>
     *
     */
    public static final String PERFORM_CHECKPOINT_SCREENING_PERM_NAME = "Perform Checkpoint Screening";

    /**
     * This member variable is a string constant that defines the name of the View Screening
     * permission.
     */
    public static final String VIEW_SCREENING_PERM_NAME = "View Screening";

    /**
     * <p>A <code>String</code> providing the name for <code>View Checkpoint Screening</code> permission.</p>
     *
     */
    public static final String VIEW_CHECKPOINT_SCREENING_PERM_NAME = "View Checkpoint Screening";

    /**
     * <p>A <code>String</code> providing the name for <code>View Checkpoint Review</code> permission.</p>
     *
     */
    public static final String VIEW_CHECKPOINT_REVIEW_PERM_NAME = "View Checkpoint Review";

    /**
     * This member variable is a string constant that defines the name of the Perform Review
     * permission.
     */
    public static final String PERFORM_REVIEW_PERM_NAME = "Perform Review";

    /**
     * <p>A <code>String</code> providing the name for <code>View Checkpoint Review</code> permission.</p>
     *
     */
    public static final String PERFORM_CHECKPOINT_REVIEW_PERM_NAME = "Perform Checkpoint Review";

    /**
     * This member variable is a string constant that defines the name of the Upload Test Cases
     * permission.
     */
    public static final String UPLOAD_TEST_CASES_PERM_NAME = "Upload Test Cases";

    /**
     * This member variable is a string constant that defines the name of the Download Test Cases
     * permission.
     */
    public static final String DOWNLOAD_TEST_CASES_PERM_NAME = "Download Test Cases";

    /**
     * This member variable is a string constant that defines the name of the
     * Download Test Cases during Review permission.
     */
    public static final String DOWNLOAD_TC_DUR_REVIEW_PERM_NAME = "Download Test Cases during Review";

    /**
     * This member variable is a string constant that defines the name of the View All Reviews
     * permission.
     */
    public static final String VIEW_ALL_REVIEWS_PERM_NAME = "View All Reviews";

    /**
     * This member variable is a string constant that defines the name of the View Reviewer Reviews
     * permission.
     */
    public static final String VIEW_REVIEWER_REVIEWS_PERM_NAME = "View Reviewer Reviews";

    /**
     * This member variable is a string constant that defines the name of the View Iterative Reviewer Reviews
     * permission.
     */
    public static final String VIEW_ITERATIVE_REVIEWER_REVIEWS_PERM_NAME = "View Iterative Reviewer Reviews";

    /**
     * This member variable is a string constant that defines the name of the
     * View Composite Scorecard permission.
     */
    public static final String VIEW_COMPOS_SCORECARD_PERM_NAME = "View Composite Scorecard";

    /**
     * This member variable is a string constant that defines the name of the
     * Edit My Review during Review permission.
     */
    public static final String EDIT_MY_REVIEW_PERM_NAME = "Edit My Review during Review";

    /**
     * This member variable is a string constant that defines the name of the Perform Appeal
     * permission.
     */
    public static final String PERFORM_APPEAL_PERM_NAME = "Perform Appeal";

    /**
     * This member variable is a string constant that defines the name of the View Appeals
     * permission.
     */
    public static final String VIEW_APPEALS_PERM_NAME = "View Appeals";

    /**
     * This member variable is a string constant that defines the name of the
     * Perform Appeals Response permission.
     */
    public static final String PERFORM_APPEAL_RESP_PERM_NAME = "Perform Appeals Response";

    /**
     * This member variable is a string constant that defines the name of the View Appeal Responses
     * permission.
     */
    public static final String VIEW_APPEAL_RESP_PERM_NAME = "View Appeal Responses";

    /**
     * This member variable is a string constant that defines the name of the
     * Edit My Appeal Response during Appeals Response permission.
     */
    public static final String EDIT_MY_APPEAL_RESP_PERM_NAME =
            "Edit My Appeal Response during Appeals Response";

    /**
     * This member variable is a string constant that defines the name of the Perform Aggregation
     * permission.
     */
    public static final String PERFORM_AGGREGATION_PERM_NAME = "Perform Aggregation";

    /**
     * This member variable is a string constant that defines the name of the View Aggregation
     * permission.
     */
    public static final String VIEW_AGGREGATION_PERM_NAME = "View Aggregation";

    /**
     * This member variable is a string constant that defines the name of the
     * Perform Aggregation Review permission.
     */
    public static final String PERFORM_AGGREG_REVIEW_PERM_NAME = "Perform Aggregation Review";

    /**
     * This member variable is a string constant that defines the name of the
     * View Aggregation Review permission.
     */
    public static final String VIEW_AGGREG_REVIEW_PERM_NAME = "View Aggregation Review";

    /**
     * This member variable is a string constant that defines the name of the Perform Final Fix
     * permission.
     */
    public static final String PERFORM_FINAL_FIX_PERM_NAME = "Perform Final Fix";

    /**
     * This member variable is a string constant that defines the name of the Download Final Fix
     * permission.
     */
    public static final String DOWNLOAD_FINAL_FIX_PERM_NAME = "Download Final Fix";

    /**
     * This member variable is a string constant that defines the name of the Perform Final Review
     * permission.
     */
    public static final String PERFORM_FINAL_REVIEW_PERM_NAME = "Perform Final Review";

    /**
     * This member variable is a string constant that defines the name of the View Final Review
     * permission.
     */
    public static final String VIEW_FINAL_REVIEW_PERM_NAME = "View Final Review";

    /**
     * This member variable is a string constant that defines the name of the
     * Submit Scorecard Comment permission.
     */
    public static final String SUBMIT_SCORECARD_COMM_PERM_NAME = "Submit Scorecard Comment";

    /**
     * This member variable is a string constant that defines the name of the Perform Approval
     * permission.
     */
    public static final String PERFORM_APPROVAL_PERM_NAME = "Perform Approval";

    /**
     * This member variable is a string constant that defines the name of the View Approval
     * permission.
     */
    public static final String VIEW_APPROVAL_PERM_NAME = "View Approval";

    /**
     * <p>A <code>String</code> providing the name for <code>View Specification Review</code> permission.</p>
     *
     */
    public static final String VIEW_SPECIFICATION_REVIEW_PERM_NAME = "View Specification Review";

    /**
     * This member variable is a string constant that defines the name of the Perform Post-Mortem Review
     * permission.
     *
     */
    public static final String PERFORM_POST_MORTEM_REVIEW_PERM_NAME = "Perform Post-Mortem Review";

    /**
     * This member variable is a string constant that defines the name of the View Post-Mortem
     * permission.
     *
     */
    public static final String VIEW_POST_MORTEM_PERM_NAME = "View Post-Mortem Review";

    /**
     * This member variable is a string constant that defines the name of the Edit Any Scorecard
     * permission.
     */
    public static final String EDIT_ANY_SCORECARD_PERM_NAME = "Edit Any Scorecard";

    /**
     * This member variable is a string constant that defines the name of the
     * Download Review Document permission.
     */
    public static final String DOWNLOAD_DOCUMENT_PERM_NAME = "Download Review Document";

    /**
     * This member variable is a string constant that defines the name of the
     * View Payments permission.
     *
     */
    public static final String VIEW_PAYMENTS_PERM_NAME = "View Payments";

    /**
     * This member variable is a string constant that defines the name of the
     * Edit Payment permission.
     *
     */
    public static final String EDIT_PAYMENTS_PERM_NAME = "Edit Payments";

    /**
     * <p>This member variable is a string constant that defines the name of the <code>View Project Management Console
     * </code>  permission. Such a permission grants access to <code>Project Management Console</code> view.</p>
     *
     */
    public static final String VIEW_PROJECT_MANAGEMENT_CONSOLE_PERM_NAME = "View Project Management Console";

    /**
     * <p>A <code>String</code> providing the name for permission for viewing the <code>Cockpit</code> project
     * name.</p>
     *
     */
    public static final String VIEW_COCKPIT_PROJECT_NAME_PERM_NAME = "View Cockpit Project Name";

    /**
     * <p>This member variable is a string constant that defines the name of the <code>Project Management</code>
     * permission. Such a permission grants access to submitting form from <code>Project Management Console</code> view.
     * </p>
     */
    public static final String PROJECT_MANAGEMENT_PERM_NAME = "Manage Project";

    /**
     * <p>A <code>String</code> providing the <code>View Late Deliverable</code> permission.</p>
     *
     */
    public static final String VIEW_LATE_DELIVERABLE_PERM_NAME = "View Late Deliverable";

    /**
     * <p>A <code>String</code> providing the <code>Edit Late Deliverable</code> permission.</p>
     *
     */
    public static final String EDIT_LATE_DELIVERABLE_PERM_NAME = "Edit Late Deliverable";

    /**
     * <p>A <code>String</code> providing the <code>Advance Submissions That Failed Screening</code> permission.</p>
     *
     */
    public static final String ADVANCE_SUBMISSION_FAILED_SCREENING_PERM_NAME = "Advance Submissions That Failed Screening";

    /**
     * <p>A <code>String</code> providing the <code>Reopen Scorecard</code> permission.</p>
     *
     */
    public static final String REOPEN_REVIEW_SCORECARD_PERM_NAME = "Reopen Scorecard";

    /**
     * This member variable is a string constant that defines the name of the View Iterative Review permission.
     */
    public static final String VIEW_ITERATIVE_REVIEW_PERM_NAME = "View Iterative Review";

    /**
     * This member variable is a string constant that defines the name of the Perform Iterative Review permission.
     */
    public static final String PERFORM_ITERATIVE_REVIEW_PERM_NAME = "Perform Iterative Review";

    // -------------------------------------------------------------------------- Phase names -----

    /**
     * This member variable is a string constant that defines the name of the Registration phase.
     */
    public static final String REGISTRATION_PHASE_NAME = "Registration";

    /**
     * This member variable is a string constant that defines the name of the Submission phase.
     */
    public static final String SUBMISSION_PHASE_NAME = "Submission";

    /**
     * This member variable is a string constant that defines the name of the Screening phase.
     */
    public static final String SCREENING_PHASE_NAME = "Screening";

    /**
     * This member variable is a string constant that defines the name of the Review phase.
     */
    public static final String REVIEW_PHASE_NAME = "Review";

    /**
     * This member variable is a string constant that defines the name of the Appeals phase.
     */
    public static final String APPEALS_PHASE_NAME = "Appeals";

    /**
     * This member variable is a string constant that defines the name of the Appeals Response
     * phase.
     */
    public static final String APPEALS_RESPONSE_PHASE_NAME = "Appeals Response";

    /**
     * This member variable is a string constant that defines the name of the Aggregation phase.
     */
    public static final String AGGREGATION_PHASE_NAME = "Aggregation";

    /**
     * This member variable is a string constant that defines the name of the Aggregation Review
     * phase.
     */
    public static final String AGGREGATION_REVIEW_PHASE_NAME = "Aggregation Review";

    /**
     * This member variable is a string constant that defines the name of the Final Fix phase.
     */
    public static final String FINAL_FIX_PHASE_NAME = "Final Fix";

    /**
     * This member variable is a string constant that defines the name of the Final Review phase.
     */
    public static final String FINAL_REVIEW_PHASE_NAME = "Final Review";

    /**
     * This member variable is a string constant that defines the name of the Approval phase.
     */
    public static final String APPROVAL_PHASE_NAME = "Approval";

    /**
     * This member variable is a string constant that defines the name of the Post-Mortem phase.
     */
    public static final String POST_MORTEM_PHASE_NAME = "Post-Mortem";

    /**
     * <p>A <code>String</code> providing the name for <code>Specification Submission</code> phase.</p>
     */
    public static final String SPECIFICATION_SUBMISSION_PHASE_NAME = "Specification Submission";

    /**
     * <p>A <code>String</code> providing the name for <code>Specification Review</code> phase.</p>
     */
    public static final String SPECIFICATION_REVIEW_PHASE_NAME = "Specification Review";

    /**
     * <p>A <code>String</code> providing the name for <code>Checkpoint Submission</code> phase.</p>
     */
    public static final String CHECKPOINT_SUBMISSION_PHASE_NAME = "Checkpoint Submission";

    /**
     * <p>A <code>String</code> providing the name for <code>Checkpoint Screening</code> phase.</p>
     */
    public static final String CHECKPOINT_SCREENING_PHASE_NAME = "Checkpoint Screening";

    /**
     * <p>A <code>String</code> providing the name for <code>Checkpoint Review</code> phase.</p>
     *
     */
    public static final String CHECKPOINT_REVIEW_PHASE_NAME = "Checkpoint Review";

    /**
     * <p>A <code>String</code> providing the name for <code>Iterative Review</code> phase.</p>
     */
    public static final String ITERATIVE_REVIEW_PHASE_NAME = "Iterative Review";


    // ------------------------------------------------------------------- Phase Status names -----

    /**
     * This member variable is a string constant that defines the name of Scheduled phase status.
     */
    public static final String SCHEDULED_PH_STATUS_NAME = "Scheduled";

    /**
     * This member variable is a string constant that defines the name of Open phase status.
     */
    public static final String OPEN_PH_STATUS_NAME = "Open";

    /**
     * This member variable is a string constant that defines the name of Closed phase status.
     */
    public static final String CLOSED_PH_STATUS_NAME = "Closed";


    // ---------------------------------------------------- Application's Functionality names -----

    /**
     * Represents the application function name of view registrants.
     */
    public static final String VIEW_REGISTRANTS_APP_FUNC = "VIEW_REGISTRANTS";

    /**
     * Represents the application function name of view submission.
     */
    public static final String VIEW_SUBMISSIONS_APP_FUNC = "VIEW_SUBMISSIONS";

    /**
     * Represents the application function name of view reviews.
     */
    public static final String VIEW_REVIEWS_APP_FUNC = "VIEW_REVIEWS";

    /**
     * Represents the application function name of aggregation.
     */
    public static final String AGGREGATION_APP_FUNC = "AGGREGATION";

    /**
     * Represents the application function name of final fix.
     */
    public static final String FINAL_FIX_APP_FUNC = "FINAL_FIX";

    /**
     * Represents the application function name of approval.
     */
    public static final String APPROVAL_APP_FUNC = "APPROVAL";

    /**
     * Name of application function for Post Mortem phase.
     */
    public static final String POST_MORTEM_APP_FUNC = "POSTMORTEM";

    /**
     * <p>A <code>String</code> providing the logical name for phase group for <code>Specification Review</code>.</p>
     */
    public static final String SPEC_REVIEW_APP_FUNC = "SPEC_REVIEW";

    /**
     * <p>A <code>String</code> providing the logical name for phase group for <code>Checkpoint</code> phases.</p>
     */
    public static final String CHECKPOINT_APP_FUNC = "CHECKPOINT";

    /**
     * <p>A <code>String</code> providing the logical name for phase group for <code>Iterative Review</code> phases.</p>
     */
    public static final String ITERATIVEREVIEW_APP_FUNC = "ITERATIVEREVIEW";

    // -------------------------------------------------------------------- Deliverable names -----

    /**
     * This member variable is a string constant that defines the name of the Submission
     * deliverable.
     */
    public static final String SUBMISSION_DELIVERABLE_NAME = "Submission";

    /**
     * <p>A <code>String</code> providing the name for <code>Specification Submission</code> deliverable.</p>
     */
    public static final String SPECIFICATION_SUBMISSION_DELIVERABLE_NAME = "Specification Submission";

    /**
     * <p>A <code>String</code> providing the name for <code>Checkpoint Submission</code> deliverable.</p>

     */
    public static final String CHECKPOINT_SUBMISSION_DELIVERABLE_NAME = "Checkpoint Submission";

    /**
     * <p>A <code>String</code> providing the name for <code>Checkpoint Screening Scorecard</code> deliverable.</p>

     */
    public static final String CHECKPOINT_SCREENING_DELIVERABLE_NAME = "Checkpoint Screening Scorecard";

    /**
     * <p>A <code>String</code> providing the name for <code>Checkpoint Review Scorecard</code> deliverable.</p>

     */
    public static final String CHECKPOINT_REVIEW_DELIVERABLE_NAME = "Checkpoint Review Scorecard";

    /**
     * <p>A <code>String</code> providing the name for <code>Specification Review</code> deliverable.</p>

     */
    public static final String SPECIFICATION_REVIEW_DELIVERABLE_NAME = "Specification Review";

    /**
     * This member variable is a string constant that defines the name of the Screening Scorecard
     * deliverable.
     */
    public static final String SCREENING_DELIVERABLE_NAME = "Screening Scorecard";

    /**
     * This member variable is a string constant that defines the name of the Primary Screening Scorecard
     * deliverable.
     */
    public static final String PRIMARY_SCREENING_DELIVERABLE_NAME = "Primary Screening Scorecard";

    /**
     * This member variable is a string constant that defines the name of the Review Scorecard
     * deliverable.
     */
    public static final String REVIEW_DELIVERABLE_NAME = "Review Scorecard";

    /**
     * This member variable is a string constant that defines the name of the Accuracy Test Cases
     * deliverable.
     */
    public static final String ACC_TEST_CASES_DELIVERABLE_NAME = "Accuracy Test Cases";

    /**
     * This member variable is a string constant that defines the name of the Failure Test Cases
     * deliverable.
     */
    public static final String FAIL_TEST_CASES_DELIVERABLE_NAME = "Failure Test Cases";

    /**
     * This member variable is a string constant that defines the name of the Stress Test Cases
     * deliverable.
     */
    public static final String STRS_TEST_CASES_DELIVERABLE_NAME = "Stress Test Cases";

    /**
     * This member variable is a string constant that defines the name of the Appeal Responses
     * deliverable.
     */
    public static final String APPEAL_RESP_DELIVERABLE_NAME = "Appeal Responses";

    /**
     * This member variable is a string constant that defines the name of the Aggregation
     * deliverable.
     */
    public static final String AGGREGATION_DELIVERABLE_NAME = "Aggregation";

    /**
     * This member variable is a string constant that defines the name of the Aggregation Review
     * deliverable.
     */
    public static final String AGGREGATION_REV_DELIVERABLE_NAME = "Aggregation Review";

    /**
     * This member variable is a string constant that defines the name of the Final Fix deliverable.
     */
    public static final String FINAL_FIX_DELIVERABLE_NAME = "Final Fix";

    /**
     * This member variable is a string constant that defines the name of the Scorecard Comment
     * deliverable.
     */
    public static final String SCORECARD_COMM_DELIVERABLE_NAME = "Scorecard Comment";

    /**
     * This member variable is a string constant that defines the name of the Final Review
     * deliverable.
     */
    public static final String FINAL_REVIEW_DELIVERABLE_NAME = "Final Review";

    /**
     * This member variable is a string constant that defines the name of the Approval deliverable.
     */
    public static final String APPROVAL_DELIVERABLE_NAME = "Approval";

    /**
     * This member variable is a string constant that defines the name of the Post-Mortem deliverable.
     */
    public static final String POST_MORTEM_DELIVERABLE_NAME = "Post-Mortem Review";

    /**
     * This member variable is a string constant that defines the name of the Iterative Review Scorecard
     * deliverable.
     */
    public static final String ITERATIVE_REVIEW_DELIVERABLE_NAME = "Iterative Review Scorecard";

    // ----------------------------------------------------------------- Action Forward names -----

    /**
     * This member variable is a string constant that defines the name of the Not Authorized global
     * action forward.
     */
    public static final String NOT_AUTHORIZED_FORWARD_NAME = "notAuthorized";

    /**
     * This member variable is a string constant that defines the name of the User Error global
     * action forward.
     */
    public static final String USER_ERROR_FORWARD_NAME = "userError";

    /**
     * This member variable is a string constant that defines the name of the Review Committed
     * global action forward.
     */
    public static final String REVIEW_COMMITTD_FORWARD_NAME = "reviewCommitted";

    /**
     * This member variable is a string constant that defines the name of the Success commonly used
     * action forward.
     */
    public static final String SUCCESS_FORWARD_NAME = "success";

    /**
     * This member variable is a string constant that defines the name of the Preview commonly used
     * action forward.
     */
    public static final String PREVIEW_FORWARD_NAME = "preview";

    /**
     * This member variable is a string constant that defines the name of the Display Page commonly
     * used action forward. This action forward is often used to initially display a page with a
     * form that a user should fill out.
     */
    public static final String DISPLAY_PAGE_FORWARD_NAME = "displayPage";

    /**
     * This member variable is a string constant that defines the name of the Edit commonly used
     * action forward. Such an action forward is often used to redirect to Edit Screening or Edit
     * Review, etc. page from the corresponding &quot;Create&quot; type of page.
     */
    public static final String EDIT_FORWARD_NAME = "edit";


    /**
     * This constant stores "No" value for Appeals Completed Early flag property.
     */
    public static final String NO_VALUE = "No";

    /**
     * This constant stores "Yes" value for Appeals Completed Early flag property.
     */
    public static final String YES_VALUE = "Yes";

    /**
     * This constant stores Appeals Completed Early flag property key.
     */
    public static final String APPEALS_COMPLETED_EARLY_PROPERTY_KEY = "Appeals Completed Early";

    /**
     * <p>A <code>long</code> providing the ID for project type for <code>Studio</code> projects.</p>
     */
    public static final long STUDIO_PROJECT_ID = 3;

    /**
     * The constant for the name of the Post-Mortem scorecard type.
     */
    public static final String POST_MORTEM_SCORECARD_TYPE_NAME = "Post-Mortem";

    /**
     * The constant for the name of the "submission" upload type.
     */
    public static final String SUBMISSION_UPLOAD_TYPE_NAME = "Submission";

    /**
     * The constant for the name of the "Activate" project status.
     */
    public static final String ACTIVE_PROJECT_STATUS_NAME = "Active";

    /**
     * The constant for the name of the "Activate" submission status.
     */
    public static final String ACTIVE_SUBMISSION_STATUS_NAME = "Active";

    /**
     * The constant for the name of the "Failed Screening" submission status.
     */
    public static final String FAILED_SCREENING_SUBMISSION_STATUS_NAME = "Failed Screening";

    /**
     * The constant for the name of the "Failed Checkpoint Screening" submission status.
     */
    public static final String FAILED_CHECKPOINT_SCREENING_SUBMISSION_STATUS_NAME = "Failed Checkpoint Screening";

    /**
     * The constant for the name of the "Contest" submission type.
     */
    public static final String CONTEST_SUBMISSION_TYPE_NAME = "Contest Submission";

    /**
     * The constant for the name of the "Specification" submission type.
     */
    public static final String SPECIFICATION_SUBMISSION_TYPE_NAME = "Specification Submission";

    /**
     * The constant for the name of the "Checkpoint" submission type.
     */
    public static final String CHECKPOINT_SUBMISSION_TYPE_NAME = "Checkpoint Submission";

    // ----------------------------------------------------------------- Late Deliverable type names -----

    /**
     * The constant for the "Missed Deadline" late deliverable type id.
     */
    public static final long MISSED_DEADLINE_ID = 1;

    /**
     * The constant for the "Rejected Final Fix" late deliverable type id.
     */
    public static final long REJECTED_FINAL_FIX_ID = 2;

    // ----------------------------------------------------------------- Project Payment Type ID -----

    /**
     * This constant for the "Contest Payment" project payment type id.
     */
    public static final Long CONTEST_PAYMENT_TYPE_ID = 1L;

    /**
     * This constant for the "Contest Checkpoint Payment" project payment type id.
     */
    public static final Long CONTEST_CHECKPOINT_PAYMENT_TYPE_ID = 2L;

    /**
     * This constant for the "Review Payment" project payment type id.
     */
    public static final Long REVIEW_PAYMENT_TYPE_ID = 3L;

    /**
     * This constant for the "Copilot Payment" project payment type id.
     */
    public static final Long COPILOT_PAYMENT_TYPE_ID = 4L;

    // ----------------------------------------------------------------- Other constants -----

    /**
     * The constant for the phase status code which can't be open.
     */
    public static final int CANNOT_OPEN_PHASE_STATUS_CODE = 5;

    /**
     * The constant for the contest prize type.
     */
    public static final String CONTEST_PRIZE_TYPE_NAME = "Contest Prize";

    /**
     * The constant for the checkpoint prize type.
     */
    public static final String CHECKPOINT_PRIZE_TYPE_NAME = "Checkpoint Prize";

    /**
     * <p>A <code>String</code> providing the path to configuration file for Config Manager.</p>
    */
    public static final String CONFIG_MANAGER_FILE = "com/topcoder/util/config/ConfigManager.properties";

    /**
     * <p>A <code>String</code> providing the configuration namespace for User Terms Of Use DAO.</p>
     */
    public static final String USER_TERMS_DAO_NAMESPACE = "com.cronos.termsofuse.dao.UserTermsOfUseDao";

    /**
     * <p>A <code>String</code> providing the configuration namespace for Project Terms Of Use DAO.</p>
     */
    public static final String PROJECT_TERMS_DAO_NAMESPACE = "com.cronos.termsofuse.dao.ProjectTermsOfUseDao";

    /**
     * <p>A <code>String</code> providing the configuration namespace for Terms Of Use DAO.</p>
     */
    public static final String TERMS_DAO_NAMESPACE = "com.cronos.termsofuse.dao.TermsOfUseDao";


    // Hidden constructor

    /**
     * This constructor is declared private to prohibit instantiation of the <code>Constants</code>
     * class.
     */
    private Constants() {
    }
}
