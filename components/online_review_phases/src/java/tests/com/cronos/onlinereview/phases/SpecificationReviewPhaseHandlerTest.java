/*
 * Copyright (C) 2010-2011 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.deliverable.Upload;
import com.topcoder.management.phase.PhaseHandlingException;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.review.data.Review;
import com.topcoder.management.scorecard.data.Scorecard;
import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.PhaseStatus;
import com.topcoder.project.phases.Project;
import com.topcoder.util.config.ConfigManager;

/**
 * All tests for <code>SpecificationReviewPhaseHandler</code> class.
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
public class SpecificationReviewPhaseHandlerTest extends BaseTest {
    /**
     * Target instance.
     */
    private SpecificationReviewPhaseHandler handler;

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

        handler = new SpecificationReviewPhaseHandler();
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
        final String[] startContents = new String[] {"topcoder.developer@gmail.com",
            "test_files/email_templates/specification_review/specification_review_start_template.txt",
            "Phase Start" };
        final String[] endContents = new String[] {"topcoder.developer@gmail.com",
            "test_files/email_templates/specification_review/specification_review_end_template.txt",
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
        handler = new SpecificationReviewPhaseHandler(PHASE_HANDLER_NAMESPACE);

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
            Phase phase = createPhase(1, 1, "Invalid", 1, "Specification Review");
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
            Phase phase = createPhase(1, 1, "Scheduled", 1, "Specification Review");
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
            Phase phase = createPhase(1, 1, "Scheduled", 1, "Specification Review");
            handler.perform(phase, "   ");
            fail("perform() did not throw IllegalArgumentException for empty operator.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Tests the canPerform method with Scheduled statuses. The dependencies are not met,
     * the method will return false.
     * @throws Exception
     *             not under test.
     */
    public void testCanPerformWithScheduled_DependenciesNotMet() throws Exception {
        try {
            cleanTables();

            Project project = super.setupPhases();
            Phase[] phases = project.getAllPhases();
            Phase phase = phases[12];
            phase.setPhaseStatus(PhaseStatus.SCHEDULED);
            assertFalse("canPerform should have returned false", handler.canPerform(phase).isSuccess());
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Tests the canPerform method with Scheduled statuses. No active submission with
     * specification submission type exists, the method will return false.
     * @throws Exception
     *             not under test.
     */
    public void testCanPerformWithScheduled_NoSubmission() throws Exception {
        try {
            cleanTables();

            Project project = super.setupPhasesForSpec(true);
            Phase[] phases = project.getAllPhases();
            Phase phase = phases[1];
            phase.setPhaseStatus(PhaseStatus.SCHEDULED);
            assertFalse("canPerform should have returned false", handler.canPerform(phase).isSuccess());
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Tests the canPerform method with Scheduled statuses. Active submission with
     * specification submission type exists, the method will return true.
     * @throws Exception
     *             not under test.
     */
    public void testCanPerformWithScheduled() throws Exception {
        try {
            cleanTables();

            Project project = super.setupPhasesForSpec(true);
            Phase[] phases = project.getAllPhases();
            phases[0].setPhaseStatus(PhaseStatus.CLOSED);

            Phase phase = phases[1];
            phase.setPhaseStatus(PhaseStatus.SCHEDULED);

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
     * Tests the canPerform method with Open statuses. The dependencies are not met, the
     * method will return false.
     * @throws Exception
     *             not under test.
     */
    public void testCanPerformWithOpen__DependenciesNotMet() throws Exception {
        try {
            cleanTables();

            Project project = super.setupPhasesForSpec(false);
            Phase[] phases = project.getAllPhases();
            Phase phase = phases[1];
            phase.setPhaseStatus(PhaseStatus.OPEN);
            phase.getAllDependencies()[0].setDependentStart(false);

            assertFalse("canPerform should have returned false", handler.canPerform(phase).isSuccess());
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
    public void testCanPerformWithOpen__SpecificationSubmissionNotExists() throws Exception {
        try {
            cleanTables();

            Project project = super.setupPhasesForSpec(false);
            Phase[] phases = project.getAllPhases();
            Phase phase = phases[1];
            phase.setPhaseStatus(PhaseStatus.OPEN);

            assertFalse("canPerform should have returned false", handler.canPerform(phase).isSuccess());
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Tests the canPerform method with Open statuses. The dependencies are met, but no
     * specification review exists for submission, the method will return false.
     * @throws Exception
     *             not under test.
     */
    public void testCanPerformWithOpen__SpecificationReviewNotExists() throws Exception {
        try {
            cleanTables();

            Project project = super.setupPhasesForSpec(false);
            Phase[] phases = project.getAllPhases();
            Phase phase = phases[1];
            phase.setPhaseStatus(PhaseStatus.OPEN);

            // insert a submission with specification submission
            Connection conn = getConnection();
            Resource resource = createResource(4, 101L, 1, 17);
            super.insertResources(conn, new Resource[] {resource });

            Upload upload = super.createUpload(1, project.getId(), 4, 1, 1, "parameter");
            super.insertUploads(conn, new Upload[] {upload });

            Submission submission = super.createSubmission(1, upload.getId(), 1, 2);
            super.insertSubmissions(conn, new Submission[] {submission });

            assertFalse("canPerform should have returned false", handler.canPerform(phase).isSuccess());
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Tests the canPerform method with Open statuses. The dependencies are met, but
     * specification review is not committed, the method will return false.
     * @throws Exception
     *             not under test.
     */
    public void testCanPerformWithOpen_ReviewNotCommitted() throws Exception {
        try {
            cleanTables();

            Project project = super.setupPhasesForSpec(false);
            Phase[] phases = project.getAllPhases();
            Phase phase = phases[1];
            phase.setPhaseStatus(PhaseStatus.OPEN);

            // insert a submission with specification submission
            Connection conn = getConnection();

            // create a register
            Resource resource = createResource(4, 101L, 1, 17);
            // create a reviewer
            Resource reviewer = createResource(5, 102L, 1, 18);
            super.insertResources(conn, new Resource[] {resource, reviewer });
            super.insertResourceInfo(conn, reviewer.getId(), 1, "3");

            Upload upload = super.createUpload(1, project.getId(), 4, 1, 1, "parameter");
            super.insertUploads(conn, new Upload[] {upload });

            Submission submission = super.createSubmission(1, upload.getId(), 1, 2);
            super.insertSubmissions(conn, new Submission[] {submission });

            // insert a review
            Scorecard scorecard = createScorecard(1, 1, 2, 6, "name", "1.0", 75.0f, 100.0f);

            Review review = createReview(11, reviewer.getId(), submission.getId(), scorecard.getId(),
                false, 90.0f);

            this.insertScorecards(conn, new Scorecard[] {scorecard });
            this.insertReviews(conn, new Review[] {review });
            assertFalse("canPerform should have returned false", handler.canPerform(phase).isSuccess());
        } finally {
            // cleanTables();
            closeConnection();
        }
    }

    /**
     * Tests the canPerform method with Open statuses. The dependencies are met,
     * specification review exists for submission, and it is committed, the method will
     * return true.
     * @throws Exception
     *             not under test.
     */
    public void testCanPerformWithOpen() throws Exception {
        try {
            cleanTables();

            Project project = super.setupPhasesForSpec(false);
            Phase[] phases = project.getAllPhases();
            Phase phase = phases[1];
            phase.setPhaseStatus(PhaseStatus.OPEN);

            // insert a submission with specification submission
            Connection conn = getConnection();

            // create a register
            Resource resource = createResource(4, 101L, 1, 17);
            // create a reviewer
            Resource reviewer = createResource(5, 102L, 1, 18);
            super.insertResources(conn, new Resource[] {resource, reviewer });
            super.insertResourceInfo(conn, reviewer.getId(), 1, "3");

            Upload upload = super.createUpload(1, project.getId(), 4, 1, 1, "parameter");
            super.insertUploads(conn, new Upload[] {upload });

            Submission submission = super.createSubmission(1, upload.getId(), 1, 2);
            super.insertSubmissions(conn, new Submission[] {submission });

            // insert a review
            Scorecard scorecard = createScorecard(1, 1, 2, 6, "name", "1.0", 75.0f, 100.0f);

            Review review = createReview(11, 5, 1, 1, true, 90.0f);

            this.insertScorecards(conn, new Scorecard[] {scorecard });
            this.insertReviews(conn, new Review[] {review });
            assertTrue("canPerform should have returned true", handler.canPerform(phase).isSuccess());
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
            Phase phase = phases[1];

            // test with scheduled status.
            phase.setPhaseStatus(PhaseStatus.SCHEDULED);

            // insert a submission with specification submission
            Connection conn = getConnection();

            // create a reviewer
            Resource reviewer = createResource(5, 102L, 1, 18);
            super.insertResources(conn, new Resource[] {reviewer });
            super.insertResourceInfo(conn, reviewer.getId(), 1, "3");

            handler.perform(phase, "1001");

            // please manually check the email
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Tests the perform with Scheduled statuses. No reviewer exists for the phase, Verify
     * the content of the email manually.
     * @throws Exception
     *             not under test.
     */
    public void testPerformWithScheduled_ZeroReviewer() throws Exception {
        try {
            cleanTables();

            Project project = super.setupProjectResourcesNotification("All", false, true);
            Phase[] phases = project.getAllPhases();
            Phase phase = phases[1];

            // test with scheduled status.
            phase.setPhaseStatus(PhaseStatus.SCHEDULED);

            handler.perform(phase, "1001");

            // please manually check the email
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Tests the perform with Open statuses. The extra info of comment is invalid,
     * PhaseHandlingException expected.
     * @throws Exception
     *             not under test.
     */
    public void testPerformWithOpen_InvalidCommentExtraInfo() throws Exception {
        try {
            cleanTables();

            Project project = super.setupProjectResourcesNotification("All", false, true);
            Phase[] phases = project.getAllPhases();
            Phase phase = phases[1];

            // test with scheduled status.
            phase.setPhaseStatus(PhaseStatus.OPEN);

            // insert a submission with specification submission
            Connection conn = getConnection();

            // create a register
            Resource resource = createResource(4, 101L, 1, 17);
            // create a reviewer
            Resource reviewer = createResource(5, 102L, 1, 18);
            super.insertResources(conn, new Resource[] {resource, reviewer });
            super.insertResourceInfo(conn, reviewer.getId(), 1, "3");

            Upload upload = super.createUpload(1, project.getId(), 4, 1, 1, "parameter");
            super.insertUploads(conn, new Upload[] {upload });

            Submission submission = super.createSubmission(1, upload.getId(), 1, 2);
            super.insertSubmissions(conn, new Submission[] {submission });

            // insert a review
            Scorecard scorecard = createScorecard(1, 1, 2, 6, "name", "1.0", 75.0f, 100.0f);

            Review review = createReview(11, 5, 1, 1, true, 90.0f);
            // add a rejected comment
            review.addComment(createComment(1111, reviewer.getId(), "Invalid", 14,
                "Specification Review Comment"));

            insertResourceSubmission(conn, resource.getId(), submission.getId());
            this.insertScorecards(conn, new Scorecard[] {scorecard });
            this.insertReviews(conn, new Review[] {review });
            insertCommentsWithExtraInfo(conn, new long[] {1 }, new long[] {reviewer.getId() },
                new long[] {review.getId() }, new String[] {"Invalid Comment" }, new long[] {14 },
                new String[] {"Invalid" });

            handler.perform(phase, "1001");
            fail("should have thrown PhaseHandlingException");
        } catch (PhaseHandlingException e) {
            // pass
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Tests the perform with Open statuses. No specification review comment,
     * PhaseHandlingException expected.
     * @throws Exception
     *             not under test.
     */
    public void testPerformWithOpen_NoSpecificationReviewComment() throws Exception {
        try {
            cleanTables();

            Project project = super.setupProjectResourcesNotification("All", false, true);
            Phase[] phases = project.getAllPhases();
            Phase phase = phases[1];

            // test with scheduled status.
            phase.setPhaseStatus(PhaseStatus.OPEN);

            // insert a submission with specification submission
            Connection conn = getConnection();

            // create a register
            Resource resource = createResource(4, 101L, 1, 17);
            // create a reviewer
            Resource reviewer = createResource(5, 102L, 1, 18);
            super.insertResources(conn, new Resource[] {resource, reviewer });
            super.insertResourceInfo(conn, reviewer.getId(), 1, "3");

            Upload upload = super.createUpload(1, project.getId(), 4, 1, 1, "parameter");
            super.insertUploads(conn, new Upload[] {upload });

            Submission submission = super.createSubmission(1, upload.getId(), 1, 2);
            super.insertSubmissions(conn, new Submission[] {submission });

            // insert a review
            Scorecard scorecard = createScorecard(1, 1, 2, 6, "name", "1.0", 75.0f, 100.0f);

            Review review = createReview(11, 5, 1, 1, true, 90.0f);

            insertResourceSubmission(conn, resource.getId(), submission.getId());
            this.insertScorecards(conn, new Scorecard[] {scorecard });
            this.insertReviews(conn, new Review[] {review });

            handler.perform(phase, "1001");
            fail("should have thrown PhaseHandlingException");
        } catch (PhaseHandlingException e) {
            // pass
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Tests the perform with Open statuses. No submission exists for the phase,
     * PhaseHandlingException expected.
     * @throws Exception
     *             not under test.
     */
    public void testPerformWithOpen_NoSubmission() throws Exception {
        try {
            cleanTables();

            Project project = super.setupProjectResourcesNotification("All", false, true);
            Phase[] phases = project.getAllPhases();
            Phase phase = phases[1];

            // test with scheduled status.
            phase.setPhaseStatus(PhaseStatus.OPEN);

            // insert a submission with specification submission
            Connection conn = getConnection();

            // create a register
            Resource resource = createResource(4, 101L, 1, 17);
            super.insertResources(conn, new Resource[] {resource });

            // create a reviewer
            Resource reviewer = createResource(5, 102L, 1, 18);
            super.insertResources(conn, new Resource[] {reviewer });
            super.insertResourceInfo(conn, reviewer.getId(), 1, "3");

            handler.perform(phase, "1001");
            fail("should have thrown PhaseHandlingException");
        } catch (PhaseHandlingException e) {
            // pass
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Tests the perform with Open statuses. No review exists for the submission,
     * PhaseHandlingException expected.
     * @throws Exception
     *             not under test.
     */
    public void testPerformWithOpen_NoReview() throws Exception {
        try {
            cleanTables();

            Project project = super.setupProjectResourcesNotification("All", false, true);
            Phase[] phases = project.getAllPhases();
            Phase phase = phases[1];

            // test with scheduled status.
            phase.setPhaseStatus(PhaseStatus.OPEN);

            // insert a submission with specification submission
            Connection conn = getConnection();

            // create a register
            Resource resource = createResource(4, 101L, 1, 17);
            // create a reviewer
            Resource reviewer = createResource(5, 102L, 1, 18);
            super.insertResources(conn, new Resource[] {resource, reviewer });
            super.insertResourceInfo(conn, reviewer.getId(), 1, "3");

            Upload upload = super.createUpload(1, project.getId(), 4, 1, 1, "parameter");
            super.insertUploads(conn, new Upload[] {upload });

            Submission submission = super.createSubmission(1, upload.getId(), 1, 2);
            super.insertSubmissions(conn, new Submission[] {submission });

            insertResourceSubmission(conn, resource.getId(), submission.getId());

            handler.perform(phase, "1001");
            fail("should have thrown PhaseHandlingException");
        } catch (PhaseHandlingException e) {
            // pass
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Tests the perform with Open statuses. The review is not committed,
     * PhaseHandlingException expected.
     * @throws Exception
     *             not under test.
     */
    public void testPerformWithOpen_ReviewNotCommitted() throws Exception {
        try {
            cleanTables();

            Project project = super.setupProjectResourcesNotification("All", false, true);
            Phase[] phases = project.getAllPhases();
            Phase phase = phases[1];

            // test with scheduled status.
            phase.setPhaseStatus(PhaseStatus.OPEN);

            // insert a submission with specification submission
            Connection conn = getConnection();

            // create a register
            Resource resource = createResource(4, 101L, 1, 17);
            // create a reviewer
            Resource reviewer = createResource(5, 102L, 1, 18);
            super.insertResources(conn, new Resource[] {resource, reviewer });
            super.insertResourceInfo(conn, reviewer.getId(), 1, "3");

            Upload upload = super.createUpload(1, project.getId(), 4, 1, 1, "parameter");
            super.insertUploads(conn, new Upload[] {upload });

            Submission submission = super.createSubmission(1, upload.getId(), 1, 2);
            super.insertSubmissions(conn, new Submission[] {submission });

            // insert a review
            Scorecard scorecard = createScorecard(1, 1, 2, 6, "name", "1.0", 75.0f, 100.0f);

            Review review = createReview(11, 5, 1, 1, false, 90.0f);
            // add a rejected comment
            review.addComment(createComment(1111, reviewer.getId(), "Rejected", 14,
                "Specification Review Comment"));

            insertResourceSubmission(conn, resource.getId(), submission.getId());
            this.insertScorecards(conn, new Scorecard[] {scorecard });
            this.insertReviews(conn, new Review[] {review });
            insertCommentsWithExtraInfo(conn, new long[] {1 }, new long[] {reviewer.getId() },
                new long[] {review.getId() }, new String[] {"Rejected Comment" }, new long[] {14 },
                new String[] {"Rejected" });

            handler.perform(phase, "1001");
            fail("should have thrown PhaseHandlingException");
        } catch (PhaseHandlingException e) {
            // pass
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Tests the perform with Open statuses. The specification review is approved, approve
     * email should be sent.
     * @throws Exception
     *             not under test.
     */
    public void testPerformWithOpen_SpecificationReviewApproved() throws Exception {
        try {
            cleanTables();

            Project project = super.setupProjectResourcesNotification("All", false, true);
            Phase[] phases = project.getAllPhases();
            Phase phase = phases[1];

            // test with scheduled status.
            phase.setPhaseStatus(PhaseStatus.OPEN);

            // insert a submission with specification submission
            Connection conn = getConnection();

            // create a register
            Resource resource = createResource(4, 101L, 1, 17);
            // create a reviewer
            Resource reviewer = createResource(5, 102L, 1, 18);
            super.insertResources(conn, new Resource[] {resource, reviewer });
            super.insertResourceInfo(conn, reviewer.getId(), 1, "3");

            Upload upload = super.createUpload(1, project.getId(), 4, 1, 1, "parameter");
            super.insertUploads(conn, new Upload[] {upload });

            Submission submission = super.createSubmission(1, upload.getId(), 1, 2);
            super.insertSubmissions(conn, new Submission[] {submission });

            // insert a review
            Scorecard scorecard = createScorecard(1, 1, 2, 6, "name", "1.0", 75.0f, 100.0f);

            Review review = createReview(11, 5, 1, 1, true, 90.0f);
            // add a rejected comment
            review.addComment(createComment(1111, reviewer.getId(), "Approved", 14,
                "Specification Review Comment"));

            insertResourceSubmission(conn, resource.getId(), submission.getId());
            this.insertScorecards(conn, new Scorecard[] {scorecard });
            this.insertReviews(conn, new Review[] {review });
            insertCommentsWithExtraInfo(conn, new long[] {1 }, new long[] {reviewer.getId() },
                new long[] {review.getId() }, new String[] {"Approved Comment" }, new long[] {14 },
                new String[] {"Approved" });

            handler.perform(phase, "1001");

            // please manually check the email
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Tests the perform with Open statuses. The specification review is rejected,
     * rejected email should be sent.
     * @throws Exception
     *             not under test.
     */
    public void testPerformWithOpen_SpecificationReviewRejected() throws Exception {
        try {
            cleanTables();

            Project project = super.setupProjectResourcesNotification("All", false, true);
            Phase[] phases = project.getAllPhases();
            Phase phase = phases[1];

            // test with scheduled status.
            phase.setPhaseStatus(PhaseStatus.OPEN);

            // insert a submission with specification submission
            Connection conn = getConnection();

            // create a register
            Resource resource = createResource(110, 101L, 1, 17);
            // create a reviewer
            Resource reviewer = createResource(111, 102L, 1, 18);
            super.insertResources(conn, new Resource[] {resource, reviewer });
            super.insertResourceInfo(conn, reviewer.getId(), 1, "3");

            Upload upload = super.createUpload(1, project.getId(), 110, 1, 1, "parameter");
            super.insertUploads(conn, new Upload[] {upload });

            Submission submission = super.createSubmission(1, upload.getId(), 1, 2);
            super.insertSubmissions(conn, new Submission[] {submission });

            // insert a review
            Scorecard scorecard = createScorecard(1, 1, 2, 6, "name", "1.0", 75.0f, 100.0f);

            Review review = createReview(11, 111, 1, 1, true, 90.0f);
            // add a rejected comment
            review.addComment(createComment(1111, reviewer.getId(), "Rejected", 14,
                "Specification Review Comment"));

            insertResourceSubmission(conn, resource.getId(), submission.getId());
            this.insertScorecards(conn, new Scorecard[] {scorecard });
            this.insertReviews(conn, new Review[] {review });
            insertCommentsWithExtraInfo(conn, new long[] {1 }, new long[] {reviewer.getId() },
                new long[] {review.getId() }, new String[] {"Rejected Comment" }, new long[] {14 },
                new String[] {"Rejected" });

            handler.perform(phase, "1001");

            // check the submission status
            assertEquals("submission status should be updated to 'Failed Review'", "Failed Review",
                super.getSubmissionStatus(conn, submission.getId()));

            // please manually check the email
        } finally {
            cleanTables();
            closeConnection();
        }
    }
}
