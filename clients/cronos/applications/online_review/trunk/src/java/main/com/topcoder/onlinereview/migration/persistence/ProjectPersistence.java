/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration.persistence;

import com.topcoder.onlinereview.migration.DatabaseUtils;
import com.topcoder.onlinereview.migration.Util;
import com.topcoder.onlinereview.migration.dto.newschema.deliverable.Submission;
import com.topcoder.onlinereview.migration.dto.newschema.deliverable.Upload;
import com.topcoder.onlinereview.migration.dto.newschema.phase.Phase;
import com.topcoder.onlinereview.migration.dto.newschema.phase.PhaseCriteria;
import com.topcoder.onlinereview.migration.dto.newschema.phase.PhaseDependency;
import com.topcoder.onlinereview.migration.dto.newschema.project.ProjectNew;
import com.topcoder.onlinereview.migration.dto.newschema.project.ProjectAudit;
import com.topcoder.onlinereview.migration.dto.newschema.project.ProjectInfo;
import com.topcoder.onlinereview.migration.dto.newschema.resource.Notification;
import com.topcoder.onlinereview.migration.dto.newschema.resource.Resource;
import com.topcoder.onlinereview.migration.dto.newschema.resource.ResourceInfo;
import com.topcoder.onlinereview.migration.dto.newschema.resource.ResourceSubmission;
import com.topcoder.onlinereview.migration.dto.newschema.review.Review;
import com.topcoder.onlinereview.migration.dto.newschema.review.ReviewComment;
import com.topcoder.onlinereview.migration.dto.newschema.review.ReviewItem;
import com.topcoder.onlinereview.migration.dto.newschema.review.ReviewItemComment;
import com.topcoder.onlinereview.migration.dto.newschema.screening.ScreeningResult;
import com.topcoder.onlinereview.migration.dto.newschema.screening.ScreeningTask;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;


/**
 * The Persistence is used to persist project transformed data.
 *
 * @author brain_cn
 * @version 1.0
 */
public class ProjectPersistence extends DatabaseUtils {
    private Connection conn = null;

    /**
     * Creates a new Persistence object.
     *
     * @param conn the connection to persist data
     */
    public ProjectPersistence(Connection conn) {
        this.conn = conn;
    }

    /**
     * Store Project and project audit data to new online review schema
     *
     * @param input the Project data
     *
     * @throws SQLException if error occurs while execute sql statement
     */
    public void storeProjects(List input) throws SQLException {
    	long startTime = Util.start("storeProjects");
        for (Iterator iter = input.iterator(); iter.hasNext();) {
        	storeProject((ProjectNew) iter.next());
        }

        Util.logAction(input.size(), "storeProjects", startTime);
    }

    /**
     * Store Project and project audit data to new online review schema
     *
     * @param input the Project data
     *
     * @throws SQLException if error occurs while execute sql statement
     */
    public boolean storeProject(ProjectNew table) throws SQLException {
    	long startTime = Util.startMain("storeProject");
        String[] fieldnames = {
                "project_id", "project_status_id", "project_category_id", "create_user", "create_date", "modify_user",
                "modify_date"
            };

        // store Project data to new online review schema
        PreparedStatement stmt = conn.prepareStatement(makeInsertSql(ProjectNew.TABLE_NAME, fieldnames));

        boolean successful = true;
        conn.setAutoCommit(false);
        try {
            int i = 1;
            stmt.setInt(i++, table.getProjectId());
            stmt.setInt(i++, table.getProjectStatusId());
            stmt.setInt(i++, table.getProjectCategoryId());
            stmt.setString(i++, table.getCreateUser());
            stmt.setDate(i++, new Date(table.getCreateDate().getTime()));
            stmt.setString(i++, table.getModifyUser());
            stmt.setDate(i++, new Date(table.getModifyDate().getTime()));
            stmt.execute();
            storeProjectInfo(table.getProjectInfos());
            storeProjectAudit(table.getProjectAudits());
            storePhase(table.getPhases());
            storePhaseDependency(table.getPhaseDependencys());
            storeResource(table.getResources());
            storeResourceInfo(table.getResourceInfos());
            storeUpload(table.getUploads());
            storeSubmission(table.getSubmissions());
            storeNotification(table.getNotifications());
            storeResourceSubmission(table.getResourceSubmissions());
            storeScreeningTask(table.getScreeningTasks());
            storeReview(table.getReviews());
            conn.commit();
        } catch(Exception e) {
        	successful = false;
        	conn.rollback();
        	Util.warn(e);
        	Util.warn("Failed to store project, project_id:" + table.getProjectId());
        }

        Util.logMainAction("storeProject", startTime);
        DatabaseUtils.closeStatementSilently(stmt);
        return successful;
    }

