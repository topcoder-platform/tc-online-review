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
	private static final int RESOURCE_INFO_EXTERNAL_ID = 1;	
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
    	PRHelper.close(rs);
    	PRHelper.close(pstmt);
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
        	PRHelper.close(rs);
        	PRHelper.close(ps);
        	return;
        }

        PRHelper.close(rs);
        PRHelper.close(ps);
        
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

        PRHelper.close(rs);
        PRHelper.close(ps);

        ps = conn.prepareStatement("SELECT rating from user_reliability where user_id = ? and phase_id = " +
                "(select 111+project_category_id from project where project_id = ?)");
        ps.setString(1, userId);
        ps.setLong(2, projectId);
        rs = ps.executeQuery();

        double oldReliability = 0;

        if (rs.next()) {
            oldReliability = rs.getDouble(1);
        }

        PRHelper.close(rs);
        PRHelper.close(ps);

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
        PRHelper.close(ps);
    }

}
