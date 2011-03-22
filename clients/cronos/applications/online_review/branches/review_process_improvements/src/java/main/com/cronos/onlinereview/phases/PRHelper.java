/**
 * Copyright (C) 2005 - 2011 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.MessageFormat;

import com.cronos.onlinereview.external.ExternalUser;
import com.cronos.onlinereview.phases.logging.LoggerMessage;
import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.deliverable.UploadManager;
import com.topcoder.management.phase.PhaseHandlingException;
import com.topcoder.management.phase.PhaseManagementException;
import com.topcoder.management.project.Project;
import com.topcoder.management.project.ProjectManager;
import com.topcoder.management.project.ProjectManagerImpl;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.resource.ResourceRole;
import com.topcoder.management.resource.search.ResourceFilterBuilder;
import com.topcoder.message.email.EmailEngine;
import com.topcoder.message.email.TCSEmailMessage;
import com.topcoder.search.builder.filter.AndFilter;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.util.errorhandling.BaseException;
import com.topcoder.util.file.DocumentGenerator;
import com.topcoder.util.file.Template;
import com.topcoder.util.file.fieldconfig.Field;
import com.topcoder.util.file.fieldconfig.Node;
import com.topcoder.util.file.fieldconfig.TemplateFields;
import com.topcoder.util.log.Level;

/**
 * The PRHelper which is used to provide helper method for Phase Handler.
 * 
 * <p>
 * Version 1.1 (Online Review Update Review Management Process assembly 2) Change notes:
 *   <ol>
 *     <li>Added {@link #PROJECT_NAME} constant.</li>
 *     <li>Added {@link #sendMailForWinners(ManagerHelper, Project, String, String, String, String)} and
 *     {@link #sendWinnersEmailForUser(ManagerHelper, Project, ExternalUser, String, String, String, String, String)} and
 *     {@link #getResourceForProjectAndUser(ManagerHelper, Project, String)} and
 *     {@link #setTemplateFieldValues(ManagerHelper, TemplateFields, Project, ExternalUser, String)} and
 *     {@link #getEmailTemplate(String, String)} to send email to the winners.</li>
 *   </ol>
 * </p>
 * 
 * @author brain_cn, TCSASSEMBER
 * @version 1.1
 */
public class PRHelper {
    private static final com.topcoder.util.log.Log logger = com.topcoder.util.log.LogFactory.getLog(PRHelper.class
            .getName());
    
