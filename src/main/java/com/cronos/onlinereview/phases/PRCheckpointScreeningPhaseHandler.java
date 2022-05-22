/*
 * Copyright (C) 2010 - 2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import com.topcoder.onlinereview.component.project.management.ProjectManager;
import com.topcoder.onlinereview.component.project.phase.ManagerHelper;
import com.topcoder.onlinereview.component.project.phase.Phase;
import com.topcoder.onlinereview.component.project.phase.PhaseHandlingException;
import com.topcoder.onlinereview.component.project.phase.handler.CheckpointScreeningPhaseHandler;
import com.topcoder.onlinereview.component.project.phase.handler.EmailOptions;
import com.topcoder.onlinereview.component.project.phase.handler.EmailScheme;
import com.topcoder.onlinereview.component.project.phase.handler.PhasesHelper;

import java.util.List;

/**
 * <p>A handle for <code>Checkpoint Screening</code> phase implementing the additional phase processing logic specific to
 * <code>Online Review</code> application.</p>
 *
 * <p>
 * Thread-safety: This class is thread-safe as it does not contain any mutable inner state.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class PRCheckpointScreeningPhaseHandler extends CheckpointScreeningPhaseHandler {
    /**
     * Represents the <code>ScreeningResultNotification</code> instance to send notification email.
     */
    private final ScreeningResultNotification notification;


    /**
     * <p>Constructs new <code>PRCheckpointScreeningPhaseHandler</code> instance initialized based on parameters from
     * specified configuration namespace.</p>
     */
    public PRCheckpointScreeningPhaseHandler(ManagerHelper managerHelper,
                                             List<EmailScheme> emailSchemes,
                                             EmailScheme reviewFeedbackEmailScheme,
                                             EmailOptions defaultStartEmailOption,
                                             EmailOptions defaultEndEmailOption,
                                             ScreeningResultNotification notification) {
        super(managerHelper, emailSchemes, reviewFeedbackEmailScheme, defaultStartEmailOption, defaultEndEmailOption);
        this.notification = notification;
    }

    /**
     * <p>Handles the current phase state transition. Sends emails on screening results to submitters.</p>
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
        ProjectManager projectManager = getManagerHelper().getProjectManager();
        long projectId = phase.getProject().getId();

        if (!toStart && PRHelper.isStudioProject(projectManager, projectId)) {
                
            try {
                notification.sendEmailToSubmitters(projectManager.getProject(projectId));
            } catch (Exception e) {
                throw new PhaseHandlingException("Failed to send email to submitters on Checkpoint Screening results", e);
            }
                
            PaymentsHelper.processAutomaticPayments(projectId, operator);
        }
    }
}
