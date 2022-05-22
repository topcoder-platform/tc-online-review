/*
 * Copyright (C) 2010 - 2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import com.topcoder.onlinereview.component.project.phase.ManagerHelper;
import com.topcoder.onlinereview.component.project.phase.Phase;
import com.topcoder.onlinereview.component.project.phase.PhaseHandlingException;
import com.topcoder.onlinereview.component.project.phase.handler.CheckpointReviewPhaseHandler;
import com.topcoder.onlinereview.component.project.phase.handler.EmailOptions;
import com.topcoder.onlinereview.component.project.phase.handler.EmailScheme;
import com.topcoder.onlinereview.component.project.phase.handler.PhasesHelper;

import java.util.List;

/**
 * <p>A handle for <code>Checkpoint Review</code> phase implementing the additional phase processing logic specific to
 * <code>Online Review</code> application.</p>
 *
 * <p>
 * Thread-safety: This class is thread-safe as it does not contain any mutable inner state.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class PRCheckpointReviewPhaseHandler extends CheckpointReviewPhaseHandler {

    /**
     * <p>Constructs new <code>PRCheckpointReviewPhaseHandler</code> instance initialized based on parameters from
     * specified configuration namespace.</p>
     *
     */
    public PRCheckpointReviewPhaseHandler(ManagerHelper managerHelper,
                                          List<EmailScheme> emailSchemes,
                                          EmailScheme reviewFeedbackEmailScheme,
                                          EmailOptions defaultStartEmailOption,
                                          EmailOptions defaultEndEmailOption,
                                          Long minPeerReviews,
                                          String peerReviewAggregationURLTemplate) {
        super(managerHelper, emailSchemes, reviewFeedbackEmailScheme, defaultStartEmailOption, defaultEndEmailOption,
                minPeerReviews, peerReviewAggregationURLTemplate);
    }
    
    /**
     * <p>Handles the current phase state transition. It will populate the submitter's checkpoint prize.</p>
     *
     * @param phase    The input phase to check.
     * @param operator The operator that execute the phase.
     * @throws PhaseHandlingException if there is any error occurred while processing the phase.
     * @throws IllegalArgumentException if the input parameters is null or empty string.
     */
    @Override
    public void perform(Phase phase, String operator) throws PhaseHandlingException {
        super.perform(phase, operator);
        boolean toStart = PhasesHelper.checkPhaseStatus(phase.getPhaseStatus());
        if (!toStart) {
            long projectId = phase.getProject().getId();
            PaymentsHelper.processAutomaticPayments(projectId, operator);
        }
    }
}
