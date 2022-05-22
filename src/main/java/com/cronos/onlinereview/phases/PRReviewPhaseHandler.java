/*
 * Copyright (C) 2005 - 2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import com.topcoder.onlinereview.component.project.phase.ManagerHelper;
import com.topcoder.onlinereview.component.project.phase.OperationCheckResult;
import com.topcoder.onlinereview.component.project.phase.Phase;
import com.topcoder.onlinereview.component.project.phase.PhaseHandlingException;
import com.topcoder.onlinereview.component.project.phase.handler.EmailOptions;
import com.topcoder.onlinereview.component.project.phase.handler.EmailScheme;
import com.topcoder.onlinereview.component.project.phase.handler.PhasesHelper;
import com.topcoder.onlinereview.component.project.phase.handler.ReviewPhaseHandler;

import java.util.List;

/**
 * The extend from ReviewPhaseHandler to add on the logic to push data to project_result.
 *
 * <p>
 * Thread-safety: This class is thread-safe as it does not contain any mutable inner state.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class PRReviewPhaseHandler extends ReviewPhaseHandler {
    
    /**
    * Used for pulling data to project_result table and filling payments.
    */
    private final PRHelper prHelper = new PRHelper();

    /**
     * Represents the <code>ReviewResultNotification</code> instance to send notification email.
     */
    private final ReviewResultNotification notification;

    /**
     * Create a new instance of ReviewPhaseHandler using the given namespace for loading configuration settings.
     *
     * @throws IllegalArgumentException if the input is null or empty string.
     */
    public PRReviewPhaseHandler(ManagerHelper managerHelper,
                                List<EmailScheme> emailSchemes,
                                EmailScheme reviewFeedbackEmailScheme,
                                EmailOptions defaultStartEmailOption,
                                EmailOptions defaultEndEmailOption,
                                Long minPeerReviews,
                                String peerReviewAggregationURLTemplate,
                                ReviewResultNotification notification) {
        super(managerHelper, emailSchemes, reviewFeedbackEmailScheme, defaultStartEmailOption, defaultEndEmailOption,
                 minPeerReviews, peerReviewAggregationURLTemplate);
        this.notification = notification;
    }

    /**
     * Check if the input phase can be executed or not. Just call super method.</p>
     *
     * @param phase The input phase to check.
     *
     * @return True if the input phase can be executed, false otherwise.
     *
     * @throws PhaseHandlingException if there is any error occurred while processing the phase.
     * @throws IllegalArgumentException if the input is null.
     */
    public OperationCheckResult canPerform(Phase phase) throws PhaseHandlingException {
        return super.canPerform(phase);
    }

    /**
     * Provides additional logic to execute a phase. this extension will update valid_submission_ind, submit_timestamp
     * field of project_result table.</p>
     *
     * @param phase The input phase to check.
     * @param operator The operator that execute the phase.
     *
     * @throws PhaseHandlingException if there is any error occurred while processing the phase.
     * @throws IllegalArgumentException if the input parameters is null or empty string.
     */
    public void perform(Phase phase, String operator) throws PhaseHandlingException {
        super.perform(phase, operator);
        boolean toStart = PhasesHelper.checkPhaseStatus(phase.getPhaseStatus());

        prHelper.processReviewPR(getManagerHelper(), phase, operator, toStart);
        try {
            if (!toStart)
            {
                Phase appealsResponsePhase = PhasesHelper.locatePhase(phase, "Appeals Response", true, false);
                if (appealsResponsePhase == null) {
                    notification.sendMailForWinners(getManagerHelper().getProjectManager().getProject(phase.getProject().getId()));
                }
            }
        } catch (Throwable e) {
            throw new PhaseHandlingException(e.getMessage(), e);
        }
    }

}
