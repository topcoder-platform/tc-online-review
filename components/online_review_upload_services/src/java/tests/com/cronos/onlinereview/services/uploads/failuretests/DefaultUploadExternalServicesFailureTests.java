/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.services.uploads.failuretests;

import com.cronos.onlinereview.services.uploads.ConfigurationException;
import com.cronos.onlinereview.services.uploads.InvalidProjectException;
import com.cronos.onlinereview.services.uploads.InvalidUserException;
import com.cronos.onlinereview.services.uploads.impl.DefaultUploadExternalServices;
import com.cronos.onlinereview.services.uploads.impl.DefaultUploadServices;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.File;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;


/**
 * <p>Failure test cases for DefaultUploadExternalServices.</p>
 *
 * @author victorsam, onsky
 * @version 1.0
 *
 * @since 1.0
 */
public class DefaultUploadExternalServicesFailureTests extends TestCase {
    /**
     * <p>The DefaultUploadExternalServices instance used for testing.</p>
     */
    private DefaultUploadExternalServices services;

    /**
     * <p>The DataHandler instance used for testing.</p>
     */
    private DataHandler dataHandler;

    /**
     * <p>Setup test environment.</p>
     *
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        FailureTestHelper.loadXMLConfig(FailureTestHelper.CONFIG_FILE);
        FailureTestHelper.loadXMLConfig("com.topcoder.util.log", FailureTestHelper.TEST_FILES + "logging.xml");

        services = new DefaultUploadExternalServices();

        FileDataSource dataSource = new FileDataSource("test_files/failuretests/config.zip");
        dataHandler = new DataHandler(dataSource);
    }

    /**
     * <p>Tears down test environment.</p>
     *
     * @throws Exception to JUnit
     */
    protected void tearDown() throws Exception {
        dataHandler = null;
        services = null;

        FailureTestHelper.clearConfig();
    }

    /**
     * <p>Return all tests.</p>
     *
     * @return all tests
     */
    public static Test suite() {
        return new TestSuite(DefaultUploadExternalServicesFailureTests.class);
    }

