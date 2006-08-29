/*
 * Copyright (C) 2006 TopCoder Inc.  All Rights Reserved.
 */
package com.cronos.onlinereview.actions;

/**
 * This class contains various constants used throughout the application.
 *
 * @author TCSAssemblyTeam
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
    public static final String VIEW_MY_SUBM_PERM_NAME = "View MY Submissions";

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
     * View Most Recent after Appeals Response permission.
     */
    public static final String VIEW_RECENT_SUBM_AAR_PERM_NAME = "View Most Recent after Appeals Response";

    /**
     * This member variable is a string constant that defines the name of the Remove Submission
     * permission.
     */
    public static final String REMOVE_SUBM_PERM_NAME = "Remove Submission";

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
     * This member variable is a string constant that defines the name of the Perform Final Fix
     * permission.
     */
    public static final String PERFORM_FINAL_FIX = "Perform Final Fix";

    /**
     * This member variable is a string constant that defines the name of the Download Final Fix
     * permission.
     */
    public static final String DOWNLOAD_FINAL_FIX = "Download Final Fix";


    // Hidden constructor

    /**
     * This constructor is declared private to prohibit instantiation of the <code>Constants</code>
     * class.
     */
    private Constants() {
    }
}
