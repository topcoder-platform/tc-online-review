/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.management.phase.autopilot;

/**
 * <p>
 * Returned by AutoPilot to represent the result of auto-piloting a project. It contains the project
 * id, the number of phases that are successfully ended, and the number of phases that are
 * successfully started.
 * </p>
 * <p>
 * This class is mutable (the count is mutable via aggregate()) however it's still thread-safe by
 * synchronizing on aggregate() and corresponding getter methods.
 * </p>
 * @author sindu, abelli
 * @version 1.0
 */
public class AutoPilotResult {

    /**
     * <p>
     * Represents the project id that is represented in this class. This variable is initially -1,
     * initialized in constructor, and immutable afterwards. It can be retrieved with the getter.
     * </p>
     */
    private final long projectId;

    /**
     * <p>
     * Represents the number of phases in the project that are successfully ended. This variable is
     * initially 0, initialized in constructor, and might be added via aggregate(). It can be
     * retrieved with the getter. Access to this variable should be synchronized to ensure
     * thread-safety.
     * </p>
     */
    private int phaseEndedCount = 0;

    /**
     * <p>
     * Represents the number of phases in the project that are successfully started. This variable is
     * initially 0, initialized in constructor, and might be added via aggregate(). It can be
     * retrieved with the getter. Access to this variable should be synchronized to ensure
     * thread-safety.
     * </p>
     */
    private int phaseStartedCount = 0;

    /**
     * <p>
     * Constructs a new instance of AutoPilotResult with the given arguments.
     * </p>
     * @param projectId the project id
     * @param endedCount number of ended phases
     * @param startCount number of started phases
     * @throws IllegalArgumentException if endedCount &lt; 0 or startCount &lt; 0
     */
    public AutoPilotResult(long projectId, int endedCount, int startCount) {
        // Check arguments.
        if (endedCount < 0) {
            throw new IllegalArgumentException("endedCount cannot be negative:" + endedCount);
        }
        if (startCount < 0) {
            throw new IllegalArgumentException("startCount cannot be negative:" + startCount);
        }

        this.projectId = projectId;
        this.phaseEndedCount = endedCount;
        this.phaseStartedCount = startCount;
    }

    /**
     * <p>
     * Return the project id.
     * </p>
     * @return the project id
     */
    public long getProjectId() {
        return this.projectId;
    }

    /**
     * <p>
     * Return the number of ended phase for this project.
     * </p>
     * @return the number of ended phase for this project.
     */
    public synchronized int getPhaseEndedCount() {
        return phaseEndedCount;
    }

    /**
     * <p>
     * Return the number of started phase for this project.
     * </p>
     * @return the number of started phase for this project.
     */
    public synchronized int getPhaseStartedCount() {
        return phaseStartedCount;
    }

    /**
     * <p>
     * Aggregates the given AutoPilotResult count with the current result. This will add the current
     * count (phase ended/started) with the given count. Project id must be the same in order to be
     * aggregated; otherwise IllegalArgumentException is thrown.
     * </p>
     * @param result another AutoPilotResult to aggregate with this instance
     * @throws IllegalArgumentException if argument is null or contains different project id
     */
    public synchronized void aggregate(AutoPilotResult result) {
        // Check arguments.
        if (null == result) {
            throw new IllegalArgumentException("result cannot be null");
        }
        if (getProjectId() != result.getProjectId()) {
            throw new IllegalArgumentException("expect the operand contains project id:"
                + getProjectId() + " but get different project id:" + result.getProjectId());
        }

        phaseEndedCount += result.phaseEndedCount;
        phaseStartedCount += result.phaseStartedCount;
    }
}