    /**
     * Store ReviewItem to new online review schema
     *
     * @param input the ReviewItem data
     *
     * @throws SQLException if error occurs while execute sql statement
     */
    void storeReviewItem(Collection input) throws SQLException {
    	long startTime = Util.start("storeReviewItem");
        String[] fieldnames = {
                "review_item_id", "review_id", "scorecard_question_id", "upload_id", "answer", "sort", 
                "create_user", "create_date", "modify_user", "modify_date"
            };

        // store ReviewItem data to new online review schema
        PreparedStatement stmt = conn.prepareStatement(makeInsertSql(ReviewItem.TABLE_NAME, fieldnames));

        for (Iterator iter = input.iterator(); iter.hasNext();) {
            ReviewItem table = (ReviewItem) iter.next();
            int i = 1;
            stmt.setInt(i++, table.getReviewItemId());
            stmt.setInt(i++, table.getReviewId());
            stmt.setInt(i++, table.getScorecardQuestionId());
            if (table.getUploadId() == 0) {
            	stmt.setNull(i++, Types.INTEGER);
            } else {
            	stmt.setInt(i++, table.getUploadId());
            }
            stmt.setString(i++, table.getAnswer());
            stmt.setInt(i++, table.getSort());
            stmt.setString(i++, table.getCreateUser());
            stmt.setDate(i++, new Date(table.getCreateDate().getTime()));
            stmt.setString(i++, table.getModifyUser());
            stmt.setDate(i++, new Date(table.getModifyDate().getTime()));
            try {
            	stmt.execute();
	        } catch(Exception e) {
	        	Util.warn(e);
	        	Util.warn("storeReviewItem, " +
	        			"reviewId: " + table.getReviewId() + 
	        			"getReviewItemId: " + table.getReviewItemId() + 
	        			"getScorecardQuestionId: " + table.getScorecardQuestionId() + 
	        			"getUploadId: " + table.getUploadId());
	        	continue;
	        }
            storeReviewItemComment(table.getReviewItemComments());
        }

        Util.logAction(input.size(), "storeReviewItem", startTime);
        DatabaseUtils.closeStatementSilently(stmt);
    }

    /**
     * Store Review to new online review schema
     *
     * @param input the Review data
     *
     * @throws SQLException if error occurs while execute sql statement
     */
    void storeReview(List input) throws SQLException {
    	long startTime = Util.start("storeReview");
        String[] fieldnames = {
                "review_id", "resource_id", "submission_id", "scorecard_id", "committed", "score",
                "create_user", "create_date", "modify_user", "modify_date"
            };

        // store Review data to new online review schema
        PreparedStatement stmt = conn.prepareStatement(makeInsertSql(Review.TABLE_NAME, fieldnames));

        for (Iterator iter = input.iterator(); iter.hasNext();) {
            Review table = (Review) iter.next();
            int i = 1;
            stmt.setInt(i++, table.getReviewId());
            stmt.setInt(i++, table.getResourceId());
            stmt.setInt(i++, table.getSubmissionId());
            stmt.setInt(i++, table.getScorecardId());
            stmt.setBoolean(i++, table.isCommitted());
            stmt.setFloat(i++, table.getScore());
            stmt.setString(i++, table.getCreateUser());
            stmt.setDate(i++, new Date(table.getCreateDate().getTime()));
            stmt.setString(i++, table.getModifyUser());
            stmt.setDate(i++, new Date(table.getModifyDate().getTime()));
            try {
            	stmt.execute();
            } catch(Exception e) {
	        	Util.warn(e);
            	Util.warn("Review, " +
            			"reviewId: " + table.getReviewId() + 
            			"getResourceId: " + table.getResourceId() + 
            			"getSubmissionId: " + table.getSubmissionId() + 
            			"getScorecardId: " + table.getScorecardId());
            	continue;
            }
            storeReviewComment(table.getReviewComments());
            storeReviewItem(table.getReviewItems());
        }

        Util.logAction(input.size(), "storeReview", startTime);
        DatabaseUtils.closeStatementSilently(stmt);
    }

