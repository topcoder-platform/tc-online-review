/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import com.topcoder.web.common.model.FixedPriceComponent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * The AutoPaymentUtil is used to auto-fill for payments of reviewers and submitters.
 *
 * @author brain_cn
 * @version 1.0
 */
public class AutoPaymentUtil {
    /** Retrieve the price from comp_version_date. */
    private static final String SELECT_PRICE_CVD = "select price " + 
    	"	from comp_version_dates, " +
        "	project_info pi, " + 
        "	project p" + 
        "	where p.project_id = pi.project_id" + 
        "	and comp_vers_id = pi.value" +
        "	and ((p.project_category_id = 1 and phase_id = 112)" +
        "		or (p.project_category_id = 2 and phase_id = 113))" + 
        "	and pi.project_info_type_id = 1 " +
        "	and pi.project_id = ? ";

    /** Retrieve the price from project_info. */
    private static final String SELECT_PRICE_PROJECT_INFO = 
    	"select value " + 
    	"	from project_info " +
        "	where project_info_type_id = 16 " + 
        "	and project_id = ? ";
    private static final String USER_ID = "phase_handler";

    public static final int SCREENING_PHASE = 3; 
    public static final int REVIEW_PHASE = 4;
    public static final int AGGREGATION_PHASE = 7;
    public static final int FINAL_REVIEW_PHASE = 10;

    /**
     * Prevent to be created.
     */
    private AutoPaymentUtil() {
    }
    
    static void resetProjectPrice(long projectId, Connection conn) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        double price = 0.0;

        try {
            pstmt = conn.prepareStatement(SELECT_PRICE_CVD);
            pstmt.setLong(1, projectId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
            	price = rs.getDouble(1);
            }
        } finally {
            PRHelper.close(rs);
            PRHelper.close(pstmt);
        }
    	
