/*
 * Copyright (C) 2006-2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.phase;

import java.util.Date;
import java.util.Iterator;

import com.topcoder.date.workdays.DefaultWorkdaysFactory;
import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.PhaseStatus;
import com.topcoder.project.phases.PhaseType;
import com.topcoder.project.phases.Project;
import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.idgenerator.IDGenerationException;
import com.topcoder.util.idgenerator.IDGenerator;

/**
 * Test suite for DefaultPhaseManager.
 * @author RachaelLCook, sokol
 * @version 1.1
 */
public class DefaultPhaseManagerTests extends PhaseManagementTestCase {

    /**
     * <p>
     * Represents handlers count constant value.
     * </p>
     * @since 1.1
     */
    private static final int HANDLERS_COUNT = 2;

    /**
     * A phase validator that does nothing.
     */
    private final PhaseValidator simpleValidator = new PhaseValidator() {

        public void validate(Phase phase) {
        }
    };

    /**
     * A phase handler that does nothing.
     */
    private final PhaseHandler handler1 = new NullPhaseHandler();

    /**
     * A phase handler that does nothing.
     */
    private final PhaseHandler handler2 = new NullPhaseHandler();

    /**
     * A phase handler that does nothing.
     */
    private final PhaseHandler handler3 = new NullPhaseHandler();

    /**
     * A phase handler that does nothing.
     */
    private final PhaseHandler handler4 = new NullPhaseHandler();

    /**
     * Pre-test configuration. This method loads the test configuration file into the ConfigManager.
     * @throws Exception if an error occurs while setting up
     */
    protected void setUp() throws Exception {
        ConfigManager manager = ConfigManager.getInstance();
        manager.add("config.xml");
    }