    /**
     * Store ReviewItemComment to new online review schema
     *
     * @param input the ReviewItemComment data
     *
     * @throws SQLException if error occurs while execute sql statement
     */
    void storeReviewItemComment(Collection input) throws SQLException {
    	long startTime = Util.start("storeReviewItemComment");
        String[] fieldnames = {
                "review_item_comment_id", "resource_id", "review_item_id", "comment_type_id", "content", "extra_info", 
                "sort", "create_user", "create_date", "modify_user", "modify_date"
            };

        // store ReviewItemComment data to new online review schema
        PreparedStatement stmt = conn.prepareStatement(makeInsertSql(ReviewItemComment.TABLE_NAME, fieldnames));

        for (Iterator iter = input.iterator(); iter.hasNext();) {
            ReviewItemComment table = (ReviewItemComment) iter.next();
            int i = 1;
            stmt.setInt(i++, table.getReviewItemCommentId());
            stmt.setInt(i++, table.getResourceId());
            stmt.setInt(i++, table.getReviewItemId());
            stmt.setInt(i++, table.getCommentTypeId());
            stmt.setString(i++, table.getContent());
            stmt.setString(i++, table.getExtraInfo());
            stmt.setInt(i++, table.getSort());
            stmt.setString(i++, table.getCreateUser());
            stmt.setDate(i++, new Date(table.getCreateDate().getTime()));
            stmt.setString(i++, table.getModifyUser());
            stmt.setDate(i++, new Date(table.getModifyDate().getTime()));
            try {
            	stmt.execute();
            } catch(Exception e) {
	        	Util.warn(e);
            	continue;
            }
        }

        Util.logAction(input.size(), "storeReviewItemComment", startTime);
        DatabaseUtils.closeStatementSilently(stmt);
    }

    /**
     * Store ReviewComment to new online review schema
     *
     * @param input the ReviewComment data
     *
     * @throws SQLException if error occurs while execute sql statement
     */
    void storeReviewComment(List input) throws SQLException {
    	long startTime = Util.start("storeReviewComment");
        String[] fieldnames = {
                "review_comment_id", "resource_id", "review_id", "comment_type_id", "content", "extra_info", 
                "sort", "create_user", "create_date", "modify_user", "modify_date"
            };

        // store ReviewComment data to new online review schema
        PreparedStatement stmt = conn.prepareStatement(makeInsertSql(ReviewComment.TABLE_NAME, fieldnames));

        for (Iterator iter = input.iterator(); iter.hasNext();) {
            ReviewComment table = (ReviewComment) iter.next();
            int i = 1;
            stmt.setInt(i++, table.getReviewCommentId());
            stmt.setInt(i++, table.getResourceId());
            stmt.setInt(i++, table.getReviewId());
            stmt.setInt(i++, table.getCommentTypeId());
            stmt.setString(i++, table.getContent());
            stmt.setString(i++, table.getExtraInfo());
            stmt.setInt(i++, table.getSort());
            stmt.setString(i++, table.getCreateUser());
            stmt.setDate(i++, new Date(table.getCreateDate().getTime()));
            stmt.setString(i++, table.getModifyUser());
            stmt.setDate(i++, new Date(table.getModifyDate().getTime()));
            try {
            	stmt.execute();
            } catch(Exception e) {
	        	Util.warn(e);
            	continue;
            }
        }

        Util.logAction(input.size(), "storeReviewComment", startTime);
        DatabaseUtils.closeStatementSilently(stmt);
    }

