/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable;

import junit.framework.TestCase;

import java.util.Date;

/**
 * Unit test for Deliverable.
 *
 * @author singlewood
 * @version 1.1
 */
public class DeliverableTests extends TestCase {

    /**
     * The test Deliverable instance.
     */
    private Deliverable deliverable = null;

    /**
     * Create the test instance.
     *
     * @throws Exception exception to JUnit.
     */
    public void setUp() throws Exception {
        deliverable = new Deliverable(100, 200, 300, new Long(400), true);
    }

    /**
     * Clean the config.
     *
     * @throws Exception exception to JUnit.
     */
    public void tearDown() throws Exception {
        deliverable = null;
    }

    /**
     * Tests constructor1 with invalid parameters. The first argument will be set to 0. IllegalArgumentException should
     * be thrown.
     */
    public void testConstructor1_InvalidLong1() {
        try {
            new Deliverable(0, 1, 1, new Long(1), true);
            fail("IllegalArgumentException should be thrown because of the null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Tests constructor1 with invalid parameters. The first argument will be set to -1. IllegalArgumentException should
     * be thrown.
     */
    public void testConstructor1_InvalidLong2() {
        try {
            new Deliverable(-1, 1, 1, new Long(1), true);
            fail("IllegalArgumentException should be thrown because of invalid parameters.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Tests constructor1 with invalid parameters. The second argument will be set to 0. IllegalArgumentException should
     * be thrown.
     */
    public void testConstructor1_InvalidLong3() {
        try {
            new Deliverable(1, 0, 1, new Long(1), true);
            fail("IllegalArgumentException should be thrown because of invalid parameters.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Tests constructor1 with invalid parameters. The second argument will be set to -1.
     * IllegalArgumentException should be thrown.
     */
    public void testConstructor1_InvalidLong4() {
        try {
            new Deliverable(1, -1, 1, new Long(1), true);
            fail("IllegalArgumentException should be thrown because of invalid parameters.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Tests constructor1 with invalid parameters. The third argument will be set to 0. IllegalArgumentException should
     * be thrown.
     */
    public void testConstructor1_InvalidLong5() {
        try {
            new Deliverable(1, 1, 1, new Long(0), true);
            fail("IllegalArgumentException should be thrown because of invalid parameters.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Tests constructor1 with invalid parameters. The third argument will be set to -1. IllegalArgumentException should
     * be thrown.
     */
    public void testConstructor1_InvalidLong6() {
        try {
            new Deliverable(1, 1, 1, new Long(-1), true);
            fail("IllegalArgumentException should be thrown because of invalid parameters.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Tests constructor1 with invalid parameters. The fourth argument will be set to 0.
     * IllegalArgumentException should be thrown.
     */
    public void testConstructor1_InvalidLong7() {
        try {
            new Deliverable(1, 1, 0, new Long(1), true);
            fail("IllegalArgumentException should be thrown because of invalid parameters.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Tests constructor1 with invalid parameters. The fourth argument will be set to -1.
     * IllegalArgumentException should be thrown.
     */
    public void testConstructor1_InvalidLong8() {
        try {
            new Deliverable(1, 1, -1, new Long(1), true);
            fail("IllegalArgumentException should be thrown because of invalid parameters.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Tests constructor1 with valid parameter. No exception should be thrown.
     */
    public void testConstructor1_Valid1() {
        try {
            new Deliverable(1, 1, 1, new Long(1), true);
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException should not be thrown with valid parameter.");
        }
    }

    /**
     * Tests constructor1 with valid parameter. No exception should be thrown.
     */
    public void testConstructor1_Valid2() {
        try {
            new Deliverable(1222, 1222, 12222, null, false);
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException should not be thrown with valid parameter.");
        }
    }

    /**
     * Tests constructor1 with valid parameter. Check if the constructor set all the private fields properly. No
     * exception should be thrown.
     */
    public void testConstructor1_Accuracy1() {
        deliverable = new Deliverable(1, 2, 3, new Long(4), true);
        assertEquals("project is not set properly.", 1, deliverable.getProject());
        assertEquals("phase is not set properly.", 2, deliverable.getPhase());
        assertEquals("resource is not set properly.", 3, deliverable.getResource());
        assertEquals("submission is not set properly.", 4, deliverable.getSubmission().longValue());
        assertEquals("required is not set properly.", true, deliverable.isRequired());
    }

    /**
     * Tests constructor1 with valid parameter. Check if the constructor set all the private fields properly. No
     * exception should be thrown.
     */
    public void testConstructor1_Accuracy2() {
        deliverable = new Deliverable(2, 3, 4, new Long(5), false);
        assertEquals("project is not set properly.", 2, deliverable.getProject());
        assertEquals("phase is not set properly.", 3, deliverable.getPhase());
        assertEquals("resource is not set properly.", 4, deliverable.getResource());
        assertEquals("submission is not set properly.", 5, deliverable.getSubmission().longValue());
        assertEquals("required is not set properly.", false, deliverable.isRequired());
    }

    /**
     * Tests the behavior of getProject. Set the project field in the constructor, see if the getProject method can get
     * the correct value. No exception should be thrown.
     */
    public void testGetProject_Accuracy1() {
        deliverable = new Deliverable(1, 2, 3, new Long(4), true);
        assertEquals("project is not set properly.", 1, deliverable.getProject());
    }

    /**
     * Tests the behavior of getProject. Set the project field in the constructor, see if the getProject method can get
     * the correct value. No exception should be thrown.
     */
    public void testGetProject_Accuracy2() {
        deliverable = new Deliverable(100, 2, 3, new Long(4), true);
        assertEquals("project is not set properly.", 100, deliverable.getProject());
    }

    /**
     * Tests the behavior of getPhase. Set the phase field in the constructor, see if the getPhase method can get the
     * correct value. No exception should be thrown.
     */
    public void testGetPhase_Accuracy1() {
        deliverable = new Deliverable(1, 2, 3, new Long(4), true);
        assertEquals("phase is not set properly.", 2, deliverable.getPhase());
    }

    /**
     * Tests the behavior of getPhase. Set the phase field in the constructor, see if the getPhase method can get the
     * correct value. No exception should be thrown.
     */
    public void testGetPhase_Accuracy2() {
        deliverable = new Deliverable(100, 200, 3, new Long(4), true);
        assertEquals("phase is not set properly.", 200, deliverable.getPhase());
    }

    /**
     * Tests the behavior of getResource. Set the resource field in the constructor, see if the getResource method can
     * get the correct value. No exception should be thrown.
     */
    public void testGetResource_Accuracy1() {
        deliverable = new Deliverable(1, 2, 3, new Long(4), true);
        assertEquals("resource is not set properly.", 3, deliverable.getResource());
    }

    /**
     * Tests the behavior of getResource. Set the resource field in the constructor, see if the getResource method can
     * get the correct value. No exception should be thrown.
     */
    public void testGetResource_Accuracy2() {
        deliverable = new Deliverable(100, 200, 300, new Long(4), true);
        assertEquals("resource is not set properly.", 300, deliverable.getResource());
    }

    /**
     * Tests the behavior of getSubmission. Set the submission field in the constructor, see if the getSubmission method
     * can get the correct value. No exception should be thrown.
     */
    public void testGetSubmission_Accuracy1() {
        deliverable = new Deliverable(1, 2, 3, new Long(4), true);
        assertEquals("submission is not set properly.", 4, deliverable.getSubmission().longValue());
    }

    /**
     * Tests the behavior of getSubmission. Set the submission field in the constructor, see if the getSubmission method
     * can get the correct value. No exception should be thrown.
     */
    public void testGetSubmission_Accuracy2() {
        deliverable = new Deliverable(100, 200, 300, new Long(400), true);
        assertEquals("submission is not set properly.", 400, deliverable.getSubmission().longValue());
    }

    /**
     * Tests the behavior of IsRequired. Set the isRequired field in the constructor, see if the IsRequired method can
     * get the correct value. No exception should be thrown.
     */
    public void testIsRequired_Accuracy1() {
        deliverable = new Deliverable(1, 2, 3, new Long(4), true);
        assertEquals("required is not set properly.", true, deliverable.isRequired());
    }

    /**
     * Tests the behavior of IsRequired. Set the isRequired field in the constructor, see if the IsRequired method can
     * get the correct value. No exception should be thrown.
     */
    public void testIsRequired_Accuracy2() {
        deliverable = new Deliverable(100, 200, 300, new Long(400), false);
        assertEquals("required is not set properly.", false, deliverable.isRequired());
    }

    /**
     * Tests setCompletionDate with null parameter. IllegalArgumentException should be thrown.
     */
    public void testSetCompletionDate_Null() {
        try {
            deliverable.setCompletionDate(null);
            fail("IllegalArgumentException should be thrown because of the null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Tests the behavior of setCompletionDate. Set the completionDate field, see if the getCompletionDate
     * method can get the correct value. No exception should be thrown.
     */
    public void testSetCompletionDate_Accuracy1() {
        Date date = new Date();
        deliverable.setCompletionDate(date);
        assertEquals("completionDate is not set properly.", date, deliverable.getCompletionDate());
    }

    /**
     * Tests the behavior of GetCompletionDate. Set the completionDate field, see if the getCompletionDate method
     * can get the correct value. No exception should be thrown.
     */
    public void testGetCompletionDate_Accuracy2() {
        Date date = new Date();
        deliverable.setCompletionDate(date);
        assertEquals("getCompletionDate doesn't work properly.", date, deliverable.getCompletionDate());
    }

    /**
     * Tests the behavior of isComplete. Set the completionDate field, see if the isComplete method can get a true. No
     * exception should be thrown.
     */
    public void testIsComplete_Accuracy1() {
        Date date = new Date();
        deliverable.setCompletionDate(date);
        assertEquals("isComplete doesn't work properly.", true, deliverable.isComplete());
    }

    /**
     * Tests the behavior of isComplete. Left completionDate field with a null value, see if the isComplete method can
     * get a false. No exception should be thrown.
     */
    public void testIsComplete_Accuracy2() {
        assertEquals("isComplete doesn't work properly.", false, deliverable.isComplete());
    }

    /**
     * Tests the behavior of isPerSubmission. Set the submission field, see if the isPerSubmission method can
     * get a true. No exception should be thrown.
     */
    public void testIsPerSubmission_Accuracy1() {
        assertEquals("isPerSubmission doesn't work properly.", true, deliverable.isPerSubmission());
    }

    /**
     * Tests the behavior of isPerSubmission. Left submission field with a null value, see if the isPerSubmission method
     * can get a false. No exception should be thrown.
     */
    public void testIsPerSubmission_Accuracy2() {
        deliverable = new Deliverable(1, 1, 1, null, true);
        assertEquals("isPerSubmission doesn't work properly.", false, deliverable.isPerSubmission());
    }
}
