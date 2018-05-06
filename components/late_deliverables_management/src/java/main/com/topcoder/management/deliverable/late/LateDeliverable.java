/*
 * Copyright (C) 2010-2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.late;

import java.util.Date;

/**
 * <p>
 * This class is a container for information about a single late deliverable. It is a simple JavaBean (POJO) that
 * provides getters and setters for all private attributes and performs no argument validation in the setters.
 * </p>
 *
 * <p>
 * <em>Changes in version 1.0.6:</em>
 * <ol>
 * <li>Added type property.</li>
 * <li>Updated toString() method.</li>
 * </ol>
 * </p>
 *
 * <p>
 * <strong>Thread Safety: </strong> This class is mutable and not thread safe.
 * </p>
 *
 * @author saarixx, sparemax
 * @version 1.0.6
 */
public class LateDeliverable {
    /**
     * <p>
     * The ID of the late deliverable.
     * </p>
     *
     * <p>
     * Can be any value. Has getter and setter.
     * </p>
     */
    private long id;

    /**
     * <p>
     * The type of the late deliverable. Can be any value. Has getter and setter.
     * </p>
     *
     * @since 1.0.6
     */
    private LateDeliverableType type;

    /**
     * <p>
     * The ID of the project.
     * </p>
     *
     * <p>
     * Can be any value. Has getter and setter.
     * </p>
     */
    private long projectId;

    /**
     * <p>
     * The ID of the project phase for this late deliverable.
     * </p>
     *
     * <p>
     * Can be any value. Has getter and setter.
     * </p>
     */
    private long projectPhaseId;

    /**
     * <p>
     * The ID of the resource (i.e. user who needs to provide a deliverable).
     * </p>
     *
     * <p>
     * Can be any value. Has getter and setter.
     * </p>
     */
    private long resourceId;

    /**
     * <p>
     * The ID of the deliverable.
     * </p>
     *
     * <p>
     * Can be any value. Has getter and setter.
     * </p>
     */
    private long deliverableId;

    /**
     * <p>
     * The deadline for this late deliverable.
     * </p>
     *
     * <p>
     * Can be any value. Has getter and setter.
     * </p>
     */
    private Date deadline;

    /**
     * <p>
     * The compensated deadline for this late deliverable.
     * </p>
     *
     * <p>
     * Can be any value. Has getter and setter.
     * </p>
     */
    private Date compensatedDeadline;

    /**
     * <p>
     * The creation date/time for this late deliverable.
     * </p>
     *
     * <p>
     * Can be any value. Has getter and setter.
     * </p>
     */
    private Date createDate;

    /**
     * <p>
     * The value indicating whether this late deliverable was forgiven.
     * </p>
     *
     * <p>
     * Has getter and setter.
     * </p>
     */
    private boolean forgiven;

    /**
     * <p>
     * The last date/time when notification was sent for this late deliverable.
     * </p>
     *
     * <p>
     * Can be any value. Has getter and setter.
     * </p>
     */
    private Date lastNotified;

    /**
     * <p>
     * The delay in seconds.
     * </p>
     *
     * <p>
     * Can be any value. Has getter and setter.
     * </p>
     */
    private Long delay;

    /**
     * <p>
     * The explanation of the delay.
     * </p>
     *
     * <p>
     * Can be any value. Has getter and setter.
     * </p>
     */
    private String explanation;

    /**
     * <p>
     * The date/time when explanation was submitted.
     * </p>
     *
     * <p> Can be any value. Has getter and setter.
     * </p>
     */
    private Date explanationDate;

    /**
     * <p>
     * The response text.
     * </p>
     *
     * <p>
     * Can be any value. Has getter and setter.
     * </p>
     */
    private String response;

    /**
     * <p>
     * The ID of the user who submitted the response.
     * </p>
     *
     * <p>Can be any value. Has getter and setter.
     * </p>
     */
    private String responseUser;

    /**
     * <p>
     * The date/time when response was submitted.
     * </p>
     *
     * <p> Can be any value. Has getter and setter.
     * </p>
     */
    private Date responseDate;

    /**
     * <p>
     * Creates an instance of LateDeliverable.
     * </p>
     */
    public LateDeliverable() {
        // Empty
    }

