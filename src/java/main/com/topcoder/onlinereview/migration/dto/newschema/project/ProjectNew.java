/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration.dto.newschema.project;

import com.topcoder.onlinereview.migration.dto.BaseDTO;
import com.topcoder.onlinereview.migration.dto.newschema.deliverable.Submission;
import com.topcoder.onlinereview.migration.dto.newschema.deliverable.Upload;
import com.topcoder.onlinereview.migration.dto.newschema.phase.Phase;
import com.topcoder.onlinereview.migration.dto.newschema.phase.PhaseDependency;
import com.topcoder.onlinereview.migration.dto.newschema.resource.Notification;
import com.topcoder.onlinereview.migration.dto.newschema.resource.Resource;
import com.topcoder.onlinereview.migration.dto.newschema.resource.ResourceInfo;
import com.topcoder.onlinereview.migration.dto.newschema.resource.ResourceSubmission;
import com.topcoder.onlinereview.migration.dto.newschema.review.Review;
import com.topcoder.onlinereview.migration.dto.newschema.screening.ScreeningTask;

import java.util.ArrayList;
import java.util.List;


/**
 * The Project DTO.
 *
 * @author brain_cn
 * @version 1.0
 */
public class ProjectNew extends BaseDTO {
    /** Represents the project table name. */
    public static final String TABLE_NAME = "project";
    public static final int PROJECT_STATUS_ACTIVE_ID = 1;
    public static final int PROJECT_STATUS_DELETED_ID = 3;
    public static final int PROJECT_STATUS_COMPLETED_ID = 7;
    public static final int PROJECT_CATEGORY_DESIGN_ID = 1;
    public static final int PROJECT_CATEGORY_DEVELOPMENT_ID = 2;
    private int projectId;
    private int projectStatusId;
    private int projectCategoryId;
    private List projectAudits = new ArrayList();
    private List projectInfos = new ArrayList();
    private List phases = new ArrayList();
    private List phaseDependencys = new ArrayList();
    private List resources = new ArrayList();
    private List uploads = new ArrayList();
    private List submissions = new ArrayList();
    private List resourceInfos = new ArrayList();
    private List screeningTasks = new ArrayList();
    private List reviews = new ArrayList();
    private List notifications = new ArrayList();
    private List resourceSubmissions = new ArrayList();
    
    /**
     * Returns the projectCategoryId.
     *
     * @return Returns the projectCategoryId.
     */
    public int getProjectCategoryId() {
        return projectCategoryId;
    }

    /**
     * Set the projectCategoryId.
     *
     * @param projectCategoryId The projectCategoryId to set.
     */
    public void setProjectCategoryId(int projectCategoryId) {
        this.projectCategoryId = projectCategoryId;
    }

    /**
     * Returns the projectId.
     *
     * @return Returns the projectId.
     */
    public int getProjectId() {
        return projectId;
    }

    /**
     * Set the projectId.
     *
     * @param projectId The projectId to set.
     */
    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    /**
     * Returns the projectStatusId.
     *
     * @return Returns the projectStatusId.
     */
    public int getProjectStatusId() {
        return projectStatusId;
    }

    /**
     * Set the projectStatusId.
     *
     * @param projectStatusId The projectStatusId to set.
     */
    public void setProjectStatusId(int projectStatusId) {
        this.projectStatusId = projectStatusId;
    }

    /**
     * Returns the projectAudit.
     *
     * @return Returns the projectAudit.
     */
    public List getProjectAudits() {
        return projectAudits;
    }

    /**
     * Set the projectAudit.
     *
     * @param projectAudit The projectAudit to set.
     */
    public void addProjectAudit(ProjectAudit projectAudit) {
        this.projectAudits.add(projectAudit);
    }

    /**
     * Returns the projectInfos.
     *
     * @return Returns the projectInfos.
     */
    public List getProjectInfos() {
        return projectInfos;
    }

    /**
     * Add ProjectInfo to this project.
     *
     * @param projectInfo The projectInfo to add.
     */
    public void addProjectInfo(ProjectInfo projectInfo) {
        if (projectInfo != null) {
            this.projectInfos.add(projectInfo);
        }
    }

