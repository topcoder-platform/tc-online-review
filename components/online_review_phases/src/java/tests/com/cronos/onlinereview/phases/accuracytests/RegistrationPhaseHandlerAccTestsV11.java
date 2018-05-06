/*
 * Copyright (C) 2009 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases.accuracytests;

import com.cronos.onlinereview.phases.RegistrationPhaseHandler;

import com.topcoder.management.resource.Resource;

import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.PhaseStatus;
import com.topcoder.project.phases.Project;

import java.sql.Connection;


/**
 * Accuracy test cases for change functions in version 1.1 of <code>RegistrationPhaseHandler</code> class.
 *
 * @author myxgyy
 * @version 1.0
 */
public class RegistrationPhaseHandlerAccTestsV11 extends BaseTestCase {
    /** Target instance. */
    private RegistrationPhaseHandler handler;

    /**
     * Sets up the environment required for test cases for this class.
     *
     * @throws Exception to JUnit.
     */
    protected void setUp() throws Exception {
        super.setUp();
        handler = new RegistrationPhaseHandler(PHASE_HANDLER_NAMESPACE);
    }

    /**
     * Cleans up the environment required for test cases for this class.
     *
     * @throws Exception to JUnit.
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Tests the perform with Scheduled and Open statuses but without registrations. The test checks whether
     * the post-mortem phase is inserted when there is no registration.
     *
     * @throws Exception to Junit.
     */
    public void testPerform1() throws Exception {
        // test with scheduled status.
        Phase registration = createPhase(1, 1, "Scheduled", 1, "Registration");
        Connection conn = getConnection();
        insertProject(conn);
        insertProjectInfo(getConnection(), 1, new long[] {44}, new String[] {"true"});

        String operator = "1000001";
        handler.perform(registration, operator);

        // test with Scheduled status
        assertFalse("Post-mortem phase should not be inserted", havePostMortemPhase(conn));

        // test with open status
        registration.setPhaseStatus(PhaseStatus.OPEN);
        handler.perform(registration, operator);

        // check whether the Post-Mortem phase is inserted when
        // there is no submission
        assertTrue("Post-mortem phase should be inserted", havePostMortemPhase(conn));
    }

    /**
     * Tests the perform with open status and with registration. The post-mortem phase should not be inserted
     * in such a situation.
     *
     * @throws Exception to JUnit.
     */
    public void testPerform2() throws Exception {
        // test with scheduled status.
        Project project = super.setupPhases();
        Phase[] phases = project.getAllPhases();
        Phase registration = phases[0];

        Connection conn = getConnection();

        // test with open status
        registration.setPhaseStatus(PhaseStatus.OPEN);

        // create a registration
        Resource resource = super.createResource(1, 101, 1, 1);
        super.insertResources(conn, new Resource[] { resource });
        super.insertResourceInfo(conn, resource.getId(), 1, "10001");

        handler.perform(registration, "1000001");

        // Post-mortem phase should not be inserted
        // when there is registration
        assertFalse("Post-mortem phase should NOT be inserted", havePostMortemPhase(conn));
    }
}