    /**
     * <p>Tests ctor DefaultUploadExternalServices#DefaultUploadExternalServices(String) for failure. It tests
     * the case that when namespace is null and expects IllegalArgumentException.</p>
     *
     * @throws Exception to JUnit
     */
    public void testCtor_NullNamespace() throws Exception {
        try {
            new DefaultUploadExternalServices(null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>Tests ctor DefaultUploadExternalServices#DefaultUploadExternalServices(String) for failure. It tests
     * the case that when namespace is empty and expects IllegalArgumentException.</p>
     *
     * @throws Exception to JUnit
     */
    public void testCtor_EmptyNamespace() throws Exception {
        try {
            new DefaultUploadExternalServices(" ");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>Tests ctor DefaultUploadExternalServices#DefaultUploadExternalServices(String) for failure. Expects
     * for ConfigurationException.</p>
     */
    public void testCtor1_UnknownNamespace() {
        try {
            new DefaultUploadExternalServices("UnknowNamespace");
            fail("ConfigurationException expected.");
        } catch (ConfigurationException e) {
            //good
        }
    }

    /**
     * <p>Tests ctor DefaultUploadExternalServices#DefaultUploadExternalServices(String) for failure. Expects
     * for ConfigurationException.</p>
     */
    public void testCtor1_FileStorageLocationMissing() {
        try {
            new DefaultUploadExternalServices("fileStorageLocation_missing");
            fail("ConfigurationException expected.");
        } catch (ConfigurationException e) {
            //good
        }
    }

    /**
     * <p>Tests ctor DefaultUploadExternalServices#DefaultUploadExternalServices(String) for failure. Expects
     * for ConfigurationException.</p>
     */
    public void testCtor1_FileStorageLocationEmpty() {
        try {
            new DefaultUploadExternalServices("fileStorageLocation_empty");
            fail("ConfigurationException expected.");
        } catch (ConfigurationException e) {
            //good
        }
    }

    /**
     * <p>Tests ctor DefaultUploadExternalServices#DefaultUploadExternalServices(UploadServices,String,String)
     * for failure. It tests the case that when uploadServices is null and expects IllegalArgumentException.</p>
     *
     * @throws Exception to JUnit
     */
    public void testCtor_NullUploadServices() throws Exception {
        try {
            new DefaultUploadExternalServices(null, "filenamePattern", "fileStorageLocation");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>Tests ctor DefaultUploadExternalServices#DefaultUploadExternalServices(UploadServices,String,String)
     * for failure. It tests the case that when fileStorageLocation is null and expects IllegalArgumentException.</p>
     *
     * @throws Exception to JUnit
     */
    public void testCtor_NullFileStorageLocation() throws Exception {
        try {
            new DefaultUploadExternalServices(new DefaultUploadServices(), "filenamePattern", null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>Tests ctor DefaultUploadExternalServices#DefaultUploadExternalServices(UploadServices,String,String)
     * for failure. It tests the case that when fileStorageLocation is empty and expects IllegalArgumentException.</p>
     *
     * @throws Exception to JUnit
     */
    public void testCtor_EmptyFileStorageLocation() throws Exception {
        try {
            new DefaultUploadExternalServices(new DefaultUploadServices(), "filenamePattern", " ");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>Tests DefaultUploadExternalServices#uploadSubmission(long,long,String,DataHandler) for failure. It
     * tests the case that when projectId is negative and expects IllegalArgumentException.</p>
     *
     * @throws Exception to JUnit
     */
    public void testUploadSubmission_NegativeProjectId()
        throws Exception {
        try {
            services.uploadSubmission(-1, 1, "filename", this.dataHandler);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>Tests DefaultUploadExternalServices#uploadSubmission(long,long,String,DataHandler) for failure. It
     * tests the case that when userId is negative and expects IllegalArgumentException.</p>
     *
     * @throws Exception to JUnit
     */
    public void testUploadSubmission_NegativeUserId() throws Exception {
        try {
            services.uploadSubmission(1, -1, "filename", this.dataHandler);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>Tests DefaultUploadExternalServices#uploadSubmission(long,long,String,DataHandler) for failure. It
     * tests the case that when filename is null and expects IllegalArgumentException.</p>
     *
     * @throws Exception to JUnit
     */
    public void testUploadSubmission_NullFilename() throws Exception {
        try {
            services.uploadSubmission(1, 1, null, this.dataHandler);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>Tests DefaultUploadExternalServices#uploadSubmission(long,long,String,DataHandler) for failure. It
     * tests the case that when filename is empty and expects IllegalArgumentException.</p>
     *
     * @throws Exception to JUnit
     */
    public void testUploadSubmission_EmptyFilename() throws Exception {
        try {
            services.uploadSubmission(1, 1, " ", this.dataHandler);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>Tests DefaultUploadExternalServices#uploadSubmission(long,long,String,DataHandler) for failure. It
     * tests the case that when submission is null and expects IllegalArgumentException.</p>
     *
     * @throws Exception to JUnit
     */
    public void testUploadSubmission_NullSubmission() throws Exception {
        try {
            services.uploadSubmission(1, 1, "filename", null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>Tests DefaultUploadExternalServices#uploadFinalFix(long,long,String,DataHandler) for failure. It
     * tests the case that when projectId is negative and expects IllegalArgumentException.</p>
     *
     * @throws Exception to JUnit
     */
    public void testUploadFinalFix_NegativeProjectId()
        throws Exception {
        try {
            services.uploadFinalFix(-1, 1, "filename", this.dataHandler);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>Tests DefaultUploadExternalServices#uploadFinalFix(long,long,String,DataHandler) for failure. It
     * tests the case that when userId is negative and expects IllegalArgumentException.</p>
     *
     * @throws Exception to JUnit
     */
    public void testUploadFinalFix_NegativeUserId() throws Exception {
        try {
            services.uploadFinalFix(1, -1, "filename", this.dataHandler);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>Tests DefaultUploadExternalServices#uploadFinalFix(long,long,String,DataHandler) for failure. It
     * tests the case that when filename is null and expects IllegalArgumentException.</p>
     *
     * @throws Exception to JUnit
     */
    public void testUploadFinalFix_NullFilename() throws Exception {
        try {
            services.uploadFinalFix(1, 1, null, this.dataHandler);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>Tests DefaultUploadExternalServices#uploadFinalFix(long,long,String,DataHandler) for failure. It
     * tests the case that when filename is empty and expects IllegalArgumentException.</p>
     *
     * @throws Exception to JUnit
     */
    public void testUploadFinalFix_EmptyFilename() throws Exception {
        try {
            services.uploadFinalFix(1, 1, " ", this.dataHandler);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>Tests DefaultUploadExternalServices#uploadFinalFix(long,long,String,DataHandler) for failure. It
     * tests the case that when finalFix is null and expects IllegalArgumentException.</p>
     *
     * @throws Exception to JUnit
     */
    public void testUploadFinalFix_NullFinalFix() throws Exception {
        try {
            services.uploadFinalFix(1, 1, "filename", null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>Tests DefaultUploadExternalServices#uploadTestCases(long,long,String,DataHandler) for failure. It
     * tests the case that when projectId is negative and expects IllegalArgumentException.</p>
     *
     * @throws Exception to JUnit
     */
    public void testUploadTestCases_NegativeProjectId()
        throws Exception {
        try {
            services.uploadTestCases(-1, 1, "filename", this.dataHandler);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>Tests DefaultUploadExternalServices#uploadTestCases(long,long,String,DataHandler) for failure. It
     * tests the case that when userId is negative and expects IllegalArgumentException.</p>
     *
     * @throws Exception to JUnit
     */
    public void testUploadTestCases_NegativeUserId() throws Exception {
        try {
            services.uploadTestCases(1, -1, "filename", this.dataHandler);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>Tests DefaultUploadExternalServices#uploadTestCases(long,long,String,DataHandler) for failure. It
     * tests the case that when filename is null and expects IllegalArgumentException.</p>
     *
     * @throws Exception to JUnit
     */
    public void testUploadTestCases_NullFilename() throws Exception {
        try {
            services.uploadTestCases(1, 1, null, this.dataHandler);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>Tests DefaultUploadExternalServices#uploadTestCases(long,long,String,DataHandler) for failure. It
     * tests the case that when filename is empty and expects IllegalArgumentException.</p>
     *
     * @throws Exception to JUnit
     */
    public void testUploadTestCases_EmptyFilename() throws Exception {
        try {
            services.uploadTestCases(1, 1, " ", this.dataHandler);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>Tests DefaultUploadExternalServices#uploadTestCases(long,long,String,DataHandler) for failure. It
     * tests the case that when testCases is null and expects IllegalArgumentException.</p>
     *
     * @throws Exception to JUnit
     */
    public void testUploadTestCases_NullTestCases() throws Exception {
        try {
            services.uploadTestCases(1, 1, "filename", null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>Tests DefaultUploadExternalServices#setSubmissionStatus(long,long,String) for failure. It tests the
     * case that when submissionId is negative and expects IllegalArgumentException.</p>
     *
     * @throws Exception to JUnit
     */
    public void testSetSubmissionStatus_NegativeSubmissionId()
        throws Exception {
        try {
            services.setSubmissionStatus(-1, 1, null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>Tests DefaultUploadExternalServices#setSubmissionStatus(long,long,String) for failure. It tests the
     * case that when submissionStatusId is negative and expects IllegalArgumentException.</p>
     *
     * @throws Exception to JUnit
     */
    public void testSetSubmissionStatus_NegativeSubmissionStatusId()
        throws Exception {
        try {
            services.setSubmissionStatus(1, -1, null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>Tests DefaultUploadExternalServices#setSubmissionStatus(long,long,String) for failure. It tests the
     * case that when operator is null and expects IllegalArgumentException.</p>
     *
     * @throws Exception to JUnit
     */
    public void testSetSubmissionStatus_NullOperator()
        throws Exception {
        try {
            services.setSubmissionStatus(1, 1, null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>Tests DefaultUploadExternalServices#setSubmissionStatus(long,long,String) for failure. It tests the
     * case that when operator is empty and expects IllegalArgumentException.</p>
     *
     * @throws Exception to JUnit
     */
    public void testSetSubmissionStatus_EmptyOperator()
        throws Exception {
        try {
            services.setSubmissionStatus(1, 1, " ");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>Tests DefaultUploadServices#uploadSpecification(long,long,String,DataHandler) for failure. It tests
     * the case that when projectId is negative and expects IllegalArgumentException.</p>
     *
     * @throws Exception to JUnit
     */
    public void testUploadSpecification_failure1() throws Exception {
        try {
            services.uploadSpecification(-1, 1, "test_file", dataHandler);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>Tests DefaultUploadServices#uploadSpecification(long,long,String,DataHandler) for failure. It tests
     * the case that when userId is negative and expects IllegalArgumentException.</p>
     *
     * @throws Exception to JUnit
     */
    public void testUploadSpecification_failure2() throws Exception {
        try {
            services.uploadSpecification(1, -1, "test_file", dataHandler);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>Tests DefaultUploadServices#uploadSpecification(long,long,String,DataHandler) for failure. It tests
     * the case that when filename is null and expects IllegalArgumentException.</p>
     *
     * @throws Exception to JUnit
     */
    public void testUploadSpecification_failure3() throws Exception {
        try {
            services.uploadSpecification(1, 1, null, dataHandler);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>Tests DefaultUploadServices#uploadSpecification(long,long,String,DataHandler) for failure. It tests
     * the case that when filename is empty and expects IllegalArgumentException.</p>
     *
     * @throws Exception to JUnit
     */
    public void testUploadSpecification_failure4() throws Exception {
        try {
            services.uploadSpecification(1, 1, " ", dataHandler);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>Tests DefaultUploadServices#uploadSpecification(long,long,String,DataHandler) for failure. It tests
     * the case that when dataHandler is empty and expects IllegalArgumentException.</p>
     *
     * @throws Exception to JUnit
     */
    public void testUploadSpecification_failure5() throws Exception {
        try {
            services.uploadSpecification(1, 1, "testfile", null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

}
