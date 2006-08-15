/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration.dto.newschema.resource;

import com.topcoder.onlinereview.migration.dto.BaseDTO;


/**
 * The test of Notification.
 *
 * @author brain_cn
 * @version 1.0
 */
public class Notification extends BaseDTO {
    /** Represents the notification table name. */
    public static final String TABLE_NAME = "notification";
    public static final int TIMELINE_NOTIFICATION_ID = 1;
    private int projectId;
    private int externalRefId;
    private int notificationTypeId;

    /**
     * Returns the externalRefId.
     *
     * @return Returns the externalRefId.
     */
    public int getExternalRefId() {
        return externalRefId;
    }

    /**
     * Set the externalRefId.
     *
     * @param externalRefId The externalRefId to set.
     */
    public void setExternalRefId(int externalRefId) {
        this.externalRefId = externalRefId;
    }

    /**
     * Returns the notificationTypeId.
     *
     * @return Returns the notificationTypeId.
     */
    public int getNotificationTypeId() {
        return notificationTypeId;
    }

    /**
     * Set the notificationTypeId.
     *
     * @param notificationTypeId The notificationTypeId to set.
     */
    public void setNotificationTypeId(int notificationTypeId) {
        this.notificationTypeId = notificationTypeId;
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
}
