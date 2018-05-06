/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.scoreaggregator.failuretests;

import junit.framework.TestCase;

import com.topcoder.management.review.scoreaggregator.AggregatedSubmission;
import com.topcoder.management.review.scoreaggregator.InconsistentDataException;
import com.topcoder.management.review.scoreaggregator.TieBreaker;
import com.topcoder.management.review.scoreaggregator.impl.StandardTieBreaker;


/**
 * <p>
 * Failure tests for <tt>StandardTieBreaker</tt>.
 * </p>
 *
 * @author GavinWang
 * @version 1.0
 */
public class StandardTieBreakerFailureTests extends TestCase {
    /** An instance of StandardTieBreaker for testing. */
    private TieBreaker breaker;

    /**
     * <p>
     * Set up each test.
     * </p>
     *
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        this.breaker = new StandardTieBreaker();
    }

    /**
     * Failure test for StandardTieBreaker(). Inputs: nothing. Expectation: should be ok.
     */
    public void testStandardTieBreaker() {
        assertNotNull("Unable to instantiate StandardTieBreaker.", breaker);
    }

    /**
     * Failure test for breakTies(AggregatedSubmission[]). Inputs: null submissions array. Expectation:
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testBreakTiesNullSubmissions() throws Exception {
        try {
            this.breaker.breakTies(null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Failure test for breakTies(AggregatedSubmission[]). Inputs: submissions array containing null entries.
     * Expectation: IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testBreakTiesNullSubmission() throws Exception {
        try {
            this.breaker.breakTies(new AggregatedSubmission[] {
                new AggregatedSubmission(1L, new float[] {94.5f}, 94.5f), null
            });
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Failure test for breakTies(AggregatedSubmission[]). Inputs: submissions array containing inconsistent entries
     * whose scores are of different length. Expectation: InconsistentDataException should be thrown.
     */
    public void testBreakTiesInconsistentSubmissions() {
        try {
            this.breaker.breakTies(new AggregatedSubmission[] {
                new AggregatedSubmission(1L, new float[] {94.5f}, 94.5f),
                new AggregatedSubmission(2L, new float[] {94.0f, 95.0f}, 94.5f)
            });
            fail("InconsistentDataException should be thrown.");
        } catch (InconsistentDataException e) {
            // expected
        }
    }
}
