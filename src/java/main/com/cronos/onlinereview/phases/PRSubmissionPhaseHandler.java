/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import com.topcoder.management.phase.PhaseHandlingException;
import com.topcoder.project.phases.Phase;

/**
 * The extend from submissionPhaseHandler to add on the logic to push data to project_result.
 *
 * @author brain_cn
 * @version 1.0
 */
public class PRSubmissionPhaseHandler extends SubmissionPhaseHandler {
	private static final int RESOURCE_INFO_EXTERNAL_ID = 1;
	private static final int SUBMISSION_STATUS_DELETE_ID = 5;
	private static final String UPDATE_PROJECT_RESULT_STMT = 
				"update project_result set valid_submission_ind = 0 " +
				"where not exists(select * from submission s " +
				"	inner join upload u on u.upload_id = s.upload_id and upload_type_id = 1  " +
				"	and u.project_id = project_result.project_id " +
				"	inner join resource r on r.resource_id = u.resource_id " +
				"	inner join resource_info ri on ri.resource_id = r.resource_id " +
				"	and ri.value = project_result.user_id and ri.resource_info_type_id = " + RESOURCE_INFO_EXTERNAL_ID +
				"	where submission_status_id <> " + SUBMISSION_STATUS_DELETE_ID + " ) and " +
				" project_id = ?";

    /**
     * Create a new instance of SubmissionPhaseHandler using the default namespace for loading configuration settings.
     *
     * @throws ConfigurationException if errors occurred while loading configuration settings.
     */
	public PRSubmissionPhaseHandler() throws ConfigurationException {
		super();
	}

    /**
     * Create a new instance of SubmissionPhaseHandler using the given namespace for loading configuration settings.
     *
     * @param namespace the namespace to load configuration settings from.
     * @throws ConfigurationException if errors occurred while loading configuration settings or required properties
     * missing.
     * @throws IllegalArgumentException if the input is null or empty string.
     */
	public PRSubmissionPhaseHandler(String namespace) throws ConfigurationException {
		super(namespace);
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
    public boolean canPerform(Phase phase) throws PhaseHandlingException {
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

        if (!toStart) {
        	Connection conn = this.createConnection();
        	try {
        		processPR(phase.getProject().getId(), conn);
        	} finally {
        		PRHelper.close(conn);
        	}
        }
    }

    /**
     * Pull data to project_result.
     * 
     * @param projectId the projectId
     * @throws PhaseHandlingException if error occurs
     */
    public static void processPR(long projectId, Connection conn) throws PhaseHandlingException {
    	PreparedStatement pstmt = null;
    	try {
    		// Ensure project_result exist
        	List submitters = PRHelper.getSubmitters(conn, projectId);
        	for (Iterator iter = submitters.iterator(); iter.hasNext();) {
        		PRHelper.insertProjectResult(conn, iter.next().toString(), projectId);
        	}

        	// Update all users who submit submission
        	pstmt = conn.prepareStatement(UPDATE_PROJECT_RESULT_STMT);
        	pstmt.setLong(1, projectId);
        	pstmt.execute();
    	} catch(SQLException e) {
    		throw new PhaseHandlingException("Failed to push data to project_result", e);
    	} finally {
    		PRHelper.close(pstmt);
    	}
    }
}
