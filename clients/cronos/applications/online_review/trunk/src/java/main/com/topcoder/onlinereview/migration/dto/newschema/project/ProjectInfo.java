/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration.dto.newschema.project;

import com.topcoder.onlinereview.migration.dto.BaseDTO;


/**
 * The ProjectInfo DTO.
 *
 * @author brain_cn
 * @version 1.0
 */
public class ProjectInfo extends BaseDTO {
    /** Represents the project_info table name. */
    public static final String TABLE_NAME = "project_info";
    public static final int EXTERNAL_REFERENCE_ID = 1;
    public static final int COMPONENT_ID = 2;
    public static final int VERSION_ID = 3;
    public static final int DEVELOPER_FORUM_ID = 4;
    public static final int ROOT_CATALOG_ID = 5;
    public static final int PROJECT_NAME= 6;
    public static final int PROJECT_VERSION = 7;
    public static final int SVN_MODULE = 8;
    public static final int AUTOPILOT_OPTION = 9;
    public static final int STATUS_NOTIFICATION = 10;
    public static final int TIMELINE_NOTIFICATION = 11;
    public static final int PUBLIC = 12;
    public static final int RATIED = 13;
    public static final int ELIGIBILITY = 14;
    public static final int PAYMENTS_REQUIRED = 15;
    public static final int PAYMENTS = 16;
    public static final int NOTES = 17;
    public static final int DEACTIVATION_TIMESTAMP = 18;
    public static final int DEACTIVATION_PHASE = 19;
    public static final int DEACTIVATION_REASON = 20;
    public static final int COMPLETION_TIMESTAP = 21;
    public static final int RATED_TIMESTAP = 22;
    public static final int WINNER_EXTERNAL_REFERENCE_ID = 23;
    public static final int RUNNER_UP_EXTERNAL_REFERENCE_ID = 24;
    private int projectId;
    private int projectInfoTypeId;
    private String value;

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
     * Returns the projectInfoTypeId.
     *
     * @return Returns the projectInfoTypeId.
     */
    public int getProjectInfoTypeId() {
        return projectInfoTypeId;
    }

    /**
     * Set the projectInfoTypeId.
     *
     * @param projectInfoTypeId The projectInfoTypeId to set.
     */
    public void setProjectInfoTypeId(int projectInfoTypeId) {
        this.projectInfoTypeId = projectInfoTypeId;
    }

    /**
     * Returns the value.
     *
     * @return Returns the value.
     */
    public String getValue() {
        return value;
    }

    /**
     * Set the value.
     *
     * @param value The value to set.
     */
    public void setValue(String value) {
        this.value = value;
    }
}