    /**
     * Store Phase to new online review schema
     *
     * @param input the Phase data
     *
     * @throws SQLException if error occurs while execute sql statement
     */
    private void storePhase(List input) throws SQLException {
    	long startTime = Util.start("storePhase");
        String[] fieldnames = {
                "phase_id", "project_id", "phase_type_id", "phase_status_id", "fixed_start_time", "scheduled_start_time",
                "scheduled_end_time", "actual_start_time", "actual_end_time", "duration", "create_user", "create_date", "modify_user",
                "modify_date"
            };

        // store Phase data to new online review schema
        PreparedStatement stmt = conn.prepareStatement(makeInsertSql(Phase.TABLE_NAME, fieldnames));

        for (Iterator iter = input.iterator(); iter.hasNext();) {
            Phase table = (Phase) iter.next();
            int i = 1;
            stmt.setInt(i++, table.getPhaseId());
            stmt.setInt(i++, table.getProjectId());
            stmt.setInt(i++, table.getPhaseTypeId());
            stmt.setInt(i++, table.getPhaseStatusId());
            stmt.setDate(i++, new Date(table.getFixedStartTime().getTime()));
            stmt.setDate(i++, new Date(table.getScheduledStartTime().getTime()));
            stmt.setDate(i++, new Date(table.getScheduledEndTime().getTime()));
            stmt.setDate(i++, new Date(table.getActualStartTime().getTime()));
            stmt.setDate(i++, new Date(table.getActualEndTime().getTime()));
            stmt.setInt(i++, table.getDuration());
            stmt.setString(i++, table.getCreateUser());
            stmt.setDate(i++, new Date(table.getCreateDate().getTime()));
            stmt.setString(i++, table.getModifyUser());
            stmt.setDate(i++, new Date(table.getModifyDate().getTime()));
            try {
            	stmt.execute();
            } catch(Exception e) {
	        	Util.warn(e);
            	continue;
            }
            storePhaseCriteria(table.getCriterias());
        }

        Util.logAction(input.size(), "storePhase", startTime);
        DatabaseUtils.closeStatementSilently(stmt);
    }

    /**
     * Store PhaseDependency to new online review schema
     *
     * @param input the PhaseDependency data
     *
     * @throws SQLException if error occurs while execute sql statement
     */
    private void storePhaseDependency(List input) throws SQLException {
    	long startTime = Util.start("storePhaseDependency");
        String[] fieldnames = {
                "dependency_phase_id", "dependent_phase_id", "dependency_start", "dependent_start", "lag_time",
                "create_user", "create_date", "modify_user", "modify_date"
            };

        // store PhaseDependency data to new online review schema
        PreparedStatement stmt = conn.prepareStatement(makeInsertSql(PhaseDependency.TABLE_NAME, fieldnames));

        for (Iterator iter = input.iterator(); iter.hasNext();) {
            PhaseDependency table = (PhaseDependency) iter.next();
            int i = 1;
            stmt.setInt(i++, table.getDependencyPhaseId());
            stmt.setInt(i++, table.getDependentPhaseId());
            stmt.setBoolean(i++, table.isDependencyStart());
            stmt.setBoolean(i++, table.isDependentStart());
            stmt.setInt(i++, table.getLagTime());
            stmt.setString(i++, table.getCreateUser());
            stmt.setDate(i++, new Date(table.getCreateDate().getTime()));
            stmt.setString(i++, table.getModifyUser());
            stmt.setDate(i++, new Date(table.getModifyDate().getTime()));
            try {
            	stmt.execute();
            } catch(Exception e) {
	        	Util.warn(e);
            	continue;
            }
        }

        Util.logAction(input.size(), "storePhaseDependency", startTime);
        DatabaseUtils.closeStatementSilently(stmt);
    }

