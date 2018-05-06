/*
 * Copyright (C) 2006, TopCoder Inc. All rights reserved.
 */

package com.topcoder.management.phase.autopilot.accuracytests.impl;

import com.topcoder.management.phase.PhaseManager;
import com.topcoder.management.phase.autopilot.AutoPilotResult;
import com.topcoder.management.phase.autopilot.ConfigurationException;
import com.topcoder.management.phase.autopilot.PhaseOperationException;
import com.topcoder.management.phase.autopilot.impl.DefaultProjectPilot;
import com.topcoder.project.phases.Phase;
import com.topcoder.util.log.Log;

import java.util.Set;

/**
 * <p>A subclass of <code>DefaultProjectPilot</code> class to be used to test the protected methods of the super class.
 * Overrides the protected methods declared by a super-class. The overridden methods are declared with package private
 * access so only the test cases could invoke them. The overridden methods simply call the corresponding method of a
 * super-class.
 *
 * @author isv
 * @version 1.0
 */
class DefaultProjectPilotSubclass extends DefaultProjectPilot {

    /**
     * <p>Constructs new <code>DefaultProjectPilotSubclass</code> instance. Nothing special occurs here.</p>
     *
     * @throws ConfigurationException if any error occurs instantiating the object factory or the phase manager
     * instance
     */
    public DefaultProjectPilotSubclass() throws ConfigurationException {
        super();
    }

    /**
     * <p>Constructs new <code>DefaultProjectPilotSubclass</code> instance. Nothing special occurs here.</p>
     *
     * @param namespace the namespace to initialize object factory with
     * @param phaseManagerKey the key defining the PhaseManager instance
     * @param scheduledStatusName A non-null string representing a phase status of scheduled
     * @param openStatusName A non-null string representing a phase status of open
     * @param logName A non-null string representing the log name to use for auditing
     * @throws IllegalArgumentException if any parameter is null or empty (trimmed) string
     * @throws ConfigurationException if any error occurs instantiating the object factory or the phase manager instance
     * or the logger
     */
    public DefaultProjectPilotSubclass(String namespace, String phaseManagerKey, String scheduledStatusName,
                                       String openStatusName, String logName) throws ConfigurationException {
        super(namespace, phaseManagerKey, scheduledStatusName, openStatusName, logName);
    }

    /**
     * <p>Constructs new <code>DefaultProjectPilotSubclass</code> instance. Nothing special occurs here.</p>
     *
     * @param phaseManager the PhaseManager instance to use
     * @param scheduledStatusName A non-null string representing a phase status of scheduled
     * @param openStatusName A non-null string representing a phase status of open
     * @param logger the Logger instance to use for auditing
     * @throws IllegalArgumentException if any of the parameter is null or any of the string parameters are empty
     * (trimmed) string
     */
    public DefaultProjectPilotSubclass(PhaseManager phaseManager, String scheduledStatusName, String openStatusName,
                                       Log logger) {
        super(phaseManager, scheduledStatusName, openStatusName, logger);
    }

    /**
     * <p>Calls corresponding method of a super class providing the specified argument. Nothing special occurs
     * here.</p>
     */
    public PhaseManager getPhaseManager() {
        return super.getPhaseManager();
    }

    /**
     * <p>Calls corresponding method of a super class providing the specified argument. Nothing special occurs
     * here.</p>
     */
    public Log getLogger() {
        return super.getLogger();
    }

    /**
     * <p>Calls corresponding method of a super class providing the specified argument. Nothing special occurs
     * here.</p>
     */
    public String getScheduledStatusName() {
        return super.getScheduledStatusName();
    }

    /**
     * <p>Calls corresponding method of a super class providing the specified argument. Nothing special occurs
     * here.</p>
     */
    public String getOpenStatusName() {
        return super.getOpenStatusName();
    }

    /**
     * <p>Calls corresponding method of a super class providing the specified argument. Nothing special occurs
     * here.</p>
     *
     * @param projectId the project id whose phases to end/start
     * @param operator the operator (used for auditing)
     * @throws IllegalArgumentException if operator is null or an empty (trimmed) string
     */
    public AutoPilotResult advancePhases(long projectId, String operator) {
        return super.advancePhases(projectId, operator);
    }

    /**
     * <p>Calls corresponding method of a super class providing the specified argument. Nothing special occurs
     * here.</p>
     *
     * @param phase the current phase to process
     * @param processedPhase a set of Long representing phases id that have been processed
     * @param operator the operator name for auditing
     * @throws PhaseOperationException if any error occurs processing the phase
     */
    public int[] processPhase(Phase phase, Set processedPhase, String operator) throws PhaseOperationException {
        return super.processPhase(phase, processedPhase, operator);
    }

    /**
     * <p>Calls corresponding method of a super class providing the specified argument. Nothing special occurs
     * here.</p>
     *
     * @param phase the phase to end/start
     * @param operator the operator name for auditing
     * @throws PhaseOperationException if any error occurs ending/starting the phase
     */
    public int[] doPhaseOperation(Phase phase, String operator) throws PhaseOperationException {
        return super.doPhaseOperation(phase, operator);
    }

    /**
     * <p>Calls corresponding method of a super class providing the specified argument. Nothing special occurs
     * here.</p>
     *
     * @param phase the phase to audit
     * @param isEnd true if the phase was ended; false if the phase was started
     * @param operator the operator name to audit
     * @throws PhaseOperationException if any error occurs auditing the entry
     */
    public void doAudit(Phase phase, boolean isEnd, String operator) throws PhaseOperationException {
        super.doAudit(phase, isEnd, operator);
    }
}