    /**
     * Returns the phases.
     *
     * @return Returns the phases.
     */
    public List getPhases() {
        return phases;
    }

    /**
     * Add the phase.
     *
     * @param phase The phase to add.
     */
    public void addPhase(Phase phase) {
        if (phase != null) {
            this.phases.add(phase);
        }
    }

    /**
     * Returns the phaseDependencys.
     *
     * @return Returns the phaseDependencys.
     */
    public List getPhaseDependencys() {
        return phaseDependencys;
    }

    /**
     * Add the phaseDependency.
     *
     * @param phaseDependency The phaseDependencys to Add.
     */
    public void addPhaseDependencys(PhaseDependency phaseDependency) {
        if (phaseDependency != null) {
            this.phaseDependencys.add(phaseDependency);
        }
    }

    /**
     * Returns the resources.
     *
     * @return Returns the resources.
     */
    public List getResources() {
        return resources;
    }

    /**
     * Add the resource.
     *
     * @param resource The resource to add.
     */
    public void addResource(Resource resource) {
        if (resource != null) {
            this.resources.add(resource);
        }
    }

    /**
     * Returns the uploads.
     *
     * @return Returns the uploads.
     */
    public List getUploads() {
        return uploads;
    }

    /**
     * Add the upload.
     *
     * @param upload The upload to add.
     */
    public void addUpload(Upload upload) {
        if (upload != null) {
            this.uploads.add(upload);
        }
    }

    /**
     * Returns the submissions.
     *
     * @return Returns the submissions.
     */
    public List getSubmissions() {
        return submissions;
    }

    /**
     * Add the submission.
     *
     * @param submission The submission to add.
     */
    public void addSubmission(Submission submission) {
        if (submission != null) {
            this.submissions.add(submission);
        }
    }

    /**
     * Returns the resourceInfos.
     *
     * @return Returns the resourceInfos.
     */
    public List getResourceInfos() {
        return resourceInfos;
    }

    /**
     * Add the resourceInfo.
     *
     * @param resourceInfo The resourceInfo to add.
     */
    public void addResourceInfo(ResourceInfo resourceInfo) {
        if (resourceInfo != null) {
            this.resourceInfos.add(resourceInfo);
        }
    }

    /**
     * Returns the screeningTasks.
     *
     * @return Returns the screeningTasks.
     */
    public List getScreeningTasks() {
        return screeningTasks;
    }

    /**
     * Add the screeningTask.
     *
     * @param screeningTask The screeningTask to add.
     */
    public void addScreeningTask(ScreeningTask screeningTask) {
        if (screeningTask != null) {
            this.screeningTasks.add(screeningTask);
        }
    }

    /**
     * Returns the reviews.
     *
     * @return Returns the reviews.
     */
    public List getReviews() {
        return reviews;
    }

    /**
     * Add the review.
     *
     * @param review The review to add.
     */
    public void addReview(Review review) {
        if (review != null) {
            this.reviews.add(review);
        }
    }

    /**
     * Add the review.
     *
     * @param review The review to add.
     */
    public void addReviews(List list) {
        if (list != null) {
            this.reviews.addAll(list);
        }
    }

    /**
     * Returns the notifications.
     *
     * @return Returns the notifications.
     */
    public List getNotifications() {
        return notifications;
    }

    /**
     * Add the notification.
     *
     * @param notification The notification to add.
     */
    public void addNotification(Notification notification) {
        if (notification != null) {
            this.notifications.add(notification);
        }
    }

    /**
     * Returns the resourceSubmissions.
     *
     * @return Returns the resourceSubmissions.
     */
    public List getResourceSubmissions() {
        return resourceSubmissions;
    }

    /**
     * Add the resourceSubmission.
     *
     * @param resourceSubmission The resourceSubmission to add.
     */
    public void addResourceSubmission(ResourceSubmission resourceSubmission) {
        if (resourceSubmission != null) {
            this.resourceSubmissions.add(resourceSubmission);
        }
    }
}
