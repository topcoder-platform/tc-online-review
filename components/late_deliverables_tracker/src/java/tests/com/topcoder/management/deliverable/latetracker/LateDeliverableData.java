/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.latetracker;

import java.util.Date;

/**
 * The POJO class to hold the data from late deliverable table.
 *
 * @author myxgyy
 * @version 1.0
 */
public class LateDeliverableData {
    /**
     * The project phase id.
     */
    private long projectPhaseId;

    /**
     * The resource id.
     */
    private long resourceId;

    /**
     * The deliverable id.
     */
    private long deliverableId;

    /**
     * The deadline.
     */
    private Date deadline;

    /**
     * The create date.
     */
    private Date createDate;

    /**
     * Last notified time.
     */
    private Date lastNotified;

    /**
     * Forgive flag.
     */
    private boolean forgive;

    /**
     * Getter for the {@link #createDate} field.
     *
     * @return createDate field.
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * Setter for the {@link #createDate} field.
     *
     * @param createDate
     *            the createDate to set.
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * Getter for the {@link #deadline} field.
     *
     * @return deadline field.
     */
    public Date getDeadline() {
        return deadline;
    }

    /**
     * Setter for the {@link #deadline} field.
     *
     * @param deadline
     *            the deadline to set.
     */
    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    /**
     * Getter for the {@link #deliverableId} field.
     *
     * @return deliverableId field.
     */
    public long getDeliverableId() {
        return deliverableId;
    }

    /**
     * Setter for the {@link #deliverableId} field.
     *
     * @param deliverableId
     *            the deliverableId to set.
     */
    public void setDeliverableId(long deliverableId) {
        this.deliverableId = deliverableId;
    }

    /**
     * Getter for the {@link #forgive} field.
     *
     * @return forgive field.
     */
    public boolean isForgive() {
        return forgive;
    }

    /**
     * Setter for the {@link #forgive} field.
     *
     * @param forgive
     *            the forgive to set.
     */
    public void setForgive(boolean forgive) {
        this.forgive = forgive;
    }

    /**
     * Getter for the {@link #lastNotified} field.
     *
     * @return lastNotified field.
     */
    public Date getLastNotified() {
        return lastNotified;
    }

    /**
     * Setter for the {@link #lastNotified} field.
     *
     * @param lastNotified
     *            the lastNotified to set.
     */
    public void setLastNotified(Date lastNotified) {
        this.lastNotified = lastNotified;
    }

    /**
     * Getter for the {@link #projectPhaseId} field.
     *
     * @return projectPhaseId field.
     */
    public long getProjectPhaseId() {
        return projectPhaseId;
    }

    /**
     * Setter for the {@link #projectPhaseId} field.
     *
     * @param projectPhaseId
     *            the projectPhaseId to set.
     */
    public void setProjectPhaseId(long projectPhaseId) {
        this.projectPhaseId = projectPhaseId;
    }

    /**
     * Getter for the {@link #resourceId} field.
     *
     * @return resourceId field.
     */
    public long getResourceId() {
        return resourceId;
    }

    /**
     * Setter for the {@link #resourceId} field.
     *
     * @param resourceId
     *            the resourceId to set.
     */
    public void setResourceId(long resourceId) {
        this.resourceId = resourceId;
    }
}