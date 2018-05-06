/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases.stresstests;

import java.sql.Connection;
import java.util.Date;

import com.cronos.onlinereview.phases.RegistrationPhaseHandler;
import com.topcoder.management.resource.Resource;
import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.PhaseStatus;
import com.topcoder.project.phases.Project;
import com.topcoder.util.config.ConfigManager;

/**
 * <p>
 * Stress tests for <code>RegistrationPhaseHandler</code>.
 * </p>
 * <p>
 * Since this handler is immutable, so it's naturally thread safe. Here just do benchmark tests.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.3
 */
public class RegistrationPhaseHandlerTest extends StressBaseTest {

    /**
     * sets up the environment required for test cases for this class.
     *
     * @throws Exception
     *             not under test.
     */
    protected void setUp() throws Exception {
        super.setUp();

        ConfigManager configManager = ConfigManager.getInstance();

        configManager.add(PHASE_HANDLER_CONFIG_FILE);
        configManager.add(MANAGER_HELPER_CONFIG_FILE);
        configManager.add(DOC_GENERATOR_CONFIG_FILE);
        configManager.add(EMAIL_CONFIG_FILE);

        // add the component configurations as well
        for (int i = 0; i < COMPONENT_FILE_NAMES.length; i++) {
            configManager.add(COMPONENT_FILE_NAMES[i]);
        }
    }

    /**
     * Clean up the environment.
     *
     * @throws Exception
     *             to JUnit
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Tests the RegistrationPhaseHandler() constructor and canPerform with Scheduled and Open
     * statuses.
     *
     * @throws Exception
     *             not under test.
     */
    public void testRegistrationPhaseHandler() throws Exception {
        RegistrationPhaseHandler handler = new RegistrationPhaseHandler();

        try {
            cleanTables();

            // create phases
            Project project = setupPhases();

            Phase[] phases = project.getAllPhases();
            Phase registration = phases[0];

            // test with open status, time has passed.
            registration.setPhaseStatus(PhaseStatus.OPEN);

            // time has passed, but no registrants
            // version 1.1 change : canPerform should return true
            // now when there is no registration
            registration.setActualEndDate(new Date());
            registration.setAttribute("Registration Number", "1");

            // time has passed, and enough registrants.
            Connection conn = getConnection();
            Resource resource = super.createResource(1, 101L, 1, 1);
            super.insertResources(conn, new Resource[]{resource });

            startRecord();
            for (int i = 0; i < FIRST_LEVEL; i++) {
                handler.canPerform(registration);
            }
            endRecord("RegistrationPhaseHandler::canPerform(Phase)--(the phase status is false)", FIRST_LEVEL);
        } finally {
            closeConnection();
            cleanTables();
        }
    }

    /**
     * Tests the RegistrationPhaseHandler() constructor and canPerform with Scheduled and Open
     * statuses.
     *
     * @throws Exception
     *             not under test.
     */
    public void testRegistrationPhaseHandler1() throws Exception {
        RegistrationPhaseHandler handler = new RegistrationPhaseHandler(
            RegistrationPhaseHandler.DEFAULT_NAMESPACE);

        try {
            cleanTables();

            // create phases
            Project project = setupPhasesWithDepedentProject();

            Phase[] phases = project.getAllPhases();
            Phase registration = phases[0];

            // test with scheduled status, but time not passed.
            registration.setPhaseStatus(PhaseStatus.SCHEDULED);

            startRecord();
            for (int i = 0; i < FIRST_LEVEL; i++) {
                handler.canPerform(registration);
            }
            endRecord(
                "RegistrationPhaseHandler::canPerform(Phase)--"
                    + "(the phase status is true, the canStart is true, the allParentProjectsCompleted is false)",
                FIRST_LEVEL);
        } finally {
            closeConnection();
            cleanTables();
        }
    }

}
