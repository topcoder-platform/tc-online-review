/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.phase;

import com.topcoder.date.workdays.DefaultWorkdays;
import com.topcoder.project.phases.MockPhase;
import com.topcoder.project.phases.MockPhaseStatus;
import com.topcoder.project.phases.MockPhaseType;
import com.topcoder.project.phases.MockProject;
import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.PhaseStatus;
import com.topcoder.project.phases.PhaseType;
import com.topcoder.project.phases.Project;

import java.util.ArrayList;
import java.util.Date;

/**
 * Mock implementation of <code>PhaseManager</code>.
 *
 * @author assistant
 * @version 1.0
 */
public class MockPhaseManager implements PhaseManager {

    /**
     * <p>A <code>Throwable</code> representing the exception to be thrown from any method of the mock class.</p>
     */
    private static Throwable globalException = null;

    /**
     * All the types.
     */
    private ArrayList types = new ArrayList();

    /**
     * All the statuses.
     */
    private ArrayList statuses = new ArrayList();

    /**
     * The constructor.
     *
     */
    public MockPhaseManager() {
        PhaseStatus open = new MockPhaseStatus(1, "Open");
        open.setId(1);
        open.setName("Open");
        statuses.add(open);

        PhaseType review = new MockPhaseType(1, "Review");
        review.setId(1);
        review.setName("Review");

        PhaseType appeal = new MockPhaseType(2, "Appeals");
        appeal.setId(2);
        appeal.setName("Appeals");

        PhaseType response = new MockPhaseType(3, "Appeals Response");
        response.setId(3);
        response.setName("Appeals Response");

        types.add(review);
        types.add(appeal);
        types.add(response);
    }
    /**
     * Return whether the phase can be cancel or not.
     * @param phase the phase
     * @return always false
     */
    public boolean canCancel(Phase phase) {
        return false;
    }

    /**
     * Return whether the phase can be end or not.
     * @param phase the phase
     * @return always false
     */
    public boolean canEnd(Phase phase) {
        return false;
    }

    /**
     * Return whether the phase can be start or not.
     * @param phase the phase
     * @return always false
     */
    public boolean canStart(Phase phase) {
        return false;
    }

    /**
     * Cancel a phase.
     * @param phase the phase to cancel
     * @param operator the operator
     */
    public void cancel(Phase phase, String operator) {
    }

    /**
     * End a phase.
     * @param phase the phase to cancel
     * @param operator the operator
     */
    public void end(Phase phase, String operator) {
    }

    /**
     * Get all the phase statuses.
     * @return all the phase statuses.
     */
    public PhaseStatus[] getAllPhaseStatuses() {

        return (PhaseStatus[]) statuses.toArray(new PhaseStatus[0]);
    }

    /**
     * Get all the phase types.
     * @return all the phase types.
     */
    public PhaseType[] getAllPhaseTypes() {

        return (PhaseType[]) types.toArray(new PhaseType[0]);
    }

    /**
     * Get the Project.
     * @param p the project id
     * @return the project
     */
    public Project getPhases(long p) throws PhaseManagementException {
        if (MockPhaseManager.globalException != null) {
            if (MockPhaseManager.globalException instanceof PhaseManagementException) {
                throw (PhaseManagementException) MockPhaseManager.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly",
                                           MockPhaseManager.globalException);
            }
        }
        Project project = new MockProject(new Date(), new DefaultWorkdays());
        project.setId(1);

        // review
        MockPhase phase = new MockPhase();
        phase.setId(1);

        PhaseType type = new MockPhaseType(1, "Review");
        type.setId(1);
        type.setName("Review");

        phase.setPhaseType(type);

        // appeal
        MockPhase appeal = new MockPhase();
        appeal.setId(2);

        PhaseType appealType = new MockPhaseType(2, "Appeals");
        appealType.setId(2);
        appealType.setName("Appeals");

        appeal.setPhaseType(appealType);

        // appeal response
        MockPhase response = new MockPhase();
        response.setId(3);

        PhaseType responseType = new MockPhaseType(3, "Appeals Response");
        responseType.setId(3);
        responseType.setName("Appeals Response");

        response.setPhaseType(responseType);

        project.addPhase(phase);
        project.addPhase(appeal);
        project.addPhase(response);
        return project;
    }

    /**
     * Get the phases.
     * @param projects the project ids.
     * @return the project objects
     */
    public Project[] getPhases(long[] projects) {
        return null;
    }

    /**
     * Start a phase.
     * @param phase the phase to start
     * @param operator the operator
     */
    public void start(Phase phase, String operator) {
    }

    /**
     * Update the phases.
     * @param project the project to update
     * @param operator the operator
     */
    public void updatePhases(Project project, String operator) {
    }

    /**
     * Clear the types.
     */
    public void removeType() {
        this.types.clear();
    }

    /**
     * Clear the statuses.
     */
    public void removeStatuses() {
        this.statuses.clear();
    }
    public PhaseHandler[] getAllHandlers() {
        // TODO Auto-generated method stub
        return null;
    }
    public HandlerRegistryInfo[] getHandlerRegistrationInfo(PhaseHandler arg0) {
        // TODO Auto-generated method stub
        return null;
    }
    public PhaseValidator getPhaseValidator() {
        // TODO Auto-generated method stub
        return null;
    }
    public void registerHandler(PhaseHandler arg0, PhaseType arg1, PhaseOperationEnum arg2) {
        // TODO Auto-generated method stub
        
    }
    public void setPhaseValidator(PhaseValidator arg0) {
        // TODO Auto-generated method stub
        
    }
    public PhaseHandler unregisterHandler(PhaseType arg0, PhaseOperationEnum arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * <p>Sets the exception to be thrown when the specified method is called.</p>
     *
     * @param exception a <code>Throwable</code> representing the exception to be thrown whenever any method is called.
     * If this argument is <code>null</code> then no exception will be thrown.
     */
    public static void throwGlobalException(Throwable exception) {
        MockPhaseManager.globalException = exception;
    }

    /**
     * <p>Releases the state of <code>MockLog</code> so all collected method arguments, configured method results and
     * exceptions are lost.</p>
     */
    public static void releaseState() {
        MockPhaseManager.globalException = null;
    }

    /**
     * <p>Initializes the initial state for all created instances of <code>MockResourceManager</code> class.</p>
     */
    public static void init() {
    }
}
