/*
 * Copyright (C) 2009 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases.accuracytests;

import com.cronos.onlinereview.phases.PostMortemPhaseHandler;

import com.topcoder.management.resource.Resource;

import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.Project;

import java.sql.Connection;


/**
 * Accuracy tests for the new added phase handler <code>PostMortemPhaseHandler</code> in version 1.1.
 *
 * @author assistant
 * @version 1.2
 *
 * @since 1.1
 */
public class PostMortemPhaseHandlerTestV12 extends BaseTestCase {
    /**
     * sets up the environment required for test cases for this class.
     *
     * @throws Exception to JUnit.
     */
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * cleans up the environment required for test cases for this class.
     *
     * @throws Exception to JUnit.
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Tests the perform with Scheduled statuses.
     *
     * @throws Exception not under test
     *
     * @since 1.2
     */
    public void testPerform_1() throws Exception {
        PostMortemPhaseHandler handler = new PostMortemPhaseHandler(PostMortemPhaseHandler.DEFAULT_NAMESPACE);

        Project project = setupProjectResourcesNotification("All", true);
        Phase[] phases = project.getAllPhases();
        Phase postMortemPhase = phases[11];

        handler.perform(postMortemPhase, "1001");
    }

    /**
     * Tests the perform with Scheduled statuses.
     *
     * @throws Exception not under test.
     *
     * @since 1.2
     */
    public void testPerform_2() throws Exception {
        PostMortemPhaseHandler handler = new PostMortemPhaseHandler(PostMortemPhaseHandler.DEFAULT_NAMESPACE);

        Project project = setupProjectResourcesNotification("All", true);
        Phase[] phases = project.getAllPhases();
        Phase postMortemPhase = phases[11];

        //assign reviewer
        Resource postMortemReviewer = createResource(1011, postMortemPhase.getId(), project.getId(), 14);
        Connection conn = getConnection();
        insertResources(conn, new Resource[] { postMortemReviewer });
        insertResourceInfo(conn, postMortemReviewer.getId(), 1, "2");

        // test perform, it should do nothing
        handler.perform(postMortemPhase, "1001");
    }
}