        if (price > 0) {
        	updateProjectInfo(projectId, 16, String.valueOf(price), conn);
        }
    }

    /**
     * Populate reviewers payment.
     *
     * @param projectId project id
     * @param conn the connection
     *
     * @throws Exception if error occurs
     */
    public static void populateReviewerPayments(long projectId, Connection conn, int phaseId)
        throws SQLException {
        long projectCategoryId = getProjectCategoryId(projectId, conn);

        if (projectCategoryId != 1 && projectCategoryId != 2) {
        	// Logic only apply to component
        	return;
        }

        int levelId = FixedPriceComponent.LEVEL1;
        int count = getCount(projectId, conn);
        int passedCount = getScreenPassedCount(projectId, conn);
        float[] payments = getPayments(projectId, projectCategoryId, conn);
        FixedPriceComponent fpc = new FixedPriceComponent(levelId, count, passedCount,
                (projectCategoryId == 1) ? 112 : 113, payments[0], payments[1]);
        List reviewers = getReviewers(projectId, conn);

        for (Iterator iter = reviewers.iterator(); iter.hasNext();) {
            Reviewer reviewer = (Reviewer) iter.next();

            if (reviewer.isPrimaryScreener() && phaseId == SCREENING_PHASE) {
                updateResourcePayment(reviewer.getResourceId(), fpc.getScreeningCost(), conn);
            } else if (reviewer.isScreener() && phaseId == SCREENING_PHASE) {
                updateResourcePayment(reviewer.getResourceId(), fpc.getScreeningCost(), conn);
            } else if (reviewer.isAggregator() && phaseId == AGGREGATION_PHASE) {
                updateResourcePayment(reviewer.getResourceId(), fpc.getAggregationCost(), conn);
            } else if (reviewer.isFinalReviewer() && phaseId == FINAL_REVIEW_PHASE) {
                updateResourcePayment(reviewer.getResourceId(), fpc.getFinalReviewCost(), conn);
            } else if (reviewer.isPrimaryReviewer() && phaseId == REVIEW_PHASE) {
                updateResourcePayment(reviewer.getResourceId(), fpc.getCoreReviewCost(), conn);
            } else if (reviewer.isReviewer() && phaseId == REVIEW_PHASE) {
                updateResourcePayment(reviewer.getResourceId(), fpc.getReviewPrice(), conn);
            }
        }
    }

    /**
     * Return submission count for given project.
     * 
     * @param projectId project id
     * @param conn the connection
     * @return screening passed count
     * @throws SQLException if error occurs
     */
    private static int getCount(long projectId, Connection conn)
        throws SQLException {
        String SELECT_SQL = 
        	"select count(s.submission_id) " +
	        "	from submission s, upload u " +
	        "	where u.upload_id = s.upload_id " +
	        "	and s.submission_status_id <> 5 " +
	        "	and upload_type_id = 1 " +
	        "	and u.project_id = ?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement(SELECT_SQL);
            pstmt.setLong(1, projectId);
            rs = pstmt.executeQuery();
            rs.next();
            return rs.getInt(1);
        } finally {
            PRHelper.close(rs);
            PRHelper.close(pstmt);
        }
    }

    /**
     * Return screening passed count for given project.
     * 
     * @param projectId project id
     * @param conn the connection
     * @return screening passed count
     * @throws SQLException if error occurs
     */
    private static int getScreenPassedCount(long projectId, Connection conn)
        throws SQLException {
        String SELECT_SQL = 
        	"select count(s.submission_id) " +
	        "	from submission s, upload u " +
	        "	where u.upload_id = s.upload_id " +
	        "	and s.submission_status_id in (1, 3, 4) " +
	        "	and upload_type_id = 1 " +
	        "	and u.project_id = ?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement(SELECT_SQL);
            pstmt.setLong(1, projectId);
            rs = pstmt.executeQuery();
            rs.next();
            return rs.getInt(1);
        } finally {
            PRHelper.close(rs);
            PRHelper.close(pstmt);
        }
    }

    /**
     * Return all reviewers for given project.
     *
     * @param projectId project id
     * @param conn connection
     *
     * @return all reviewers
     *
     * @throws SQLException if error occurs
     */
    private static List getReviewers(long projectId, Connection conn)
        throws SQLException {
        String SELECT_SQL = 
        	"select r.resource_id, " + 
        	"	resource_role_id, " + 
        	"	value " + 
        	"	from resource r, " +
            "	resource_info ri" + 
            "	where r.resource_id = ri.resource_id" + 
            "	and ri.resource_info_type_id = 1" +
            "	and r.resource_role_id in (2, 3, 4, 5, 6, 7, 8, 9)" + 
            "	and r.project_id = ? order by resource_role_id";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List reviewers = new ArrayList();
        long primaryScreener = 0;

        try {
            pstmt = conn.prepareStatement(SELECT_SQL);
            pstmt.setLong(1, projectId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Reviewer reviewer = new Reviewer(rs.getLong(1), rs.getLong(2), rs.getLong(3));
                reviewers.add(reviewer);

                if (reviewer.isPrimaryScreener()) {
                    primaryScreener = reviewer.getUserId();
                } else if (reviewer.isReviewer() && (reviewer.getUserId() == primaryScreener)) {
                    reviewer.setPrimaryReviewer(true);
                }
            }

            return reviewers;
        } finally {
            PRHelper.close(rs);
            PRHelper.close(pstmt);
        }
    }

    /**
     * Retrieve payment from rboard_payment table.
     *
     * @param projectId project id
     * @param projectCategoryId project category id
     * @param conn connection
     *
     * @return payments for primary and second payment
     *
     * @throws SQLException if error occurs
     */
    private static float[] getPayments(long projectId, long projectCategoryId, Connection conn)
        throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        float[] payments = new float[2];

        try {
            pstmt = conn.prepareStatement(
                    "SELECT primary_ind, amount FROM rboard_payment WHERE project_id = ? AND phase_id = ?");
            pstmt.setLong(1, projectId);
            pstmt.setLong(2, (projectCategoryId == 1) ? 112 : 113);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                if (rs.getInt("primary_ind") == 1) {
                    payments[0] = rs.getFloat("amount");
                } else {
                    payments[1] = rs.getFloat("amount");
                }
            }

            return payments;
        } finally {
            PRHelper.close(rs);
            PRHelper.close(pstmt);
        }
    }

    /**
     * Return project category id for given project id
     * 
     * @param projectId project id
     * @param conn the connection
     * @return project category id
     * @throws SQLException if error occurs
     */
    static long getProjectCategoryId(long projectId, Connection conn)
        throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement("SELECT project_category_id from project where project_id = ? ");
            pstmt.setLong(1, projectId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getLong(1);
            } else {
                return -1;
            }
        } finally {
            PRHelper.close(rs);
            PRHelper.close(pstmt);
        }
    }

    /**
     * Populate submitter payments.
     *
     * @param projectId project id
     * @param conn connection
     *
     * @throws SQLException if error occurs
     */
    static void populateSubmitterPayments(long projectId, Connection conn)
        throws SQLException {
        // Retrieve the price
        double price = getPriceByProjectId(projectId, conn);

        if (price == 0) {
            // No price is defined
            return;
        }

        String clearPayment = "update resource_info set value = 0 " + 
        					  " where resource_info_type_id = 7 " +
        					  " and resource_id in (select resource_id from resource where resource_role_id = 1 and project_id = ?)";

        PreparedStatement pstmt = conn.prepareStatement(clearPayment);
        pstmt.setLong(1, projectId);
        pstmt.executeUpdate();
        PRHelper.close(pstmt);

        // prepare price for differnt placed
        double[] prices = new double[] { price, Math.round(price * .5) };

        // Update the payment resource_info and paid resource_info
        long winnerRunnerCount = getWinnerRunnerCount(projectId, conn);
        
        if (winnerRunnerCount > 0) {
            long submitter = getResourceIdByPlaced(projectId, 1, conn);

            if (submitter > 0) {
                // Update the payment for given submitter which has placed    
                updateResourcePayment(submitter, prices[0], conn);
            }
        }

        if (winnerRunnerCount > 1) {
            long submitter = getResourceIdByPlaced(projectId, 2, conn);

            if (submitter > 0) {
                // Update the payment for given submitter which has placed    
                updateResourcePayment(submitter, prices[1], conn);
            }
        }
    }

    /**
     * Update the submitter payment with price.
     *
     * @param resourceId the resource id
     * @param price the price
     * @param conn DOCUMENT ME!
     *
     * @throws SQLException if error occurs
     */
    private static void updateResourcePayment(long resourceId, double price, Connection conn)
        throws SQLException {
        // Set price
        updateResourceInfo(resourceId, 7, String.valueOf(price), conn);

        // set to Not Paid
        updateResourceInfo(resourceId, 8, "No", conn);
    }

    /**
     * Update or insert resource info with given value.
     *
     * @param resourceId the resource id
     * @param resourceInfoTypeId DOCUMENT ME!
     * @param value the value
     * @param conn the connection
     *
     * @throws SQLException if error occurs
     */
    private static void updateResourceInfo(long resourceId, long resourceInfoTypeId, String value, Connection conn)
        throws SQLException {
        String UPDATE_SQL = "update resource_info set value = ? where resource_id = ? and resource_info_type_id = ? ";
        String INSERT_SQL = "INSERT INTO resource_info " +
            "(resource_id, resource_info_type_id, value, create_user, create_date, modify_user, modify_date) " +
            " VALUES (?, ?, ?, ?, CURRENT, ?, CURRENT)";

        // Update the resource_info
        PreparedStatement pstmt = conn.prepareStatement(UPDATE_SQL);
        pstmt.setString(1, value);
        pstmt.setLong(2, resourceId);
        pstmt.setLong(3, resourceInfoTypeId);

        int result = pstmt.executeUpdate();

        if (result == 0) {
            // No given resource_info exists, insert instead
            PRHelper.close(pstmt);
            pstmt = conn.prepareStatement(INSERT_SQL);
            pstmt.setLong(1, resourceId);
            pstmt.setLong(2, resourceInfoTypeId);
            pstmt.setString(3, value);
            pstmt.setString(4, USER_ID);
            pstmt.setString(5, USER_ID);
            pstmt.execute();
        }

        PRHelper.close(pstmt);
    }

    /**
     * Update or insert project info with given value.
     *
     * @param projectId the project id
     * @param projectInfoTypeId projectInfoTypeId
     * @param value the value
     * @param conn the connection
     *
     * @throws SQLException if error occurs
     */
    private static void updateProjectInfo(long projectId, long projectInfoTypeId, String value, Connection conn)
        throws SQLException {
        String UPDATE_SQL = "update project_info set value = ? where project_id = ? and project_info_type_id = ? ";
        String INSERT_SQL = "INSERT INTO project_info " +
            "(project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date) " +
            " VALUES (?, ?, ?, ?, CURRENT, ?, CURRENT)";

        // Update the ProjectInfo
        PreparedStatement pstmt = conn.prepareStatement(UPDATE_SQL);
        pstmt.setString(1, value);
        pstmt.setLong(2, projectId);
        pstmt.setLong(3, projectInfoTypeId);

        int result = pstmt.executeUpdate();

        if (result == 0) {
            // No given ProjectInfo exists, insert instead
            PRHelper.close(pstmt);
            pstmt = conn.prepareStatement(INSERT_SQL);
            pstmt.setLong(1, projectId);
            pstmt.setLong(2, projectInfoTypeId);
            pstmt.setString(3, value);
            pstmt.setString(4, USER_ID);
            pstmt.setString(5, USER_ID);
            pstmt.execute();
        }

        PRHelper.close(pstmt);
    }

    /**
     * Retrieve price for given project.
     *
     * @param projectId project id
     * @param conn the connection
     *
     * @return the price
     *
     * @throws SQLException if error occurs
     */
    private static double getPriceByProjectId(long projectId, Connection conn)
        throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement(SELECT_PRICE_PROJECT_INFO);
            pstmt.setLong(1, projectId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
            	if (rs.getDouble(1) > 0) {
            		return rs.getDouble(1);
            	}
            }
        } finally {
            PRHelper.close(rs);
            PRHelper.close(pstmt);
        }

        try {
            pstmt = conn.prepareStatement(SELECT_PRICE_CVD);
            pstmt.setLong(1, projectId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble(1);
            }
        } finally {
            PRHelper.close(rs);
            PRHelper.close(pstmt);
        }

        return 0;
    }

    /**
     * Return the winner/Runner count with given project.
     * 
     * @param projectId the project id
     * @param conn the connection
     * @return the count
     * @throws SQLException if error occurs
     */
    private static long getWinnerRunnerCount(long projectId, Connection conn) throws SQLException {
    	String sqlString = "select count(*) from project_info where project_id = ? and project_info_type_id in (23,24);";

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement(sqlString);
            pstmt.setLong(1, projectId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getLong(1);
            } else {
                return 0;
            }
        } finally {
            PRHelper.close(rs);
            PRHelper.close(pstmt);
        }
    }
    
    /**
     * Retrieve the resourceId with placed, projectid.
     *
     * @param projectId the project id
     * @param placed the placed
     * @param conn connection
     *
     * @return the resourceId
     *
     * @throws SQLException if error occurs
     */
    private static long getResourceIdByPlaced(long projectId, long placed, Connection conn)
        throws SQLException {
        String selectResourceId = "select r.resource_id " + "	from resource_info ri, " + "	resource r " +
            "	where r.resource_id = ri.resource_id " + "	and ri.resource_info_type_id = 12 " +
            "	and r.project_id = ? " + "	and ri.value = ? ";
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement(selectResourceId);
            pstmt.setLong(1, projectId);
            pstmt.setLong(2, placed);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getLong(1);
            } else {
                return 0;
            }
        } finally {
            PRHelper.close(rs);
            PRHelper.close(pstmt);
        }
    }
}


