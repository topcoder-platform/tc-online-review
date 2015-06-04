/**
 * Copyright (C) 2013 - 2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.phase.PhaseHandlingException;
import com.topcoder.project.phases.Phase;

/**
 * The iterative review phase handler.
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class PRIterativeReviewPhaseHandler extends IterativeReviewPhaseHandler {

    /**
     * Create a new instance of this class using the default namespace for loading configuration settings.
     * @throws ConfigurationException
     *             if errors occurred while loading configuration settings.
     */
    public PRIterativeReviewPhaseHandler() throws ConfigurationException {
        super();
    }

    /**
     * Create a new instance of this class using the given namespace for loading configuration settings.
     * @param namespace
     *            the namespace to load configuration settings from.
     * @throws ConfigurationException
     *             if errors occurred while loading configuration settings or required properties missing.
     * @throws IllegalArgumentException
     *             if the input is null or empty string.
     */
    public PRIterativeReviewPhaseHandler(String namespace) throws ConfigurationException {
        super(namespace);
    }

    /**
     * Performs the phase operation.
     * @param phase
     *            The input phase to check.
     * @param operator
     *            The operator that execute the phase.
     * @throws PhaseNotSupportedException
     *             if the input phase type is not "Iterative Review" type.
     * @throws PhaseHandlingException
     *             if there is any error occurred while processing the phase.
     * @throws IllegalArgumentException
     *             if the input parameters is null or empty string.
     */
    public void perform(Phase phase, String operator) throws PhaseHandlingException {
        super.perform(phase, operator);
        boolean toStart = PhasesHelper.checkPhaseStatus(phase.getPhaseStatus());

        long projectId = phase.getProject().getId();
        if (!toStart) {
            // check if the submission has passed review
            boolean passedReview = checkPassedReview(phase.getProject().getId());

            if (passedReview) {
                PRHelper.completeProject(getManagerHelper(), phase, operator);
            }

            PaymentsHelper.processAutomaticPayments(projectId, operator);
        }
    }

    /**
     * Checks whether any submission has passed Iterative Review.
     *
     * @param projectId
     *            the project ID.
     * @return true if any submission has passed review.
     * @throws PhaseHandlingException
     *             if any error occurs
     */
    public boolean checkPassedReview(long projectId) throws PhaseHandlingException {
        // Search all "Active" submissions for current project with contest submission type
        Submission[] submissions = PhasesHelper.getActiveProjectSubmissions(getManagerHelper().getUploadManager(),
                projectId, Constants.SUBMISSION_TYPE_CONTEST_SUBMISSION);

        // Check if there's a winning active submission
        for (Submission sub : submissions) {
            if (sub.getSubmissionStatus().getName().equals(Constants.SUBMISSION_STATUS_ACTIVE) &&
                    sub.getPlacement() != null && sub.getPlacement() == 1 && sub.getFinalScore() != null) {
                return true;
            }
        }
        return false;
    }
}
