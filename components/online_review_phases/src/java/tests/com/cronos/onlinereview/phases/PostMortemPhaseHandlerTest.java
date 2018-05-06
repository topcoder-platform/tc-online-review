/*
 * Copyright (C) 2009-2011 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.deliverable.Upload;
import com.topcoder.management.phase.PhaseHandler;
import com.topcoder.management.phase.PhaseHandlingException;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.review.data.Review;
import com.topcoder.management.scorecard.data.Scorecard;

import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.PhaseStatus;
import com.topcoder.project.phases.Project;

import com.topcoder.util.config.ConfigManager;

import java.sql.Connection;

import java.util.Date;

/**
 * Unit tests for the new added phase handler <code>PostMortemPhaseHandler</code> in version 1.1.
 * <p>
 * Version 1.2 change notes : since the email-templates and role-supported has been enhanced. The test cases will
 * try to do on that way while for email content, please check it manually.
 * </p>
 * <p>
 * Version 1.4 change notes : tests both user has accepted terms of use or does not has accepted the terms can be
 * assigned specified role.
 * </p>
 * <p>
 * Version 1.6.1 changes note:
 * <ul>
 * <li>Change some test because the return of canPerform change from boolean to OperationCheckResult.</li>
 * </ul>
 * </p>
 * @author waits, myxgyy, microsky
 * @version 1.6.1
 * @since 1.1
 */
public class PostMortemPhaseHandlerTest extends BaseTest {
    /**
     * sets up the environment required for test cases for this class.
     * @throws Exception to JUnit.
     */
    protected void setUp() throws Exception {
        super.setUp();

        ConfigManager configManager = ConfigManager.getInstance();

        configManager.add(PHASE_HANDLER_CONFIG_FILE);

        configManager.add(EMAIL_CONFIG_FILE);
        configManager.add(MANAGER_HELPER_CONFIG_FILE);

        // add the component configurations as well
        for (int i = 0; i < COMPONENT_FILE_NAMES.length; i++) {
            configManager.add(COMPONENT_FILE_NAMES[i]);
        }
    }

    /**
     * cleans up the environment required for test cases for this class.
     * @throws Exception to JUnit.
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Tests the default constructor of PostMortemPhaseHandler.
     * @throws Exception to JUnit.
     */
    public void testDefaultConstructor() throws Exception {
        PhaseHandler handler = new PostMortemPhaseHandler();

        assertNotNull("PostMortemPhaseHandler should be correctly created.", handler);
    }

