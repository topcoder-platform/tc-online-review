/*
 * Copyright (C) 2009 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases.accuracytests;

import java.sql.Connection;

import com.cronos.onlinereview.phases.ApprovalPhaseHandler;
import com.topcoder.management.resource.Resource;
import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.Project;


/**
 * Accuracy tests for ApprovalPhaseHandler class.
 *
 * @author assistant
 * @version 1.2
 */
public class ApprovalPhaseHandlerTestV12 extends BaseTestCase {
    /**
     * sets up the environment required for test cases for this class.
     *
     * @throws Exception not under test.
     */
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * cleans up the environment required for test cases for this class.
     *
     * @throws Exception not under test.
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Tests the perform with Scheduled statuses.
     *
     * @throws Exception not under test.
     *
     * @since 1.2
     */
    public void testPerform_1() throws Exception {
        ApprovalPhaseHandler handler = new ApprovalPhaseHandler(ApprovalPhaseHandler.DEFAULT_NAMESPACE);
        Project project = super.setupProjectResourcesNotification("Approval", true);
        Phase[] phases = project.getAllPhases();
        Phase approvalPhase = phases[10];
        handler.perform(approvalPhase, "1001");
    }

    /**
     * Tests the perform with Scheduled statuses.
     *
     * @throws Exception not under test.
     *
     * @since 1.2
     */
    public void testPerform_2() throws Exception {
        ApprovalPhaseHandler handler = new ApprovalPhaseHandler(ApprovalPhaseHandler.DEFAULT_NAMESPACE);

        Project project = super.setupProjectResourcesNotification("Approval", true);
        Phase[] phases = project.getAllPhases();
        Phase approvalPhase = phases[10];

        Resource approval = createResource(100233, approvalPhase.getId(), project.getId(), 10);
        Connection conn = getConnection();
        insertResources(conn, new Resource[] {approval});
        insertResourceInfo(conn, approval.getId(), 1, "2");

        handler.perform(approvalPhase, "1001");

    }
}
