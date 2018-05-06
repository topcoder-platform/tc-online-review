/*
 * Copyright (C) 2010-2011 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import java.sql.Connection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.deliverable.Upload;
import com.topcoder.management.phase.PhaseHandlingException;
import com.topcoder.management.resource.Resource;
import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.PhaseStatus;
import com.topcoder.project.phases.Project;
import com.topcoder.util.config.ConfigManager;

/**
 * All tests for <code>SpecificationSubmissionPhaseHandler</code> class.
 * <p>
 * Version 1.6.1 changes note:
 * <ul>
 * <li>Change some test because the return of canPerform change from boolean to OperationCheckResult.</li>
 * </ul>
 * </p>
 * @author myxgyy, microsky
 * @version 1.6.1
 * @since 1.4
 */
public class SpecificationSubmissionPhaseHandlerTest extends BaseTest {
    /**
     * Target instance.
     */
    private SpecificationSubmissionPhaseHandler handler;

    /**
     * sets up the environment required for test cases for this class.
     * @throws Exception
     *             not under test.
     */
    protected void setUp() throws Exception {
        super.setUp();

        ConfigManager configManager = ConfigManager.getInstance();

        configManager.add(PHASE_HANDLER_CONFIG_FILE);

        configManager.add(MANAGER_HELPER_CONFIG_FILE);

        // add the component configurations as well
        for (int i = 0; i < COMPONENT_FILE_NAMES.length; i++) {
            configManager.add(COMPONENT_FILE_NAMES[i]);
        }

        handler = new SpecificationSubmissionPhaseHandler();
    }

    /**
     * cleans up the environment required for test cases for this class.
     * @throws Exception
     *             not under test.
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Tests non-argument constructor of <code>SpecificationSubmissionPhaseHandler</code> class.
     * @throws Exception
     *             to JUnit.
     */
    public void testConstructor1() throws Exception {
        final String[] startContents = new String[] {
            "topcoder.developer@gmail.com",
            "test_files/email_templates/specification_submission/specification_submission_start_template.txt",
            "Phase Start" };
        final String[] endContents = new String[] {
            "topcoder.developer@gmail.com",
            "test_files/email_templates/specification_submission/specification_submission_end_template.txt",
            "Phase End" };

        Map<String, String[]> startContentsMap = new HashMap<String, String[]>();
        startContentsMap.put("Specification Reviewer", startContents);
        startContentsMap.put("Manager", startContents);
        startContentsMap.put("Specification Submitter", startContents);
        startContentsMap.put("Observer", startContents);

        Map<String, String[]> endContentsMap = new HashMap<String, String[]>();
        endContentsMap.put("Specification Reviewer", endContents);
        endContentsMap.put("Manager", endContents);
        endContentsMap.put("Specification Submitter", endContents);
        endContentsMap.put("Observer", endContents);

        Map<String, Map<String, String[]>> contents = new HashMap<String, Map<String, String[]>>();
        contents.put("start", startContentsMap);
        contents.put("end", endContentsMap);
        verifyFileds(handler, contents);
    }

    /**
     * Tests constructor with argument of <code>SpecificationSubmissionPhaseHandler</code> class.
     * @throws Exception
     *             to JUnit.
     */
    public void testConstructor2() throws Exception {
        handler = new SpecificationSubmissionPhaseHandler(PHASE_HANDLER_NAMESPACE);

        Map<String, String[]> startContents = new HashMap<String, String[]>();
        startContents.put("Reviewer", new String[] {"topcoder.developer@gmail.com",
            "test_files/valid_email_template.txt", "Phase Start" });
        startContents.put("Manager", new String[] {"topcoder.developer@gmail.com",
            "test_files/valid_email_template_manager.txt", "Phase Start" });

        Map<String, String[]> endContents = new HashMap<String, String[]>();
        endContents.put("Reviewer", new String[] {"topcoder.developer@gmail.com",
            "test_files/valid_email_template.txt", "Phase End" });
        endContents.put("Manager", new String[] {"topcoder.developer@gmail.com",
            "test_files/valid_email_template_manager.txt", "Phase End" });

        Map<String, Map<String, String[]>> contents = new HashMap<String, Map<String, String[]>>();
        contents.put("start", startContents);
        contents.put("end", endContents);
        verifyFileds(handler, contents);
    }

