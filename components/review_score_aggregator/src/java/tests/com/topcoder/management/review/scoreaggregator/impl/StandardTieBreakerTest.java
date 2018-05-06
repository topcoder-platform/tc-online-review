/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.scoreaggregator.impl;

import com.topcoder.management.review.scoreaggregator.AggregatedSubmission;
import com.topcoder.management.review.scoreaggregator.InconsistentDataException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * This test case aggregates all test cases for <code>StandardTieBreaker</code>.
 * </p>
 *
 * @author daiwb
 * @version 1.0
 */
public class StandardTieBreakerTest extends TestCase {
    /**
     * A <code>StandardTieBreaker</code> instance used for tests.
     */
    private StandardTieBreaker breaker = null;

    /**
     * Returns the test suite of this test case.
     *
     * @return the test suite of this test case.
     */
    public static Test suite() {
        return new TestSuite(StandardTieBreakerTest.class);
    }

    /**
     * Set up.
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        breaker = new StandardTieBreaker();
    }

    /**
     * Tear down.
     * @throws Exception to JUnit
     */
    protected void tearDown() throws Exception {
        breaker = null;
    }

    /**
     * Test constructor <code>StandardTieBreaker()</code>.
     */
    public void testStandardTieBreaker_Accuracy() {
        assertNotNull("The instance should be created successfully.", breaker);
    }

    /**
     * Test method <code>breakTies(AggregatedSubmission[])</code>.
     * @throws Exception to JUnit
     */
    public void testBreakTies_Accuracy_1() throws Exception {
        AggregatedSubmission sub1 = new AggregatedSubmission(1, new float[] {10.0f, 90.0f}, 50.0f);
        AggregatedSubmission sub2 = new AggregatedSubmission(2, new float[] {11.0f, 89.0f}, 50.0f);
        int[] expected = new int[] {1, 1};
        int[] received = breaker.breakTies(new AggregatedSubmission[] {sub1, sub2});
        assertEquals("Received array not match expected one.", expected.length, received.length);
        for (int i = 0; i < expected.length; ++i) {
            assertEquals("Received array not match expected one.", expected[i], received[i]);
        }
    }

    /**
     * Test method <code>breakTies(AggregatedSubmission[])</code>.
     * @throws Exception to JUnit
     */
    public void testBreakTies_Accuracy_2() throws Exception {
        AggregatedSubmission sub1 = new AggregatedSubmission(1, new float[] {10.0f, 90.0f, 40.0f}, 50.0f);
        AggregatedSubmission sub2 = new AggregatedSubmission(2, new float[] {11.0f, 89.0f, 20.0f}, 50.0f);
        AggregatedSubmission sub3 = new AggregatedSubmission(3, new float[] {12.0f, 79.0f, 21.0f}, 50.0f);
        int[] expected = new int[] {1, 3, 2};
        int[] received = breaker.breakTies(new AggregatedSubmission[] {sub1, sub2, sub3});
        assertEquals("Received array not match expected one.", expected.length, received.length);
        for (int i = 0; i < expected.length; ++i) {
            assertEquals("Received array not match expected one.", expected[i], received[i]);
        }
    }

    /**
     * Test method <code>breakTies(AggregatedSubmission[])</code>.
     * @throws Exception to JUnit
     */
    public void testBreakTies_Accuracy_3() throws Exception {
        AggregatedSubmission sub1 = new AggregatedSubmission(1, new float[] {15.0f, 95.0f, 14.0f}, 50.0f);
        AggregatedSubmission sub2 = new AggregatedSubmission(2, new float[] {11.0f, 89.0f, 20.0f}, 50.0f);
        AggregatedSubmission sub3 = new AggregatedSubmission(3, new float[] {12.0f, 79.0f, 21.0f}, 50.0f);
        int[] expected = new int[] {1, 3, 2};
        int[] received = breaker.breakTies(new AggregatedSubmission[] {sub1, sub2, sub3});
        assertEquals("Received array not match expected one.", expected.length, received.length);
        for (int i = 0; i < expected.length; ++i) {
            assertEquals("Received array not match expected one.", expected[i], received[i]);
        }
    }

    /**
     * Test method <code>breakTies(AggregatedSubmission[])</code>.
     * @throws Exception to JUnit
     */
    public void testBreakTies_Accuracy_4() throws Exception {
        AggregatedSubmission sub1 = new AggregatedSubmission(1, new float[] {15.0f, 95.0f, 14.0f}, 50.0f);
        AggregatedSubmission sub2 = new AggregatedSubmission(2, new float[] {11.0f, 99.0f, 20.0f}, 50.0f);
        AggregatedSubmission sub3 = new AggregatedSubmission(3, new float[] {12.0f, 79.0f, 21.0f}, 50.0f);
        int[] expected = new int[] {1, 1, 1};
        int[] received = breaker.breakTies(new AggregatedSubmission[] {sub1, sub2, sub3});
        assertEquals("Received array not match expected one.", expected.length, received.length);
        for (int i = 0; i < expected.length; ++i) {
            assertEquals("Received array not match expected one.", expected[i], received[i]);
        }
    }