    /**
     * Constant for "Project Name" project info.
     * 
     * @since 1.1
     */
    private static final String PROJECT_NAME = "Project Name";
    
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
     * Pull data to project_result.
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
    public static void resetProjectResultWithChangedScores(long projectId, Object userID, Connection conn)
            throws SQLException {
        logger.log(Level.INFO,
                new LoggerMessage("project", new Long(projectId), null, "reset update_result and user_reliability."));
        // reset old_reliability, new_reliability, current_reliability_ind, reliable_submission_ind,
        // reliability_ind
        String sqlStr = "update project_result set old_reliability = null, new_reliability = null, current_reliability_ind = null,"
                + "reliable_submission_ind = null, reliability_ind = null where project_id = ? and user_id = ?";

        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sqlStr);
            pstmt.setLong(1, projectId);
            pstmt.setString(2, userID.toString());
            pstmt.executeUpdate();
        } finally {
            close(pstmt);
        }

        long categoryId = AutoPaymentUtil.getProjectCategoryId(projectId, conn);
        long phaseId = 111 + categoryId;

        // Reset user_reliability rating by phase_id, user_id
        // without any reliable_submission_ind is 1 for this category, rating should be set to null
        sqlStr = "update user_reliability set rating = null where user_id = ? " + " and phase_id = ? "
                + " and not exists (select * from project_result where user_id = ? and reliable_submission_ind = 1"
                + " and project_id in (select project_id from project where project_category_id = ?))";
        try {
            pstmt = conn.prepareStatement(sqlStr);
            pstmt.setString(1, userID.toString());
            pstmt.setLong(2, phaseId);
            pstmt.setString(3, userID.toString());
            pstmt.setLong(4, categoryId);
            pstmt.executeUpdate();
        } finally {
            close(pstmt);
        }

        // maybe others placement will be changed
        populateProjectResult(projectId, conn);
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
     * Send notification email for the winners.
     * 
     * @param project
     *            the project
     * @throws Exception
     *             if any error occurs when sending the email
     * @since 1.1
     */
    public static void sendMailForWinners(ManagerHelper managerHelper, Project project, String winnersEmailSubject, String winnersEmailFromAddress,
            String winnersEmailTemplateSource, String winnersEmailTemplateName) throws Exception {
        logger.log(Level.DEBUG, "we're in the send email method");

        String winnerId = (String) project.getProperty("Winner External Reference ID");
        String runnerUpId = (String) project.getProperty("Runner-up External Reference ID");

        String sendWinnerEmail = (String) project.getProperty("Send Winner Emails");

        if (sendWinnerEmail == null || !sendWinnerEmail.equalsIgnoreCase("true")) {
            return;
        }

        if (winnerId != null) {
            sendWinnersEmailForUser(managerHelper, project, managerHelper.getUserRetrieval().retrieveUser(Long.parseLong(winnerId)),
                    "1st", winnersEmailSubject, winnersEmailFromAddress, winnersEmailTemplateSource, winnersEmailTemplateName);
        }
        if (runnerUpId != null) {
            sendWinnersEmailForUser(managerHelper, project, managerHelper.getUserRetrieval().retrieveUser(Long.parseLong(runnerUpId)),
                    "2nd", winnersEmailSubject, winnersEmailFromAddress, winnersEmailTemplateSource, winnersEmailTemplateName);
        }
    }

    /**
     * Send notification email to a specific user.
     * 
     * @param project
     *            the project
     * @param user
     *            the user to send email to
     * @param position
     *            the placement
     * @throws Exception
     *             if any error occurs when sending the email
     * @since 1.1
     */
    private static void sendWinnersEmailForUser(ManagerHelper managerHelper, Project project, ExternalUser user, String position, String winnersEmailSubject, String winnersEmailFromAddress,
            String winnersEmailTemplateSource, String winnersEmailTemplateName) throws Exception {
        DocumentGenerator docGenerator = DocumentGenerator.getInstance();
        Template template = getEmailTemplate(winnersEmailTemplateSource, winnersEmailTemplateName);
        logger.log(Level.DEBUG, "sending winner email for projectId: " + project.getId() + " handle: " + user.getHandle()
                + " position: " + position);
        TemplateFields root = setTemplateFieldValues(managerHelper, docGenerator.getFields(template), project, user, position);

        String emailContent = docGenerator.applyTemplate(root);
        TCSEmailMessage message = new TCSEmailMessage();
        message.setSubject(MessageFormat.format(winnersEmailSubject,
                new Object[] { "Online Review", project.getProperty(PROJECT_NAME) }));
        message.setBody(emailContent);
        message.setFromAddress(winnersEmailFromAddress);
        message.setToAddress(user.getEmail(), TCSEmailMessage.TO);
        EmailEngine.send(message);
    }

    /**
     * Gets the submitter resource for a project.
     * 
     * @param project
     *            the projcet
     * @param userId
     *            the user id of the resource
     * @return the submitter resource
     * @throws PhaseManagementException
     *             if any error when getting the resource
     * @since 1.1
     */
    private static Resource getResourceForProjectAndUser(ManagerHelper managerHelper, Project project, String userId) throws PhaseManagementException {
        try {
            ResourceRole submitterRole = null;
            ResourceRole[] roles = managerHelper.getResourceManager().getAllResourceRoles();
            logger.log(Level.DEBUG, "roles size: " + roles.length);
            for (int i = 0; i < roles.length; i++) {
                ResourceRole role = roles[i];
                logger.log(Level.DEBUG, "roles id: " + roles[i].getId() + ", name: " + roles[i].getName());
                if ("Submitter".equals(role.getName())) {
                    submitterRole = role;
                    break;
                }
            }
            if (submitterRole == null) {
                throw new PhaseHandlingException("can't find submitter role id");
            }
            // Create filter to filter only the resources for the project in question
            Filter filterProject = ResourceFilterBuilder.createProjectIdFilter(project.getId());
            Filter filterManager = ResourceFilterBuilder.createResourceRoleIdFilter(submitterRole.getId());
            // Create combined final filter
            Filter filter = new AndFilter(filterProject, filterManager);

            // Perform search for resources
            Resource[] submitters = managerHelper.getResourceManager().searchResources(filter);
            for (int i = 0; i < submitters.length; i++) {
                Resource resource = submitters[i];
                if (userId.equals(resource.getProperty("External Reference ID"))) {
                    return resource;
                }
            }
        } catch (Exception e) {
            throw new PhaseManagementException(e.getMessage(), e);
        }
        throw new PhaseHandlingException("couldn't found the resource for userId: " + userId + " projectId: "
                + project.getId());
    }

    /**
     * Set values for template field.
     * 
     * @param root
     *            the root of the template fields.
     * @param project
     *            the project
     * @param user
     *            the user to send email to
     * @param position
     *            the placement
     * @return the <code>TempalteFields</code> instance.
     * @throws BaseException
     *             if any error occurs when set the values for tempalte field
     * @since 1.1
     */
    private static TemplateFields setTemplateFieldValues(ManagerHelper managerHelper, TemplateFields root, Project project, ExternalUser user,
            String position) throws BaseException {
        Node[] nodes = root.getNodes();

        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i] instanceof Field) {
                Field field = (Field) nodes[i];

                if ("PROJECT_TYPE".equals(field.getName())) {
                    field.setValue(project.getProjectCategory().getDescription());
                } else if ("PROJECT_NAME".equals(field.getName())) {
                    field.setValue((String) project.getProperty(PROJECT_NAME));
                } else if ("SCORE".equals(field.getName())) {
                    // get all the submissions for the user in the project
                    Long[] submissions = getResourceForProjectAndUser(managerHelper, project, String.valueOf(user.getId()))
                            .getSubmissions();
                    int placement = position.equals("1st") ? 1 : 2;
                    UploadManager uploadManager = managerHelper.getUploadManager();
                    for (Long submissionId : submissions) {
                        // get the score for the placement depending on the position
                        Submission submission = uploadManager.getSubmission(submissionId);
                        if (submission.getPlacement() != null && submission.getPlacement() == placement) {
                            field.setValue(submission.getFinalScore() + "");
                            break;
                        }
                    }
                } else if ("PLACE".equals(field.getName())) {
                    field.setValue(position);
                }
            }
        }

        return root;
    }

    /**
     * Gets email template for winner email.
     * 
     * @return the template for winer email
     * @throws Exception
     *             if any error occurs when getting the email template
     * @since 1.1
     */
    private static Template getEmailTemplate(String winnersEmailTemplateSource, String winnersEmailTemplateName) throws Exception {
        return DocumentGenerator.getInstance().getTemplate(winnersEmailTemplateSource, winnersEmailTemplateName);
    }
}