    /**
     * Store PhaseCriteria to new online review schema
     *
     * @param input the PhaseCriteria data
     *
     * @throws SQLException if error occurs while execute sql statement
     */
    private void storePhaseCriteria(List input) throws SQLException {
    	long startTime = Util.start("storePhaseCriteria");
        String[] fieldnames = {
                "phase_id", "phase_criteria_type_id", "parameter", "create_user", "create_date", "modify_user", "modify_date"
            };

        // store PhaseCriteria data to new online review schema
        PreparedStatement stmt = conn.prepareStatement(makeInsertSql(PhaseCriteria.TABLE_NAME, fieldnames));

        for (Iterator iter = input.iterator(); iter.hasNext();) {
            PhaseCriteria table = (PhaseCriteria) iter.next();
            int i = 1;
            stmt.setInt(i++, table.getPhaseId());
            stmt.setInt(i++, table.getPhaseCriteriaTypeId());
            stmt.setString(i++, table.getParameter());
            stmt.setString(i++, table.getCreateUser());
            stmt.setDate(i++, new Date(table.getCreateDate().getTime()));
            stmt.setString(i++, table.getModifyUser());
            stmt.setDate(i++, new Date(table.getModifyDate().getTime()));
            try {
            	stmt.execute();
            } catch(Exception e) {
	        	Util.warn(e);
            	continue;
            }
        }

        Util.logAction(input.size(), "storePhaseCriteria", startTime);
        DatabaseUtils.closeStatementSilently(stmt);
    }

    /**
     * Store Upload to new online review schema
     *
     * @param input the Upload data
     *
     * @throws SQLException if error occurs while execute sql statement
     */
    private void storeUpload(List input) throws SQLException {
    	long startTime = Util.start("storeUpload");
        String[] fieldnames = {
                "upload_id", "project_id", "resource_id", "upload_type_id", "upload_status_id", "parameter",
                "create_user", "create_date", "modify_user",
                "modify_date"
            };

        // store Upload data to new online review schema
        PreparedStatement stmt = conn.prepareStatement(makeInsertSql(Upload.TABLE_NAME, fieldnames));

        for (Iterator iter = input.iterator(); iter.hasNext();) {
            Upload table = (Upload) iter.next();
            int i = 1;
            stmt.setInt(i++, table.getUploadId());
            stmt.setInt(i++, table.getProjectId());
            stmt.setInt(i++, table.getResourceId());
            stmt.setInt(i++, table.getUploadTypeId());
            stmt.setInt(i++, table.getUploadStatusId());
            stmt.setString(i++, table.getParameter());
            stmt.setString(i++, table.getCreateUser());
            stmt.setDate(i++, new Date(table.getCreateDate().getTime()));
            stmt.setString(i++, table.getModifyUser());
            stmt.setDate(i++, new Date(table.getModifyDate().getTime()));
            try {
            	stmt.execute();
            } catch(Exception e) {
	        	Util.warn(e);
            	continue;
            }
        }

        Util.logAction(input.size(), "storeUpload", startTime);
        DatabaseUtils.closeStatementSilently(stmt);
    }

    /**
     * Store Submission to new online review schema
     *
     * @param input the Submission data
     *
     * @throws SQLException if error occurs while execute sql statement
     */
    private void storeSubmission(List input) throws SQLException {
    	long startTime = Util.start("storeSubmission");
        String[] fieldnames = {
                "submission_id", "upload_id", "submission_status_id",
                "create_user", "create_date", "modify_user", "modify_date"
            };

        // store Submission data to new online review schema
        PreparedStatement stmt = conn.prepareStatement(makeInsertSql(Submission.TABLE_NAME, fieldnames));

        for (Iterator iter = input.iterator(); iter.hasNext();) {
            Submission table = (Submission) iter.next();
            int i = 1;
            stmt.setInt(i++, table.getSubmissionId());
            stmt.setInt(i++, table.getUploadId());
            stmt.setInt(i++, table.getSubmissionStatusId());
            stmt.setString(i++, table.getCreateUser());
            stmt.setDate(i++, new Date(table.getCreateDate().getTime()));
            stmt.setString(i++, table.getModifyUser());
            stmt.setDate(i++, new Date(table.getModifyDate().getTime()));
            try {
            	stmt.execute();
            } catch(Exception e) {
	        	Util.warn(e);
            	continue;
            }
        }

        Util.logAction(input.size(), "storeSubmission", startTime);
        DatabaseUtils.closeStatementSilently(stmt);
    }

