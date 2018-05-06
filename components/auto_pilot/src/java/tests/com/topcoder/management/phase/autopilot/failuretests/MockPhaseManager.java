/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.phase.autopilot.failuretests;

import com.topcoder.management.phase.HandlerRegistryInfo;
import com.topcoder.management.phase.PhaseHandler;
import com.topcoder.management.phase.PhaseManagementException;
import com.topcoder.management.phase.PhaseManager;
import com.topcoder.management.phase.PhaseOperationEnum;
import com.topcoder.management.phase.PhaseValidationException;
import com.topcoder.management.phase.PhaseValidator;

import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.PhaseStatus;
import com.topcoder.project.phases.PhaseType;
import com.topcoder.project.phases.Project;


/**
 * A mock class implements <code>PhaseManager</code> interface. The behaviors of some methods like canEnd, end,
 * canStart and start can be adjusted.
 *
 * @author skatou
 * @version 1.0
 */
public class MockPhaseManager implements PhaseManager {
    /** Whether a phase can end. */
    private static boolean canEnd = false;

    /** Whether a phase can start. */
    private static boolean canStart = false;

    /** Whether to throw exception in canEnd method. */
    private static boolean canEndException = false;

    /** Whether to throw exception in canStart method. */
    private static boolean canStartException = false;

    /** Whether to throw exception in end method. */
    private static boolean endException = false;

    /** Whether to throw exception in start method. */
    private static boolean startException = false;

    /** Whether to throw exception in getPhases method. */
    private static boolean getPhasesException = false;

    /**
     * Sets the value of canEnd.
     *
     * @param canEnd whether a phase can end.
     */
    public static void setCanEnd(boolean canEnd) {
        MockPhaseManager.canEnd = canEnd;
    }

    /**
     * Sets the value of canStart.
     *
     * @param canStart whether a phase can start.
     */
    public static void setCanStart(boolean canStart) {
        MockPhaseManager.canStart = canStart;
    }

    /**
     * Sets the value of canEndException.
     *
     * @param canEndException whether throw exception in canEnd method.
     */
    public static void setCanEndException(boolean canEndException) {
        MockPhaseManager.canEndException = canEndException;
    }

    /**
     * Sets the value of canStartException.
     *
     * @param canStartException whether throw exception in canStart method.
     */
    public static void setCanStartException(boolean canStartException) {
        MockPhaseManager.canStartException = canStartException;
    }

    /**
     * Sets the value of endException.
     *
     * @param endException whether throw exception in end method.
     */
    public static void setEndException(boolean endException) {
        MockPhaseManager.endException = endException;
    }

    /**
     * Sets the value of startException.
     *
     * @param startException whether throw exception in start method.
     */
    public static void setStartException(boolean startException) {
        MockPhaseManager.startException = startException;
    }

    /**
     * Sets the value of getPhasesException.
     *
     * @param getPhasesException whether throw exception in start method.
     */
    public static void setGetPhasesException(boolean getPhasesException) {
        MockPhaseManager.getPhasesException = getPhasesException;
    }

    /**
     * Do nothing.
     *
     * @param project ignore.
     * @param operator ignore.
     *
     * @throws PhaseManagementException never.
     */
    public void updatePhases(Project project, String operator)
        throws PhaseManagementException {
    }

    /**
     * Returns null.
     *
     * @param project ignore.
     *
     * @return null.
     *
     * @throws PhaseManagementException if getPhasesException is set.
     */
    public Project getPhases(long project) throws PhaseManagementException {
        if (getPhasesException) {
            throw new PhaseManagementException("");
        }

        return null;
    }

    /**
     * Returns null.
     *
     * @param projects ignore.
     *
     * @return null.
     *
     * @throws PhaseManagementException never.
     */
    public Project[] getPhases(long[] projects) throws PhaseManagementException {
        return null;
    }

    /**
     * Returns null.
     *
     * @return null.
     *
     * @throws PhaseManagementException never.
     */
    public PhaseType[] getAllPhaseTypes() throws PhaseManagementException {
        return null;
    }

    /**
     * Returns null.
     *
     * @return null.
     *
     * @throws PhaseManagementException never.
     */
    public PhaseStatus[] getAllPhaseStatuses() throws PhaseManagementException {
        return null;
    }

    /**
     * Returns whether the specified phase can start.
     *
     * @param phase ignore.
     *
     * @return true if canStart is set.
     *
     * @throws PhaseManagementException if canStartException is set.
     */
    public boolean canStart(Phase phase) throws PhaseManagementException {
        if (canStartException) {
            throw new PhaseManagementException("");
        }

        return canStart;
    }

    /**
     * Starts the specified phase.
     *
     * @param phase ignore.
     * @param operator ignore.
     *
     * @throws PhaseManagementException if startException is set.
     */
    public void start(Phase phase, String operator) throws PhaseManagementException {
        if (startException) {
            throw new PhaseManagementException("");
        }
    }

    /**
     * Returns whether the specified phase can end.
     *
     * @param phase ignore.
     *
     * @return true if canEnd is set.
     *
     * @throws PhaseManagementException if canEndException is set.
     */
    public boolean canEnd(Phase phase) throws PhaseManagementException {
        if (canEndException) {
            throw new PhaseManagementException("");
        }

        return canEnd;
    }

    /**
     * Ends the specified phase.
     *
     * @param phase ignore.
     * @param operator ignore.
     *
     * @throws PhaseManagementException if endException is set.
     */
    public void end(Phase phase, String operator) throws PhaseManagementException {
        if (endException) {
            throw new PhaseManagementException("");
        }
    }

    /**
     * Returns false.
     *
     * @param phase ignore.
     *
     * @return false.
     *
     * @throws PhaseManagementException never.
     */
    public boolean canCancel(Phase phase) throws PhaseManagementException {
        return false;
    }

    /**
     * Do nothing.
     *
     * @param phase ignore.
     * @param operator ignore.
     *
     * @throws PhaseManagementException never.
     */
    public void cancel(Phase phase, String operator) throws PhaseManagementException {
    }

    /**
     * Do nothing.
     *
     * @param handler ignore.
     * @param type ignore.
     * @param operation ignore.
     */
    public void registerHandler(PhaseHandler handler, PhaseType type, PhaseOperationEnum operation) {
    }

    /**
     * Returns null.
     *
     * @param type ignore.
     * @param operation ignore.
     *
     * @return null.
     */
    public PhaseHandler unregisterHandler(PhaseType type, PhaseOperationEnum operation) {
        return null;
    }

    /**
     * Returns null.
     *
     * @return null.
     */
    public PhaseHandler[] getAllHandlers() {
        return null;
    }

    /**
     * Returns null.
     *
     * @param handler ignore.
     *
     * @return null.
     */
    public HandlerRegistryInfo[] getHandlerRegistrationInfo(PhaseHandler handler) {
        return null;
    }

    public PhaseValidator getPhaseValidator() {
      return null;
    }

    public void setPhaseValidator(PhaseValidator arg0) {
    }
}
