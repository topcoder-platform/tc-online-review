/*
 * Copyright (C) 2006-2011 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import com.topcoder.management.phase.PhaseHandlingException;
import com.topcoder.management.resource.Resource;

import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.PhaseStatus;
import com.topcoder.project.phases.Project;

import com.topcoder.util.config.ConfigManager;

import java.sql.Connection;

import java.util.Date;

/**
 * All test cases for RegistrationPhaseHandler class.
 * <p>
 * Version 1.1 change notes: test cases are added for testing perform with registrations and no registrations. When
 * there is no registration, a post-mortem phase should be inserted.
 * </p>
 * <p>
 * Version 1.2 change notes : since the email-templates and role-supported has been enhanced. The test cases will
 * try to do on that way while for email content, please check it manually.
 * </p>
 * <p>
 * Version 1.4 change notes : add new test cases for updated function in version 1.4.
 * </p>
 * <p>
 * Version 1.6.1 changes note:
 * <ul>
 * <li>Change some test because the return of canPerform change from boolean to OperationCheckResult.</li>
 * </ul>
 * </p>
 * @author bose_java, waits, myxgyy, microsky
 * @version 1.6.1
 * @since 1.0
 */
public class RegistrationPhaseHandlerTest extends BaseTest {
    /**
     * sets up the environment required for test cases for this class.
     * @throws Exception not under test.
     */
    protected void setUp() throws Exception {
        super.setUp();

        ConfigManager configManager = ConfigManager.getInstance();

        configManager.add(PHASE_HANDLER_CONFIG_FILE);
        configManager.add(MANAGER_HELPER_CONFIG_FILE);
        configManager.add(EMAIL_CONFIG_FILE);

        // add the component configurations as well
        for (int i = 0; i < COMPONENT_FILE_NAMES.length; i++) {
            configManager.add(COMPONENT_FILE_NAMES[i]);
        }
    }

    /**
     * cleans up the environment required for test cases for this class.
     * @throws Exception not under test.
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Tests canPerform(Phase) with null phase.
     * @throws Exception not under test.
     */
    public void testCanPerform() throws Exception {
        RegistrationPhaseHandler handler = new RegistrationPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            handler.canPerform(null);
            fail("canPerform() did not throw IllegalArgumentException for null argument.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Tests canPerform(Phase) with invalid phase status.
     * @throws Exception not under test.
     */
    public void testCanPerformWithInvalidStatus() throws Exception {
        RegistrationPhaseHandler handler = new RegistrationPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            Phase phase = createPhase(1, 1, "Invalid", 1, "Registration");
            handler.canPerform(phase);
            fail("canPerform() did not throw PhaseHandlingException for invalid phase status.");
        } catch (PhaseHandlingException e) {
            // expected.
        }
    }

    /**
     * Tests canPerform(Phase) with invalid phase type.
     * @throws Exception not under test.
     */
    public void testCanPerformWithInvalidType() throws Exception {
        RegistrationPhaseHandler handler = new RegistrationPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            Phase phase = createPhase(1, 1, "Scheduled", 1, "INVALID");
            handler.canPerform(phase);
            fail("canPerform() did not throw PhaseHandlingException for invalid phase type.");
        } catch (PhaseHandlingException e) {
            // expected.
        }
    }

    /**
     * Tests perform(Phase) with null phase.
     * @throws Exception not under test.
     */
    public void testPerformWithNullPhase() throws Exception {
        RegistrationPhaseHandler handler = new RegistrationPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            handler.perform(null, "operator");
            fail("perform() did not throw IllegalArgumentException for null argument.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Tests perform(Phase) with invalid phase status.
     * @throws Exception not under test.
     */
    public void testPerformWithInvalidStatus() throws Exception {
        RegistrationPhaseHandler handler = new RegistrationPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            Phase phase = createPhase(1, 1, "Invalid", 1, "Registration");
            handler.perform(phase, "operator");
            fail("perform() did not throw PhaseHandlingException for invalid phase status.");
        } catch (PhaseHandlingException e) {
            // expected.
        }
    }

    /**
     * Tests perform(Phase) with invalid phase type.
     * @throws Exception not under test.
     */
    public void testPerformWithInvalidType() throws Exception {
        RegistrationPhaseHandler handler = new RegistrationPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            Phase phase = createPhase(1, 1, "Scheduled", 1, "INVALID");
            handler.perform(phase, "operator");
            fail("perform() did not throw PhaseHandlingException for invalid phase type.");
        } catch (PhaseHandlingException e) {
            // expected.
        }
    }

