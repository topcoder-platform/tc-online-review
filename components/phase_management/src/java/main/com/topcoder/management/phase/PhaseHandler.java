/*
 * Copyright (C) 2006-2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.phase;

import com.topcoder.project.phases.Phase;

/**
 * <p>
 * Optional, pluggable phase handling mechanism that can be configured per phase type/operation. The handler will
 * provide the decision of whether the start or end operation can be performed as well as extra logic when the phase is
 * starting or ending. Notice that the status and timestamp persistence is still handled by the component. When a user
 * wants a phase to be changed, the manager will check if a handler for that phase (i.e. for that PhaseType and for the
 * operation being done such as START or END a phase) exists and will then use the handler to make decisions about what
 * to do, as well as use the handler for additional work if phase can be changed
 * </p>
 * <p>
 * We have the following invocation scenarios from a PhaseManager implementation with reference to phase handlers:
 * <ol>
 * <li>PhaseManager.canStart() - if a handler exists in the registry then the manager invokes the handler's
 * canPerform() method to see if we can change phase (i.e. start a new phase) and if yes, then we use perform
 * handler.perform() for any additional tasks to be performed.</li>
 * <li>PhaseManager.canEnd() - if a handler exists in the registry then the manager invokes the handler's canPerform()
 * method to see if we can change phase (i.e. end current phase) and if yes, then we use perform handler.perform() for
 * any additional tasks to be performed.</li>
 * <li>PhaseManager.start() - if a handler exists call handler.perform() before performing the associated persistence
 * operations</li>
 * <li>PhaseManager.end() - if a handler exists call handler.perform() before performing the associated persistence
 * operations</li>
 * </ol>
 * </p>
 * <p>
 * Changes in 1.1:
 * <ol>
 * <li>canPerform() method was updated to return not only true/false value, but additionally an explanation message in
 * case if operation cannot be performed</li>
 * </ol>
 * </p>
 * <p>
 * Thread Safety: Implementations of this interface are not required to be thread safe.
 * </p>
 * @author AleaActaEst, saarixx, RachaelLCook, sokol
 * @version 1.1
 */
public interface PhaseHandler {

    /**
     * <p>
     * The handler will make the decision as to whether the start or end operation can be performed for the specified
     * phase.
     * </p>
     * <p>
     * Changes in 1.1:
     * <ol>
     * <li>Changed return type from boolean to OperationCheckResult.</li>
     * </ol>
     * </p>
     * @param phase phase to test
     * @return the validation result indicating whether the associated operation can be performed, and if not,
     *         providing a reasoning message (not null)
     * @throws IllegalArgumentException if <code>phase</code> is <code>null</code>
     * @throws PhaseHandlingException if an exception occurs while determining whether the operation can be performed
     */
    public OperationCheckResult canPerform(Phase phase) throws PhaseHandlingException;

    /**
     * Extra logic to be used when the phase is starting or ending. This will be called by the
     * {@link PhaseManager PhaseManager} implementation at phase change time to perform additional tasks that are due
     * when the specified phase has changed (moved to the next phase).
     * @param phase phase to apply an operation to
     * @param operator operator applying
     * @throws IllegalArgumentException if any argument is <code>null</code>, or if <code>operator</code> is an empty
     *             string
     * @throws PhaseHandlingException if an exception occurs while performing the phase transition
     */
    public void perform(Phase phase, String operator) throws PhaseHandlingException;
}
