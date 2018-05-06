/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.management.phase.autopilot;


/**
 * <p>
 * Defines the contract to pilot a project. This interface defines a method to advance a project's
 * phase given its project id. Advancing a phase means ending all opened phases that can be ended,
 * and starting all scheduled phases that can be started. Interface of this interface will be
 * created by AutoPilot using object factory component.
 * </p>
 * <p>
 * Implementations are not required to be thread-safe. Typically the instance will only be used by a
 * single-thread (eg. from command-line). In a multiple thread situation, application is advised to
 * synchronize on the AutoPilot instance to ensure that only a single thread is retrieving/advancing
 * project phases at one time.
 * </p>
 * @author sindu, abelli
 * @version 1.0
 */
public interface ProjectPilot {
    /**
     * <p>
     * Advances project's phases. This will end all opened phases that can be ended, and start all
     * scheduled phases that can be started. It should repeatedly do so until no more phase changes
     * can be made for the given project. The returned value is an AutoPilotResult instance.
     * </p>
     * @param projectId the project id whose phases to end/start
     * @param operator the operator (used for auditing)
     * @return AutoPilotResult representing number of ended/started phases
     * @throws IllegalArgumentException if operator is null or empty (trimmed) string
     * @throws PhaseOperationException if any error occurs while ending/starting a phase
     */
    public AutoPilotResult advancePhases(long projectId, String operator) throws PhaseOperationException;
}