    /**
     * Tests canPerform(Phase) with null phase.
     * @throws Exception not under test.
     */
    public void testCanPerform() throws Exception {
        PostMortemPhaseHandler handler = new PostMortemPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            handler.canPerform(null);
            fail("canPerform() did not throw IllegalArgumentException for null argument.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Tests canPerform(Phase) with invalid phase status.
     * @throws Exception to JUnit.
     */
    public void testCanPerformWithInvalidStatus() throws Exception {
        PostMortemPhaseHandler handler = new PostMortemPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            Phase phase = createPhase(1, 1, "Invalid", 12, "Post-Mortem");
            handler.canPerform(phase);
            fail("canPerform() did not throw PhaseHandlingException for invalid phase status.");
        } catch (PhaseHandlingException e) {
            // expected.
        }
    }

    /**
     * Tests canPerform(Phase) with invalid phase type.
     * @throws Exception to JUnit.
     */
    public void testCanPerformWithInvalidType() throws Exception {
        PostMortemPhaseHandler handler = new PostMortemPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            Phase phase = createPhase(1, 1, "Scheduled", 12, "INVALID");
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
        PostMortemPhaseHandler handler = new PostMortemPhaseHandler(PHASE_HANDLER_NAMESPACE);

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
        PostMortemPhaseHandler handler = new PostMortemPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            Phase phase = createPhase(1, 1, "Invalid", 12, "Post-Mortem");
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
        PostMortemPhaseHandler handler = new PostMortemPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            Phase phase = createPhase(1, 1, "Scheduled", 12, "INVALID");
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
        PostMortemPhaseHandler handler = new PostMortemPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            Phase phase = createPhase(1, 1, "Scheduled", 12, "Post-Mortem");
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
        PostMortemPhaseHandler handler = new PostMortemPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            Phase phase = createPhase(1, 1, "Scheduled", 12, "Post-Mortem");
            handler.perform(phase, "   ");
            fail("perform() did not throw IllegalArgumentException for empty operator.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Tests the PostMortemPhaseHandler() constructor and canPerform with Scheduled statuses.
     * @throws Exception to JUnit.
     */
    public void testCanPerformWithScheduled() throws Exception {
        PostMortemPhaseHandler handler = new PostMortemPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            cleanTables();

            Project project = super.setupPhasesWithPostMortem();
            Phase[] phases = project.getAllPhases();
            Phase postMortemPhase = phases[11];

            // test with scheduled status.
            postMortemPhase.setPhaseStatus(PhaseStatus.SCHEDULED);

            // time has not passed
            assertFalse("canPerform should have returned false", handler.canPerform(postMortemPhase).isSuccess());

            // time has passed, but dependency not met.
            postMortemPhase.setActualEndDate(new Date());
            postMortemPhase.setActualStartDate(new Date());
            assertFalse("canPerform should have returned false", handler.canPerform(postMortemPhase).isSuccess());

            // set the number of required post-mortem reviewer to 1
            postMortemPhase.setAttribute("Reviewer Number", "1");

            // time has passed and dependency met
            postMortemPhase.getAllDependencies()[0].getDependency().setPhaseStatus(PhaseStatus.CLOSED);
            assertTrue("canPerform should return true when dependencies met and time is up",
                handler.canPerform(postMortemPhase).isSuccess());
        } finally {
            cleanTables();
        }
    }

    /**
     * Tests the PostMortemPhaseHandler() constructor and canPerform with Open statuses.
     * @throws Exception to JUnit.
     */
    public void testCanPerformWithOpen1() throws Exception {
        PostMortemPhaseHandler handler = new PostMortemPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            cleanTables();

            Project project = super.setupPhasesWithPostMortem();
            Phase[] phases = project.getAllPhases();
            Phase postMortemPhase = phases[11];

            // change dependency type to false
            postMortemPhase.getAllDependencies()[0].setDependentStart(false);

            // test with open status.
            postMortemPhase.setPhaseStatus(PhaseStatus.OPEN);

            // dependencies not met
            assertFalse("canPerform should have returned false", handler.canPerform(postMortemPhase).isSuccess());

            // remove all dependencies
            for (int i = 0; i < postMortemPhase.getAllDependencies().length; ++i) {
                postMortemPhase.removeDependency(postMortemPhase.getAllDependencies()[i]);
            }

            // set the number of required post-mortem reviewer to 1
            postMortemPhase.setAttribute("Reviewer Number", "1");

            // dependencies met but without post-mortem reviewer
            assertFalse("canPerform should have returned false", handler.canPerform(postMortemPhase).isSuccess());

            // insert post-mortem reviewer
            Resource postMortemReviewer = createResource(101, postMortemPhase.getId(), project.getId(), 14);

            Connection conn = getConnection();

            // insert records
            insertResources(conn, new Resource[] {postMortemReviewer });

            // we need to insert an external reference id
            // which references to resource's user id in resource_info table
            insertResourceInfo(conn, postMortemReviewer.getId(), 1, "1001");

            // there is not scorecard filled
            assertFalse("canPerform should have returned false", handler.canPerform(postMortemPhase).isSuccess());

            // insert a scorecard here
            Upload upload = createUpload(101, project.getId(), postMortemReviewer.getId(), 4, 1, "parameter");
            Submission submission = createSubmission(101, upload.getId(), 1, 1);
            submission.setUpload(upload);

            Scorecard scorecard = createScorecard(1, 1, 2, 1, "name", "1.0", 75.0f, 100.0f);

            // scorecard not committed
            Review postMortemScorecard = createReview(11, postMortemReviewer.getId(), submission.getId(),
                    scorecard.getId(), false, 90.0f);

            this.insertUploads(conn, new Upload[] {upload });
            this.insertSubmissions(conn, new Submission[] {submission });
            this.insertScorecards(conn, new Scorecard[] {scorecard });
            this.insertReviews(conn, new Review[] {postMortemScorecard });

            // scorecard not committed return false
            assertFalse("canPerform should have returned false", handler.canPerform(postMortemPhase).isSuccess());
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Tests the PostMortemPhaseHandler() constructor and canPerform with Open statuses.
     * @throws Exception to JUnit.
     */
    public void testCanPerformWithOpen2() throws Exception {
        PostMortemPhaseHandler handler = new PostMortemPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
            cleanTables();

            Project project = super.setupPhasesWithPostMortem();
            Phase[] phases = project.getAllPhases();
            Phase postMortemPhase = phases[11];

            // test with open status.
            postMortemPhase.setPhaseStatus(PhaseStatus.OPEN);

            // time passed
            postMortemPhase.setActualEndDate(new Date());
            postMortemPhase.setActualStartDate(new Date(System.currentTimeMillis() - 1000));
            // remove all dependencies
            for (int i = 0; i < postMortemPhase.getAllDependencies().length; ++i) {
                postMortemPhase.removeDependency(postMortemPhase.getAllDependencies()[i]);
            }

            // set the number of required post-mortem reviewer to 1
            postMortemPhase.setAttribute("Reviewer Number", "1");

            // insert post-mortem reviewer
            Resource postMortemReviewer = createResource(101, postMortemPhase.getId(), project.getId(), 16);

            Connection conn = getConnection();

            // insert records
            insertResources(conn, new Resource[] {postMortemReviewer });

            // we need to insert an external reference id
            // which references to resource's user id in resource_info table
            insertResourceInfo(conn, postMortemReviewer.getId(), 1, "1001");

            // insert a scorecard here
            Upload upload = createUpload(101, project.getId(), postMortemReviewer.getId(), 4, 1, "parameter");
            Submission submission = createSubmission(101, upload.getId(), 1, 1);
            submission.setUpload(upload);

            Scorecard scorecard1 = createScorecard(1, 1, 2, 1, "name", "1.0", 75.0f, 100.0f);

            // insert a committed scorecard
            Review postMortemScorecard = createReview(11, postMortemReviewer.getId(), submission.getId(),
                    scorecard1.getId(), true, 90.0f);

            this.insertUploads(conn, new Upload[] {upload });
            this.insertSubmissions(conn, new Submission[] {submission });
            this.insertScorecards(conn, new Scorecard[] {scorecard1 });
            this.insertReviews(conn, new Review[] {postMortemScorecard });

            // scorecard committed
            assertTrue("canPerform should have returned true", handler.canPerform(postMortemPhase).isSuccess());
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Tests the perform with Scheduled statuses.
     * @throws Exception not under test
     * @since 1.2
     */
    public void testPerform_start_no_reviwer() throws Exception {
        PostMortemPhaseHandler handler = new PostMortemPhaseHandler(PostMortemPhaseHandler.DEFAULT_NAMESPACE);

        try {
            cleanTables();

            Project project = setupProjectResourcesNotification("All", true, false);
            Phase[] phases = project.getAllPhases();
            Phase postMortemPhase = phases[11];

            // test perform, it should do nothing
            handler.perform(postMortemPhase, "1001");
        } finally {
            cleanTables();
        }
    }

    /**
     * Tests the perform with Scheduled statuses.
     * @throws Exception not under test.
     * @since 1.2
     */
    public void testPerform_start_with_reviewer() throws Exception {
        PostMortemPhaseHandler handler = new PostMortemPhaseHandler(PostMortemPhaseHandler.DEFAULT_NAMESPACE);

        try {
            cleanTables();

            Project project = setupProjectResourcesNotification("All", true, false);
            Phase[] phases = project.getAllPhases();
            Phase postMortemPhase = phases[11];

            // assign reviewer
            Resource postMortemReviewer = createResource(1011, postMortemPhase.getId(), project.getId(), 14);
            Connection conn = getConnection();
            insertResources(conn, new Resource[] {postMortemReviewer });
            insertResourceInfo(conn, postMortemReviewer.getId(), 1, "2");

            // test perform, it should do nothing
            handler.perform(postMortemPhase, "1001");
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Tests the perform with Scheduled statuses.
     * @throws Exception not under test.
     * @since 1.4
     */
    public void testPerformStart_ReviewerSubmitterHasTermsOfUse() throws Exception {
        PostMortemPhaseHandler handler = new PostMortemPhaseHandler(PostMortemPhaseHandler.DEFAULT_NAMESPACE);

        try {
            cleanTables();

            Project project = this.setupPhasesWithPostMortem();
            Phase[] phases = project.getAllPhases();
            Phase postMortemPhase = phases[11];
            Connection conn = getConnection();
            // create a registration
            Resource resource = createResource(4, 101L, 1, 1);
            super.insertResources(conn, new Resource[] {resource });
            insertResourceInfo(conn, resource.getId(), 1, "4");

            // insert upload/submission
            Upload upload = super.createUpload(1, project.getId(), 4, 1, 1, "Parameter");
            super.insertUploads(conn, new Upload[] {upload });

            Submission submission = super.createSubmission(1, upload.getId(), 1, 1);
            super.insertSubmissions(conn, new Submission[] {submission });
            super.insertResourceSubmission(conn, 4, 1);

            // assign reviewer
            Resource postMortemReviewer = createResource(1011, postMortemPhase.getId(), project.getId(), 14);
            insertResources(conn, new Resource[] {postMortemReviewer });
            insertResourceInfo(conn, postMortemReviewer.getId(), 1, "2");

            // test perform, it should do nothing
            handler.perform(postMortemPhase, "1001");
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Tests the perform with Scheduled statuses.
     * @throws Exception not under test.
     * @since 1.4
     */
    public void testPerformStart_ReviewerSubmitterNoTermsOfUse() throws Exception {
        PostMortemPhaseHandler handler = new PostMortemPhaseHandler(PostMortemPhaseHandler.DEFAULT_NAMESPACE);
        try {
            cleanTables();

            Project project = this.setupPhasesWithPostMortem();
            Phase[] phases = project.getAllPhases();
            Phase postMortemPhase = phases[11];
            Connection conn = getConnection();
            // create a registration
            Resource resource = createResource(1, 101L, 1, 1);
            super.insertResources(conn, new Resource[] {resource });
            // user has term of use not accepted
            insertResourceInfo(conn, resource.getId(), 1, "1");

            // insert upload/submission
            Upload upload = super.createUpload(1, project.getId(), 1, 1, 1, "Parameter");
            super.insertUploads(conn, new Upload[] {upload });

            Submission submission = super.createSubmission(1, upload.getId(), 1, 1);
            super.insertSubmissions(conn, new Submission[] {submission });
            super.insertResourceSubmission(conn, 1, 1);

            // assign reviewer
            Resource postMortemReviewer = createResource(1011, postMortemPhase.getId(), project.getId(), 14);
            insertResources(conn, new Resource[] {postMortemReviewer });
            insertResourceInfo(conn, postMortemReviewer.getId(), 1, "2");

            // test perform
            handler.perform(postMortemPhase, "1001");
            Resource updated = PhasesHelper.searchProjectResourcesForRoleNames(handler.getManagerHelper(),
                conn, new String[] {"Post-Mortem Reviewer" }, 1)[0];
            //assertEquals("should be '0.00'", "0.00", updated.getProperty(PhasesHelper.PAYMENT_PROPERTY_KEY));
            //Change from 'No' to 'N/A'
            assertEquals("should be 'N/A'", "N/A", updated.getProperty("Payment Status"));
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Tests the perform with Open statuses.
     * @throws Exception not under test.
     * @since .12
     */
    public void testPerform_stop() throws Exception {
        PostMortemPhaseHandler handler = new PostMortemPhaseHandler();

        try {
            cleanTables();

            Project project = setupProjectResourcesNotification("All", true, false);
            Phase[] phases = project.getAllPhases();
            Phase postMortemPhase = phases[11];

            postMortemPhase.setPhaseStatus(PhaseStatus.OPEN);
            // test perform
            handler.perform(postMortemPhase, "1001");

            // manually check the email
        } finally {
            cleanTables();
        }
    }
}
