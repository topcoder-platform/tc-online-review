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

import com.topcoder.management.phase.PhaseHandlingException;
import com.topcoder.project.phases.Phase;

/**
 * The extend from RegistrationPhaseHandler to add on the logic to insert data to project_result.
 *
 * @author brain_cn
 * @version 1.0
 */
public class PRRegistrationPhaseHandler extends RegistrationPhaseHandler {
	private static final int RESOURCE_INFO_EXTERNAL_ID = 1;	
	private static final String SELECT_SUBMITTERS_STMT = 
				"SELECT value from resource_info ri, resource r " +
				"where ri.resource_id = r.resource_id " +
				"and r.project_id = ? and r.resource_role_id = 1 " +
				"and ri.resource_info_type_id = " + RESOURCE_INFO_EXTERNAL_ID;

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

        if (!toStart) {
        	Connection conn = this.createConnection();
        	try {
        		processPR(phase.getProject().getId(), conn);
        	} finally {
        		close(conn);
        	}
        }
    }

    /**
     * Pull data to project_result.
     * 
     * @param phaseId the phase id
     * @throws PhaseHandlingException if error occurs
     */
    public static void processPR(long projectId, Connection conn) throws PhaseHandlingException {
    	try {
        	List submitters = getSubmitters(conn, projectId);
        	for (Iterator iter = submitters.iterator(); iter.hasNext();) {
        		insertProjectResult(conn, iter.next().toString(), projectId);
        	}
    	} catch(SQLException e) {
    		throw new PhaseHandlingException("Failed to push data to project_result", e);
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
    private static List getSubmitters(Connection conn, long projectId) throws SQLException {
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
     * Insert project result for given user id and projectId.
     * 
     * @param conn the connection
     * @param userId the user id
     * @param projectId the project id
     * @throws SQLException if error occurs while executing sql statement
     */
    private static void insertProjectResult(Connection conn, String userId, long projectId) throws SQLException {
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
    	}
    }
}
