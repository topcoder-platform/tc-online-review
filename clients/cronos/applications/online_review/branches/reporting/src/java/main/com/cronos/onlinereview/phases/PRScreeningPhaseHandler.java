/**
 * Copyright (C) 2005 - 2011 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import java.sql.Connection;
import java.sql.SQLException;

import com.topcoder.management.phase.OperationCheckResult;
import com.topcoder.management.phase.PhaseHandlingException;
import com.topcoder.project.phases.Phase;

/**
 * The extend from ScreeningPhaseHandler to add on the logic to push data to project_result.
 *
 * <p>
 * Version 1.1 (Online Review Replatforming Release 2) Change notes:
 *   <ol>
 *     <li>Update {@link #perform(Phase, String)} to send screening result notification emails for studio contest.</li>
 *   </ol>
 * </p>
 *
 * @author brain_cn, TCSASSEMBER
 * @version 1.1
 */
public class PRScreeningPhaseHandler extends ScreeningPhaseHandler {
    /**
     * Represents the <code>ScreeningResultNotification</code> instance to send notification email.
     * 
     * @since 1.1
     */
    private final ScreeningResultNotification notification;
    
    /**
     * Create a new instance of ScreeningPhaseHandler using the default namespace for loading configuration settings.
     *
     * @throws ConfigurationException if errors occurred while loading configuration settings.
     */
	public PRScreeningPhaseHandler() throws ConfigurationException {
		super();
		notification = new ScreeningResultNotification(DEFAULT_NAMESPACE, "Contest Submission", "Screening", "Failed Screening");
	}

    /**
     * Create a new instance of ScreeningPhaseHandler using the given namespace for loading configuration settings.
     *
     * @param namespace the namespace to load configuration settings from.
     * @throws ConfigurationException if errors occurred while loading configuration settings or required properties
     * missing.
     * @throws IllegalArgumentException if the input is null or empty string.
     */
	public PRScreeningPhaseHandler(String namespace) throws ConfigurationException {
		super(namespace);
		notification = new ScreeningResultNotification(namespace, "Contest Submission", "Screening", "Failed Screening");
	}

    /**
     * Check if the input phase can be executed or not. Just call super method.</p>
     *
     * @param phase The input phase to check.
     *
     * @return True if the input phase can be executed, false otherwise.
     *
     * @throws PhaseNotSupportedException if the input phase type is not "Submission" type.
     * @throws PhaseHandlingException if there is any error occurred while processing the phase.
     * @throws IllegalArgumentException if the input is null.
     */
    public OperationCheckResult canPerform(Phase phase) throws PhaseHandlingException {
    	return super.canPerform(phase);
    }

    /**
     * Provides additional logic to execute a phase. this exetension will update valid_submission_ind, submit_timestamp
     * field of project_result table.</p>
     *
     * @param phase The input phase to check.
     * @param operator The operator that execute the phase.
     *
     * @throws PhaseNotSupportedException if the input phase type is not "Submission" type.
     * @throws PhaseHandlingException if there is any error occurred while processing the phase.
     * @throws IllegalArgumentException if the input parameters is null or empty string.
     */
    public void perform(Phase phase, String operator) throws PhaseHandlingException {
    	super.perform(phase, operator);
        boolean toStart = PhasesHelper.checkPhaseStatus(phase.getPhaseStatus());

    	Connection conn = this.createConnection();
    	try {
    		processPR(phase.getProject().getId(), conn, toStart);
    		
    		if (!toStart) {
    		    long projectId = phase.getProject().getId();
    		    if (PRHelper.isStudioProject(projectId)) {
    		        try {
    	                notification.sendEmailToSubmitters(getManagerHelper().getProjectManager().getProject(projectId));
    	            } catch (Exception e) {
    	                throw new PhaseHandlingException("Failed to send email to submitters on Screening results", 
    	                                                 e);
    	            }
    		    }
    		}
    	} finally {
    		PRHelper.close(conn);
    	}
    }

    /**
     * Pull data to project_result.
     * 
     * @param projectId the projectId
     * @throws PhaseHandlingException if error occurs
     */
    public void processPR(long projectId, Connection conn, boolean toStart) throws PhaseHandlingException {
    	try {
    		PRHelper.processScreeningPR(projectId, conn, toStart);
    	} catch(SQLException e) {
    		throw new PhaseHandlingException("Failed to push data to project_result", e);
    	}
    }
}