    /**
     * Tests canPerform(Phase) with null phase. IllegalArgumentException expected.
     * @throws Exception
     *             not under test.
     */
    public void testCanPerform() throws Exception {
        try {
            handler.canPerform(null);
            fail("canPerform() did not throw IllegalArgumentException for null argument.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Tests canPerform(Phase) with invalid phase status. PhaseHandlingException expected.
     * @throws Exception
     *             not under test.
     */
    public void testCanPerformWithInvalidStatus() throws Exception {
        try {
            Phase phase = createPhase(1, 1, "Invalid", 3, "Specification Submission");
            handler.canPerform(phase);
            fail("canPerform() did not throw PhaseHandlingException for invalid phase status.");
        } catch (PhaseHandlingException e) {
            // expected.
        }
    }

    /**
     * Tests canPerform(Phase) with invalid phase type. PhaseHandlingException expected.
     * @throws Exception
     *             not under test.
     */
    public void testCanPerformWithInvalidType() throws Exception {
        try {
            Phase phase = createPhase(1, 1, "Scheduled", 1, "INVALID");
            handler.canPerform(phase);
            fail("canPerform() did not throw PhaseHandlingException for invalid phase type.");
        } catch (PhaseHandlingException e) {
            // expected.
        }
    }

    /**
     * Tests perform(Phase) with null phase. IllegalArgumentException expected.
     * @throws Exception
     *             not under test.
     */
    public void testPerformWithNullPhase() throws Exception {
        try {
            handler.perform(null, "operator");
            fail("perform() did not throw IllegalArgumentException for null argument.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Tests perform(Phase) with invalid phase status. PhaseHandlingException expected.
     * @throws Exception
     *             not under test.
     */
    public void testPerformWithInvalidStatus() throws Exception {
        try {
            Phase phase = createPhase(1, 1, "Invalid", 1, "Specification Submission");
            handler.perform(phase, "operator");
            fail("perform() did not throw PhaseHandlingException for invalid phase status.");
        } catch (PhaseHandlingException e) {
            // expected.
        }
    }

    /**
     * Tests perform(Phase) with invalid phase type. PhaseHandlingException expected.
     * @throws Exception
     *             not under test.
     */
    public void testPerformWithInvalidType() throws Exception {
        try {
            Phase phase = createPhase(1, 1, "Scheduled", 1, "INVALID");
            handler.perform(phase, "operator");
            fail("perform() did not throw PhaseHandlingException for invalid phase type.");
        } catch (PhaseHandlingException e) {
            // expected.
        }
    }

    /**
     * Tests perform(Phase) with null operator. IllegalArgumentException expected.
     * @throws Exception
     *             not under test.
     */
    public void testPerformWithNullOperator() throws Exception {
        try {
            Phase phase = createPhase(1, 1, "Scheduled", 1, "Specification Submission");
            handler.perform(phase, null);
            fail("perform() did not throw IllegalArgumentException for null operator.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Tests perform(Phase) with empty operator. IllegalArgumentException expected.
     * @throws Exception
     *             not under test.
     */
    public void testPerformWithEmptyOperator() throws Exception {
        try {
            Phase phase = createPhase(1, 1, "Scheduled", 1, "Specification Submission");
            handler.perform(phase, "   ");
            fail("perform() did not throw IllegalArgumentException for empty operator.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Tests the canPerform method with Scheduled statuses. The dependencies are not met, the
     * method will return false.
     * @throws Exception
     *             not under test.
     */
    public void testCanPerformWithScheduled_DependenciesNotMet() throws Exception {
        try {
            cleanTables();

            Project project = super.setupPhases();
            Phase[] phases = project.getAllPhases();
            Phase phase = phases[11];
            phase.setPhaseStatus(PhaseStatus.SCHEDULED);
            assertFalse("canPerform should have returned false", handler.canPerform(phase).isSuccess());
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Tests the canPerform method with Scheduled statuses. The specification submission
     * phase is not first phase in this case, the method will return true.
     * @throws Exception
     *             not under test.
     */
    public void testCanPerformWithScheduled_NotFirstPhase() throws Exception {
        try {
            cleanTables();

            Project project = super.setupPhases();
            Phase[] phases = project.getAllPhases();
            // make all dependencies met
            for (int i = 0; i < 11; i++) {
                phases[i].setPhaseStatus(PhaseStatus.CLOSED);
            }

            Phase phase = phases[11];

            // make start time reached
            phase.setActualStartDate(new Date());
            phase.setActualEndDate(new Date());

            phase.setPhaseStatus(PhaseStatus.SCHEDULED);
            assertTrue("canPerform should have returned true", handler.canPerform(phase).isSuccess());
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Tests the canPerform method with Scheduled statuses. The specification submission
     * phase is the first phase but parent project is not completed in this case, the
     * method will return false.
     * @throws Exception
     *             not under test.
     */
    public void testCanPerformWithScheduled_ParentProjectsNotCompleted() throws Exception {
        try {
            cleanTables();

            Project project = super.setupPhasesForSpec(false);
            Phase[] phases = project.getAllPhases();
            Phase phase = phases[0];

            phase.setPhaseStatus(PhaseStatus.SCHEDULED);
            assertFalse("canPerform should have returned false", handler.canPerform(phase).isSuccess());
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Tests the canPerform method with Scheduled statuses. The specification submission
     * phase is the first phase and parent project is completed in this case, the method
     * will return true.
     * @throws Exception
     *             not under test.
     */
    public void testCanPerformWithScheduled() throws Exception {
        try {
            cleanTables();

            Project project = super.setupPhasesForSpec(true);
            Phase[] phases = project.getAllPhases();
            Phase phase = phases[0];

            // test with scheduled status.
            phase.setPhaseStatus(PhaseStatus.SCHEDULED);
            assertTrue("canPerform should have returned false", handler.canPerform(phase).isSuccess());
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Tests the canPerform method with Open statuses. The dependencies are met, but no
     * specification submission exists, the method will return false.
     * @throws Exception
     *             not under test.
     */
    public void testCanPerformWithOpen_NoSpecificationSubmission() throws Exception {
        try {
            cleanTables();

            Project project = super.setupPhasesForSpec(true);
            Phase[] phases = project.getAllPhases();
            Phase phase = phases[0];

            // test with scheduled status.
            phase.setPhaseStatus(PhaseStatus.OPEN);
            assertFalse("canPerform should have returned false", handler.canPerform(phase).isSuccess());
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Tests the canPerform method with Open statuses. The dependencies are not met, the
     * method will return false.
     * @throws Exception
     *             not under test.
     */
    public void testCanPerformWithOpen_DependenciesNotMet() throws Exception {
        try {
            cleanTables();

            Project project = super.setupPhases();
            Phase[] phases = project.getAllPhases();
            Phase phase = phases[11];

            // test with scheduled status.
            phase.getAllDependencies()[0].setDependentStart(false);
            phase.setPhaseStatus(PhaseStatus.OPEN);
            assertFalse("canPerform should have returned false", handler.canPerform(phase).isSuccess());
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Tests the canPerform method with Open statuses. The dependencies are met and there
     * is one specification submission exists, the method will return true.
     * @throws Exception
     *             not under test.
     */
    public void testCanPerformWithOpen() throws Exception {
        try {
            cleanTables();

            Project project = super.setupPhasesForSpec(true);
            Phase[] phases = project.getAllPhases();
            Phase phase = phases[0];

            // test with open status.
            phase.setPhaseStatus(PhaseStatus.OPEN);

            // insert a submission with specification submission
            Connection conn = getConnection();
            Resource resource = createResource(4, 101L, 1, 17);
            super.insertResources(conn, new Resource[] {resource });

            Upload upload = super.createUpload(1, project.getId(), 4, 1, 1, "parameter");
            super.insertUploads(conn, new Upload[] {upload });

            Submission submission = super.createSubmission(1, upload.getId(), 1, 2);
            super.insertSubmissions(conn, new Submission[] {submission });

            assertTrue("canPerform should have returned true", handler.canPerform(phase).isSuccess());
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Tests the perform with Open statuses. Verify the content of the email manually.
     * @throws Exception
     *             not under test.
     */
    public void testPerformWithOpen() throws Exception {
        try {
            cleanTables();

            super.setupProjectResourcesNotification("All", false, true);
            // test with open status.
            Phase phase = createPhase(101, 2, "Open", 13, "Specification Submission");

            handler.perform(phase, "operator");
            // one phase end email should be sent
            // please manually check the email
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Tests the perform with Scheduled statuses. Verify the content of the email
     * manually.
     * @throws Exception
     *             not under test.
     */
    public void testPerformWithScheduled() throws Exception {
        try {
            cleanTables();

            Project project = super.setupProjectResourcesNotification("All", false, true);
            Phase[] phases = project.getAllPhases();
            Phase phase = phases[0];

            // test with Scheduled status.
            phase.setPhaseStatus(PhaseStatus.SCHEDULED);

            // insert a reviewer
            Connection conn = getConnection();
            Resource reviewer = createResource(1, 102L, 1, 15);
            super.insertResources(conn, new Resource[] {reviewer });
            insertResourceInfo(conn, reviewer.getId(), 1, "3");

            // create a registration
            Resource resource = createResource(4, 101L, 1, 17);
            super.insertResources(conn, new Resource[] {resource });
            insertResourceInfo(conn, resource.getId(), 1, "4");

            // insert upload/submission
            Upload upload = super.createUpload(1, project.getId(), 1, 1, 1, "parameter");
            super.insertUploads(conn, new Upload[] {upload });

            Submission submission = super.createSubmission(1, upload.getId(), 1, 2);
            super.insertSubmissions(conn, new Submission[] {submission });

            handler.perform(phase, "operator");
            // two phase start email should be sent
            // please manually check the email
        } finally {
            cleanTables();
            closeConnection();
        }
    }
}