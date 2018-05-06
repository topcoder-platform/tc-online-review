/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.management.phase.autopilot.accuracytests;

import com.topcoder.project.phases.Phase;
import com.topcoder.management.project.ProjectManager;
import com.topcoder.management.project.Project;
import com.topcoder.management.phase.autopilot.AutoPilotResult;
import com.topcoder.management.phase.autopilot.ProjectPilot;
import com.topcoder.management.phase.autopilot.AutoPilotSource;
import com.topcoder.management.phase.autopilot.AutoPilot;
import com.topcoder.management.phase.autopilot.accuracytests.mock.MockPhase;
import com.topcoder.management.phase.autopilot.accuracytests.mock.MockProjectManager;
import com.topcoder.management.phase.autopilot.accuracytests.mock.MockProjectManagerAlternate;
import com.topcoder.management.phase.autopilot.accuracytests.mock.MockProject;
import com.topcoder.management.phase.autopilot.accuracytests.mock.MockLog;
import com.topcoder.management.phase.autopilot.accuracytests.mock.MockPhaseManager;
import com.topcoder.management.phase.autopilot.accuracytests.mock.MockPhaseManagerAlternate;
import com.topcoder.management.phase.autopilot.accuracytests.mock.MockPhaseType;
import com.topcoder.management.phase.autopilot.accuracytests.mock.MockProjectPilot;
import com.topcoder.management.phase.autopilot.accuracytests.mock.MockProjectPilotAlternate;
import com.topcoder.management.phase.autopilot.accuracytests.mock.MockPhaseStatus;
import com.topcoder.management.phase.autopilot.accuracytests.mock.MockAutoPilotSource;
import com.topcoder.management.phase.autopilot.accuracytests.mock.MockAutoPilotSourceAlternate;
import com.topcoder.management.phase.autopilot.accuracytests.mock.MockAutoPilot;
import com.topcoder.management.phase.PhaseManager;
import com.topcoder.util.log.Log;
import com.topcoder.date.workdays.DefaultWorkdays;

import java.util.Set;
import java.util.HashSet;
import java.util.Date;

/**
 * <p>A factory producing the sample data which could be used for testing purposes. This class provides a set of static
 * constants and a set of static methods producing the sample data. Note that the methods produce a new copy of sample
 * data on each method call.</p>
 *
 * @author  isv
 * @version 1.0
 */
public class TestDataFactory {

    /**
     * <p>A <code>String</code> providing the sample exception message to be utilized for testing purposes.</p>
     */
    public static final String EXCEPTION_MESSAGE = "An exception message";

    /**
     * <p>A <code>Throwable</code> providing the sample exception cause to be utilized for testing purposes.</p>
     */
    public static final Throwable EXCEPTION_CAUSE = new Exception("Cause");;

    /**
     * <p>A <code>long</code> providing the sample ID of a project to be passed to exception for testing purposes.</p>
     */
    public static final long EXCEPTION_PROJECT_ID = 12345;

    /**
     * <p>A <code>long</code> providing the sample valid ID of a project to be utilized for testing purposes.</p>
     */
    public static final long VALID_PROJECT_ID = 21;

    /**
     * <p>An <code>int</code> providing the sample value for ended phases count.</p>
     */
    public static final int VALID_ENDED_PHASES_COUNT = 10;

    /**
     * <p>An <code>int</code> providing the sample value for started phases count.</p>
     */
    public static final int VALID_STARTED_PHASES_COUNT = 20;

    /**
     * <p>A <code>String</code> providing the auto pilot job namespace to be used for testing.</p>
     */
    public static final String AUTO_PILOT_JOB_NAMESPACE = "com.topcoder.management.phase.autopilot.accuracytests";

    /**
     * <p>A <code>String</code> providing the auto pilot namespace to be used for testing.</p>
     */
    public static final String AUTO_PILOT_SOURCE_NAMESPACE = "com.topcoder.management.phase.autopilot.accuracytests";

    /**
     * <p>A <code>String</code> providing the status for active project.</p>
     */
    public static final String ACTIVE_PROJECT_STATUS = "ActiveProject";

    /**
     * <p>A <code>String</code> providing the name of the auto pilot switch.</p>
     */
    public static final String AUTO_PILOT_PROPERTY_NAME = "AutoPilotProject";

