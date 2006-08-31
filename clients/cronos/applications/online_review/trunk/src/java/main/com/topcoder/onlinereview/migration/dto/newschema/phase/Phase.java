/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration.dto.newschema.phase;

import com.topcoder.onlinereview.migration.dto.BaseDTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * The Phase DTO.
 *
 * @author brain_cn
 * @version 1.0
 */
public class Phase extends BaseDTO {
    /** Represents the phase table name. */
    public static final String TABLE_NAME = "project_phase";
    public static final int REGISTRATION_TYPE = 1;
    public static final int SUBMISSION_TYPE = 2;
    public static final int SCREENING_TYPE = 3;
    public static final int REVIEW_TYPE = 4;
    private int phaseId;
    private int projectId;
    private int phaseTypeId;
    private int phaseStatusId;
    private Date fixedStartTime;
    private Date scheduledStartTime;
    private Date scheduledEndTime;
    private Date actualStartTime;
    private Date actualEndTime;
    private int duration;
    private List criterias = new ArrayList();

    /**
     * Returns the actualEndTime.
     *
     * @return Returns the actualEndTime.
     */
    public Date getActualEndTime() {
        return actualEndTime;
    }

    /**
     * Set the actualEndTime.
     *
     * @param actualEndTime The actualEndTime to set.
     */
    public void setActualEndTime(Date actualEndTime) {
        this.actualEndTime = actualEndTime;
    }

    /**
     * Returns the actualStartTime.
     *
     * @return Returns the actualStartTime.
     */
    public Date getActualStartTime() {
        return actualStartTime;
    }

    /**
     * Set the actualStartTime.
     *
     * @param actualStartTime The actualStartTime to set.
     */
    public void setActualStartTime(Date actualStartTime) {
        this.actualStartTime = actualStartTime;
    }

    /**
     * Returns the duration.
     *
     * @return Returns the duration.
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Set the duration.
     *
     * @param duration The duration to set.
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * Returns the fixedStartTime.
     *
     * @return Returns the fixedStartTime.
     */
    public Date getFixedStartTime() {
        return fixedStartTime;
    }

    /**
     * Set the fixedStartTime.
     *
     * @param fixedStartTime The fixedStartTime to set.
     */
    public void setFixedStartTime(Date fixedStartTime) {
        this.fixedStartTime = fixedStartTime;
    }

    /**
     * Returns the phaseId.
     *
     * @return Returns the phaseId.
     */
    public int getPhaseId() {
        return phaseId;
    }

    /**
     * Set the phaseId.
     *
     * @param phaseId The phaseId to set.
     */
    public void setPhaseId(int phaseId) {
        this.phaseId = phaseId;
    }

    /**
     * Returns the phaseStatusId.
     *
     * @return Returns the phaseStatusId.
     */
    public int getPhaseStatusId() {
        return phaseStatusId;
    }

    /**
     * Set the phaseStatusId.
     *
     * @param phaseStatusId The phaseStatusId to set.
     */
    public void setPhaseStatusId(int phaseStatusId) {
        this.phaseStatusId = phaseStatusId;
    }

    /**
     * Returns the phaseTypeId.
     *
     * @return Returns the phaseTypeId.
     */
    public int getPhaseTypeId() {
        return phaseTypeId;
    }

    /**
     * Set the phaseTypeId.
     *
     * @param phaseTypeId The phaseTypeId to set.
     */
    public void setPhaseTypeId(int phaseTypeId) {
        this.phaseTypeId = phaseTypeId;
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
     * Returns the scheduledEndTime.
     *
     * @return Returns the scheduledEndTime.
     */
    public Date getScheduledEndTime() {
        return scheduledEndTime;
    }

    /**
     * Set the scheduledEndTime.
     *
     * @param scheduledEndTime The scheduledEndTime to set.
     */
    public void setScheduledEndTime(Date scheduledEndTime) {
        this.scheduledEndTime = scheduledEndTime;
    }

    /**
     * Returns the scheduledStartTime.
     *
     * @return Returns the scheduledStartTime.
     */
    public Date getScheduledStartTime() {
        return scheduledStartTime;
    }

    /**
     * Set the scheduledStartTime.
     *
     * @param scheduledStartTime The scheduledStartTime to set.
     */
    public void setScheduledStartTime(Date scheduledStartTime) {
        this.scheduledStartTime = scheduledStartTime;
    }

    /**
     * Returns the criterias.
     *
     * @return Returns the criterias.
     */
    public List getCriterias() {
        return criterias;
    }

    /**
     * Add the criteria.
     *
     * @param criteria The criteria to set.
     */
    public void addCriteria(PhaseCriteria criteria) {
        if (criteria != null) {
            this.criterias.add(criteria);
        }
    }
}
