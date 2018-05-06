/**
 * 
 */

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
 * Accuracy tests for V1.2 <code>RegistrationPhaseHandler</code>.
 *
 * @author assistant
 * @version 1.2
 */
public class RegistrationPhaseHandlerTestV12 extends BaseTestCase {
    /** Instance to test. */
    private RegistrationPhaseHandler instance;

    /**
     * Sets up the environment.
     *
     * @throws java.lang.Exception to JUnit
     */
    protected void setUp() throws Exception {
        super.setUp();
        instance = new RegistrationPhaseHandler();
    }

    /**
     * Cleans up the environment.
     *
     * @throws java.lang.Exception to JUnit
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test method for {@link com.cronos.onlinereview.phases.RegistrationPhaseHandler
     * #perform(com.topcoder.project.phases.Phase, java.lang.String)}.
     *
     * @throws Exception to JUnit
     */
    public void testPerform_1() throws Exception {
        setupProjectResourcesNotification("Registration", false);

        Phase registration = createPhase(1, 1, "Scheduled", 1, "Registration");
        Connection conn = getConnection();

        // test with open status
        registration.setPhaseStatus(PhaseStatus.OPEN);

        Resource resource = createResource(4, 101L, 1, 1);
        super.insertResources(conn, new Resource[] { resource });
        insertResourceInfo(conn, resource.getId(), 1, "4");
        insertResourceInfo(conn, resource.getId(), 2, "ACRush");
        insertResourceInfo(conn, resource.getId(), 4, "3808");
        insertResourceInfo(conn, resource.getId(), 5, "100");

        instance.perform(registration, "1001");

        // the subject should be Phase End: Online Review Phases
        // there should be one registration information in the email
        // manager/observer/reviewer should receive the email
        closeConnection();
    }

    /**
     * Test method for {@link com.cronos.onlinereview.phases.RegistrationPhaseHandler
     * #perform(com.topcoder.project.phases.Phase, java.lang.String)}.
     *
     * @throws Exception to JUnit
     */
    public void testPerform_2() throws Exception {
        Project project = setupProjectResourcesNotification("Registration", true);
        project.getAllPhases()[0].setPhaseStatus(PhaseStatus.OPEN);

        instance.perform(project.getAllPhases()[0], "1001");

        // the subject should be Phase End: Online Review Phases
        // no registrants
        // manager/observer should receive this email
    }

    /**
     * Test method for {@link com.cronos.onlinereview.phases.RegistrationPhaseHandler
     * #perform(com.topcoder.project.phases.Phase, java.lang.String)}.
     *
     * @throws Exception to JUnit
     */
    public void testPerform_3() throws Exception {
        setupProjectResourcesNotification("Registration", true);

        // test phase start
        Phase registration = createPhase(101, 1, "Scheduled", 1, "Registration");

        instance.perform(registration, "1001");

        // the subject should be Phase Start: Online Review Phases
        // manager/observer should receive this email
    }
}
