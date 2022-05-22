/*
 * Copyright (C) 2005 - 2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import com.cronos.onlinereview.phases.logging.LoggerMessage;
import com.topcoder.onlinereview.component.deliverable.Submission;
import com.topcoder.onlinereview.component.deliverable.SubmissionFilterBuilder;
import com.topcoder.onlinereview.component.deliverable.UploadManager;
import com.topcoder.onlinereview.component.exception.BaseException;
import com.topcoder.onlinereview.component.project.management.PersistenceException;
import com.topcoder.onlinereview.component.project.management.Project;
import com.topcoder.onlinereview.component.project.management.ProjectManager;
import com.topcoder.onlinereview.component.project.management.ProjectStatus;
import com.topcoder.onlinereview.component.project.management.ValidationException;
import com.topcoder.onlinereview.component.project.phase.ManagerHelper;
import com.topcoder.onlinereview.component.project.phase.Phase;
import com.topcoder.onlinereview.component.project.phase.PhaseHandlingException;
import com.topcoder.onlinereview.component.project.phase.handler.Constants;
import com.topcoder.onlinereview.component.project.phase.handler.LookupHelper;
import com.topcoder.onlinereview.component.project.phase.handler.PhasesHelper;
import com.topcoder.onlinereview.component.review.Review;
import com.topcoder.onlinereview.component.search.SearchBuilderException;
import com.topcoder.onlinereview.component.search.filter.AndFilter;
import com.topcoder.onlinereview.component.search.filter.EqualToFilter;
import com.topcoder.onlinereview.component.search.filter.Filter;
import com.topcoder.onlinereview.component.search.filter.NotFilter;
import com.topcoder.onlinereview.component.search.filter.OrFilter;
import com.topcoder.util.log.Level;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.topcoder.onlinereview.util.CommonUtils.executeSqlWithParam;
import static com.topcoder.onlinereview.util.CommonUtils.executeUpdateSql;
import static com.topcoder.onlinereview.util.CommonUtils.getDouble;
import static com.topcoder.onlinereview.util.CommonUtils.getInt;
import static com.topcoder.onlinereview.util.CommonUtils.getLong;
import static com.topcoder.onlinereview.util.SpringUtils.getTcsJdbcTemplate;

/**
 * The PRHelper which is used to provide helper method for Phase Handler.
 *
 * <p>
 * Thread-safety: This class is thread-safe as it does not contain any mutable inner state.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class PRHelper {

    private static final com.topcoder.util.log.Log logger = com.topcoder.util.log.LogManager.getLog(PRHelper.class
            .getName());
    // OrChange : Modified the statement to take the placed and final score from submission table
    /**
     * The SQL query to populate submissions data to be inserted to project_result table.
     */
    private static final String APPEAL_RESPONSE_SELECT_STMT = "select s.final_score as final_score, "
            + " ri_u.value as user_id, " + "    s.placement as placed, "
            + "    r.project_id, "
            + " s.submission_status_id " + "from resource r, " + "  resource_info ri_u,"
            + " upload u," + "  submission s " + "where  r.resource_id = ri_u.resource_id "
            + "and s.submission_type_id = 1 "
            + "and ri_u.resource_info_type_id = 1 " + "and u.project_id = r.project_id "
            + "and u.resource_id = r.resource_id " + "and upload_type_id = 1 " + "and u.upload_id = s.upload_id "
            + "and r.project_id = ? " + "and r.resource_role_id = 1 and s.submission_status_id != 5 ";

    private static final String APPEAL_RESPONSE_UPDATE_PROJECT_RESULT_STMT = "update project_result set final_score = ?, placed = ?, passed_review_ind = ? "
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

    private static final String REVIEW_UPDATE_PROJECT_RESULT_STMT = "update project_result set valid_submission_ind = 1, rating_ind = 1, raw_score = ?  "
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
     * This member variable holds the formatting string used to format dates.
     */
    private static final String dateFormat = "MM.dd.yyyy HH:mm z";

    /**
     * Pull data to project_result.
     */
    void processRegistrationPR(long projectId, boolean toStart) {
        if (toStart) {
            logger.log(Level.INFO,
                new LoggerMessage("project", projectId, null, "start registration process."));
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
    void processSubmissionPR(long projectId, boolean toStart) throws PhaseHandlingException {
        if (!toStart) {
            logger.log(Level.INFO,
                new LoggerMessage("project", projectId, null, "process submission phase."));
            executeUpdateSql(getTcsJdbcTemplate(), UPDATE_PROJECT_RESULT_STMT, newArrayList(projectId));
        }
    }

    /**
     * Pull data to project_result.
     *
     * @param projectId
     *            the projectId
     * @param toStart
     *            whether the phase is to start or not.
     * @param operator
     *            the operator.
     * @throws PhaseHandlingException
     *             if error occurs
     */
    void processScreeningPR(long projectId, boolean toStart, String operator) throws PhaseHandlingException {
        if (!toStart) {
            logger.log(Level.INFO,
                new LoggerMessage("project", projectId, null, "process screening phase."));
            // Update all users who failed to pass screen, set valid_submission_ind = 0
            executeUpdateSql(getTcsJdbcTemplate(), FAILED_PASS_SCREENING_STMT, newArrayList(projectId));

            // Update all users who pass screen, set valid_submission_ind = 1
            executeUpdateSql(getTcsJdbcTemplate(), PASS_SCREENING_STMT, newArrayList(projectId));

            PaymentsHelper.processAutomaticPayments(projectId, operator);
        }
    }

    /**
     * Pull data to project_result for software competitions; update submitter's payments and complete project
     * for Studio competitions.
     *
     * @param managerHelper
     *            the <code>ManagerHelper</code> instance.
     * @param phase
     *            the corresponding review phase.
     * @param operator
     *            the operator.
     * @param toStart
     *            whether the phase is to start or not.
     * @throws PhaseHandlingException
     *             if error occurs
     */
    void processReviewPR(ManagerHelper managerHelper, Phase phase, String operator, boolean toStart) throws PhaseHandlingException {
        long projectId = phase.getProject().getId();
        boolean paymentsProcessed = false;
        try {
            if (!toStart) {
                logger.log(Level.INFO,
                        new LoggerMessage("project", projectId, null, "process review phase."));

                // if review phase is last one and there is at least one active submission complete the project.
                if (isLastPhase(phase)) {
                    Submission[] activeSubs = PhasesHelper.getActiveProjectSubmissions(managerHelper.getUploadManager(),
                        projectId, Constants.SUBMISSION_TYPE_CONTEST_SUBMISSION);
                    if (activeSubs.length > 0) {
                        completeProject(managerHelper, phase, operator);
                    }
                }

                if (!isStudioProject(managerHelper.getProjectManager(), projectId)) {
                    // Retrieve all
                    List<Map<String, Object>> rs = executeSqlWithParam(getTcsJdbcTemplate(), REVIEW_SELECT_STMT, newArrayList(projectId));

                    for (Map<String, Object> r: rs) {
                        // Update all raw score
                        double rawScore = getDouble(r, "raw_score");
                        long userId = getLong(r, "user_id");
                        executeUpdateSql(getTcsJdbcTemplate(), REVIEW_UPDATE_PROJECT_RESULT_STMT, newArrayList(rawScore, projectId, userId));
                    }

                    Phase appealsResponsePhase = PhasesHelper.locatePhase(phase, "Appeals Response", true, false);
                    if (appealsResponsePhase == null) {
                        // populate project result
                        populateProjectResult(projectId, operator);
                        paymentsProcessed = true;
                    }
                }

                if (!paymentsProcessed) {
                    PaymentsHelper.processAutomaticPayments(projectId, operator);
                }
            }
        } catch(SQLException e) {
            throw new PhaseHandlingException("Failed to push data to project_result", e);
        }
    }

    /**
     * Pull data to project_result for while appeal response phase closed.
     *
     * @param managerHelper
     *            the <code>ManagerHelper</code> instance.
     * @param phase
     *            the corresponding review phase.
     * @param toStart
     *            whether the phase is to start or not.
     * @param operator
     *            the operator.
     * @throws PhaseHandlingException
     *             if error occurs
     */
    void processAppealResponsePR(ManagerHelper managerHelper, Phase phase, boolean toStart, String operator) throws PhaseHandlingException {
        long projectId = phase.getProject().getId();

        try {
            if (!toStart) {
                logger.log(Level.INFO,
                    new LoggerMessage("project", projectId, null, "process Appeal Response phase."));

                // if appeals response phase is last one and there is at least one active submission complete the project.
                if (isLastPhase(phase)) {
                    Submission [] activeSubs = PhasesHelper.getActiveProjectSubmissions(managerHelper.getUploadManager(),
                            projectId, Constants.SUBMISSION_TYPE_CONTEST_SUBMISSION);
                    if (activeSubs.length > 0) {
                        completeProject(managerHelper, phase, operator);
                    }
                }

                populateProjectResult(projectId, operator);
            }
        } catch(SQLException e) {
            throw new PhaseHandlingException("Failed to push data to project_result", e);
        }
    }

    /**
     * Pull data to project_result.
     *
     * @param projectId
     *            the projectId
     * @param toStart
     *            whether the phase is to start or not.
     * @param operator
     *            the operator.
     * @throws PhaseHandlingException
     *             if error occurs
     */
    void processAggregationPR(long projectId,  boolean toStart, String operator) throws PhaseHandlingException {
        try {
            if (!toStart) {
                logger.log(Level.INFO,
                        new LoggerMessage("project", projectId, null, "process Aggregation phase."));
                populateProjectResult(projectId, operator);
            }
        } catch(SQLException e) {
            throw new PhaseHandlingException("Failed to push data to project_result", e);
        }
    }

    /**
     * Pull data to project_result.
     *
     * @param projectId
     *            the projectId
     * @param toStart
     *            whether the phase is to start or not.
     * @param operator
     *            the operator.
     * @throws PhaseHandlingException
     *             if error occurs
     */
    void processFinalFixPR(long projectId, boolean toStart, String operator) throws PhaseHandlingException {
        try {
            if (!toStart) {
                logger.log(Level.INFO,
                        new LoggerMessage("project", projectId, null, "Process final fix phase."));
                populateProjectResult(projectId, operator);
            }
        } catch(SQLException e) {
            throw new PhaseHandlingException("Failed to push data to project_result", e);
        }
    }

    /**
     * Pull data to project_result.
     *
     * @param projectId
     *            the projectId
     * @param toStart
     *            whether the phase is to start or not.
     * @param operator
     *            the operator.
     * @throws PhaseHandlingException
     *             if error occurs
     */
    void processFinalReviewPR(long projectId, boolean toStart, String operator) throws PhaseHandlingException {
        try {
            if (!toStart) {
                logger.log(Level.INFO,
                        new LoggerMessage("project", projectId, null, "Process final review phase."));
                populateProjectResult(projectId, operator);
            } else {
                logger.log(Level.INFO,
                        new LoggerMessage("project", projectId, null, "start final review phase."));
            }
        } catch(SQLException e) {
            throw new PhaseHandlingException("Failed to push data to project_result", e);
        }
    }

    /**
     * Pull data to project_result, populate reviewer payments and clear copilot payments.
     *
     * @param projectId
     *            the projectId
     * @param toStart
     *            whether the phase is to start or not.
     * @param operator
     *            the operator.
     * @throws PhaseHandlingException
     *             if error occurs
     */
    void processPostMortemPR(long projectId, boolean toStart, String operator) throws PhaseHandlingException {
        try {
            if (!toStart) {
                logger.log(Level.INFO,
                        new LoggerMessage("project", projectId, null, "Process post mortem phase."));
                populateProjectResult(projectId, operator);
            } else {
                logger.log(Level.INFO,
                        new LoggerMessage("project", projectId, null, "start post mortem phase."));
            }
        } catch(SQLException e) {
            throw new PhaseHandlingException("Failed to push data to project_result", e);
        }
    }

    /**
     * Populate final_score, placed and passed_review_ind.
     *
     * @param projectId
     *            project id
     * @param operator
     *            the operator.
     * @throws SQLException
     *             if error occurs
     */
    public static void populateProjectResult(long projectId, String operator)
            throws SQLException, PhaseHandlingException {
        // Payment should be set before populate to project_result table
        PaymentsHelper.processAutomaticPayments(projectId, operator);
        PaymentsHelper.updateProjectResultPayments(projectId);

            // Retrieve all
            List<Map<String, Object>> rs = executeSqlWithParam(getTcsJdbcTemplate(), APPEAL_RESPONSE_SELECT_STMT, newArrayList(projectId));


            logger.log(Level.DEBUG, new LoggerMessage("Project", projectId, null,
                    "update project_result with final scores, placed and passed_review_ind."));
            for (Map<String, Object> r: rs) {
                List<Object> params = new ArrayList<>();
                int status = getInt(r, "submission_status_id");
                // Update final score, placed and passed_review_ind
                params.add(getDouble(r, "final_score"));
                params.add(getInt(r, "placed"));
                // 1 is active, 4 is Completed Without Win
                params.add(status == 1 || status == 4 ? 1 : 0);
                params.add(projectId);
                params.add(getLong(r, "user_id"));
                executeUpdateSql(getTcsJdbcTemplate(), APPEAL_RESPONSE_UPDATE_PROJECT_RESULT_STMT, params);
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
                logger.log(Level.DEBUG, "close the connection");
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
    static boolean isStudioProject(ProjectManager projectManager, long projectId) {
        try {
            return "Studio".equals(projectManager.getProject(projectId).getProjectCategory().getProjectType().getName());
        } catch (BaseException e) {
            return false;
        }
    }

    /**
     * Checks if the given phase is the last phase in the project. Note that if multiple
     * phases end at the same date/time at the end of the project, all they are
     * considered to be last phases of the project.
     * @param phase
     *            the phase to be checked.
     * @return true if phase is the last phase in the project, false otherwise.
     */
    static boolean isLastPhase(Phase phase) {
        // Get all phases for the project
        Phase[] phases = phase.getProject().getAllPhases();

        // Get index of the input phase in phases array
        int phaseIndex = 0;

        for (int i = 0; i < phases.length; i++) {
            if (phases[i].getId() == phase.getId()) {
                phaseIndex = i;
            }
        }

        Date endDate = phases[phaseIndex].calcEndDate();
        for (int i=0; i < phases.length; i++) {
            // Dirty fix for Studio F2F contests - Review phase should be considered as "last" even if the
            // Registration and Submission phases are still open and scheduled to close after Review.
            String phaseType = phases[i].getPhaseType().getName();
            if (Constants.PHASE_REGISTRATION.equals(phaseType) || Constants.PHASE_SUBMISSION.equals(phaseType)) {
                continue;
            }
            
            if (i != phaseIndex && phases[i].calcEndDate().after(endDate)) {
                return false;
            }
        }

        return true;
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
     */
    static void completeProject(ManagerHelper managerHelper, Phase phase, String operator)
        throws PhaseHandlingException {
        try {
            ProjectManager projectManager = managerHelper.getProjectManager();
            Project project = projectManager.getProject(phase.getProject().getId());

            Format format = new SimpleDateFormat(dateFormat);
            project.setProperty("Completion Timestamp", format.format(new Date()));

            ProjectStatus completedStatus = PRHelper.findProjectStatusByName(projectManager, "Completed");
            project.setProjectStatus(completedStatus);
            projectManager.updateProject(project, "Setting the project status to Completed automatically", operator);

            AmazonSNSHelper.publishProjectUpdateEvent(project);
        } catch (PersistenceException e) {
            throw new PhaseHandlingException("Problem when updating project", e);
        } catch (ValidationException e) {
            throw new PhaseHandlingException("Problem when updating project", e);
        }
    }

    /**
     * Gets all the reviews for a given project.
     *
     * @param managerHelper the <code>ManagerHelper</code> instance.
     * @param projectId the id of the given project.
     * @param complete true if to retrieve complete review data structure, false otherwise.
     * @return all the reviews of the given project.
     * @throws PhaseHandlingException if there was an error during retrieval.
     */
    static Review[] searchReviewsForProject(ManagerHelper managerHelper, long projectId, boolean complete)
            throws PhaseHandlingException {
        Filter filter = new EqualToFilter("project", projectId);
        return managerHelper.getReviewManager().searchReviews(filter, complete);
    }

    /**
     * Retrieves all non-deleted submitters' submissions for the given project id.
     * @param uploadManager
     *            UploadManager instance to use for searching.
     * @param projectId
     *            project id.
     * @return all non-deleted submissions for the given project id.
     * @throws PhaseHandlingException
     *             if an error occurs during retrieval.
     */
    static Submission[] getNonDeletedProjectSubmitterSubmissions(UploadManager uploadManager, long projectId)
            throws PhaseHandlingException {
        try {
            Filter projectIdFilter = SubmissionFilterBuilder.createProjectIdFilter(projectId);
            Filter nonDeletedFilter = new NotFilter(SubmissionFilterBuilder.createSubmissionStatusIdFilter(
                    LookupHelper.getSubmissionStatus(uploadManager, Constants.SUBMISSION_STATUS_DELETED).getId()));
            Filter typeFilter = new OrFilter(
                    SubmissionFilterBuilder.createSubmissionTypeIdFilter(LookupHelper.getSubmissionType(
                            uploadManager, Constants.SUBMISSION_TYPE_CONTEST_SUBMISSION).getId()),
                    SubmissionFilterBuilder.createSubmissionTypeIdFilter(LookupHelper.getSubmissionType(
                            uploadManager, Constants.SUBMISSION_TYPE_CHECKPOINT_SUBMISSION).getId())
            );
            Filter fullFilter = new AndFilter(Arrays.asList(projectIdFilter, nonDeletedFilter, typeFilter));
            return uploadManager.searchSubmissions(fullFilter);
        } catch (SearchBuilderException e) {
            throw new PhaseHandlingException("There was a search builder error", e);
        }
    }
}
