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
import java.util.Iterator;
import java.util.List;

/**
 * The PRHelper which is used to provide helper method for Phase Handler.
 *
 * @author brain_cn
 * @version 1.0
 */
class PRHelper {
	private static final int RESOURCE_INFO_EXTERNAL_ID = 1;	
	private static final int RESOURCE_INFO_INITIAL_SCORE_ID = 10;
	private static final int RESOURCE_INFO_FINAL_SCORE_ID = 11;
	private static final int RESOURCE_INFO_PLACED_ID = 12;
	private static final int SUBMISSION_STATUS_FAILED_SCREENING_ID = 2;
	private static final int SUBMISSION_STATUS_DELETE_ID = 5;

	private static final String APPEAL_RESPONSE_SELECT_STMT = 
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

	private static final String APPEAL_RESPONSE_UPDATE_PROJECT_RESULT_STMT = 
				"update project_result set final_score = ?, placed = ?, passed_review_ind = ?  " +
				"where project_id = ? and user_id = ? ";

	private static final String REVIEW_SELECT_STMT = 
				"select ri_s.value as raw_score, ri_u.value as user_id, r.project_id " +
				"from resource r " +
				"inner join resource_info ri_u " +
				"on r.resource_id = ri_u.resource_id " +
				"and ri_u.resource_info_type_id = " + RESOURCE_INFO_EXTERNAL_ID + " " +
				"inner join resource_info ri_s " +
				"on r.resource_id = ri_s.resource_id " +
				"and ri_s.resource_info_type_id =  " + RESOURCE_INFO_INITIAL_SCORE_ID + " " +
				"where r.project_id = ? ";

	private static final String REVIEW_UPDATE_PROJECT_RESULT_STMT = 
				"update project_result set raw_score = ?  " +
				"where project_id = ? and user_id = ? ";
	private static final String FAILED_PASS_SCREENING_STMT = 
				"update project_result set valid_submission_ind = 0, reliability_ind = 0, rating_ind = 0 " +
				"where exists(select * from submission s " +
				"	inner join upload u on u.upload_id = s.upload_id and u.upload_type_id = 1 " +
				"	and u.project_id = project_result.project_id " +
				"	inner join resource r on r.resource_id = u.resource_id " +
				"	inner join resource_info ri on ri.resource_id = r.resource_id " +
				"	and ri.value = project_result.user_id and ri.resource_info_type_id = " + RESOURCE_INFO_EXTERNAL_ID +
				"	where submission_status_id = " + SUBMISSION_STATUS_FAILED_SCREENING_ID + " ) and " +
				" project_id = ?";

	private static final String PASS_SCREENING_STMT = 
		"update project_result set valid_submission_ind = 1, reliability_ind = 1, rating_ind = 1 " +
		"where exists(select * from submission s " +
		"	inner join upload u on u.upload_id = s.upload_id and u.upload_type_id = 1  " +
		"	and u.project_id = project_result.project_id " +
		"	inner join resource r on r.resource_id = u.resource_id " +
		"	inner join resource_info ri on ri.resource_id = r.resource_id " +
		"	and ri.value = project_result.user_id and ri.resource_info_type_id = " + RESOURCE_INFO_EXTERNAL_ID +
		"	where submission_status_id not in (" + SUBMISSION_STATUS_FAILED_SCREENING_ID + "," + SUBMISSION_STATUS_DELETE_ID  + ") ) and " +
		" project_id = ?";
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
	private static final String SELECT_SUBMITTERS_STMT = 
		"SELECT value from resource_info ri, resource r " +
		"where ri.resource_id = r.resource_id " +
		"and r.project_id = ? and r.resource_role_id = 1 " +
		"and ri.resource_info_type_id = " + RESOURCE_INFO_EXTERNAL_ID;
	
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
    	List submitters = getSubmitters(conn, projectId);
    	for (Iterator iter = submitters.iterator(); iter.hasNext();) {
    		insertProjectResult(conn, iter.next().toString(), projectId);
    	}
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
    		// Ensure project_result exist
        	List submitters = getSubmitters(conn, projectId);
        	for (Iterator iter = submitters.iterator(); iter.hasNext();) {
        		insertProjectResult(conn, iter.next().toString(), projectId);
        	}

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
        		int placed = rs.getInt("placed");

        		// Update final score, placed and passed_review_ind
        		updateStmt.setDouble(1, finalScore);
        		updateStmt.setInt(2, placed);
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

    /**
     * Insert project result for given user id and projectId, do nothing if project result exist.
     * 
     * @param conn the connection
     * @param userId the user id
     * @param projectId the project id
     * @throws SQLException if error occurs while executing sql statement
     */
    static void insertProjectResult(Connection conn, String userId, long projectId) throws SQLException {
        PreparedStatement ps = null;
        ps = conn.prepareStatement("SELECT * FROM PROJECT_RESULT WHERE user_id = ? and project_id = ?");
        ps.setString(1, userId);
        ps.setLong(2, projectId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
        	// the project result is exsting
        	close(rs);
        	close(ps);
        	return;
        }

        close(rs);
        close(ps);
        
        // prepare rating/Reliability
        ps = conn.prepareStatement("SELECT rating from user_rating where user_id = ? and phase_id = " +
                "(select 111+project_category_id from project where project_id = ?)");
        ps.setString(1, userId);
        ps.setLong(2, projectId);
        rs = ps.executeQuery();

        double old_rating = 0;

        if (rs.next()) {
            old_rating = rs.getLong(1);
        }

        close(rs);
        close(ps);

        ps = conn.prepareStatement("SELECT rating from user_reliability where user_id = ? and phase_id = " +
                "(select 111+project_category_id from project where project_id = ?)");
        ps.setString(1, userId);
        ps.setLong(2, projectId);
        rs = ps.executeQuery();

        double oldReliability = 0;

        if (rs.next()) {
            oldReliability = rs.getDouble(1);
        }

        close(rs);
        close(ps);

        // add reliability_ind and old_reliability
        ps = conn.prepareStatement("INSERT INTO project_result " +
                "(project_id, user_id, rating_ind, reliability_ind, valid_submission_ind, old_rating, old_reliability) " +
                "values (?, ?, ?, ?, ?, ?, ?)");

        ps.setLong(1, projectId);
        ps.setString(2, userId);
        ps.setLong(3, 0);
        ps.setLong(4, 0);
        ps.setLong(5, 0);

        if (old_rating == 0) {
            ps.setNull(6, Types.DOUBLE);
        } else {
            ps.setDouble(6, old_rating);
        }

        if (oldReliability == 0) {
            ps.setNull(7, Types.DOUBLE);
        } else {
            ps.setDouble(7, oldReliability);
        }

        ps.execute();
        close(ps);
    }

}
