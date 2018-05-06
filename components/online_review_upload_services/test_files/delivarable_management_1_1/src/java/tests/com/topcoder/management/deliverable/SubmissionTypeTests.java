/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable;

import junit.framework.TestCase;

/**
 * <p>Unit test for submissionType.</p>
 *
 * @author TCSDEVELOPER
 * @version 1.1
 */
public class SubmissionTypeTests extends TestCase {

    /**
     * <p>The test submissionType instance.</p>
     */
    private SubmissionType submissionType = null;

    /**
     * <p>Create the test instance.</p>
     *
     * @throws Exception exception if any error occurs during test
     */
    public void setUp() throws Exception {
        submissionType = new SubmissionType();
    }

    /**
     * <p>Clean the config.</p>
     *
     * @throws Exception exception if any error occurs during test
     */
    public void tearDown() throws Exception {
        submissionType = null;
    }

    /**
     * <p>The default constructor should set id to UNSET_ID. So check if id is set properly. No exception should be
     * thrown.</p>
     */
    public void testConstructor1_Accuracy() {
        assertEquals("the constructor doesn't set id properly", submissionType.UNSET_ID,
                submissionType.getId());
    }

    /**
     * <p>The default constructor should set name to null. So check if name is set properly. No exception should be
     * thrown.</p>
     */
    public void testConstructor1_Accuracy2() {
        assertEquals("the constructor doesn't set name properly", null, submissionType.getName());
    }

    /**
     * <p>Test second constructor with invalid parameters. The argument will be set to 0.
     * IllegalArgumentException should be thrown.</p>
     */
    public void testConstructor2_InvalidLong1() {
        try {
            new NamedDeliverableStructureExtends(0);
            fail("IllegalArgumentException should be thrown because of the null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * <p>Test second constructor with invalid parameters. The argument will be set to -2.
     * IllegalArgumentException should be thrown.</p>
     */
    public void testConstructor2_InvalidLong2() {
        try {
            new NamedDeliverableStructureExtends(-2);
            fail("IllegalArgumentException should be thrown because of invalid parameters.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * <p>Test second constructor with valid parameter. Check if the constructor set the id fields properly.
     * No exception should be thrown.</p>
     */
    public void testConstructor2_Accuracy() {
        submissionType = new SubmissionType(123);
        assertEquals("constructor doesn't work properly.", 123, submissionType.getId());
    }

    /**
     * <p>Test third constructor with invalid parameters. The first argument will be set to 0.
     * IllegalArgumentException should be thrown.</p>
     */
    public void testConstructor3_InvalidLong1() {
        try {
            new SubmissionType(0, null);
            fail("IllegalArgumentException should be thrown because of the null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * <p>Test third constructor with invalid parameters. The first argument will be set to -3.
     * IllegalArgumentException should be thrown.</p>
     */
    public void testConstructor3_InvalidLong2() {
        try {
            new SubmissionType(-3, null);
            fail("IllegalArgumentException should be thrown because of invalid parameters.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * <p>Test third constructor with valid parameter. Check if the constructor set the name fields properly.
     * No exception should be thrown.</p>
     */
    public void testConstructor3_Accuracy() {
        submissionType = new SubmissionType(133, "name");
        assertEquals("constructor doesn't work properly.", "name", submissionType.getName());
    }
}
