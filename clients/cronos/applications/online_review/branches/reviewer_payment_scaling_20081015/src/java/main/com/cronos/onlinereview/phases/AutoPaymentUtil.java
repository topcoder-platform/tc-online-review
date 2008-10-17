/**
 * Copyright (C) 2006-2007 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import com.topcoder.util.log.Level;
import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogFactory;
import com.topcoder.web.common.model.FixedPriceComponent;
import com.topcoder.web.common.model.SoftwareComponent;

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
 * @author George1
 * @version 1.0
 */
public class AutoPaymentUtil {
	/**
	 * The logger instance.
	 */
	private static final Log logger = LogFactory.getLog(AutoPaymentUtil.class.getName());
	
//    /** Retrieve the price from comp_version_date. */
//    private static final String SELECT_PRICE_CVD = "select price " +
//    	"	from comp_version_dates, " +
//        "	project_info pi, " +
//        "	project p" +
//        "	where p.project_id = pi.project_id" +
//        "	and comp_vers_id = pi.value" +
//        "	and ((p.project_category_id = 1 and phase_id = 112)" +
//        "		or (p.project_category_id = 2 and phase_id = 113))" +
//        "	and pi.project_info_type_id = 1 " +
//        "	and pi.project_id = ? ";

    /** Retrieve the price from project_info. */
    private static final String SELECT_PRICE_PROJECT_INFO =
    	"select value " +
    	"	from project_info " +
        "	where project_info_type_id = 16 " +
        "	and project_id = ? ";

    /**
     * This is a string constant that specifies a <code>SELECT</code> statement for retrieving the
     * ID of the resource who passed the review (when project is in after Appeals Response state)
     * and took some certain place as a result of the competition. This <code>SELECT</code>
     * statement takes two parameters: the first is the ID of the project which the resource should
     * be retrieved for, and the second parameter is the number of place of ineterst (places
     * numbering start from 1). The result of the executing of this statement will be either one ID
     * of the resource, or empty result set if either the resource did not pass review (or
     * screening), or there is not such resulting place for the project.
     * <p>
     * This constant is used by <code>getPassingSubmitterIdByPlace(long, long, Connection)</code>
     * method.
     * </p>
     *
     * @see #getPassingSubmitterIdByPlace(long, long, Connection)
     */
    // OrChange - Modified to take the placement from the submission table
    private static final String SELECT_PASSED_REVIEW_RESOURCE_ID_FOR_PLACE =
        "SELECT DISTINCT resource.resource_id " +
        "  FROM resource, " +
        "       upload, " +
        "       submission, " +
        "       submission_status_lu " +
        " WHERE upload.resource_id = resource.resource_id " +
        "   AND submission.upload_id = upload.upload_id " +
        "   AND submission.submission_status_id = submission_status_lu.submission_status_id " +
        "   AND submission_status_lu.name in ('Active', 'Completed Without Win') " +
        "   AND resource.project_id = ? " +
        "   AND submission.placement = ? ";

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

        // OrChange - no modification needed as the current logic is applied only to design and development. (and component testing)
        if (projectCategoryId != 1 && projectCategoryId != 2 && projectCategoryId != 5) {
        	// Logic only apply to component
        	return;
        }

        logger.log(Level.INFO,
        		"Populate reviewer payments for the projectId:" + projectId + " in the phase:" + phaseId);
        int levelId = SoftwareComponent.LEVEL1;
        int count = getCount(projectId, conn);
        int passedCount = getScreenPassedCount(projectId, conn);
        float[] payments = getPayments(projectId, projectCategoryId, conn);
        
        FixedPriceComponent fpc = new FixedPriceComponent(levelId, count, passedCount,
                (int)(projectCategoryId + 111), payments[0], payments[1]);
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
            "	and r.project_id = ? " +
            "	and not exists (select ri1.resource_id from resource_info ri1 " +
            "		where r.resource_id = ri1.resource_id" +
            "		and ri1.resource_info_type_id = 8" +
            "		and ri1.value = 'N/A')" +
            "order by resource_role_id";
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
        // OrChange - Do not modify the payment if the project is a studio
        if(PRHelper.isStudioProject(projectId)) {
            return;
        }
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