    /**
     * Tests perform(Phase) with null operator.
     * @throws Exception not under test.
     */
    public void testPerformWithNullOperator() throws Exception {
        RegistrationPhaseHandler handler = new RegistrationPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            Phase phase = createPhase(1, 1, "Scheduled", 1, "Registration");
            handler.perform(phase, null);
            fail("perform() did not throw IllegalArgumentException for null operator.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Tests perform(Phase) with empty operator.
     * @throws Exception not under test.
     */
    public void testPerformWithEmptyOperator() throws Exception {
        RegistrationPhaseHandler handler = new RegistrationPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            Phase phase = createPhase(1, 1, "Scheduled", 1, "Registration");
            handler.perform(phase, "   ");
            fail("perform() did not throw IllegalArgumentException for empty operator.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Tests the canPerform method with Scheduled statuses. The phase is the first phase,
     * and parent project completed, the method should return true.
     * @throws Exception not under test.
     * @since 1.4
     */
    public void testCanPerformScheduled1() throws Exception {
        RegistrationPhaseHandler handler = new RegistrationPhaseHandler();

        try {
            cleanTables();

            // create phases
            Project project = super.setupPhasesWithDepedentProject();

            Phase[] phases = project.getAllPhases();
            Phase registration = phases[0];

            // test with scheduled status.
            registration.setPhaseStatus(PhaseStatus.SCHEDULED);
            assertTrue("can start should return true", handler.canPerform(registration).isSuccess());
        } finally {
            closeConnection();
            cleanTables();
        }
    }

    /**
     * Tests the canPerform method with Scheduled statuses. The phase is not first phase,
     * the method should return true.
     * @throws Exception not under test.
     * @since 1.4
     */
    public void testCanPerformScheduled2() throws Exception {
        RegistrationPhaseHandler handler = new RegistrationPhaseHandler();

        try {
            cleanTables();

            // create phases
            Project project = super.setupPhasesForSpec(true);

            Phase[] phases = project.getAllPhases();
            Phase registration = phases[2];

            phases[0].setPhaseStatus(PhaseStatus.CLOSED);
            phases[1].setPhaseStatus(PhaseStatus.CLOSED);
            // test with scheduled status.
            registration.setPhaseStatus(PhaseStatus.SCHEDULED);
            assertTrue("can start should return true", handler.canPerform(registration).isSuccess());
        } finally {
            closeConnection();
            cleanTables();
        }
    }

    /**
     * Tests the canPerform method with Scheduled statuses. The phase is the first phase,
     * but the parent project not completed, the method should return false.
     * @throws Exception not under test.
     * @since 1.4
     */
    public void testCanPerformScheduled_ParentProjectNotCompleted() throws Exception {
        RegistrationPhaseHandler handler = new RegistrationPhaseHandler();

        try {
            cleanTables();

            // create phases
            Project project = super.setupPhasesWithNonCompletedDepedentProject();

            Phase[] phases = project.getAllPhases();
            Phase registration = phases[0];

            // test with scheduled status.
            registration.setPhaseStatus(PhaseStatus.SCHEDULED);
            assertFalse("can start should return false", handler.canPerform(registration).isSuccess());
        } finally {
            closeConnection();
            cleanTables();
        }
    }

    /**
     * Tests the RegistrationPhaseHandler() constructor and canPerform with Scheduled and Open statuses.
     * @throws Exception not under test.
     */
    public void testRegistrationPhaseHandler() throws Exception {
        RegistrationPhaseHandler handler = new RegistrationPhaseHandler();

        try {
            cleanTables();

            // create phases
            Project project = setupPhases();

            Phase[] phases = project.getAllPhases();
            Phase registration = phases[0];

            // test with scheduled status.
            registration.setPhaseStatus(PhaseStatus.SCHEDULED);
            assertTrue("can start should return true", handler.canPerform(registration).isSuccess());

            // test with open status, time has passed.
            registration.setPhaseStatus(PhaseStatus.OPEN);
            assertTrue("can stop should return true", handler.canPerform(registration).isSuccess());

            // time has passed, but no registrants
            // version 1.1 change : canPerform should return true
            // now when there is no registration
            registration.setActualEndDate(new Date());
            registration.setAttribute("Registration Number", "0");
            assertTrue("can stop should return True", handler.canPerform(registration).isSuccess());

            // time has passed, and enough registrants.
            Connection conn = getConnection();
            Resource resource = super.createResource(1, 101L, 1, 1);
            super.insertResources(conn, new Resource[] {resource });
            assertTrue("can stop should return true", handler.canPerform(registration).isSuccess());
        } finally {
            closeConnection();
            cleanTables();
        }
    }

    /**
     * Tests the canPerform method with open statuses. The dependencies are not met,
     * the method should return false.
     * @throws Exception not under test.
     * @since 1.4
     */
    public void testCanPerformOpen_DependenciesNotMet() throws Exception {
        RegistrationPhaseHandler handler = new RegistrationPhaseHandler();

        try {
            cleanTables();

            // create phases
            Project project = super.setupPhasesForSpec(true);

            Phase[] phases = project.getAllPhases();
            Phase registration = phases[2];

            registration.getAllDependencies()[0].setDependentStart(false);
            // test with scheduled status.
            registration.setPhaseStatus(PhaseStatus.OPEN);
            assertFalse("can start should return false", handler.canPerform(registration).isSuccess());
        } finally {
            closeConnection();
            cleanTables();
        }
    }

    /**
     * Tests the canPerform method with open statuses. The registration is not enough,
     * the method should return false.
     * @throws Exception not under test.
     * @since 1.4
     */
    public void testCanPerformOpen_RegistrationNotEnough() throws Exception {
        RegistrationPhaseHandler handler = new RegistrationPhaseHandler();

        try {
            cleanTables();

            // create phases
            Project project = super.setupPhasesForSpec(true);

            Phase[] phases = project.getAllPhases();
            Phase registration = phases[2];

            // test with scheduled status.
            registration.setPhaseStatus(PhaseStatus.OPEN);
            registration.setAttribute("Registration Number", "2");

            // only one register
            Connection conn = getConnection();
            Resource resource = super.createResource(1, 101L, 1, 1);
            super.insertResources(conn, new Resource[] {resource });
            assertFalse("can start should return false", handler.canPerform(registration).isSuccess());
        } finally {
            closeConnection();
            cleanTables();
        }
    }

    /**
     * Tests the RegistrationPhaseHandler() constructor and canPerform with Scheduled and Open statuses.
     * @throws Exception not under test.
     */
    public void testRegistrationPhaseHandler1() throws Exception {
        RegistrationPhaseHandler handler = new RegistrationPhaseHandler(RegistrationPhaseHandler.DEFAULT_NAMESPACE);

        try {
            cleanTables();

            // create phases
            Project project = setupPhasesWithDepedentProject();

            Phase[] phases = project.getAllPhases();
            Phase registration = phases[0];

            // test with scheduled status, but time not passed.
            registration.setPhaseStatus(PhaseStatus.SCHEDULED);
            handler.canPerform(registration);
        } finally {
            closeConnection();
            cleanTables();
        }
    }

    /**
     * Tests the RegistrationPhaseHandler() constructor and perform with Scheduled and Open statuses but without
     * registrations. The test checks whether the post-mortem phase is inserted when there is no registration.
     * @throws Exception to Junit.
     * @since 1.1
     */
    public void testPerformWithNoRegistrants() throws Exception {
        RegistrationPhaseHandler handler = new RegistrationPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            cleanTables();

            // test with scheduled status.
            Phase registration = createPhase(1, 1, "Scheduled", 1, "Registration");
            Connection conn = getConnection();
            insertProject(conn);
            insertProjectInfo(conn, 1, new long[] {44 }, new String[] {"true" });

            String operator = "1001";

            // test with open status
            registration.setPhaseStatus(PhaseStatus.OPEN);
            handler.perform(registration, operator);

            // check whether the Post-Mortem phase is inserted when
            // there is no submission
            assertTrue("Post-mortem phase should be inserted", havePostMortemPhase(conn));
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Tests the perform method to start the registration phase.
     * @throws Exception to Junit.
     * @since 1.2
     */
    public void testPerform_startPhase() throws Exception {
        RegistrationPhaseHandler handler = new RegistrationPhaseHandler(RegistrationPhaseHandler.DEFAULT_NAMESPACE);

        try {
            cleanTables();
            setupProjectResourcesNotification("Registration");

            // test with scheduled status.
            Phase registration = createPhase(101, 1, "Scheduled", 1, "Registration");

            String operator = "1001";

            handler.perform(registration, operator);

            // manually check the email
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Tests the perform with open status and with registration. The post-mortem phase should not be inserted in
     * such a
     * situation.
     * @throws Exception to JUnit.
     * @since 1.1
     */
    public void testPerformWithRegistrants() throws Exception {
        RegistrationPhaseHandler handler = new RegistrationPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            cleanTables();

            // test with scheduled status.
            Project project = super.setupPhases();
            Phase[] phases = project.getAllPhases();
            Phase registration = phases[0];

            Connection conn = getConnection();

            // test with open status
            registration.setPhaseStatus(PhaseStatus.OPEN);

            // create a registration
            Resource resource = super.createResource(1, 101L, 1, 1);
            super.insertResources(conn, new Resource[] {resource });
            insertResourceInfo(conn, resource.getId(), 1, "1001");
            handler.perform(registration, "1001");

            // Post-mortem phase should not be inserted
            // when there is registration
            assertFalse("Post-mortem phase should NOT be inserted", havePostMortemPhase(conn));
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Tests the perform with open status and with registration. The post-mortem phase should not be inserted in
     * such a
     * situation. Also, checking the email.
     * @throws Exception to JUnit.
     * @since 1.2
     */
    public void testPerformWithRegistrants_stopHandler()
        throws Exception {
        RegistrationPhaseHandler handler = new RegistrationPhaseHandler(RegistrationPhaseHandler.DEFAULT_NAMESPACE);

        try {
            cleanTables();
            setupProjectResourcesNotification("Registration");

            // test with scheduled status.
            Phase registration = createPhase(1, 1, "Scheduled", 1, "Registration");
            Connection conn = getConnection();

            // test with open status
            registration.setPhaseStatus(PhaseStatus.OPEN);

            // create a registration
            Resource resource = createResource(4, 101L, 1, 1);
            super.insertResources(conn, new Resource[] {resource });
            insertResourceInfo(conn, resource.getId(), 1, "4");
            insertResourceInfo(conn, resource.getId(), 2, "ACRush");
            insertResourceInfo(conn, resource.getId(), 4, "3808");
            insertResourceInfo(conn, resource.getId(), 5, "100");

            // another register
            resource = createResource(5, 101L, 1, 1);
            super.insertResources(conn, new Resource[] {resource });
            insertResourceInfo(conn, resource.getId(), 1, "5");
            insertResourceInfo(conn, resource.getId(), 2, "UdH-WiNGeR");
            insertResourceInfo(conn, resource.getId(), 4, "3338");
            insertResourceInfo(conn, resource.getId(), 5, "90");

            handler.perform(registration, "1001");

            // manually check the email
            // Post-mortem phase should not be inserted
            // when there is registration
            assertFalse("Post-mortem phase should NOT be inserted", havePostMortemPhase(conn));
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Tests the perform method. No registrants.
     * @throws Exception to Junit.
     * @since 1.2
     */
    public void testPerformWithNoRegistrants_stopHandler()
        throws Exception {
        RegistrationPhaseHandler handler = new RegistrationPhaseHandler();

        try {
            cleanTables();
            Project project = setupProjectResourcesNotification("Registration");
            insertProjectInfo(getConnection(), project.getId(), new long[] {44 }, new String[] {"true" });
            // test with scheduled status.
            Phase registration = project.getAllPhases()[0];

            // test with open status
            registration.setPhaseStatus(PhaseStatus.OPEN);

            String operator = "1001";

            // test with open status
            registration.setPhaseStatus(PhaseStatus.OPEN);
            handler.perform(registration, operator);

            // check whether the Post-Mortem phase is inserted when
            // there is no submission
            Connection conn = getConnection();
            assertTrue("Post-mortem phase should be inserted", havePostMortemPhase(conn));
        } finally {
            cleanTables();
            closeConnection();
        }
    }
}