    /**
     * <p>A <code>String</code> providing the value of auto pilot switch tobe used for testing.</p>
     */
    public static final String AUTO_PILOT_PROPERTY_VALUE = "on";

    /**
     * <p>A <code>String</cdoe> providing the configuration property referencing the project manager to be in use.</p>
     */
    public static final String PROJECT_MANAGER_CONFIG_PROPERTY = "ProjectManager";

    /**
     * <p>A <code>String</code> providing the default project pilot namespace to be used for testing.</p>
     */
    public static final String DEFAULT_PROJECT_PILOT_NAMESPACE
        = "com.topcoder.management.phase.autopilot.accuracytests";

    /**
     * <p>A <code>String</cdoe> providing the configuration property referencing the phase manager to be in use.</p>
     */
    public static final String PHASE_MANAGER_CONFIG_PROPERTY = "PhaseManager";

    /**
     * <p>A <code>String</cdoe> providing the status for scheduled phase.</p>
     */
    public static final String SCHEDULED_PHASE_STATUS = "Scheduled";

    /**
     * <p>A <code>String</cdoe> providing the status for open phase.</p>
     */
    public static final String OPEN_PHASE_STATUS = "Open";

    /**
     * <p>A <code>String</code> providing the sample log name to be utilized for testing purposes.</p>
     */
    public static final String LOG_NAME = "AccuracyLog";

    /**
     * <p>A <code>String</code> providing the sample operator name to be utilized for testing purposes.</p>
     */
    public static final String OPERATOR = "AccuracyTest";

    /**
     * <p>A <code>String</code> providing the configuration namespace for auto pilot to be used for testing.</p>
     */
    public static final String AUTO_PILOT_NAMESPACE = "com.topcoder.management.phase.autopilot.accuracytests";

    /**
     * <p>Generates a new instance of <code>Phase</code> type initialized with random data.</p>
     *
     * @return a new <code>Phase</code> instance.
     */
    public static Phase getExceptionPhase() {
        return (Phase) new MockPhase(1, getPhaseProject());
    }

    /**
     * <p>Generates a new instance of <code>AutoPilotResult</code> type initialized with random data.</p>
     *
     * @return a new <code>AutoPilotResult</code> instance.
     */
    public static AutoPilotResult getAutoPilotResult() {
        return new AutoPilotResult(VALID_PROJECT_ID, VALID_ENDED_PHASES_COUNT, VALID_STARTED_PHASES_COUNT);
    }

    /**
     * <p>Generates a new instance of <code>AutoPilotResult</code> type initialized with random data.</p>
     *
     * @return a new <code>AutoPilotResult</code> instance.
     */
    public static AutoPilotResult getDifferentAutoPilotResult() {
        return new AutoPilotResult(9999, 7, 11);
    }

    /**
     * <p>Generates a new instance of <code>ProjectManager</code> type initialized with random data.</p>
     *
     * @return a new <code>ProjectManager</code> instance.
     */
    public static ProjectManager getProjectManager() {
        return (ProjectManager) new MockProjectManager();
    }

    /**
     * <p>Generates a new instance of <code>ProjectManager</code> type initialized with random data.</p>
     *
     * @return a new <code>ProjectManager</code> instance.
     */
    public static ProjectManager getAlternateProjectManager() {
        return new MockProjectManagerAlternate();
    }

    /**
     * <p>Generates a new instance of <code>Project[]</code> type initialized with random data.</p>
     *
     * @return a new <code>Project[]</code> instance.
     */
    public static Project[] getActiveProjects() {
        long[] projectIds = getActiveProjectIds();
        Project[] result = new Project[projectIds.length];
        for (int i = 0; i < projectIds.length; i++) {
            result[i] = new MockProject();
            result[i].setId(projectIds[i]);
        }
        return result;
    }

