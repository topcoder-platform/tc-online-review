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
 * The extend from AppealsResponsePhaseHandler to add on the logic to push data to project_result.
 *
 * @author brain_cn
 * @version 1.0
 */
public class PRAppealResponsePhaseHandler extends AppealsResponsePhaseHandler {
	private static final int RESOURCE_INFO_EXTERNAL_ID = 1;
	private static final int RESOURCE_INFO_FINAL_SCORE_ID = 11;
	private static final int RESOURCE_INFO_PLACED_ID = 12;

	private static final String SELECT_STMT = 
				"select ri_s.value as final_score, ri_u.value as user_id, round(ri_p.value) as placed, r.project_id, s.submission_status_id " +
				"from resource r " +
				"inner join resource_info ri_u " +
				"on r.resource_id = ri_u.resource_id " +
				"and ri_u.resource_info_type_id = " + RESOURCE_INFO_EXTERNAL_ID + " " +
				"inner join resource_info ri_s " +
				"on r.resource_id = ri_s.resource_id " +
				"and ri_s.resource_info_type_id =  " + RESOURCE_INFO_FINAL_SCORE_ID + " " +
				"inner join resource_info ri_p " +
				"on r.resource_id = ri_p.resource_id " +
				"and ri_p.resource_info_type_id =  " + RESOURCE_INFO_PLACED_ID + " " +
				"inner join upload u on u.project_id = r.project_id and u.resource_id = r.resource_id and upload_type_id = 1 " +
				"inner join submission s on u.upload_id = s.upload_id " +
				"where r.project_id = ? and r.resource_role_id = 1 ";

	private static final String UPDATE_PROJECT_RESULT_STMT = 
				"update project_result set final_score = ?, placed = ?, passed_review_ind = ?  " +
				"where project_id = ? and user_id = ? ";

    /**
     * Create a new instance of AppealsResponsePhaseHandler using the default namespace for loading configuration settings.
     *
     * @throws ConfigurationException if errors occurred while loading configuration settings.
     */
	public PRAppealResponsePhaseHandler() throws ConfigurationException {
		super();
	}

    /**
     * Create a new instance of AppealsResponsePhaseHandler using the given namespace for loading configuration settings.
     *
     * @param namespace the namespace to load configuration settings from.
     * @throws ConfigurationException if errors occurred while loading configuration settings or required properties
     * missing.
     * @throws IllegalArgumentException if the input is null or empty string.
     */
	public PRAppealResponsePhaseHandler(String namespace) throws ConfigurationException {
		super(namespace);
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
        	// Only will perform while submission phase is ended
        	Connection conn = this.createConnection();
        	try {
        		processPR(phase.getId(), conn);
        	} finally {
        		close(conn);
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
    	PreparedStatement updateStmt = null;
    	ResultSet rs = null;
    	try {
        	// Retrieve all 
        	pstmt = conn.prepareStatement(SELECT_STMT);
        	pstmt.setLong(1, projectId);
        	rs = pstmt.executeQuery();

        	updateStmt = conn.prepareStatement(UPDATE_PROJECT_RESULT_STMT);
        	while(rs.next()) {
        		float finalScore = rs.getFloat("final_score");
        		long userId = rs.getLong("user_id");
        		int status = rs.getInt("submission_status_id");
        		int placed = rs.getInt("placed");

        		// Update final score, placed and passed_review_ind
        		updateStmt.setFloat(1, finalScore);
        		updateStmt.setInt(2, placed);
        		// 1 is active, 4 is Completed Without Win
        		updateStmt.setInt(3, status == 1 || status == 4 ? 1 : 0);
        		updateStmt.setLong(4, projectId);
        		updateStmt.setLong(5, userId);
        		updateStmt.execute();
        	}
    	} catch(SQLException e) {
    		throw new PhaseHandlingException("Failed to push data to project_result", e);
    	} finally {
    		close(rs);
    		close(pstmt);
    		close(updateStmt);
    	}
    }

    /**
     * Close the jdbc resource.
     * 
     * @param obj the jdbc resource object
     */
    private static void close(Object obj) {
    	if (obj instanceof Connection) {
    		try {
    			((Connection) obj).close();
    		} catch(Exception e) {
    			// Just ignore
    		}
    	} else if (obj instanceof PreparedStatement) {
    		try {
    			((PreparedStatement) obj).close();
    		} catch(Exception e) {
    			// Just ignore
    		}
    	} else if (obj instanceof ResultSet) {
    		try {
    			((ResultSet) obj).close();
    		} catch(Exception e) {
    			// Just ignore
    		}
    	}
    }
}