    /**
     * <p>
     * Gets the ID of the late deliverable.
     * </p>
     *
     * @return the ID of the late deliverable.
     */
    public long getId() {
        return id;
    }

    /**
     * <p>
     * Sets the ID of the late deliverable.
     * </p>
     *
     * @param id
     *            the ID of the late deliverable.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * <p>
     * Retrieves the type of the late deliverable.
     * </p>
     *
     * @return the type of the late deliverable.
     *
     * @since 1.0.6
     */
    public LateDeliverableType getType() {
        return type;
    }

    /**
     * <p>
     * Sets the type of the late deliverable.
     * </p>
     *
     * @param type
     *            the type of the late deliverable.
     *
     * @since 1.0.6
     */
    public void setType(LateDeliverableType type) {
        this.type = type;
    }

    /**
     * <p>
     * Gets the ID of the project.
     * </p>
     *
     * @return the ID of the project.
     */
    public long getProjectId() {
        return projectId;
    }

    /**
     * <p>
     * Sets the ID of the project.
     * </p>
     *
     * @param projectId
     *            the ID of the project.
     */
    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    /**
     * <p>
     * Gets the ID of the project phase for this late deliverable.
     * </p>
     *
     * @return the ID of the project phase for this late deliverable.
     */
    public long getProjectPhaseId() {
        return projectPhaseId;
    }

    /**
     * <p>
     * Sets the ID of the project phase for this late deliverable.
     * </p>
     *
     * @param projectPhaseId
     *            the ID of the project phase for this late deliverable.
     */
    public void setProjectPhaseId(long projectPhaseId) {
        this.projectPhaseId = projectPhaseId;
    }

    /**
     * <p>
     * Gets the ID of the resource.
     * </p>
     *
     * @return the ID of the resource.
     */
    public long getResourceId() {
        return resourceId;
    }

    /**
     * <p>
     * Sets the ID of the resource.
     * </p>
     *
     * @param resourceId
     *            the ID of the resource.
     */
    public void setResourceId(long resourceId) {
        this.resourceId = resourceId;
    }

    /**
     * <p>
     * Gets the ID of the deliverable.
     * </p>
     *
     * @return the ID of the deliverable.
     */
    public long getDeliverableId() {
        return deliverableId;
    }

    /**
     * <p>
     * Sets the ID of the deliverable.
     * </p>
     *
     * @param deliverableId
     *            the ID of the deliverable.
     */
    public void setDeliverableId(long deliverableId) {
        this.deliverableId = deliverableId;
    }

    /**
     * <p>
     * Gets the deadline for this late deliverable.
     * </p>
     *
     * @return the deadline for this late deliverable.
     */
    public Date getDeadline() {
        return deadline;
    }

    /**
     * <p>
     * Sets the deadline for this late deliverable.
     * </p>
     *
     * @param deadline
     *            the deadline for this late deliverable.
     */
    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    /**
     * <p>
     * Gets the compensated deadline for this late deliverable.
     * </p>
     *
     * @return the compensated deadline for this late deliverable.
     */
    public Date getCompensatedDeadline() {
        return compensatedDeadline;
    }

    /**
     * <p>
     * Sets the compensated deadline for this late deliverable.
     * </p>
     *
     * @param compensatedDeadline
     *            the compensated deadline for this late deliverable.
     */
    public void setCompensatedDeadline(Date compensatedDeadline) {
        this.compensatedDeadline = compensatedDeadline;
    }

    /**
     * <p>
     * Gets the creation date/time for this late deliverable.
     * </p>
     *
     * @return the creation date/time for this late deliverable.
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * <p>
     * Sets the creation date/time for this late deliverable.
     * </p>
     *
     * @param createDate
     *            the creation date/time for this late deliverable.
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * <p>
     * Gets the value indicating whether this late deliverable was forgiven.
     * </p>
     *
     * @return the value indicating whether this late deliverable was forgiven.
     */
    public boolean isForgiven() {
        return forgiven;
    }

