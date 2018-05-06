/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.scoreaggregator.accuracytests;

import com.topcoder.management.review.scoreaggregator.impl.StandardTieDetector;

import junit.framework.TestCase;


/**
 * <p>
 * Accuracy tests of the StandardTieDetector.
 * </p>
 *
 * @author magicpig
 * @version 1.0
 */
public class StandardTieDetectorAccuracyTests extends TestCase {
    /**
     * Test of constructor :StandardTieDetector(float).
     */
    public void testCtor1() {
        assertNotNull("Unable to create StandardTieDetector.", new StandardTieDetector(1));
    }

    /**
     * Test of constructor :StandardTieDetector().
     */
    public void testCtor2() {
        assertNotNull("Unable to create StandardTieDetector.", new StandardTieDetector());
    }

    /**
     * Test of tied.
     */
    public void testTied1() {
        // they are tied
        assertTrue("Fails to invoke tied", new StandardTieDetector().tied(1.234f, 1.234f));
    }

    /**
     * Test of tied.
     */
    public void testTied2() {
        // There are tie because epsilon == 10 and Math.abs(a-b)<10
        assertTrue("Fails to invoke tied", new StandardTieDetector(10).tied(10.234f, 1.234f));
    }

    /**
     * Test of tied.
     */
    public void testTied3() {
        // There are tie because epsilon == 1 and  3 is between 1.9*(1-1)=0 and 1.9*(1+1)=3.8
        assertTrue("Fails to invoke tied", new StandardTieDetector(1).tied(3f, 1.9f));
    }

    /**
     * Test of tied.
     */
    public void testTied4() {
        // There are tie because epsilon == 1 and  3 is between 1.9*(1-1)=0 and 1.9*(1+1)=3.8
        assertTrue("Fails to invoke tied", new StandardTieDetector(1).tied(1.9f, 3f));
    }

    /**
     * Test of tied.
     */
    public void testTied5() {
        assertFalse("Fails to invoke tied", new StandardTieDetector(0.5f).tied(1f, 3f));
    }

    /**
     * Test of tied.
     */
    public void testTied6() {
        // There are tie because epsilon == 1 and  1 is between 3*(1-1)=0 and 3*(1+1)=6
        assertTrue("Fails to invoke tied", new StandardTieDetector(1).tied(1f, 3f));
    }

    /**
     * Test of tied.
     */
    public void testTied7() {
        // There are tie because epsilon == 1 and  1 is between 3*(1-1)=0 and 3*(1+1)=6
        assertTrue("Fails to invoke tied", new StandardTieDetector(1).tied(3f, 1f));
    }

    /**
     * Test of tied.
     */
    public void testTied8() {
        // There are tie because epsilon == 1 and  0 is between 3*(1-1)=0 and 3*(1+1)=6
        assertTrue("Fails to invoke tied", new StandardTieDetector(1).tied(3f, 0f));
    }

    /**
     * Test of tied.
     */
    public void testTied9() {
        // There are tie because epsilon == 1 and  0 is between 3*(1-1)=0 and 3*(1+1)=6
        assertTrue("Fails to invoke tied", new StandardTieDetector(1).tied(0f, 3f));
    }
}
