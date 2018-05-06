/*
 * Copyright (C) 2007-2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.services.uploads.stresstests;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Date;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;

import junit.framework.TestCase;

import com.cronos.onlinereview.services.uploads.ManagersProvider;
import com.cronos.onlinereview.services.uploads.TestHelper;
import com.cronos.onlinereview.services.uploads.impl.DefaultUploadExternalServices;
import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.deliverable.Upload;

/**
 * <p>
 * This is the stress test fixture for <code>DefaultUploadExternalServices</code> class.
 * The thread-safety is totally depending on the manager implementation, so it is the responsibility
 * of the manager implementors to write thread-safety test.
 * <br/>
 *
 * 1.1 Changes:<br/>
 * Add tests for uploadSpecification.<br/>
 * Fix version 1.0.
 * </p>
 *
 * @author Thinfox, moon.river
 * @version 1.1
 * @since 1.0
 */
public class DefaulUploadExternalServicesStressTests extends TestCase {
    /**
     * The instance of <code>DefaultUploadExternalServices</code> on which to perform tests.
     */
    private DefaultUploadExternalServices services;

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
        services = new DefaultUploadExternalServices();

        ManagersProvider managersProvider = (ManagersProvider) TestHelper.getFieldValue(TestHelper.getFieldValue(services,
            "uploadServices"), "managersProvider");
        uploadManager = (MockUploadManager) managersProvider.getUploadManager();
        screeningManager = (MockScreeningManager) managersProvider.getScreeningManager();

