/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.topcoder.management.phase.PhaseHandlingException;
import com.topcoder.project.phases.Phase;

/**
 * The extend from ReviewPhaseHandler to add on the logic to push data to project_result.
 *
 * @author brain_cn
 * @version 1.0
 */
public class PRReviewPhaseHandler extends ReviewPhaseHandler {
	private static final int RESOURCE_INFO_EXTERNAL_ID = 1;
	private static final int RESOURCE_INFO_INITIAL_SCORE_ID = 10;

	private static final String SELECT_STMT = 
				"select ri_s.value as raw_score, ri_u.value as user_id, r.project_id " +
				"from resource r " +
				"inner join resource_info ri_u " +
				"on r.resource_id = ri_u.resource_id " +
				"and ri_u.resource_info_type_id = " + RESOURCE_INFO_EXTERNAL_ID + " " +
				"inner join resource_info ri_s " +
				"on r.resource_id = ri_s.resource_id " +
				"and ri_s.resource_info_type_id =  " + RESOURCE_INFO_INITIAL_SCORE_ID + " " +
				"where r.project_id = ? ";

	private static final String UPDATE_PROJECT_RESULT_STMT = 
				"update project_result set raw_score = ?  " +
				"where project_id = ? and user_id = ? ";

    /**
     * Create a new instance of ReviewPhaseHandler using the default namespace for loading configuration settings.
     *
     * @throws ConfigurationException if errors occurred while loading configuration settings.
     */
	public PRReviewPhaseHandler() throws ConfigurationException {
		super();
	}

    /**
     * Create a new instance of ReviewPhaseHandler using the given namespace for loading configuration settings.
     *
     * @param namespace the namespace to load configuration settings from.
     * @throws ConfigurationException if errors occurred while loading configuration settings or required properties
     * missing.
     * @throws IllegalArgumentException if the input is null or empty string.
     */
	public PRReviewPhaseHandler(String namespace) throws ConfigurationException {
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
     * @param phaseId the phase id
     * @throws PhaseHandlingException if error occurs
     */
    public static void processPR(long projectId, Connection conn) throws PhaseHandlingException {
    	PreparedStatement pstmt = null;
    	PreparedStatement updateStmt = null;
    	ResultSet rs = null;
    	try {
        	// Retrieve all 
        	pstmt = conn.prepareStatement(SELECT_STMT);
        	pstmt.setLong(1, projectId);
        	rs = pstmt.executeQuery();

        	updateStmt = conn.prepareStatement(UPDATE_PROJECT_RESULT_STMT);
        	while(rs.next()) {
	        	// Update all raw score
        		float rawScore = rs.getFloat("raw_score");
        		long userId = rs.getLong("user_id");
        		updateStmt.setFloat(1, rawScore);
        		updateStmt.setLong(2, projectId);
        		updateStmt.setLong(3, userId);
        		updateStmt.execute();
        	}
    	} catch(SQLException e) {
    		throw new PhaseHandlingException("Failed to push data to project_result", e);
    	} finally {
    		PRHelper.close(rs);
    		PRHelper.close(pstmt);
    		PRHelper.close(updateStmt);
    	}
    }
}
