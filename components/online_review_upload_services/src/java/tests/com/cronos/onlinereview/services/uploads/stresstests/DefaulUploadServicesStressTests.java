/*
 * Copyright (C) 2007-2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.services.uploads.stresstests;

import java.util.Date;

import junit.framework.TestCase;

import com.cronos.onlinereview.services.uploads.ManagersProvider;
import com.cronos.onlinereview.services.uploads.TestHelper;
import com.cronos.onlinereview.services.uploads.impl.DefaultUploadServices;
import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.deliverable.Upload;

/**
 * <p>
 * This is the stress test fixture for <code>DefaultUploadServices</code> class.
 * The thread-safety is totally depending on the manager implementation, so it is the responsibility
 * of the manager implementors to write thread-safety test.
 * <br/>
 *
 * 1.1 Changes:<br/>
 * Added tests for uploadSpecification.<br/>
 * Fixed the tests from 1.0.
 * </p>
 *
 * @author Thinfox, moon.river
 * @version 1.1
 * @since 1.0
 */
public class DefaulUploadServicesStressTests extends TestCase {
    /**
     * The instance of <code>DefaultUploadServices</code> on which to perform tests.
     */
    private DefaultUploadServices services;

    /**
     * Represents the mock upload manager.
     */
    private MockUploadManager uploadManager;

    /**
     * Represents the mock screening manager.
     */
    private MockScreeningManager screeningManager;

    /**
     * Sets up the test environment.
     * @throws Exception to JUnit.
     */
    protected void setUp() throws Exception {
        super.setUp();
        StressTestHelper.clearConfig();
        StressTestHelper.loadConfig("stress.xml");
        StressTestHelper.loadConfig("com.topcoder.util.log", "logging.xml");

        services = new DefaultUploadServices();

        ManagersProvider managersProvider = (ManagersProvider) TestHelper.getFieldValue(services,
            "managersProvider");
        uploadManager = (MockUploadManager) managersProvider.getUploadManager();
        screeningManager = (MockScreeningManager) managersProvider.getScreeningManager();
    }

    /**
     * Cleans up the test environment.
     * @throws Exception to JUnit.
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        StressTestHelper.clearConfig();
    }

    /**
     * <p>
     * Test method for <code>uploadSubmission</code>.
     * </p>
     * @throws Exception Exception to JUnit.
     * @since 1.0
     */
    public void testUploadSubmission() throws Exception {

        Date startTime = new Date();
        for (int i = 0; i < StressTestHelper.TOTOL; ++i) {
            services.uploadSubmission(StressTestHelper.PROJECT_ID, StressTestHelper.USER_ID,
                    StressTestHelper.FILE);
        }
        Date endTime = new Date();

        System.out.println("Executing uploadSubmission for " + StressTestHelper.TOTOL + " times takes "
            + (endTime.getTime() - startTime.getTime()) + " milliseconds");

        // add the check for submission type V1.1
        Submission submission = uploadManager.getCreatedSubmission();
        assertEquals("Wrong type", submission.getSubmissionType().getName(), "Contest Submission");

        // check other results V1.1
        // check the upload
        Upload upload = uploadManager.getCreatedUpload();

        assertEquals("Wrong type", "Submission", upload.getUploadType().getName());
        assertEquals("Wrong status", "Active", upload.getUploadStatus().getName());
        assertEquals("Wrong project id", StressTestHelper.PROJECT_ID, upload.getProject());
        assertEquals("Wrong file name", StressTestHelper.FILE, upload.getParameter());
        assertEquals("Wrong user id", String.valueOf(StressTestHelper.USER_ID), uploadManager.getCreatedUploadUserId());

        // check the submission
        assertEquals("Wrong status", submission.getSubmissionStatus().getName(), "Active");
        assertEquals("Wrong user id",
                String.valueOf(StressTestHelper.USER_ID), uploadManager.getCreatedSubmissionUserId());
    }

