/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.cronos.onlinereview.phases.logging.LoggerMessage;
import com.topcoder.management.phase.PhaseHandlingException;
import com.topcoder.management.project.PersistenceException;
import com.topcoder.management.project.ProjectManager;
import com.topcoder.management.project.ProjectManagerImpl;
import com.topcoder.management.project.ProjectStatus;
import com.topcoder.management.project.ValidationException;
import com.topcoder.project.phases.Phase;
import com.topcoder.util.errorhandling.BaseException;
import com.topcoder.util.log.Level;

/**
 * The PRHelper which is used to provide helper method for Phase Handler.
 *
 * <p>
 * Version 1.1 (Online Review Payments and Status Automation Assembly 1.0) Change notes:
 * <ol>
 *     <li>Added {@link #findProjectStatusByName(ProjectManager, String)} method.</li>
 *     <li>Added {@link #completeProject(ManagerHelper, Phase, String)} method.</li>
 *   </ol>
 * <p>
 * @author brain_cn, FireIce
 * @version 1.1
 */
public class PRHelper {
    /**
     * This member variable holds the formatting string used to format dates.
     */
    private static String dateFormat = "MM.dd.yyyy HH:mm z";

    private static final com.topcoder.util.log.Log logger = com.topcoder.util.log.LogFactory.getLog(PRHelper.class
            .getName());
    // OrChange : Modified the statement to take the placed and final score from submission table
    private static final String APPEAL_RESPONSE_SELECT_STMT = "select s.final_score as final_score, "
            + " ri_u.value as user_id, " + "    s.placement as placed, " + "    ri1.value payment, " + "    r.project_id, "
            + " s.submission_status_id " + "from resource r, " + "  resource_info ri_u," + "    outer resource_info ri1,"
            + " upload u," + "  submission s " + "where  r.resource_id = ri_u.resource_id " + "and s.submission_type_id = 1 "
            + "and ri_u.resource_info_type_id = 1 " + "and r.resource_id = ri1.resource_id "
            + "and ri1.resource_info_type_id = 7 " + "and u.project_id = r.project_id "
            + "and u.resource_id = r.resource_id " + "and upload_type_id = 1 " + "and u.upload_id = s.upload_id "
            + "and r.project_id = ? " + "and r.resource_role_id = 1 ";

    private static final String APPEAL_RESPONSE_UPDATE_PROJECT_RESULT_STMT = "update project_result set final_score = ?, placed = ?, passed_review_ind = ?, payment = ? "
            + "where project_id = ? and user_id = ? ";

    // OrChange : Modified to take the raw score from the Submission table
    private static final String REVIEW_SELECT_STMT = "select s.initial_score as raw_score, ri_u.value as user_id, r.project_id "
            + "from resource r, resource_info ri_u, "
            + "   upload u,"
            + "   submission s "
            + "where r.resource_id = ri_u.resource_id and ri_u.resource_info_type_id = 1 "
            + "and s.submission_type_id = 1 "
            + "and u.project_id = r.project_id "
            + "and u.resource_id = r.resource_id "
            + "and upload_type_id = 1 "
            + "and u.upload_id = s.upload_id " + " and r.project_id = ? ";

    private static final String REVIEW_UPDATE_PROJECT_RESULT_STMT = "update project_result set raw_score = ?  "
            + "where project_id = ? and user_id = ? ";

    private static final String FAILED_PASS_SCREENING_STMT = "update project_result set valid_submission_ind = 0, rating_ind = 0 "
            + "where exists(select * from submission s,upload u,resource r,resource_info ri   "
            + " where u.upload_id = s.upload_id and u.upload_type_id = 1 "
            + " and s.submission_type_id = 1 "
            + " and u.project_id = project_result.project_id "
            + " and r.resource_id = u.resource_id "
            + " and ri.resource_id = r.resource_id "
            + " and ri.value = project_result.user_id and ri.resource_info_type_id = 1"
            + " and submission_status_id = 2 ) and " + " project_id = ?";

    private static final String PASS_SCREENING_STMT = "update project_result set valid_submission_ind = 1, rating_ind = 1 "
            + "where exists(select * from submission s,upload u,resource r,resource_info ri    "
            + " where u.upload_id = s.upload_id and u.upload_type_id = 1  "
            + " and s.submission_type_id = 1 "
            + " and u.project_id = project_result.project_id "
            + " and r.resource_id = u.resource_id "
            + " and ri.resource_id = r.resource_id "
            + " and ri.value = project_result.user_id and ri.resource_info_type_id = 1 "
            + " and submission_status_id in (1,3,4) ) and " + " project_id = ?";

