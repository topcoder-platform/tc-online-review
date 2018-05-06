/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable;

import junit.framework.TestCase;

/**
 * <p>Unit test for Submission.</p>
 *
 * @author singlewood, TCSDEVELOPER
 * @version 1.1
 */
public class SubmissionTests extends TestCase {

    /**
     * The test Submission instance.
     */
    private Submission submission = null;

    /**
     * The test Upload instance.
     */
    private Upload upload = null;

    /**
     * The test SubmissionStatus instance.
     */
    private SubmissionStatus submissionStatus = null;

    /**
     * The test SubmissionType instance.
     */
    private SubmissionType submissionType;

    /**
     * Create the test instance.
     *
     * @throws Exception exception to JUnit.
     */
    public void setUp() throws Exception {
        upload = DeliverableTestHelper.getValidToPersistUpload();
        upload.setId(1);

        submissionStatus = DeliverableTestHelper.getValidToPersistSubmissionStatus();
        submissionStatus.setId(1);

        submissionType = DeliverableTestHelper.getValidToPersistSubmissionType();
        submissionType.setId(1);

        submission = DeliverableTestHelper.getValidToPersistSubmission();
    }

    /**
     * Clean the config.
     *
     * @throws Exception exception to JUnit.
     */
    public void tearDown() throws Exception {
        upload = null;
        submissionStatus = null;
        submission = null;
    }

    /**
     * The default constructor should set id to UNSET_ID. So check if id is set properly. No exception should be
     * thrown.
     */
    public void testConstructor1_Accuracy1() {
        assertEquals("the constructor doesn't set id properly", Submission.UNSET_ID,
                submission.getId());
    }