    /**
     * <p>
     * Test method for <code>uploadFinalFix</code>.
     * </p>
     * @throws Exception Exception to JUnit.
     */
    public void testUploadFinalFix() throws Exception {
        Date startTime = new Date();
        for (int i = 0; i < StressTestHelper.TOTOL; ++i) {
            services.uploadFinalFix(StressTestHelper.PROJECT_ID, StressTestHelper.USER_ID, StressTestHelper.FILE);
        }

        Date endTime = new Date();

        System.out.println("Executing uploadFinalFix for " + StressTestHelper.TOTOL + " times takes "
            + (endTime.getTime() - startTime.getTime()) + " milliseconds");

        // check the upload
        Upload upload = uploadManager.getCreatedUpload();
        assertEquals("Wrong type", "Final Fix", upload.getUploadType().getName());
        assertEquals("Wrong status", "Active", upload.getUploadStatus().getName());
        assertEquals("Wrong project id", StressTestHelper.PROJECT_ID, upload.getProject());
        assertEquals("Wrong file name", StressTestHelper.FILE, upload.getParameter());
        assertEquals("Wrong user id", String.valueOf(StressTestHelper.USER_ID), uploadManager.getCreatedUploadUserId());

        // verify screening
        assertEquals("Screening should not be initiate", -1, screeningManager.getSubmissionId());

        // verify previous submissions
        Upload updatedUpload = uploadManager.getUpdatedUpload();
        assertEquals("Previous upload should be deleted", updatedUpload.getUploadStatus().getName(), "Deleted");
}

    /**
     * <p>
     * Test method for <code>uploadTestCases</code>.
     * </p>
     * @throws Exception Exception to JUnit.
     */
    public void testUploadTestCases() throws Exception {
        Date startTime = new Date();
        for (int i = 0; i < StressTestHelper.TOTOL; ++i) {
            services.uploadTestCases(StressTestHelper.PROJECT_ID, StressTestHelper.USER_ID, StressTestHelper.FILE);
        }

        Date endTime = new Date();

        System.out.println("Executing uploadTestCases for " + StressTestHelper.TOTOL + " times takes "
            + (endTime.getTime() - startTime.getTime()) + " milliseconds");

        // check the upload
        Upload upload = uploadManager.getCreatedUpload();

        assertEquals("Wrong type", "Test Case", upload.getUploadType().getName());
        assertEquals("Wrong status", "Active", upload.getUploadStatus().getName());
        assertEquals("Wrong project id", StressTestHelper.PROJECT_ID, upload.getProject());
        assertEquals("Wrong file name", StressTestHelper.FILE, upload.getParameter());
        assertEquals("Wrong user id", String.valueOf(StressTestHelper.USER_ID), uploadManager.getCreatedUploadUserId());
    }

    /**
     * <p>
     * Test method for <code>setSubmissionStatus</code>.
     * </p>
     * @throws Exception Exception to JUnit.
     */
    public void testSetSubmissionStatus() throws Exception {
        Date startTime = new Date();
        for (int i = 0; i < StressTestHelper.TOTOL; ++i) {
            services.setSubmissionStatus(StressTestHelper.TOTOL + i, 1, "tc");
        }

        Date endTime = new Date();

        System.out.println("Executing setSubmissionStatus for " + StressTestHelper.TOTOL + " times takes "
            + (endTime.getTime() - startTime.getTime()) + " milliseconds");

        // check the result
        Submission submission = uploadManager.updatedSubmission;
        assertNotNull("The submission is not updated.", submission);
        assertEquals("Wrong status.", "Active", submission.getSubmissionStatus().getName());
        assertEquals("Wrong user.", "tc", uploadManager.updatedSubmissionUserId);
    }

    /**
     * <p>
     * Stress test of
     * <code>{@link DefaultUploadServices#uploadSpecification(long projectId, long userId, String filename)}</code>
     * method.
     *
     * This is not thread safe so no thread-safety test. Also there is no specific
     * </p>
     *
     * @throws Exception if any error occurs
     */
    public void testUploadSpecification() throws Exception {

        long start = System.currentTimeMillis();
        for (int i = 0; i < StressTestHelper.TOTOL; i++) {
            services.uploadSpecification(StressTestHelper.PROJECT_ID, StressTestHelper.USER_ID, StressTestHelper.FILE);
        }
        long end = System.currentTimeMillis();
        System.out.println("Run uploadSpecification took " + (end - start) + "ms");

        // check the upload
        Upload upload = uploadManager.getCreatedUpload();

        assertEquals("Wrong type", upload.getUploadType().getName(), "Submission");
        assertEquals("Wrong status", upload.getUploadStatus().getName(), "Active");
        assertEquals("Wrong project id", upload.getProject(), StressTestHelper.PROJECT_ID);
        assertEquals("Wrong file name", upload.getParameter(), StressTestHelper.FILE);
        assertEquals("Wrong user id", String.valueOf(StressTestHelper.USER_ID), uploadManager.getCreatedUploadUserId());

        // check the submission
        Submission submission = uploadManager.getCreatedSubmission();
        assertEquals("Wrong status", submission.getSubmissionStatus().getName(), "Active");
        assertEquals("Wrong type", submission.getSubmissionType().getName(), "Specification Submission");
        assertEquals("Wrong user id",
                String.valueOf(StressTestHelper.USER_ID), uploadManager.getCreatedSubmissionUserId());
    }
}
