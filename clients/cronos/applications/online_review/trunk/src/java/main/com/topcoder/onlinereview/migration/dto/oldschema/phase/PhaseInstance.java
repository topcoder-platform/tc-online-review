/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration.dto.oldschema.phase;

import java.util.Date;


/**
 * The PhaseInstance DTO.
 *
 * @author brain_cn
 * @version 1.0
 */
public class PhaseInstance {
    /** DOCUMENT ME! */
    public static final int PHASE_STATUS_SCHEDULED = 1;

    /** DOCUMENT ME! */
    public static final int PHASE_STATUS_OPEN = 2;

    /** DOCUMENT ME! */
    public static final int PHASE_STATUS_CLOSED = 3;

    /** Represents the phase_instance table name. */
    public static final String TABLE_NAME = "phase_instance";

    /** Represents phase_instance_id field name. */
    public static final String PHASE_INSTANCE_ID_NAME = "phase_instance_id";

    /** Represents project_id field name. */
    public static final String PROJECT_ID_NAME = "project_id";

    /** Represents phase_id field name. */
    public static final String PHASE_ID_NAME = "phase_id";

    /** Represents start_date field name. */
    public static final String START_DATE_NAME = "start_date";

    /** Represents end_date field name. */
    public static final String END_DATE_NAME = "end_date";
    private int phaseInstanceId;
    private int projectId;
    private int phaseId;
    private Date startDate;
    private Date endDate;
    private int phaseStatusId;

    /** Only exist for screening and review phase. retrieve from project_template table. */
    private int templateId;

    /**
     * Returns the endDate.
     *
     * @return Returns the endDate.
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * Set the endDate.
     *
     * @param endDate The endDate to set.
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
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
     * Returns the phaseInstanceId.
     *
     * @return Returns the phaseInstanceId.
     */
    public int getPhaseInstanceId() {
        return phaseInstanceId;
    }

    /**
     * Set the phaseInstanceId.
     *
     * @param phaseInstanceId The phaseInstanceId to set.
     */
    public void setPhaseInstanceId(int phaseInstanceId) {
        this.phaseInstanceId = phaseInstanceId;
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
     * Returns the startDate.
     *
     * @return Returns the startDate.
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * Set the startDate.
     *
     * @param startDate The startDate to set.
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
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
     * Returns the templateId.
     *
     * @return Returns the templateId.
     */
    public int getTemplateId() {
        return templateId;
    }

    /**
     * Set the templateId.
     *
     * @param templateId The templateId to set.
     */
    public void setTemplateId(int templateId) {
        this.templateId = templateId;
    }
}
