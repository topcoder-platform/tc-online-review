/*
 * Copyright (C) 2006 TopCoder, Inc. All Rights Reserved.
 */
package com.cronos.onlinereview.project;

/**
 * <p>A provider for the various error and information messages which are displayed to users in context of <code>Online
 * Review</code> application.
 *
 * @author  TCSDEVELOPER
 * @version 1.0
 */
public class Messages {

    /**
     * <p>The configuration interface.</p>
     */
    private static Configuration configuration = new Configuration(Messages.class.getName());

    /**
     * <p>A private constructor disabling the instantiation of this class.</p>
     */
    private Messages() {
    }

    /**
     * <p>Gets the error message which is expected to be displayed to user in case of an attempt to view the screening
     * results for selected project and submission while the user is not granted the appropriate permission.</p>
     *
     * @return a <code>String</code> providing the sort of <code>Unauthorized Access</code> message.
     */
    public static String getNoPermissionViewScreening() {
        return Messages.getMessage("project.no_permission.view_screening");
    }

    /**
     * <p>Gets the error message which is expected to be displayed to user in case of an attempt to view the screening
     * results for selected project and submission while the user is not granted the appropriate permission.</p>
     *
     * @return a <code>String</code> providing the sort of <code>Unauthorized Access</code> message.
     */
    public static String getNoPermissionViewAggregation() {
        return Messages.getMessage("project.no_permission.view_aggregation");
    }

    /**
     * <p>Gets the error message which is expected to be displayed to user in case of an attempt to view the final
     * review results for selected project while the user is not granted the appropriate permission.</p>
     *
     * @return a <code>String</code> providing the sort of <code>Unauthorized Access</code> message.
     */
    public static String getNoPermissionViewFinalReview() {
        return Messages.getMessage("project.no_permission.view_final_review");
    }

    /**
     * <p>Gets the error message which is expected to be displayed to user in case of an attempt to view the composite
     * review results for selected project while the user is not granted the appropriate permission.</p>
     *
     * @return a <code>String</code> providing the sort of <code>Unauthorized Access</code> message.
     */
    public static String getNoPermissionViewCompositeReview() {
        return Messages.getMessage("project.no_permission.view_composite_review");
    }

    /**
     * <p>Gets the error message which is expected to be displayed to user in case of an attempt to view the appeals
     * for selected submission while the user is not granted the appropriate permission.</p>
     *
     * @return a <code>String</code> providing the sort of <code>Unauthorized Access</code> message.
     */
    public static String getNoPermissionViewAppeals() {
        return Messages.getMessage("project.no_permission.view_appeals");
    }

    /**
     * <p>Gets the error message which is expected to be displayed to user in case of an attempt to view the appeal
     * responses for selected submission while the user is not granted the appropriate permission.</p>
     *
     * @return a <code>String</code> providing the sort of <code>Unauthorized Access</code> message.
     */
    public static String getNoPermissionViewAppealResponses() {
        return Messages.getMessage("project.no_permission.view_appeal_responses");
    }

    /**
     * <p>Gets the error message which is expected to be displayed to reviewer in case of an attempt to view the review
     * made by another reviewer.</p>
     *
     * @return a <code>String</code> providing the sort of <code>Unauthorized Access</code> message.
     */
    public static String getNoPermissionViewReviewerReview() {
        return Messages.getMessage("project.no_permission.view_reviewer_review");
    }

    /**
     * <p>Gets the error message which is expected to be displayed to user in case of an attempt to submit an approval
     * scorecard for selected project.</p>
     *
     * @return a <code>String</code> providing the sort of <code>Unauthorized Access</code> message.
     */
    public static String getNoPermissionPerformApproval() {
        return Messages.getMessage("project.no_permission.perform_approval");
    }

    /**
     * <p>Gets the error message which is expected to be displayed to user in case of an attempt to submit a final
     * review scorecard for selected project.</p>
     *
     * @return a <code>String</code> providing the sort of <code>Unauthorized Access</code> message.
     */
    public static String getNoPermissionPerformFinalReview() {
        return Messages.getMessage("project.no_permission.perform_final_review");
    }

    /**
     * <p>Gets the error message which is expected to be displayed to user in case of an attempt to submit a screening
     * scorecard for selected project and submission.</p>
     *
     * @return a <code>String</code> providing the sort of <code>Unauthorized Access</code> message.
     */
    public static String getNoPermissionPerformScreening() {
        return Messages.getMessage("project.no_permission.perform_screening");
    }

    /**
     * <p>Gets the error message which is expected to be displayed to user in case of an attempt to submit an approval
     * scorecard for selected project.</p>
     *
     * @return a <code>String</code> providing the sort of <code>Unauthorized Access</code> message.
     */
    public static String getInvalidInputApprovalIncomplete() {
        return Messages.getMessage("project.invalid_input.approval_incomplete");
    }

    /**
     * <p>Gets the error message which is expected to be displayed to user in case of an attempt to submit a screening
     * scorecard for selected project.</p>
     *
     * @return a <code>String</code> providing the sort of <code>Unauthorized Access</code> message.
     */
    public static String getInvalidInputScreeningIncomplete() {
        return Messages.getMessage("project.invalid_input.screening_incomplete");
    }

