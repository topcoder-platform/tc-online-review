/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.management.phase.autopilot;

import com.topcoder.project.phases.Phase;

/**
 * <p>
 * This exception is thrown by ProjectPilot implementations when an error occurs while
 * ending/starting a project phase. It contains the project id and the phase that cause the
 * exception.
 * </p>
 * <p>
 * This class is thread-safe because it's immutable.
 * </p>
 * @author sindu, abelli
 * @version 1.0
 */
public class PhaseOperationException extends AutoPilotException {

    /**
     * <p>
     * Represents the project id that causes the exception. This variable is immutable, initially
     * set to -1, can be set in the constructor, and can be referenced by the getter.
     * </p>
     */
    private final long projectId;

    /**
     * <p>
     * Represents the phase that causes the exception. This variable is immutable, initially set to
     * null, can be set in the constructor (could be null), and can be referenced by the getter.
     * </p>
     */
    private final Phase phase;

    /**
     * <p>
     * Constructs a new instance of PhaseOperationException.
     * </p>
     */
    public PhaseOperationException() {
        this.projectId = -1;
        this.phase = null;
    }

    /**
     * <p>
     * Constructs a new instance of PhaseOperationException with the given project id, phase, and
     * error message.
     * </p>
     * @param projectId the project id that causes the exception
     * @param phase the phase that causes the exception (could be null)
     * @param message the error message
     */
    public PhaseOperationException(long projectId, Phase phase,
        String message) {
        super(message);
        this.projectId = projectId;
        this.phase = phase;
    }

    /**
     * <p>
     * Constructs a new instance of PhaseOperationException with the given project id, phase, error
     * message, and inner exception.
     * </p>
     * @param projectId the project id that causes the exception
     * @param phase the phase that causes the exception
     * @param message the error message
     * @param cause the inner cause
     */
    public PhaseOperationException(long projectId, Phase phase,
        String message, Throwable cause) {
        super(message, cause);
        this.projectId = projectId;
        this.phase = phase;
    }

    /**
     * <p>
     * Gets the project id that causes the exception.
     * </p>
     * @return a long representing the project id. -1 if the project id is not set.
     */
    public long getProjectId() {
        return this.projectId;
    }

    /**
     * <p>
     * Gets the phase that causes the exception.
     * </p>
     * @return the project's Phase that causes the exception (could be null)
     */
    public Phase getPhase() {
        return phase;
    }
}
