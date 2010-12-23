/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import java.sql.Connection;
import java.sql.SQLException;

import com.topcoder.management.phase.PhaseHandlingException;
import com.topcoder.management.phase.PhaseManagementException;
import com.topcoder.management.review.data.Comment;
import com.topcoder.management.review.data.Review;
import com.topcoder.project.phases.Phase;

/**
 * This class is the customized phase handler for Approval phase, it will automatically complete the project, if the
 * approval phase is approved.
 *
 * @author FireIce
 * @version 1.0
 * @since Online Review Payments and Status Automation Assembly 1.0
 */
public class PRApprovalPhaseHandler extends ApprovalPhaseHandler {
    /**
     * Constant for Approver resource role name.
     */
    private static final String APPROVER = "Approver";

    /**
     * Create a new instance of PRApprovalPhaseHandler using the default namespace for loading configuration settings.
     *
     * @throws ConfigurationException
     *             if errors occurred while loading configuration settings.
     */
    public PRApprovalPhaseHandler() throws ConfigurationException {
        super();
    }

    /**
     * Create a new instance of PRApprovalPhaseHandler using the given namespace for loading configuration settings.
     *
     * @param namespace
     *            the namespace to load configuration settings from.
     * @throws ConfigurationException
     *             if errors occurred while loading configuration settings or required properties missing.
     * @throws IllegalArgumentException
     *             if the input is null or empty string.
     */
    public PRApprovalPhaseHandler(String namespace) throws ConfigurationException {
        super(namespace);
    }

    /**
     * Provides additional logic to execute a phase.
     *
     * @param phase
     *            The input phase to check.
     * @param operator
     *            The operator that execute the phase.
     * @throws PhaseNotSupportedException
     *             if the input phase type is not "Approval" type.
     * @throws PhaseHandlingException
     *             if there is any error occurred while processing the phase.
     * @throws IllegalArgumentException
     *             if the input parameters is null or empty string.
     */
    public void perform(Phase phase, String operator) throws PhaseHandlingException {
        // invoke the original Approval task.
        super.perform(phase, operator);

        boolean toStart = PhasesHelper.checkPhaseStatus(phase.getPhaseStatus());

        if (!toStart) {
            // check final fix is not been rejected
            boolean rejected = checkScorecardsRejected(phase);

            if (!rejected) {
                // update project status to Complete
                PRHelper.completeProject(getManagerHelper(), phase, operator);
            }
        }
    }

    /**
     * Checks whether any the approval scorecard is approved. If any approval scorecard is rejected, true
     * will be returned, otherwise false.
     *
     * @param phase
     *            the input phase.
     * @return true if any committed approval scorecard is rejected, false otherwise.
     * @throws PhaseHandlingException
     *             if any error occurs
     */
    private boolean checkScorecardsRejected(Phase phase) throws PhaseHandlingException {
        Connection conn = null;
        try {
            conn = createConnection();
            Review[] approveReviews = PhasesHelper.searchProjectReviewsForResourceRoles(conn, getManagerHelper(), phase
                    .getProject().getId(), new String[] {APPROVER}, null);
            approveReviews = PhasesHelper.getApprovalPhaseReviews(approveReviews, phase);

            // check for approved/rejected comments.
            boolean rejected = false;
            for (int reviewIndex = 0; reviewIndex < approveReviews.length; reviewIndex++) {
                if (!approveReviews[reviewIndex].isCommitted()) {
                    continue;
                }

                Comment[] comments = approveReviews[reviewIndex].getAllComments();

                for (int i = 0; i < comments.length; i++) {
                    String value = (String) comments[i].getExtraInfo();
                    if (comments[i].getCommentType().getName().equals("Approval Review Comment")) {
                        if (PhasesHelper.APPROVED.equalsIgnoreCase(value)
                                || PhasesHelper.ACCEPTED.equalsIgnoreCase(value)) {
                            continue;
                        } else if (PhasesHelper.REJECTED.equalsIgnoreCase(value)) {
                            rejected = true;
                            break;
                        } else {
                            throw new PhaseHandlingException("Comment can either be Approved, Accepted or Rejected.");
                        }
                    }
                }
            }

            return rejected;

        } catch (SQLException e) {
            throw new PhaseHandlingException("Problem when connecting to database", e);
        } catch (PhaseManagementException e) {
            throw new PhaseHandlingException("Problem when persisting phases", e);
        } finally {
            PhasesHelper.closeConnection(conn);
        }
    }
}