    /**
     * Test method <code>breakTies(AggregatedSubmission[])</code>.
     * @throws Exception to JUnit
     */
    public void testBreakTies_Accuracy_5() throws Exception {
        AggregatedSubmission sub1 = new AggregatedSubmission(1, new float[] {15.0f, 95.0f, 14.0f}, 50.0f);
        AggregatedSubmission sub2 = new AggregatedSubmission(2, new float[] {15.0f, 95.0f, 14.0f}, 50.0f);
        AggregatedSubmission sub3 = new AggregatedSubmission(3, new float[] {15.0f, 95.0f, 14.0f}, 50.0f);
        int[] expected = new int[] {1, 1, 1};
        int[] received = breaker.breakTies(new AggregatedSubmission[] {sub1, sub2, sub3});
        assertEquals("Received array not match expected one.", expected.length, received.length);
        for (int i = 0; i < expected.length; ++i) {
            assertEquals("Received array not match expected one.", expected[i], received[i]);
        }
    }

    /**
     * Test method <code>breakTies(AggregatedSubmission[])</code>.
     * @throws Exception to JUnit
     */
    public void testBreakTies_Accuracy_6() throws Exception {
        AggregatedSubmission sub1 = new AggregatedSubmission(1, new float[] {15.0f, 96.0f, 14.0f}, 50.0f);
        AggregatedSubmission sub2 = new AggregatedSubmission(2, new float[] {15.0f, 95.0f, 13.0f}, 50.0f);
        AggregatedSubmission sub3 = new AggregatedSubmission(3, new float[] {15.0f, 95.0f, 14.0f}, 50.0f);
        int[] expected = new int[] {1, 3, 2};
        int[] received = breaker.breakTies(new AggregatedSubmission[] {sub1, sub2, sub3});
        assertEquals("Received array not match expected one.", expected.length, received.length);
        for (int i = 0; i < expected.length; ++i) {
            assertEquals("Received array not match expected one.", expected[i], received[i]);
        }
    }

    /**
     * Test method <code>breakTies(AggregatedSubmission[])</code>.
     * @throws Exception to JUnit
     */
    public void testBreakTies_Accuracy_7() throws Exception {
        AggregatedSubmission sub1 = new AggregatedSubmission(1, new float[] {15.0f, 96.0f, 14.0f}, 50.0f);
        AggregatedSubmission sub2 = new AggregatedSubmission(2, new float[] {15.0f, 95.0f, 15.0f}, 50.0f);
        AggregatedSubmission sub3 = new AggregatedSubmission(3, new float[] {14.0f, 93.0f, 12.0f}, 50.0f);
        int[] expected = new int[] {1, 1, 2};
        int[] received = breaker.breakTies(new AggregatedSubmission[] {sub1, sub2, sub3});
        assertEquals("Received array not match expected one.", expected.length, received.length);
        for (int i = 0; i < expected.length; ++i) {
            assertEquals("Received array not match expected one.", expected[i], received[i]);
        }
    }

    /**
     * Test method <code>breakTies(AggregatedSubmission[])</code>.
     * @throws Exception to JUnit
     */
    public void testBreakTies_Accuracy_8() throws Exception {
        AggregatedSubmission sub1 = new AggregatedSubmission(1, new float[] {15.0f, 92.0f, 14.0f}, 50.0f);
        AggregatedSubmission sub2 = new AggregatedSubmission(2, new float[] {15.0f, 95.0f, 11.0f}, 50.0f);
        AggregatedSubmission sub3 = new AggregatedSubmission(3, new float[] {14.0f, 95.0f, 14.0f}, 50.0f);
        int[] expected = new int[] {1, 1, 1};
        int[] received = breaker.breakTies(new AggregatedSubmission[] {sub1, sub2, sub3});
        assertEquals("Received array not match expected one.", expected.length, received.length);
        for (int i = 0; i < expected.length; ++i) {
            assertEquals("Received array not match expected one.", expected[i], received[i]);
        }
    }

    /**
     * Test method <code>breakTies(AggregatedSubmission[])</code> with null <code>submissions</code>.
     * <code>IllegalArgumentException</code> is expected.
     * @throws Exception to JUnit
     */
    public void testBreakTies_NullArg() throws Exception {
        try {
            breaker.breakTies(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>breakTies(AggregatedSubmission[])</code> with <code>submissions</code> containing null element.
     * <code>IllegalArgumentException</code> is expected.
     * @throws Exception to JUnit
     */
    public void testBreakTies_ArrayWithNullElement() throws Exception {
        try {
            breaker.breakTies(new AggregatedSubmission[] {null});
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>breakTies(AggregatedSubmission[])</code> with inconsistent <code>submissions</code>.
     * <code>InconsistentDataException</code> is expected.
     * @throws Exception to JUnit
     */
    public void testBreakTies_InconsistentArray() throws Exception {
        try {
            AggregatedSubmission sub1 = new AggregatedSubmission(1, new float[] {10.0f, 90.0f}, 50.0f);
            AggregatedSubmission sub2 = new AggregatedSubmission(2, new float[] {10.0f}, 10.0f);
            breaker.breakTies(new AggregatedSubmission[] {sub1, sub2});
            fail("InconsistentDataException is expected.");
        } catch (InconsistentDataException ide) {
            // Success
        }
    }

    /**
     * Test method <code>breakTies(AggregatedSubmission[])</code> with empty <code>submissions</code>.
     * @throws Exception to JUnit
     */
    public void testBreakTies_EmptyArray() throws Exception {
        int[] receiced = breaker.breakTies(new AggregatedSubmission[0]);
        assertEquals("Empty array should be returned.", 0, receiced.length);
    }

}