    /**
     * Tests constructor2 with invalid parameters. The argument will be set to 0. IllegalArgumentException should be
     * thrown.
     */
    public void testConstructor2_InvalidLong1() {
        try {
            new Submission(0);
            fail("IllegalArgumentException should be thrown because of the null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Tests constructor2 with invalid parameters. The argument will be set to -2. IllegalArgumentException should be
     * thrown.
     */
    public void testConstructor2_InvalidLong2() {
        try {
            new Submission(-2);
            fail("IllegalArgumentException should be thrown because of invalid parameters.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Tests constructor2 with valid parameter. Check if the constructor set the id fields properly. No exception should
     * be thrown.
     */
    public void testConstructor2_Accuracy1() {
        submission = new Submission(123);
        assertEquals("constructor doesn't work properly.", 123, submission.getId());
    }

    /**
     * Tests the behavior of setUpload. Set the upload field, see if the getUpload method can get the correct value. No
     * exception should be thrown.
     */
    public void testSetUpload_Accuracy1() {
        submission.setUpload(upload);
        assertEquals("upload is not set properly.", upload, submission.getUpload());
    }

    /**
     * Tests the behavior of getUpload. Set the upload field, see if the getUpload method can get the correct value. No
     * exception should be thrown.
     */
    public void testGetUpload_Accuracy1() {
        submission.setUpload(upload);
        assertEquals("getUpload doesn't work properly.", upload, submission.getUpload());
    }

    /**
     * Tests the behavior of setSubmissionStatus. Set the submissionStatus field, see if the getSubmissionStatus method
     * can get the correct value. No exception should be thrown.
     */
    public void testSetSubmissionStatus_Accuracy1() {
        submission.setSubmissionStatus(submissionStatus);
        assertEquals("submissionStatus is not set properly.", submissionStatus, submission
                .getSubmissionStatus());
    }

    /**
     * Tests the behavior of getSubmissionStatus. Set the submissionStatus field, see if the getSubmissionStatus method
     * can get the correct value. No exception should be thrown.
     */
    public void testGetSubmissionStatus_Accuracy1() {
        submission.setSubmissionStatus(submissionStatus);
        assertEquals("getSubmissionStatus doesn't work properly.", submissionStatus, submission
                .getSubmissionStatus());
    }

    /**
     * Tests the behavior of isValidToPersist. Set the upload field to null, see if the isValidToPersist returns false.
     * No exception should be thrown.
     */
    public void testIsValidToPersist_Accuracy1() {
        submission.setUpload(null);
        submission.setSubmissionStatus(submissionStatus);
        submission.setSubmissionType(submissionType);
        submission.setId(1);
        assertEquals("isValidToPersist doesn't work properly.", false, submission.isValidToPersist());
    }

    /**
     * Tests the behavior of isValidToPersist. Set the upload.isValidToPersist to false, see if the isValidToPersist
     * returns false. No exception should be thrown.
     */
    public void testIsValidToPersist_Accuracy2() {
        upload.setParameter(null);
        submission.setUpload(upload);
        submission.setSubmissionStatus(submissionStatus);
        submission.setSubmissionType(submissionType);
        submission.setId(1);
        assertEquals("isValidToPersist doesn't work properly.", false, submission.isValidToPersist());
    }

    /**
     * Tests the behavior of isValidToPersist. Do not set id field, see if the isValidToPersist returns false. No
     * exception should be thrown.
     */
    public void testIsValidToPersist_Accuracy3() {
        submission.setUpload(upload);
        submission.setSubmissionStatus(submissionStatus);
        submission.setSubmissionType(submissionType);
        assertEquals("isValidToPersist doesn't work properly.", false, submission.isValidToPersist());
    }

    /**
     * Tests the behavior of isValidToPersist. Set the submissionStatus field to null, see if the isValidToPersist
     * returns false. No exception should be thrown.
     */
    public void testIsValidToPersist_Accuracy4() {
        submission.setUpload(upload);
        submission.setSubmissionStatus(null);
        submission.setSubmissionType(submissionType);
        submission.setId(1);
        assertEquals("isValidToPersist doesn't work properly.", false, submission.isValidToPersist());
    }

    /**
     * Tests the behavior of isValidToPersist. Sets the submissionStatus.isValidToPersist field to false,
     * see if the isValidToPersist returns false. No exception should be thrown.
     */
    public void testIsValidToPersist_Accuracy5() {
        submissionStatus.setName(null);
        submission.setUpload(upload);
        submission.setSubmissionStatus(submissionStatus);
        submission.setSubmissionType(submissionType);
        submission.setId(1);
        assertEquals("isValidToPersist doesn't work properly.", false, submission.isValidToPersist());
    }

    /**
     * Tests the behavior of isValidToPersist. Set super.isValidToPersist() to false, see if the isValidToPersist
     * returns false. No exception should be thrown.
     */
    public void testIsValidToPersist_Accuracy6() {
        submission.setUpload(upload);
        submission.setSubmissionStatus(submissionStatus);
        submission.setSubmissionType(submissionType);
        submission.setId(1);
        submission.setCreationUser(null);
        assertEquals("isValidToPersist doesn't work properly.", false, submission.isValidToPersist());
    }

    /**
     * Tests the behavior of isValidToPersist. Set SubmissionType to null, see if the isValidToPersist returns
     * false. No exception should be thrown.
     */
    public void testIsValidToPersist_Accuracy7() {
        submission.setUpload(upload);
        submission.setSubmissionStatus(submissionStatus);
        submission.setSubmissionType(null);
        submission.setId(1);
        submission.setCreationUser(null);
        assertEquals("isValidToPersist doesn't work properly.", false, submission.isValidToPersist());
    }

    /**
     * Tests the behavior of isValidToPersist. Set submissionType.isValidToPersist() to false,
     * see if the isValidToPersist returns false. No exception should be thrown.
     */
    public void testIsValidToPersist_Accuracy8() {
        submissionType.setName(null);

        submission.setUpload(upload);
        submission.setSubmissionStatus(submissionStatus);
        submission.setSubmissionType(submissionType);
        submission.setId(1);
        submission.setCreationUser(null);
        assertEquals("isValidToPersist doesn't work properly.", false, submission.isValidToPersist());
    }

    /**
     * Tests the behavior of isValidToPersist. Set all the field with non-null values, see if the isValidToPersist
     * returns true. No exception should be thrown.
     */
    public void testIsValidToPersist_Accuracy9() {
        submission.setUpload(upload);
        submission.setSubmissionStatus(submissionStatus);
        submission.setSubmissionType(submissionType);
        submission.setId(1);
        assertEquals("isValidToPersist doesn't work properly.", true, submission.isValidToPersist());
    }

    /**
     * Tests the behavior of setSubmissionType. Set the submissionType field, see if the getSubmissionType method
     * will return the correct value. No exception should be thrown.
     */
    public void testSubmissionType_Accuracy() {
        submission.setSubmissionType(submissionType);
        assertEquals("submissionType is not set properly.", submissionType, submission
                .getSubmissionType());
    }

    /**
     * Tests the behavior of getScreeningScore. Set the upload field, see if the getScreeningScore method will return
     * the correct value. No exception should be thrown.
     */
    public void testScreeningScore_Accuracy() {
        Double screeningScore = new Double(123D);

        submission.setScreeningScore(screeningScore);
        assertEquals("getScreeningScore doesn't work properly.", screeningScore, submission.getScreeningScore());
    }

    /**
     * Tests the behavior of getInitialScore. Set the upload field, see if the getInitialScore method will return
     * the correct value. No exception should be thrown.
     */
    public void testInitialScore_Accuracy() {
        Double initialScore = new Double(123D);

        submission.setInitialScore(initialScore);
        assertEquals("getInitialScore doesn't work properly.", initialScore, submission.getInitialScore());
    }

    /**
     * Tests the behavior of getFinalScore. Set the upload field, see if the getFinalScore method will return
     * the correct value. No exception should be thrown.
     */
    public void testFinalScore_Accuracy() {
        Double finalScore = new Double(123D);

        submission.setFinalScore(finalScore);
        assertEquals("getFinalScore doesn't work properly.", finalScore, submission.getFinalScore());
    }


    /**
     * Tests the behavior of getPlacement. Set the upload field, see if the getPlacement method will return
     * the correct value. No exception should be thrown.
     */
    public void testPlacement_Accuracy() {
        Long placement = new Long(1L);

        submission.setPlacement(placement);
        assertEquals("getPlacement doesn't work properly.", placement, submission.getPlacement());
    }
}
