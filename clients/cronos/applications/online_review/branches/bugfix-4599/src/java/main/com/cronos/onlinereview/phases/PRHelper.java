/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * The PRHelper which is used to provide helper method for Phase Handler.
 *
 * @author brain_cn
 * @version 1.0
 */
class PRHelper {
	private static final String APPEAL_RESPONSE_SELECT_STMT = 
				"select ri_s.value as final_score, ri_u.value as user_id, ri_p.value as placed, r.project_id, s.submission_status_id " +
				"from resource r, resource_info ri_u,resource_info ri_s,resource_info ri_p,upload u,submission s " +
				"where  r.resource_id = ri_u.resource_id " +
				"and ri_u.resource_info_type_id = 1 " +
				"and r.resource_id = ri_s.resource_id " +
				"and ri_s.resource_info_type_id = 11 " +
				"and r.resource_id = ri_p.resource_id " +
				"and ri_p.resource_info_type_id = 12 " +
				"and u.project_id = r.project_id and u.resource_id = r.resource_id and upload_type_id = 1 " +
				"and u.upload_id = s.upload_id and r.project_id = ? and r.resource_role_id = 1 ";

	private static final String APPEAL_RESPONSE_UPDATE_PROJECT_RESULT_STMT = 
				"update project_result set final_score = ?, placed = ?, passed_review_ind = ?  " +
				"where project_id = ? and user_id = ? ";

	private static final String PLACED_FINALSCORE_UPDATE_PROJECT_RESULT_STMT = 
				"update project_result set final_score = ?, placed = ?  " +
				"where project_id = ? and user_id = ? ";

	private static final String REVIEW_SELECT_STMT = 
				"select ri_s.value as raw_score, ri_u.value as user_id, r.project_id " +
				"from resource r, resource_info ri_u,resource_info ri_s  " +
				"where r.resource_id = ri_s.resource_id and ri_s.resource_info_type_id = 10 " +
				" and r.resource_id = ri_u.resource_id and ri_u.resource_info_type_id = 1 and r.project_id = ? ";

	private static final String REVIEW_UPDATE_PROJECT_RESULT_STMT = 
				"update project_result set raw_score = ?  " +
				"where project_id = ? and user_id = ? ";
	private static final String FAILED_PASS_SCREENING_STMT = 
				"update project_result set valid_submission_ind = 0, rating_ind = 0 " +
				"where exists(select * from submission s,upload u,resource r,resource_info ri   " +
				"	where u.upload_id = s.upload_id and u.upload_type_id = 1 " +
				"	and u.project_id = project_result.project_id " +
				"	and r.resource_id = u.resource_id " +
				"	and ri.resource_id = r.resource_id " +
				"	and ri.value = project_result.user_id and ri.resource_info_type_id = 1" + 
				"	and submission_status_id = 2 ) and " +
				" project_id = ?";

	private static final String PASS_SCREENING_STMT = 
		"update project_result set valid_submission_ind = 1, rating_ind = 1 " +
		"where exists(select * from submission s,upload u,resource r,resource_info ri    " +
		"	where u.upload_id = s.upload_id and u.upload_type_id = 1  " +
		"	and u.project_id = project_result.project_id " +
		"	and r.resource_id = u.resource_id " +
		"	and ri.resource_id = r.resource_id " +
		"	and ri.value = project_result.user_id and ri.resource_info_type_id = 1 " + 
		"	and submission_status_id not in (2,5) ) and " +
		" project_id = ?";
	
	private static final String UPDATE_PROJECT_RESULT_STMT = 
				"update project_result set valid_submission_ind = 0 " +
				"where not exists(select * from submission s,upload u,resource r,resource_info ri  " +
				"	where u.upload_id = s.upload_id and upload_type_id = 1  " +
				"	and u.project_id = project_result.project_id " +
				"	and r.resource_id = u.resource_id " +
				"	and ri.resource_id = r.resource_id " +
				"	and ri.value = project_result.user_id and ri.resource_info_type_id = 1 " + 
				"	and submission_status_id <> 5 ) and " +
				" project_id = ?";
	
	private static final String SELECT_SUBMITTERS_STMT = 
		"SELECT value from resource_info ri, resource r " +
		"where ri.resource_id = r.resource_id " +
		"and r.project_id = ? and r.resource_role_id = 1 " +
		"and ri.resource_info_type_id = 1";
	
	/**
	 * Prevent to be created outside.
	 */
	private PRHelper() {		
	}

    /**
     * Pull data to project_result.
     * 
     * @param phaseId the phase id
     * @throws PhaseHandlingException if error occurs
     */
    static void processRegistrationPR(long projectId, Connection conn) throws SQLException {
    }

    /**
     * Pull data to project_result.
     * 
     * @param projectId the projectId
     * @throws PhaseHandlingException if error occurs
     */
    static void processSubmissionPR(long projectId, Connection conn) throws SQLException {
    	PreparedStatement pstmt = null;
    	try {
        	// Update all users who submit submission
        	pstmt = conn.prepareStatement(UPDATE_PROJECT_RESULT_STMT);
        	pstmt.setLong(1, projectId);
        	pstmt.execute();
    	} finally {
    		close(pstmt);
    	}
    }

