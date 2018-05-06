/*
 * Copyright (C) 2010, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.latetracker;

import java.util.Date;

import com.topcoder.management.deliverable.Deliverable;
import com.topcoder.management.project.Project;
import com.topcoder.project.phases.Phase;

/**
 * <p>
 * This class is a container for information about a single late deliverable. It is a simple JavaBean (POJO) that
 * provides getters and setters for all private attributes and performs no argument validation in the setters.
 * </p>
 *
 * <p>
 * <em>Change in 1.1:</em>
 * <ol>
 * <li>Added an attribute that holds a compensated deadline.</li>
 * </ol>
 * </p>
 *
 * <p>
 * <em>Changes in version 1.3:</em>
 * <ol>
 * <li>Added type:LateDeliverableType property.</li>
 * </ol>
 * </p>
 *
 * <p>
 * Thread Safety: This class is mutable and not thread safe.
 * </p>
 *
 * @author saarixx, myxgyy, sparemax
 * @version 1.3
 */
public class LateDeliverable {
    /**
     * <p>
     * The deliverable that is late.
     * </p>
     * <p>
     * Can be any value.
     * </p>
     * <p>
     * Has getter and setter.
     * </p>
     */
    private Deliverable deliverable;

    /**
     * <p>
     * The phase for this late deliverable.
     * </p>
     * <p>
     * Can be any value.
     * </p>
     * <p>
     * Has getter and setter.
     * </p>
     */
    private Phase phase;

    /**
     * <p>
     * The project for this late deliverable.
     * </p>
     * <p>
     * Can be any value.
     * </p>
     * <p>
     * Has getter and setter.
     * </p>
     */
    private Project project;

    /**
     * <p>
     * The compensated deadline for this late deliverable.
     * </p>
     *
     * <p>
     * Is null if compensated deadline equals to the real one (i.e. when no time should be compensated). Can be any
     * value. Has getter and setter.
     * </p>
     *
     * @since 1.1
     */
    private Date compensatedDeadline;

    /**
     * <p>
     * The type of the late deliverable.
     * </p>
     *
     * <p>
     * Can be any value. Has getter and setter.
     * </p>
     *
     * @since 1.3
     */
    private LateDeliverableType type;

    /**
     * Creates an instance of <code>LateDeliverable</code>.
     */
    public LateDeliverable() {
        // Empty
    }

    /**
     * Retrieves the deliverable that is late.
     *
     * @return the deliverable that is late.
     */
    public Deliverable getDeliverable() {
        return deliverable;
    }

    /**
     * Sets the deliverable that is late.
     *
     * @param deliverable
     *            the deliverable that is late.
     */
    public void setDeliverable(Deliverable deliverable) {
        this.deliverable = deliverable;
    }

    /**
     * Retrieves the phase for this late deliverable.
     *
     * @return the phase for this late deliverable.
     */
    public Phase getPhase() {
        return phase;
    }

    /**
     * Sets the phase for this late deliverable.
     *
     * @param phase
     *            the phase for this late deliverable.
     */
    public void setPhase(Phase phase) {
        this.phase = phase;
    }

    /**
     * Retrieves the project for this late deliverable.
     *
     * @return the project for this late deliverable.
     */
    public Project getProject() {
        return project;
    }

    /**
     * Sets the project for this late deliverable.
     *
     * @param project
     *            the project for this late deliverable.
     */
    public void setProject(Project project) {
        this.project = project;
    }

    /**
     * <p>
     * Gets the compensated deadline for this late deliverable.
     * </p>
     *
     * @return the compensated deadline for this late deliverable.
     *
     * @since 1.1
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
     *
     * @since 1.1
     */
    public void setCompensatedDeadline(Date compensatedDeadline) {
        this.compensatedDeadline = compensatedDeadline;
    }

    /**
     * <p>
     * Retrieves the type of the late deliverable.
     * </p>
     *
     * @return the type of the late deliverable.
     *
     * @since 1.3
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
     * @since 1.3
     */
    public void setType(LateDeliverableType type) {
        this.type = type;
    }
}
