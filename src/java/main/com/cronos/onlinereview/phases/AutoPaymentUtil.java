/*
 * Copyright (C) 2004 - 2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

import com.topcoder.management.payment.calculator.ProjectPaymentCalculator;
import com.topcoder.management.payment.calculator.ProjectPaymentCalculatorException;
import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.config.Property;
import com.topcoder.util.log.Level;
import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogManager;

/**
 * <p>The AutoPaymentUtil is used to auto-fill for payments of reviewers and submitters.</p>
 *
 * <p>Version 1.1 (Studio Coding In Online Review) Change notes:
 *  Added support for new UI prototype, RIA Build and RIA Component competitions so that
 *  reviewer payment is populated automatically</p>
 *
 * <p>Version 1.2 (Testing Competition Split Release Assembly 1.0) Change notes:
 *  Updated Application Testing to Test Suites and added support for new Test Scenarios competitions.</p>
 *
 * <p>Version 1.3 (Appeals Early Completion Release Assembly 1.0) Change notes:
 *  Updated Reviewer payments to pay final review and aggregation just once.</p>
 *
 * <p>Version 1.4 (Copilot Selection Contest Online Review and TC Site Integration Assembly 1.0) Change notes:
 *  Updated to support copilot posting project type.</p>
 *  
 * <p>Version 1.5 (Online Review Replatforming Release 2) Change notes:
 *   <ol>
 *     <li>Update {@link #populateReviewerPayments(long, Connection, int)} method populate reviewer payments for
 *     studio contest and for the new milestone screening phase.</li>
 *     <li>Update {@link #getReviewers(long, Connection)} method to add support for Milestone Screener.</li>
 *     <li>Update {@link #populateSubmitterPayments(long, Connection)} method to add support to populate submitter payments
 *     from prize table.</li>
 *   </ol> 
 * </p>
 *
 * <p>Version 1.5 (Content Creation Contest Online Review and TC Site Integration Assembly 1.0) Change notes:
 *  Updated to support content creation project type.</p>
 *
 *  <p>Version 1.6 (Release Assembly - TopCoder BugHunt Competition Integration) Change notes:
  *  Updated to support Bug Hunt project type.</p>
 *
 * <p>
 * Version 1.7 Change notes:
 *   <ol>
 *     <li>Updated {@link #populateReviewerPayments(long, Connection, int)} methods to use <code>Project Payment
 *     Calculator</code> component for calculating the reviewer payments.</li>
 *     <li>Added {@link #projectPaymentCalculator} class field.</li>
 *   </ol>
 * </p>
 *
 * @author George1, brain_cn, pulky, Blues, FireIce, VolodymyrK, TCSASSEMBLER, isv
 * @version 1.7
 */
public class AutoPaymentUtil {
        /**
         * The logger instance.
         */
        private static final Log logger = LogManager.getLog(AutoPaymentUtil.class.getName());


    /**
     * <p>A <code>ProjectPaymentCalculator</code> to be used for calculating the payments for reviewer roles for desired
     * projects.</p>
     * 
     * @since 1.7
     */
    private static final ProjectPaymentCalculator projectPaymentCalculator = createProjectPaymentCalculator();

    /**
     * The SQL statement to calculate the submitter's payment for a specified contest. The submiiter's payment
     * including the contest submission payment and milestone contest submission payment.
     * 
     * @since 1.5
     */
    private static final String SELECT_SUBMITTERS_PAYMENT = 
        "select resource.resource_id, sum(prize.prize_amount) " +
        "  from submission, " +
        "       upload, " + 
        "       submission_status_lu, " +
        "       resource, " + 
        "       prize " +
        "  where upload.upload_id = submission.upload_id " +
        "    and submission.submission_type_id in (1,3) " +
        "    and submission.submission_status_id = submission_status_lu.submission_status_id " +
        "    and submission_status_lu.name in ('Active', 'Completed Without Win') " +
        "    and resource.project_id = ? " + 
        "    and upload.resource_id = resource.resource_id " +
        "    and submission.prize_id = prize.prize_id " +
        "    and not exists (select 1 from resource_info ri where ri.resource_id = resource.resource_id " +
        "    and ri.resource_info_type_id = 8 and ri.value = 'Yes' ) " +
        "  group by resource.resource_id";