    /**
     * Pull data to project_result.
     * 
     * @param projectId the projectId
     * @throws PhaseHandlingException if error occurs
     */
    static void processScreeningPR(long projectId, Connection conn) throws SQLException {
    	PreparedStatement pstmt = null;
    	try {
        	// Update all users who failed to pass screen, set valid_submission_ind = 0
        	pstmt = conn.prepareStatement(FAILED_PASS_SCREENING_STMT);
        	pstmt.setLong(1, projectId);
        	pstmt.execute();
        	close(pstmt);

        	// Update all users who pass screen, set valid_submission_ind = 1
        	pstmt = conn.prepareStatement(PASS_SCREENING_STMT);
        	pstmt.setLong(1, projectId);
        	pstmt.execute();
    	} finally {
    		close(pstmt);
    	}    	
    }

    /**
     * Pull data to project_result.
     * 
     * @param projectId the projectId
     * @param phaseId the phase id
     * @throws PhaseHandlingException if error occurs
     */
    static void processReviewPR(long projectId, Connection conn) throws SQLException {
    	PreparedStatement pstmt = null;
    	PreparedStatement updateStmt = null;
    	ResultSet rs = null;
    	try {
        	// Retrieve all 
        	pstmt = conn.prepareStatement(REVIEW_SELECT_STMT);
        	pstmt.setLong(1, projectId);
        	rs = pstmt.executeQuery();

        	updateStmt = conn.prepareStatement(REVIEW_UPDATE_PROJECT_RESULT_STMT);
        	while(rs.next()) {
	        	// Update all raw score
        		double rawScore = rs.getDouble("raw_score");
        		long userId = rs.getLong("user_id");
        		updateStmt.setDouble(1, rawScore);
        		updateStmt.setLong(2, projectId);
        		updateStmt.setLong(3, userId);
        		updateStmt.execute();
        	}
    	} finally {
    		close(rs);
    		close(pstmt);
    		close(updateStmt);
    	}
    }

    /**
     * Pull data to project_result.
     * 
     * @param projectId the projectId
     * @throws PhaseHandlingException if error occurs
     */
    static void processAppealResponsePR(long projectId, Connection conn) throws SQLException {
    	PreparedStatement pstmt = null;
    	PreparedStatement updateStmt = null;
    	ResultSet rs = null;
    	try {
        	// Retrieve all 
        	pstmt = conn.prepareStatement(APPEAL_RESPONSE_SELECT_STMT);
        	pstmt.setLong(1, projectId);
        	rs = pstmt.executeQuery();

        	updateStmt = conn.prepareStatement(APPEAL_RESPONSE_UPDATE_PROJECT_RESULT_STMT);
        	while(rs.next()) {
        		double finalScore = rs.getDouble("final_score");
        		long userId = rs.getLong("user_id");
        		int status = rs.getInt("submission_status_id");
        		String p = rs.getString("placed");
        		int placed = 0;
        		if (p != null) {
        			try {
        				placed = Integer.parseInt(p);
        			} catch (Exception e) {
        				// Ignore
        			}
        		}

        		// Update final score, placed and passed_review_ind
        		updateStmt.setDouble(1, finalScore);
        		if (placed == 0) {
        			updateStmt.setNull(2, Types.INTEGER);
        		} else {
        			updateStmt.setInt(2, placed);
        		}
        		// 1 is active, 4 is Completed Without Win
        		updateStmt.setInt(3, status == 1 || status == 4 ? 1 : 0);
        		updateStmt.setLong(4, projectId);
        		updateStmt.setLong(5, userId);
        		updateStmt.execute();
        	}
    	} finally {
    		close(rs);
    		close(pstmt);
    		close(updateStmt);
    	}
    }

    /**
     * Pull data to project_result.
     * 
     * @param projectId the projectId
     * @throws PhaseHandlingException if error occurs
     */
    static void processPlacedFinalScore(long projectId, Connection conn) throws SQLException {
    	PreparedStatement pstmt = null;
    	PreparedStatement updateStmt = null;
    	ResultSet rs = null;
    	try {
        	// Retrieve all 
        	pstmt = conn.prepareStatement(APPEAL_RESPONSE_SELECT_STMT);
        	pstmt.setLong(1, projectId);
        	rs = pstmt.executeQuery();

        	updateStmt = conn.prepareStatement(PLACED_FINALSCORE_UPDATE_PROJECT_RESULT_STMT);
        	while(rs.next()) {
        		double finalScore = rs.getDouble("final_score");
        		long userId = rs.getLong("user_id");
        		String p = rs.getString("placed");
        		int placed = 0;
        		if (p != null) {
        			try {
        				placed = Integer.parseInt(p);
        			} catch (Exception e) {
        				// Ignore
        			}
        		}

        		// Update final score, placed and passed_review_ind
        		updateStmt.setDouble(1, finalScore);
        		if (placed == 0) {
        			updateStmt.setNull(2, Types.INTEGER);
        		} else {
        			updateStmt.setInt(2, placed);
        		}
        		updateStmt.setLong(3, projectId);
        		updateStmt.setLong(4, userId);
        		updateStmt.execute();
        	}
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
    static void close(Object obj) {
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
    	}
    }

    /**
     * Retrieve all submitters's external ids.
     * 
     * @param conn the connection
     * @param projectId the project id
     * @return all submitters external ids
     * @throws SQLException if error occurs while executing sql statement
     */
    static List getSubmitters(Connection conn, long projectId) throws SQLException {
    	List submitters = new ArrayList();
    	PreparedStatement pstmt = conn.prepareStatement(SELECT_SUBMITTERS_STMT);
    	pstmt.setLong(1, projectId);
    	ResultSet rs = pstmt.executeQuery();
    	while (rs.next()) {
    		submitters.add(rs.getString("value"));
    	}
    	close(rs);
    	close(pstmt);
    	return submitters;
    }
}