    /**
     * Store ScreeningTask to new online review schema
     *
     * @param input the ScreeningTask data
     *
     * @throws SQLException if error occurs while execute sql statement
     */
    private void storeScreeningTask(List input) throws SQLException {
    	long startTime = Util.start("storeScreeningTask");
        String[] fieldnames = {
                "screening_task_id", "upload_id", "screening_status_id", "screener_id", "start_timestamp", "create_user",
                "create_date", "modify_user", "modify_date"
            };

        // store ScreeningTask data to new online review schema
        PreparedStatement stmt = conn.prepareStatement(makeInsertSql(ScreeningTask.TABLE_NAME, fieldnames));

        for (Iterator iter = input.iterator(); iter.hasNext();) {
            ScreeningTask table = (ScreeningTask) iter.next();
            int i = 1;
            stmt.setInt(i++, table.getScreeningTaskId());
            stmt.setInt(i++, table.getUploadId());
            stmt.setInt(i++, table.getScreeningStatusId());
            stmt.setInt(i++, table.getScreenerId());
            stmt.setDate(i++, new Date(table.getStartTimestamp().getTime()));
            stmt.setString(i++, table.getCreateUser());
            stmt.setDate(i++, new Date(table.getCreateDate().getTime()));
            stmt.setString(i++, table.getModifyUser());
            stmt.setDate(i++, new Date(table.getModifyDate().getTime()));
            try {
            	stmt.execute();
            } catch(Exception e) {
	        	Util.warn(e);
            	continue;
            }
            storeScreeningResult(table.getScreeningResults());
        }

        Util.logAction(input.size(), "storeScreeningTask", startTime);
        DatabaseUtils.closeStatementSilently(stmt);
    }

    /**
     * Store ScreeningResult to new online review schema
     *
     * @param input the ScreeningResult data
     *
     * @throws SQLException if error occurs while execute sql statement
     */
    private void storeScreeningResult(List input) throws SQLException {
    	long startTime = Util.start("storeScreeningResult");
        String[] fieldnames = {
                "screening_result_id", "screening_task_id", "screening_response_id", "dynamic_response_text",
                "create_user", "create_date", "modify_user", "modify_date"
            };

        // store ScreeningResult data to new online review schema
        PreparedStatement stmt = conn.prepareStatement(makeInsertSql(ScreeningResult.TABLE_NAME, fieldnames));

        for (Iterator iter = input.iterator(); iter.hasNext();) {
            ScreeningResult table = (ScreeningResult) iter.next();
            int i = 1;
            stmt.setInt(i++, table.getScreeningResultId());
            stmt.setInt(i++, table.getScreeningTaskId());
            stmt.setInt(i++, table.getScreeningResponseId());
            stmt.setString(i++, table.getDynamicResponseText() == null ? "default" : table.getDynamicResponseText());
            stmt.setString(i++, table.getCreateUser());
            stmt.setDate(i++, new Date(table.getCreateDate().getTime()));
            stmt.setString(i++, table.getModifyUser());
            stmt.setDate(i++, new Date(table.getModifyDate().getTime()));
            try {
            	stmt.execute();
            } catch(Exception e) {
	        	Util.warn(e);
            	continue;
            }
        }

        Util.logAction(input.size(), "storeScreeningResult", startTime);
        DatabaseUtils.closeStatementSilently(stmt);
    }