    /**
     * The SQL statement to retrieve a project info value.
     */
    private static final String SELECT_PROJECT_INFO_VALUE =
        "select value from project_info where project_id = ? and project_info_type_id = ?";

    private static final String USER_ID = "phase_handler";

    public static final long SPECIFICATION_REVIEWER_ROLE = 18;
    public static final long FINAL_REVIEWER_ROLE = 9;

    /**
     * Prevent to be created.
     */
    private AutoPaymentUtil() {
    }

    /**
     * <p>Creates new instance of calculator for the project payments based on the available application configuration.
     * </p>
     *
     * @return a <code>ProjectPaymentCalculator</code> to be used for calculating the payments for reviewer roles for
     *         desired projects.
     * @since 1.7
     */
    private static ProjectPaymentCalculator createProjectPaymentCalculator() {
        String className = null;
        try {
            ConfigManager cfgMgr = ConfigManager.getInstance();
            Property config = cfgMgr.getPropertyObject("com.cronos.OnlineReview", "ProjectPaymentConfig");
            className = config.getValue("CalculatorClass");
            Class clazz = Class.forName(className);
            return (ProjectPaymentCalculator) clazz.newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to instantiate the project payment calculator of type: "
                    + className, e);
        }
    }

    /**
     * Populate reviewers payment.
     *
     * @param projectId project id
     * @param conn the connection
     *
     * @throws SQLException if an error occurs in the persistence layer
     * @throws ProjectPaymentCalculatorException if an error occurs while calculating reviewer payments.
     */
    public static void populateReviewerPayments(long projectId, Connection conn)
            throws SQLException, ProjectPaymentCalculatorException {
        if (!isMemberPaymentEligible(projectId, conn)) {
            return;
        }

        logger.log(Level.INFO, "Populate reviewer payments for project " + projectId);

        boolean isStudio = getProjectTypeId(projectId, conn) == 3;
        List<Reviewer> reviewers = getReviewers(projectId, conn);
        
        // Get the default payments for reviewers
        Set<Long> reviewerResourceRoleIds = new HashSet<Long>();
        for (Reviewer reviewer : reviewers) {
            reviewerResourceRoleIds.add(reviewer.resourceRoleId);
        }
        Map<Long, BigDecimal> reviewerPayments
                = projectPaymentCalculator.getDefaultPayments(projectId, new ArrayList<Long>(reviewerResourceRoleIds));

        // Unlike other reviewer payments specification reviewer is paid only after
        // at least one submission passed review. Specification reviewer is always paid in studio competitions.
        boolean paySpecReviewer = isStudio || projectPassedReview(projectId, conn);

        // this is added to pay final review and spec reviewer only once.
        boolean alreadyPaidSpecReviewer = false, alreadyPaidFinalReviewer = false;

        for (Reviewer reviewer : reviewers) {
            if (!reviewerPayments.containsKey(reviewer.resourceRoleId)) {
                continue;
            }
            
            boolean payResource = false;
            if (reviewer.resourceRoleId == SPECIFICATION_REVIEWER_ROLE) {
                if (!alreadyPaidSpecReviewer && paySpecReviewer) {
                    payResource = true;
                    alreadyPaidSpecReviewer = true;
                }
            } else if (reviewer.resourceRoleId == FINAL_REVIEWER_ROLE) {
                if (!alreadyPaidFinalReviewer) {
                    payResource = true;
                    alreadyPaidFinalReviewer = true;
                }
            } else {
                payResource = true;
            }

            if (payResource) {
                updateResourcePayment(reviewer.resourceId,
                        reviewerPayments.get(reviewer.resourceRoleId).doubleValue(), conn);
            }
        }
    }

    /**
     * Clears payment for all Copilot resources that have not been paid yet.
     *
     * @param projectId project id
     * @param conn the connection
     *
     * @throws SQLException if an error occurs in the persistence layer
     */
    public static void clearCopilotPayments(long projectId, Connection conn) throws SQLException {

        String SELECT_SQL =
            " select r.resource_id " +
            " from resource r " +
            " where r.resource_role_id = 14 and r.project_id = ? " +
            " and not exists (select ri.resource_id from resource_info ri " +
            "   where r.resource_id = ri.resource_id " +
            "   and ri.resource_info_type_id = 8 and ri.value = 'Yes')";

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement(SELECT_SQL);
            pstmt.setLong(1, projectId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                long resourceId = rs.getLong(1);

                // Clear payment value.
                deleteResourceInfo(resourceId, 7, conn);

                // Set payment status to "N/A".
                updateResourceInfo(resourceId, 8, "N/A", conn);
            }
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
    private static List<Reviewer> getReviewers(long projectId, Connection conn)
        throws SQLException {
        String SELECT_SQL =
                "select r.resource_id, " +
                "       resource_role_id " +
                "       from resource r " +
            "   where r.resource_role_id in (2, 3, 4, 5, 6, 7, 8, 9, 16, 18, 19)" +
            "   and r.project_id = ? " +
            "   and not exists (select ri.resource_id from resource_info ri " +
            "           where r.resource_id = ri.resource_id " +
            "           and ri.resource_info_type_id = 8 " +
            "           and (ri.value = 'N/A' or ri.value = 'Yes')) " +
            "   and r.resource_id in (select resource_id from review where committed = 1) " +
            "order by resource_role_id";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Reviewer> reviewers = new ArrayList<Reviewer>();

        try {
            pstmt = conn.prepareStatement(SELECT_SQL);
            pstmt.setLong(1, projectId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Reviewer reviewer = new Reviewer(rs.getLong(1), rs.getLong(2));
                reviewers.add(reviewer);
            }

            return reviewers;
        } finally {
            PRHelper.close(rs);
            PRHelper.close(pstmt);
        }
    }

    /**
     * Returns true if at least one submission passed review for the specified project.
     *
     * @param projectId project id
     * @param conn connection
     *
     * @return true if there is at least one submission that passed review and false otherwise.
     *
     * @throws SQLException if error occurs
     */
    private static boolean projectPassedReview(long projectId, Connection conn) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean passedReview = false;

        try {
            pstmt = conn.prepareStatement(
                    "SELECT user_id FROM project_result WHERE project_id=? AND passed_review_ind = 1");
            pstmt.setLong(1, projectId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                passedReview = true;
            }

        } finally {
            PRHelper.close(rs);
            PRHelper.close(pstmt);
        }

        return passedReview;
    }

    /**
     * Return project type id for given project id
     *
     * @param projectId project id
     * @param conn the connection
     * @return project type id
     * @throws SQLException if error occurs
     */
    static long getProjectTypeId(long projectId, Connection conn)
        throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement("SELECT pcl.project_type_id from project p, project_category_lu pcl where " +
                    "p.project_id = ? and p.project_category_id = pcl.project_category_id");
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
    public static void populateSubmitterPayments(long projectId, Connection conn)
            throws SQLException {
        if (!isMemberPaymentEligible(projectId, conn)) {
            return;
        }

        logger.log(Level.INFO, "Setting submitter payment for the project : " + projectId);

        // Select submitter resources that have not been paid yet. The ones that already have been paid shouldn't change.
        String SELECT_SQL  = "select resource_id from resource where resource_role_id = 1 and project_id = ? and " +
                             " resource_id not in (select resource_id from resource_info where resource_info_type_id = 8 and value = 'Yes')";

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            // clear non-paid resources in case if project was reverted. 
            pstmt = conn.prepareStatement(SELECT_SQL);
            pstmt.setLong(1, projectId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                // Clear payment value.
                deleteResourceInfo(rs.getLong(1), 7, conn);

                // Set payment status to "N/A"
                updateResourceInfo(rs.getLong(1), 8, "N/A", conn);
            }

            // get the payments from the prize table
            Map<Long, Float> submittersPayment = getSubmittersPayment(projectId, conn);
            for (Map.Entry<Long, Float> entry : submittersPayment.entrySet()) {
                // Update the payment for given submitter
                updateResourcePayment(entry.getKey(), entry.getValue(), conn);
            }

        } finally {
            PRHelper.close(rs);
            PRHelper.close(pstmt);
        }

    }

    /**
     * Update the submitter payment with price.
     * 
     * @param resourceId
     *            the resource id
     * @param price
     *            the price
     * @param conn
     *            DOCUMENT ME!
     * 
     * @throws SQLException
     *             if error occurs
     */
    private static void updateResourcePayment(long resourceId, double price, Connection conn)
        throws SQLException {
        // Set price
        updateResourceInfo(resourceId, 7, String.valueOf(price), conn);

        // set to Not Paid
        updateResourceInfo(resourceId, 8, "No", conn);
    }

    /**
     * Deletes resource info for the specified resource and resoruce info type.
     *
     * @param resourceId the Resource id
     * @param resourceInfoTypeId Resource info type Id.
     * @param conn the connection
     *
     * @throws SQLException if error occurs
     */
    private static void deleteResourceInfo(long resourceId, long resourceInfoTypeId, Connection conn)
        throws SQLException {
        String DELETE_SQL = "delete from resource_info where resource_id = ? and resource_info_type_id = ?";

        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(DELETE_SQL);
            pstmt.setLong(1, resourceId);
            pstmt.setLong(2, resourceInfoTypeId);

            pstmt.executeUpdate();
        } finally {
            PRHelper.close(pstmt);
        }
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
                logger.log(Level.DEBUG, "insert record into resource_info for the resource_id:"
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
                logger.log(Level.DEBUG, "update record in resource_info for the resource_id:"
                                + resourceId + ", resource_info_type_id:" + resourceInfoTypeId + " and with new value:" + value);
        }

        PRHelper.close(pstmt);
    }

    /**
     * Check whether the member payment is eligible.
     * @param projectId the project id
     * @param conn the db connection
     */
    private static boolean isMemberPaymentEligible(long projectId, Connection conn)
        throws SQLException {
            String str = getProjectInfo(projectId, 46, conn);
            return "true".equalsIgnoreCase(str);
    }

    /**
     * Retrieve the project info value for the given project and info type.
     * @param projectId the project id
     * @param projectInfoTypeId the project info type id
     * @param conn the db connection
     */
    private static String getProjectInfo(long projectId, long projectInfoTypeId, Connection conn)
        throws SQLException {
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            try {
                pstmt = conn.prepareStatement(SELECT_PROJECT_INFO_VALUE);
                pstmt.setLong(1, projectId);
                pstmt.setLong(2, projectInfoTypeId);

                rs = pstmt.executeQuery();

                if (rs.next()) {
                    return rs.getString(1);
                }
            } finally {
                PRHelper.close(rs);
                PRHelper.close(pstmt);
            }
            return null;

    }

    /**
     * Gets the submitter's payment from the prize table for a specified contest. The submitter's payment
     * includes the conetst submission prize and milestone submission prize.
     *
     * @param projectId the project id of the specified conetst
     * @param connection SQL connection to use for information retrieving.
     * @return a <code>Map</code> providing the submitter's payment. The key is the resource id
     * of the submitter, the value is the submitter's payment.
     * @throws SQLException if any error occurs accessing the data store or reading the result set.
     * @since 1.5
     */
    private static Map<Long, Float> getSubmittersPayment(long projectId, Connection connection)
        throws SQLException {
        PreparedStatement statement = null;
        ResultSet rs = null;
        
        Map<Long, Float> submittersPayment = new HashMap<Long, Float>();
        
        try {
            statement = connection.prepareStatement(SELECT_SUBMITTERS_PAYMENT);
            statement.setLong(1, projectId);
            rs = statement.executeQuery();
            
            while (rs.next()) {
                submittersPayment.put(rs.getLong(1), rs.getFloat(2));
            }
            return submittersPayment;
        } finally {
            PRHelper.close(rs);
            PRHelper.close(statement);
        }
    }

    static class Reviewer {
        long resourceId;
        long resourceRoleId;

        Reviewer(long resourceId, long resourceRoleId) {
            this.resourceId = resourceId;
            this.resourceRoleId = resourceRoleId;
        }
    }
}
