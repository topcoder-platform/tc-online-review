/*
 * Copyright (C) 2005 - 2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import com.topcoder.onlinereview.component.project.management.ProjectManager;
import com.topcoder.onlinereview.component.project.phase.ManagerHelper;
import com.topcoder.onlinereview.component.project.phase.OperationCheckResult;
import com.topcoder.onlinereview.component.project.phase.Phase;
import com.topcoder.onlinereview.component.project.phase.PhaseHandlingException;
import com.topcoder.onlinereview.component.project.phase.handler.EmailOptions;
import com.topcoder.onlinereview.component.project.phase.handler.EmailScheme;
import com.topcoder.onlinereview.component.project.phase.handler.PhasesHelper;
import com.topcoder.onlinereview.component.project.phase.handler.ScreeningPhaseHandler;

import java.util.List;

/**
 * The extend from ScreeningPhaseHandler to add on the logic to push data to project_result.
 *
 * <p>
 * Thread-safety: This class is thread-safe as it does not contain any mutable inner state.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class PRScreeningPhaseHandler extends ScreeningPhaseHandler {
    
    /**
    * Used for pulling data to project_result table and filling payments.
    */
    private final PRHelper prHelper = new PRHelper();
    
    /**
     * Represents the <code>ScreeningResultNotification</code> instance to send notification email.
     */
    private final ScreeningResultNotification notification;
    
    /**
     * Create a new instance of ScreeningPhaseHandler using the given namespace for loading configuration settings.
     *
     * @throws IllegalArgumentException if the input is null or empty string.
     */
    public PRScreeningPhaseHandler(ManagerHelper managerHelper,
                                   List<EmailScheme> emailSchemes,
                                   EmailScheme reviewFeedbackEmailScheme,
                                   EmailOptions defaultStartEmailOption,
                                   EmailOptions defaultEndEmailOption,
                                   ScreeningResultNotification notification) {
        super(managerHelper, emailSchemes, reviewFeedbackEmailScheme, defaultStartEmailOption, defaultEndEmailOption);
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

        long projectId = phase.getProject().getId();
        boolean toStart = PhasesHelper.checkPhaseStatus(phase.getPhaseStatus());
        ProjectManager projectManager = getManagerHelper().getProjectManager();

        prHelper.processScreeningPR(projectId, toStart, operator);
            
        if (!toStart) {
            if (PRHelper.isStudioProject(projectManager, projectId)) {
                try {
                    notification.sendEmailToSubmitters(projectManager.getProject(projectId));
                } catch (Exception e) {
                    throw new PhaseHandlingException("Failed to send email to submitters on Screening results", e);
                }
            }
        }
    }

}