    private static final String UPDATE_PROJECT_RESULT_STMT = "update project_result set valid_submission_ind = 0 "
            + "where not exists(select * from submission s,upload u,resource r,resource_info ri  "
            + " where u.upload_id = s.upload_id and upload_type_id = 1  "
            + " and s.submission_type_id = 1 "
            + " and u.project_id = project_result.project_id " + "  and r.resource_id = u.resource_id "
            + " and ri.resource_id = r.resource_id "
            + " and ri.value = project_result.user_id and ri.resource_info_type_id = 1 "
            + " and submission_status_id <> 5 ) and " + " project_id = ?";

    /**
     * Prevent to be created outside.
     */
    private PRHelper() {
    }

    /**
     * Pull data to project_result.
     *
     * @param phaseId
     *            the phase id
     * @throws PhaseHandlingException
     *             if error occurs
     */
    static void processRegistrationPR(long projectId, Connection conn, boolean toStart) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            if (toStart) {
                logger.log(Level.INFO,
                    new LoggerMessage("project", new Long(projectId), null, "start registration process."));
            }
        } finally {
            close(pstmt);
        }
    }

    /**
     * Pull data to project_result.
     *
     * @param projectId
     *            the projectId
     * @throws PhaseHandlingException
     *             if error occurs
     */
    static void processSubmissionPR(long projectId, Connection conn, boolean toStart) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            if (!toStart) {
                logger.log(Level.INFO,
                    new LoggerMessage("project", new Long(projectId), null, "process submission phase."));

                // Update all users who submit submission
                pstmt = conn.prepareStatement(UPDATE_PROJECT_RESULT_STMT);
                pstmt.setLong(1, projectId);
                pstmt.execute();
            }
        } finally {
            close(pstmt);
        }
    }

    /**
     * Pull data to project_result.
     *
     * @param projectId
     *            the projectId
     * @throws PhaseHandlingException
     *             if error occurs
     */
    static void processScreeningPR(long projectId, Connection conn, boolean toStart) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            if (!toStart) {
                logger.log(Level.INFO,
                    new LoggerMessage("project", new Long(projectId), null, "process screening phase."));
                // Update all users who failed to pass screen, set valid_submission_ind = 0
                pstmt = conn.prepareStatement(FAILED_PASS_SCREENING_STMT);
                pstmt.setLong(1, projectId);
                pstmt.execute();
                close(pstmt);

                // Update all users who pass screen, set valid_submission_ind = 1
                pstmt = conn.prepareStatement(PASS_SCREENING_STMT);
                pstmt.setLong(1, projectId);
                pstmt.execute();
            }

            AutoPaymentUtil.populateReviewerPayments(projectId, conn, AutoPaymentUtil.SCREENING_PHASE);
        } finally {
            close(pstmt);
        }
    }

    /**
     * Pull data to project_result.
     *
     * @param projectId
     *            the projectId
     * @param phaseId
     *            the phase id
     * @throws PhaseHandlingException
     *             if error occurs
     */
    static void processReviewPR(long projectId, Connection conn, boolean toStart) throws SQLException {
        PreparedStatement pstmt = null;
        PreparedStatement updateStmt = null;
        ResultSet rs = null;
        try {
            if (!toStart) {
                logger.log(Level.INFO,
                    new LoggerMessage("project", new Long(projectId), null, "process review phase."));
                // Retrieve all
                pstmt = conn.prepareStatement(REVIEW_SELECT_STMT);
                pstmt.setLong(1, projectId);
                rs = pstmt.executeQuery();

                updateStmt = conn.prepareStatement(REVIEW_UPDATE_PROJECT_RESULT_STMT);
                while (rs.next()) {
                    // Update all raw score
                    double rawScore = rs.getDouble("raw_score");
                    long userId = rs.getLong("user_id");
                    updateStmt.setDouble(1, rawScore);
                    updateStmt.setLong(2, projectId);
                    updateStmt.setLong(3, userId);
                    updateStmt.execute();
                }
            }

            AutoPaymentUtil.populateReviewerPayments(projectId, conn, AutoPaymentUtil.REVIEW_PHASE);
        } finally {
            close(rs);
            close(pstmt);
            close(updateStmt);
        }
    }

    /**
     * Pull data to project_result for while appeal response phase closed.
     *
     * @param projectId
     *            the projectId
     * @throws PhaseHandlingException
     *             if error occurs
     */
    static void processAppealResponsePR(long projectId, Connection conn, boolean toStart) throws SQLException {
        if (!toStart) {
            logger.log(Level.INFO,
                new LoggerMessage("project", new Long(projectId), null, "process Appeal Response phase."));
            populateProjectResult(projectId, conn);
        }
    }

    /**
     * Pull data to project_result.
     *
     * @param projectId
     *            the projectId
     * @throws PhaseHandlingException
     *             if error occurs
     */
    static void processAggregationPR(long projectId, Connection conn, boolean toStart) throws SQLException {
        if (!toStart) {
            logger.log(Level.INFO,
                    new LoggerMessage("project", new Long(projectId), null, "process Aggregation phase."));
            populateProjectResult(projectId, conn);
        }
        AutoPaymentUtil.populateReviewerPayments(projectId, conn, AutoPaymentUtil.AGGREGATION_PHASE);
    }

    /**
     * Pull data to project_result.
     *
     * @param projectId
     *            the projectId
     * @throws PhaseHandlingException
     *             if error occurs
     */
    static void processAggregationReviewPR(long projectId, Connection conn, boolean toStart) throws SQLException {
        if (!toStart) {
            logger.log(Level.INFO,
                    new LoggerMessage("project", new Long(projectId), null, "Process Aggregation Review phase."));
            populateProjectResult(projectId, conn);
        }
    }

    /**
     * Pull data to project_result.
     *
     * @param projectId
     *            the projectId
     * @throws PhaseHandlingException
     *             if error occurs
     */
    static void processFinalFixPR(long projectId, Connection conn, boolean toStart) throws SQLException {
        if (!toStart) {
            logger.log(Level.INFO,
                    new LoggerMessage("project", new Long(projectId), null, "Process final fix phase."));
            populateProjectResult(projectId, conn);
        }
    }

    /**
     * Pull data to project_result.
     *
     * @param projectId
     *            the projectId
     * @throws PhaseHandlingException
     *             if error occurs
     */
    static void processFinalReviewPR(long projectId, Connection conn, boolean toStart) throws SQLException {
        if (!toStart) {
            logger.log(Level.INFO,
                    new LoggerMessage("project", new Long(projectId), null, "Process final review phase."));
            populateProjectResult(projectId, conn);
        } else {
            logger.log(Level.INFO,
                    new LoggerMessage("project", new Long(projectId), null, "start final review phase."));
        }
        AutoPaymentUtil.populateReviewerPayments(projectId, conn, AutoPaymentUtil.FINAL_REVIEW_PHASE);
    }

    /**
     * Pull data to project_result, populate reviewer payments and clear copilot payments.
     *
     * @param projectId
     *            the projectId
     * @throws PhaseHandlingException
     *             if error occurs
     */
    static void processPostMortemPR(long projectId, Connection conn, boolean toStart) throws SQLException {
        if (!toStart) {
            logger.log(Level.INFO,
                    new LoggerMessage("project", new Long(projectId), null, "Process post mortem phase."));
            populateProjectResult(projectId, conn);
        } else {
            logger.log(Level.INFO,
                    new LoggerMessage("project", new Long(projectId), null, "start post mortem phase."));
        }
        AutoPaymentUtil.populateReviewerPayments(projectId, conn, AutoPaymentUtil.POST_MORTEM_PHASE);
		
		// Copilots aren't getting paid for failed projects.
        AutoPaymentUtil.clearCopilotPayments(projectId, conn);
    }

    /**
     * Populate final_score, placed and passed_review_ind.
     *
     * @param projectId
     *            project id
     * @param conn
     *            connection
     * @throws SQLException
     *             if error occurs
     */
    public static void populateProjectResult(long projectId, Connection conn) throws SQLException {
        // Payment should be set before populate to project_result table
        AutoPaymentUtil.populateSubmitterPayments(projectId, conn);

        PreparedStatement pstmt = null;
        PreparedStatement updateStmt = null;
        ResultSet rs = null;
        try {
            // Retrieve all
            pstmt = conn.prepareStatement(APPEAL_RESPONSE_SELECT_STMT);
            pstmt.setLong(1, projectId);
            rs = pstmt.executeQuery();

            logger.log(Level.INFO, new LoggerMessage("Project", new Long(projectId), null,
                    "update project_result with final scores, placed and passed_review_ind."));
            updateStmt = conn.prepareStatement(APPEAL_RESPONSE_UPDATE_PROJECT_RESULT_STMT);
            while (rs.next()) {
                double finalScore = rs.getDouble("final_score");
                long userId = rs.getLong("user_id");
                int status = rs.getInt("submission_status_id");
                String p = null;

                double payment = 0;
                p = rs.getString("payment");
                if (p != null) {
                    try {
                        payment = Double.parseDouble(p);
                    } catch (Exception e) {
                        // Ignore
                    }
                }

                int placed = 0;
                p = rs.getString("placed");
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
                if (payment == 0) {
                    updateStmt.setNull(4, Types.DOUBLE);
                } else {
                    updateStmt.setDouble(4, payment);
                }
                updateStmt.setLong(5, projectId);
                updateStmt.setLong(6, userId);
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
     * @param obj
     *            the jdbc resource object
     */
    static void close(Object obj) {
        if (obj instanceof Connection) {
            try {
                ((Connection) obj).close();
                logger.log(Level.INFO, "close the connection");
            } catch (Exception e) {
                // Just ignore
            }
        } else if (obj instanceof PreparedStatement) {
            try {
                ((PreparedStatement) obj).close();
            } catch (Exception e) {
                // Just ignore
            }
        }
    }

    /**
     * Finds whether the project id belongs to a studio project or not.
     *
     * @param projectId
     *            the project d.
     * @return true if it is a studio project.
     */
    static boolean isStudioProject(long projectId) {
        try {
            ProjectManager projectManager = new ProjectManagerImpl();
            return "Studio".equals(projectManager.getProject(projectId).getProjectCategory().getProjectType().getName());
        } catch (BaseException e) {
            return false;
        }
    }

    /**
     * Finds the project status with the given name.
     *
     * @param projectManager
     *            The ProjectManager instance
     * @param statusName
     *            the status name
     * @return the matched project status
     * @throws PhaseHandlingException
     *             If any problem to find the project status
     * @since Online Review Payments and Status Automation Assembly 1.0
     */
    static ProjectStatus findProjectStatusByName(ProjectManager projectManager, String statusName)
        throws PhaseHandlingException {
        try {
            ProjectStatus[] statuses = projectManager.getAllProjectStatuses();

            ProjectStatus result = null;
            for (ProjectStatus status : statuses) {
                if (statusName.equals(status.getName())) {
                    result = status;
                }
            }

            if (result == null) {
                throw new PhaseHandlingException(
                        "There is no corresponding record in persistence for status with name - " + statusName);
            }

            return result;
        } catch (PersistenceException e) {
            throw new PhaseHandlingException("Fail to retrieve all project statuses", e);
        }
    }

    /**
     * Updates the project status to "Completed".
     *
     * @param managerHelper
     *            the ManagerHelper instance.
     * @param phase
     *            the phase
     * @param operator
     *            the operator
     * @throws PhaseHandlingException
     *             If any error occurs while updating the project status.
     * @since Online Review Payments and Status Automation Assembly 1.0
     */
    static void completeProject(ManagerHelper managerHelper, Phase phase, String operator)
        throws PhaseHandlingException {
        try {
            ProjectManager projectManager = managerHelper.getProjectManager();
            com.topcoder.management.project.Project project = projectManager.getProject(phase.getProject().getId());

            Format format = new SimpleDateFormat(dateFormat);
            project.setProperty("Completion Timestamp", format.format(new Date()));

            ProjectStatus completedStatus = PRHelper.findProjectStatusByName(projectManager, "Completed");
            project.setProjectStatus(completedStatus);
            projectManager.updateProject(project, "Setting the project status to Completed automatically", operator);
        } catch (PersistenceException e) {
            throw new PhaseHandlingException("Problem when updating project", e);
        } catch (ValidationException e) {
            throw new PhaseHandlingException("Problem when updating project", e);
        }
    }
}