        // create a big file
        File file = new File("test_files/stress.jar");
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        for (int i = 0; i < 1000; i++) {
            for (int j = 0; j < 1000; j++) {
                bw.write("111111111111111111111111111111111111111111111");
            }
        }
        bw.flush();
        bw.close();

    }

    /**
     * Cleans up the test environment.
     * @throws Exception to JUnit.
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        clearFiles();
        StressTestHelper.clearConfig();
    }

    /**
     * <p>
     * Test method for <code>uploadSubmission</code>.
     * </p>
     * @throws Exception Exception to JUnit.
     */
    public void testUploadSubmission() throws Exception {
        FileDataSource dataSource = new FileDataSource("test_files/stress.jar");
        DataHandler dataHandler = new DataHandler(dataSource);

        Date startTime = new Date();
        for (int i = 0; i < 10; ++i) {
            services.uploadSubmission(StressTestHelper.PROJECT_ID, StressTestHelper.USER_ID, "submission.jar", dataHandler);
        }

        Date endTime = new Date();

        System.out.println("Executing uploadSubmission for 10 times takes "
            + (endTime.getTime() - startTime.getTime()) + " milliseconds");

        Upload upload = uploadManager.getCreatedUpload();

        assertEquals("Wrong type", "Submission", upload.getUploadType().getName());
        assertEquals("Wrong status", "Active", upload.getUploadStatus().getName());
        assertEquals("Wrong project id", StressTestHelper.PROJECT_ID, upload.getProject());
        assertTrue("Wrong file name", upload.getParameter().startsWith("submission"));
        assertTrue("Wrong file name", upload.getParameter().endsWith("jar"));
        assertEquals("Wrong user id", String.valueOf(StressTestHelper.USER_ID), uploadManager.getCreatedUploadUserId());

        // check the submission
        Submission submission = uploadManager.getCreatedSubmission();
        assertEquals("Wrong type", submission.getSubmissionType().getName(), "Contest Submission");
        assertEquals("Wrong status", submission.getSubmissionStatus().getName(), "Active");
        assertEquals("Wrong user id",
                String.valueOf(StressTestHelper.USER_ID), uploadManager.getCreatedSubmissionUserId());

        File uploadedDir = new File("test_files/upload/");
        File uploaded = uploadedDir.listFiles()[0];
        assertEquals("Failed to upload submission", new File("test_files/stress.jar").length(), uploaded.length());
    }

    /**
     * <p>
     * Test method for <code>uploadFinalFix</code>.
     * </p>
     * @throws Exception Exception to JUnit.
     */
    public void testUploadFinalFix() throws Exception {
        FileDataSource dataSource = new FileDataSource("test_files/stress.jar");
        DataHandler dataHandler = new DataHandler(dataSource);

        Date startTime = new Date();
        for (int i = 0; i < 10; ++i) {
            services.uploadFinalFix(StressTestHelper.PROJECT_ID, StressTestHelper.USER_ID, "finalfix.jar", dataHandler);
        }

        Date endTime = new Date();

        System.out.println("Executing uploadFinalFix for 10 times takes "
            + (endTime.getTime() - startTime.getTime()) + " milliseconds");

        // check the upload
        Upload upload = uploadManager.getCreatedUpload();
        assertEquals("Wrong type", "Final Fix", upload.getUploadType().getName());
        assertEquals("Wrong status", "Active", upload.getUploadStatus().getName());
        assertEquals("Wrong project id", StressTestHelper.PROJECT_ID, upload.getProject());
        assertTrue("Wrong file name", upload.getParameter().startsWith("submission"));
        assertTrue("Wrong file name", upload.getParameter().endsWith("jar"));
        assertEquals("Wrong user id", String.valueOf(StressTestHelper.USER_ID), uploadManager.getCreatedUploadUserId());

        // verify screening
        assertEquals("Screening should not be initiate", -1, screeningManager.getSubmissionId());

        // verify previous submissions
        Upload updatedUpload = uploadManager.getUpdatedUpload();
        assertEquals("Previous upload should be deleted", updatedUpload.getUploadStatus().getName(), "Deleted");


        File uploadedDir = new File("test_files/upload/");
        File uploaded = uploadedDir.listFiles()[0];
        assertEquals("Failed to upload submission", new File("test_files/stress.jar").length(), uploaded.length());
    }

    /**
     * <p>
     * Test method for <code>uploadTestCases</code>.
     * </p>
     * @throws Exception Exception to JUnit.
     */
    public void testUploadTestCases() throws Exception {
        FileDataSource dataSource = new FileDataSource("test_files/stress.jar");
        DataHandler dataHandler = new DataHandler(dataSource);

        Date startTime = new Date();
        for (int i = 0; i < 10; ++i) {
            services.uploadTestCases(StressTestHelper.PROJECT_ID, StressTestHelper.USER_ID,
                    "testcases.jar", dataHandler);
        }

        Date endTime = new Date();

        System.out.println("Executing uploadTestCases for 10 times takes "
            + (endTime.getTime() - startTime.getTime()) + " milliseconds");

        // check the result
        Upload upload = uploadManager.getCreatedUpload();

        assertEquals("Wrong type", "Test Case", upload.getUploadType().getName());
        assertEquals("Wrong status", "Active", upload.getUploadStatus().getName());
        assertEquals("Wrong project id", StressTestHelper.PROJECT_ID, upload.getProject());
        assertTrue("Wrong file name", upload.getParameter().startsWith("submission"));
        assertTrue("Wrong file name", upload.getParameter().endsWith("jar"));
        assertEquals("Wrong user id", String.valueOf(StressTestHelper.USER_ID), uploadManager.getCreatedUploadUserId());

        File uploadedDir = new File("test_files/upload/");
        File uploaded = uploadedDir.listFiles()[0];
        assertEquals("Failed to upload submission", new File("test_files/stress.jar").length(), uploaded.length());
    }

    /**
     * <p>
     * Test method for <code>setSubmissionStatus</code>.
     * </p>
     * @throws Exception Exception to JUnit.
     */
    public void testSetSubmissionStatus() throws Exception {
        Date startTime = new Date();
        for (int i = 0; i < 100; ++i) {
            services.setSubmissionStatus(100 + i, 1, "tc");
        }

        Date endTime = new Date();

        System.out.println("Executing setSubmissionStatus for 100 times takes "
            + (endTime.getTime() - startTime.getTime()) + " milliseconds");

    }

    /**
     * Clear all the files created using tesing.
     */
    private void clearFiles() {
        File file = new File("test_files/upload");
        File[] files = file.listFiles();
        for (File delFile : files) {
            delFile.delete();
        }
        file = new File("test_files/stress.jar");
        file.delete();
    }

    /**
     * <p>
     * Test method for <code>uploadSpecification</code>.
     * </p>
     * @throws Exception Exception to JUnit.
     */
    public void testUploadSpecification() throws Exception {
        FileDataSource dataSource = new FileDataSource("test_files/stress.jar");
        DataHandler dataHandler = new DataHandler(dataSource);

        Date startTime = new Date();
        services.uploadSpecification(StressTestHelper.PROJECT_ID,
                    StressTestHelper.USER_ID, "submission.jar", dataHandler);

        Date endTime = new Date();

        System.out.println("Run uploadSpecification for a big file takes "
            + (endTime.getTime() - startTime.getTime()) + " milliseconds");

        // check the result
        File uploadedDir = new File("test_files/upload/");
        File uploaded = uploadedDir.listFiles()[0];
        assertEquals("Failed to upload submission", new File("test_files/stress.jar").length(), uploaded.length());
    }
}