    /**
     * Post-test cleanup. This method removes all namespaces from the ConfigManager.
     * @throws Exception if an error occurs while cleaning up
     */
    protected void tearDown() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();
        Iterator < String > it = cm.getAllNamespaces();
        while (it.hasNext()) {
            cm.removeNamespace(it.next());
        }
    }

    /**
     * Tests invalid arguments to setPhaseValidator.
     * @throws Exception if an unexpected exception occurs
     */
    public void testSetPhaseValidator() throws Exception {
        DefaultPhaseManager manager = new DefaultPhaseManager("test.default");
        try {
            manager.setPhaseValidator(null);
            fail("should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // pass
        }
        // the successful case is tested as part of testGetPhaseValidator
    }

    /**
     * Tests that setPhaseValidator sets the validator correctly.
     * @throws Exception if an unexpected exception occurs
     */
    public void testGetPhaseValidator() throws Exception {
        DefaultPhaseManager manager = new DefaultPhaseManager("test.default");
        manager.setPhaseValidator(simpleValidator);
        assertEquals("phase validator should equal the set value", manager.getPhaseValidator(), simpleValidator);
    }

    /**
     * Tests the invalid arguments to registerHandler.
     * @throws Exception if an unexpected exception occurs
     */
    public void testRegisterHandlerExceptions() throws Exception {
        DefaultPhaseManager manager = new DefaultPhaseManager("test.default");
        try {
            manager.registerHandler(handler1, PHASE_TYPE_ONE, null);
            fail("registerHandler should check for null args");
        } catch (IllegalArgumentException ex) {
            // pass
        }
        try {
            manager.registerHandler(handler1, null, PhaseOperationEnum.START);
            fail("registerHandler should check for null args");
        } catch (IllegalArgumentException ex) {
            // pass
        }
        try {
            manager.registerHandler(null, PHASE_TYPE_ONE, PhaseOperationEnum.START);
            fail("registerHandler should check for null args");
        } catch (IllegalArgumentException ex) {
            // pass
        }
    }

    /**
     * Tests successful operation of registerHandler.
     * @throws Exception if an unexpected exception occurs
     */
    public void testRegisterHandler() throws Exception {
        DefaultPhaseManager manager = new DefaultPhaseManager("test.default");
        manager.unregisterHandler(new PhaseType(1, "type1"), PhaseOperationEnum.START);
        // register several handlers
        manager.registerHandler(handler1, PHASE_TYPE_ONE, PhaseOperationEnum.START);
        manager.registerHandler(handler2, PHASE_TYPE_ONE, PhaseOperationEnum.END);
        manager.registerHandler(handler3, PHASE_TYPE_TWO, PhaseOperationEnum.START);
        // make sure each handler is associated with the expected registry info
        HandlerRegistryInfo[] hri1 = manager.getHandlerRegistrationInfo(handler1);
        assertEquals("registerHandler failed for handler1", hri1.length, 1);
        assertEquals("registerHandler failed for handler1", hri1[0].getType(), PHASE_TYPE_ONE);
        assertEquals("registerHandler failed for handler1", hri1[0].getOperation(), PhaseOperationEnum.START);
        // make sure each handler is associated with the expected registry info
        HandlerRegistryInfo[] hri2 = manager.getHandlerRegistrationInfo(handler2);
        assertEquals("registerHandler failed for handler2", hri2.length, 1);
        assertEquals("registerHandler failed for handler2", hri2[0].getType(), PHASE_TYPE_ONE);
        assertEquals("registerHandler failed for handler2", hri2[0].getOperation(), PhaseOperationEnum.END);
        // make sure each handler is associated with the expected registry info
        HandlerRegistryInfo[] hri3 = manager.getHandlerRegistrationInfo(handler3);
        assertEquals("registerHandler failed for handler3", hri3.length, 1);
        assertEquals("registerHandler failed for handler3", hri3[0].getType(), PHASE_TYPE_TWO);
        assertEquals("registerHandler failed for handler3", hri3[0].getOperation(), PhaseOperationEnum.START);
        // test that calling with the same args replaces the previous handler
        manager.registerHandler(handler4, PHASE_TYPE_ONE, PhaseOperationEnum.START);
        HandlerRegistryInfo[] hri4 = manager.getHandlerRegistrationInfo(handler4);
        assertEquals("registerHandler failed for handler4", hri4.length, 1);
        assertEquals("registerHandler failed for handler4", hri4[0].getType(), PHASE_TYPE_ONE);
        assertEquals("registerHandler failed for handler4", hri4[0].getOperation(), PhaseOperationEnum.START);
        assertEquals("registerHandler didn't replace previous handler",
                manager.getHandlerRegistrationInfo(handler1).length, 0);
        // test a handler registered for multiple phase/operator combos
        manager.registerHandler(handler4, PHASE_TYPE_ONE, PhaseOperationEnum.END);
        HandlerRegistryInfo[] hri5 = manager.getHandlerRegistrationInfo(handler4);
        assertEquals("registerHandler failed for handler4", hri5.length, 2);
    }

    /**
     * Tests getHandlerRegistrationInfo for a handler that isn't registered.
     * @throws Exception if an unexpected exception occurs
     */
    public void testGetHandlerRegistrationInfo() throws Exception {
        // this was mostly tested in testRegisterHandler, so here we just check for the null case
        DefaultPhaseManager manager = new DefaultPhaseManager("test.default");
        manager.registerHandler(handler1, PHASE_TYPE_ONE, PhaseOperationEnum.START);
        assertEquals("getHandlerRegistrationInfo should return null",
                manager.getHandlerRegistrationInfo(handler2).length, 0);
    }

    /**
     * Tests that getAllHandlers returns an empty array when there are no handlers.
     * @throws Exception if an unexpected exception occurs
     */
    public void testGetAllHandlersEmpty() throws Exception {
        DefaultPhaseManager manager = new DefaultPhaseManager("test.default");
        PhaseHandler[] handlers = manager.getAllHandlers();
        assertNotNull("getAllHandlers should not return null", handlers);
        assertEquals("getAllHandlers should return empty array", 1, handlers.length);
    }

    /**
     * Tests that getAllHandlers returns the correct values.
     * @throws Exception if an unexpected exception occurs
     */
    public void testGetAllHandlers() throws Exception {
        DefaultPhaseManager manager = new DefaultPhaseManager("test.default");
        manager.registerHandler(handler1, PHASE_TYPE_ONE, PhaseOperationEnum.START);
        manager.registerHandler(handler2, PHASE_TYPE_ONE, PhaseOperationEnum.END);
        PhaseHandler[] handlers = manager.getAllHandlers();
        assertEquals("getAllHandlers should return 2 handlers", HANDLERS_COUNT, handlers.length);
        // the handlers could be returned in any order
        boolean found = false;
        for (int i = 0; (i < HANDLERS_COUNT) && !found; ++i) {
            if (handlers[i] == handler1) {
                found = true;
            }
        }
        if (!found) {
            fail("getAllHandlers did not return handler1");
        }
        found = false;
        for (int i = 0; (i < HANDLERS_COUNT) && !found; ++i) {
            if (handlers[i] == handler2) {
                found = true;
            }
        }
        if (!found) {
            fail("getAllHandlers did not return handler2");
        }
    }

    /**
     * Tests invalid arguments to unregisterHandler.
     * @throws Exception if an unexpected exception occurs
     */
    public void testUnregisterHandlerExceptions() throws Exception {
        DefaultPhaseManager manager = new DefaultPhaseManager("test.default");
        try {
            manager.unregisterHandler(PHASE_TYPE_ONE, null);
            fail("unregisterHandler should check for null arg");
        } catch (IllegalArgumentException ex) {
            // pass
        }
        try {
            manager.unregisterHandler(null, PhaseOperationEnum.START);
            fail("unregisterHandler should check for null arg");
        } catch (IllegalArgumentException ex) {
            // pass
        }
    }

    /**
     * Tests normal operation of unregisterHandler.
     * @throws Exception if an unexpected exception occurs
     */
    public void testUnregisterHandler() throws Exception {
        DefaultPhaseManager manager = new DefaultPhaseManager("test.default");
        manager.registerHandler(handler1, PHASE_TYPE_ONE, PhaseOperationEnum.START);
        manager.registerHandler(handler2, PHASE_TYPE_ONE, PhaseOperationEnum.END);
        assertEquals("unregisterHandler failed for handler1", handler1,
                manager.unregisterHandler(PHASE_TYPE_ONE, PhaseOperationEnum.START));
        assertNull("unregisterHandler should return null for handler that's already remove",
                manager.unregisterHandler(PHASE_TYPE_ONE, PhaseOperationEnum.START));
    }

    /**
     * Tests invalid arguments to canEnd.
     * @throws Exception if an unexpected exception occurs
     */
    public void testCanEndException() throws Exception {
        DefaultPhaseManager manager = new DefaultPhaseManager("test.default");
        try {
            manager.canEnd(null);
            fail("should throw IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // pass
        }
    }

    /**
     * Tests normal operation of canEnd.
     * @throws Exception if an unexpected exception occurs
     */
    public void testCanEndNoHandler() throws Exception {
        DefaultPhaseManager manager = new DefaultPhaseManager("test.default");
        assertEquals("canEnd should return success for PHASE_ONE", OperationCheckResult.SUCCESS,
                manager.canEnd(PHASE_ONE));
        assertFalse("canEnd should return not success for PHASE_TWO", manager.canEnd(PHASE_TWO).isSuccess());
    }

    /**
     * Tests that canEnd calls the canPerform method of the phase handler.
     * @throws Exception if an unexpected exception occurs
     */
    public void testCanEndHandler() throws Exception {
        DefaultPhaseManager manager = new DefaultPhaseManager("test.default");
        manager.registerHandler(NEVER_PERFORM, PHASE_TYPE_ONE, PhaseOperationEnum.END);
        manager.registerHandler(ALWAYS_PERFORM, PHASE_TYPE_TWO, PhaseOperationEnum.END);
        assertFalse("canEnd should return not success for PHASE_ONE", manager.canEnd(PHASE_ONE).isSuccess());
        assertEquals("canEnd should return success for PHASE_TWO", OperationCheckResult.SUCCESS,
                manager.canEnd(PHASE_TWO));
    }

    /**
     * Tests invalid arguments to canStart.
     * @throws Exception if an unexpected exception occurs
     */
    public void testCanStartException() throws Exception {
        DefaultPhaseManager manager = new DefaultPhaseManager("test.default");
        try {
            manager.canStart(null);
            fail("should throw IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // pass
        }
    }

    /**
     * Tests normal operation of canStart.
     * @throws Exception if an unexpected exception occurs
     */
    public void testCanStartNoHandler() throws Exception {
        DefaultPhaseManager manager = new DefaultPhaseManager("test.default");
        assertTrue("canStart should return success for PHASE_ONE",
                manager.canStart(PHASE_ONE) == OperationCheckResult.SUCCESS);
        assertFalse("canStart should return not success for PHASE_TWO", manager.canStart(PHASE_TWO).isSuccess());
    }

    /**
     * Tests that canStart calls the canPerform method of the phase handler.
     * @throws Exception if an unexpected exception occurs
     */
    public void testCanStartHandler() throws Exception {
        DefaultPhaseManager manager = new DefaultPhaseManager("test.default");
        manager.registerHandler(NEVER_PERFORM, PHASE_TYPE_ONE, PhaseOperationEnum.START);
        manager.registerHandler(ALWAYS_PERFORM, PHASE_TYPE_TWO, PhaseOperationEnum.START);
        assertFalse("canStart should return not success for PHASE_ONE", manager.canStart(PHASE_ONE).isSuccess());
        assertTrue("canStart should return success for PHASE_TWO",
                manager.canStart(PHASE_TWO) == OperationCheckResult.SUCCESS);
    }

    /**
     * Tests invalid arguments to start.
     * @throws Exception if an unexpected exception occurs
     */
    public void testStartExceptions() throws Exception {
        DefaultPhaseManager manager = new DefaultPhaseManager("test.default");
        try {
            manager.start(PHASE_ONE, null);
            fail("should throw IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // pass
        }
        try {
            manager.start(null, "hi");
            fail("should throw IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // pass
        }
        try {
            manager.start(PHASE_ONE, "");
            fail("should throw IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // pass
        }
        try {
            manager.start(PHASE_ONE, "yo");
            fail("should throw PhaseManagementException");
        } catch (PhaseManagementException ex) {
            // pass
        }
    }

    /**
     * Tests that start makes the correct calls to persistence.
     * @throws Exception if an unexpected exception occurs
     */
    public void testStartPersistence() throws Exception {
        final Phase phase = new Phase(PROJECT, 1);
        phase.setPhaseType(PHASE_TYPE_ONE);
        final String operator = "wow";
        final NullPhasePersistence persistence = new NullPhasePersistence() {

            public void updatePhases(Phase[] phases, String theOperator) throws PhasePersistenceException {
                if (phases.length != 3 || !theOperator.equals(operator)) {
                    throw new RuntimeException("unexpected arguments to updatePhases");
                }
                setPassed(true);
            }
        };
        DefaultPhaseManager manager = new DefaultPhaseManager(persistence, new NullIdGenerator());
        manager.start(phase, operator);
        assertTrue("should have called PhasePersistence#updatePhase", persistence.getPassed());
    }

    /**
     * Tests that start sets the actual start date.
     * @throws Exception if an unexpected exception occurs
     */
    public void testStartSetStartDate() throws Exception {
        final NullPhasePersistence persistence = new NullPhasePersistence() {

            public void updatePhases(Phase[] phases, String theOperator) throws PhasePersistenceException {
                // do nothing
            }
        };
        DefaultPhaseManager manager = new DefaultPhaseManager(persistence, new NullIdGenerator());
        Phase phase = new Phase(PROJECT, 1);
        phase.setPhaseType(PHASE_TYPE_ONE);
        manager.start(phase, "yay");
        assertNotNull("start should have set start date", phase.getActualStartDate());
    }

    /**
     * Tests that start sets the phase status to OPEN.
     * @throws Exception if an unexpected exception occurs
     */
    public void testStartSetStatus() throws Exception {
        final NullPhasePersistence persistence = new NullPhasePersistence() {

            public void updatePhases(Phase[] phases, String theOperator) throws PhasePersistenceException {
                // do nothing
            }
        };
        DefaultPhaseManager manager = new DefaultPhaseManager(persistence, new NullIdGenerator());
        Phase phase = new Phase(PROJECT, 1);
        phase.setPhaseType(PHASE_TYPE_ONE);
        manager.start(phase, "yo");
        assertEquals("start should have set phase status to open", phase.getPhaseStatus().getId(),
                PhaseStatus.OPEN.getId());
    }

    /**
     * Tests that start invokes the perform method of the phase handler.
     * @throws Exception if an unexpected exception occurs
     */
    public void testStartHandler() throws Exception {
        final NullPhasePersistence persistence = new NullPhasePersistence() {

            public void updatePhases(Phase[] phases, String theOperator) throws PhasePersistenceException {
                // do nothing
            }
        };
        DefaultPhaseManager manager = new DefaultPhaseManager(persistence, new NullIdGenerator());
        final Phase phase = new Phase(PROJECT, 1);
        phase.setPhaseType(PHASE_TYPE_ONE);
        final String operator = "hello";
        final NullPhaseHandler handler = new NullPhaseHandler() {

            public void perform(Phase thePhase, String theOperator) {
                if (thePhase != phase || !theOperator.equals(operator)) {
                    throw new RuntimeException("perform called with unexpected arguments");
                }
                setPassed(true);
            }
        };
        manager.registerHandler(handler, PHASE_TYPE_ONE, PhaseOperationEnum.START);
        manager.start(phase, operator);
        assertTrue("should have called handler.perform", handler.getPassed());
    }

    /**
     * Tests invalid arguments to end.
     * @throws Exception if an unexpected exception occurs
     */
    public void testEndExceptions() throws Exception {
        DefaultPhaseManager manager = new DefaultPhaseManager("test.default");
        try {
            manager.end(PHASE_ONE, null);
            fail("should throw IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // pass
        }
        try {
            manager.end(null, "hi");
            fail("should throw IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // pass
        }
        try {
            manager.end(PHASE_ONE, "");
            fail("should throw IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // pass
        }
        try {
            manager.end(PHASE_ONE, "yo");
            fail("should throw PhaseManagementException");
        } catch (PhaseManagementException ex) {
            // pass
        }
    }

    /**
     * Tests that end invokes the appropriate persistence methods.
     * @throws Exception if an unexpected exception occurs
     */
    public void testEndPersistence() throws Exception {
        final Phase phase = new Phase(PROJECT, 1);
        phase.setPhaseType(PHASE_TYPE_ONE);
        final String operator = "wow";
        final NullPhasePersistence persistence = new NullPhasePersistence() {

            public void updatePhases(Phase[] phases, String theOperator) throws PhasePersistenceException {
                if (!theOperator.equals(operator)) {
                    throw new RuntimeException("unexpected arguments to updatePhases");
                }
                setPassed(true);
            }
        };
        DefaultPhaseManager manager = new DefaultPhaseManager(persistence, new NullIdGenerator());
        manager.end(phase, operator);
        assertTrue("should have called PhasePersistence#updatePhase", persistence.getPassed());
    }

    /**
     * Tests that end sets the actual end date.
     * @throws Exception if an unexpected exception occurs
     */
    public void testEndSetEndDate() throws Exception {
        final NullPhasePersistence persistence = new NullPhasePersistence() {

            public void updatePhases(Phase[] phases, String theOperator) throws PhasePersistenceException {
                // do nothing
            }
        };
        DefaultPhaseManager manager = new DefaultPhaseManager(persistence, new NullIdGenerator());
        Phase phase = new Phase(PROJECT, 1);
        phase.setPhaseType(PHASE_TYPE_ONE);
        manager.end(phase, "yay");
        assertNotNull("end should have set end date", phase.getActualEndDate());
    }

    /**
     * Tests that end sets the phase status to CLOSE.
     * @throws Exception if an unexpected exception occurs
     */
    public void testEndSetStatus() throws Exception {
        final NullPhasePersistence persistence = new NullPhasePersistence() {

            public void updatePhases(Phase[] phases, String theOperator) throws PhasePersistenceException {
                // do nothing
            }
        };
        DefaultPhaseManager manager = new DefaultPhaseManager(persistence, new NullIdGenerator());
        Phase phase = new Phase(PROJECT, 1);
        PhaseStatus open = new PhaseStatus(PhaseStatusEnum.OPEN.getId(), PhaseStatusEnum.OPEN.getName());
        phase.setPhaseStatus(open);
        phase.setPhaseType(PHASE_TYPE_ONE);
        manager.end(phase, "yo");
        assertEquals("end should have set phase status to close", phase.getPhaseStatus().getId(),
                PhaseStatus.CLOSED.getId());
        assertTrue("the phase status should be set to a new object", open != phase.getPhaseStatus());
    }

    /**
     * Tests that end invokes the perform method of the phase handler.
     * @throws Exception if an unexpected exception occurs
     */
    public void testEndHandler() throws Exception {
        final NullPhasePersistence persistence = new NullPhasePersistence() {

            public void updatePhases(Phase[] phases, String theOperator) throws PhasePersistenceException {
                // do nothing
            }
        };
        DefaultPhaseManager manager = new DefaultPhaseManager(persistence, new NullIdGenerator());
        final Phase phase = new Phase(PROJECT, 1);
        phase.setPhaseType(PHASE_TYPE_ONE);
        final String operator = "hello";
        final NullPhaseHandler handler = new NullPhaseHandler() {

            public void perform(Phase thePhase, String theOperator) {
                if (thePhase != phase || !theOperator.equals(operator)) {
                    throw new RuntimeException("perform called with unexpected arguments");
                }
                setPassed(true);
            }
        };
        manager.registerHandler(handler, PHASE_TYPE_ONE, PhaseOperationEnum.END);
        manager.end(phase, operator);
        assertTrue("should have called handler.perform", handler.getPassed());
    }

    /**
     * Tests that persistence exceptions are handled by getAllPhaseStatuses.
     * @throws Exception if an unexpected exception occurs
     */
    public void testGetAllPhaseStatusesException() throws Exception {
        DefaultPhaseManager manager = new DefaultPhaseManager("test.default");
        try {
            manager.getAllPhaseStatuses();
            fail("should have thrown PhaseManagementException");
        } catch (PhaseManagementException ex) {
            // pass
        }
    }

    /**
     * Tests normal operation of getAllPhaseStatuses.
     * @throws Exception if an unexpected exception occurs
     */
    public void testGetAllPhaseStatuses() throws Exception {
        final PhaseStatus[] statuses = new PhaseStatus[] {PhaseStatus.SCHEDULED, PhaseStatus.OPEN, PhaseStatus.CLOSED};
        final NullPhasePersistence persistence = new NullPhasePersistence() {

            public PhaseStatus[] getAllPhaseStatuses() {
                return statuses;
            }
        };
        DefaultPhaseManager manager = new DefaultPhaseManager(persistence, new NullIdGenerator());
        assertEquals("bad return value from getAllPhaseStatuses", statuses, manager.getAllPhaseStatuses());
    }

    /**
     * Tests that persistence exceptions are handled properly by getAllPhaseTypes.
     * @throws Exception if an unexpected exception occurs
     */
    public void testGetAllPhaseTypesException() throws Exception {
        DefaultPhaseManager manager = new DefaultPhaseManager("test.default");
        try {
            manager.getAllPhaseTypes();
            fail("should have thrown PhaseManagementException");
        } catch (PhaseManagementException ex) {
            // pass
        }
    }

    /**
     * Tests normal operation of getAllPhaseTypes.
     * @throws Exception if an unexpected exception occurs
     */
    public void testGetAllPhaseTypes() throws Exception {
        final PhaseType[] types = new PhaseType[] {PHASE_TYPE_ONE, PHASE_TYPE_TWO};
        final NullPhasePersistence persistence = new NullPhasePersistence() {

            public PhaseType[] getAllPhaseTypes() {
                return types;
            }
        };
        DefaultPhaseManager manager = new DefaultPhaseManager(persistence, new NullIdGenerator());
        assertEquals("bad return value from getAllPhaseTypes", types, manager.getAllPhaseTypes());
    }

    /**
     * Tests that persistence exceptions are handled properly by getPhases.
     * @throws Exception if an unexpected exception occurs
     */
    public void testGetPhasesExceptions() throws Exception {
        DefaultPhaseManager manager = new DefaultPhaseManager("test.default");
        try {
            manager.getPhases(1);
            fail("should have thrown PhaseManagementException");
        } catch (PhaseManagementException ex) {
            // pass
        }
    }

    /**
     * Tests normal operation of getPhases.
     * @throws Exception if an unexpected exception occurs
     */
    public void testGetPhases() throws Exception {
        final NullPhasePersistence persistence = new NullPhasePersistence() {

            public Project getProjectPhases(long id) {
                return PROJECT;
            }
        };
        DefaultPhaseManager manager = new DefaultPhaseManager(persistence, new NullIdGenerator());
        assertEquals("bad return value from getPhases", PROJECT, manager.getPhases(1));
    }

    /**
     * Tests invalid arguments to getPhases(long[]).
     * @throws Exception if an unexpected exception occurs
     */
    public void testGetPhases2Exceptions() throws Exception {
        DefaultPhaseManager manager = new DefaultPhaseManager("test.default");
        try {
            manager.getPhases(null);
            fail("should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // pass
        }
        try {
            manager.getPhases(new long[] {1, 2, 3});
            fail("should have thrown PhaseManagementException");
        } catch (PhaseManagementException ex) {
            // pass
        }
    }

    /**
     * Tests normal operation of getPhases(long[]).
     * @throws Exception if an unexpected exception occurs
     */
    public void testGetPhases2() throws Exception {
        final Project[] projects = new Project[] {PROJECT, PROJECT_TWO};
        final NullPhasePersistence persistence = new NullPhasePersistence() {

            public Project[] getProjectPhases(long[] ids) {
                return projects;
            }
        };
        DefaultPhaseManager manager = new DefaultPhaseManager(persistence, new NullIdGenerator());
        assertEquals("bad return value from getPhases", projects, manager.getPhases(new long[] {1, 2}));
    }

    /**
     * Tests invalid arguments to the constructor.
     */
    public void testCtorExceptions() {
        try {
            new DefaultPhaseManager(new NullPhasePersistence(), null);
            fail("should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // pass
        }
        try {
            new DefaultPhaseManager(null, new NullIdGenerator());
            fail("should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // pass
        }
    }

    /**
     * Tests invalid arguments to updatePhases.
     * @throws Exception if an unexpected exception occurs
     */
    public void testUpdatePhasesArguments() throws Exception {
        DefaultPhaseManager manager = new DefaultPhaseManager("test.default");
        try {
            manager.updatePhases(PROJECT, null);
            fail("should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // pass
        }
        try {
            manager.updatePhases(null, "yo");
            fail("should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // pass
        }
        try {
            manager.updatePhases(PROJECT, "");
            fail("should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // pass
        }
    }

    /**
     * Tests that updatePhases performs the appropriate persistence operations and sets the phase IDs.
     * @throws Exception if an unexpected exception occurs
     */
    public void testUpdatePhases() throws Exception {
        final Project project = new Project(new Date(), new DefaultWorkdaysFactory(false).createWorkdaysInstance());
        final Phase[] phases = new Phase[] {new Phase(project, 1), new Phase(project, 2)};
        final String operator = "op";
        for (int i = 0; i < phases.length; ++i) {
            phases[i].setPhaseStatus(PhaseStatus.OPEN);
            phases[i].setScheduledStartDate(new Date());
            phases[i].setScheduledEndDate(new Date());
            phases[i].setPhaseType(PHASE_TYPE_ONE);
            project.addPhase(phases[i]);
        }
        final NullPhasePersistence persistence = new NullPhasePersistence() {

            public boolean isNewPhase(Phase phase) {
                return true;
            }

            public Project getProjectPhases(long id) {
                return new Project(new Date(), new DefaultWorkdaysFactory(false).createWorkdaysInstance());
            }

            public void createPhases(Phase[] newPhases, String newOperator) {
                if (newPhases.length != 2 || newOperator != operator) {
                    throw new RuntimeException("invalid arguments to createPhases");
                }
                setPassed(true);
            }
        };
        final IDGenerator idgen = new NullIdGenerator() {

            public long getNextID() {
                return getNext();
            }
        };
        DefaultPhaseManager manager = new DefaultPhaseManager(persistence, idgen);
        manager.updatePhases(project, operator);
        assertTrue("should have called createPhases", persistence.getPassed());
        assertTrue("should have set phase IDs", phases[0].getId() >= 2 && phases[0].getId() <= 3);
        assertTrue("should have set phase IDs", phases[1].getId() >= 2 && phases[1].getId() <= 3);
    }

    /**
     * Tests invalid arguments to the namespace constructor.
     * @throws Exception if an unexpected exception occurs
     */
    public void testNamespaceCtorArguments() throws Exception {
        try {
            new DefaultPhaseManager(null);
            fail("should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // pass
        }
        try {
            new DefaultPhaseManager("");
        } catch (IllegalArgumentException ex) {
            // pass
        }
    }

    /**
     * Tests the namespace constructor when the namespace does not exist.
     */
    public void testNamespaceCtorBadNamespace() {
        try {
            new DefaultPhaseManager("does.not.exist");
            fail("should have thrown ConfigurationException");
        } catch (ConfigurationException ex) {
            // pass
        }
    }

    /**
     * Tests the namespace constructor when the persistence class is missing or cannot be instantiated.
     */
    public void testNamespaceCtorBadPersistence() {
        try {
            new DefaultPhaseManager("test.bad.persistence");
            fail("should have thrown ConfigurationException");
        } catch (ConfigurationException ex) {
            // pass
        }
        try {
            new DefaultPhaseManager("test.invalid.persistence");
            fail("should have thrown ConfigurationException");
        } catch (ConfigurationException ex) {
            // pass
        }
    }

    /**
     * Tests the namespace constructor when the validator class is missing or cannot be instantiated.
     */
    public void testNamespaceCtorBadValidator() {
        try {
            new DefaultPhaseManager("test.bad.validator");
            fail("should have thrown ConfigurationException");
        } catch (ConfigurationException ex) {
            // pass
        }
        try {
            new DefaultPhaseManager("test.invalid.validator");
            fail("should have thrown ConfigurationException");
        } catch (ConfigurationException ex) {
            // pass
        }
    }

    /**
     * Tests that the namespace constructor sets the phase validator appropriately.
     * @throws Exception if an unexpected exception occurs
     */
    public void testNamespaceCtorValidator() throws Exception {
        assertEquals("validator class should be TestValidator", new DefaultPhaseManager("test.validator")
                .getPhaseValidator().getClass().getName(), "com.topcoder.management.phase.TestValidator");
    }

    /**
     * Tests the namespace constructor when the ID generator class is missing or cannot be instantiated.
     */
    public void testNamespaceCtorBadIdGenerator() {
        try {
            new DefaultPhaseManager("test.bad.generator");
            fail("should have thrown ConfigurationException");
        } catch (ConfigurationException ex) {
            // pass
        }
        try {
            new DefaultPhaseManager("test.invalid.generator");
            fail("should have thrown ConfigurationException");
        } catch (ConfigurationException ex) {
            // pass
        }
    }

    /**
     * <p>
     * Tests {@link DefaultPhaseManager#getHandlerRegistrationInfo(PhaseHandler)} with null argument passed.
     * </p>
     * <p>
     * IllegalArgumentException exception is expected.
     * </p>
     * @throws ConfigurationException if any error occurs
     * @since 1.1
     */
    public void testGetHandlerRegistrationInfo_NullHandler() throws ConfigurationException {
        DefaultPhaseManager manager = new DefaultPhaseManager("test.default");
        try {
            manager.getHandlerRegistrationInfo(null);
            fail("IllegalArgumentException exception is expected.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * <p>
     * Tests DefaultPhaseManager constructor. Configuration doesn't have persistence class name value.
     * </p>
     * <p>
     * ConfigurationException exception is expected.
     * </p>
     * @throws ConfigurationException if any error occurs
     * @since 1.1
     */
    public void testConstructor_PersistenceClassParameter() throws ConfigurationException {
        try {
            new DefaultPhaseManager("test.bad.persistence.null_classname");
            fail("ConfigurationException exception is expected.");
        } catch (ConfigurationException e) {
            // expected
        }
    }

    /**
     * <p>
     * Tests DefaultPhaseManager constructor. Configuration has empty ObjectFactoryNamespace property.
     * </p>
     * <p>
     * ConfigurationException exception is expected.
     * </p>
     * @throws ConfigurationException if any error occurs
     * @since 1.1
     */
    public void testConstructor_EmptyObjectFactoryNamespace() throws ConfigurationException {
        try {
            new DefaultPhaseManager("test.bad.object_factory_namespace.empty");
            fail("ConfigurationException exception is expected.");
        } catch (ConfigurationException e) {
            // expected
        }
    }

    /**
     * <p>
     * Tests DefaultPhaseManager constructor. Configuration doesn't have ObjectFactoryNamespace property.
     * </p>
     * <p>
     * ConfigurationException exception is expected.
     * </p>
     * @throws ConfigurationException if any error occurs
     * @since 1.1
     */
    public void testConstructor_NullObjectFactoryNamespace() throws ConfigurationException {
        try {
            new DefaultPhaseManager("test.bad.object_factory_namespace.null");
            fail("ConfigurationException exception is expected.");
        } catch (ConfigurationException e) {
            // expected
        }
    }

    /**
     * <p>
     * Tests DefaultPhaseManager constructor. Configuration contains unknown phase operation.
     * </p>
     * <p>
     * ConfigurationException exception is expected.
     * </p>
     * @throws ConfigurationException if any error occurs
     * @since 1.1
     */
    public void testConstructor_UnknownPhaseOperation() throws ConfigurationException {
        try {
            new DefaultPhaseManager("test.bad.operation");
            fail("ConfigurationException exception is expected.");
        } catch (ConfigurationException e) {
            // expected
        }
    }

    /**
     * <p>
     * Tests DefaultPhaseManager constructor. Configuration doesn't contain phase operation.
     * </p>
     * <p>
     * ConfigurationException exception is expected.
     * </p>
     * @throws ConfigurationException if any error occurs
     * @since 1.1
     */
    public void testConstructor_MissingPhaseOperation() throws ConfigurationException {
        try {
            new DefaultPhaseManager("test.bad.operation.missing");
            fail("ConfigurationException exception is expected.");
        } catch (ConfigurationException e) {
            // expected
        }
    }

    /**
     * <p>
     * Tests DefaultPhaseManager constructor. Configuration doesn't contain id generator sequence name.
     * </p>
     * <p>
     * ConfigurationException exception is expected.
     * </p>
     * @throws ConfigurationException if any error occurs
     * @since 1.1
     */
    public void testConstructor_MissingIdGeneratorSequenceName() throws ConfigurationException {
        try {
            new DefaultPhaseManager("test.bad.idgenerator.idSequence_null");
            fail("ConfigurationException exception is expected.");
        } catch (ConfigurationException e) {
            // expected
        }
    }

    /**
     * <p>
     * Tests DefaultPhaseManager constructor. Configuration doesn't contain id generator class name.
     * </p>
     * <p>
     * ConfigurationException exception is expected.
     * </p>
     * @throws ConfigurationException if any error occurs
     * @since 1.1
     */
    public void testConstructor_MissingIdGeneratorClassName() throws ConfigurationException {
        try {
            new DefaultPhaseManager("test.bad.idgenerator.idClass_null");
            fail("ConfigurationException exception is expected.");
        } catch (ConfigurationException e) {
            // expected
        }
    }

    /**
     * <p>
     * Tests DefaultPhaseManager constructor. Validator class is abstract.
     * </p>
     * <p>
     * ConfigurationException exception is expected.
     * </p>
     * @throws ConfigurationException if any error occurs
     * @since 1.1
     */
    public void testConstructor_ValidatorAbstract() throws ConfigurationException {
        try {
            new DefaultPhaseManager("test.bad.validator.ctor.abstract");
            fail("ConfigurationException exception is expected.");
        } catch (ConfigurationException e) {
            // expected
        }
    }

    /**
     * <p>
     * Tests DefaultPhaseManager constructor. Validator class has private constructor.
     * </p>
     * <p>
     * ConfigurationException exception is expected.
     * </p>
     * @throws ConfigurationException if any error occurs
     * @since 1.1
     */
    public void testConstructor_ValidatorPrivateCtor() throws ConfigurationException {
        try {
            new DefaultPhaseManager("test.bad.validator.ctor.private");
            fail("ConfigurationException exception is expected.");
        } catch (ConfigurationException e) {
            // expected
        }
    }

    /**
     * <p>
     * Tests DefaultPhaseManager constructor. Validator class has constructor that throws exception.
     * </p>
     * <p>
     * ConfigurationException exception is expected.
     * </p>
     * @throws ConfigurationException if any error occurs
     * @since 1.1
     */
    public void testConstructor_ValidatorCtorException() throws ConfigurationException {
        try {
            new DefaultPhaseManager("test.bad.validator.ctor.exception");
            fail("ConfigurationException exception is expected.");
        } catch (ConfigurationException e) {
            // expected
        }
    }

    /**
     * <p>
     * Tests DefaultPhaseManager constructor. Validator class doesn't have default constructor.
     * </p>
     * <p>
     * ConfigurationException exception is expected.
     * </p>
     * @throws ConfigurationException if any error occurs
     * @since 1.1
     */
    public void testConstructor_ValidatorNoDefaultCtor() throws ConfigurationException {
        try {
            new DefaultPhaseManager("test.bad.validator.ctor.no_method");
            fail("ConfigurationException exception is expected.");
        } catch (ConfigurationException e) {
            // expected
        }
    }

    /**
     * <p>
     * Tests {@link DefaultPhaseManager#updatePhases(Project, String)} with valid arguments passed, but underlying
     * validator throws exception.
     * </p>
     * <p>
     * PhaseManagementException exception is expected.
     * </p>
     * @throws Exception if any error occurs
     * @since 1.1
     */
    public void testUpdatePhases_BadValidator() throws Exception {
        try {
            new DefaultPhaseManager("test.bad.validator.throw").updatePhases(PROJECT, "someOperator");
            fail("PhaseManagementException exception is expected.");
        } catch (PhaseManagementException e) {
            // expected
        }
    }

    /**
     * <p>
     * Tests that updatePhases performs the appropriate persistence operations and sets the phase IDs.
     * </p>
     * <p>
     * Phase should be deleted.
     * </p>
     * @throws Exception if an unexpected exception occurs
     * @since 1.1
     */
    public void testUpdatePhases_Delete() throws Exception {
        final Project project = new Project(new Date(), new DefaultWorkdaysFactory(false).createWorkdaysInstance());
        final Phase[] phases = new Phase[] {new Phase(project, 1)};
        final String operator = "op";
        for (int i = 0; i < phases.length; ++i) {
            phases[i].setPhaseStatus(PhaseStatus.CLOSED);
            phases[i].setScheduledStartDate(new Date());
            phases[i].setScheduledEndDate(new Date());
            phases[i].setPhaseType(PHASE_TYPE_ONE);
            project.addPhase(phases[i]);
        }
        final NullPhasePersistence persistence = new NullPhasePersistence() {

            public boolean isNewPhase(Phase phase) {
                return true;
            }

            public Project getProjectPhases(long projectId) throws PhasePersistenceException {
                return PROJECT;
            }

            public void deletePhases(Phase[] phases) throws PhasePersistenceException {
                setPassed(true);
            }

            public void createPhases(Phase[] phases, String operator) throws PhasePersistenceException {
                // do nothing
            }

            public void updatePhases(Phase[] phases, String operator) throws PhasePersistenceException {
                // do nothing
            }
        };
        final IDGenerator idgen = new NullIdGenerator() {

            public long getNextID() {
                return getNext();
            }
        };
        DefaultPhaseManager manager = new DefaultPhaseManager(persistence, idgen);
        manager.updatePhases(project, operator);
        assertTrue("should have called deletePhase", persistence.getPassed());
    }

    /**
     * <p>
     * Tests {@link DefaultPhaseManager#updatePhases(Project, String)} with valid arguments passed, but underlying id
     * generator throws exception.
     * </p>
     * <p>
     * PhaseManagementException exception is expected.
     * </p>
     * @throws Exception if an unexpected exception occurs
     * @since 1.1
     */
    public void testUpdatePhases_IdGeneratorException() throws Exception {
        Project project = new Project(new Date(), new DefaultWorkdaysFactory(false).createWorkdaysInstance());
        final Phase[] phases = new Phase[] {new Phase(project, 1)};
        for (int i = 0; i < phases.length; ++i) {
            phases[i].setPhaseStatus(PhaseStatus.CLOSED);
            phases[i].setScheduledStartDate(new Date());
            phases[i].setScheduledEndDate(new Date());
            phases[i].setPhaseType(PHASE_TYPE_ONE);
            project.addPhase(phases[i]);
        }
        final NullPhasePersistence persistence = new NullPhasePersistence() {

            public boolean isNewPhase(Phase phase) {
                return true;
            }
        };
        final IDGenerator idgen = new NullIdGenerator() {

            public long getNextID() throws IDGenerationException {
                throw new IDGenerationException("just for testing.");
            }
        };
        DefaultPhaseManager manager = new DefaultPhaseManager(persistence, idgen);
        try {
            manager.updatePhases(project, "op");
            fail("PhaseManagementException exception is expected.");
        } catch (PhaseManagementException e) {
            // expected
        }
    }

    /**
     * <p>
     * Tests {@link DefaultPhaseManager#updatePhases(Project, String)} with valid arguments passed, but underlying
     * phase persistence throws exception.
     * </p>
     * <p>
     * PhaseManagementException exception is expected.
     * </p>
     * @throws Exception if an unexpected exception occurs
     * @since 1.1
     */
    public void testUpdatePhases_PhasePersistenceException() throws Exception {
        Project project = new Project(new Date(), new DefaultWorkdaysFactory(false).createWorkdaysInstance());
        final Phase[] phases = new Phase[] {new Phase(project, 1)};
        for (int i = 0; i < phases.length; ++i) {
            phases[i].setPhaseStatus(PhaseStatus.CLOSED);
            phases[i].setScheduledStartDate(new Date());
            phases[i].setScheduledEndDate(new Date());
            phases[i].setPhaseType(PHASE_TYPE_ONE);
            project.addPhase(phases[i]);
        }
        final NullPhasePersistence persistence = new NullPhasePersistence() {

            public boolean isNewPhase(Phase phase) {
                return true;
            }

            public void createPhases(Phase[] phases, String operator) throws PhasePersistenceException {
                throw new PhasePersistenceException("just for testing.");
            }

            public Project getProjectPhases(long projectId) throws PhasePersistenceException {
                return PROJECT;
            }
        };
        final IDGenerator idgen = new NullIdGenerator() {

            public long getNextID() throws IDGenerationException {
                return getNext();
            }
        };
        DefaultPhaseManager manager = new DefaultPhaseManager(persistence, idgen);
        try {
            manager.updatePhases(project, "op");
            fail("PhaseManagementException exception is expected.");
        } catch (PhaseManagementException e) {
            // expected
        }
    }
}
