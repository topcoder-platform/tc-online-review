/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration.dto.newschema.resource;

import com.topcoder.onlinereview.migration.dto.BaseDTO;


/**
 * The Resource DTO.
 *
 * @author brain_cn
 * @version 1.0
 */
public class Resource extends BaseDTO {
	public static final int SUBMITTER_RESOURCE_ROLE = 1;
	public static final int PRIMARY_SCREENER_RESOURCE_ROLE = 2;
	public static final int SCREENER_RESOURCE_ROLE = 3;
	public static final int REVIEWER_RESOURCE_ROLE = 4;
	public static final int ACCURACY_REVIEWER_RESOURCE_ROLE = 5;
	public static final int FAILURE_REVIEWER_RESOURCE_ROLE = 6;
	public static final int STRESS_REVIEWER_RESOURCE_ROLE = 7;
	public static final int AGGREGATOR_RESOURCE_ROLE = 8;
	public static final int FINA_REVIEWER_RESOURCE_ROLE = 9;
	public static final int MANAGER_RESOURCE_ROLE = 13;
    /** Represents the resource table name. */
    public static final String TABLE_NAME = "resource";
    private int resourceId;
    private int resourceRoleId;
    private int projectId;
    private int phaseId;

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
     * Returns the resourceId.
     *
     * @return Returns the resourceId.
     */
    public int getResourceId() {
        return resourceId;
    }

    /**
     * Set the resourceId.
     *
     * @param resourceId The resourceId to set.
     */
    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    /**
     * Returns the resourceRoleId.
     *
     * @return Returns the resourceRoleId.
     */
    public int getResourceRoleId() {
        return resourceRoleId;
    }

    /**
     * Set the resourceRoleId.
     *
     * @param resourceRoleId The resourceRoleId to set.
     */
    public void setResourceRoleId(int resourceRoleId) {
        this.resourceRoleId = resourceRoleId;
    }
}
