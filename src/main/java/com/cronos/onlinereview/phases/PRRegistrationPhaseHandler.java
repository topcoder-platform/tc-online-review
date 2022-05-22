/*
 * Copyright (C) 2005 - 2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import com.topcoder.onlinereview.component.project.management.PersistenceException;
import com.topcoder.onlinereview.component.project.management.Project;
import com.topcoder.onlinereview.component.project.management.ProjectManager;
import com.topcoder.onlinereview.component.project.phase.ManagerHelper;
import com.topcoder.onlinereview.component.project.phase.Phase;
import com.topcoder.onlinereview.component.project.phase.PhaseHandlingException;
import com.topcoder.onlinereview.component.project.phase.handler.EmailOptions;
import com.topcoder.onlinereview.component.project.phase.handler.EmailScheme;
import com.topcoder.onlinereview.component.project.phase.handler.PhasesHelper;
import com.topcoder.onlinereview.component.project.phase.handler.RegistrationPhaseHandler;

import java.util.List;

/**
 * The extend from RegistrationPhaseHandler to add on the logic to insert data to project_result.
 *
 * <p>
 * Thread-safety: This class is thread-safe as it does not contain any mutable inner state.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class PRRegistrationPhaseHandler extends RegistrationPhaseHandler {
    
    /**
    * Used for pulling data to project_result table and filling payments.
    */
    private final PRHelper prHelper = new PRHelper();
    
    /**
     * Create a new instance of RegistrationPhaseHandler using the given namespace for loading configuration settings.
     *
     * @throws IllegalArgumentException if the input is null or empty string.
     */
    public PRRegistrationPhaseHandler(ManagerHelper managerHelper,
                                      List<EmailScheme> emailSchemes,
                                      EmailScheme reviewFeedbackEmailScheme,
                                      EmailOptions defaultStartEmailOption,
                                      EmailOptions defaultEndEmailOption) {
        super(managerHelper, emailSchemes, reviewFeedbackEmailScheme, defaultStartEmailOption, defaultEndEmailOption);
    }

    /**
     * Provides additional logic to execute a phase. this extension will insert data to project_result table.</p>
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

        prHelper.processRegistrationPR(phase.getProject().getId(), toStart);

        try {
            ProjectManager projectManager = getManagerHelper().getProjectManager();
            Project project = projectManager.getProject(phase.getProject().getId());
            AmazonSNSHelper.publishProjectUpdateEvent(project);
        } catch (PersistenceException e) {
            System.out.println(e);
            throw new PhaseHandlingException("Problem when retrieving project", e);
        }
    }
}
