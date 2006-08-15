/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration.dto.newschema.project;

import com.topcoder.onlinereview.migration.dto.BaseDTO;


/**
 * The test of ProjectAudit.
 *
 * @author brain_cn
 * @version 1.0
 */
public class ProjectAudit extends BaseDTO {
    /** Represents the project_audit table name. */
    public static final String TABLE_NAME = "project_audit";
    private int projectAuditId;
    private int projectid;
    private String updateReason;

    /**
     * Returns the projectAuditId.
     *
     * @return Returns the projectAuditId.
     */
    public int getProjectAuditId() {
        return projectAuditId;
    }

    /**
     * Set the projectAuditId.
     *
     * @param projectAuditId The projectAuditId to set.
     */
    public void setProjectAuditId(int projectAuditId) {
        this.projectAuditId = projectAuditId;
    }

    /**
     * Returns the projectid.
     *
     * @return Returns the projectid.
     */
    public int getProjectid() {
        return projectid;
    }

    /**
     * Set the projectid.
     *
     * @param projectid The projectid to set.
     */
    public void setProjectid(int projectid) {
        this.projectid = projectid;
    }

    /**
     * Returns the updateReason.
     *
     * @return Returns the updateReason.
     */
    public String getUpdateReason() {
        return updateReason;
    }

    /**
     * Set the updateReason.
     *
     * @param updateReason The updateReason to set.
     */
    public void setUpdateReason(String updateReason) {
        this.updateReason = updateReason;
    }
}