    /**
     * <p>Generates a new instance of <code>long[]</code> type initialized with random data.</p>
     *
     * @return a new <code>long[]</code> instance.
     */
    public static long[] getActiveProjectIds() {
        return new long[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    }

    /**
     * <p>Generates a new instance of <code>Log</code> type initialized with random data.</p>
     *
     * @return a new <code>Log</code> instance.
     */
    public static Log getLog() {
        return new MockLog("");
    }

    /**
     * <p>Generates a new instance of <code>Log</code> type initialized with random data.</p>
     *
     * @return a new <code>Log</code> instance.
     */
    public static Log getAlternateLog() {
        return new MockLog("");
    }

    /**
     * <p>Generates a new instance of <code>PhaseManager</code> type initialized with random data.</p>
     *
     * @return a new <code>PhaseManager</code> instance.
     */
    public static PhaseManager getPhaseManager() {
        return new MockPhaseManager();
    }

    /**
     * <p>Generates a new instance of <code>PhaseManager</code> type initialized with random data.</p>
     *
     * @return a new <code>PhaseManager</code> instance.
     */
    public static PhaseManager getAlternatePhaseManager() {
        return new MockPhaseManagerAlternate();
    }

    /**
     * <p>Generates a new instance of <code>Phase</code> type initialized with random data.</p>
     *
     * @return a new <code>Phase</code> instance.
     */
    public static Phase getPhaseWithNonNullType(com.topcoder.project.phases.Project project) {
        MockPhase result = new MockPhase(1, project);
        result.setPhaseType(MockPhaseType.REVIEW);
        return result;
    }

    /**
     * <p>Generates a new instance of <code>Phase</code> type initialized with random data.</p>
     *
     * @return a new <code>Phase</code> instance.
     */
    public static Phase getPhaseWithNullType(com.topcoder.project.phases.Project project) {
        MockPhase result = new MockPhase(1, project);
        result.setPhaseType(null);
        return result;
    }

    /**
     * <p>Generates a new instance of <code>Phase</code> type initialized with random data.</p>
     *
     * @return a new <code>Phase</code> instance.
     */
    public static Phase getClosedPhase(com.topcoder.project.phases.Project project) {
        MockPhase result = new MockPhase(1, project);
        result.setPhaseType(MockPhaseType.REVIEW);
        result.setPhaseStatus(MockPhaseStatus.CLOSED);
        return result;
    }

    /**
     * <p>Generates a new instance of <code>Phase</code> type initialized with random data.</p>
     *
     * @return a new <code>Phase</code> instance.
     */
    public static Phase getOpenPhase(com.topcoder.project.phases.Project project) {
        MockPhase result = new MockPhase(1, project);
        result.setPhaseType(MockPhaseType.REVIEW);
        result.setPhaseStatus(MockPhaseStatus.OPEN);
        return result;
    }

    /**
     * <p>Generates a new instance of <code>Phase</code> type initialized with random data.</p>
     *
     * @return a new <code>Phase</code> instance.
     */
    public static Phase getScheduledPhase(com.topcoder.project.phases.Project project) {
        MockPhase result = new MockPhase(1, project);
        result.setPhaseType(MockPhaseType.REVIEW);
        result.setPhaseStatus(MockPhaseStatus.SCHEDULED);
        return result;
    }

    /**
     * <p>Generates a new instance of <code>Phase</code> type initialized with random data.</p>
     *
     * @return a new <code>Phase</code> instance.
     */
    public static Phase getPhaseWithNullStatus(com.topcoder.project.phases.Project project) {
        MockPhase result = new MockPhase(1, project);
        result.setPhaseType(MockPhaseType.REVIEW);
        result.setPhaseStatus(null);
        return result;
    }

    /**
     * <p>Generates a new instance of <code>Phase</code> type initialized with random data.</p>
     *
     * @return a new <code>Phase</code> instance.
     */
    public static Phase getPhaseWithoutDependencies(com.topcoder.project.phases.Project project) {
        MockPhase result = new MockPhase(1, project);
        result.setPhaseType(MockPhaseType.REVIEW);
        result.setPhaseStatus(MockPhaseStatus.OPEN);
        return result;
    }

    /**
     * <p>Generates a new instance of <code>Phase</code> type initialized with random data.</p>
     *
     * @return a new <code>Phase</code> instance.
     */
    public static Phase getProcessedPhase(com.topcoder.project.phases.Project project) {
        MockPhase result = new MockPhase(1, project);
        result.setPhaseType(MockPhaseType.REVIEW);
        result.setPhaseStatus(MockPhaseStatus.CLOSED);
        return result;
    }

    /**
     * <p>Generates a new instance of <code>Set</code> type initialized with random data.</p>
     *
     * @return a new <code>Set</code> instance.
     */
    public static Set getProcessedPhases() {
        Set result = new HashSet();
        result.add(new Long(1));
        return result;
    }

    /**
     * <p>Generates a new instance of <code>Phase</code> type initialized with random data.</p>
     *
     * @return a new <code>Phase</code> instance.
     */
    public static Phase getClosedPhaseWitDependencies() {
        MockPhase result = new MockPhase(1, null);
        result.setPhaseType(MockPhaseType.REVIEW);
        result.setPhaseStatus(MockPhaseStatus.CLOSED);
        return result;
    }

    /**
     * <p>Generates a new instance of <code>Phase</code> type initialized with random data.</p>
     *
     * @return a new <code>Phase</code> instance.
     */
    public static Phase getOpenPhaseWitDependencies() {
        MockPhase result = new MockPhase(1, null);
        result.setPhaseType(MockPhaseType.REVIEW);
        result.setPhaseStatus(MockPhaseStatus.OPEN);
        return result;
    }

    /**
     * <p>Generates a new instance of <code>Phase</code> type initialized with random data.</p>
     *
     * @return a new <code>Phase</code> instance.
     */
    public static Phase getScheduledPhaseWitDependencies() {
        MockPhase result = new MockPhase(1, null);
        result.setPhaseType(MockPhaseType.REVIEW);
        result.setPhaseStatus(MockPhaseStatus.SCHEDULED);
        return result;
    }

    /**
     * <p>Generates a new instance of <code>AutoPilotSource</code> type initialized with random data.</p>
     *
     * @return a new <code>AutoPilotSource</code> instance.
     */
    public static AutoPilotSource getAutoPilotSource() {
        return new MockAutoPilotSource();
    }

    /**
     * <p>Generates a new instance of <code>AutoPilotSource</code> type initialized with random data.</p>
     *
     * @return a new <code>AutoPilotSource</code> instance.
     */
    public static AutoPilotSource getAlternateAutoPilotSource() {
        return new MockAutoPilotSourceAlternate();
    }

    /**
     * <p>Generates a new instance of <code>ProjectPilot</code> type initialized with random data.</p>
     *
     * @return a new <code>ProjectPilot</code> instance.
     */
    public static ProjectPilot getProjectPilot() {
        return (ProjectPilot) new MockProjectPilot();
    }

    /**
     * <p>Generates a new instance of <code>ProjectPilot</code> type initialized with random data.</p>
     *
     * @return a new <code>ProjectPilot</code> instance.
     */
    public static ProjectPilot getAlternateProjectPilot() {
        return (ProjectPilot) new MockProjectPilotAlternate();
    }

    /**
     * <p>Generates a new instance of <code>long</code> type initialized with random data.</p>
     *
     * @return a new <code>long</code> instance.
     */
    public static long getAlternateProjectId() {
        return 100;
    }

    /**
     * <p>Generates a new instance of <code>long[]</code> type initialized with random data.</p>
     *
     * @return a new <code>long[]</code> instance.
     */
    public static long[] getAdvancableProjectIds() {
        long[] result = getActiveProjectIds();
        return result;
    }

    /**
     * <p>Generates a new instance of <code>long[]</code> type initialized with random data.</p>
     *
     * @return a new <code>long[]</code> instance.
     */
    public static long[] getNonAdvancableProjectIds() {
        long[] result = getActiveProjectIds();
        return result;
    }

    /**
     * <p>Gets the auto pilot instance to be used for testing.</p>
     *
     * @return
     * @throws Exception if an unexpected error occurs.
     */
    public static AutoPilot getAutoPilot() throws Exception {
        return new MockAutoPilot();
    }

    /**
     * <p>Gets the phase project.</p>
     *
     * @return a <code>Project</code> representing a sample phase project.
     */
    public static com.topcoder.project.phases.Project getPhaseProject() {
        com.topcoder.project.phases.Project project = new com.topcoder.project.phases.Project(new Date(),
                                                                                              new DefaultWorkdays());
        return project;
    }
}