        logger.log(Level.INFO, "Clear submitter payment for the project :" + projectId);
        
        // Prepare prices for differnt places
        double[] prices = new double[] { price, Math.round(price * 0.5) };
        long[] places = new long[] { 1, 2 };

        // Update the payment resource_info and paid resource_info
        for (int i = 0; i < places.length; ++i) {
            long submitterId = getPassingSubmitterIdByPlace(projectId, places[i], conn);

            if (submitterId != 0) {
                // Update the payment for given submitter which has placed
                updateResourcePayment(submitterId, prices[i], conn);
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
        	logger.log(Level.INFO, "insert record into resource_info for the resource_id:"
        			+ resourceId + " and resource_info_type_id:" + resourceInfoTypeId + " and value:" + value);
            // No given resource_info exists, insert instead
            PRHelper.close(pstmt);
            pstmt = conn.prepareStatement(INSERT_SQL);
            pstmt.setLong(1, resourceId);
            pstmt.setLong(2, resourceInfoTypeId);
            pstmt.setString(3, value);
            pstmt.setString(4, USER_ID);
            pstmt.setString(5, USER_ID);
            pstmt.execute();
        } else {
        	logger.log(Level.INFO, "update record in resource_info for the resource_id:"
        			+ resourceId + ", resource_info_type_id:" + resourceInfoTypeId + " and with new value:" + value);
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
        	logger.log(Level.INFO, "insert record into project_info for the project_id:"
        			+ projectId + " and project_info_type_id:" + projectInfoTypeId + " and value:" + value);
            // No given ProjectInfo exists, insert instead
            PRHelper.close(pstmt);
            pstmt = conn.prepareStatement(INSERT_SQL);
            pstmt.setLong(1, projectId);
            pstmt.setLong(2, projectInfoTypeId);
            pstmt.setString(3, value);
            pstmt.setString(4, USER_ID);
            pstmt.setString(5, USER_ID);
            pstmt.execute();
        } else {
        	logger.log(Level.INFO, "update record in project_info for the project_id:"
        			+ projectId + " and project_info_type_id:" + projectInfoTypeId + " with new value:" + value);
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

//        try {
//            pstmt = conn.prepareStatement(SELECT_PRICE_CVD);
//            pstmt.setLong(1, projectId);
//            rs = pstmt.executeQuery();
//
//            if (rs.next()) {
//                return rs.getDouble(1);
//            }
//        } finally {
//            PRHelper.close(rs);
//            PRHelper.close(pstmt);
//        }

        return 0;
    }

    /**
     * This static method retrieves the ID of the submitter (resource) that passed review and took
     * up the placement specifid by the <code>place</code> parameter for project specified by the
     * <code>projectId</code> parameter.
     *
     * @return an ID of the resource, or 0 if no resource matching the search criteria was found.
     * @param projectId
     *            the ID of the project to search the passing submitter (resource) with placement
     *            for.
     * @param place
     *            the place that the submitter must have for the current search.
     * @param connection
     *            SQL connection to use for information retrieving.
     * @throws SQLException
     *             if any error occurs accessing the data store or reading the result set.
     */
    private static long getPassingSubmitterIdByPlace(long projectId, long place, Connection connection)
        throws SQLException {
        PreparedStatement statement = null;
        ResultSet rs = null;

        try {
            statement = connection.prepareStatement(SELECT_PASSED_REVIEW_RESOURCE_ID_FOR_PLACE);
            statement.setLong(1, projectId);
            statement.setString(2, String.valueOf(place));
            rs = statement.executeQuery();

            return (rs.next()) ? rs.getLong(1) : 0L;
        } finally {
            PRHelper.close(rs);
            PRHelper.close(statement);
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
