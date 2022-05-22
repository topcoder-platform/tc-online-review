/*
 * Copyright (C) 2005 - 2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import com.topcoder.onlinereview.component.project.management.PersistenceException;
import com.topcoder.onlinereview.component.project.management.Project;
import com.topcoder.onlinereview.component.project.management.ProjectManager;
import com.topcoder.onlinereview.component.project.phase.ManagerHelper;
import com.topcoder.onlinereview.component.project.phase.OperationCheckResult;
import com.topcoder.onlinereview.component.project.phase.Phase;
import com.topcoder.onlinereview.component.project.phase.PhaseHandlingException;
import com.topcoder.onlinereview.component.project.phase.handler.EmailOptions;
import com.topcoder.onlinereview.component.project.phase.handler.EmailScheme;
import com.topcoder.onlinereview.component.project.phase.handler.PhasesHelper;
import com.topcoder.onlinereview.component.project.phase.handler.SubmissionPhaseHandler;

import java.util.List;

/**
 * The extend from submissionPhaseHandler to add on the logic to push data to project_result.
 *
 * <p>
 * Thread-safety: This class is thread-safe as it does not contain any mutable inner state.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class PRSubmissionPhaseHandler extends SubmissionPhaseHandler {

    /**
    * Used for pulling data to project_result table and filling payments.
    */
    private final PRHelper prHelper = new PRHelper();
    
    /**
     * Create a new instance of SubmissionPhaseHandler using the given namespace for loading configuration settings.
     *
     * @throws IllegalArgumentException if the input is null or empty string.
     */
    public PRSubmissionPhaseHandler(ManagerHelper managerHelper,
                                    List<EmailScheme> emailSchemes,
                                    EmailScheme reviewFeedbackEmailScheme,
                                    EmailOptions defaultStartEmailOption,
                                    EmailOptions defaultEndEmailOption) {
        super(managerHelper, emailSchemes, reviewFeedbackEmailScheme, defaultStartEmailOption, defaultEndEmailOption);
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

        prHelper.processSubmissionPR(phase.getProject().getId(), toStart);

        try {
            ProjectManager projectManager = getManagerHelper().getProjectManager();
            Project project = projectManager.getProject(phase.getProject().getId());
            AmazonSNSHelper.publishProjectUpdateEvent(project);
        } catch (PersistenceException e) {
            throw new PhaseHandlingException("Problem when retrieving project", e);
        }
    }
}