    /**
     * Store ResourceSubmission to new online review schema
     *
     * @param input the ResourceSubmission data
     *
     * @throws SQLException if error occurs while execute sql statement
     */
    void storeResourceSubmission(List input) throws SQLException {
    	long startTime = Util.start("storeResourceSubmission");
        String[] fieldnames = {
                "resource_id", "submission_id", "create_user", "create_date", "modify_user", "modify_date"
            };

        // store ResourceSubmission data to new online review schema
        PreparedStatement stmt = conn.prepareStatement(makeInsertSql(ResourceSubmission.TABLE_NAME, fieldnames));

        for (Iterator iter = input.iterator(); iter.hasNext();) {
            ResourceSubmission table = (ResourceSubmission) iter.next();
            int i = 1;
            stmt.setInt(i++, table.getResourceId());
            stmt.setInt(i++, table.getSubmissionId());
            stmt.setString(i++, table.getCreateUser());
            stmt.setDate(i++, new Date(table.getCreateDate().getTime()));
            stmt.setString(i++, table.getModifyUser());
            stmt.setDate(i++, new Date(table.getModifyDate().getTime()));
            try {
            	stmt.execute();
            } catch(Exception e) {
	        	Util.warn(e);
            	continue;
            }
        }

        Util.logAction(input.size(), "storeResourceSubmission", startTime);
        DatabaseUtils.closeStatementSilently(stmt);
    }

    /**
     * Store Notification to new online review schema
     *
     * @param input the Notification data
     *
     * @throws SQLException if error occurs while execute sql statement
     */
    void storeNotification(List input) throws SQLException {
    	long startTime = Util.start("storeNotification");
        String[] fieldnames = {
                "project_id", "external_ref_id", "notification_type_id", "create_user", "create_date", "modify_user", "modify_date"
            };

        // store Notification data to new online review schema
        PreparedStatement stmt = conn.prepareStatement(makeInsertSql(Notification.TABLE_NAME, fieldnames));

        for (Iterator iter = input.iterator(); iter.hasNext();) {
            Notification table = (Notification) iter.next();
            int i = 1;
            stmt.setInt(i++, table.getProjectId());
            stmt.setInt(i++, table.getExternalRefId());
            stmt.setInt(i++, table.getNotificationTypeId());
            stmt.setString(i++, table.getCreateUser());
            stmt.setDate(i++, new Date(table.getCreateDate().getTime()));
            stmt.setString(i++, table.getModifyUser());
            stmt.setDate(i++, new Date(table.getModifyDate().getTime()));
            try {
            	stmt.execute();
            } catch(Exception e) {
            	// It's ok since some user will take more than one role
	        	// Util.warn(e);
            	continue;
            }
        }

        Util.logAction(input.size(), "storeNotification", startTime);
        DatabaseUtils.closeStatementSilently(stmt);
    }

    /**
     * Store ResourceInfo to new online review schema
     *
     * @param input the ResourceInfo data
     *
     * @throws SQLException if error occurs while execute sql statement
     */
    private void storeResourceInfo(List input) throws SQLException {
    	long startTime = Util.start("storeResourceInfo");
        String[] fieldnames = {
                "resource_id", "resource_info_type_id", "value", "create_user", "create_date", "modify_user", "modify_date"
            };

        // store ResourceInfo data to new online review schema
        PreparedStatement stmt = conn.prepareStatement(makeInsertSql(ResourceInfo.TABLE_NAME, fieldnames));

        for (Iterator iter = input.iterator(); iter.hasNext();) {
            ResourceInfo table = (ResourceInfo) iter.next();
            int i = 1;
            stmt.setInt(i++, table.getResourceId());
            stmt.setInt(i++, table.getResourceInfoTypeId());
            stmt.setString(i++, table.getValue());
            stmt.setString(i++, table.getCreateUser());
            stmt.setDate(i++, new Date(table.getCreateDate().getTime()));
            stmt.setString(i++, table.getModifyUser());
            stmt.setDate(i++, new Date(table.getModifyDate().getTime()));
            try {
            	stmt.execute();
            } catch(Exception e) {
	        	Util.warn(e);
            	continue;
            }
        }

        Util.logAction(input.size(), "storeResourceInfo", startTime);
        DatabaseUtils.closeStatementSilently(stmt);
    }