class Reviewer {
    private long resourceId;
    private long resourceRoleId;
    private long userId;
    private boolean isPrimaryReviewer;

    Reviewer(long resourceId, long resourceRoleId, long userId) {
        this.resourceId = resourceId;
        this.resourceRoleId = resourceRoleId;
        this.userId = userId;
    }

    /**
     * Returns the isPrimaryReviewer.
     *
     * @return Returns the isPrimaryReviewer.
     */
    public boolean isPrimaryReviewer() {
        return isPrimaryReviewer;
    }

    /**
     * Set isPrimaryReviewer
     *
     * @param isPrimaryReviewer The isPrimaryReviewer to set.
     */
    public void setPrimaryReviewer(boolean isPrimaryReviewer) {
        this.isPrimaryReviewer = isPrimaryReviewer;
    }

    /**
     * Returns the resourceId.
     *
     * @return Returns the resourceId.
     */
    public long getResourceId() {
        return resourceId;
    }

    /**
     * Set resourceId
     *
     * @param resourceId The resourceId to set.
     */
    public void setResourceId(long resourceId) {
        this.resourceId = resourceId;
    }

    /**
     * Returns the resourceRoleId.
     *
     * @return Returns the resourceRoleId.
     */
    public long getResourceRoleId() {
        return resourceRoleId;
    }

