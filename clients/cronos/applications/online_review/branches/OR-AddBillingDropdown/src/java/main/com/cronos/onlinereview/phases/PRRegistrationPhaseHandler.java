/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import java.sql.Connection;
import java.sql.SQLException;

import com.topcoder.management.phase.PhaseHandlingException;
import com.topcoder.project.phases.Phase;

/**
 * The extend from RegistrationPhaseHandler to add on the logic to insert data to project_result.
 *
 * @author brain_cn
 * @version 1.0
 */
public class PRRegistrationPhaseHandler extends RegistrationPhaseHandler {

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
     * Provides additional logic to execute a phase. this exetension will insert data to project_result table.</p>
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

    	Connection conn = this.createConnection();
    	try {
    		processPR(phase.getProject().getId(), conn, toStart);
    	} finally {
    		PRHelper.close(conn);
    	}
    }

    /**
     * Pull data to project_result.
     * 
     * @param phaseId the phase id
     * @throws PhaseHandlingException if error occurs
     */
    public void processPR(long projectId, Connection conn, boolean toStart) throws PhaseHandlingException {
    	try {
        	PRHelper.processRegistrationPR(projectId, conn, toStart);
    	} catch(SQLException e) {
    		throw new PhaseHandlingException("Failed to push data to project_result", e);
    	}
    }
}