    /**
     * Store Resource to new online review schema
     *
     * @param input the Resource data
     *
     * @throws SQLException if error occurs while execute sql statement
     */
    private void storeResource(List input) throws SQLException {
    	long startTime = Util.start("storeResource");
        String[] fieldnames = {
                "resource_id", "resource_role_id", "project_id", "phase_id", "create_user", "create_date", "modify_user", "modify_date"
            };

        // store Resource data to new online review schema
        PreparedStatement stmt = conn.prepareStatement(makeInsertSql(Resource.TABLE_NAME, fieldnames));

        for (Iterator iter = input.iterator(); iter.hasNext();) {
            Resource table = (Resource) iter.next();
            int i = 1;
            stmt.setInt(i++, table.getResourceId());
            stmt.setInt(i++, table.getResourceRoleId());
            stmt.setInt(i++, table.getProjectId());
            if (table.getPhaseId() == 0) {
            	stmt.setNull(i++, Types.INTEGER);
            } else {
            	stmt.setInt(i++, table.getPhaseId());
            }
            stmt.setString(i++, table.getCreateUser());
            stmt.setDate(i++, new Date(table.getCreateDate().getTime()));
            stmt.setString(i++, table.getModifyUser());
            stmt.setDate(i++, new Date(table.getModifyDate().getTime()));
            try {
            	stmt.execute();
            } catch(Exception e) {
	        	Util.warn(e);
            	continue;
            }
        }

        Util.logAction(input.size(), "storeResource", startTime);
        DatabaseUtils.closeStatementSilently(stmt);
    }

    /**
     * Store ProjectInfo data to new online review schema
     *
     * @param input the ProjectInfo data
     *
     * @throws SQLException if error occurs while execute sql statement
     */
    private void storeProjectInfo(List input) throws SQLException {
    	long startTime = Util.start("storeProjectInfo");
        String[] fieldnames = {
                "project_id", "project_info_type_id", "value", "create_user", "create_date", "modify_user",
                "modify_date"
            };

        // store ProjectInfo data to new online review schema
        PreparedStatement stmt = conn.prepareStatement(makeInsertSql(ProjectInfo.TABLE_NAME, fieldnames));

        for (Iterator iter = input.iterator(); iter.hasNext();) {
            ProjectInfo table = (ProjectInfo) iter.next();
            int i = 1;
            stmt.setInt(i++, table.getProjectId());
            stmt.setInt(i++, table.getProjectInfoTypeId());
            stmt.setString(i++, table.getValue());
            stmt.setString(i++, table.getCreateUser());
            stmt.setDate(i++, new Date(table.getCreateDate().getTime()));
            stmt.setString(i++, table.getModifyUser());
            stmt.setDate(i++, new Date(table.getModifyDate().getTime()));
            try {
            	stmt.execute();
            } catch(Exception e) {
	        	Util.warn(e);
	        	Util.warn("The failed projectInfo, typeId: " + table.getProjectInfoTypeId());
            	continue;
            }
        }
        Util.logAction(input.size(), "storeProjectInfo", startTime);

        DatabaseUtils.closeStatementSilently(stmt);
    }

    /**
     * Store ProjectAudit data to new online review schema
     *
     * @param input the ProjectAudit data
     *
     * @throws SQLException if error occurs while execute sql statement
     */
    private void storeProjectAudit(List input) throws SQLException {
    	long startTime = Util.start("storeProjectAudit");
        String[] fieldnames = {
                "project_audit_id", "project_id", "update_reason", "create_user", "create_date", "modify_user",
                "modify_date"
            };

        // store ProjectAudit data to new online review schema
        PreparedStatement stmt = conn.prepareStatement(makeInsertSql(ProjectAudit.TABLE_NAME, fieldnames));

        for (Iterator iter = input.iterator(); iter.hasNext();) {
            ProjectAudit table = (ProjectAudit) iter.next();
            int i = 1;
            stmt.setInt(i++, table.getProjectAuditId());
            stmt.setInt(i++, table.getProjectid());
            stmt.setString(i++, table.getUpdateReason());
            stmt.setString(i++, table.getCreateUser());
            stmt.setDate(i++, new Date(table.getCreateDate().getTime()));
            stmt.setString(i++, table.getModifyUser());
            stmt.setDate(i++, new Date(table.getModifyDate().getTime()));
            try {
            	stmt.execute();
            } catch(Exception e) {
	        	Util.warn(e);
            	continue;
            }
        }

        Util.logAction(input.size(), "storeProjectAudit", startTime);
        DatabaseUtils.closeStatementSilently(stmt);
    }
}