    /**
     * <p>Gets the confirmation message which is expected to be displayed to user in case the "Final Review Scorecard"
     * for selected project is submitted without approval.</p>
     *
     * @return a <code>String</code> providing the sort of <code>Unauthorized Access</code> message.
     */
    public static String getFinalReviewNoApprovalConfirmation() {
        return Messages.getMessage("project.messages.confirmation.final_review_no_approval");
    }

    /**
     * <p>Gets the information message which is expected to be displayed to user in case the "Final Review"
     * for selected project is submitted without approval.</p>
     *
     * @return a <code>String</code> providing the sort of <code>Unauthorized Access</code> message.
     */
    public static String getNotificationFinalReviewFailed() {
        return Messages.getMessage("project.messages.notification.final_review_failed");
    }

    /**
     * <p>Gets the information message which is expected to be displayed to reviewer in case the reviewer is pressing
     * "Edit" button to edit an appeal response while the "Appeal Response" phase is ended.</p>
     *
     * @return a <code>String</code> providing the sort of <code>Appeals Phase Ended</code> message.
     */
    public static String getNotificationAppealsResponsePhaseEnded() {
        return Messages.getMessage("project.messages.notification.appeals_response_phase_ended");
    }

    /**
     * <p>Gets the error message which is expected to be displayed to user in case the modified response is not selected
     * when an appeals response is submitted.</p>
     *
     * @return a <code>String</code> providing the sort of <code>Unauthorized Access</code> message.
     */
    public static String getInvalidInputModifiedResponseNotSelected() {
        return Messages.getMessage("project.invalid_input.appeals_response.modified_response.not_selected");
    }

    /**
     * <p>Gets the error message which is expected to be displayed to user in case of an attempt to submit a review
     * scorecard for selected project.</p>
     *
     * @return a <code>String</code> providing the sort of <code>Invalid Review Scorecard</code> message.
     */
    public static String getInvalidInputReviewIncomplete() {
        return Messages.getMessage("project.invalid_input.approval_incomplete");
    }

    /**
     * <p>Gets the error message which is expected to be displayed to user in case of an attempt to submit an appeal
     * with empty text.</p>
     *
     * @return a <code>String</code> providing the sort of <code>Invalid Appeal</code> message.
     */
    public static String getInvalidInputAppealTextEmpty() {
        return Messages.getMessage("project.invalid_input.appeal_text_empty");
    }

    /**
     * <p>Gets the error message which is expected to be displayed to user in case of an attempt to submit an
     * aggregation review with empty text.</p>
     *
     * @return a <code>String</code> providing the sort of <code>Invalid Appeal</code> message.
     */
    public static String getInvalidInputAggregationReviewIncomplete() {
        return Messages.getMessage("project.invalid_input.aggregation_review_incomplete");
    }

    /**
     * <p>Gets the error message which is expected to be displayed to user in case of an attempt to submit an
     * aggregation with empty text.</p>
     *
     * @return a <code>String</code> providing the sort of <code>Invalid Aggregation</code> message.
     */
    public static String getInvalidInputAggregationIncomplete() {
        return Messages.getMessage("project.invalid_input.aggregation_incomplete");
    }

    /**
     * <p>Gets the error message which is expected to be displayed to user in case of an attempt to submit an updated
     * project with missing details.</p>
     *
     * @return a <code>String</code> providing the sort of <code>Invalid Project Details</code> message.
     */
    public static String getInvalidInputProjectDetails() {
        return Messages.getMessage("project.invalid_input.project_details");
    }

    /**
     * <p>Gets the error message which is expected to be displayed to user in case of an attempt to submit a aggregation
     * review scorecard for selected project.</p>
     *
     * @return a <code>String</code> providing the sort of <code>Unauthorized Access</code> message.
     */
    public static String getNoPermissionPerformAggregationReview() {
        return Messages.getMessage("project.no_permission.perform_aggregation_review");
    }

    /**
     * <p>Gets the error message which is expected to be displayed to user in case of an attempt to remove a submission
     * for selected project.</p>
     *
     * @return a <code>String</code> providing the sort of <code>Unauthorized Access</code> message.
     */
    public static String getNoPermissionRemoveSubmission() {
        return Messages.getMessage("project.no_permission.remove_submission");
    }

    /**
     * <p>Gets the error message which is expected to be displayed to user in case of an attempt to download a final fix
     * for selected project.</p>
     *
     * @return a <code>String</code> providing the sort of <code>Unauthorized Access</code> message.
     */
    public static String getNoPermissionDownloadFinalFix() {
        return Messages.getMessage("project.no_permission.download_final_fix");
    }

    /**
     * <p>Gets the error message which is expected to be displayed to user in case of an attempt to download a test case
     * for selected project.</p>
     *
     * @return a <code>String</code> providing the sort of <code>Unauthorized Access</code> message.
     */
    public static String getNoPermissionDownloadTestCases() {
        return Messages.getMessage("project.no_permission.download_test_cases");
    }