    /**
     * <p>
     * Sets the value indicating whether this late deliverable was forgiven.
     * </p>
     *
     * @param forgiven
     *            the value indicating whether this late deliverable was forgiven.
     */
    public void setForgiven(boolean forgiven) {
        this.forgiven = forgiven;
    }

    /**
     * <p>
     * Gets the last date/time when notification was sent for this late deliverable.
     * </p>
     *
     * @return the last date/time when notification was sent for this late deliverable.
     */
    public Date getLastNotified() {
        return lastNotified;
    }

    /**
     * <p>
     * Sets the last date/time when notification was sent for this late deliverable.
     * </p>
     *
     * @param lastNotified
     *            the last date/time when notification was sent for this late deliverable.
     */
    public void setLastNotified(Date lastNotified) {
        this.lastNotified = lastNotified;
    }

    /**
     * <p>
     * Gets the delay in seconds.
     * </p>
     *
     * @return the delay in seconds.
     */
    public Long getDelay() {
        return delay;
    }

    /**
     * <p>
     * Sets the delay in seconds.
     * </p>
     *
     * @param delay
     *            the delay in seconds.
     */
    public void setDelay(Long delay) {
        this.delay = delay;
    }

    /**
     * <p>
     * Gets the explanation of the delay.
     * </p>
     *
     * @return the explanation of the delay.
     */
    public String getExplanation() {
        return explanation;
    }

    /**
     * <p>
     * Sets the explanation of the delay.
     * </p>
     *
     * @param explanation
     *            the explanation of the delay.
     */
    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    /**
     * <p>
     * Gets the date/time when explanation was submitted.
     * </p>
     *
     * @return the date/time when explanation was submitted.
     */
    public Date getExplanationDate() {
        return explanationDate;
    }

    /**
     * <p>
     * Sets the date/time when explanation was submitted.
     * </p>
     *
     * @param explanationDate
     *            the date/time when explanation was submitted.
     */
    public void setExplanationDate(Date explanationDate) {
        this.explanationDate = explanationDate;
    }

    /**
     * <p>
     * Gets the response text.
     * </p>
     *
     * @return the response text.
     */
    public String getResponse() {
        return response;
    }

    /**
     * <p>
     * Sets the response text.
     * </p>
     *
     * @param response
     *            the response text.
     */
    public void setResponse(String response) {
        this.response = response;
    }

    /**
     * <p>
     * Gets the user who submitted a response.
     * </p>
     *
     * @return the user who submitted a response.
     */
    public String getResponseUser() {
        return responseUser;
    }

    /**
     * <p>
     * Sets the user who submitted a response.
     * </p>
     *
     * @param responseUser
     *            the user who submitted a response.
     */
    public void setResponseUser(String responseUser) {
        this.responseUser = responseUser;
    }

    /**
     * <p>
     * Gets the date/time when response was submitted.
     * </p>
     *
     * @return the date/time when response was submitted.
     */
    public Date getResponseDate() {
        return responseDate;
    }

    /**
     * <p>
     * Sets the date/time when response was submitted.
     * </p>
     *
     * @param responseDate
     *            the date/time when response was submitted.
     */
    public void setResponseDate(Date responseDate) {
        this.responseDate = responseDate;
    }

    /**
     * <p>
     * Returns a <code>String</code> representing this object.
     * </p>
     *
     * <p>
     * <em>Changes in version 1.0.6:</em>
     * <ol>
     * <li>Added type property.</li>
     * </ol>
     * </p>
     *
     * @return a string representation of this object.
     */
    @Override
    public String toString() {
        return Helper.concat(this.getClass().getName(), "{",
            "id:", id,
            ", lateDeliverableType:", type,
            ", projectId:", projectId,
            ", projectPhaseId:", projectPhaseId,
            ", resourceId:", resourceId,
            ", deliverableId:", deliverableId,
            ", deadline:", deadline,
            ", compensatedDeadline:", compensatedDeadline,
            ", createDate:", createDate,
            ", forgiven:", forgiven,
            ", lastNotified:", lastNotified,
            ", delay:", delay,
            ", explanation:", explanation,
            ", explanationDate:", explanationDate,
            ", response:", response,
            ", responseUser:", responseUser,
            ", responseDate:", responseDate,
            "}");
    }
}