    /**
     * Set resourceRoleId
     *
     * @param resourceRoleId The resourceRoleId to set.
     */
    public void setResourceRoleId(long resourceRoleId) {
        this.resourceRoleId = resourceRoleId;
    }

    /**
     * Returns the userId.
     *
     * @return Returns the userId.
     */
    public long getUserId() {
        return userId;
    }

    /**
     * Set userId
     *
     * @param userId The userId to set.
     */
    public void setUserId(long userId) {
        this.userId = userId;
    }

    /**
     * isPrimaryScreener
     *
     * @return isPrimaryScreener
     */
    public boolean isPrimaryScreener() {
        return this.resourceRoleId == 2;
    }

    /**
     * isReviewer
     *
     * @return isReviewer
     */
    public boolean isReviewer() {
        return (this.resourceRoleId >= 4) && (this.resourceRoleId <= 7);
    }

    /**
     * isAggregator
     *
     * @return isAggregator
     */
    public boolean isAggregator() {
        return this.resourceRoleId == 8;
    }

    /**
     * isFinalReviewer
     *
     * @return isFinalReviewer
     */
    public boolean isFinalReviewer() {
        return this.resourceRoleId == 9;
    }

    /**
     * isScreener
     *
     * @return isScreener
     */
    public boolean isScreener() {
        return this.resourceRoleId == 3;
    }
}
