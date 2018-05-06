/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.scoreaggregator.impl;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * This test case aggregates all test cases for <code>StandardTieDetector</code>.
 * </p>
 *
 * @author daiwb
 * @version 1.0
 */
public class StandardTieDetectorTest extends TestCase {
    /**
     * A <code>StandardTieDetector</code> instance used for tests.
     */
    private StandardTieDetector detector = null;

    /**
     * Returns the test suite of this test case.
     *
     * @return the test suite of this test case.
     */
    public static Test suite() {
        return new TestSuite(StandardTieDetectorTest.class);
    }

    /**
     * Set up.
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        detector = new StandardTieDetector(0.00000001f);
    }

    /**
     * Tear down.
     * @throws Exception to JUnit
     */
    protected void tearDown() throws Exception {
        detector = null;
    }

    /**
     * Test constructor <code>StandardTieDetector(float)</code>.
     */
    public void testStandardTieDetectorFloat_Accuracy() {
        assertNotNull("The instance should be created successfully.", detector);
    }

    /**
     * Test constructor <code>StandardTieDetector(float)</code> with negative <code>epsilon</code>.
     * <code>IllegalArgumentException</code> is expected.
     */
    public void testStandardTieDetectorFloat_NegativeEpsilon() {
        try {
            new StandardTieDetector(-0.00000001f);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor <code>StandardTieDetector(float)</code> with invalid <code>epsilon</code>.
     * <code>IllegalArgumentException</code> is expected.
     */
    public void testStandardTieDetectorFloat_InvalidEpsilon_1() {
        try {
            new StandardTieDetector(Float.NaN);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor <code>StandardTieDetector(float)</code> with invalid <code>epsilon</code>.
     * <code>IllegalArgumentException</code> is expected.
     */
    public void testStandardTieDetectorFloat_InvalidEpsilon_2() {
        try {
            new StandardTieDetector(Float.NEGATIVE_INFINITY);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>StandardTieDetector()</code>.
     */
    public void testStandardTieDetector_Accuracy() {
        detector = new StandardTieDetector();
        assertNotNull("The instance should be created successfully.", detector);
    }

    /**
     * Test method <code>tied(float, float)</code>.
     */
    public void testTied_Accuracy_1() {
        assertTrue("Tie result mismatches.", detector.tied(0.00000001f, 0.00000001f));
    }

    /**
     * Test method <code>tied(float, float)</code>.
     */
    public void testTied_Accuracy_2() {
        assertTrue("Tie result mismatches.", detector.tied(0.00000001f, 0.00000002f));
    }

    /**
     * Test method <code>tied(float, float)</code>.
     */
    public void testTied_Accuracy_3() {
        assertTrue("Tie result mismatches.", detector.tied(0.00000001f, 0.000000005f));
    }

    /**
     * Test method <code>tied(float, float)</code>.
     */
    public void testTied_Accuracy_4() {
        detector = new StandardTieDetector();
        assertTrue("Tie result mismatches.", detector.tied(0.00000001f, 0.00000001f));
    }

    /**
     * Test method <code>tied(float, float)</code>.
     */
    public void testTied_Accuracy_5() {
        detector = new StandardTieDetector();
        assertTrue("Tie result mismatches.", detector.tied(0.000000001f, 0.000000001f));
    }

    /**
     * Test method <code>tied(float, float)</code>.
     * Left value is negative.
     * <code>IllegalArgumentException</code> is expected.
     */
    public void testTied_InvalidArg_1() {
        try {
            detector.tied(-0.1f, 0.1f);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>tied(float, float)</code>.
     * Right value is negative.
     * <code>IllegalArgumentException</code> is expected.
     */
    public void testTied_InvalidArg_2() {
        try {
            detector.tied(0.1f, -0.1f);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>tied(float, float)</code>.
     * Left value is NaN.
     * <code>IllegalArgumentException</code> is expected.
     */
    public void testTied_InvalidArg_3() {
        try {
            detector.tied(Float.NaN, 0.1f);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>tied(float, float)</code>.
     * Right value is NaN.
     * <code>IllegalArgumentException</code> is expected.
     */
    public void testTied_InvalidArg_4() {
        try {
            detector.tied(0.1f, Float.NaN);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>tied(float, float)</code>.
     * Left value is POSITIVE_INFINITY.
     * <code>IllegalArgumentException</code> is expected.
     */
    public void testTied_InvalidArg_5() {
        try {
            detector.tied(Float.POSITIVE_INFINITY, 0.1f);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>tied(float, float)</code>.
     * Right value is POSITIVE_INFINITY.
     * <code>IllegalArgumentException</code> is expected.
     */
    public void testTied_InvalidArg_6() {
        try {
            detector.tied(0.1f, Float.POSITIVE_INFINITY);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>tied(float, float)</code>.
     * Left value is NEGATIVE_INFINITY.
     * <code>IllegalArgumentException</code> is expected.
     */
    public void testTied_InvalidArg_7() {
        try {
            detector.tied(Float.NEGATIVE_INFINITY, 0.1f);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>tied(float, float)</code>.
     * Right value is NEGATIVE_INFINITY.
     * <code>IllegalArgumentException</code> is expected.
     */
    public void testTied_InvalidArg_8() {
        try {
            detector.tied(0.1f, Float.NEGATIVE_INFINITY);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

}