    /**
     * <p>Gets the error message which is expected to be displayed to user in case of an attempt to submit a aggregation
     * review scorecard for selected project.</p>
     *
     * @return a <code>String</code> providing the sort of <code>Unauthorized Access</code> message.
     */
    public static String getNotRequiredPerformAggregationReview() {
        return Messages.getMessage("project.messages.not_required_perform_aggregation_review");
    }

    /**
     * <p>Gets the confirmation message which is expected to be displayed to user after submitting a "Contact Manager"
     * form.</p>
     *
     * @return a <code>String</code> providing the sort of <code>Unauthorized Access</code> message.
     */
    public static String getContactManagerConfirmation() {
        return Messages.getMessage("project.messages.confirmation.contact_manager");
    }

    /**
     * <p>Gets the confirmation message which is expected to be displayed to user after saving project details with bad
     * project phase date.</p>
     *
     * @return a <code>String</code> providing the sort of <code>Unauthorized Access</code> message.
     */
    public static String getBadPhaseDateConfirmation() {
        return Messages.getMessage("project.messages.confirmation.bad_phase_date");
    }

    /**
     * <p>Gets the error message which is expected to be displayed to user after clicking "Upload Test Cases" link.</p>
     *
     * @return a <code>String</code> providing the sort of <code>Unauthorized Access</code> message.
     */
    public static String getInvalidPhaseForTestCases() {
        return Messages.getMessage("project.messages.invalid_phase_for_testcase");
    }

    /**
     * <p>Gets the error message which is expected to be displayed to user who is not granted permission to edit project
     * details.</p>
     *
     * @return a <code>String</code> providing the sort of <code>Unauthorized Access</code> message.
     */
    public static String getNoPermissionEditProject() {
        return Messages.getMessage("project.no_permission.edit_project");
    }

    /**
     * <p>Gets the error message which is expected to be displayed to user who is not granted permission to create
     * project details.</p>
     *
     * @return a <code>String</code> providing the sort of <code>Unauthorized Access</code> message.
     */
    public static String getNoPermissionCreateProject() {
        return Messages.getMessage("project.no_permission.create_project");
    }

    /**
     * <p>Gets the error message which is expected to be displayed to user who is not granted permission to edit project
     * details.</p>
     *
     * @return a <code>String</code> providing the sort of <code>Unauthorized Access</code> message.
     */
    public static String getPhaseNotDeleted() {
        return Messages.getMessage("project.messages.edit_project.phase_not_deleted");
    }

    /**
     * <p>Gets the error message which is expected to be displayed to user when project phase end date is before start
     * date.</p>
     *
     * @return a <code>String</code> providing the sort of <code>Unauthorized Access</code> message.
     */
    public static String getInvalidInputEndAfterStart() {
        return Messages.getMessage("project.invalid_input.start_end");
    }

    /**
     * <p>Gets the error message which is expected to be displayed to user when project phase duration is invalid.</p>
     *
     * @return a <code>String</code> providing the sort of <code>Unauthorized Access</code> message.
     */
    public static String getInvalidInputPhaseDuration() {
        return Messages.getMessage("project.invalid_input.phase_duration");
    }

    /**
     * <p>Gets the error message which is expected to be displayed to user when more than 1 designer are assigned to
     * project.</p>
     *
     * @return a <code>String</code> providing the sort of <code>Unauthorized Access</code> message.
     */
    public static String getInvalidInputOneDesigner() {
        return Messages.getMessage("project.invalid_input.one_designer");
    }

    /**
     * <p>Gets the error message which is expected to be displayed to user when more than 1 reviewer are assigned to
     * project.</p>
     *
     * @return a <code>String</code> providing the sort of <code>Unauthorized Access</code> message.
     */
    public static String getInvalidInputOneReviewer() {
        return Messages.getMessage("project.invalid_input.one_reviewer");
    }

    /**
     * <p>Gets the error message which is expected to be displayed to user when unknown member is assigned to
     * project.</p>
     *
     * @return a <code>String</code> providing the sort of <code>Unauthorized Access</code> message.
     */
    public static String getInvalidInputUnknownHandle() {
        return Messages.getMessage("project.invalid_input.unknown_handle");
    }

    /**
     * <p>Gets the error message which is expected to be displayed to user when a project is moved to previous phase.
     * </p>
     *
     * @return a <code>String</code> providing the sort of <code>Unauthorized Access</code> message.
     */
    public static String getNoPermissionMovePhaseBack() {
        return Messages.getMessage("project.no_permission.phase_back");
    }

    /**
     * <p>Gets the message referenced by the specified key.</p>
     *
     * @param messageKey a <code>String</code> providing the key referencing the requested message in resource bundle.
     * @return a <code>String</code> providing the message matching the specified key.
     */
    private static String getMessage(String messageKey) {
        String message = Messages.configuration.getProperty(messageKey);
        assert (message != null) && (message.trim().length() > 0) : "The message [" + messageKey + "] is not provided "
                                                                    + "by the existing tests configuration "
                                                                    + "(Messages.xml)";
        return message;
    }
}