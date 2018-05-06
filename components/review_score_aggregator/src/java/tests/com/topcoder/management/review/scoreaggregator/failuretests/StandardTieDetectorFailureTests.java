/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.scoreaggregator.failuretests;

import junit.framework.TestCase;

import com.topcoder.management.review.scoreaggregator.TieDetector;
import com.topcoder.management.review.scoreaggregator.impl.StandardTieDetector;


/**
 * <p>
 * Failure tests for <tt>StandardTieDetector</tt>.
 * </p>
 *
 * @author GavinWang
 * @version 1.0
 */
public class StandardTieDetectorFailureTests extends TestCase {
    /** An epsilon value. */
    private static final float EPS = 0.0001f;

    /** An instance of StandardTieDetector for testing. */
    private TieDetector detector;

    /**
     * <p>
     * Set up each test.
     * </p>
     *
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        this.detector = new StandardTieDetector(EPS);
    }

    /**
     * Failure test for StandardTieDetector(float). Inputs: negative epsilon value. Expectation:
     * IllegalArgumentException should be thrown.
     */
    public void testStandardTieDetectorFloatNegative() {
        try {
            new StandardTieDetector(-0.005f);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Failure test for StandardTieDetector(). Inputs: nothing. Expectation: should be ok.
     */
    public void testStandardTieDetector() {
        assertNotNull("Unable to instantiate StandardTieDetector.", new StandardTieDetector());
    }

    /**
     * Failure test for tied(float, float). Inputs: negative first score. Expectation: IllegalArgumentException should
     * be thrown.
     */
    public void testTiedNegativeFirst() {
        try {
            this.detector.tied(-95.5f, 95.5f);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Failure test for tied(float, float). Inputs: negative second score. Expectation: IllegalArgumentException should
     * be thrown.
     */
    public void testTiedNegativeSecond() {
        try {
            this.detector.tied(95.5f, -95.5f);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Failure test for tied(float, float). Inputs: negative infinitive first score. Expectation:
     * IllegalArgumentException should be thrown.
     */
    public void testTiedNegativeInfinitiveFirst() {
        try {
            this.detector.tied(Float.NEGATIVE_INFINITY, 95.0f);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Failure test for tied(float, float). Inputs: positive infinitive first score. Expectation:
     * IllegalArgumentException should be thrown.
     */
    public void testTiedPositiveInfinitiveFirst() {
        try {
            this.detector.tied(Float.POSITIVE_INFINITY, 95.0f);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Failure test for tied(float, float). Inputs: negative infinitive second score. Expectation:
     * IllegalArgumentException should be thrown.
     */
    public void testTiedNegativeInfinitiveSecond() {
        try {
            this.detector.tied(95.5f, Float.NEGATIVE_INFINITY);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Failure test for tied(float, float). Inputs: positive infinitive second score. Expectation:
     * IllegalArgumentException should be thrown.
     */
    public void testTiedPositiveInfinitiveSecond() {
        try {
            this.detector.tied(95.5f, Float.POSITIVE_INFINITY);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Failure test for tied(float, float). Inputs: NaN first score. Expectation: IllegalArgumentException should be
     * thrown.
     */
    public void testTiedNaNFirst() {
        try {
            this.detector.tied(Float.NaN, 95.5f);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Failure test for tied(float, float). Inputs: NaN second score. Expectation: IllegalArgumentException should be
     * thrown.
     */
    public void testTiedNaNSecond() {
        try {
            this.detector.tied(95.5f, Float.NaN);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}
