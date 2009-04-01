/*
 * Copyright (C) 2006-2007 TopCoder Inc.  All Rights Reserved.
 */
package com.cronos.onlinereview.actions;

/**
 * This class contains various constants used throughout the application.
 * <p>
 * This class is thread safe as it contains only immutable data.
 * </p>
 *
 * @author George1
 * @author real_vg
 * @version 1.0
 */
public class Constants {

    // ------------------------------------------------------------------ Resource Role names -----

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
     * This member variable is a string constant that defines the name of the Public role.
     */
    public static final String PUBLIC_ROLE_NAME = "Public";

    /**
     * This member variable is a string constant that defines the name of the Designer role.
     * Designer role is assigned as a project resource.  Designers do not have much difference
     * from the Public view.  The purpose is for the designer to view private projects and to
     * receive payments.
     */
    public static final String DESIGNER_ROLE_NAME = "Designer";

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
     * This member variable is a constant array that holds names of different manager roles.
     *
     * @see #MANAGER_ROLE_NAME
     * @see #GLOBAL_MANAGER_ROLE_NAME
     */
    public static final String[] MANAGER_ROLE_NAMES = new String[] {MANAGER_ROLE_NAME, GLOBAL_MANAGER_ROLE_NAME};


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
     * This member variable is a string constant that defines the name of the View Projects Inactive
     * permission.
     */
    public static final String VIEW_PROJECTS_INACTIVE_PERM_NAME = "View Projects Inactive";

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
     * This member variable is a string constant that defines the name of the Perform Submission
     * permission.
     */
    public static final String PERFORM_SUBM_PERM_NAME = "Perform Submission";

    /**
     * This member variable is a string constant that defines the name of the View All Submissions
     * permission.
     */
    public static final String VIEW_ALL_SUBM_PERM_NAME = "View All Submissions";

    /**
     * This member variable is a string constant that defines the name of the View My Submissions
     * permission.
     */
    public static final String VIEW_MY_SUBM_PERM_NAME = "View My Submissions";

    /**
     * This member variable is a string constant that defines the name of the
     * View Screener Submission permission.
     */
    public static final String VIEW_SCREENER_SUBM_PERM_NAME = "View Screener Submission";

    /**
     * This member variable is a string constant that defines the name of the
     * View Most Recent Submissions permission.
     */
    public static final String VIEW_RECENT_SUBM_PERM_NAME = "View Most Recent Submissions";

    /**
     * This member variable is a string constant that defines the name of the
     * View Winning Submission permission.
     */
    public static final String VIEW_WINNING_SUBM_PERM_NAME = "View Winning Submission";

    /**
     * This member variable is a string constant that defines the name of the
     * Download Custom Submission permission.
     */
    public static final String DOWNLOAD_CUSTOM_SUBM_PERM_NAME = "Download Custom Submission";

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
     * This member variable is a string constant that defines the name of the View Screening
     * permission.
     */
    public static final String VIEW_SCREENING_PERM_NAME = "View Screening";

    /**
     * This member variable is a string constant that defines the name of the Perform Review
     * permission.
     */
    public static final String PERFORM_REVIEW_PERM_NAME = "Perform Review";

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
     * This member variable is a string constant that defines the name of the Edit Any Scorecard
     * permission.
     */
    public static final String EDIT_ANY_SCORECARD_PERM_NAME = "Edit Any Scorecard";

    /**
     * This member variable is a string constant that defines the name of the
     * Download Review Document permission.
     */
    public static final String DOWNLOAD_DOCUMENT_PERM_NAME = "Download Review Document";


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
     * TODO: Add reasonable description here
     */
    public static final String VIEW_REGISTRANTS_APP_FUNC = "VIEW_REGISTRANTS";

    /**
     * TODO: Add reasonable description here
     */
    public static final String VIEW_SUBMISSIONS_APP_FUNC = "VIEW_SUBMISSIONS";

    /**
     * TODO: Add reasonable description here
     */
    public static final String VIEW_REVIEWS_APP_FUNC = "VIEW_REVIEWS";

    /**
     * TODO: Add reasonable description here
     */
    public static final String AGGREGATION_APP_FUNC = "AGGREGATION";

    /**
     * TODO: Add reasonable description here
     */
    public static final String FINAL_FIX_APP_FUNC = "FINAL_FIX";

    /**
     * TODO: Add reasonable description here
     */
    public static final String APPROVAL_APP_FUNC = "APPROVAL";


    // -------------------------------------------------------------------- Deliverable names -----

    /**
     * This member variable is a string constant that defines the name of the Submission
     * deliverable.
     */
    public static final String SUBMISSION_DELIVERABLE_NAME = "Submission";

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


    // --------------------------------------------------- Auto Screening Response Severities -----

    /**
     * This member variable is a string constant that defines the name of the Success auto screening
     * response severity.
     */
    public static final String SUCCESS_SCREENING_SEVERITY_NAME = "Success";

    /**
     * This member variable is a string constant that defines the name of the Warning auto screening
     * response severity.
     */
    public static final String WARNING_SCREENING_SEVERITY_NAME = "Warning";

    /**
     * This member variable is a string constant that defines the name of the Fatal Error auto
     * screening response severity.
     */
    public static final String FATAL_ERROR_SCREENING_SEVERITY_NAME = "Fatal Error";


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
    public static final String USER_ERRROR_FORWARD_NAME = "userError";

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
     * action forward. Such an action foward is often used to redirect to Edit Screening or Edit
     * Review, etc. page from the corresponding &quot;Create&quot; type of page.
     */
    public static final String EDIT_FORWARD_NAME = "edit";


    // Hidden constructor

    /**
     * This constructor is declared private to prohibit instantiation of the <code>Constants</code>
     * class.
     */
    private Constants() {
    }
}
