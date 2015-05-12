/*
 * Copyright (C) 2005 - 2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import com.topcoder.management.phase.PhaseHandlingException;
import com.topcoder.management.project.PersistenceException;
import com.topcoder.management.project.ProjectManager;
import com.topcoder.project.phases.Phase;

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
     * Create a new instance of RegistrationPhaseHandler using the default namespace for loading configuration settings.
     *
     * @throws ConfigurationException if errors occurred while loading configuration settings.
     */
    public PRRegistrationPhaseHandler() throws ConfigurationException {
        super();
    }

    /**
     * Create a new instance of RegistrationPhaseHandler using the given namespace for loading configuration settings.
     *
     * @param namespace the namespace to load configuration settings from.
     * @throws ConfigurationException if errors occurred while loading configuration settings or required properties
     * missing.
     * @throws IllegalArgumentException if the input is null or empty string.
     */
    public PRRegistrationPhaseHandler(String namespace) throws ConfigurationException {
        super(namespace);
    }

    /**
     * Provides additional logic to execute a phase. this extension will insert data to project_result table.</p>
     *
     * @param phase The input phase to check.
     * @param operator The operator that execute the phase.
     *
     * @throws PhaseNotSupportedException if the input phase type is not "Registration" type.
     * @throws PhaseHandlingException if there is any error occurred while processing the phase.
     * @throws IllegalArgumentException if the input parameters is null or empty string.
     */
    public void perform(Phase phase, String operator) throws PhaseHandlingException {
        super.perform(phase, operator);
        boolean toStart = PhasesHelper.checkPhaseStatus(phase.getPhaseStatus());

        prHelper.processRegistrationPR(phase.getProject().getId(), toStart);

        try {
            ProjectManager projectManager = getManagerHelper().getProjectManager();
            com.topcoder.management.project.Project project = projectManager.getProject(phase.getProject().getId());
            AmazonSNSHelper.publishProjectUpdateEvent(project);
        } catch (PersistenceException e) {
            System.out.println(e);
            throw new PhaseHandlingException("Problem when retrieving project", e);
        }
    }
}
